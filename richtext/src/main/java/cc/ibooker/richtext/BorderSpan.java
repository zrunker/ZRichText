package cc.ibooker.richtext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * 添加边框
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class BorderSpan extends ReplacementSpan {
    private int start, end;
    private int color;
    private final Paint mPaint;
    private int mWidth;

    public BorderSpan(int start, int end, int color) {
        this.start = start;
        this.end = end;
        this.color = color;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        // return text with relative to the Paint
        mWidth = (int) paint.measureText(text, start, end);
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        if (this.end < start
                || this.start > end) return;
        // draw the frame with custom Paint
        canvas.drawRect(x, top, x + mWidth, bottom, mPaint);
        canvas.drawText(text, start, end, x, y, paint);
    }
}