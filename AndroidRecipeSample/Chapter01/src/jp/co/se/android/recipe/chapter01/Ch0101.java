package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class Ch0101 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0101_main);

        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setAutoLinkMask(Linkify.WEB_URLS);
        textView2.setText("在程式中所設定的URL：http://www.shoeisha.co.jp/");
    }
}
