package cc.ibooker.richtext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.ibooker.richtext.jlatexmath.core.AjLatexMath;
import cc.ibooker.richtext.jlatexmath.core.Insets;
import cc.ibooker.richtext.jlatexmath.core.TeXConstants;
import cc.ibooker.richtext.jlatexmath.core.TeXFormula;
import cc.ibooker.richtext.jlatexmath.core.TeXIcon;

/**
 * 自定义富文本View
 *
 * @author 邹峰立
 * <p>
 * https://github.com/zrunker/ZRichText
 */
public class RichTextView extends android.support.v7.widget.AppCompatTextView {
    private ArrayList<String> tempList;
    private SpannableString spannableString;
    private ArrayList<Integer> imgTextList;
    private ArrayList<RichBean> richBeanList;
    private ArrayList<LatexBean> latexBeanList;
    private int richTvWidth;
    private Drawable defaultDrawable;

    private boolean isScroll;// 是否可以滚动
    private int richMaxLines;// 最大行数
    private int loadImgTatol = 0;// 待加载图片总数
    private int loadImgComplete = 0;// 已加载图片总数
    private boolean isOpenImgCache = true;// 是否开启图片缓存，默认开启
    private int loadImgModel = 0;// 加载图片模式，0-Glide，1-DownLoadImage，默认0
    private String backGroundColor;// 背景颜色
    private String tintColor;// 文字颜色
    private boolean isResetData = true;// 是否重置数据
    private CharSequence richText;// 文本内容
    private boolean isLatexOneStr = false;// 是否将Latex公式当成一个字符串处理

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHighlightColor(Color.TRANSPARENT);// 消除点击时的背景色
        if (attrs != null) {
            TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RichTextView, 0, 0);
            isScroll = typeArray.getBoolean(R.styleable.RichTextView_isScroll, true);
            isOpenImgCache = typeArray.getBoolean(R.styleable.RichTextView_isOpenImgCache, true);
            loadImgModel = typeArray.getInt(R.styleable.RichTextView_loadImgModel, 0);
            backGroundColor = typeArray.getString(R.styleable.RichTextView_backGroundColor);
            tintColor = typeArray.getString(R.styleable.RichTextView_tintColor);
            isLatexOneStr = typeArray.getBoolean(R.styleable.RichTextView_isLatexOneStr, false);
            typeArray.recycle();
        }
        setRichTvScroll();
        if (!TextUtils.isEmpty(backGroundColor))
            try {
                setBackgroundColor(Color.parseColor(backGroundColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        if (!TextUtils.isEmpty(tintColor))
            try {
                setTextColor(Color.parseColor(tintColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        richText = getText();
        // 重置数据
        resetData();
        // 初始化AjLatexMath
        AjLatexMath.init(getContext().getApplicationContext());
    }

    // 设置是否能够滚动
    public RichTextView setScroll(boolean scroll) {
        this.isScroll = scroll;
        setRichTvScroll();
        return this;
    }

    // 设置默认图片
    public RichTextView setDefaultDrawable(Drawable defaultDrawable) {
        this.defaultDrawable = defaultDrawable;
        return this;
    }

    // 设置背景颜色
    public RichTextView setBackGroundColor(String backGroundColor) {
        this.backGroundColor = backGroundColor;
        return this;
    }

    // 设置Latex公式是否为一个字符串
    public RichTextView setLatexOneStr(boolean latexOneStr) {
        this.isLatexOneStr = latexOneStr;
        return this;
    }

    // 设置字体颜色
    public RichTextView setTintColor(String tintColor) {
        this.tintColor = tintColor;
        return this;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        if (maxLines > 0 && maxLines < Integer.MAX_VALUE) {
            this.richMaxLines = maxLines;
            this.isScroll = true;
        } else {
            this.isScroll = false;
        }
        setRichTvScroll();
    }

    @Override
    public CharSequence getText() {
        return richText;
    }

    // 设置是否能够滚动
    private void setRichTvScroll() {
        if (isScroll && richMaxLines <= 0)
            setMovementMethod(LinkMovementMethod.getInstance());
        else
            setOnTouchListener(new LinkMovementMethodOverride());
    }

    // 销毁
    public void onDestory() {
        DownLoadImage.getInstance().deStory();
        try {
            Glide.with(getContext()).pauseRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 设置图片加载模式
    public RichTextView setLoadImgModel(int loadImgModel) {
        if (loadImgModel == 0 || loadImgModel == 1)
            this.loadImgModel = loadImgModel;
        return this;
    }

    // 设置是否打开缓存
    public RichTextView setOpenImgCache(boolean openImgCache) {
        this.isOpenImgCache = openImgCache;
        return this;
    }

    // 清空数据
    private void resetData() {
        if (isResetData) {
            if (tempList == null)
                tempList = new ArrayList<>();
            else
                tempList.clear();
            spannableString = null;
            if (imgTextList == null)
                imgTextList = new ArrayList<>();
            else
                imgTextList.clear();
            if (richBeanList == null)
                richBeanList = new ArrayList<>();
            else
                richBeanList.clear();
            if (latexBeanList == null)
                latexBeanList = new ArrayList<>();
            else
                latexBeanList.clear();
            richTvWidth = 0;
            defaultDrawable = null;
            loadImgTatol = 0;
            loadImgComplete = 0;
        } else {
            if (latexBeanList == null)
                latexBeanList = new ArrayList<>();
            else
                latexBeanList.clear();
            loadImgTatol = 0;
            loadImgComplete = 0;
        }
        isResetData = true;
    }

    /**
     * 显示数据
     *
     * @param text 待显示数据
     */
    public RichTextView setRichText(final CharSequence text) {
        richText = text;
        setRichText(richText, false);
        return this;
    }

    /**
     * 显示数据
     *
     * @param text          待显示数据
     * @param isLatexOneStr 是否将Latex公式当中1位字符串处理
     * @return
     */
    public RichTextView setRichText(final CharSequence text, final boolean isLatexOneStr) {
        if (!TextUtils.isEmpty(text)) {
            resetData();
            // 显示数据
            richText = text;
            spannableString = new SpannableString(richText);
            setText(spannableString);
            // 处理Latex
            this.isLatexOneStr = isLatexOneStr;
            if (richTvWidth > 0) {
                dealWithLatex(text);
                setText(spannableString);
            } else {
                ViewTreeObserver vto = getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        richTvWidth = getMeasuredWidth();
                        dealWithLatex(text);
                        setText(spannableString);
                    }
                });
            }
        }
        return this;
    }

    /**
     * 显示数据
     *
     * @param text                     待显示数据
     * @param onLatexClickSpanListener Latex公式点击事件
     * @return
     */
    public RichTextView setRichText(final CharSequence text, LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener) {
        this.onLatexClickSpanListener = onLatexClickSpanListener;
        setRichText(text);
        return this;
    }

    /**
     * 显示数据
     *
     * @param richBean 待显示数据
     */
    public RichTextView setRichText(RichBean richBean) {
        setRichText(richBean, true);
        return this;
    }

    /**
     * 显示数据
     *
     * @param richBean    待显示数据
     * @param isOpenCache 是否开始图片缓存 默认缓存
     */
    public RichTextView setRichText(RichBean richBean, boolean isOpenCache) {
        ArrayList<RichBean> datas = new ArrayList<>();
        datas.add(richBean);
        setRichText(datas, isOpenCache);
        return this;
    }

    /**
     * 展示数据到TextView上，图片预显示为：多空格·多空格，总宽度与图片大小一致  或者  空格i空格
     *
     * @param datas 待显示数据
     */
    public RichTextView setRichText(ArrayList<RichBean> datas) {
        setRichText(datas, true);
        return this;
    }

    /**
     * 展示数据到TextView上，图片预显示为：多空格·多空格，总宽度与图片大小一致  或者  空格i空格
     *
     * @param datas       待显示数据
     * @param isOpenCache 是否开始图片缓存 默认缓存
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, boolean isOpenCache) {
        resetData();
        this.isOpenImgCache = isOpenCache;
        if (datas != null && datas.size() > 0) {
            richBeanList = datas;
            if (richTvWidth > 0) {
                updateRichTvData1();
            } else {
                ViewTreeObserver vto = getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        richTvWidth = getMeasuredWidth();
                        updateRichTvData1();
                    }
                });
            }
        }
        return this;
    }

    /**
     * 展示数据到TextView上，图片没预显示
     *
     * @param datas      待显示数据
     * @param defaultRes 默认预显示图片
     */

    public RichTextView setRichText(ArrayList<RichBean> datas, int defaultRes) {
        return setRichText(datas, defaultRes, true);
    }

    /**
     * 展示数据到TextView上，图片没预显示
     *
     * @param datas       待显示数据
     * @param defaultRes  默认预显示图片
     * @param isOpenCache 是否开启图片缓存 默认true
     */

    public RichTextView setRichText(ArrayList<RichBean> datas, int defaultRes, boolean isOpenCache) {
        resetData();
        this.isOpenImgCache = isOpenCache;
        return setRichText(datas, getResources().getDrawable(defaultRes), isOpenCache);
    }

    /**
     * 展示数据到TextView上，图片预显示
     *
     * @param datas    待显示数据
     * @param drawable 默认预显示图片
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, final Drawable drawable) {
        setRichText(datas, drawable, true);
        return this;
    }

    /**
     * 展示数据到TextView上，图片预显示
     *
     * @param datas       待显示数据
     * @param drawable    默认预显示图片
     * @param isOpenCache 是否开启图片缓存 默认true
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, final Drawable drawable, boolean isOpenCache) {
        resetData();
        this.isOpenImgCache = isOpenCache;
        if (datas != null && datas.size() > 0) {
            richBeanList = datas;
            defaultDrawable = drawable;
            if (richTvWidth > 0) {
                updateRichTvData2();
            } else {
                ViewTreeObserver vto = getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        richTvWidth = getMeasuredWidth();
                        updateRichTvData2();
                    }
                });
            }
        }
        return this;
    }

    // 更新数据，刷新界面
    private synchronized void updateRichTvData1() {
        float size = getTextSize();
        if (tempList == null)
            tempList = new ArrayList<>();
        tempList.clear();

        // 初始化文本
        if (imgTextList == null)
            imgTextList = new ArrayList<>();
        imgTextList.clear();

        // 循环遍历获取文本
        for (int i = 0; i < richBeanList.size(); i++) {
            final RichBean data = richBeanList.get(i);
            if (onLatexClickSpanListener == null)
                onLatexClickSpanListener = data.getOnLatexClickSpan();
            if (data.getType() == 0) {// 文本
                tempList.add(data.getText());
            } else {// 图片
                loadImgTatol++;
                StringBuilder strBuilder = new StringBuilder();
                // 设置占位符 - 要是唯一值
                if (data.getWidth() > 0) {
                    int width = DensityUtil.px2sp(getContext(), data.getWidth());
                    int num = (int) (width / size);
                    // 判断num是否已存在
                    numExist(num);
                    // 设置唯一值
                    for (int j = 0; j < num; j++)
                        strBuilder.append("\t");
                    strBuilder.append("·");
                    for (int j = 0; j < num; j++)
                        strBuilder.append("\t");
                } else {
                    strBuilder.append("\t")
                            .append(i)
                            .append("\t");
                }
                tempList.add(strBuilder.toString());
            }
        }

        // 设置TextView
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : tempList)
            stringBuilder.append(str);
        richText = stringBuilder.toString();
        spannableString = new SpannableString(richText);
        setText(spannableString);
        dealWithLatex(richText);

        // 重新刷新数据
        updateRichTvView();

        // 循环遍历显示图片
        for (int i = 0; i < richBeanList.size(); i++) {
            final RichBean data = richBeanList.get(i);
            if (data.getType() == 0)
                continue;
            downLoadImage(data, true);
        }
    }

    // 判断num是否已存在
    private void numExist(int num) {
        if (imgTextList == null)
            imgTextList = new ArrayList<>();
        if (imgTextList.contains(num)) {
            num = num + 1;
            numExist(num);
        } else {
            imgTextList.add(num);
        }
    }

    /**
     * 更新RichTv - 处理文本点击事件
     */
    private synchronized void updateRichTvView() {
        if (spannableString != null && tempList != null) {
            for (int j = 0; j < richBeanList.size(); j++) {
                RichBean richBean = richBeanList.get(j);
                updateRichBean(richBean, j);
            }
            // 刷新richTv
            setText(spannableString);
        }
    }

    // 修改文本数据
    private synchronized void updateRichBean(RichBean richBean, int position) {
        // 添加文本点击事件
        if ((richBean.getOnClickSpan() != null
                || !TextUtils.isEmpty(richBean.getBackgroundColor())
                || !TextUtils.isEmpty(richBean.getColor()))
                && richBean.getType() == 0
                && spannableString != null) {
            String text = richBean.getText();
            if (tempList.contains(text)) {
                int startPosition = 0;// 标记realText的开始位置
                for (int i = 0; i < tempList.size(); i++) {
                    if (i == position) break;
                    else startPosition += tempList.get(i).length();
                }
                if (startPosition >= 0 && startPosition < spannableString.length()) {
                    int endPosition = startPosition + text.length();

                    // 点击事件
                    if (richBean.getOnClickSpan() != null) {
                        ClickSpan clickSpan = new ClickSpan(text);
                        spannableString.setSpan(clickSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // 设置具体的点击事件
                        clickSpan.setOnClickSpan(richBean.getOnClickSpan());
                    }

                    setTextSpan(richBean, startPosition, endPosition);
                }
            }
        }
    }

    /**
     * 更新单项文本Item
     *
     * @param richBean 待更新数据
     * @param position 待更新项
     */
    private synchronized void updateRichTvItemView(RichBean richBean, int position) {
        updateRichBean(richBean, position);
        // 刷新richTv
        setText(spannableString);
    }

    /**
     * 更新RichTv - 处理图片以及图片点击事件
     */
    private synchronized void updateRichImgView() {
        for (int j = 0; j < richBeanList.size(); j++) {
            RichBean richBean = richBeanList.get(j);
            if (richBean.getRichImgBean() != null) // 加载图片
                updateRichTvImgSpan(richBean.getRichImgBean(), j);
        }
    }

    /**
     * 更新RichTv - ImgSpan
     */
    private synchronized void updateRichTvImgSpan(RichImgBean richImgBean, int position) {
        if (richImgBean != null
                && spannableString != null
                && tempList != null) {
            int startPosition = 0;// 标记realText的开始位置
            for (int i = 0; i < tempList.size(); i++) {
                if (i == position) break;
                else startPosition += tempList.get(i).length();
            }
            if (startPosition >= 0
                    && startPosition < spannableString.length()
                    && position < tempList.size()) {
                try {
                    VerticalImageSpan verticalImageSpan = richImgBean.getVerticalImageSpan();
                    String realText = tempList.get(position);
                    int endPosition = startPosition + realText.length();

                    // 设置点击事件
                    if (richImgBean.getOnClickSpan() != null
                            || onImageSpanClickListener != null) {
                        ClickSpan clickSpan = new ClickSpan(richImgBean.getRealText());
                        spannableString.setSpan(clickSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // 设置具体的点击事件
                        if (richImgBean.getOnClickSpan() != null)
                            clickSpan.setOnClickSpan(richImgBean.getOnClickSpan());
                        else if (onImageSpanClickListener != null)
                            clickSpan.setOnClickSpan(onImageSpanClickListener);
                    }

                    // 显示图片
                    spannableString.setSpan(verticalImageSpan,
                            startPosition, endPosition,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // 刷新richTv
                    setText(spannableString);
                } catch (Exception e) {
                    Log.d("updateRichTvImgSpan", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新RichTv
     */
    private synchronized void updateRichTvImgView() {
        if (spannableString != null && tempList != null) {
            for (int j = 0; j < richBeanList.size(); j++) {
                RichBean richBean = richBeanList.get(j);
                if (richBean.getRichImgBean() != null) {// 加载图片
                    updateRichTvImgSpan(richBean.getRichImgBean(), j);
                } else {
                    if (richBean.getOnClickSpan() != null
                            || !TextUtils.isEmpty(richBean.getBackgroundColor())
                            || !TextUtils.isEmpty(richBean.getColor())) {
                        String text = richBean.getText();
                        if (tempList.contains(text)) {
                            int startPosition = 0;// 标记realText的开始位置
                            for (int i = 0; i < tempList.size(); i++) {
                                if (i == j) break;
                                else startPosition += tempList.get(i).length();
                            }
                            if (startPosition >= 0 && startPosition < spannableString.length()) {
                                int endPosition = startPosition + text.length();

                                // 点击事件
                                if (richBean.getOnClickSpan() != null) {
                                    ClickSpan clickSpan = new ClickSpan(text);
                                    spannableString.setSpan(clickSpan,
                                            startPosition, endPosition,
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    // 设置具体的点击事件
                                    clickSpan.setOnClickSpan(richBean.getOnClickSpan());
                                }

                                setTextSpan(richBean, startPosition, endPosition);
                            }
                        }
                    }
                }
            }
            // 刷新richTv
            setText(spannableString);
        }
    }

    /**
     * 清空背景颜色
     */
    public synchronized RichTextView clearBackgroundColor() {
        if (spannableString != null) {
            // 移除EHeightRoundBackgroundColorSpan
            EHeightRoundBackgroundColorSpan[] eHeightRoundBackgroundColorSpans = spannableString.getSpans(0, spannableString.length(), EHeightRoundBackgroundColorSpan.class);
            for (EHeightRoundBackgroundColorSpan span : eHeightRoundBackgroundColorSpans)
                spannableString.removeSpan(span);
            // 修改图片背景
            updateBackgroundColor("#00FFFFFF", 0, spannableString.length(), 1);
        }
        return this;
    }

    /**
     * 重置背景颜色
     */
    public synchronized RichTextView resetBackgroundColor() {
        // 清空背景
        clearBackgroundColor();
        // 修改背景
        Drawable drawable = getBackground();
        if (drawable != null && spannableString != null) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            int color = colorDrawable.getColor();
            // 修改图片背景
            updateBackgroundColor(color, 0, spannableString.length(), 1);
        }
        return this;
    }

    /**
     * 重置文本颜色-包含图片部分
     */
    public synchronized RichTextView resetForegroundColor() {
        if (spannableString != null) {
            int color = getCurrentTextColor();
            updateForegroundColor(color, 0, spannableString.length());
        }
        return this;
    }

    /**
     * 重置文本颜色-非图片部分
     */
    public synchronized RichTextView resetTextColor() {
        if (spannableString != null) {
            int color = getCurrentTextColor();
            setTextColor(color);
            updateTextColor(color, 0, spannableString.length());
        }
        return this;
    }

    /**
     * 文本背景
     *
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改
     */
    public synchronized RichTextView updateBackgroundColor(final String backgroundColor,
                                                           final int startPosition,
                                                           final int endPosition,
                                                           final int updateBackGroundColorModel) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition <= spannableString.length()
                    && endPosition <= spannableString.length()
                    && startPosition <= endPosition
                    && !TextUtils.isEmpty(backgroundColor)) {
                try {
                    if (updateBackGroundColorModel == 0) {
                        EHeightRoundBackgroundColorSpan eHeightRoundBackgroundColorSpan = new EHeightRoundBackgroundColorSpan(startPosition, endPosition, Color.parseColor(backgroundColor));
                        spannableString.setSpan(eHeightRoundBackgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (updateBackGroundColorModel == 1) {
                        // 只适用于文本
                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(backgroundColor));
                        spannableString.setSpan(backgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        VerticalImageSpan[] verticalImageSpans = spannableString.getSpans(startPosition, endPosition, VerticalImageSpan.class);
                        if (verticalImageSpans != null && verticalImageSpans.length > 0) {
                            for (VerticalImageSpan verticalImageSpan : verticalImageSpans) {
                                verticalImageSpan.getDrawable().getIntrinsicHeight();
                                // DST_OVER 背景-DARKEN
                                verticalImageSpan.getDrawable().setColorFilter(Color.parseColor(backgroundColor),
                                        PorterDuff.Mode.DARKEN);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setText(spannableString);
            }
        } else {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateBackgroundColor(backgroundColor, startPosition, endPosition, updateBackGroundColorModel);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 文本背景2
     *
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改
     */
    public synchronized RichTextView updateBackgroundColor(final int backgroundColor,
                                                           final int startPosition,
                                                           final int endPosition,
                                                           final int updateBackGroundColorModel) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition <= spannableString.length()
                    && endPosition <= spannableString.length()
                    && startPosition <= endPosition) {
                try {
                    if (updateBackGroundColorModel == 0) {
                        EHeightRoundBackgroundColorSpan eHeightRoundBackgroundColorSpan = new EHeightRoundBackgroundColorSpan(startPosition, endPosition, backgroundColor);
                        spannableString.setSpan(eHeightRoundBackgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (updateBackGroundColorModel == 1) {
                        // 只适用文本
                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor);
                        spannableString.setSpan(backgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        VerticalImageSpan[] verticalImageSpans = spannableString.getSpans(startPosition, endPosition, VerticalImageSpan.class);
                        if (verticalImageSpans != null && verticalImageSpans.length > 0) {
                            for (VerticalImageSpan verticalImageSpan : verticalImageSpans) {
                                // DST_OVER 背景-DARKEN
                                verticalImageSpan.getDrawable().setColorFilter(backgroundColor,
                                        PorterDuff.Mode.DARKEN);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("RichTextView", e.getMessage());
                }
                setText(spannableString);
            }
        } else {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateBackgroundColor(backgroundColor, startPosition, endPosition, updateBackGroundColorModel);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 更新文本圆角背景
     *
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改
     */
    public synchronized RichTextView updateBackgroundColorRound(
            final String backgroundColor,
            final int radius,
            final int startPosition,
            final int endPosition,
            final int updateBackGroundColorModel) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition <= spannableString.length()
                    && endPosition <= spannableString.length()
                    && startPosition <= endPosition
                    && !TextUtils.isEmpty(backgroundColor)) {
                try {
                    if (updateBackGroundColorModel == 0) {
                        EHeightRoundBackgroundColorSpan eHeightRoundBackgroundColorSpan = new EHeightRoundBackgroundColorSpan(startPosition, endPosition, Color.parseColor(backgroundColor), radius);
                        spannableString.setSpan(eHeightRoundBackgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (updateBackGroundColorModel == 1) {
                        // 只适合文字部分
                        RoundBackgroundColorSpan backgroundColorSpan = new RoundBackgroundColorSpan(Color.parseColor(backgroundColor), getCurrentTextColor(), radius);
                        spannableString.setSpan(backgroundColorSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("RichTextView", e.getMessage());
                }
                setText(spannableString);
            }
        } else {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateBackgroundColorRound(backgroundColor, radius, startPosition, endPosition, updateBackGroundColorModel);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 文本颜色
     */
    public synchronized RichTextView updateForegroundColor(final String color, final int startPosition, final int endPosition) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition <= spannableString.length()
                    && endPosition <= spannableString.length()
                    && startPosition <= endPosition
                    && !TextUtils.isEmpty(color)) {
                try {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor(color));
                    spannableString.setSpan(foregroundColorSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    VerticalImageSpan[] verticalImageSpans = spannableString.getSpans(startPosition, endPosition, VerticalImageSpan.class);
                    if (verticalImageSpans != null && verticalImageSpans.length > 0) {
                        for (VerticalImageSpan verticalImageSpan : verticalImageSpans) {
                            Bitmap bitmap = verticalImageSpan.getBitmap();
                            if (bitmap != null) {
                                Canvas canvas = new Canvas(bitmap);
                                Paint paint1 = new Paint();
                                paint1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN));
                                canvas.drawBitmap(bitmap, 0, 0, paint1);
                            } else {
                                // SRC_ATOP 颜色-MULTIPLY
                                verticalImageSpan.getDrawable().setColorFilter(Color.parseColor(color),
                                        PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setText(spannableString);
            }
        } else {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateForegroundColor(color, startPosition, endPosition);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 文本颜色2
     */
    public synchronized RichTextView updateForegroundColor(final int color, final int startPosition, final int endPosition) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition <= spannableString.length()
                    && endPosition <= spannableString.length()
                    && startPosition <= endPosition) {
                try {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                    spannableString.setSpan(foregroundColorSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    VerticalImageSpan[] verticalImageSpans = spannableString.getSpans(startPosition, endPosition, VerticalImageSpan.class);
                    if (verticalImageSpans != null && verticalImageSpans.length > 0) {
                        for (VerticalImageSpan verticalImageSpan : verticalImageSpans) {
                            Bitmap bitmap = verticalImageSpan.getBitmap();
                            if (bitmap != null) {
                                Canvas canvas = new Canvas(bitmap);
                                Paint paint1 = new Paint();
                                paint1.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                                canvas.drawBitmap(bitmap, 0, 0, paint1);
                            } else {
                                // SRC_ATOP 颜色-MULTIPLY
                                verticalImageSpan.getDrawable().setColorFilter(color,
                                        PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setText(spannableString);
            }
        } else {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateForegroundColor(color, startPosition, endPosition);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 文本颜色3
     */
    public synchronized RichTextView updateTextColor(int color, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            try {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                spannableString.setSpan(foregroundColorSpan,
                        startPosition, endPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setText(spannableString);
        }
        return this;
    }

    /**
     * 文本颜色4
     */
    public synchronized RichTextView updateTextColor(String color, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && !TextUtils.isEmpty(color)) {
            try {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor(color));
                spannableString.setSpan(foregroundColorSpan,
                        startPosition, endPosition,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setText(spannableString);
        }
        return this;
    }

    /**
     * 添加超链接
     */
    public synchronized RichTextView updateURL(String addUrl, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && !TextUtils.isEmpty(addUrl)) {
            URLSpan urlSpan = new URLSpan(addUrl);
            spannableString.setSpan(urlSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            try {
//                if (!TextUtils.isEmpty(richBean.getAddUrlColor()))
//                    setHighlightColor(Color.parseColor(richBean.getAddUrlColor()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            setText(spannableString);
        }
        return this;
    }

    /**
     * 字体倍数
     */
    public synchronized RichTextView updateRelativeSize(float textSizeMultiple, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(textSizeMultiple);
            spannableString.setSpan(relativeSizeSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否添加删除线
     */
    public synchronized RichTextView updateUnderline(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            UnderlineSpan underlineSpan = new UnderlineSpan();
            spannableString.setSpan(underlineSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否添加删除线
     */
    public synchronized RichTextView updateStrikethrough(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            spannableString.setSpan(strikethroughSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否为上标
     */
    public synchronized RichTextView updateSuperscript(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            SuperscriptSpan superscriptSpan = new SuperscriptSpan();
            spannableString.setSpan(superscriptSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否为下标
     */
    public synchronized RichTextView updateSubscript(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            SubscriptSpan subscriptSpan = new SubscriptSpan();
            spannableString.setSpan(subscriptSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否加粗
     */
    public synchronized RichTextView updateStyleBold(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否斜体
     */
    public synchronized RichTextView updateStyleItalic(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
            spannableString.setSpan(styleSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 是否加粗并斜体
     */
    public synchronized RichTextView updateStyleBoldItalic(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
            spannableString.setSpan(styleSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * X轴缩放倍数
     */
    public synchronized RichTextView updateScaleX(float scaleXMultiple, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            ScaleXSpan scaleXSpan = new ScaleXSpan(scaleXMultiple);
            spannableString.setSpan(scaleXSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 添加边框
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param borderColor   边框颜色
     */
    public synchronized RichTextView updateBorder(int startPosition, int endPosition, int borderColor) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            BorderSpan borderSpan = new BorderSpan(borderColor);
            spannableString.setSpan(borderSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 添加模糊效果
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param radius        模糊半径，越大越模糊
     * @param style         样式
     */
    public synchronized RichTextView updateBlurMaskFilter(int startPosition, int endPosition, float radius, BlurMaskFilter.Blur style) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && style != null) {
            BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius, style);
            spannableString.setSpan(blurMaskFilter,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 浮雕效果
     */
    public synchronized RichTextView updateEmbossMaskFilter(int startPosition, int endPosition,
                                                            float[] direction, float ambient, float specular, float blurRadius) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && direction != null) {
            EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
            spannableString.setSpan(embossMaskFilter,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 文本插入图片
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param drawable      待插入图片
     * @param pad           内边距
     */
    public synchronized RichTextView updateDrawableMargin(int startPosition, int endPosition,
                                                          Drawable drawable, int pad) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && drawable != null) {
            DrawableMarginSpan drawableMarginSpan = new DrawableMarginSpan(drawable, pad);
            spannableString.setSpan(drawableMarginSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 添加竖线
     *
     * @param startPosition 开始位置
     * @param endPosition   解释位置
     * @param quoteColor    竖线颜色
     */
    public synchronized RichTextView updateQuote(int startPosition, int endPosition, int quoteColor) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            QuoteSpan quoteSpan = new QuoteSpan(quoteColor);
            spannableString.setSpan(quoteSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 绝对大小
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param size          大小
     * @param dip           是否为dp
     */
    public synchronized RichTextView updateAbsoluteSize(int startPosition, int endPosition, int size, boolean dip) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && size > 0) {
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, dip);
            spannableString.setSpan(absoluteSizeSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置图片，基于文本基线或底部对齐
     *
     * @param startPosition     开始位置
     * @param endPosition       结束位置
     * @param verticalAlignment 位置 DynamicDrawableSpan.ALIGN_BASELINE、 DynamicDrawableSpan.ALIGN_BOTTOM
     * @param drawable          带显示图片
     */
    public synchronized RichTextView updateDynamicDrawable(int startPosition, int endPosition, int verticalAlignment, final Drawable drawable) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && drawable != null) {
            DynamicDrawableSpan dynamicDrawableSpan = new DynamicDrawableSpan(verticalAlignment) {
                @Override
                public Drawable getDrawable() {
                    return drawable;
                }
            };
            spannableString.setSpan(dynamicDrawableSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 插入图片
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param bitmap        待插入bitmap
     * @param pad           内边距
     */
    public synchronized RichTextView updateIconMargin(int startPosition, int endPosition, Bitmap bitmap, int pad) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && bitmap != null) {
            IconMarginSpan iconMarginSpan = new IconMarginSpan(bitmap, pad);
            spannableString.setSpan(iconMarginSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置文本方向
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param align         方向
     */
    public synchronized RichTextView updateAlignmentStandard(int startPosition, int endPosition, Layout.Alignment align) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && align != null) {
            AlignmentSpan.Standard standard = new AlignmentSpan.Standard(align);
            spannableString.setSpan(standard,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 修改文本样式
     */
    public synchronized RichTextView updateTextAppearance(int startPosition, int endPosition,
                                                          String family, int style, int size, ColorStateList color, ColorStateList linkColor) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && !TextUtils.isEmpty(family)
                && color != null
                && linkColor != null) {
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(family, style, size, color, linkColor);
            spannableString.setSpan(textAppearanceSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 修改文本样式2
     */
    public synchronized RichTextView updateTextAppearance(int startPosition, int endPosition,
                                                          int appearance, int colorList) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), appearance, colorList);
            spannableString.setSpan(textAppearanceSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 修改文本样式3
     */
    public synchronized RichTextView updateTextAppearance(int startPosition, int endPosition,
                                                          int appearance) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), appearance);
            spannableString.setSpan(textAppearanceSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 文本字体
     */
    public synchronized RichTextView updateTypeface(int startPosition, int endPosition, String family) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && !TextUtils.isEmpty(family)) {
            TypefaceSpan typefaceSpan = new TypefaceSpan(family);
            spannableString.setSpan(typefaceSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 文本字体2
     */
    @RequiresApi(api = 28)
    public synchronized RichTextView updateTypeface(int startPosition, int endPosition, Typeface typeface) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && typeface != null) {
            TypefaceSpan typefaceSpan = new TypefaceSpan(typeface);
            spannableString.setSpan(typefaceSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置颜色数组
     */
    public synchronized RichTextView updateColors(int startPosition, int endPosition, int[] colors) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && colors != null) {
            ColorsSpan colorsSpan = new ColorsSpan(colors);
            spannableString.setSpan(colorsSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 段落层次-小圆点
     */
    public synchronized RichTextView updateBullet(int startPosition, int endPosition, int gapWidth, int color) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            BulletSpan bulletSpan = new BulletSpan(gapWidth, color);
            spannableString.setSpan(bulletSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 段落层次-小圆点2
     */
    public synchronized RichTextView updateBullet(int startPosition, int endPosition, int gapWidth) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            BulletSpan bulletSpan = new BulletSpan(gapWidth);
            spannableString.setSpan(bulletSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 段落层次-小圆点3
     */
    @RequiresApi(api = 28)
    public synchronized RichTextView updateBullet(int startPosition, int endPosition, int gapWidth, int color, int bulletRadius) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            BulletSpan bulletSpan = new BulletSpan(gapWidth, color, bulletRadius);
            spannableString.setSpan(bulletSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置文本环绕
     */
    public synchronized RichTextView updateTextRound(int startPosition, int endPosition, int lines, int margin) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            TextRoundSpan textRoundSpan = new TextRoundSpan(lines, margin);
            spannableString.setSpan(textRoundSpan,
                    startPosition, endPosition,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置span
     */
    public synchronized RichTextView setSpan(Object what, int startPosition, int endPosition, int flags) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition
                && what != null) {
            spannableString.setSpan(what,
                    startPosition, endPosition,
                    flags);
            setText(spannableString);
        }
        return this;
    }

    /**
     * 设置Span事件
     */
    private synchronized void setTextSpan(RichBean richBean, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition <= spannableString.length()
                && endPosition <= spannableString.length()
                && startPosition <= endPosition) {
            // 文本背景
            if (!TextUtils.isEmpty(richBean.getBackgroundColor())) {
                try {
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(richBean.getBackgroundColor()));
                    spannableString.setSpan(backgroundColorSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 文本颜色
            if (!TextUtils.isEmpty(richBean.getColor())) {
                try {
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor(richBean.getColor()));
                    spannableString.setSpan(foregroundColorSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 添加超链接
            if (!TextUtils.isEmpty(richBean.getAddUrl())) {
                URLSpan urlSpan = new URLSpan(richBean.getAddUrl());
                spannableString.setSpan(urlSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            try {
//                if (!TextUtils.isEmpty(richBean.getAddUrlColor()))
//                    setHighlightColor(Color.parseColor(richBean.getAddUrlColor()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            }
            // 字体倍数
            if (richBean.getTextSizeMultiple() > 0) {
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(richBean.getTextSizeMultiple());
                spannableString.setSpan(relativeSizeSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否添加删除线
            if (richBean.isUnderline()) {
                UnderlineSpan underlineSpan = new UnderlineSpan();
                spannableString.setSpan(underlineSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否添加删除线
            if (richBean.isStrikethrough()) {
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                spannableString.setSpan(strikethroughSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否为上标
            if (richBean.isSuperscript()) {
                SuperscriptSpan superscriptSpan = new SuperscriptSpan();
                spannableString.setSpan(superscriptSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否为下标
            if (richBean.isSubscript()) {
                SubscriptSpan subscriptSpan = new SubscriptSpan();
                spannableString.setSpan(subscriptSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否加粗
            if (richBean.isBold()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否斜体
            if (richBean.isItalic()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                spannableString.setSpan(styleSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 是否加粗并斜体
            if (richBean.isBoldItalic()) {
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD_ITALIC);
                spannableString.setSpan(styleSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // X轴缩放倍数
            if (richBean.getScaleXMultiple() > 0) {
                ScaleXSpan scaleXSpan = new ScaleXSpan(richBean.getScaleXMultiple());
                spannableString.setSpan(scaleXSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 添加边框
            if (!TextUtils.isEmpty(richBean.getBorderColor())) {
                BorderSpan borderSpan = new BorderSpan(richBean.getBorderColor());
                spannableString.setSpan(borderSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 待插入图片
            if (richBean.getDrawable() != null) {
                DrawableMarginSpan drawableMarginSpan = new DrawableMarginSpan(richBean.getDrawable(), richBean.getPad());
                spannableString.setSpan(drawableMarginSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 插入竖线
            if (!TextUtils.isEmpty(richBean.getQuoteColor())) {
                try {
                    QuoteSpan quoteSpan = new QuoteSpan(Color.parseColor(richBean.getQuoteColor()));
                    spannableString.setSpan(quoteSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 绝对大小
            if (richBean.getAbsoluteSize() > 0) {
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(richBean.getAbsoluteSize(), richBean.isAbsoluteSizeDip());
                spannableString.setSpan(absoluteSizeSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 插入图片
            if (richBean.getBitmap() != null) {
                IconMarginSpan iconMarginSpan = new IconMarginSpan(richBean.getBitmap(), richBean.getBitmapPad());
                spannableString.setSpan(iconMarginSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 文本方向
            if (richBean.getAlign() != null) {
                AlignmentSpan.Standard standard = new AlignmentSpan.Standard(richBean.getAlign());
                spannableString.setSpan(standard,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 字体样式
            if (!TextUtils.isEmpty(richBean.getFamily())) {
                TypefaceSpan typefaceSpan = new TypefaceSpan(richBean.getFamily());
                spannableString.setSpan(typefaceSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            // 设置颜色数组
            if (richBean.getColors() != null) {
                ColorsSpan colorsSpan = new ColorsSpan(richBean.getColors());
                spannableString.setSpan(colorsSpan,
                        startPosition, endPosition,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 下载图片
     *
     * @param data   待处理数据
     * @param isSign 是否需要标记 图片加载次数
     */
    private synchronized void downLoadImage(final RichBean data, final boolean isSign) {
        try {
            if (data != null) {
                Object object = data.getText();
                if (TextUtils.isEmpty(data.getText()))
                    object = data.getRes();
                if (loadImgModel == 0 || TextUtils.isEmpty(data.getText())) {
                    // 采用Glide加载图片
                    Glide.with(getContext())
                            .load(object)
                            .dontAnimate()
                            .skipMemoryCache(!isOpenImgCache)
                            .diskCacheStrategy(!isOpenImgCache ? DiskCacheStrategy.NONE : DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<GlideDrawable>() {

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    // 判断图片是否全部加载完成
                                    if (isSign) {
                                        if (loadImgTatol > loadImgComplete)
                                            loadImgComplete++;
                                    }
                                }

                                @Override
                                public void onResourceReady(GlideDrawable drawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    RichImgBean richImgBean = new RichImgBean();
                                    richImgBean.setOnClickSpan(data.getOnClickSpan());
                                    richImgBean.setRealText(data.getText());
                                    richImgBean.setRes(data.getRes());

                                    int width = data.getWidth();
                                    int height = data.getHeight();
                                    if (width <= 0)
                                        width = drawable.getIntrinsicWidth();
                                    if (height <= 0)
                                        height = drawable.getIntrinsicHeight();

                                    // 图片宽度大于文本控件的宽度设置点击事件
                                    if (richTvWidth < width && width != 0) {
                                        float fl = new BigDecimal((float) richTvWidth / width)
                                                .setScale(5, BigDecimal.ROUND_HALF_UP)
                                                .floatValue();
                                        height = (int) (fl * height) + 1;
                                        width = richTvWidth;
                                        if (onLongImageSpanClickListener != null)
                                            richImgBean.setOnClickSpan(onLongImageSpanClickListener);
                                    }

                                    drawable.setBounds(0, 0, width, height);
                                    try {
                                        if (!TextUtils.isEmpty(data.getColor())) {
                                            // SRC_ATOP 颜色-MULTIPLY
                                            drawable.setColorFilter(Color.parseColor(data.getColor()),
                                                    PorterDuff.Mode.MULTIPLY);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (!TextUtils.isEmpty(data.getBackgroundColor())) {
                                            // DST_OVER 背景-DARKEN
                                            drawable.setColorFilter(Color.parseColor(data.getBackgroundColor()),
                                                    PorterDuff.Mode.DARKEN);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
                                    richImgBean.setVerticalImageSpan(verticalImageSpan);
                                    data.setRichImgBean(richImgBean);

                                    // 重新刷新数据
                                    updateRichImgView();

                                    // 判断图片是否全部加载完成
                                    if (isSign) {
                                        if (loadImgTatol > loadImgComplete)
                                            loadImgComplete++;
                                    }
                                }
                            });
                } else if (loadImgModel == 1) {
                    // 采用DownLoadImage加载图片
                    DownLoadImage.getInstance().loadImage(data.getText(), isOpenImgCache, new DownLoadImage.ImageCallBack() {
                        @Override
                        public void getDrawable(Drawable drawable) {
                            RichImgBean richImgBean = new RichImgBean();
                            richImgBean.setOnClickSpan(data.getOnClickSpan());
                            richImgBean.setRealText(data.getText());
                            richImgBean.setRes(data.getRes());

                            int width = drawable.getIntrinsicWidth();
                            int height = drawable.getIntrinsicHeight();

                            // 图片宽度大于文本控件的宽度设置点击事件
                            if (richTvWidth < width && width != 0) {
                                float fl = new BigDecimal((float) richTvWidth / width)
                                        .setScale(5, BigDecimal.ROUND_HALF_UP)
                                        .floatValue();
                                height = (int) (fl * height) + 1;
                                width = richTvWidth;
                                if (onLongImageSpanClickListener != null)
                                    richImgBean.setOnClickSpan(onLongImageSpanClickListener);
                            }

                            drawable.setBounds(0, 0, DensityUtil.dp2px(getContext(), width), DensityUtil.dp2px(getContext(), height));
                            try {
                                if (!TextUtils.isEmpty(data.getColor())) {
                                    // SRC_ATOP 颜色-MULTIPLY
                                    drawable.setColorFilter(Color.parseColor(data.getColor()),
                                            PorterDuff.Mode.MULTIPLY);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (!TextUtils.isEmpty(data.getBackgroundColor())) {
                                    // DST_OVER 背景-DARKEN
                                    drawable.setColorFilter(Color.parseColor(data.getBackgroundColor()),
                                            PorterDuff.Mode.DARKEN);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
                            richImgBean.setVerticalImageSpan(verticalImageSpan);
                            data.setRichImgBean(richImgBean);

                            // 重新刷新数据
                            updateRichImgView();

                            // 判断图片是否全部加载完成
                            if (isSign) {
                                if (loadImgTatol > loadImgComplete)
                                    loadImgComplete++;
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            // 判断图片是否全部加载完成
                            if (isSign) {
                                if (loadImgTatol > loadImgComplete)
                                    loadImgComplete++;
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新单项
     *
     * @param richBean 待更新数据
     * @param position 待更新项
     */
    public synchronized RichTextView updateItem(RichBean richBean, int position) {
        if (richBean != null
                && richBeanList != null
                && tempList != null
                && position < richBeanList.size()
                && tempList.size() == richBeanList.size()) {
            if (richBean.getType() == 0) {// 文本
                if (richBean.getText().equals(tempList.get(position))) {
                    // 更新数据
                    richBeanList.set(position, richBean);
                    // 样式变化
                    updateRichTvItemView(richBean, position);
                } else {
                    // 更新数据
                    richBeanList.set(position, richBean);
                    // 内容变化
                    isResetData = false;
                    setRichText(richBeanList, isOpenImgCache);
                }
            } else {// 图片
                downLoadImage(richBean, false);
            }
        }
        return this;
    }

    // 更新数据，刷新界面
    private synchronized void updateRichTvData2() {
        if (tempList == null)
            tempList = new ArrayList<>();
        tempList.clear();
        // 循环遍历获取文本
        for (int i = 0; i < richBeanList.size(); i++) {
            final RichBean data = richBeanList.get(i);
            if (onLatexClickSpanListener == null)
                onLatexClickSpanListener = data.getOnLatexClickSpan();
            if (data.getType() == 0) {
                tempList.add(data.getText());
            } else {// 图片
                loadImgTatol++;
                // 设置占位符 - 空格
                tempList.add("\t");
                // 添加默认图片
                RichImgBean richImgBean = new RichImgBean();
                richImgBean.setOnClickSpan(data.getOnClickSpan());
                richImgBean.setRealText(data.getText());
                if (defaultDrawable != null) {
                    int width = defaultDrawable.getIntrinsicWidth();
                    int height = defaultDrawable.getIntrinsicHeight();
                    defaultDrawable.setBounds(0, 0, DensityUtil.dp2px(getContext(), width), DensityUtil.dp2px(getContext(), height));
                    VerticalImageSpan verticalImageSpan = new VerticalImageSpan(defaultDrawable);
                    richImgBean.setVerticalImageSpan(verticalImageSpan);
                }
                data.setRichImgBean(richImgBean);
            }
        }

        // 设置TextView
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : tempList)
            stringBuilder.append(str);
        richText = stringBuilder.toString();
        spannableString = new SpannableString(richText);
        setText(spannableString);
        dealWithLatex(richText);

        // 重新刷新数据
        updateRichTvImgView();

        // 循环遍历显示图片
        for (int i = 0; i < richBeanList.size(); i++) {
            final RichBean data = richBeanList.get(i);
            if (data.getType() == 0)
                continue;
            downLoadImage(data, true);
        }
    }

    // 处理LaTeX数学公式
    private synchronized RichTextView dealWithLatex(CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            String latexPattern = "(?i)\\$\\$?((.|\\n)+?)\\$\\$?";
            Pattern pattern = Pattern.compile(latexPattern);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                LatexBean latexBean = new LatexBean(matcher.group(), matcher.start(), matcher.end());
                latexBeanList.add(latexBean);
            }
            // 替换公式为空格
            if (isLatexOneStr) {
                String tempText = text.toString();
                for (LatexBean latexBean : latexBeanList) {
                    String latex = latexBean.getLatex();
                    int index = tempText.indexOf(latex);
                    tempText = tempText.replaceFirst(latexPattern, "\t");
                    latexBean.setStartPosition(index);
                    latexBean.setEndPosition(index + 1);
                }
                richText = tempText;
                spannableString = new SpannableString(richText);
            }
            // 加载公式图片
            for (LatexBean latexBean : latexBeanList) {
                String result = latexBean.getLatex().replaceAll("\\$+", "");
                Bitmap bitmap;
                if (isOpenImgCache) {
                    bitmap = BitmapCacheUtil.getInstanse().init().getBitmapByPath(result);
                    if (bitmap == null) {
                        bitmap = latexDrawable(result, latexBean);
                        if (bitmap != null)
                            BitmapCacheUtil.getInstanse().init().putBitmapByPath(result, bitmap);
                    }
                } else
                    bitmap = latexDrawable(result, latexBean);
                int startPosition = latexBean.getStartPosition();
                int endPosition = latexBean.getEndPosition();
                if (spannableString != null
                        && startPosition <= spannableString.length()
                        && endPosition <= spannableString.length()
                        && startPosition <= endPosition
                        && bitmap != null) {
                    // 显示图片
                    spannableString.setSpan(
                            new VerticalImageSpan(getContext(), bitmap),
                            startPosition,
                            endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 设置Latex点击事件
                    if (onLatexClickSpanListener != null) {
                        LatexClickSpan latexClickSpan = new LatexClickSpan(latexBean.getLatex(), bitmap);
                        latexClickSpan.setOnLatexClickSpan(onLatexClickSpanListener);
                        spannableString.setSpan(latexClickSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    // 设置长Latex点击事件
                    if (latexBean.getOnLatexClickSpanListener() != null) {
                        LatexClickSpan latexClickSpan = new LatexClickSpan(latexBean.getLatex(), bitmap);
                        latexClickSpan.setOnLatexClickSpan(latexBean.getOnLatexClickSpanListener());
                        spannableString.setSpan(latexClickSpan,
                                startPosition, endPosition,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        return this;
    }

    // 将latex文本转换为Bitmap
    private synchronized Bitmap latexDrawable(String latex, LatexBean latexBean) {
        TextPaint paint = getPaint();
        float density = paint.density;
        float textSize = paint.getTextSize();
        float proportion = textSize / density;

        TeXFormula formula = TeXFormula.getPartialTeXFormula(latex);
        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(proportion)
                .setWidth(TeXConstants.UNIT_SP, proportion, TeXConstants.ALIGN_LEFT)
                .setIsMaxWidth(true)
                .setInterLineSpacing(TeXConstants.UNIT_SP, AjLatexMath.getLeading(proportion))
                .build();
        icon.setInsets(new Insets(5, 5, 5, 5));

        Bitmap image = Bitmap.createBitmap(icon.getIconWidth(), icon.getIconHeight(),
                Bitmap.Config.ARGB_8888);

//        Canvas g2 = new Canvas(image);
//        g2.drawColor(Color.TRANSPARENT);
//        icon.paintIcon(g2, 0, 0);

        // 设置背景
        Canvas g2 = new Canvas(image);
        int drawColor = Color.TRANSPARENT;
        if (!TextUtils.isEmpty(backGroundColor)) {
            try {
                drawColor = Color.parseColor(backGroundColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g2.drawColor(drawColor);
        icon.paintIcon(g2, 0, 0);

        // 设置颜色
        if (!TextUtils.isEmpty(tintColor)) {
            try {
                Canvas canvas = new Canvas(image);
                Paint paint1 = new Paint();
                paint1.setColorFilter(new PorterDuffColorFilter(Color.parseColor(tintColor), PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(image, 0, 0, paint1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 如果image的宽度>RichTextView的宽度，改变image的大小
        if (image.getWidth() > richTvWidth) {
            image = Bitmap.createScaledBitmap(image, richTvWidth,
                    image.getHeight() * richTvWidth / image.getWidth(),
                    false);
            if (onLongLatexClickSpanListener != null)
                latexBean.setOnLatexClickSpanListener(onLongLatexClickSpanListener);
        }

        return image;

//        return new BitmapDrawable(getResources(), image);
//        return new BitmapDrawable( bitmap);
//        return bitmap;
    }

    /**
     * 图片点击回调接口
     */
    private ClickSpan.OnClickSpan onImageSpanClickListener;

    public RichTextView setOnImageSpanClickListener(ClickSpan.OnClickSpan onImageSpanClickListener) {
        this.onImageSpanClickListener = onImageSpanClickListener;
        return this;
    }

    /**
     * 长图点击事件
     */
    private ClickSpan.OnClickSpan onLongImageSpanClickListener;

    public RichTextView setOnLongImageSpanClickListener(ClickSpan.OnClickSpan onLongImageSpanClickListener) {
        this.onLongImageSpanClickListener = onLongImageSpanClickListener;
        return this;
    }

    /**
     * Latex公式点击事件
     */
    private LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener;

    public RichTextView setOnLatexClickSpan(LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener) {
        this.onLatexClickSpanListener = onLatexClickSpanListener;
        return this;
    }

    /**
     * Latex长公式点击事件
     */
    private LatexClickSpan.OnLatexClickSpan onLongLatexClickSpanListener;

    public RichTextView setOnLongLatexClickSpanListener(LatexClickSpan.OnLatexClickSpan onLongLatexClickSpanListener) {
        this.onLongLatexClickSpanListener = onLongLatexClickSpanListener;
        return this;
    }
}
