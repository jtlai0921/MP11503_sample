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
        // �إ�ContactDbOpenHelper
        ContactDbOpenHelper helper = new ContactDbOpenHelper(this);
        // ���o�i�g�J��SQLiteDatabase���
        SQLiteDatabase db = helper.getWritableDatabase();
        // ������Ʈw
        db.close();
        helper.close();
    }

}
