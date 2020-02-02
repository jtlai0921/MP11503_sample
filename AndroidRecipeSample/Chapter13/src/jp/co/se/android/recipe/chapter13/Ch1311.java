package jp.co.se.android.recipe.chapter13;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Ch1311 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1311_main);

        findViewById(R.id.buttonShow).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showToast("Hello");
            }
        });
    }

    private void showToast(String msg) {
        // ≈„•‹Toast
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
