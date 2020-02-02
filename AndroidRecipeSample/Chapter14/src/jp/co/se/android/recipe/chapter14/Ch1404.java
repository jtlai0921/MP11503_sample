package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1404 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1404_main);

        findViewById(R.id.linkText).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得文字
                EditText etText = (EditText) findViewById(R.id.intputText);
                String text = etText.getText().toString();

                // 若文字是空白則發出警告
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(Ch1404.this,
                            getString(R.string.ch1404_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // 文字協作
                Intent intent = new Intent();
                // 設定為進行文字協作的Aciton
                intent.setAction(Intent.ACTION_SEND);
                // 設定文字資料的類別
                intent.setType("text/plain");
                // 設定文字資料
                intent.putExtra(Intent.EXTRA_TEXT, text);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1404.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
