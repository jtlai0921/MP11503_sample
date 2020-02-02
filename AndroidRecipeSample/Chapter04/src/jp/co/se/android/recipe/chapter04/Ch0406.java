package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch0406 extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0406_main);

        findViewById(R.id.launchActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 建立呼叫畫面的Intent
                        final Intent intent = new Intent(Ch0406.this,
                                Ch0406Sub.class);
                        // 在Intent中使用KEY_NAME的鍵設定字串
                        EditText inputString = (EditText) findViewById(R.id.inputString);
                        String value = inputString.getText().toString();
                        intent.putExtra("key_name", value);

                        // 使用可取得返回值的呼叫方式來開始Activity
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 以startActivityForResult執行時的引數,來比較所設定的RequestCode
        if (requestCode == REQUEST_CODE) {
            // 判定Activity結束時的旗標
            if (resultCode == RESULT_OK) {
                // 取得被設定為返回值的KEY_NAME的值
                String value = data.getStringExtra("key_name");
                EditText result = (EditText) findViewById(R.id.result);
                result.setText(value);
            }
        }
    }
}
