package cc.ibooker.richtext;

/**
 * 富文本相关数据
 */
public class RichBean {
    private String text;// 文本或图片URL
    private int type;// 0-文本，1-图片
    private int height;
    private int width;
    private ClickSpan.OnClickSpan onClickSpan;// 点击事件
    private RichImgBean richImgBean;

    public RichBean() {
        super();
    }

    public RichBean(String text, int type, int height, int width) {
        this.text = text;
        this.type = type;
        this.height = height;
        this.width = width;
    }

    public RichBean(String text, int type, int height, int width, ClickSpan.OnClickSpan onClickSpan) {
        this.text = text;
        this.type = type;
        this.height = height;
        this.width = width;
        this.onClickSpan = onClickSpan;
    }

    public RichBean(String text, int type, int height, int width, ClickSpan.OnClickSpan onClickSpan, RichImgBean richImgBean) {
        this.text = text;
        this.type = type;
        this.height = height;
        this.width = width;
        this.onClickSpan = onClickSpan;
        this.richImgBean = richImgBean;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ClickSpan.OnClickSpan getOnClickSpan() {
        return onClickSpan;
    }

    public void setOnClickSpan(ClickSpan.OnClickSpan onClickSpan) {
        this.onClickSpan = onClickSpan;
    }

    public RichImgBean getRichImgBean() {
        return richImgBean;
    }

    public void setRichImgBean(RichImgBean richImgBean) {
        this.richImgBean = richImgBean;
    }

    @Override
    public String toString() {
        return "RichBean{" +
                "text='" + text + '\'' +
                ", type=" + type +
                ", height=" + height +
                ", width=" + width +
                ", onClickSpan=" + onClickSpan +
                ", richImgBean=" + richImgBean +
                '}';
    }
}
