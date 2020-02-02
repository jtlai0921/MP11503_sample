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

public class Ch1402 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1402_main);

        findViewById(R.id.linkMail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得收件人／主旨／本文
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etSubject = (EditText) findViewById(R.id.intputSubject);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String subject = etSubject.getText().toString();
                String body = etBody.getText().toString();

                // 若收件人／主旨／本文有任何一項是空白則發出警告
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(subject)
                        || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // 電子郵件協作
                Uri uri = Uri.parse("mailto:" + address);
                // 使用引數設定傳送目標，此值若使用setData設定也是相同意義
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // 電子郵件本文是Text時，設定為text/plain；若是HTML時，設定為text/html
                intent.setType("text/plain");
                // 設定標題名稱
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // 設定本文
                intent.putExtra(Intent.EXTRA_TEXT, body);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1402_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
