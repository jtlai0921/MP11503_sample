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
            // ContactDbOpenHelper‚ð¶¬
            helper = new ContactDbOpenHelper(this);
            // ¨ú±o¥i¼g¤JªºSQLiteDatabase¹ê¨Ò
            db = helper.getWritableDatabase();

            // ¸ê®Æ·j´M
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
            // ¨ú±oComments tableªº¥þ³¡¸ê®Æ
            cursor = db.query(Contact.TBNAME, null, Contact.AGE + " > ?",
                    new String[] { Integer.toString(20) }, null, null,
                    Contact.NAME);
            // Cursor¤¤¦³1¥ó¥H¤Wªº¸ê®Æ®É«h¶i¦æ³B²z
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // ¨ú±o¦W¦r
                    String name = cursor.getString(cursor
                            .getColumnIndex(Contact.NAME));
                    // ¨ú±o¦~ÄÖ
                    int age = cursor.getInt(cursor.getColumnIndex(Contact.AGE));
                    Log.d(TAG, name + ":" + age);

                    // ±NCursor²¾°Ê¨ì¤U¤@­Ó¸ê®Æ
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
