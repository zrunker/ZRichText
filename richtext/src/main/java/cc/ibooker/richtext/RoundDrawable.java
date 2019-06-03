package cc.ibooker.richtext;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

/**
 * 圆角图片实现
 */
public class RoundDrawable extends BitmapDrawable {
    private final Paint paint;
    private float radisu;
    private RectF rectF;

    public RoundDrawable(Resources resources, Bitmap bitmap) {
        super(resources, bitmap);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(rectF, radisu, radisu, paint);
    }

    public void setRadius(float radisu) {
        this.radisu = radisu;
    }


    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        rectF = new RectF(left, top, right, bottom);
    }
}

