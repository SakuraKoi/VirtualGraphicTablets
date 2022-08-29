package sakura.kooi.virtualgraphictablets.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ImageDiffDecoder {
    private Bitmap frame;
    private Canvas canvas;
    private Paint paint;

    private int width, height;

    public ImageDiffDecoder(int tabletWidth, int tabletHeight) {
        this.width = tabletWidth;
        this.height = tabletHeight;
        frame = Bitmap.createBitmap(tabletWidth, tabletHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(frame);
        this.paint = new Paint();
        this.paint.setAntiAlias(false);
        this.paint.setAlpha(0);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public Bitmap update(byte[] data) {
        // TODO async decoding

        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = buffer.readByte() & 0xFF;
                int g = buffer.readByte() & 0xFF;
                int b = buffer.readByte() & 0xFF;
                if (r == 0xff && g == 0xff && b == 0xff) {
                    continue;
                }

                this.paint.setColor(Color.rgb(r, g, b));
                canvas.drawPoint(x, y, this.paint);
            }
        }
        return frame;
    }
}
