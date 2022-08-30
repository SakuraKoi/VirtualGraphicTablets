package sakura.kooi.virtualgraphictablets.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class ImageDiffDecoder {
    private Bitmap frame;
    private AtomicReference<int[]> pixels;

    private int width, height;

    private int WORKER_COUNT = Runtime.getRuntime().availableProcessors();
    private ArrayList<ImageDiffWorker> workers = new ArrayList<>();
    private ExecutorService threadPool = new ForkJoinPool();

    public ImageDiffDecoder(int tabletWidth, int tabletHeight) {
        this.width = tabletWidth;
        this.height = tabletHeight;
        frame = Bitmap.createBitmap(tabletWidth, tabletHeight, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width*height];
        frame.getPixels(pixels, 0, width, 0, 0, width, height);
        this.pixels = new AtomicReference<>(pixels);

        int linePerWorker = tabletHeight / WORKER_COUNT;
        if (linePerWorker * WORKER_COUNT < tabletHeight)
            WORKER_COUNT++;

        for (int i = 0 ; i < WORKER_COUNT; i++) {
            workers.add(new ImageDiffWorker(tabletWidth, tabletHeight, i, linePerWorker));
        }
    }

    public Bitmap update(byte[] data) {
        ArrayList<Future<?>> pendingDecodes = new ArrayList<>(WORKER_COUNT);
        for (ImageDiffWorker worker : workers) {
            pendingDecodes.add(threadPool.submit(() -> worker.call(data, pixels)));
        }

        for (Future<?> pendingDecode : pendingDecodes) {
            try {
                pendingDecode.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return frame;
            }
        }
        frame.setPixels(pixels.get(), 0, width, 0, 0, width, height);

        return frame;
    }

    public void stop() {
        threadPool.shutdownNow();
    }
}
