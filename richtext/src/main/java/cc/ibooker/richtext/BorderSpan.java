package cc.ibooker.richtext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.style.ReplacementSpan;

/**
 * 添加边框
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class BorderSpan extends ReplacementSpan {
    private Paint mPaint;
    private int mWidth;

    public BorderSpan(int borderColor) {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(borderColor);
        this.mPaint.setAntiAlias(true);
    }

    public BorderSpan(String borderColor) {
        int color = Color.TRANSPARENT;
        try {
            if (!TextUtils.isEmpty(borderColor))
                color = Color.parseColor(borderColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(color);
        this.mPaint.setAntiAlias(true);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        // return text with relative to the Paint
        mWidth = (int) paint.measureText(text, start, end);
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        // draw the frame with custom Paint
        canvas.drawRect(x, top, x + mWidth, bottom, mPaint);
        canvas.drawText(text, start, end, x, y, paint);
    }
}