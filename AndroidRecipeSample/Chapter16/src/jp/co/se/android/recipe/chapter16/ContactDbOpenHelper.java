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
        // ���w��Ʈw�ɦW�P�����A�ê�l��SQLiteOpenHelper���O
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "�I�s�FContactDbOpenHelper���غc�l");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "�I�s�FContactDbOpenHelper.onCreate");
        // �إ�Contact table
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
        Log.d(TAG, "�I�s�FContactDbOpenHelper.onUpgrade");
        // ���F�n�A���w�qContact table�A�R���ثe��table
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TBNAME);
        onCreate(db);
    }

}