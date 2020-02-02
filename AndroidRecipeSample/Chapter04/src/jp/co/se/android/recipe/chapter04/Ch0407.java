package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @see Ch0407AppWidgetProvider
 */
public class Ch0407 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        String msg = String.format(
                "請確認下方的Widget\n* %1$s\n* %2$s\n* %3$s",
                Ch0407AppWidgetProvider.class.getName(),
                "res/xml/chapter04_widget.xml", "AndroidManifest.xml");
        textView.setText(msg);
        setContentView(textView);
    }

}
