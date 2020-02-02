package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1607 extends Activity {
    private static final String TAG = "Ch1607";
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
                            // ´ÿ•ﬂContactDbOpenHelperÇê∂ê¨
                            helper = new ContactDbOpenHelper(Ch1607.this);
                            // ®˙±o•iºg§J™∫SQLiteDatabaseπÍ®“
                            db = helper.getWritableDatabase();

                            // ´ÿ•ﬂ∏ÍÆ∆
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
            db.beginTransaction();

            // ±N≤{¶b¿x¶s™∫∏ÍÆ∆•˛≥°ßR∞£
            db.delete(Contact.TBNAME, null, null);

            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(Contact.TBNAME).append("(")
                    .append(Contact.NAME).append(",").append(Contact.AGE)
                    .append(") values(?, ?)");

            // ´ÿ•ﬂπwΩsƒ∂≥Ø≠z¶°
            SQLiteStatement stat = db.compileStatement(sql.toString());
            Log.d(TAG, "´ÿ•ﬂπwΩsƒ∂≥Ø≠z¶°: " + sql.toString());

            for (int i = 0; i < NAMES.length; i++) {
                stat.bindString(1, NAMES[i]);
                stat.bindLong(2, 30);
                // ¶b∏ÍÆ∆Æw´ÿ•ﬂContact™∫∏ÍÆ∆
                long id = stat.executeInsert();
                Log.d(TAG, String.format("´ÿ•ﬂ∏ÍÆ∆[%d]", id));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

}
