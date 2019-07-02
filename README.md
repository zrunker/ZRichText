# ZRichText
自定义富文本控件RichTextView，用于显示图片+文字+Latex公式，支持图片点击、文本点击、Latex点击事件，支持文本特殊处理，支持图片颜色和背景色更改，支持富文局部操作：下划线、删除线、背景色、前景色、上标、下标等等，支持任意富文本显示，以及自动识别Latex公式并显示。

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
	implementation 'com.github.zrunker:ZRichText:v1.1.6.1'
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
	<version>v1.1.6.1</version>
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
例如布局：
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="200dp"
        android:layout_height="wrap_content"
        android:maxLines="2"
        android:singleLine="false"
        android:scrollbars="vertical"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <cc.ibooker.richtext.RichTextView
        android:id="@+id/richTextView4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:text="Hello World!"
        android:maxLines="2"
        android:scrollbars="vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:orientation="vertical">

            <cc.ibooker.richtext.RichTextView
                android:id="@+id/richTextView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Hello World!"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <cc.ibooker.richtext.RichTextView
                android:id="@+id/richTextView2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:text="Hello World!"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <cc.ibooker.richtext.RichTextView
                android:id="@+id/richTextView3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp"
                android:text="Hello World!"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <ImageView
                android:id="@+id/image"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="100dp"
                android:contentDescription="@null" />

        </LinearLayout>
    </ScrollView>
</LinearLayout>
```
B. Activity中设置内容和操作：
```
package cc.ibooker.zrichtext;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cc.ibooker.richtext.ClickSpan;
import cc.ibooker.richtext.LatexClickSpan;
import cc.ibooker.richtext.bean.RichBean;
import cc.ibooker.richtext.RichTextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private RichTextView richTextView, richTextView2, richTextView3, richTextView4;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richTextView.onDestory();
        richTextView2.onDestory();
        richTextView3.onDestory();
        richTextView4.onDestory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        richTextView = findViewById(R.id.richTextView);
        richTextView2 = findViewById(R.id.richTextView2);
        richTextView3 = findViewById(R.id.richTextView3);
        richTextView4 = findViewById(R.id.richTextView4);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText("阿搜噶搜噶是否噶是购房人个人过去而过个人首付该发as嘎嘎嘎嘎递给他还让他忽然还是电话司徒浩然四氧化三铁花容失色的还是电话");


        ArrayList<RichBean> richBeans = new ArrayList<>();
        RichBean richBean1 = new RichBean();
        richBean1.setHeight(0);
        richBean1.setWidth(0);
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
        richBean3.setHeight(0);
        richBean3.setWidth(0);
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
        richBean5.setHeight(0);
        richBean5.setWidth(0);
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
        richBean7.setHeight(0);
        richBean7.setWidth(0);
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
        richBean9.setHeight(0);
        richBean9.setWidth(0);
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
        richBean11.setHeight(0);
        richBean11.setWidth(0);
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
        richBean13.setHeight(0);
        richBean13.setWidth(0);
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
        richBean15.setHeight(0);
        richBean15.setWidth(0);
        richBean15.setText(", 椭圆E的方程为( )");
        richBean15.setType(0);
        richBeans.add(richBean15);

        richTextView.setRichText(richBeans, null, false);

        richTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.updateBackgroundColor("#FFE892", 18, 20, 1);
                richTextView.updateBackgroundColor("#FFE892", 21, 24, 1);
                richTextView.updateBackgroundColor("#FFE892", 28, 29, 1);

                richTextView.updateBorder(1, 3, Color.BLUE);
            }
        }, 100);


        String source = "椭圆$E$: $\\frac{x^2}{a^2}+\\frac{y^2}{b^2}$ $=1$ $(a\\gt b\\gt 0)$的离心率是$\\frac{\\sqrt{2}}{2}$, 点$P(0, 1)$在短轴$CD$上, 且$\\vec{PC}\\cdot \\vec{PD}=-1$, 椭圆$E$的方程为( )";

        richTextView2.setRichText(source, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView2.updateBackgroundColor("#FFE892", 18, 20, 1);
                richTextView2.updateBackgroundColor("#FFE892", 21, 24, 1);
                richTextView2.updateBackgroundColor("#FFE892", 28, 29, 1);
            }
        }, 100);

        richTextView3.setRichText(source, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView3.updateBackgroundColor("#FFE892", 18, 20, 0);
                richTextView3.updateBackgroundColor("#FFE892", 21, 24, 0);
                richTextView3.updateBackgroundColor("#FFE892", 28, 29, 0);
            }
        }, 100);


        // 数学公式
        final String richText = "$\\therefore$ 椭圆$C$的方程为$\\frac{x^2}{4}+\\frac{y^2}{3}=1$"
                + "111111111226565asdfa" +
                "$e^{\\pi i} + 1 = 0$" +
                "$$\n" +
                "\\begin{aligned}\n" +
                "u \\cdot n\n" +
                "& =u n^T \\\\\n" +
                "& = u(A A^{-1})n^T \\\\\n" +
                "& =(uA)(A^{-1}n^T) \\\\\n" +
                "& =(uA)((A^{-1}n^T)^T)^T \\\\\n" +
                "& =(uA)(n(A^{-1})^T)^T \\\\\n" +
                "& =uA \\cdot n(A^{-1})^T \\\\\n" +
                "& =uA\\cdot nB \\\\\n" +
                "& =0\n" +
                "\\end{aligned}\n" +
                "$$" +
                "$$f(x_1,x_x,\\ldots,x_n) = x_1^2 + x_2^2 + \\cdots + x_n^2 $$" +
                "$$f(x_1,x_x,\\ldots,x_n) = x_1^2 + x_2^2 + \\cdots + x_n^2 $$" +
                "$$f(x_1,x_x,\\ldots,x_n) = x_1^2 + x_2^2 + \\cdots + x_n^2 $$" +
                "$$f(x_1,x_x,\\ldots,x_n) = x_1^2 + x_2^2 + \\cdots + x_n^2 $$" +
                "水电费感受到个人身体根深蒂固防守打法跟不上的根本是赶不上的根本是人工";

