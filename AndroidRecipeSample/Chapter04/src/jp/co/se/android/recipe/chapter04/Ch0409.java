package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ch0409 extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0409_main);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            Intent serviceIntent = new Intent(this, Ch0409Service.class);
            startService(serviceIntent);
        } else if (v.getId() == R.id.btnStop) {
            Intent serviceIntent = new Intent(this, Ch0409Service.class);
            stopService(serviceIntent);
        }
    }
}
