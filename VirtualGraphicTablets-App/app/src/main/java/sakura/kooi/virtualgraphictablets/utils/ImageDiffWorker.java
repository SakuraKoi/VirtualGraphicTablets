package sakura.kooi.virtualgraphictablets.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.util.Consumer;

import java.util.concurrent.Callable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ImageDiffWorker {
    private int width;
    private int selfHeight;
    private int startlinePosition;

    private Bitmap frame;
    private Canvas canvas;
    private Paint paint;

    public ImageDiffWorker(int tabletWidth, int tabletHeight, int index, int linePerWorker) {
        this.width = tabletWidth;
        startlinePosition = index * linePerWorker;
        int endLinePosition = startlinePosition + linePerWorker;
        selfHeight = linePerWorker;
        if (tabletHeight < endLinePosition) {
            selfHeight = tabletHeight - startlinePosition;
        }

        frame = Bitmap.createBitmap(tabletWidth, selfHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(frame);
        this.paint = new Paint();
        this.paint.setAntiAlias(false);
        this.paint.setAlpha(0);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public Consumer<Canvas> call(byte[] data) {
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        buffer.skipBytes(startlinePosition * width * 3);
        for (int y = 0; y < selfHeight; y++) {
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
        return (masterCanvas ->
                masterCanvas.drawBitmap(this.frame, 0, startlinePosition, null)
        );
    }
}
