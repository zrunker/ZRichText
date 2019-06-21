# ZRichText
自定义富文本控件RichTextView，用于显示图片+文字，支持图片点击和文本点击事件，支持文本特殊处理，支持图片颜色和背景色更改。

>微信公众号：书客创作

![书客创作](http://www.ibookerfile.cc/upload/images/acontents/1_1519218464761acontentimage.jpg)

**1、导入资源**
A. gradle方式：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.zrunker:ZRichText:v1.0.5'
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
	<version>v1.0.5</version>
</dependency>
```
**2、引用**
A. 布局文件中引入RichTextView：
```
<cc.ibooker.richtext.RichTextView
        android:id="@+id/richTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
```
B. Activity中设置内容和操作：
```
public class MainActivity extends AppCompatActivity {
    private RichTextView richTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<RichBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RichBean richBean = new RichBean();
            if (i % 2 == 0) {
                richBean.setType(1);
                if (i == 0) {
                    richBean.setText("https://graph.baidu.com/resource/101de050033f9aea1f04601554958922.jpg");
                    richBean.setBackgroundColor("#40aff2");
                } else if (i == 2) {
                    richBean.setText("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=991546949,3543761346&fm=26&gp=0.jpg");
                    richBean.setColor("#55F033");
                } else if (i == 4) {
                    richBean.setText("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3421137803,3191126749&fm=26&gp=0.jpg");
                } else if (i == 6)
                    richBean.setText("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=322845814,2288534704&fm=26&gp=0.jpg");
                else if (i == 8) {
                    richBean.setText("http://img4.imgtn.bdimg.com/it/u=3735493004,329574884&fm=26&gp=0.jpg");
                } else
                    richBean.setText("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=659351332,2214018330&fm=26&gp=0.jpg");
                richBean.setWidth(0);
                richBean.setHeight(0);
            } else {
                richBean.setType(0);
                richBean.setText("书客创作" + i + "测试富文本");
                if (i == 1) {
                    richBean.setTextSizeMultiple(1.5f);
                    richBean.setStrikethrough(true);
                    richBean.setBold(true);
                }
                if (i == 3) {
                    richBean.setAddUrl("http://ibooker.cc/article/search/list/1");
                }
                if (i == 5) {
                    richBean.setBackgroundColor("#FF4040");
                    richBean.setSuperscript(true);
                    richBean.setBoldItalic(true);
                }
                if (i == 7) {
                    richBean.setUnderline(true);
                    richBean.setItalic(true);
                }
                if (i == 9) {
                    richBean.setColor("#4aFF00");
                    richBean.setSubscript(true);
                    richBean.setScaleXMultiple(1.7f);
                }
                richBean.setOnClickSpan(new ClickSpan.OnClickSpan() {
                    @Override
                    public void onClickSpan(String txt) {
                        Toast.makeText(MainActivity.this, "文本点击事件：" + txt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            list.add(richBean);
        }

        richTextView = findViewById(R.id.richTextView);
//        richTextView.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
//                Toast.makeText(MainActivity.this, "长图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
//            }
//        }).setOnImageSpanClickListener(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
//                Toast.makeText(MainActivity.this, "图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
//            }
//        }).setRichText(list);

//        richTextView.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
//                Toast.makeText(MainActivity.this, "长图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
//            }
//        }).setOnImageSpanClickListener(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
//                Toast.makeText(MainActivity.this, "图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
//            }
//        }).setRichText(list, R.mipmap.ic_launcher);

        richTextView.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
            @Override
            public void onClickSpan(String txt) {
                Toast.makeText(MainActivity.this, "长图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
            }
        }).setOnImageSpanClickListener(new ClickSpan.OnClickSpan() {
            @Override
            public void onClickSpan(String txt) {
                Toast.makeText(MainActivity.this, "图片点击事件，图片地址：" + txt, Toast.LENGTH_SHORT).show();
            }
        }).setRichText(list, ContextCompat.getDrawable(this, R.mipmap.ic_launcher));


        // 5s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(0);
                richBean.setBackgroundColor("#55FF00");
                richTextView.updateItem(richBean, 0);
            }
        }, 5000);

        // 5s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(9);
                richBean.setText("下面是加载图片对比图：");
                richTextView.updateItem(richBean, 9);
            }
        }, 10000);

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .load("https://graph.baidu.com/resource/101de050033f9aea1f04601554958922.jpg")
                .into(image);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        richTextView.onDestory();
//    }
}
```

富文本相关数据
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
}
```

最终显示效果：
![图片描述](http://www.ibookerfile.cc/upload/images/acontents/1_1550577253961acontentimage.jpg)
![图片描述](http://www.ibookerfile.cc/upload/images/acontents/1_1554988318078acontentimage.jpg)

**混淆规则**
在proguard-rules.pro文件中添加以下代码：
```
# RichTextView
-keep class cc.ibooker.richtext.jlatexmath.** { *; }
```