//        richTextView4.setRichText(richText, new LatexClickSpan.OnLatexClickSpan() {
//            @Override
//            public void onLatexClickSpan(String latex, Bitmap bitmap) {
//                Toast.makeText(MainActivity.this, latex, Toast.LENGTH_SHORT).show();
//            }
//        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                richTextView4.updateBackgroundColor(Color.parseColor("#40aff2"), 0, 31, 1);
//            }
//        }, 2000);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                richTextView4.updateForegroundColor(Color.parseColor("#40aff2"), 61, 101);
//            }
//        }, 3000);


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
                richBean.setText("书客创作" + i + "测试富文本" + "$e^{\\pi i} + 1 = 0$");
                richBean.setOnLatexClickSpan(new LatexClickSpan.OnLatexClickSpan() {

                    @Override
                    public void onLatexClickSpan(String latex, Bitmap bitmap) {
                        Toast.makeText(MainActivity.this, latex, Toast.LENGTH_SHORT).show();
                    }
                });
                if (i == 1) {
                    richBean.setTextSizeMultiple(1.5f);
                    richBean.setStrikethrough(true);
                    richBean.setBold(true);
                }
                if (i == 3) {
                    richBean.setAddUrl("http://ibooker.cc/article/search/list/1");
                    richBean.setOnClickSpan(new ClickSpan.OnClickSpan() {
                        @Override
                        public void onClickSpan(String txt) {
                            Toast.makeText(MainActivity.this, "文本点击事件：" + txt, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (i == 5) {
                    richBean.setBackgroundColor("#FF4040");
                    richBean.setSuperscript(true);
                    richBean.setBoldItalic(true);
                }
                if (i == 7) {
                    richBean.setUnderline(true);
                    richBean.setItalic(true);
                    richBean.setBorderColor("#FF4501");
                    richBean.setOnClickSpan(new ClickSpan.OnClickSpan() {
                        @Override
                        public void onClickSpan(String txt) {
                            Toast.makeText(MainActivity.this, "文本点击事件：" + txt, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (i == 9) {
                    richBean.setColor("#4aFF00");
                    richBean.setSubscript(true);
                    richBean.setScaleXMultiple(1.7f);
                }

            }
            list.add(richBean);
        }

//        richTextView4.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
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

//        richTextView4.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
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

        richTextView4.setOnLongImageSpanClickListener(new ClickSpan.OnClickSpan() {
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateBackgroundColor("#FFF000", 2, 18, 1);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateBackgroundColor("#FFF000", 19, 27, 1);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateBackgroundColor("#FFF000", 2, 3, 1);
                richTextView4.updateBackgroundColor("#FFF000", 5, 30, 1);
                richTextView4.updateBackgroundColor("#FFF000", 30, 34, 1);
                richTextView4.updateBackgroundColor("#FFF000", 35, 39, 1);
            }
        }, 3000);

        // 3s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(0);
                richBean.setBackgroundColor("#55FF00");
                richTextView4.updateItem(richBean, 0);
            }
        }, 3000);

        // 5s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(9);
                richBean.setText("下面是加载图片对比图：");
                richTextView4.updateItem(richBean, 9);
            }
        }, 5000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateBackgroundColor("#5643f5", 0, 20, 1);
            }
        }, 7000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateForegroundColor("#5643f5", 20, 40);
            }
        }, 9000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.updateBackgroundColorRound("#5643f5", 8, 35, 65, 1);
            }
        }, 10000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.clearBackgroundColor();
            }
        }, 11000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.resetBackgroundColor();
            }
        }, 12000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.resetTextColor();
            }
        }, 13000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView4.resetForegroundColor();
            }
        }, 14000);


        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .load("https://graph.baidu.com/resource/101de050033f9aea1f04601554958922.jpg")
                .into(image);
    }

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
    private int[] colors;
}
```

**自定义属性**
```
<cc.ibooker.richtext.RichTextView
        android:id="@+id/richTextView4"
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

最终显示效果：
![图片描述](http://www.ibookerfile.cc/upload/images/acontents/1_1550577253961acontentimage.jpg)
![图片描述](http://www.ibookerfile.cc/upload/images/acontents/1_1554988318078acontentimage.jpg)

**混淆规则**
在proguard-rules.pro文件中添加以下代码：
```
# RichTextView
-keep class cc.ibooker.richtext.jlatexmath.** { *; }
-keep class cc.ibooker.richtext.bean.** { *; }
```
同时注意Glide的混淆：
```
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
```
