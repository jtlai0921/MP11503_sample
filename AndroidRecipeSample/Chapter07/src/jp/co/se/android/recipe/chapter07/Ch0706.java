package jp.co.se.android.recipe.chapter07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch0706 extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0706_main);

        findViewById(R.id.buttonPlay).setOnClickListener(this);
        findViewById(R.id.buttonStop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlay) {
            Intent service = new Intent(this, Ch0706Service.class);
            service.setAction("play");
            startService(service);
        } else if (v.getId() == R.id.buttonStop) {
            Intent service = new Intent(this, Ch0706Service.class);
            service.setAction("stop");
            startService(service);
        }
    }

}
