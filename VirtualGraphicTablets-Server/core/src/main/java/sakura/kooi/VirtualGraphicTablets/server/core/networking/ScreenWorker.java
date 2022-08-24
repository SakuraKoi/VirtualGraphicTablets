package sakura.kooi.VirtualGraphicTablets.server.core.networking;

import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;

public class ScreenWorker extends Thread {
    private VTabletServer parent;
    private PacketWriter packetWriter;
    public ScreenWorker(VTabletServer parent, PacketWriter packetWriter) {
        this.parent = parent;
        this.packetWriter = packetWriter;
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            // TODO pos and size
            // TODO image capture
            // TODO preview
            // TODO add image into queue
            // TODO fps limit
        }
    }
}
