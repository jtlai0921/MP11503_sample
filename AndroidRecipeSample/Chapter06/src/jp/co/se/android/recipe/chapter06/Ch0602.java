package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class Ch0602 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaint = new Paint();
        float[] mXY;

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(5);
            // @formatter:off
            mXY = new float[]{
                     100, 200, 300, 200
                    ,300, 200, 300, 500
                    ,300, 500, 500, 500};
            // @formatter:on
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // 線段的描繪
            canvas.drawLine(100, 50, 500, 50, mPaint);
            // 複數線段的描繪
            canvas.drawLines(mXY, mPaint);
        }
    }

}
