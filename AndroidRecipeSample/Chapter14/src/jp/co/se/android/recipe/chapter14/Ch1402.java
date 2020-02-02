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
                // ���o����H���D��������
                EditText etAddress = (EditText) findViewById(R.id.intputAddress);
                EditText etSubject = (EditText) findViewById(R.id.intputSubject);
                EditText etBody = (EditText) findViewById(R.id.intputBody);
                String address = etAddress.getText().toString();
                String subject = etSubject.getText().toString();
                String body = etBody.getText().toString();

                // �Y����H���D�������妳����@���O�ťիh�o�Xĵ�i
                if (TextUtils.isEmpty(address) || TextUtils.isEmpty(subject)
                        || TextUtils.isEmpty(body)) {
                    Toast.makeText(Ch1402.this,
                            getString(R.string.ch1401_label_notfound_app),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // �q�l�l���@
                Uri uri = Uri.parse("mailto:" + address);
                // �ϥΤ޼Ƴ]�w�ǰe�ؼСA���ȭY�ϥ�setData�]�w�]�O�ۦP�N�q
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                // �q�l�l�󥻤�OText�ɡA�]�w��text/plain�F�Y�OHTML�ɡA�]�w��text/html
                intent.setType("text/plain");
                // �]�w���D�W��
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                // �]�w����
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
