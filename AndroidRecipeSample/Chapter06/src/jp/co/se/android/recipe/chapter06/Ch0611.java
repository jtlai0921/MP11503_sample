package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ch0611 extends Activity implements OnClickListener {
    private static final String EXTRA_BACKGROUND_POINTER = "extra.BACKGROUND_POINTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0611_main);

        Button btnStartAnimation = (Button) findViewById(R.id.btnStartAnimation);
        btnStartAnimation.setOnClickListener(this);

        View contentView = (View) findViewById(android.R.id.content);
        int justColor = sBackgroundColors[getBackgroundPointer()
                % sBackgroundColors.length];
        contentView.setBackgroundColor(justColor);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Ch0611.class);
        intent.putExtra(EXTRA_BACKGROUND_POINTER, getBackgroundPointer() + 1);
        startActivity(intent);
    }

    private static final int[] sBackgroundColors = {
        Color.rgb(0xEC, 0xAC, 0xB5), 
        Color.rgb(0xF9, 0xE3, 0xAA),
        Color.rgb(0xDF, 0xEC, 0xAA),
        Color.rgb(0xA6, 0xE3, 0x9D),
        Color.rgb(0x91, 0xDB, 0xB9),
        Color.rgb(0x95, 0xDF, 0xD6),
        Color.rgb(0x97, 0xD3, 0xE3),
        Color.rgb(0x99, 0xCF, 0xE5),
        Color.rgb(0x9A, 0xCD, 0xE7),
        Color.rgb(0xAE, 0xC1, 0xE3),
        Color.rgb(0xBF, 0xC2, 0xDF),
        Color.rgb(0xE7, 0xA5, 0xC9),
    };

    private int getBackgroundPointer() {
        int p = 0;
        Intent intent = getIntent();
        if (intent != null) {
            p = intent.getIntExtra(EXTRA_BACKGROUND_POINTER, 0);
        }

        return p;
    }

}
