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

public class MySecureContentProvider extends ContentProvider {
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15.secure";
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    public static final int CODE_LOGINID = 0;
    public static final int CODE_LOGINID_ID = 1;

    private MySQLiteOpenHelper mDatabaseHelper = null;
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_LOGINID);
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_LOGINID_ID);

        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_LOGINID:
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_LOGINID_ID:
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_LOGINID: {
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_LOGINID_ID: {
            long id = ContentUris.parseId(uri);
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_LOGINID: {
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_LOGINID: {
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_LOGINID_ID: {
            long id = ContentUris.parseId(uri);
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
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
