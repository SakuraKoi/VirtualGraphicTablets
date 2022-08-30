package sakura.kooi.virtualgraphictablets.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.concurrent.atomic.AtomicReference;

public class ImageDiffWorker {
    private int width;
    private int height;
    private int startlinePosition;

    private int[] pixels;

    public ImageDiffWorker(int tabletWidth, int tabletHeight, int index, int linePerWorker) {
        this.width = tabletWidth;
        startlinePosition = index * linePerWorker;
        int endLinePosition = startlinePosition + linePerWorker;
        height = linePerWorker;
        if (tabletHeight < endLinePosition) {
            height = tabletHeight - startlinePosition;
        }

        Bitmap frame = Bitmap.createBitmap(tabletWidth, height, Bitmap.Config.ARGB_8888);
        pixels = new int[width*height];
        frame.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    public void call(byte[] data, AtomicReference<int[]> master) {
       // ByteBuf buffer = Unpooled.wrappedBuffer(data);
       // buffer.skipBytes(startlinePosition * width * 3);
        int position = startlinePosition * width * 3;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = data[position++] & 0xFF;
                int g = data[position++] & 0xFF;
                int b = data[position++] & 0xFF;
                //int r = buffer.readByte() & 0xFF;
                //int g = buffer.readByte() & 0xFF;
                //int b = buffer.readByte() & 0xFF;
                if (r == 0xff && g == 0xff && b == 0xff) {
                    continue;
                }
                this.pixels[y * width + x] = Color.rgb(r, g, b);
            }
        }
        System.arraycopy(this.pixels, 0, master.get(), startlinePosition * width, this.pixels.length);
    }
}
