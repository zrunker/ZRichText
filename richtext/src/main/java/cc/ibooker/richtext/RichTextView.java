package cc.ibooker.richtext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 自定义富文本View
 */
public class RichTextView extends android.support.v7.widget.AppCompatTextView {
    private ArrayList<String> tempList;
    private SpannableString spannableString;
    private DownLoadImage downLoadImage;
    private ArrayList<Integer> imgTextList;

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHighlightColor(Color.TRANSPARENT);// 消除点击时的背景色
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    // 销毁
    public void onDestory() {
        if (downLoadImage != null)
            downLoadImage.destory();
    }

    /**
     * 展示数据到TextView上，图片预显示为：多空格·多空格，总宽度与图片大小一致  或者  空格i空格
     *
     * @param list 待显示数据
     */
    public RichTextView setRichText(final ArrayList<RichBean> list) {
        if (list != null && list.size() > 0) {
            ViewTreeObserver vto = getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    final int richTvWidth = getMeasuredWidth();
                    tempList = new ArrayList<>();
                    float size = getTextSize();

                    // 初始化文本
                    if (imgTextList == null)
                        imgTextList = new ArrayList<>();

                    // 循环遍历获取文本
                    for (int i = 0; i < list.size(); i++) {
                        final RichBean data = list.get(i);
                        if (data.getType() == 0) {// 文本
                            tempList.add(data.getText());
                        } else {// 图片
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
                    updateRichTv(list);

                    // 循环遍历显示图片
                    for (int i = 0; i < list.size(); i++) {
                        final RichBean data = list.get(i);
                        if (data.getType() == 0)
                            continue;
//                        // 采用Glide加载图片
//                        Glide.with(getContext())
//                                .load(data.getText())
//                                .into(new SimpleTarget<Drawable>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
//                                        RichImgBean richImgBean = new RichImgBean();
//                                        richImgBean.setOnClickSpan(data.getOnClickSpan());
//                                        richImgBean.setRealText(data.getText());
//
//                                        int width = drawable.getIntrinsicWidth() / 2;
//                                        int height = drawable.getIntrinsicHeight() / 2;
//
//                                        if (richTvWidth < width && width != 0) {
//                                            float fl = new BigDecimal((float) richTvWidth / width)
//                                                    .setScale(5, BigDecimal.ROUND_HALF_UP)
//                                                    .floatValue();
//                                            height = (int) (fl * height) + 1;
//                                            width = richTvWidth;
//                                            if (onLongImageSpanClickListener != null)
//                                                richImgBean.setOnClickSpan(onLongImageSpanClickListener);
//                                        }
//
//                                        drawable.setBounds(0, 0, width, height);
//                                        VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
//                                        richImgBean.setVerticalImageSpan(verticalImageSpan);
//                                        data.setRichImgBean(richImgBean);
//
//                                        // 重新刷新数据
//                                        updateRichTv(list);
//                                    }
//                                });
                        downLoadImage = new DownLoadImage();
                        downLoadImage.loadImage(data.getText(), new DownLoadImage.ImageCallBack() {
                            @Override
                            public void getDrawable(Drawable drawable) {
                                RichImgBean richImgBean = new RichImgBean();
                                richImgBean.setOnClickSpan(data.getOnClickSpan());
                                richImgBean.setRealText(data.getText());

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

                                drawable.setBounds(0, 0, width, height);
                                VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
                                richImgBean.setVerticalImageSpan(verticalImageSpan);
                                data.setRichImgBean(richImgBean);

                                // 重新刷新数据
                                updateRichImg(list);
                            }
                        });
                    }
                }
            });
        }
        return this;
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
    private synchronized void updateRichTv(ArrayList<RichBean> list) {
        if (spannableString != null && tempList != null) {
            for (int j = 0; j < list.size(); j++) {
                RichBean richBean = list.get(j);
                // 添加文本点击事件
                if ((richBean.getOnClickSpan() != null
                        || !TextUtils.isEmpty(richBean.getBackgroundColor())
                        || !TextUtils.isEmpty(richBean.getColor()))
                        && richBean.getType() == 0) {
                    String text = richBean.getText();
                    if (tempList.contains(text)) {
                        int startPosition = 0;// 标记realText的开始位置
                        for (int i = 0; i < tempList.size(); i++) {
                            if (i == j) break;
                            else startPosition += tempList.get(i).length();
                        }
                        if (startPosition >= 0 && startPosition < spannableString.length()) {
                            // 点击事件
                            if (richBean.getOnClickSpan() != null) {
                                ClickSpan clickSpan = new ClickSpan(text);
                                spannableString.setSpan(clickSpan,
                                        startPosition, startPosition + text.length(),
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                // 设置具体的点击事件
                                clickSpan.setOnClickSpan(richBean.getOnClickSpan());
                            }
                            // 文本背景
                            if (!TextUtils.isEmpty(richBean.getBackgroundColor())) {
                                try {
                                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(richBean.getBackgroundColor()));
                                    spannableString.setSpan(backgroundColorSpan,
                                            startPosition, startPosition + text.length(),
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
                                            startPosition, startPosition + text.length(),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
     * 更新RichTv - 处理图片以及图片点击事件
     */
    private synchronized void updateRichImg(ArrayList<RichBean> list) {
        for (int j = 0; j < list.size(); j++) {
            RichBean richBean = list.get(j);
            if (richBean.getRichImgBean() != null) // 加载图片
                updateRichTvImgSpan(richBean.getRichImgBean(), j);
        }
    }

    /**
     * 更新RichTv - ImgSpan
     */
    private void updateRichTvImgSpan(RichImgBean richImgBean, int position) {
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

                    // 设置点击事件
                    if (richImgBean.getOnClickSpan() != null
                            || onImageSpanClickListener != null) {
                        ClickSpan clickSpan = new ClickSpan(richImgBean.getRealText());
                        spannableString.setSpan(clickSpan,
                                startPosition, startPosition + realText.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        // 设置具体的点击事件
                        if (richImgBean.getOnClickSpan() != null)
                            clickSpan.setOnClickSpan(richImgBean.getOnClickSpan());
                        else if (onImageSpanClickListener != null)
                            clickSpan.setOnClickSpan(onImageSpanClickListener);
                    }

                    // 显示图片
                    spannableString.setSpan(verticalImageSpan,
                            startPosition, startPosition + realText.length(),
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
    private synchronized void updateRichTvImg(ArrayList<RichBean> list) {
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
                                // 点击事件
                                if (richBean.getOnClickSpan() != null) {
                                    ClickSpan clickSpan = new ClickSpan(text);
                                    spannableString.setSpan(clickSpan,
                                            startPosition, startPosition + text.length(),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    // 设置具体的点击事件
                                    clickSpan.setOnClickSpan(richBean.getOnClickSpan());
                                }
                                // 文本背景
                                if (!TextUtils.isEmpty(richBean.getBackgroundColor())) {
                                    try {
                                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor(richBean.getBackgroundColor()));
                                        spannableString.setSpan(backgroundColorSpan,
                                                startPosition, startPosition + text.length(),
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
                                                startPosition, startPosition + text.length(),
                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
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
     * 展示数据到TextView上，图片没预显示
     *
     * @param list 待显示数据
     */
    public RichTextView setRichText2(final ArrayList<RichBean> list, final Drawable defaultDrawable) {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final int richTvWidth = getMeasuredWidth();
                tempList = new ArrayList<>();
                // 循环遍历获取文本
                for (int i = 0; i < list.size(); i++) {
                    final RichBean data = list.get(i);
                    if (data.getType() == 0) {
                        tempList.add(data.getText());
                    } else {
                        // 设置占位符 - 唯一值
                        tempList.add("" + i);

                        // 添加默认图片
                        RichImgBean richImgBean = new RichImgBean();
                        richImgBean.setOnClickSpan(data.getOnClickSpan());
                        richImgBean.setRealText(data.getText());
                        int width = defaultDrawable.getIntrinsicWidth();
                        int height = defaultDrawable.getIntrinsicHeight();
                        defaultDrawable.setBounds(0, 0, width, height);
                        VerticalImageSpan verticalImageSpan = new VerticalImageSpan(defaultDrawable);
                        richImgBean.setVerticalImageSpan(verticalImageSpan);
                        data.setRichImgBean(richImgBean);
                    }
                }

                // 设置TextView
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : tempList)
                    stringBuilder.append(str);
                spannableString = new SpannableString(stringBuilder.toString());

                // 重新刷新数据
                updateRichTvImg(list);

                // 循环遍历显示图片
                for (int i = 0; i < list.size(); i++) {
                    final RichBean data = list.get(i);
                    if (data.getType() == 0)
                        continue;
//                        // 采用Glide加载图片
//                        Glide.with(getContext())
//                                .load(data.getText())
//                                .into(new SimpleTarget<Drawable>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
//                                        RichImgBean richImgBean = new RichImgBean();
//                                        richImgBean.setOnClickSpan(data.getOnClickSpan());
//                                        richImgBean.setRealText(data.getText());
//
//                                        int width = drawable.getIntrinsicWidth() / 2;
//                                        int height = drawable.getIntrinsicHeight() / 2;
//
//                                        if (richTvWidth < width && width != 0) {
//                                            float fl = new BigDecimal((float) richTvWidth / width)
//                                                    .setScale(5, BigDecimal.ROUND_HALF_UP)
//                                                    .floatValue();
//                                            height = (int) (fl * height) + 1;
//                                            width = richTvWidth;
//                                            if (onLongImageSpanClickListener != null)
//                                                richImgBean.setOnClickSpan(onLongImageSpanClickListener);
//                                        }
//
//                                        drawable.setBounds(0, 0, width, height);
//                                        VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
//                                        richImgBean.setVerticalImageSpan(verticalImageSpan);
//                                        data.setRichImgBean(richImgBean);
//
//                                        // 重新刷新数据
//                                        updateRichTv(list);
//                                    }
//                                });
                    downLoadImage = new DownLoadImage();
                    downLoadImage.loadImage(data.getText(), new DownLoadImage.ImageCallBack() {
                        @Override
                        public void getDrawable(Drawable drawable) {
                            RichImgBean richImgBean = new RichImgBean();
                            richImgBean.setOnClickSpan(data.getOnClickSpan());
                            richImgBean.setRealText(data.getText());

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

                            drawable.setBounds(0, 0, width, height);
                            VerticalImageSpan verticalImageSpan = new VerticalImageSpan(drawable);
                            richImgBean.setVerticalImageSpan(verticalImageSpan);
                            data.setRichImgBean(richImgBean);

                            // 重新刷新数据
                            updateRichImg(list);
                        }
                    });
                }
            }
        });
        return this;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

//    private void setDrawable(Button button, Drawable drawable, int color) {
//        drawable = tintDrawable(drawable, ColorStateList.valueOf(color));
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int size;
//        size = 90 * dm.heightPixels / 2560;
//        drawable.setBounds(0, 0, size, size);
//        button.setCompoundDrawables(null, drawable, null, null);
//        button.setTextColor(color);
//    }

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
