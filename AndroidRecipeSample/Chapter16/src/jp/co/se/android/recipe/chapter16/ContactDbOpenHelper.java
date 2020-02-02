package jp.co.se.android.recipe.chapter16;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "ContactDbOpenHelper";

    static final String DATABASE_NAME = "contact.db";
    static final int DATABASE_VERSION = 1;

    public ContactDbOpenHelper(Context context) {
        // 指定資料庫檔名與版本，並初始化SQLiteOpenHelper類別
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "呼叫了ContactDbOpenHelper的建構子");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "呼叫了ContactDbOpenHelper.onCreate");
        // 建立Contact table
        // @formatter:off
	database.execSQL("CREATE TABLE " 
        + Contact.TBNAME + "(" 
        + Contact._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + Contact.NAME + " TEXT NOT NULL, " 
        + Contact.AGE + " INTEGER " + ");");
	// @formatter:on
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "呼叫了ContactDbOpenHelper.onUpgrade");
        // 為了要再次定義Contact table，刪除目前的table
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TBNAME);
        onCreate(db);
    }

}