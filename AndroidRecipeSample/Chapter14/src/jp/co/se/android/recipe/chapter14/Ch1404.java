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
                // ���o��r
                EditText etText = (EditText) findViewById(R.id.intputText);
                String text = etText.getText().toString();

                // �Y��r�O�ťիh�o�Xĵ�i
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(Ch1404.this,
                            getString(R.string.ch1404_label_input_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // ��r��@
                Intent intent = new Intent();
                // �]�w���i���r��@��Aciton
                intent.setAction(Intent.ACTION_SEND);
                // �]�w��r��ƪ����O
                intent.setType("text/plain");
                // �]�w��r���
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
