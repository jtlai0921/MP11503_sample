package jp.co.se.android.recipe.chapter02;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ch0218 extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("變更標題");
        button.setOnClickListener(this);

        setContentView(button);
    }

    @Override
    public void onClick(View v) {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Hello, ActionBar!!");
        actionBar.setSubtitle("and, Subtitle!!");
    }
}
