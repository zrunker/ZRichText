package cc.ibooker.richtext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
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
    private ArrayList<RichBean> list;
    private int richTvWidth;
    private Drawable defaultDrawable;

    private boolean isScroll;// 是否可以滚动
    private int richMaxLines;// 最大行数
    private int loadImgTatol = 0;// 待加载图片总数
    private int loadImgComplete = 0;// 已加载图片总数
    private boolean isOpenImgCache = true;// 是否开启图片缓存，默认开启
    private int loadImgModel = 0;// 加载图片模式，0-Glide，1-DownLoadImage，默认0

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
            typeArray.recycle();
        }
        setRichTvScroll();
    }


    // 设置是否能够滚动
    public void setScroll(boolean scroll) {
        this.isScroll = scroll;
        setRichTvScroll();
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

    private void setRichTvScroll() {
        if (isScroll && richMaxLines <= 0)
            setMovementMethod(LinkMovementMethod.getInstance());
        else
            setOnTouchListener(new LinkMovementMethodOverride());
    }

    // 销毁
    public void onDestory() {
        DownLoadImage.getInstance().deStory();
    }

    // 设置加载模式
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
        loadImgComplete = 0;
        loadImgTatol = 0;
        this.isOpenImgCache = isOpenCache;
        if (datas != null && datas.size() > 0) {
            list = datas;
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

    // 更新数据，刷新界面
    private void updateRichTvData1() {
        float size = getTextSize();
        if (tempList == null)
            tempList = new ArrayList<>();
        tempList.clear();

        // 初始化文本
        if (imgTextList == null)
            imgTextList = new ArrayList<>();
        imgTextList.clear();

        // 循环遍历获取文本
        for (int i = 0; i < list.size(); i++) {
            final RichBean data = list.get(i);
            if (data.getType() == 0) {// 文本
                tempList.add(data.getText());
            } else {// 图片
                loadImgTatol++;
                StringBuilder strBuilder = new StringBuilder();
                // 设置占位符 - 要是唯一值
                if (data.getWidth() > 0) {
                    int width = px2sp(getContext(), data.getWidth());
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
        spannableString = new SpannableString(stringBuilder.toString());

        // 重新刷新数据
        updateRichTvView();

        // 循环遍历显示图片
        for (int i = 0; i < list.size(); i++) {
            final RichBean data = list.get(i);
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
            for (int j = 0; j < list.size(); j++) {
                RichBean richBean = list.get(j);
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
                && richBean.getType() == 0) {
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
        for (int j = 0; j < list.size(); j++) {
            RichBean richBean = list.get(j);
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
            for (int j = 0; j < list.size(); j++) {
                RichBean richBean = list.get(j);
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
     * 文本背景
     */
    public RichTextView updateBackgroundColor(final String backgroundColor, final int startPosition, final int endPosition) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition < spannableString.length()
                    && endPosition < spannableString.length()
                    && startPosition <= endPosition
                    && !TextUtils.isEmpty(backgroundColor)) {
                try {
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(backgroundColor));
                    spannableString.setSpan(backgroundColorSpan,
                            startPosition, endPosition,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    VerticalImageSpan[] verticalImageSpans = spannableString.getSpans(startPosition, endPosition, VerticalImageSpan.class);
                    if (verticalImageSpans != null && verticalImageSpans.length > 0) {
                        for (VerticalImageSpan verticalImageSpan : verticalImageSpans) {
                            // DST_OVER 背景-DARKEN
                            verticalImageSpan.getDrawable().setColorFilter(Color.parseColor(backgroundColor),
                                    PorterDuff.Mode.DARKEN);
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
                    updateBackgroundColor(backgroundColor, startPosition, endPosition);
                }
            }, 100);
        }
        return this;
    }

    /**
     * 文本颜色
     */
    public RichTextView updateForegroundColor(final String color, final int startPosition, final int endPosition) {
        if (loadImgTatol == loadImgComplete) {
            if (spannableString != null
                    && startPosition < spannableString.length()
                    && endPosition < spannableString.length()
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
                            // SRC_ATOP 颜色-MULTIPLY
                            verticalImageSpan.getDrawable().setColorFilter(Color.parseColor(color),
                                    PorterDuff.Mode.MULTIPLY);
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
     * 添加超链接
     */
    public RichTextView updateURL(String addUrl, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateRelativeSize(float textSizeMultiple, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateUnderline(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateStrikethrough(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateSuperscript(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateSubscript(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateStyleBold(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateStyleItalic(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateStyleBoldItalic(int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
    public RichTextView updateScaleX(float scaleXMultiple, int startPosition, int endPosition) {
        if (spannableString != null
                && startPosition < spannableString.length()
                && endPosition < spannableString.length()
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
     * 设置Span事件
     */
    private void setTextSpan(RichBean richBean, int startPosition, int endPosition) {
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
    }

    /**
     * 下载图片
     *
     * @param data   待处理数据
     * @param isSign 是否需要标记 图片加载次数
     */
    private synchronized void downLoadImage(final RichBean data, final boolean isSign) {
        if (data != null) {
            Object object = data.getText();
            if (TextUtils.isEmpty(data.getText()))
                object = data.getRes();
            if (loadImgModel == 0) {
                // 采用Glide加载图片
                Glide.with(getContext())
                        .load(object)
                        .dontAnimate()
                        .skipMemoryCache(isOpenImgCache)
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

                        drawable.setBounds(0, 0, dp2px(getContext(), width), dp2px(getContext(), height));
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
    }

    /**
     * 更新单项
     *
     * @param richBean 待更新数据
     * @param position 待更新项
     */
    public synchronized RichTextView updateItem(RichBean richBean, int position) {
        if (richBean != null
                && list != null
                && tempList != null
                && position < list.size()
                && tempList.size() == list.size()) {
            if (richBean.getType() == 0) {// 文本
                if (richBean.getText().equals(tempList.get(position))) {
                    // 更新数据
                    list.set(position, richBean);
                    // 样式变化
                    updateRichTvItemView(richBean, position);
                } else {
                    // 更新数据
                    list.set(position, richBean);
                    // 内容变化
                    setRichText(list, isOpenImgCache);
                }
            } else {// 图片
                downLoadImage(richBean, false);
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
        loadImgComplete = 0;
        loadImgTatol = 0;
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
        loadImgComplete = 0;
        loadImgTatol = 0;
        this.isOpenImgCache = isOpenCache;
        if (datas != null && datas.size() > 0) {
            list = datas;
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
    private void updateRichTvData2() {
        if (tempList == null)
            tempList = new ArrayList<>();
        tempList.clear();
        // 循环遍历获取文本
        for (int i = 0; i < list.size(); i++) {
            final RichBean data = list.get(i);
            if (data.getType() == 0) {
                tempList.add(data.getText());
            } else {// 图片
                loadImgTatol++;
                // 设置占位符 - 唯一值
                tempList.add("" + i);
                // 添加默认图片
                RichImgBean richImgBean = new RichImgBean();
                richImgBean.setOnClickSpan(data.getOnClickSpan());
                richImgBean.setRealText(data.getText());
                if (defaultDrawable != null) {
                    int width = defaultDrawable.getIntrinsicWidth();
                    int height = defaultDrawable.getIntrinsicHeight();
                    defaultDrawable.setBounds(0, 0, dp2px(getContext(), width), dp2px(getContext(), height));
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
        spannableString = new SpannableString(stringBuilder.toString());

        // 重新刷新数据
        updateRichTvImgView();

        // 循环遍历显示图片
        for (int i = 0; i < list.size(); i++) {
            final RichBean data = list.get(i);
            if (data.getType() == 0)
                continue;
            downLoadImage(data, true);
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机分辨率从DP转成PX
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
}
