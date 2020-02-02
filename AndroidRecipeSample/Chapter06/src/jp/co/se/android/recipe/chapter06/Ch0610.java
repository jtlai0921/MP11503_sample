package jp.co.se.android.recipe.chapter06;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;

public class Ch0610 extends Activity implements OnClickListener {

    private static final String TAG = Ch0610.class.getSimpleName();
    private ImageView mImageView;
    private float mInitialX;
    private float mInitialY;
    private float mInitialAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0610_main);

        mImageView = (ImageView) findViewById(R.id.imageView);
        Button btnViewPropertyAnimator = (Button) findViewById(R.id.btnViewPropertyAnimator);
        Button btnObjectAnimator = (Button) findViewById(R.id.btnObjectAnimator);
        Button btnValueAnimator = (Button) findViewById(R.id.btnValueAnimator);
        btnViewPropertyAnimator.setOnClickListener(this);
        btnObjectAnimator.setOnClickListener(this);
        btnValueAnimator.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 維持初始值
        mInitialX = mImageView.getX();
        mInitialY = mImageView.getY();
        mInitialAlpha = mImageView.getAlpha();
    }

    @Override
    public void onClick(View v) {
        // 初始化
        mImageView.setX(mInitialX);
        mImageView.setY(mInitialY);
        mImageView.setAlpha(mInitialAlpha);
        mImageView.setRotation(0);

        int id = v.getId();
        if (id == R.id.btnViewPropertyAnimator) {
            doViewAnimation(mImageView);
        } else if (id == R.id.btnObjectAnimator) {
            doObjectAnimation(mImageView);
        } else if (id == R.id.btnValueAnimator) {
            doValueAnimator(mImageView);
        }
    }

    private void doViewAnimation(View v) {
        ViewPropertyAnimator animator = v.animate();
        animator.x(100f).setDuration(2000).start();
    }

    private void doObjectAnimation(View v) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "x", 100f);
        animator.setDuration(2000).start();
    }

    private void doValueAnimator(final View v) {
        ValueAnimator animation = ValueAnimator.ofFloat(mInitialX, 100f);
        animation.setDuration(2000).addListener(
                new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.v(TAG, "onAnimationStart");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        Log.v(TAG, "onAnimationRepeat");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.v(TAG, "onAnimationEnd");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        Log.v(TAG, "onAnimationCancel");
                    }
                });
        animation.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                Log.v(TAG, "onAnimationUpdate, value=" + value);
                v.setX(value);
            }
        });
        animation.start();
    }
}
