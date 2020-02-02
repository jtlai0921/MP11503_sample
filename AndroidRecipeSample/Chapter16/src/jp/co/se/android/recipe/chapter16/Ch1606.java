package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1606 extends Activity {
    private static final String TAG = "Ch1606";
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
        setContentView(R.layout.ch1606_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ContactDbOpenHelper helper = null;
                        SQLiteDatabase db = null;
                        try {
                            // ContactDbOpenHelperÇê∂ê¨
                            helper = new ContactDbOpenHelper(Ch1606.this);
                            // ®˙±o•iºg§J™∫SQLiteDatabaseπÍ®“
                            db = helper.getWritableDatabase();

                            // ´ÿ•ﬂ∏ÍÆ∆ÉfÅ[É^ÇÃçÏê¨
                            createDataByTransaction(db);
                        } finally {
                            if (db != null) {
                                db.close();
                            }
                            if (helper != null) {
                                helper.close();
                            }
                        }
                    }
                });
    }

    private void createDataByTransaction(SQLiteDatabase db) {
        try {
            // ∂}©lTransaction
            db.beginTransaction();
            Log.d(TAG, "∂}©lTransaction");

            // ±N≤{¶b©“¿x¶s™∫∏ÍÆ∆•˛≥°ßR∞£
            db.delete(Contact.TBNAME, null, null);

            for (int i = 0; i < NAMES.length; i++) {
                // ´ÿ•ﬂ¿x¶s∏ÍÆ∆™∫ContentValues
                ContentValues values = new ContentValues();
                values.put(Contact.NAME, NAMES[i]);
                values.put(Contact.AGE, 20);
                // ¶b∏ÍÆ∆Æw§§´ÿ•ﬂContact™∫∏ÍÆ∆
                db.insert(Contact.TBNAME, null, values);
                Log.d(TAG, String.format("´ÿ•ﬂ∏ÍÆ∆[%d]", i + 1));
            }
            // ΩT©wTransaction
            db.setTransactionSuccessful();
            Log.d(TAG, "ΩT©wTransaction");
        } finally {
            // µ≤ßÙTransaction
            db.endTransaction();
            Log.d(TAG, "µ≤ßÙTransaction");
        }
    }

}
