package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class Ch0122 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0122_main);

        // 從頁面配置建立評分欄實例
        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        // 在評分欄實例加入監聽器
        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            // 當評分變化時
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                // 顯示Toast訊息
                Toast.makeText(Ch0122.this,
                        "New Rating: " + rating + "  /fromUser:" + fromUser,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
