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
            // ¨ú±o³Q½á¤©ªº¦r¦ê—ñ‚ğæ“¾
            String sValue = intent.getStringExtra("key_name");

            EditText getString = (EditText) findViewById(R.id.inputString);
            getString.setText(sValue);
        }

        findViewById(R.id.backActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        EditText result = (EditText) findViewById(R.id.result);
                        // ¥u¦³·íªğ¦^­È¤¤¦³³]©w¦r¦êªº±¡ªp¤U¤~³]©wªğ¦^­È–ß‚è’l‚ğİ’è
                        if (result.getText().length() > 0) {
                            // ²£¥Íªğ¦^­È¥ÎªºIntent‚ğ¶¬
                            Intent data = new Intent();
                            // ³]©wªğ¦^­È–ß‚è’l‚ğİ’è
                            data.putExtra("key_name", result.getText()
                                    .toString());
                            // ³]©w¬°¦¨¥\¬Œ÷‚Æ‚µ‚Äİ’è
                            setResult(RESULT_OK, data);
                        }
                        finish();
                    }
                });
    }
}
