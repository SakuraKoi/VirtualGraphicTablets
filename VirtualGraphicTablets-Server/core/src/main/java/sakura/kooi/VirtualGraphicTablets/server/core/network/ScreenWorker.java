package sakura.kooi.VirtualGraphicTablets.server.core.network;

import com.google.protobuf.ByteString;
import lombok.CustomLog;
import sakura.kooi.VirtualGraphicTablets.protocol.Vgt;
import sakura.kooi.VirtualGraphicTablets.server.core.VTabletServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@CustomLog
public class ScreenWorker extends Thread {
    private VTabletServer parent;
    private PacketWriter packetWriter;

    public ScreenWorker(VTabletServer parent, PacketWriter packetWriter) {
        this.parent = parent;
        this.packetWriter = packetWriter;
    }

    @Override
    public void run() {
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            log.e("Create screen capture failed", e);
            return;
        }
        log.i("Screen stream started...");
        while (!isInterrupted()) {
            int posX = (int) parent.numCanvaPosX.getValue();
            int posY = (int) parent.numCanvaPosY.getValue();
            int width = (int) parent.numCanvaWidth.getValue();
            int height = (int) parent.numCanvaHeight.getValue();

            Rectangle capture = new Rectangle(posX, posY, width, height);
            BufferedImage originalImage = r.createScreenCapture(capture);


            parent.canvas.setIcon(new ImageIcon(
                    scaleToFit(originalImage,
                            parent.canvas.getWidth(),
                            parent.canvas.getHeight(),
                            Image.SCALE_FAST)
            ));

            Image sendToClient = scaleToFit(originalImage, parent.tabletWidth, parent.tabletHeight, Image.SCALE_SMOOTH);
            BufferedImage transcodedImage = toBufferedImage(sendToClient);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(transcodedImage, "jpg", baos);
            } catch (IOException e) {
                log.w("Encode screen image failed, skip 1 frame", e);
                continue;
            }

            byte[] imageData = baos.toByteArray();
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
            try {
                Thread.sleep(1000 / (int) parent.numFps.getValue());
            } catch (InterruptedException e) {
                break;
            }
        }
        parent.removePreview();
        log.i("Screen stream end");
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
