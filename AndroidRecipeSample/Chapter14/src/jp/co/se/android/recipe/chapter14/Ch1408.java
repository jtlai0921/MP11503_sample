package jp.co.se.android.recipe.chapter14;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class Ch1408 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1408_main);

        findViewById(R.id.linkMap).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得搜尋的字串
                EditText etSearchWord = (EditText) findViewById(R.id.intputSearchWord);
                String searchWord = etSearchWord.getText().toString();

                // 若搜尋字串為空白則發出警告
                if (TextUtils.isEmpty(searchWord)) {
                    Toast.makeText(Ch1408.this,
                            getString(R.string.ch1408_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // 將geo:0,0的預設位置視為現在位置來搜尋新的地點
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse("geo:0,0?q="
                                    + URLEncoder.encode(searchWord, "UTF-8")));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1408.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
