package sakura.kooi.VirtualGraphicTablets.server.core.networking;

import com.sunnysidesoft.VirtualTablet.core.VTService.VTPenEvent;
import com.sunnysidesoft.VirtualTablet.core.VTService.VTServiceConstants;
import lombok.Cleanup;
import org.apache.thrift.transport.TSocket;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;

import java.util.concurrent.LinkedBlockingQueue;

public class UpstreamThread extends Thread {
    private VTabletServer parent;
    protected LinkedBlockingQueue<VTPenEvent> mQueue = new LinkedBlockingQueue<>();

    public UpstreamThread(VTabletServer parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            @Cleanup TSocket mTransport = new TSocket("127.0.0.1", VTServiceConstants.SERVER_PORT, 5000);

        } catch (Exception e) {
            parent.appendLog("§c");
        }
    }
}
