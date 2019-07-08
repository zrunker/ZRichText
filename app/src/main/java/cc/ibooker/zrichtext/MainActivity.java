package cc.ibooker.zrichtext;

import android.graphics.Bitmap;
import android.graphics.Color;
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
import cc.ibooker.richtext.RichTextView;
import cc.ibooker.richtext.bean.RichBean;

public class MainActivity extends AppCompatActivity {
    private RichTextView textView, richTextView, richTextView2, richTextView3, richTextView4, richTextView5;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textView.onDestory();
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
        richTextView5 = findViewById(R.id.richTextView5);
        textView.setText("阿搜噶搜噶是否噶是购房人个人过去而过个人首付该发as嘎嘎嘎嘎递给他还让他忽然还是电话司徒浩然四氧化三铁花容失色的还是电话")
                .setHorizontallyScroll();

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

//        richTextView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        richTextView.updateBackgroundColor("#FFE892", 18, 20, 1);
        richTextView.updateBackgroundColor("#FFE892", 21, 24, 1);
        richTextView.updateBackgroundColor("#FFE892", 28, 29, 1);

        richTextView.updateBorder(1, 3, Color.BLUE);
//            }
//        }, 100);


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


        ArrayList<RichBean> list2 = new ArrayList<>();
        RichBean richBean41 = new RichBean();
        richBean41.setType(0);
        richBean41.setText("爱人发噶尔尕尔发电房");
        list2.add(richBean41);

        final RichBean richBean42 = new RichBean();
        richBean42.setType(1);
        richBean42.setRes(R.drawable.xizhi_topic_img_pencil_tiankong);
//        richBean42.setOnClickSpan(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
////                richBean42.setRes(R.drawable.xizhi_topic_img_line_tiankong);
////                richTextView5.updateItem(richBean42, 1);
//                richTextView5.updateImageItems(R.drawable.xizhi_topic_img_line_tiankong);
//            }
//        });
        list2.add(richBean42);

        RichBean richBean43 = new RichBean();
        richBean43.setType(0);
        richBean43.setText("爱人发噶尔尕尔发电房");
        list2.add(richBean43);

        final RichBean richBean44 = new RichBean();
        richBean44.setType(1);
        richBean44.setRes(R.drawable.xizhi_topic_img_pencil_tiankong);
//        richBean44.setOnClickSpan(new ClickSpan.OnClickSpan() {
//            @Override
//            public void onClickSpan(String txt) {
//                Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_LONG).show();
//            }
//        });
        list2.add(richBean44);
        richTextView5.setOnImageSpanClickListener(new ClickSpan.OnClickSpan() {
            @Override
            public void onClickSpan(String txt) {
                richTextView5.updateImageItems(R.drawable.xizhi_topic_img_line_tiankong);
            }
        });
//        richTextView5.setRichText(list2);

        richTextView5.setText("________江东 $$f(n)$$ 父老________接受的(公式开始) $$f(n)=\\[ \\sum_{k=1}^n k^2 = \\frac{1}{2} n (n+1)\\]$$公式结束________", "________", R.drawable.xizhi_topic_img_pencil_tiankong);
    }

}
