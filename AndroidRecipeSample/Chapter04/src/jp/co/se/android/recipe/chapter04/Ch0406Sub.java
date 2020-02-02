package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch0406Sub extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0406_sub);

        Intent intent = getIntent();
        if (intent != null) {
            // ���o�Q�ᤩ���r�����擾
            String sValue = intent.getStringExtra("key_name");

            EditText getString = (EditText) findViewById(R.id.inputString);
            getString.setText(sValue);
        }

        findViewById(R.id.backActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText result = (EditText) findViewById(R.id.result);
                        // �u�����^�Ȥ����]�w�r�ꪺ���p�U�~�]�w��^�Ȗ߂�l��ݒ�
                        if (result.getText().length() > 0) {
                            // ���ͪ�^�ȥΪ�Intent�𐶐�
                            Intent data = new Intent();
                            // �]�w��^�Ȗ߂�l��ݒ�
                            data.putExtra("key_name", result.getText()
                                    .toString());
                            // �]�w�����\�����Ƃ��Đݒ�
                            setResult(RESULT_OK, data);
                        }
                        finish();
                    }
                });
    }
}
