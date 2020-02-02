package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class Ch0132 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0132_main);

        ImageView img = (ImageView) findViewById(R.id.image);

        Resources res = getResources();

        Bitmap bitmap = BitmapFactory.decodeResource(res,
                android.R.drawable.btn_star_big_on);

        // 以200×90建立bitmap圖像
        Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, 200, 90, false);

        img.setImageBitmap(bitmap2);
    }
}
