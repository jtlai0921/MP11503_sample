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
                        // �إߩI�s�e����Intent
                        final Intent intent = new Intent(Ch0406.this,
                                Ch0406Sub.class);
                        // �bIntent���ϥ�KEY_NAME����]�w�r��
                        EditText inputString = (EditText) findViewById(R.id.inputString);
                        String value = inputString.getText().toString();
                        intent.putExtra("key_name", value);

                        // �ϥΥi���o��^�Ȫ��I�s�覡�Ӷ}�lActivity
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // �HstartActivityForResult����ɪ��޼�,�Ӥ���ҳ]�w��RequestCode
        if (requestCode == REQUEST_CODE) {
            // �P�wActivity�����ɪ��X��
            if (resultCode == RESULT_OK) {
                // ���o�Q�]�w����^�Ȫ�KEY_NAME����
                String value = data.getStringExtra("key_name");
                EditText result = (EditText) findViewById(R.id.result);
                result.setText(value);
            }
        }
    }
}
