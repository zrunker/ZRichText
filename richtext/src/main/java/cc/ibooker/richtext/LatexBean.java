package cc.ibooker.richtext;

/**
 * 公式相关数据
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class LatexBean {
    private String latex;
    private int startPosition;
    private int endPosition;
    private LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener;

    public LatexBean() {
        super();
    }

    public LatexBean(String latex, int startPosition, int endPosition) {
        this.latex = latex;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public LatexBean(String latex, int startPosition, int endPosition, LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener) {
        this.latex = latex;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.onLatexClickSpanListener = onLatexClickSpanListener;
    }

    public String getLatex() {
        return latex;
    }

    public void setLatex(String latex) {
        this.latex = latex;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public LatexClickSpan.OnLatexClickSpan getOnLatexClickSpanListener() {
        return onLatexClickSpanListener;
    }

    public void setOnLatexClickSpanListener(LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener) {
        this.onLatexClickSpanListener = onLatexClickSpanListener;
    }

    @Override
    public String toString() {
        return "LatexBean{" +
                "latex='" + latex + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", onLatexClickSpanListener=" + onLatexClickSpanListener +
                '}';
    }
}
