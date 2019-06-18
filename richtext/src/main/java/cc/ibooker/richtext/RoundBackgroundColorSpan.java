package cc.ibooker.richtext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * 圆角BackgroundColorSpan
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int radius;
    private int backgroundColor;
    private int textColor;

    public RoundBackgroundColorSpan(int backgroundColor, int textColor, int radius) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.radius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
//        int originalColor = paint.getColor();
        RectF rect = new RectF(x, top, x + measureText(paint, text, start, end), bottom);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, radius, radius, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);
//        // 将paint复原
//        paint.setColor(originalColor);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}