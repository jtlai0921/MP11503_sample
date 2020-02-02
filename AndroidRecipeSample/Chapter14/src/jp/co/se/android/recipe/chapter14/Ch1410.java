package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1410 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1410_main);

        findViewById(R.id.linkLine).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得搜尋的字串
                EditText etMessage = (EditText) findViewById(R.id.intputMessage);
                String message = etMessage.getText().toString();

                // 若搜尋字串為空白則發出警告
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(Ch1410.this,
                            getString(R.string.ch1410_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // LINE協作
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 設定要傳送到LINE的訊息
                intent.setData(Uri.parse("line://msg/text/" + message));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1410.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}