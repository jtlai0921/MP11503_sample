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
    // ¦s¨úContentProvider®É©Ò«Ø¥ßªºUriªºAuthority
    public static final String AUTHORITY = "jp.co.se.android.recipe.chapter15";
    // ¥]§tAuthorityªºUri¦r¦ê
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY
            + "/";

    // ³sµ²¨ìµù¸ÑÄæ¦ìªºUri
    public static final Uri COMMENTS_CONTENT_URI = Uri.parse(CONTENT_AUTHORITY
            + MySQLiteOpenHelper.TABLE_COMMENTS);

    // µù¸ÑªºUriªºÃÑ§O½X
    public static final int CODE_COMMENT = 0;
     // «ü©wIDªºªºµù¸ÑUriÃÑ§O½X
    public static final int CODE_COMMENT_ID = 1;

    // ¦bContentProvider¤º¥i¦@³q¨Ï¥ÎªºSQLiteOpenHelper¹ê¨Ò
    private MySQLiteOpenHelper mDatabaseHelper = null;
    // ¦bÃÑ§OUri®É¨Ï¥Î
    private UriMatcher mUriMatcher = null;

    @Override
    public boolean onCreate() {
        // ¬°¤FÃÑ§OUri¦Ó«Ø¥ßUriMatcher
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // µù¥U¬°¤FÃÑ§Oµù¸ÑUriªºÃþ«¬
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS,
                CODE_COMMENT);
        // µù¥U«ü©wÃÑ§OIDªºµù¸ÑUriÃþ«¬
        mUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_COMMENTS + "/#",
                CODE_COMMENT_ID);

        // ¨ú±oSQLiteOpenHelper¹ê¨Ò
        mDatabaseHelper = new MySQLiteOpenHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {
        // ¥»½d¨Ò¨S¦³¹ïÀ³¦h­ÓUri¡A©Ò¥H¨S¦³¹ê§@
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // ¬°¤F«Ø¥ß·j´Mquery¦Ó«Ø¥ßSQLiteQueryBuilder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // ¨Ï¥ÎUriMatcher¨Ó§P©w±qUri·j´Mªº¸ê®ÆÃþ§O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT:
            // ³]©w°w¹ïµù¸Ñªºquery
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            break;
        case CODE_COMMENT_ID:
            // ³]©w°w¹ïID«ü©wµù¸Ñªºquery
            queryBuilder.setTables(MySQLiteOpenHelper.TABLE_COMMENTS);
            queryBuilder.appendWhere(BaseColumns._ID + "="
                    + uri.getLastPathSegment());
            break;
        default:
            // ¦]¬°µLªk¹w´Áªº¸ê®ÆÃþ§O²£¥Íªº¨Ò¥~
            throw new IllegalArgumentException("Unknown URI");
        }

        // °õ¦æquery¨Ãªð¦^·j´Mµ²ªG
        Cursor cursor = queryBuilder.query(
                mDatabaseHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // ¨Ï¥ÎUriMatcher¨ÓÃÑ§O±qUri©Ò§R°£ªº¸ê®ÆÃþ§O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ¹ïµù¸Ñ°õ¦ædelete¨Ã¦^¶Ç§R°£¼Æ­È
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            // ±qUri¨ú±oID
            long id = ContentUris.parseId(uri);
            // ¹ïID«ü©wªºµù¸Ñ°õ¦ædelete¨Ã¦^¶Ç§R°£¼Æ­È
            return delete(MySQLiteOpenHelper.TABLE_COMMENTS, BaseColumns._ID
                    + " = ?", new String[] { Long.toString(id) });
        }
        }
        // ¨S¦³°õ¦æ³B²z«h¦^¶Ç0
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // ¨Ï¥ÎUriMatcher¨Ó§P©w±qUri©Ò´¡¤Jªº¸ê®ÆÃþ§O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ¹ïµù¸Ñ°õ¦æinsert¡A±q¨ä°õ¦æµ²ªG©Ò¨ú±oªºID¨Ó«Ø¥ßUri¨Ã¦^¶Ç
            return ContentUris.withAppendedId(COMMENTS_CONTENT_URI,
                    insert(MySQLiteOpenHelper.TABLE_COMMENTS, values));
        }
        }
        // ¨S¦³°õ¦æ³B²z«h¦^¶Çnull‚ð•Ô‹p
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // ¨Ï¥ÎUriMatcher¨Ó§P©w±qUri©Ò´¡¤Jªº¸ê®ÆÃþ§O
        int match = mUriMatcher.match(uri);
        switch (match) {
        case CODE_COMMENT: {
            // ¹ïµù¸Ñ°õ¦æupdate¨Ã¦^¶Ç§ó·s¼Æ­È
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values, selection,
                    selectionArgs);
        }
        case CODE_COMMENT_ID: {
            long id = ContentUris.parseId(uri);
            // ¹ïID«ü©wªºµù¸Ñ°õ¦æupdate¨Ã¦^¶Ç§ó·s¼Æ­È
            return update(MySQLiteOpenHelper.TABLE_COMMENTS, values,
                    BaseColumns._ID + " = ?",
                    new String[] { Long.toString(id) });
        }
        }
        // ¨S¦³°õ¦æ³B²z«h¦^¶Ç0
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
