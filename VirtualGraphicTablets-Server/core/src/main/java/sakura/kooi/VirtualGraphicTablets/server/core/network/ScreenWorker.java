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

            sendPacket(posX, posY, width, height);

            try {
                Thread.sleep(1000 / (int) parent.numFps.getValue());
            } catch (InterruptedException e) {
                break;
            }
        }
        parent.removePreview();
        log.i("Screen stream end");
    }

    private void sendPacket(int posX, int posY, int width, int height) {
        Rectangle capture = new Rectangle(-posX, -posY, width, height);
        BufferedImage originalImage = JnaUtils.bitbltRegion(capture);

        parent.canvas.setIcon(new ImageIcon(
                scaleToFit(originalImage,
                        parent.canvas.getWidth(),
                        parent.canvas.getHeight(),
                        Image.SCALE_FAST)
        ));

        Image sendToClient = scaleToFit(originalImage, parent.tabletWidth, parent.tabletHeight, Image.SCALE_FAST);
        BufferedImage transcodedImage = toBufferedImage(sendToClient);
        byte[] imageData = encodeImage(transcodedImage);
        if (imageData == null) return;

        Vgt.S03PacketScreen packetScreen = Vgt.S03PacketScreen.newBuilder()
                .setWidth(width)
                .setHeight(height)
                .setScreenImage(ByteString.copyFrom(imageData))
                .build();
        Vgt.PacketContainer container = Vgt.PacketContainer.newBuilder()
                .setPacketId(3)
                .setPayload(packetScreen.toByteString()).build();

        packetWriter.sendQueue.add(container);
        TrafficCounter.getCounterFrame().incrementAndGet();
    }

    private byte[] encodeImage(BufferedImage transcodedImage) {
        return imageDiffEncoder.encode(transcodedImage);
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
