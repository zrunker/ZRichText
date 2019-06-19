package cc.ibooker.richtext;

import android.graphics.Bitmap;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Latex点击事件
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class LatexClickSpan extends ClickableSpan {
    private String latex;
    private Bitmap bitmap;

    public LatexClickSpan(String latex, Bitmap bitmap) {
        this.latex = latex;
        this.bitmap = bitmap;
    }

    @Override
    public void onClick(View widget) {
        if (onLatexClickSpan != null)
            onLatexClickSpan.onLatexClickSpan(latex, bitmap);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        // 根据自己的需求定制文本的样式
//        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

    /**
     * 点击回调接口
     */
    public interface OnLatexClickSpan {
        void onLatexClickSpan(String latex, Bitmap bitmap);
    }

    private OnLatexClickSpan onLatexClickSpan;

    public void setOnLatexClickSpan(OnLatexClickSpan onLatexClickSpan) {
        this.onLatexClickSpan = onLatexClickSpan;
    }
}
