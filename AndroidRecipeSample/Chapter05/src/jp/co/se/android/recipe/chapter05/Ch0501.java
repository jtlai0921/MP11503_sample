package jp.co.se.android.recipe.chapter05;

import jp.co.se.android.recipe.chapter05.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

public class Ch0501 extends Activity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // �إ�GestureDetector���
        mGestureDetector = new GestureDetector(this, this);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        // �ϥ�gestureDetector#onTouchEvent��k�ӧP�O�B�Ϥ��I���ƥ�
        mGestureDetector.onTouchEvent(ev);

        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(this, "����F���I��", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(this, "����F���I��", Toast.LENGTH_LONG).show();
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
