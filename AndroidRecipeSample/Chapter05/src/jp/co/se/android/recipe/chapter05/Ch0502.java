package jp.co.se.android.recipe.chapter05;

import jp.co.se.android.recipe.chapter05.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

public class Ch0502 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "�ܬ���V�F", Toast.LENGTH_LONG).show();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "�ܬ��a�V�F", Toast.LENGTH_LONG).show();
        }
    }
}