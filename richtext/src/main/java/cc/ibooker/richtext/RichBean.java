package cc.ibooker.richtext;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * 富文本相关数据
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class RichBean {
    private String text;// 文本或图片URL/图片文件地址
    private int res;// 图片Res
    private int type;// 0-文本，1-图片
    private int height; // 图片高
    private int width; // 图片宽
    private String backgroundColor;// 背景颜色16进制
    private String color;// 文本颜色16进制
    private String addUrl;// 添加超链接 - 针对于文本
    private float textSizeMultiple;// 字体倍数 - 针对于文本
    private boolean isUnderline;// 是否添加下划线 - 针对于文本
    private boolean isStrikethrough;// 是否添加删除线 - 针对于文本
    private boolean isSuperscript;// 是否为上标 - 针对于文本
    private boolean isSubscript;// 是否为下标 - 针对于文本
    private boolean isBold;// 是否加粗 - 针对于文本
    private boolean isItalic;// 是否斜体 - 针对于文本
    private boolean isBoldItalic;// 是否加粗并斜体 - 针对于文本
    private float scaleXMultiple;// X轴缩放倍数 - 针对于文本
    private ClickSpan.OnClickSpan onClickSpan;// 点击事件
    private LatexClickSpan.OnLatexClickSpan onLatexClickSpan;
    private RichImgBean richImgBean; // 图片相关信息
    private String borderColor;// 边框颜色
    private Drawable drawable;// 待插入图片
    private int pad;// 待出入图片边距
    private String quoteColor;// 竖线颜色
    private int absoluteSize;// 绝对大小
    private boolean isAbsoluteSizeDip;// 绝对大小是否dp为单位
    private Bitmap bitmap;// 待插入图片
    private int bitmapPad;// 待出入图片内边距

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

    public RichBean(String text, int type, int height, int width, String backgroundColor, String color, ClickSpan.OnClickSpan onClickSpan, RichImgBean richImgBean) {
        this.text = text;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.onClickSpan = onClickSpan;
        this.richImgBean = richImgBean;
    }

    public RichBean(String text, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, RichImgBean richImgBean) {
        this.text = text;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.richImgBean = richImgBean;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, RichImgBean richImgBean) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.richImgBean = richImgBean;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean, String borderColor) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
        this.borderColor = borderColor;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean, String borderColor, Drawable drawable, int pad) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
        this.borderColor = borderColor;
        this.drawable = drawable;
        this.pad = pad;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean, String borderColor, Drawable drawable, int pad, String quoteColor) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
        this.borderColor = borderColor;
        this.drawable = drawable;
        this.pad = pad;
        this.quoteColor = quoteColor;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean, String borderColor, Drawable drawable, int pad, String quoteColor, int absoluteSize, boolean isAbsoluteSizeDip) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
        this.borderColor = borderColor;
        this.drawable = drawable;
        this.pad = pad;
        this.quoteColor = quoteColor;
        this.absoluteSize = absoluteSize;
        this.isAbsoluteSizeDip = isAbsoluteSizeDip;
    }

    public RichBean(String text, int res, int type, int height, int width, String backgroundColor, String color, String addUrl, float textSizeMultiple, boolean isUnderline, boolean isStrikethrough, boolean isSuperscript, boolean isSubscript, boolean isBold, boolean isItalic, boolean isBoldItalic, float scaleXMultiple, ClickSpan.OnClickSpan onClickSpan, LatexClickSpan.OnLatexClickSpan onLatexClickSpan, RichImgBean richImgBean, String borderColor, Drawable drawable, int pad, String quoteColor, int absoluteSize, boolean isAbsoluteSizeDip, Bitmap bitmap, int bitmapPad) {
        this.text = text;
        this.res = res;
        this.type = type;
        this.height = height;
        this.width = width;
        this.backgroundColor = backgroundColor;
        this.color = color;
        this.addUrl = addUrl;
        this.textSizeMultiple = textSizeMultiple;
        this.isUnderline = isUnderline;
        this.isStrikethrough = isStrikethrough;
        this.isSuperscript = isSuperscript;
        this.isSubscript = isSubscript;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isBoldItalic = isBoldItalic;
        this.scaleXMultiple = scaleXMultiple;
        this.onClickSpan = onClickSpan;
        this.onLatexClickSpan = onLatexClickSpan;
        this.richImgBean = richImgBean;
        this.borderColor = borderColor;
        this.drawable = drawable;
        this.pad = pad;
        this.quoteColor = quoteColor;
        this.absoluteSize = absoluteSize;
        this.isAbsoluteSizeDip = isAbsoluteSizeDip;
        this.bitmap = bitmap;
        this.bitmapPad = bitmapPad;
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAddUrl() {
        return addUrl;
    }

    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    public float getTextSizeMultiple() {
        return textSizeMultiple;
    }

    public void setTextSizeMultiple(float textSizeMultiple) {
        this.textSizeMultiple = textSizeMultiple;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean underline) {
        isUnderline = underline;
    }

    public boolean isStrikethrough() {
        return isStrikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        isStrikethrough = strikethrough;
    }

    public boolean isSuperscript() {
        return isSuperscript;
    }

    public void setSuperscript(boolean superscript) {
        isSuperscript = superscript;
    }

    public boolean isSubscript() {
        return isSubscript;
    }

    public void setSubscript(boolean subscript) {
        isSubscript = subscript;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public boolean isBoldItalic() {
        return isBoldItalic;
    }

    public void setBoldItalic(boolean boldItalic) {
        isBoldItalic = boldItalic;
    }

    public float getScaleXMultiple() {
        return scaleXMultiple;
    }

    public void setScaleXMultiple(float scaleXMultiple) {
        this.scaleXMultiple = scaleXMultiple;
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

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public LatexClickSpan.OnLatexClickSpan getOnLatexClickSpan() {
        return onLatexClickSpan;
    }

    public void setOnLatexClickSpan(LatexClickSpan.OnLatexClickSpan onLatexClickSpan) {
        this.onLatexClickSpan = onLatexClickSpan;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public String getQuoteColor() {
        return quoteColor;
    }

    public void setQuoteColor(String quoteColor) {
        this.quoteColor = quoteColor;
    }

    public int getAbsoluteSize() {
        return absoluteSize;
    }

    public void setAbsoluteSize(int absoluteSize) {
        this.absoluteSize = absoluteSize;
    }

    public boolean isAbsoluteSizeDip() {
        return isAbsoluteSizeDip;
    }

    public void setAbsoluteSizeDip(boolean absoluteSizeDip) {
        isAbsoluteSizeDip = absoluteSizeDip;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getBitmapPad() {
        return bitmapPad;
    }

    public void setBitmapPad(int bitmapPad) {
        this.bitmapPad = bitmapPad;
    }

    @Override
    public String toString() {
        return "RichBean{" +
                "text='" + text + '\'' +
                ", res=" + res +
                ", type=" + type +
                ", height=" + height +
                ", width=" + width +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", color='" + color + '\'' +
                ", addUrl='" + addUrl + '\'' +
                ", textSizeMultiple=" + textSizeMultiple +
                ", isUnderline=" + isUnderline +
                ", isStrikethrough=" + isStrikethrough +
                ", isSuperscript=" + isSuperscript +
                ", isSubscript=" + isSubscript +
                ", isBold=" + isBold +
                ", isItalic=" + isItalic +
                ", isBoldItalic=" + isBoldItalic +
                ", scaleXMultiple=" + scaleXMultiple +
                ", onClickSpan=" + onClickSpan +
                ", onLatexClickSpan=" + onLatexClickSpan +
                ", richImgBean=" + richImgBean +
                ", borderColor='" + borderColor + '\'' +
                ", drawable=" + drawable +
                ", pad=" + pad +
                ", quoteColor='" + quoteColor + '\'' +
                ", absoluteSize=" + absoluteSize +
                ", isAbsoluteSizeDip=" + isAbsoluteSizeDip +
                ", bitmap=" + bitmap +
                ", bitmapPad=" + bitmapPad +
                '}';
    }
}
