package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageDiffEncoder {
    private BufferedImage lastFrame;

    private int width, height;

    public ImageDiffEncoder(int tabletWidth, int tabletHeight) {
        this.width = tabletWidth;
        this.height = tabletHeight;
        lastFrame = new BufferedImage(tabletWidth, tabletHeight, BufferedImage.TYPE_INT_ARGB);
    }

    public byte[] encode(BufferedImage currentFrame) {
        byte[] result = encodeDiff(lastFrame, currentFrame);
        Graphics2D composite = lastFrame.createGraphics();
        composite.clearRect(0, 0, width, height);
        composite.drawImage(currentFrame, 0, 0, null);
        composite.dispose();
        return result;
    }

    private byte[] encodeDiff(BufferedImage lastFrame, BufferedImage currentFrame) {
        int frameHeight = currentFrame.getHeight();
        int frameWidth = currentFrame.getWidth();
        if (frameHeight > height)
            throw new IllegalArgumentException("too large frame height");
        if (frameWidth > width)
            throw new IllegalArgumentException("too large frame width");

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(width * height * 3);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < frameWidth && y < frameHeight) {
                    int rgbCurrent = currentFrame.getRGB(x, y);
                    if (lastFrame.getRGB(x, y) == rgbCurrent) {
                        writeRgb(buffer, 0xff, 0xff, 0xff);
                    } else {
                        int r = (rgbCurrent >> 16) & 0xFF;
                        int g = (rgbCurrent >> 8) & 0xFF;
                        int b = (rgbCurrent) & 0xFF;
                        if (r == 0xff && g == 0xff && b == 0xff) {
                            r = g = b = 0xfe;
                        }

                        writeRgb(buffer, r, g, b);
                    }
                } else { // 0x00, 0x00, 0x00 -> black
                    writeRgb(buffer, 0x00, 0x00, 0x00);
                }
            }
        }
        return buffer.array();
    }

    private void writeRgb(ByteBuf buffer, int r, int g, int b) {
        buffer.writeByte(r);
        buffer.writeByte(g);
        buffer.writeByte(b);
    }
}
