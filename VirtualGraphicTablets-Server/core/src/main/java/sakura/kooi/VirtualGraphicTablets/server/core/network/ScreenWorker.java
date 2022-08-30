package sakura.kooi.VirtualGraphicTablets.server.core.network;

import com.google.protobuf.ByteString;
import lombok.CustomLog;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;
import sakura.kooi.VirtualGraphicTablets.server.core.utils.ImageDiffEncoder;
import sakura.kooi.VirtualGraphicTablets.server.core.utils.JnaUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

@CustomLog
public class ScreenWorker extends Thread {
    private VTabletServer parent;
    private PacketWriter packetWriter;
    private ImageDiffEncoder imageDiffEncoder;

    public ScreenWorker(VTabletServer parent, PacketWriter packetWriter) {
        this.parent = parent;
        this.packetWriter = packetWriter;
    }

    @Override
    public void run() {
        //ImageIO.setUseCache(false);
        imageDiffEncoder = new ImageDiffEncoder(parent.tabletWidth, parent.tabletHeight);
        log.i("Screen stream started...");
        while (!isInterrupted()) {
            int posX = (int) parent.numCanvaPosX.getValue();
            int posY = (int) parent.numCanvaPosY.getValue();
            int width = (int) parent.numCanvaWidth.getValue();
            int height = (int) parent.numCanvaHeight.getValue();

            long start = System.currentTimeMillis();
            sendPacket(posX, posY, width, height);
            long took = System.currentTimeMillis() - start;
            parent.lblEncodingDelay.setText(String.valueOf(took));

            try {
                long waitBefore = System.currentTimeMillis();
                synchronized (packetWriter.sendQueue) {
                    while (!packetWriter.sendQueue.isEmpty())
                        packetWriter.sendQueue.wait();
                }
                long waitTook = System.currentTimeMillis() - waitBefore;

                long wait = Math.max(parent.clientDecodeTime - took, 1000 / (int) parent.numFps.getValue());
                wait = Math.max(wait - waitTook, 0);
                Thread.sleep(wait);
            } catch (InterruptedException e) {
                break;
            }
        }
        parent.removePreview();
        log.i("Screen stream end");
    }

    private AtomicInteger frameCounter = new AtomicInteger();

    private void sendPacket(int posX, int posY, int width, int height) {
        Rectangle capture = new Rectangle(-posX, -posY, width, height);
        BufferedImage originalImage = JnaUtils.bitbltRegion(capture);

        parent.canvas.setIcon(new ImageIcon(
                scaleToFit(originalImage,
                        parent.canvas.getWidth(),
                        parent.canvas.getHeight(),
                        Image.SCALE_FAST)
        ));

        int resizeFactor = (int) parent.numResizeFactor.getValue();
        Image sendToClient = scaleToFit(originalImage, parent.tabletWidth * resizeFactor / 100 , parent.tabletHeight * resizeFactor / 100, Image.SCALE_SMOOTH);
        BufferedImage transcodeImage = toBufferedImage(sendToClient);
        boolean forceFullFrame = frameCounter.getAndUpdate(count -> count > 300 ? 0 : count + 1) > 300;
        byte[] imageData = imageDiffEncoder.encode(transcodeImage, forceFullFrame);
        if (imageData == null) return;

        Vgt.S03PacketScreen packetScreen = Vgt.S03PacketScreen.newBuilder()
                .setWidth(width)
                .setHeight(height)
                .setImageWidth(transcodeImage.getWidth())
                .setImageHeight(transcodeImage.getHeight())
                .setResizeFactor(resizeFactor)
                .setIsFullFrame(forceFullFrame)
                .setScreenImage(ByteString.copyFrom(imageData))
                .setTimestamp(System.currentTimeMillis())
                .build();
        Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                .setPacketId(3)
                .setPayload(packetScreen.toByteString()).build();

        packetWriter.sendQueue.add(container);
        TrafficCounter.getCounterFrame().incrementAndGet();
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }

    public Image scaleToFit(BufferedImage originalImage, int width, int height, int scaleMode) {
        float canvasRatio = width / (float) height;
        float imageRatio = originalImage.getWidth() / (float) originalImage.getHeight();
        if (imageRatio > canvasRatio) { // width larger, fit height
            return originalImage.getScaledInstance(width, (int) (width / imageRatio), scaleMode);
        } else {
            return originalImage.getScaledInstance((int) (height * imageRatio), height, scaleMode);
        }
    }
}
