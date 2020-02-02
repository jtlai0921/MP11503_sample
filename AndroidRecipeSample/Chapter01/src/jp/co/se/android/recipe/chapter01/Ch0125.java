package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ZoomControls;

public class Ch0125 extends Activity {
    private float scale = 1.0F;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.ch0125_main);

        final TextView tvContents = (TextView) findViewById(R.id.Contents);
        final ZoomControls zc = (ZoomControls) findViewById(R.id.ZoomControl);

        // 設定縮放的預設值
        tvContents.setText(String.valueOf(scale));

        // ZoomIn
        zc.setOnZoomInClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scale += 0.1;
                if (scale > 4) {
                    scale = 4;
                }
                tvContents.setScaleX(scale);
                tvContents.setScaleY(scale);
                tvContents.setText(String.valueOf(scale));
            }
        });
        // ZoomOut
        zc.setOnZoomOutClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scale -= 0.1;
                if (scale <= 1) {
                    scale = 1;
                }
                tvContents.setScaleX(scale);
                tvContents.setScaleY(scale);
                tvContents.setText(String.valueOf(scale));
            }
        });
    }
}
