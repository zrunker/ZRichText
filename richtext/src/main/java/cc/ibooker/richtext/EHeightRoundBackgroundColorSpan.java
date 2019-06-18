package cc.ibooker.richtext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.LineBackgroundSpan;

/**
 * 背景-文字和图片等高+设置圆角
 */
public class EHeightRoundBackgroundColorSpan implements LineBackgroundSpan {
    private int start, end;
    private int backGroundColor;
    private int radius;

    public EHeightRoundBackgroundColorSpan(int start, int end, int backGroundColor) {
        this.start = start;
        this.end = end;
        this.backGroundColor = backGroundColor;
    }

    public EHeightRoundBackgroundColorSpan(int start, int end, int backGroundColor, int radius) {
        this.start = start;
        this.end = end;
        this.backGroundColor = backGroundColor;
        this.radius = radius;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int start, int end, int lineNum) {
        int originalColor = paint.getColor();
        if (this.end < start
                || this.start > end) return;
        SpannableString spannableString = new SpannableString(charSequence);
        int offsetX = 0;

        if (this.start > start) {
            for (int i = start; i < this.start; i++) {
                VerticalImageSpan[] spans = spannableString.getSpans(i, i + 1, VerticalImageSpan.class);
                if (spans.length > 0) {
                    Drawable drawable = spans[0].getDrawable();
                    int width = drawable.getBounds().right - drawable.getBounds().left;
                    offsetX += width;
                } else {
                    offsetX += paint.measureText(charSequence.subSequence(i, i + 1).toString());
                }
            }
        }

        int min = Math.max(this.start, start);
        int max = Math.min(this.end, end);
        int length = 0;
        for (int i = 0; i < (max - min); i++) {
            int index = min + i;
            VerticalImageSpan[] spans = spannableString.getSpans(index, index + 1, VerticalImageSpan.class);
            if (spans.length > 0) {
                int tempWidth = 0;
                for (VerticalImageSpan span : spans) {
                    Drawable drawable = span.getDrawable();
                    int width = drawable.getBounds().right - drawable.getBounds().left;
                    if (tempWidth < width)
                        tempWidth = width;
                }
                length += tempWidth;
            } else {
                length += paint.measureText(charSequence.subSequence(index, index + 1).toString());
            }
        }

        // 高
        int imageHeight = 0;
        VerticalImageSpan[] spans = spannableString.getSpans(start, end, VerticalImageSpan.class);
        if (spans.length > 0) {
            for (VerticalImageSpan span : spans) {
                Drawable drawable = span.getDrawable();
                int height = drawable.getBounds().bottom - drawable.getBounds().top;
                if (imageHeight < height)
                    imageHeight = drawable.getBounds().bottom;
            }
        }

        int newLeft = offsetX;
        int newRight = length + offsetX;
        int newBottom = (int) (baseline + paint.descent());
        if (imageHeight != 0)
            newBottom = imageHeight + top;

        RectF rectF = new RectF();
        rectF.left = newLeft;
        rectF.right = newRight;
        rectF.top = top;
        rectF.bottom = newBottom;
        if (this.start == start)
            rectF.left -= 4;
        if (this.end == end)
            rectF.right += 4;
        paint.setColor(backGroundColor);
        if (radius > 0)
            canvas.drawRoundRect(rectF, radius, radius, paint);
        else
            canvas.drawRect(rectF, paint);

        // 将paint复原
        paint.setColor(originalColor);
    }
}
