package jp.co.se.android.recipe.chapter15;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1501 extends Activity {
    private EditText mEditContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1501_main);

        mEditContents = (EditText) findViewById(R.id.editContents);
        findViewById(R.id.buttonWrite).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        writeContents(mEditContents.getText().toString());
                        mEditContents.setText("");
                        Toast.makeText(Ch1501.this, R.string.ch1501_message,
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void writeContents(String contents) {
        // 建立檔案輸出的資料夾
        File temppath = new File(Environment.getExternalStorageDirectory(),
                "temp");
        if (temppath.exists() != true) {
            temppath.mkdirs();
        }

        // 開啟檔案
        File tempfile = new File(temppath, "test.txt");
        FileWriter output = null;
        try {
            output = new FileWriter(tempfile, true);
            // 寫入內容
            output.write(contents);
            output.write("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
