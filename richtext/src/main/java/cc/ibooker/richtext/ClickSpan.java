package cc.ibooker.richtext;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 可点击ClickableSpan
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class ClickSpan extends ClickableSpan {
    private String txt;

    public ClickSpan(String txt) {
        this.txt = txt;
    }

    @Override
    public void onClick(View widget) {
        if (onClickSpan != null)
            onClickSpan.onClickSpan(txt);
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
    public interface OnClickSpan {
        void onClickSpan(String txt);
    }

    private OnClickSpan onClickSpan;

    public void setOnClickSpan(OnClickSpan onClickSpan) {
        this.onClickSpan = onClickSpan;
    }
}