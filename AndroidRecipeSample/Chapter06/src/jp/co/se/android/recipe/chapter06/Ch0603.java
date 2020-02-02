package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

public class Ch0603 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaint = new Paint();
        Path mPath = new Path();

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeWidth(5);

            mPath.moveTo(50, 50);
            mPath.cubicTo(300, 50, 100, 400, 400, 400);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            canvas.drawPath(mPath, mPaint);
        }
    }

}
