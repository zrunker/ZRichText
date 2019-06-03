package cc.ibooker.zrichtext;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cc.ibooker.richtext.ClickSpan;
import cc.ibooker.richtext.RichBean;
import cc.ibooker.richtext.RichTextView;

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

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .load("https://graph.baidu.com/resource/101de050033f9aea1f04601554958922.jpg")
                .into(image);


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
    }

}
