package jp.co.se.android.recipe.chapter05;

import jp.co.se.android.recipe.chapter05.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class Ch0504 extends Activity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 建立GestureDetector實例
        mGestureDetector = new GestureDetector(this, this);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        // 在gestureDetector#onTouchEvent方法判別、區分觸碰事件
        mGestureDetector.onTouchEvent(ev);

        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(this, "called onSingleTap()", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(this, "called onDoubleTap()", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(this, "被長時間按住了", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
}
