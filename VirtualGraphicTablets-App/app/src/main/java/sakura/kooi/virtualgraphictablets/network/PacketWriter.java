package sakura.kooi.virtualgraphictablets.network;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;

public class PacketWriter extends Thread {
    private Socket socket;
    private DataOutputStream dos;

    public LinkedBlockingQueue<Vgt.PacketContainer> sendQueue = new LinkedBlockingQueue<>();

    public PacketWriter(Socket socket, DataOutputStream dos) {
        this.socket = socket;
        this.dos = dos;
    }

    @Override
    public void run() {
        Log.i("VGT-PacketWriter", "Client packet writer started");
        while (!this.isInterrupted()) {
            Vgt.PacketContainer packet;
            try {
                packet = this.sendQueue.take();
            } catch (InterruptedException unused) {
                break;
            }
            byte[] data = packet.toByteArray();
            try {
                dos.writeInt(data.length);
                dos.write(data);
                dos.flush();
            } catch (IOException e) {
                Log.e("VGT-PacketWriter", "An error occurred while writing packet", e);
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                break;
            }
        }
        Log.i("VGT-PacketWriter", "Client packet writer end");
    }
}
