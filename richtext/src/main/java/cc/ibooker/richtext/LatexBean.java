package cc.ibooker.richtext;

/**
 * 公式相关数据
 */
public class LatexBean {
    private String latex;
    private int startPosition;
    private int endPosition;

    public LatexBean() {
        super();
    }

    public LatexBean(String latex, int startPosition, int endPosition) {
        this.latex = latex;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
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

    @Override
    public String toString() {
        return "LatexBean{" +
                "latex='" + latex + '\'' +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}