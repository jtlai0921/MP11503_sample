package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Ch0105 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0105_main);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_horizontal);
        progressBar.setMax(100);
        progressBar.setProgress(30);
        progressBar.setSecondaryProgress(70);
    }
}