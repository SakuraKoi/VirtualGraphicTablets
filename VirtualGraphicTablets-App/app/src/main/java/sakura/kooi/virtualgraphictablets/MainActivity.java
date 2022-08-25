package sakura.kooi.virtualgraphictablets;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editServerAddress = findViewById(R.id.editServerAddress);
        EditText editServerPort = findViewById(R.id.editServerPort);
        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(e -> {
            String serverAddress = editServerAddress.getText().toString().trim();
            String serverPortStr = editServerPort.getText().toString().trim();
            if (serverAddress.isEmpty()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Error");
                dialog.setMessage("Server address is empty");
                dialog.show();
                return;
            }
            int serverPort;
            try {
                serverPort = Integer.parseInt(serverPortStr);
            } catch (NumberFormatException ex) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Error");
                dialog.setMessage("Invalid server port");
                dialog.show();
                return;
            }
            // TODO start tablet activity
        });
    }
}