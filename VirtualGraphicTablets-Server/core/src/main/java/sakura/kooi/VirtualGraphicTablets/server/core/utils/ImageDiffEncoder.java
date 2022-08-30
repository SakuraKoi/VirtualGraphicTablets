package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageDiffEncoder {
    private byte[] buffer;

    private int width, height;

    public ImageDiffEncoder(int tabletWidth, int tabletHeight) {
        this.width = tabletWidth;
        this.height = tabletHeight;
        buffer = new byte[width * height * 3];
    }

    public byte[] encode(BufferedImage currentFrame, boolean forceFullFrame) {
        int[] pixels = ((DataBufferInt) currentFrame.getRaster().getDataBuffer()).getData();
        return encodeDiff(pixels, currentFrame.getWidth(), currentFrame.getHeight(), forceFullFrame);
    }

    private byte[] encodeDiff(int[] currentFrame, int frameWidth, int frameHeight, boolean forceFullFrame) {
        if (frameHeight > height)
            throw new IllegalArgumentException("too large frame height");
        if (frameWidth > width)
            throw new IllegalArgumentException("too large frame width");

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r, g, b;
                if (x < frameWidth && y < frameHeight) {
                    int rgbCurrent = currentFrame[y * frameWidth + x] & 0xFFFFFF;
                    if (rgbCurrent == 0xFFFFFF) {
                        rgbCurrent = 0xFEFEFE;
                    }

                    int start = (y * width + x) * 3;
                    int rgbLast = ((buffer[start] & 0xFF) << 16) |
                            ((buffer[start + 1] & 0xFF) << 8) |
                            ((buffer[start + 2]) & 0xFF);

                    if (rgbLast == rgbCurrent && !forceFullFrame) {
                        r = g = b = 0xff;
                    } else {
                        r = (rgbCurrent >> 16) & 0xFF;
                        g = (rgbCurrent >> 8) & 0xFF;
                        b = (rgbCurrent) & 0xFF;

                        buffer[start] = (byte) r;
                        buffer[start + 1] = (byte) g;
                        buffer[start + 2] = (byte) b;
                    }
                } else { // 0x00, 0x00, 0x00 -> black
                    r = g = b = 0x00;
                }

                buffer[index++] = (byte) r;
                buffer[index++] = (byte) g;
                buffer[index++] = (byte) b;
            }
        }
        return buffer;
    }
}
