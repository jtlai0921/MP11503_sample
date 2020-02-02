package jp.co.se.android.recipe.chapter06;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class Ch0601 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        ArrayList<PointF> mPointList = new ArrayList<PointF>();
        Paint mPaint = new Paint();

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(50);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mPointList.add(new PointF(event.getX(), event.getY()));
                invalidate();
                break;
            default:
                break;
            }

            return true;
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            for (int i = 0; i < mPointList.size(); i++) {
                PointF point = mPointList.get(i);
                canvas.drawPoint(point.x, point.y, mPaint);
            }
        }
    }

}
