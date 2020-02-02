package jp.co.se.android.recipe.chapter06;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class Ch0616 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        private Bitmap mBitmap;
        private ScaleGestureDetector mScaleDetector;
        private float mScaleFactor = 1.0f;

        public MyView(Context context) {
            super(context);
            try {
                InputStream is = getResources().getAssets()
                        .open("sirokuma.jpg");
                mBitmap = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
            }
            mScaleDetector = new ScaleGestureDetector(context,
                    new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                        @Override
                        public boolean onScale(ScaleGestureDetector detector) {
                            mScaleFactor *= detector.getScaleFactor();
                            invalidate();
                            return true;
                        }
                    });
        }

        protected void onDraw(Canvas canvas) {
            canvas.save();

            canvas.scale(mScaleFactor, mScaleFactor);
            canvas.drawBitmap(mBitmap, 0, 0, null);

            canvas.restore();
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            mScaleDetector.onTouchEvent(ev);
            return true;
        }
    }

}
