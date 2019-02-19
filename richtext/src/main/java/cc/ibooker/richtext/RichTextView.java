package cc.ibooker.richtext;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 自定义富文本View
 */
public class RichTextView extends android.support.v7.widget.AppCompatTextView {
    private ArrayList<String> tempList;
    private SpannableString spannableString;

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

    /**
     * 展示数据到TextView上
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

                    for (int i = 0; i < list.size(); i++) {
                        final RichBean data = list.get(i);
                        if (data.getType() == 0) {
                            tempList.add(data.getText());
                        } else {
                            StringBuilder strBuilder = new StringBuilder();
                            // 设置占位符 - 唯一值
                            if (data.getWidth() > 0) {
                                int width = px2sp(getContext(), data.getWidth());
                                int num = (int) (width / size);
                                for (int j = 0; j < num; j++)
                                    strBuilder.append("\t");
                                strBuilder.append(".");
                                for (int j = 0; j < num; j++)
                                    strBuilder.append("\t");
                            } else {
                                strBuilder.append("\t")
                                        .append(i)
                                        .append("\t");
                            }
                            tempList.add(strBuilder.toString());

                            // 采用Glide加载图片
                            Glide.with(getContext())
                                    .load(data.getText())
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                                            RichImgBean richImgBean = new RichImgBean();
                                            richImgBean.setOnClickSpan(data.getOnClickSpan());
                                            richImgBean.setRealText(data.getText());

                                            int width = drawable.getIntrinsicWidth() / 2;
                                            int height = drawable.getIntrinsicHeight() / 2;

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
                                            updateRichTv(list);
                                        }
                                    });
                        }
                    }
                    // 设置TextView
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String str : tempList)
                        stringBuilder.append(str);
                    spannableString = new SpannableString(stringBuilder.toString());
                    setText(spannableString);

                    // 重新刷新数据
                    updateRichTv(list);
                }
            });
        }
        return this;
    }

    /**
     * 更新RichTv
     */
    private synchronized void updateRichTv(ArrayList<RichBean> list) {
        if (spannableString != null && tempList != null) {
            for (int j = 0; j < list.size(); j++) {
                RichBean richBean = list.get(j);
                if (richBean.getRichImgBean() != null) {// 加载图片
                    updateRichTvImgSpan(richBean.getRichImgBean(), j);
                } else {
                    if (richBean.getOnClickSpan() != null) {
                        String text = richBean.getText();
                        if (tempList.contains(text)) {
                            int startPosition = 0;// 标记realText的开始位置
                            for (int i = 0; i < tempList.size(); i++) {
                                if (i == j) break;
                                else startPosition += tempList.get(i).length();
                            }
                            if (startPosition >= 0 && startPosition < spannableString.length()) {
                                ClickSpan clickSpan = new ClickSpan(text);
                                spannableString.setSpan(clickSpan,
                                        startPosition, startPosition + text.length(),
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                // 设置具体的点击事件
                                clickSpan.setOnClickSpan(richBean.getOnClickSpan());
                            }
                        }
                        // 刷新richTv
                        setText(spannableString);
                    }
                }
            }
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
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    private int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
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
