package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1603 extends Activity {
    private static final String TAG = "Ch1603";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1603_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startTest();
                    }
                });
    }

    private void startTest() {
        ContactDbOpenHelper helper = null;
        SQLiteDatabase db = null;
        try {
            // ContactDbOpenHelper�𐶐�
            helper = new ContactDbOpenHelper(this);
            // ���o�i�g�J��SQLiteDatabase���
            db = helper.getWritableDatabase();

            // ��Ʒj�M
            searchData(db);
        } finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }
        }
    }

    private void searchData(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            // ���oComments table���������
            cursor = db.query(Contact.TBNAME, null, Contact.AGE + " > ?",
                    new String[] { Integer.toString(20) }, null, null,
                    Contact.NAME);
            // Cursor����1��H�W����Ʈɫh�i��B�z
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // ���o�W�r
                    String name = cursor.getString(cursor
                            .getColumnIndex(Contact.NAME));
                    // ���o�~��
                    int age = cursor.getInt(cursor.getColumnIndex(Contact.AGE));
                    Log.d(TAG, name + ":" + age);

                    // �NCursor���ʨ�U�@�Ӹ��
                }
                ;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
