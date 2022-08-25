package sakura.kooi.virtualgraphictablets;

import static android.view.MotionEvent.TOOL_TYPE_STYLUS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;

public class TabletActivity extends AppCompatActivity {
    private String server;
    private int port;

    private ConnectionThread connectionThread;

    @SuppressWarnings("deprecation")
    private ProgressDialog waitingDialog;

    private ImageView canvas;
    private int canvasWidth;
    private int canvasHeight;

    private float convertRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        canvas = findViewById(R.id.canvas);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            server = extras.getString("server", savedInstanceState == null ? null : savedInstanceState.getString("server"));
            port = extras.getInt("port", savedInstanceState == null ? 0 : savedInstanceState.getInt("port"));
        } else if (savedInstanceState != null) {
            server = savedInstanceState.getString("server");
            port = savedInstanceState.getInt("port");
        }
        initializeTablet();
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        if (z)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("server", server);
        outState.putInt("port", port);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();
        if (server == null || port == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Server address is null");
            dialog.setOnDismissListener(e -> TabletActivity.this.finish());
            dialog.show();
            return;
        }
        waitingDialog = new ProgressDialog(this);
        waitingDialog.setMessage("Connecting to " + server + ":" + port);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        connectionThread = new ConnectionThread(this, server, port);
        connectionThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (connectionThread != null) {
            connectionThread.interrupt();
        }
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    public void onClientConnected() {
        runOnUiThread(() -> {
            waitingDialog.setMessage("Handshaking...");
        });

        Vgt.C01PacketHandshake resp = Vgt.C01PacketHandshake.newBuilder()
                .setScreenWidth(canvas.getWidth())
                .setScreenHeight(canvas.getHeight())
                .build();
        Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                .setPacketId(1)
                .setPayload(resp.toByteString())
                .build();
        connectionThread.packetWriter.sendQueue.add(container);
    }

    public void onPacketReceived(Object pkt) {
        if (pkt instanceof Vgt.S02PacketServerInfo) {
            runOnUiThread(() -> {
                waitingDialog.dismiss();
            });
        } else if (pkt instanceof Vgt.S03PacketScreen) {
            Vgt.S03PacketScreen packet = (Vgt.S03PacketScreen) pkt;
            canvasWidth = packet.getWidth();
            canvasHeight = packet.getHeight();

            byte[] imageData = packet.toByteArray();
            Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            convertRatio = canvasWidth / (float) image.getWidth();
            canvas.setImageBitmap(image);
        }
    }

    public void onDisconnected(boolean error) {
        if (error)
            return;
        runOnUiThread(() -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Server disconnected");
            dialog.setOnDismissListener(e -> TabletActivity.this.finish());
            dialog.show();
        });
    }

    public void onNetworkError(IOException e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        runOnUiThread(() -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Error occurred");
            dialog.setMessage(sw.toString());
            dialog.setOnDismissListener(ex -> TabletActivity.this.finish());
            dialog.show();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeTablet() {
        canvas.setOnTouchListener((view, motionEvent) -> {
            handleMotionEvent(motionEvent, (x, y, pressure) -> {
                Vgt.C05PacketTouch pkt = Vgt.C05PacketTouch.newBuilder().setPosX(x).setPosY(y).setPressure(pressure).build();
                Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder().setPacketId(5).setPayload(pkt.toByteString()).build();
                connectionThread.packetWriter.sendQueue.add(container);
            });
            return true;
        });
        canvas.setOnHoverListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                Vgt.C06PacketExit pkt = Vgt.C06PacketExit.newBuilder().setPosX(0).setPosY(0).setPressure(0f).build();
                Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder().setPacketId(6).setPayload(pkt.toByteString()).build();
                connectionThread.packetWriter.sendQueue.add(container);
            } else {
                handleMotionEvent(motionEvent, (x, y, pressure) -> {
                    Vgt.C04PacketHover pkt = Vgt.C04PacketHover.newBuilder().setPosX(x).setPosY(y).build();
                    Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder().setPacketId(4).setPayload(pkt.toByteString()).build();
                    connectionThread.packetWriter.sendQueue.add(container);
                });
            }
            return true;
        });
    }

    private void handleMotionEvent(MotionEvent motionEvent, TriConsumer<Integer, Integer, Float> callback) {
        int historySize = motionEvent.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            callback.apply((int) (motionEvent.getHistoricalX(i) * convertRatio),
                    (int) (motionEvent.getHistoricalY(i) * convertRatio),
                    motionEvent.getToolType(0) == TOOL_TYPE_STYLUS ? motionEvent.getHistoricalPressure(i) : 1);
        }
        callback.apply((int) (motionEvent.getX(0) * convertRatio),
                (int) (motionEvent.getY(0) * convertRatio),
                motionEvent.getToolType(0) == TOOL_TYPE_STYLUS ? motionEvent.getPressure(0) : 1);
    }
}