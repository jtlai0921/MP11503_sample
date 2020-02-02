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
    // �s��ContentProvider�ɩҫإߪ�Uri��Authority
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15";
    // �]�tAuthority��Uri�r��
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    // �s���������쪺Uri
    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    // ���Ѫ�Uri���ѧO�X
    public static final int CODE_COMMENT = 0;
     // ���wID��������Uri�ѧO�X
    public static final int CODE_COMMENT_ID = 1;

    // �bContentProvider���i�@�q�ϥΪ�SQLiteOpenHelper���
    private MySQLiteOpenHelper mDatabaseHelper = null;
    // �b�ѧOUri�ɨϥ�
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        // ���F�ѧOUri�ӫإ�UriMatcher
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // ���U���F�ѧO����Uri������
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_COMMENT);
        // ���U���w�ѧOID������Uri����
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_COMMENT_ID);

        // ���oSQLiteOpenHelper���
        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        // ���d�ҨS�������h��Uri�A�ҥH�S����@
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // ���F�إ߷j�Mquery�ӫإ�SQLiteQueryBuilder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // �ϥ�UriMatcher�ӧP�w�qUri�j�M��������O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT:
            // �]�w�w����Ѫ�query
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_COMMENT_ID:
            // �]�w�w��ID���w���Ѫ�query
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            // �]���L�k�w����������O���ͪ��ҥ~
            throw new IllegalArgumentException("Unknown URI");
        }

        // ����query�ê�^�j�M���G
        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // �ϥ�UriMatcher���ѧO�qUri�ҧR����������O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ����Ѱ���delete�æ^�ǧR���ƭ�
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            // �qUri���oID
            long id = ContentUris.parseId(uri);
            // ��ID���w�����Ѱ���delete�æ^�ǧR���ƭ�
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        // �S������B�z�h�^��0
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // �ϥ�UriMatcher�ӧP�w�qUri�Ҵ��J��������O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ����Ѱ���insert�A�q����浲�G�Ҩ��o��ID�ӫإ�Uri�æ^��
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        // �S������B�z�h�^��null��ԋp
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // �ϥ�UriMatcher�ӧP�w�qUri�Ҵ��J��������O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ����Ѱ���update�æ^�ǧ�s�ƭ�
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            long id = ContentUris.parseId(uri);
            // ��ID���w�����Ѱ���update�æ^�ǧ�s�ƭ�
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
        // �S������B�z�h�^��0
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
