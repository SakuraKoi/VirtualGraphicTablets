package sakura.kooi.virtualgraphictablets.network;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.virtualgraphictablets.TabletActivity;

public class ScreenRenderThread extends Thread {
    public LinkedBlockingQueue<Vgt.S03PacketScreen> renderQueue = new LinkedBlockingQueue<>();

    private TabletActivity parent;
    public ScreenRenderThread(TabletActivity tabletActivity) {
        this.parent = tabletActivity;
    }

    private int canvasWidth;
    private int canvasHeight;

    private int fps = 0;
    private long lastFpsTime = 0L;

    @Override
    public void run() {
        Log.i("VGT-PacketWriter", "Client packet writer started");
        while (!this.isInterrupted()) {
            Vgt.S03PacketScreen packet;
            try {
                packet = this.renderQueue.take();
            } catch (InterruptedException unused) {
                break;
            }

            long timing = System.currentTimeMillis();
            byte[] imageData = packet.getScreenImage().toByteArray();
            Bitmap image = parent.imageDiffDecoder.update(imageData);
            canvasWidth = packet.getWidth();
            canvasHeight = packet.getHeight();
            float scaleFactor = packet.getResizeFactor() / 100.0f;
            parent.convertRatio = (float)canvasWidth / packet.getImageWidth() * scaleFactor;

            parent.canvas.setContent(image, scaleFactor);
            parent.canvas.setFps(++fps);
            if (System.currentTimeMillis() > lastFpsTime) {
                lastFpsTime = System.currentTimeMillis() + 1000;
                fps = 0;
            }

            int renderQueueSize = renderQueue.size();
            long decodeTook = System.currentTimeMillis() - timing;
            Vgt.C08PacketDecodePerformanceReport resp = Vgt.C08PacketDecodePerformanceReport.newBuilder()
                    .setDecodeTook(decodeTook * (renderQueueSize+1))
                    .setTimestamp(packet.getTimestamp())
                    .setRenderQueueSize(renderQueueSize)
                    .build();
            Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                    .setPacketId(8)
                    .setPayload(resp.toByteString())
                    .build();
            parent.connectionThread.packetWriter.sendQueue.add(container);
        }
        Log.i("VGT-PacketWriter", "Client packet writer end");
    }
}
