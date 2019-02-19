package cc.ibooker.zrichtext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

import cc.ibooker.richtext.ClickSpan;
import cc.ibooker.richtext.RichBean;
import cc.ibooker.richtext.RichTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<RichBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RichBean richBean = new RichBean();
            if (i % 2 == 0) {
                richBean.setType(1);
                if (i == 4)
                    richBean.setText("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3421137803,3191126749&fm=26&gp=0.jpg");
                else if (i == 6)
                    richBean.setText("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=322845814,2288534704&fm=26&gp=0.jpg");
                else
                    richBean.setText("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=659351332,2214018330&fm=26&gp=0.jpg");
                richBean.setWidth(0);
                richBean.setHeight(0);
            } else {
                richBean.setType(0);
                richBean.setText("书客创作" + i + "测试富文本");
                richBean.setOnClickSpan(new ClickSpan.OnClickSpan() {
                    @Override
                    public void onClickSpan(String txt) {
                        Toast.makeText(MainActivity.this, "文本点击事件：" + txt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            list.add(richBean);
        }

        RichTextView richTextView = findViewById(R.id.richTextView);
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
        }).setRichText(list);
    }
}
