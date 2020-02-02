package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Ch0609 extends Activity implements OnClickListener {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0609_main);

        Button btnStartAnimation = (Button) findViewById(R.id.btnStartAnimation);
        btnStartAnimation.setOnClickListener(this);

        // ©I¥sImageView
        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        doAnimation();
    }

    private void doAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.ch0609);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        mImageView.startAnimation(animation);
    }
}
