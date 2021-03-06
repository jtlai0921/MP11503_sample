package jp.co.se.android.recipe.chapter15;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class MyContentProvider extends ContentProvider {
    // 存取ContentProvider時所建立的Uri的Authority
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15";
    // 包含Authority的Uri字串
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    // 連結到註解欄位的Uri
    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    // 註解的Uri的識別碼
    public static final int CODE_COMMENT = 0;
     // 指定ID的的註解Uri識別碼
    public static final int CODE_COMMENT_ID = 1;

    // 在ContentProvider內可共通使用的SQLiteOpenHelper實例
    private MySQLiteOpenHelper mDatabaseHelper = null;
    // 在識別Uri時使用
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        // 為了識別Uri而建立UriMatcher
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 註冊為了識別註解Uri的類型
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_COMMENT);
        // 註冊指定識別ID的註解Uri類型
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_COMMENT_ID);

        // 取得SQLiteOpenHelper實例
        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        // 本範例沒有對應多個Uri，所以沒有實作
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // 為了建立搜尋query而建立SQLiteQueryBuilder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // 使用UriMatcher來判定從Uri搜尋的資料類別
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT:
            // 設定針對註解的query
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_COMMENT_ID:
            // 設定針對ID指定註解的query
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            // 因為無法預期的資料類別產生的例外
            throw new IllegalArgumentException("Unknown URI");
        }

        // 執行query並返回搜尋結果
        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // 使用UriMatcher來識別從Uri所刪除的資料類別
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // 對註解執行delete並回傳刪除數值
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            // 從Uri取得ID
            long id = ContentUris.parseId(uri);
            // 對ID指定的註解執行delete並回傳刪除數值
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        // 沒有執行處理則回傳0
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 使用UriMatcher來判定從Uri所插入的資料類別
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // 對註解執行insert，從其執行結果所取得的ID來建立Uri並回傳
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        // 沒有執行處理則回傳null�����p
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // 使用UriMatcher來判定從Uri所插入的資料類別
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // 對註解執行update並回傳更新數值
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            long id = ContentUris.parseId(uri);
            // 對ID指定的註解執行update並回傳更新數值
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
        // 沒有執行處理則回傳0
        return 0;
    }

    public int delete(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(table, selection, selectionArgs);
    }

    private long insert(String table, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.insert(table, null, values);
    }

    private int update(String table, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.update(table, values, selection, selectionArgs);
    }

}
