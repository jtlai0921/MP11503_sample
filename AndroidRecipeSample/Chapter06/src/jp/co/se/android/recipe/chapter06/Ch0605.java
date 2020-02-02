package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

public class Ch0605 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaintStd = new Paint();
        Paint mPaintFill = new Paint();
        Rect mRectStd = new Rect();
        Rect mRectFill = new Rect();

        public MyView(Context context) {
            super(context);
            // 定義無填滿的Paint
            mPaintStd = new Paint();
            mPaintStd.setColor(Color.RED);
            mPaintStd.setStyle(Style.STROKE);
            mPaintStd.setStrokeWidth(5);
            // 定義有填滿的Paint
            mPaintFill = new Paint();
            mPaintFill.setColor(Color.RED);
            mPaintFill.setStyle(Style.FILL);

            // 定義無填滿的矩形的範圍
            mRectStd.set(100, 100, 300, 300);
            // 定義有填滿的矩形的範圍
            mRectFill.set(100, 500, 300, 700);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            // 描繪無填滿的矩形
            canvas.drawRect(mRectStd, mPaintStd);
            // 描繪有填滿的矩形
            canvas.drawRect(mRectFill, mPaintFill);
        }
    }

}
