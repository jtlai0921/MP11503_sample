package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch1405 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1405_main);

        findViewById(R.id.getclipboard).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 從剪貼簿取得文字
                        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = cm.getPrimaryClip();
                        if (clipData != null) {
                            ClipData.Item item = clipData.getItemAt(0);
                            EditText et = (EditText) findViewById(R.id.inputClipBoard);
                            et.setText(item.getText());
                        }
                    }
                });
    }
}
