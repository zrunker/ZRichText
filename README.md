最近写了一个富文本插件：[ZRichText](https://github.com/zrunker/ZRichText)

# ZRichText
自定义富文本控件RichTextView，用于显示图片+文字+Latex公式，支持图片点击、文本点击、Latex点击事件，支持文本特殊处理，支持图片颜色和背景色更改，支持富文局部操作：下划线、删除线、背景色、前景色、上标、下标等等，支持任意富文本显示，以及自动识别Latex公式并显示。

>微信公众号：书客创作

![书客创作](https://upload-images.jianshu.io/upload_images/3480018-94d5f20e4f88d0ae.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

现在越来越多的APP需要富文本的支持，网上也有比较成熟的富文本支持框架，而今天给大家推荐的富文本ZRichText，不仅仅能够很灵活的支持富文本显示、操作，而且还支持Latex公式。

#### 1、导入资源
A. gradle方式：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.zrunker:ZRichText:v1.1.6.5'
}
```
B. maven方式：
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.zrunker</groupId>
	<artifactId>ZRichText</artifactId>
	<version>v1.1.6.5</version>
</dependency>
```
#### 2、引用
A. 布局文件中引入RichTextView：
```
<cc.ibooker.richtext.RichTextView
        android:id="@+id/richTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```
B. 对应Activity中进行使用：
```
public class MainActivity extends AppCompatActivity {
    private RichTextView richTextView, richTextView2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richTextView.onDestory();
        richTextView2.onDestory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        richTextView = findViewById(R.id.richTextView);
        richTextView2 = findViewById(R.id.richTextView2);

        // 第一种实现方式
        ArrayList<RichBean> richBeans = new ArrayList<>();
        RichBean richBean1 = new RichBean();
        richBean1.setText("椭圆E: ");
        richBean1.setType(0);
        richBeans.add(richBean1);

        RichBean richBean2 = new RichBean();
        richBean2.setHeight(80);
        richBean2.setWidth(124);
        richBean2.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_1.png");
        richBean2.setType(1);
        richBeans.add(richBean2);

        RichBean richBean3 = new RichBean();
        richBean3.setText(" ");
        richBean3.setType(0);
        richBeans.add(richBean3);

        RichBean richBean4 = new RichBean();
        richBean4.setHeight(25);
        richBean4.setWidth(52);
        richBean4.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_3.png");
        richBean4.setType(1);
        richBeans.add(richBean4);

        RichBean richBean5 = new RichBean();
        richBean5.setText(" ");
        richBean5.setType(0);
        richBeans.add(richBean5);

        RichBean richBean6 = new RichBean();
        richBean6.setHeight(37);
        richBean6.setWidth(172);
        richBean6.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_5.png");
        richBean6.setType(1);
        richBeans.add(richBean6);

        RichBean richBean7 = new RichBean();
        richBean7.setText("的离心率是");
        richBean7.setType(0);
        richBeans.add(richBean7);

        RichBean richBean8 = new RichBean();
        richBean8.setHeight(81);
        richBean8.setWidth(49);
        richBean8.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_7.png");
        richBean8.setType(1);
        richBeans.add(richBean8);

        RichBean richBean9 = new RichBean();
        richBean9.setText(", 点");
        richBean9.setType(0);
        richBeans.add(richBean9);

        RichBean richBean10 = new RichBean();
        richBean10.setHeight(37);
        richBean10.setWidth(105);
        richBean10.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_9.png");
        richBean10.setType(1);
        richBeans.add(richBean10);

        RichBean richBean11 = new RichBean();
        richBean11.setText("在短轴");
        richBean11.setType(0);
        richBeans.add(richBean11);

        RichBean richBean12 = new RichBean();
        richBean12.setHeight(27);
        richBean12.setWidth(56);
        richBean12.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_11.png");
        richBean12.setType(1);
        richBeans.add(richBean12);

        RichBean richBean13 = new RichBean();
        richBean13.setText("上, 且");
        richBean13.setType(0);
        richBeans.add(richBean13);

        RichBean richBean14 = new RichBean();
        richBean14.setHeight(37);
        richBean14.setWidth(235);
        richBean14.setText("http://192.168.1.203:10000/equation_v2/question_5238_stem_13.png");
        richBean14.setType(1);
        richBeans.add(richBean14);

        RichBean richBean15 = new RichBean();
        richBean15.setText(", 椭圆E的方程为( )");
        richBean15.setType(0);
        richBeans.add(richBean15);

        richTextView.setRichText(richBeans, null, false);

        // 第二种实现方式
        String source = "椭圆$E$: $\\frac{x^2}{a^2}+\\frac{y^2}{b^2}$ $=1$ $(a\\gt b\\gt 0)$的离心率是$\\frac{\\sqrt{2}}{2}$, 点$P(0, 1)$在短轴$CD$上, 且$\\vec{PC}\\cdot \\vec{PD}=-1$, 椭圆$E$的方程为( )";
        richTextView2.setRichText(source, true);
   }
}
```
C. 效果图
![效果图](https://upload-images.jianshu.io/upload_images/3480018-64e66e98f4c47c34.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

以上案例采用两种方式实现了一样的效果，方式一属于图片+文字的形式，而方式二采用的是Latex公式。

#### 3、RichBean解析
```
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
    private Layout.Alignment align;// 文本方向
    private String family;// 文字字体
    private int[] colors;// 文字颜色集合
```
#### 4、自定义属性
```
<cc.ibooker.richtext.RichTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
	// 背景色，同时修改图片的背景
        app:backGroundColor="#40aff2"
	// Latex公式是否当作1个字符处理
        app:isLatexOneStr="true"
	// 是否支持图片缓存
        app:isOpenImgCache="true"
	// RichTextView是否可以滚动
        app:isScroll="true"
	// 图片加载模式，MODE_0-Glide,1-DownLoadImage
        app:loadImgModel="MODE_0"
	// 文字颜色，同事修改图片的颜色
        app:tintColor="#fff000" />
```
#### 5、方法
```
    /**
     * 设置是否能够滚动
     *
     * @param scroll boolean
     */
    public RichTextView setScroll(boolean scroll);

    /**
     * 设置默认显示图片
     *
     * @param defaultDrawable 默认显示图片Drawable
     */
    public RichTextView setDefaultDrawable(Drawable defaultDrawable);

    /**
     * 设置背景颜色 包括图片背景颜色
     *
     * @param backGroundColor 背景颜色 String 16进制 "#FFF000"
     */
    public RichTextView setBackGroundColor(String backGroundColor);

    /**
     * 设置字体颜色 包括图片颜色
     *
     * @param tintColor 字体颜色 String 16进制
     */
    public RichTextView setTintColor(String tintColor);

    /**
     * 销毁
     */
    public RichTextView onDestory();

    /**
     * 设置图片加载模式
     *
     * @param loadImgModel 加载图片模式，0-Glide，1-DownLoadImage，默认0
     */
    public RichTextView setLoadImgModel(int loadImgModel);

    /**
     * 设置是否打开缓存
     *
     * @param openImgCache boolean
     */
    public RichTextView setOpenImgCache(boolean openImgCache);

    /**
     * 显示数据
     *
     * @param text 待显示数据
     */
    public RichTextView setRichText(final CharSequence text);

    /**
     * 显示数据
     *
     * @param text          待显示数据
     * @param isLatexOneStr 是否将Latex公式当中1位字符串处理
     */
    public RichTextView setRichText(final CharSequence text, final boolean isLatexOneStr);

    /**
     * 显示数据
     *
     * @param text                     待显示数据
     * @param onLatexClickSpanListener Latex公式点击事件
     */
    public RichTextView setRichText(final CharSequence text, LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener);

    /**
     * 显示数据
     *
     * @param richBean 待显示数据
     */
    public RichTextView setRichText(RichBean richBean);

    /**
     * 显示数据
     *
     * @param richBean    待显示数据
     * @param isOpenCache 是否开始图片缓存 默认缓存true
     */
    public RichTextView setRichText(RichBean richBean, boolean isOpenCache);

    /**
     * 展示数据到TextView上，图片预显示为：多空格·多空格，总宽度与图片大小一致  或者  空格i空格
     *
     * @param datas 待显示数据列表
     */
    public RichTextView setRichText(ArrayList<RichBean> datas);

    /**
     * 展示数据到TextView上，图片预显示为：多空格·多空格，总宽度与图片大小一致  或者  空格i空格
     *
     * @param datas       待显示数据
     * @param isOpenCache 是否开始图片缓存 默认缓存
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, boolean isOpenCache);

    /**
     * 展示数据到TextView上，图片没预显示
     *
     * @param datas      待显示数据
     * @param defaultRes 默认预显示图片
     */

    public RichTextView setRichText(ArrayList<RichBean> datas, int defaultRes);

    /**
     * 展示数据到TextView上，图片没预显示
     *
     * @param datas       待显示数据
     * @param defaultRes  默认预显示图片
     * @param isOpenCache 是否开启图片缓存 默认true
     */

    public RichTextView setRichText(ArrayList<RichBean> datas, int defaultRes, boolean isOpenCache);

    /**
     * 展示数据到TextView上，图片预显示
     *
     * @param datas    待显示数据
     * @param drawable 默认预显示图片
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, final Drawable drawable);

    /**
     * 展示数据到TextView上，图片预显示
     *
     * @param datas       待显示数据
     * @param drawable    默认预显示图片
     * @param isOpenCache 是否开启图片缓存 默认true
     */
    public RichTextView setRichText(ArrayList<RichBean> datas, final Drawable drawable, boolean isOpenCache);

    /**
     * 清空背景颜色
     */
    public RichTextView clearBackgroundColor();

    /**
     * 重置背景颜色
     */
    public RichTextView resetBackgroundColor();

    /**
     * 重置文本颜色-包含图片部分
     */
    public RichTextView resetForegroundColor();

    /**
     * 重置文本颜色-非图片部分
     */
    public RichTextView resetTextColor();

    /**
     * 更新单项
     *
     * @param richBean 待更新数据
     * @param position 待更新项
     */
    public RichTextView updateItem(RichBean richBean, int position);

    /**
     * 图片点击回调
     */
    public RichTextView setOnImageSpanClickListener(ClickSpan.OnClickSpan onImageSpanClickListener);

    /**
     * 长图点击事件
     */
    public RichTextView setOnLongImageSpanClickListener(ClickSpan.OnClickSpan onLongImageSpanClickListener);
```

#### 6、富文本的支持
![富文本](https://upload-images.jianshu.io/upload_images/3480018-90817739d0a62144.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
    /**
     * 文本背景
     *
     * @param backgroundColor            背景颜色 16进制  "#FFF000"
     * @param startPosition              开始位置
     * @param endPosition                结束位置
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改（图片与文本背景是否等高）
     */
    public RichTextView updateBackgroundColor(final String backgroundColor,
                                                           final int startPosition,
                                                           final int endPosition,
                                                           final int updateBackGroundColorModel);

    /**
     * 文本背景2
     *
     * @param backgroundColor            背景颜色 int
     * @param startPosition              开始位置
     * @param endPosition                结束位置
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改（图片与文本背景是否等高）
     */
    public RichTextView updateBackgroundColor(final int backgroundColor,
                                                           final int startPosition,
                                                           final int endPosition,
                                                           final int updateBackGroundColorModel);

    /**
     * 更新文本圆角背景
     *
     * @param backgroundColor            背景颜色 16进制  "#FFF000"
     * @param radius                     圆角半径
     * @param startPosition              开始位置
     * @param endPosition                结束位置
     * @param updateBackGroundColorModel 修改背景模式，0-等高修改，1-不等高修改-只适合文字部分（图片与文本背景是否等高）
     */
    public RichTextView updateBackgroundColorRound(
            final String backgroundColor,
            final int radius,
            final int startPosition,
            final int endPosition,
            final int updateBackGroundColorModel);

    /**
     * 文本颜色
     *
     * @param color         文本颜色 16进制  "#FFF000"
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateForegroundColor(final String color,
                                                           final int startPosition,
                                                           final int endPosition);

    /**
     * 文本颜色2
     *
     * @param color         文本颜色 int
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateForegroundColor(final int color,
                                                           final int startPosition,
                                                           final int endPosition);

    /**
     * 文本颜色3
     *
     * @param color         文本颜色 int 单纯修改文本
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateTextColor(int color,
                                                     int startPosition,
                                                     int endPosition);

    /**
     * 文本颜色4
     *
     * @param color         文本颜色 String 16进制 单纯修改文本  "#FFF000"
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateTextColor(String color,
                                                     int startPosition,
                                                     int endPosition);

    /**
     * 添加超链接
     *
     * @param addUrl        超链接地址
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateURL(String addUrl,
                                               int startPosition,
                                               int endPosition);

    /**
     * 字体倍数
     *
     * @param textSizeMultiple 字体倍数
     * @param startPosition    开始位置
     * @param endPosition      结束位置
     */
    public RichTextView updateRelativeSize(float textSizeMultiple,
                                                        int startPosition,
                                                        int endPosition);

    /**
     * 是否添加删除线
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateUnderline(int startPosition, int endPosition);

    /**
     * 是否添加删除线
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateStrikethrough(int startPosition, int endPosition);

    /**
     * 是否为上标
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateSuperscript(int startPosition, int endPosition);

    /**
     * 是否为下标
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateSubscript(int startPosition, int endPosition);

    /**
     * 加粗
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateStyleBold(int startPosition, int endPosition);

    /**
     * 斜体
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateStyleItalic(int startPosition, int endPosition);

    /**
     * 加粗并斜体
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     */
    public RichTextView updateStyleBoldItalic(int startPosition, int endPosition);

    /**
     * X轴缩放倍数
     *
     * @param scaleXMultiple X轴缩放倍数
     * @param startPosition  开始位置
     * @param endPosition    结束位置
     */
    public RichTextView updateScaleX(float scaleXMultiple, int startPosition, int endPosition);

    /**
     * 添加边框
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param borderColor   边框颜色
     */
    public RichTextView updateBorder(int startPosition, int endPosition, int borderColor);

    /**
     * 添加模糊效果
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param radius        模糊半径，越大越模糊
     * @param style         样式
     */
    public RichTextView updateBlurMaskFilter(int startPosition,
                                                          int endPosition,
                                                          float radius,
                                                          BlurMaskFilter.Blur style);

    /**
     * 浮雕效果
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param direction     该参数必须是一个长度为3的浮点型数组，表示光源从x、y、z照射曲线的方向
     * @param ambient       表示周围光源的数量，取值范围是0..1
     * @param specular      光照的反射系数 如6
     * @param blurRadius    在光照前喷涂的范围
     */
    public RichTextView updateEmbossMaskFilter(int startPosition,
                                                            int endPosition,
                                                            float[] direction,
                                                            float ambient,
                                                            float specular,
                                                            float blurRadius);

    /**
     * 文本插入图片
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param drawable      待插入图片
     * @param pad           内边距
     */
    public RichTextView updateDrawableMargin(int startPosition,
                                                          int endPosition,
                                                          Drawable drawable,
                                                          int pad);

    /**
     * 添加竖线
     *
     * @param startPosition 开始位置
     * @param endPosition   解释位置
     * @param quoteColor    竖线颜色
     */
    public RichTextView updateQuote(int startPosition, int endPosition, int quoteColor);

    /**
     * 绝对大小
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param size          大小
     * @param dip           是否为dp
     */
    public RichTextView updateAbsoluteSize(int startPosition, int endPosition, int size, boolean dip);

    /**
     * 设置图片，基于文本基线或底部对齐
     *
     * @param startPosition     开始位置
     * @param endPosition       结束位置
     * @param verticalAlignment 位置 DynamicDrawableSpan.ALIGN_BASELINE、 DynamicDrawableSpan.ALIGN_BOTTOM
     * @param drawable          带显示图片
     */
    public RichTextView updateDynamicDrawable(int startPosition, int endPosition, int verticalAlignment, final Drawable drawable);

    /**
     * 插入图片
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param bitmap        待插入bitmap
     * @param pad           内边距
     */
    public RichTextView updateIconMargin(int startPosition, int endPosition, Bitmap bitmap, int pad);

    /**
     * 设置文本方向
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param align         方向
     */
    public RichTextView updateAlignmentStandard(int startPosition, int endPosition, Layout.Alignment align);

    /**
     * 修改文本样式
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param family        字体样式 monospace serif sans-serif
     * @param style         字体形式 Typeface.NORMAL Typeface.BOLD Typeface.ITALIC Typeface.BOLD_ITALIC
     * @param size          字体大小（单位px）
     * @param color         文字颜色
     * @param linkColor     链接文字颜色
     */
    public RichTextView updateTextAppearance(int startPosition,
                                                          int endPosition,
                                                          String family,
                                                          int style,
                                                          int size,
                                                          ColorStateList color,
                                                          ColorStateList linkColor);

    /**
     * 修改文本样式2
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param appearance    appearance资源id（例如：android.R.style.TextAppearance_Small）
     * @param colorList     文本的颜色资源id（例如：android.R.styleable.Theme_textColorPrimary）
     */
    public RichTextView updateTextAppearance(int startPosition,
                                                          int endPosition,
                                                          int appearance,
                                                          int colorList);

    /**
     * 修改文本样式3
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param appearance    appearance资源id（例如：android.R.style.TextAppearance_Small）
     */
    public RichTextView updateTextAppearance(int startPosition,
                                                          int endPosition,
                                                          int appearance);

    /**
     * 文本字体
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param family        字体样式 monospace serif sans-serif
     */
    public RichTextView updateTypeface(int startPosition, int endPosition, String family);

    /**
     * 文本字体2
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param typeface      自定义字体
     */
    @RequiresApi(api = 28)
    public RichTextView updateTypeface(int startPosition, int endPosition, Typeface typeface);

    /**
     * 设置颜色数组
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param colors        颜色数组
     */
    public RichTextView updateColors(int startPosition, int endPosition, int[] colors);

    /**
     * 段落层次-小圆点
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param gapWidth      小圆点的宽度
     * @param color         小圆点的颜色
     */
    public RichTextView updateBullet(int startPosition, int endPosition, int gapWidth, int color);

    /**
     * 段落层次-小圆点2
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param gapWidth      小圆点的宽度
     */
    public RichTextView updateBullet(int startPosition, int endPosition, int gapWidth);

    /**
     * 段落层次-小圆点3
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param gapWidth      小圆点的宽度
     * @param color         小圆点颜色
     * @param bulletRadius  半径
     */
    @RequiresApi(api = 28)
    public RichTextView updateBullet(int startPosition, int endPosition, int gapWidth, int color, int bulletRadius);

    /**
     * 设置文本环绕
     *
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param lines         所占行数
     * @param margin        外边距
     */
    public RichTextView updateTextRound(int startPosition, int endPosition, int lines, int margin);

    /**
     * 设置span
     *
     * @param what          span对象
     * @param startPosition 开始位置
     * @param endPosition   结束位置
     * @param flags         如 Spanned.SPAN_INCLUSIVE_EXCLUSIVE
     */
    public RichTextView setSpan(Object what, int startPosition, int endPosition, int flags);
```

#### 7、Latex公式的支持
```
    /**
     * 设置Latex公式是否为一个字符串
     *
     * @param latexOneStr boolean
     */
    public RichTextView setLatexOneStr(boolean latexOneStr);

    /**
     * Latex公式点击事件
     */
    public RichTextView setOnLatexClickSpan(LatexClickSpan.OnLatexClickSpan onLatexClickSpanListener);

    /**
     * Latex长公式点击事件
     */
    public RichTextView setOnLongLatexClickSpanListener(LatexClickSpan.OnLatexClickSpan onLongLatexClickSpanListener);
```

#### 8、混淆规则
在proguard-rules.pro文件中添加以下代码：
```
# RichTextView
-keep class cc.ibooker.richtext.jlatexmath.** { *; }
```
同时注意Glide3.7.0的混淆：（如果采用1-DownLoadImage就不需要混淆）
```
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
```

推荐应用 [书客编辑器](https://www.coolapk.com/apk/cc.ibooker.ibookereditor)
