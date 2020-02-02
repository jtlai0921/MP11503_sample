package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Ch0131 extends Activity implements OnClickListener {
    ImageView mImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0131_main);
        mImageView = (ImageView) findViewById(R.id.ImageView);

        findViewById(R.id.btnScaleCenter).setOnClickListener(this);
        findViewById(R.id.btnScaleFitCenter).setOnClickListener(this);
        findViewById(R.id.btnScaleFitEnd).setOnClickListener(this);
        findViewById(R.id.btnScaleFitStart).setOnClickListener(this);
        findViewById(R.id.btnScaleFitXY).setOnClickListener(this);
        findViewById(R.id.btnScaleMatrix).setOnClickListener(this);
    }

    public void onClick(View v) {
        // mImageView.setImageResource(R.id.ImageView);
        switch (v.getId()) {
        case R.id.btnScaleCenter:
            mImageView.setScaleType(ImageView.ScaleType.CENTER);
            break;

        case R.id.btnScaleFitCenter:
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            break;

        case R.id.btnScaleFitEnd:
            mImageView.setScaleType(ImageView.ScaleType.FIT_END);
            break;

        case R.id.btnScaleFitStart:
            mImageView.setScaleType(ImageView.ScaleType.FIT_START);
            break;

        case R.id.btnScaleFitXY:
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            break;

        case R.id.btnScaleMatrix:
            Matrix mtrx = new Matrix();
            mImageView.setScaleType(ScaleType.MATRIX);
            mtrx.postRotate(90.0f, mImageView.getWidth() / 3,
                    mImageView.getHeight() / 2);
            mImageView.setImageMatrix(mtrx);
            break;

        }
    }
}