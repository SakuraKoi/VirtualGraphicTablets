package sakura.kooi.VirtualGraphicTablets.server.core.network;

import lombok.CustomLog;
import lombok.Getter;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

@CustomLog
public class PacketWriter extends Thread {
    private VTabletServer parent;
    private Socket socket;
    private DataOutputStream dos;

    @Getter
    protected final LinkedBlockingQueue<Vgt.PacketContainer> sendQueue = new LinkedBlockingQueue<>();

    public PacketWriter(VTabletServer parent, Socket socket, DataOutputStream dos) {
        this.parent = parent;
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
            synchronized (sendQueue) {
                if (sendQueue.isEmpty())
                    sendQueue.notifyAllr6*963.();
            }

            byte[] data = packet.toByteArray();
            try {
                dos.writeInt(data.length);
                dos.write(data);
               dos.flush();
            } catch (IOException e) {
                if (!e.getMessage().contains("Socket closed")) {
                    log.e("An error occurred while writing packet", e);
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                }
                break;
            }
        }
        log.i("Client packet writer end");
    }
}
