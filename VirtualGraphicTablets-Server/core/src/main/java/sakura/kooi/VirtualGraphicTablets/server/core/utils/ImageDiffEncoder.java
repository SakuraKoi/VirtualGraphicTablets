package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ImageDiffEncoder {
    private byte[] buffer;

    private int width, height;

    private ForkJoinPool forkJoinPool = new ForkJoinPool();

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

        forkJoinPool.invoke(new ImageDiffWorker(frameWidth, frameHeight, forceFullFrame, currentFrame, 0, height));
        return buffer;
/*
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
        return buffer;*/
    }

    public void shutdownPool() {
        forkJoinPool.shutdownNow();
    }

    @AllArgsConstructor
    protected class ImageDiffWorker extends RecursiveAction {
        private int frameWidth;
        private int frameHeight;
        private boolean forceFullFrame;
        private int[] currentFrame;

        private int startHeight;
        private int encodeRange;

        @Override
        protected void compute() {
            if (encodeRange > 1 && encodeRange * width > 150000) {
                int split = encodeRange / 2;
                var worker1 = new ImageDiffWorker(frameWidth, frameHeight, forceFullFrame, currentFrame, startHeight, split);
                var worker2 = new ImageDiffWorker(frameWidth, frameHeight, forceFullFrame, currentFrame, startHeight + split, encodeRange - split);
                worker1.fork();
                worker2.fork();
                worker1.join();
                worker2.join();
            } else {
                for (int y = startHeight, len = startHeight + encodeRange; y < len; y++) {
                    for (int x = 0; x < width; x++) {
                        int start = (y * width + x) * 3;
                        int r, g, b;
                        if (x < frameWidth && y < frameHeight) {
                            int rgbCurrent = currentFrame[y * frameWidth + x] & 0xFFFFFF;
                            if (rgbCurrent == 0xFFFFFF) {
                                rgbCurrent = 0xFEFEFE;
                            }

                            int rgbLast = ((buffer[start] & 0xFF) << 16) |
                                    ((buffer[start + 1] & 0xFF) << 8) |
                                    ((buffer[start + 2]) & 0xFF);

                            if (rgbLast == rgbCurrent && !forceFullFrame) {
                                r = g = b = 0xff;
                            } else {
                                r = (rgbCurrent >> 16) & 0xFF;
                                g = (rgbCurrent >> 8) & 0xFF;
                                b = (rgbCurrent) & 0xFF;
                            }
                        } else { // 0x00, 0x00, 0x00 -> black
                            r = g = b = 0x00;
                        }

                        buffer[start] = (byte) r;
                        buffer[start + 1] = (byte) g;
                        buffer[start + 2] = (byte) b;
                    }
                }
            }
        }
    }
}
