package sakura.kooi.virtualgraphictablets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CanvasView extends View {
    private Bitmap content;
    private int fps;
    private boolean showFps;

    private Paint paintContent;
    private Paint paintFps;

    public CanvasView(Context context) {
        super(context);
        this.paintContent = new Paint();
        this.paintContent.setAntiAlias(true);
        paintFps = new Paint();
        paintFps.setColor(Color.GREEN);
        paintFps.setTextSize(12);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.content != null)
            canvas.drawBitmap(this.content, 0.0f, 0.0f, paintContent);
        if (showFps)
            canvas.drawText(fps + " FPS", 8, 8, paintFps);
    }

    public void setContent(Bitmap content) {
        this.content = content;
        this.invalidate();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setShowFps(boolean showFps) {
        this.showFps = showFps;
    }
}
