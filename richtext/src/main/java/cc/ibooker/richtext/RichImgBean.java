package cc.ibooker.richtext;

/**
 * 与ImageSpan相关数据
 */
public class RichImgBean {
    private String realText;// 真实文本 一般是Url
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

    @Override
    public String toString() {
        return "RichImgBean{" +
                ", realText='" + realText + '\'' +
                ", verticalImageSpan=" + verticalImageSpan +
                ", onClickSpan=" + onClickSpan +
                '}';
    }
}
