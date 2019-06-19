package cc.ibooker.richtext;

import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/**
 * 设置颜色数组
 */
public class ColorsSpan extends CharacterStyle implements UpdateAppearance {
    private int[] colors;

    public ColorsSpan(int[] colors) {
        this.colors = colors;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors, null, Shader.TileMode.MIRROR);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
    }
}