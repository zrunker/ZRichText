package cc.ibooker.zrichtext;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cc.ibooker.richtext.ClickSpan;
import cc.ibooker.richtext.LatexClickSpan;
import cc.ibooker.richtext.RichBean;
import cc.ibooker.richtext.RichTextView;

public class MainActivity extends AppCompatActivity {
    private RichTextView richTextView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richTextView.onDestory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        richTextView = findViewById(R.id.richTextView);

        String source = "设$F_1$, $F_2$分别是椭圆$E$: $x^2+\\frac{y^2}{b^2}=1(0\\lt b\\lt 1)$的左、右焦点, 过点$F_1$的直线交椭圆$E$于$A$、$B$两点, 若$|AF_1|=3|F_1B|$, $AF_2\\perp x$轴, 则椭圆$E$的方程为( )";
//        richTextView.setRichText(source);


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

//        richTextView.setRichText(richText, new LatexClickSpan.OnLatexClickSpan() {
//            @Override
//            public void onLatexClickSpan(String latex, Bitmap bitmap) {
//                Toast.makeText(MainActivity.this, latex, Toast.LENGTH_SHORT).show();
//            }
//        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                richTextView.updateBackgroundColor(Color.parseColor("#40aff2"), 0, 31);
//            }
//        }, 2000);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                richTextView.updateForegroundColor(Color.parseColor("#40aff2"), 61, 101);
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


        // 3s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(0);
                richBean.setBackgroundColor("#55FF00");
                richTextView.updateItem(richBean, 0);
            }
        }, 3000);

        // 5s之后修改单项数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RichBean richBean = list.get(9);
                richBean.setText("下面是加载图片对比图：");
                richTextView.updateItem(richBean, 9);
            }
        }, 5000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.updateBackgroundColor("#5643f5", 0, 20);
            }
        }, 7000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.updateForegroundColor("#5643f5", 20, 40);
            }
        }, 9000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.updateBackgroundColorRound("#5643f5", 8, 35, 65);
            }
        }, 10000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.clearBackgroundColor();
            }
        }, 11000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.resetBackgroundColor();
            }
        }, 12000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.resetTextColor();
            }
        }, 13000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                richTextView.resetForegroundColor();
            }
        }, 14000);


        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .load("https://graph.baidu.com/resource/101de050033f9aea1f04601554958922.jpg")
                .into(image);
    }

}
