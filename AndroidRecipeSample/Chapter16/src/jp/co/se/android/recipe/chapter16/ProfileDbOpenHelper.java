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
        // ���w��Ʈw�ɦW�P�����A�ê�l��SQLiteOpenHelper���O
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "�I�s�FProfileDbOpenHelper���غc�l");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "�I�s�FProfileDbOpenHelper.onCreate");
        // �إ�Profile table
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
        Log.d(TAG, "�I�s�FProfileDbOpenHelper.onUpgrade");
        // ���F�n�A���w�qProfile table�A�R���{����table
        db.execSQL("DROP TABLE IF EXISTS " + Profile.TBNAME);
        onCreate(db);
    }

}