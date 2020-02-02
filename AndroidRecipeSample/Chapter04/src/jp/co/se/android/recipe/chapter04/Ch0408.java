package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Ch0408 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        String msg = String.format(
                "請確認下方的Widget\n* %1$s\n* %2$s\n* %3$s",
                Ch0408AppWidgetProvider.class.getName(),
                "res/xml/chapter04_widget_click.xml", "AndroidManifest.xml");
        textView.setText(msg);
        setContentView(textView);
    }

}
