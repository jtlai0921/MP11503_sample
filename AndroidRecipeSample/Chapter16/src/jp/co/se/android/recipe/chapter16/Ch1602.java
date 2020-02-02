package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1602 extends Activity {
    private static final String TAG = "Ch1602";

    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco",
            "Lorenzio", "Maryvonne", "Siv",
            "Georgie", "Casimir", "Catharine",
            "Joker"};
    // @formatter:on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1602_main);

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
            // ContactDbOpenHelper‚ğ¶¬
            helper = new ContactDbOpenHelper(this);
            // ¨ú±o¥i¼g¤JªºSQLiteDatabase¹ê¨Ò
            db = helper.getWritableDatabase();

            // ¸ê®Æªº«Ø¥ß
            createData(db);
            // ¸ê®Æªº§ó·s
            updateData(db);
            // ¸ê®Æªº§R°£
            deleteData(db);
        } finally {
            if (db != null) {
                db.close();
            }
            if (helper != null) {
                helper.close();
            }
        }
    }

    private void createData(SQLiteDatabase db) {
        for (int i = 0; i < NAMES.length; i++) {
            // «Ø¥ß­nÀx¦s©Ò«Ø¥ß¸ê®ÆªºContentValues
            ContentValues values = new ContentValues();
            values.put(Contact.NAME, NAMES[i]);
            values.put(Contact.AGE, 20);
            // ¦^¶Ç©Ò«Ø¥ßªº¸ê®Æ_ID
            long id = db.insert(Contact.TBNAME, null, values);
            Log.d(TAG, "insert data:" + id);
        }
    }

    private void updateData(SQLiteDatabase db) {
        // «Ø¥ß­nÀx¦s©Ò«Ø¥ß¸ê®ÆªºContentValues
        ContentValues values = new ContentValues();
        // ±NContact.NAME¤¤¦³¥]§taªº¸ê®Æ¦~ÄÖÅÜ§ó¬°25
        values.put(Contact.AGE, 25);
        // ¦^¶Ç§ó·sªº¼Æ¦r
        int n = db.update(Contact.TBNAME, values, Contact.NAME + " like ?",
                new String[] { "%a%" });
        Log.d(TAG, "insert data:" + n);
    }

    private void deleteData(SQLiteDatabase db) {
        // §R°£Contact.NAME¬°Jokerªº¸ê®Æ
        // ¦^¶Ç§R°£ªº¼Æ¦r
        int n = db.delete(Contact.TBNAME, Contact.NAME + " = ?",
                new String[] { "Joker" });
        Log.d(TAG, "delete data:" + n);
    }

}
