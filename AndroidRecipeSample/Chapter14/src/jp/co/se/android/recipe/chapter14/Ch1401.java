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

public class Ch1401 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1401_main);

        findViewById(R.id.linkSms).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ���o����H�P����
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String body = etBody.getText().toString();

                // �Y����H�Τ��妳����@���O�ťէY�o�Xĵ�i
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1401.this,
                            getString(R.string.ch1401_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // ²�T��@
                Uri uri = Uri.parse("smsto:" + address);
                // �ϥΤ޼Ƴ]�w�ǰe�ؼСA���ȭY�ϥ�setData�]�w�]�O�ۦP�N�q
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // �]�w����
                intent.putExtra("sms_body", body);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Ch1401.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
