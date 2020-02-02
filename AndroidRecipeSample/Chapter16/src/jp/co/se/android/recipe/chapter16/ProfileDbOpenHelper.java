package jp.co.se.android.recipe.chapter16;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "ProfileDbOpenHelper";
    static final String DATABASE_NAME = "profile.db";
    static final int DATABASE_VERSION = 1;

    public ProfileDbOpenHelper(Context context) {
        // 指定資料庫檔名與版本，並初始化SQLiteOpenHelper類別
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "呼叫了ProfileDbOpenHelper的建構子");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "呼叫了ProfileDbOpenHelper.onCreate");
        // 建立Profile table
        // @formatter:off
	database.execSQL("CREATE TABLE " 
        + Profile.TBNAME + "(" 
        + Profile._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
        + Profile.NAME + " TEXT NOT NULL, " 
        + Profile.PHOTOGRAPHMAGE + " blob"+ ");");
	// @formatter:on
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "呼叫了ProfileDbOpenHelper.onUpgrade");
        // 為了要再次定義Profile table，刪除現有的table
        db.execSQL("DROP TABLE IF EXISTS " + Profile.TBNAME);
        onCreate(db);
    }

}