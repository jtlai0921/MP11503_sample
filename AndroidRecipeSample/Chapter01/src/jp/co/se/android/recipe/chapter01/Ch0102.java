package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.widget.TextView;

public class Ch0102 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0102_main);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setEllipsize(TruncateAt.END);
        textView2.setSingleLine(true);
        textView2.setText("在程式碼當中所設定的省略文字:世界你好!");
    }

}
