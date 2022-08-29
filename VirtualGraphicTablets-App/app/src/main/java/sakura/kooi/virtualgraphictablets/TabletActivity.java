package sakura.kooi.virtualgraphictablets;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_UP;
import static android.view.MotionEvent.TOOL_TYPE_STYLUS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.virtualgraphictablets.network.ConnectionThread;
import sakura.kooi.virtualgraphictablets.utils.ImageDiffDecoder;
import sakura.kooi.virtualgraphictablets.utils.TriConsumer;

public class TabletActivity extends AppCompatActivity {
    private String server;
    private int port;
    private ImageView btnBrush;
    private ImageView btnEraser;
    private ImageView btnSlice;
    private ImageView btnHand;

    private ConnectionThread connectionThread;

    @SuppressWarnings("deprecation")
    private ProgressDialog waitingDialog;

    private CanvasView canvas;
    private LinearLayout canvasContainer;
    private int canvasWidth;
    private int canvasHeight;

    private float convertRatio;

    private Vgt.HotkeyType currentBrush = Vgt.HotkeyType.TOOL_BRUSH;

    private ImageDiffDecoder imageDiffDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_tablet);
        if (this.getSupportActionBar() != null)
            this.getSupportActionBar().hide();

        canvas = findViewById(R.id.canvas);
        canvasContainer = findViewById(R.id.canvasContainer);
        btnBrush = findViewById(R.id.btnBrush);
        btnEraser = findViewById(R.id.btnEraser);
        btnSlice = findViewById(R.id.btnSlice);
        btnHand = findViewById(R.id.btnHand);

        btnBrush.setOnClickListener(e -> {
            sendTriggerHotkey(currentBrush = Vgt.HotkeyType.TOOL_BRUSH);
        });
        btnEraser.setOnClickListener(e -> {
            sendTriggerHotkey(currentBrush = Vgt.HotkeyType.TOOL_ERASER);
        });
        btnSlice.setOnClickListener(e -> {
            sendTriggerHotkey(currentBrush = Vgt.HotkeyType.TOOL_SLICE);
        });
        btnHand.setOnClickListener(e -> {
            sendTriggerHotkey(currentBrush = Vgt.HotkeyType.TOOL_HAND);
        });

        findViewById(R.id.btnUndo).setOnClickListener(e -> {
            sendTriggerHotkey(Vgt.HotkeyType.ACTION_UNDO);
        });
        findViewById(R.id.btnRedo).setOnClickListener(e -> {
            sendTriggerHotkey(Vgt.HotkeyType.ACTION_REDO);
        });
        findViewById(R.id.btnZoomIn).setOnClickListener(e -> {
            sendTriggerHotkey(Vgt.HotkeyType.ACTION_ZOOM_IN);
        });
        findViewById(R.id.btnZoomOut).setOnClickListener(e -> {
            sendTriggerHotkey(Vgt.HotkeyType.ACTION_ZOOM_OUT);
        });

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

    private void sendTriggerHotkey(Vgt.HotkeyType hotkey) {
        btnBrush.setBackgroundColor(currentBrush == Vgt.HotkeyType.TOOL_BRUSH ? Color.rgb(0x66, 0x99, 0x00) : Color.rgb(0x36, 0x36, 0x36));
        btnEraser.setBackgroundColor(currentBrush == Vgt.HotkeyType.TOOL_ERASER ? Color.rgb(0x66, 0x99, 0x00) : Color.rgb(0x36, 0x36, 0x36));
        btnSlice.setBackgroundColor(currentBrush == Vgt.HotkeyType.TOOL_SLICE ? Color.rgb(0x66, 0x99, 0x00) : Color.rgb(0x36, 0x36, 0x36));
        btnHand.setBackgroundColor(currentBrush == Vgt.HotkeyType.TOOL_HAND ? Color.rgb(0x66, 0x99, 0x00) : Color.rgb(0x36, 0x36, 0x36));

        Vgt.C07PacketTriggerHotkey resp = Vgt.C07PacketTriggerHotkey.newBuilder()
                .setKey(hotkey)
                .build();
        Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                .setPacketId(7)
                .setPayload(resp.toByteString())
                .build();
        connectionThread.packetWriter.sendQueue.add(container);
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
    protected void onStart() {
        super.onStart();
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
    protected void onStop() {
        super.onStop();
        if (connectionThread != null) {
            connectionThread.interrupt();
        }
        if (waitingDialog != null) {
            waitingDialog.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    public void onClientConnected() {
        canvas.post(() -> {
            waitingDialog.setMessage("Handshaking...");
            imageDiffDecoder = new ImageDiffDecoder(canvasContainer.getWidth(), canvasContainer.getHeight());
            Vgt.C01PacketHandshake resp = Vgt.C01PacketHandshake.newBuilder()
                    .setScreenWidth(canvasContainer.getWidth())
                    .setScreenHeight(canvasContainer.getHeight())
                    .build();
            Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                    .setPacketId(1)
                    .setPayload(resp.toByteString())
                    .build();
            connectionThread.packetWriter.sendQueue.add(container);
        });
    }

    public void onPacketReceived(Object pkt) {
        if (pkt instanceof Vgt.S02PacketServerInfo) {
            runOnUiThread(() -> {
                waitingDialog.dismiss();
            });
        } else if (pkt instanceof Vgt.S03PacketScreen) {
            Vgt.S03PacketScreen packet = (Vgt.S03PacketScreen) pkt;

            // TODO async decoding
            // TODO clear all current decode task if a full frame received
            byte[] imageData = packet.getScreenImage().toByteArray();
            long timing = System.currentTimeMillis();
            Bitmap image = imageDiffDecoder.update(imageData);
            long decodeTook = System.currentTimeMillis() - timing;
            // TODO send performance report to server, auto adjust fps
            canvasWidth = packet.getWidth();
            canvasHeight = packet.getHeight();
            convertRatio = canvasWidth / (float) packet.getImageWidth();

            runOnUiThread(() -> canvas.setContent(image));
        }
    }

    public void onDisconnected(boolean error) {
        if (error)
            return;
        runOnUiThread(() -> {
            if (!isFinishing() && !isDestroyed()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Server disconnected");
                dialog.show();
            }
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
            switch (motionEvent.getAction()) {
                case ACTION_DOWN:
                case ACTION_MOVE:
                    handleMotionEvent(motionEvent, (x, y, pressure) -> {
                        Vgt.C05PacketTouch pkt = Vgt.C05PacketTouch.newBuilder().setPosX(x).setPosY(y).setPressure(pressure).build();
                        Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder().setPacketId(5).setPayload(pkt.toByteString()).build();
                        connectionThread.packetWriter.sendQueue.add(container);
                    });
                    break;
                case ACTION_UP:
                case ACTION_OUTSIDE:
                    Vgt.C06PacketExit pkt = Vgt.C06PacketExit.newBuilder().build();
                    Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder().setPacketId(6).setPayload(pkt.toByteString()).build();
                    connectionThread.packetWriter.sendQueue.add(container);
                    break;
            }
            return true;
        });
        canvas.setOnHoverListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                Vgt.C06PacketExit pkt = Vgt.C06PacketExit.newBuilder().build();
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