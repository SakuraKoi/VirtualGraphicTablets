package sakura.kooi.virtualgraphictablets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        canvas = findViewById(R.id.canvas);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            server = extras.getString("server", savedInstanceState.getString("server"));
            port = extras.getInt("port", savedInstanceState.getInt("port"));
        } else {
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
        waitingDialog.setMessage("Handshaking...");
    }

    public void onPacketReceived(Object pkt) {
        if (pkt instanceof Vgt.S02PacketServerInfo) {
            Vgt.C01PacketHandshake resp = Vgt.C01PacketHandshake.newBuilder()
                    .setScreenWidth(canvas.getWidth())
                    .setScreenHeight(canvas.getHeight())
                    .build();
            Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                    .setPacketId(1)
                    .setPayload(resp.toByteString())
                    .build();
            connectionThread.packetWriter.sendQueue.add(container);
            waitingDialog.dismiss();
        } else if (pkt instanceof Vgt.S03PacketScreen) {
            Vgt.S03PacketScreen packet = (Vgt.S03PacketScreen) pkt;
            canvasWidth = packet.getWidth();
            canvasHeight = packet.getHeight();

            byte[] image = packet.toByteArray();
            canvas.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
    }

    public void onDisconnected(boolean error) {
        if (error)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Server disconnected");
        dialog.setOnDismissListener(e -> TabletActivity.this.finish());
        dialog.show();
    }

    public void onNetworkError(IOException e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Error occurred");
        dialog.setMessage(sw.toString());
        dialog.setOnDismissListener(ex -> TabletActivity.this.finish());
        dialog.show();
    }
    
    private void initializeTablet() {
    }
}