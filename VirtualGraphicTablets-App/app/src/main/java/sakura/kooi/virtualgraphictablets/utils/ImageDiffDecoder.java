package sakura.kooi.virtualgraphictablets.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ImageDiffDecoder {
    private Bitmap frame;
    private Canvas canvas;
    private Paint paint;

    private int width, height;

    private int WORKER_COUNT = 32;
    private ArrayList<ImageDiffWorker> workers = new ArrayList<>();
    private ExecutorService threadPool = new ForkJoinPool();

    public ImageDiffDecoder(int tabletWidth, int tabletHeight) {
        this.width = tabletWidth;
        this.height = tabletHeight;
        frame = Bitmap.createBitmap(tabletWidth, tabletHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(frame);
        this.paint = new Paint();
        this.paint.setAntiAlias(false);
        this.paint.setAlpha(0);
        this.paint.setStyle(Paint.Style.FILL);

        int linePerWorker = tabletHeight / WORKER_COUNT;
        if (linePerWorker * WORKER_COUNT < tabletHeight)
            WORKER_COUNT++;

        for (int i = 0 ; i < WORKER_COUNT; i++) {
            workers.add(new ImageDiffWorker(tabletWidth, tabletHeight, i, linePerWorker));
        }
    }

    public Bitmap update(byte[] data) {
        ArrayList<Future<Consumer<Canvas>>> pendingDecodes = new ArrayList<>(WORKER_COUNT);
        for (ImageDiffWorker worker : workers) {
            pendingDecodes.add(threadPool.submit(() -> worker.call(data)));
        }

        for (Future<Consumer<Canvas>> pendingDecode : pendingDecodes) {
            try {
                pendingDecode.get().accept(canvas);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return frame;
            }
        }
        return frame;
    }

    public void stop() {
        threadPool.shutdownNow();
    }
}
