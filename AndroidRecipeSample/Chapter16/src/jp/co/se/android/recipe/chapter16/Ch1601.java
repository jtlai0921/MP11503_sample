package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1601 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1601_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startTest();
                    }
                });
    }

    private void startTest() {
        // 建立ContactDbOpenHelper
        ContactDbOpenHelper helper = new ContactDbOpenHelper(this);
        // 取得可寫入的SQLiteDatabase實例
        SQLiteDatabase db = helper.getWritableDatabase();
        // 關閉資料庫
        db.close();
        helper.close();
    }

}
