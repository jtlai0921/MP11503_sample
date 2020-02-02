package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ZoomControls;

public class Ch0126 extends Activity {
    private float scale = 1.0F;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.ch0126_main);

        // 顯示ZoomControl的倍率
        final TextView tvContents = (TextView) findViewById(R.id.Contents);
        tvContents.setText(String.valueOf(scale));

        final ZoomControls zc = (ZoomControls) findViewById(R.id.ZoomControl);
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

        // 設定縮放的延遲速度
        final Button btnSlowZoom = (Button) findViewById(R.id.ZoomSpeedSlow);
        btnSlowZoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zc.setZoomSpeed(1000);
            }
        });
        // 設定縮放的延遲速度
        final Button btnFastZoom = (Button) findViewById(R.id.ZoomSpeedFast);
        btnFastZoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zc.setZoomSpeed(1);
            }
        });

    }
}
