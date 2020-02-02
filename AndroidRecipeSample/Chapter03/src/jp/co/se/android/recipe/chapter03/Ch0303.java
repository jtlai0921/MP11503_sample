package jp.co.se.android.recipe.chapter03;

import jp.co.se.android.recipe.chapter03.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.GridLayout;

public class Ch0303 extends Activity {
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0303_main);

        // 想要伸縮行或列時
        final GridLayout gl = (GridLayout) findViewById(R.id.GridLayout);
        gl.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // 伸縮行
                        // stretchColumns(gl);
                        // 伸縮列
                        // stretchRows(gl);
                        gl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    /**
     * 伸縮行
     * 
     * @param gl
     */
    public void stretchColumns(GridLayout gl) {
        int margin = 10 * gl.getColumnCount();
        int childWidth = (int) ((gl.getWidth() - margin) / gl.getColumnCount());
        for (int i = 0; i < gl.getChildCount(); i++) {
            View childView = gl.getChildAt(i);
            childView.setMinimumWidth(childWidth);
        }
    }

    /**
     * 伸縮列
     * 
     * @param gl
     */
    public void stretchRows(GridLayout gl) {
        int margin = 10 * gl.getRowCount();
        int childHeight = (int) ((gl.getHeight() - margin) / gl.getRowCount());
        for (int i = 0; i < gl.getChildCount(); i++) {
            View childView = gl.getChildAt(i);
            childView.setMinimumHeight(childHeight);
        }
    }
}
