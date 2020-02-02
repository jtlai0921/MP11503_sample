package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ch0219 extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = new Button(this);
        button.setText("變更圖示");
        button.setOnClickListener(this);

        setContentView(button);
    }

    @Override
    public void onClick(View v) {
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ch0219_ic_launcher);
    }
}
