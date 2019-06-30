package cc.ibooker.richtext.bean;

import cc.ibooker.richtext.ClickSpan;
import cc.ibooker.richtext.VerticalImageSpan;

/**
 * 与ImageSpan相关数据
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class RichImgBean {
    private String realText;// 真实文本 一般是Url
    private int res;// 图片Res
    private VerticalImageSpan verticalImageSpan;
    private ClickSpan.OnClickSpan onClickSpan;// 点击事件

    public RichImgBean() {
        super();
    }

    public RichImgBean(String realText, VerticalImageSpan verticalImageSpan, ClickSpan.OnClickSpan onClickSpan) {
        this.realText = realText;
        this.verticalImageSpan = verticalImageSpan;
        this.onClickSpan = onClickSpan;
    }

    public RichImgBean(String realText, int res, VerticalImageSpan verticalImageSpan, ClickSpan.OnClickSpan onClickSpan) {
        this.realText = realText;
        this.res = res;
        this.verticalImageSpan = verticalImageSpan;
        this.onClickSpan = onClickSpan;
    }

    public String getRealText() {
        return realText;
    }

    public void setRealText(String realText) {
        this.realText = realText;
    }

    public VerticalImageSpan getVerticalImageSpan() {
        return verticalImageSpan;
    }

    public void setVerticalImageSpan(VerticalImageSpan verticalImageSpan) {
        this.verticalImageSpan = verticalImageSpan;
    }

    public ClickSpan.OnClickSpan getOnClickSpan() {
        return onClickSpan;
    }

    public void setOnClickSpan(ClickSpan.OnClickSpan onClickSpan) {
        this.onClickSpan = onClickSpan;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "RichImgBean{" +
                "realText='" + realText + '\'' +
                ", res=" + res +
                ", verticalImageSpan=" + verticalImageSpan +
                ", onClickSpan=" + onClickSpan +
                '}';
    }
}
