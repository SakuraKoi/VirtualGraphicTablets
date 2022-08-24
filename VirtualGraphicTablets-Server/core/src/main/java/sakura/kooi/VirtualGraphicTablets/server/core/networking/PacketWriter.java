package sakura.kooi.VirtualGraphicTablets.server.core.networking;

import lombok.CustomLog;
import lombok.Getter;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

@CustomLog
public class PacketWriter extends Thread {
    private Socket socket;
    private DataOutputStream dos;

    @Getter
    protected LinkedBlockingQueue<Vgt.PacketContainer> sendQueue = new LinkedBlockingQueue<>();

    public PacketWriter(Socket socket, DataOutputStream dos) {
        this.socket = socket;
        this.dos = dos;
    }

    @Override
    public void run() {
        log.i("Client packet writer started");
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
            } catch (IOException e) {
                log.e("An error occurred while writing packet", e);
                try {
                    socket.close();
                } catch (IOException ignored) { }
                break;
            }
        }
        log.i("Client packet writer end");
    }
}
