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

        // �q�����t�m�إߵ�������
        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        // �b�������ҥ[�J��ť��
        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            // ������ܤƮ�
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                // ���Toast�T��
                Toast.makeText(Ch0122.this,
                        "New Rating: " + rating + "  /fromUser:" + fromUser,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
