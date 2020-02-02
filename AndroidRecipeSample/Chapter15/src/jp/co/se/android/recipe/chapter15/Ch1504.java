package jp.co.se.android.recipe.chapter15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1504 extends Activity {
    private TextView mTextAlice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1504_main);

        mTextAlice = (TextView) findViewById(R.id.textAlice);

        readFile(R.raw.alice);
    }

    private void readFile(int resId) {
        try {
            // 從Raw資源資料夾的資源ID來取得InputStream實例
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(getResources().openRawResource(resId)));
            // 使用BufferedReader逐行進行讀取
            String StringBuffer;
            String stringText = "";
            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();
            // 將所讀取的檔案內容顯示在畫面
            mTextAlice.setText(stringText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mTextAlice.setText(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            mTextAlice.setText(e.toString());
        }
    }
}
