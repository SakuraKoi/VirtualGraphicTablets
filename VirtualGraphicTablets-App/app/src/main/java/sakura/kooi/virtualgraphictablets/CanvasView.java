package sakura.kooi.virtualgraphictablets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap content;
    private int fps;
    private boolean showFps;
    private int pointerX = -1, pointerY = -1;

    private Paint paintContent;
    private Paint paintFps;
    private Paint paintPointer;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CanvasView(Context context, AttributeSet attributeSet, int a, int b) {
        super(context, attributeSet, a, b);
        init();
    }

    public CanvasView(Context context, AttributeSet attributeSet, int a) {
        super(context, attributeSet, a);
        init();
    }

    private void init() {
        this.getHolder().addCallback(this);
        this.paintContent = new Paint();
        this.paintContent.setAntiAlias(true);
        paintFps = new Paint();
        paintFps.setColor(Color.GREEN);
        paintFps.setTextSize(12);
        paintPointer = new Paint();
        paintPointer.setColor(Color.argb(0x88, 0x00, 0x00, 0x00));
        paintPointer.setStyle(Paint.Style.STROKE);
        paintPointer.setStrokeWidth(1f);
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setShowFps(boolean showFps) {
        this.showFps = showFps;
    }

    public void drawContent(Canvas canvas) {
        if (this.content != null)
            canvas.drawBitmap(this.content, 0.0f, 0.0f, paintContent);
        if (showFps)
            canvas.drawText(fps + " FPS", 16, 16, paintFps);
        if (this.pointerX != -1 && this.pointerY != -1) {
            canvas.drawLine(pointerX, pointerY - 10, pointerX, pointerY + 10, paintPointer);
            canvas.drawLine(pointerX - 10, pointerY, pointerX + 10, pointerY, paintPointer);
        }
    }

    public void setContent(Bitmap content) {
        this.content = content;
        Canvas canvas = holder.lockCanvas();
        if (canvas == null)
            return;
        drawContent(canvas);
        holder.unlockCanvasAndPost(canvas);
    }
    public void setCursor(float x, float y) {
        this.pointerX = (int) x;
        this.pointerY = (int) y;
        Canvas canvas = holder.lockCanvas();
        if (canvas == null)
            return;
        drawContent(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private SurfaceHolder holder;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.holder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
