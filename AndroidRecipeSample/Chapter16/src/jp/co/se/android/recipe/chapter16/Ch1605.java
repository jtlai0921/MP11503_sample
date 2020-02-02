package jp.co.se.android.recipe.chapter16;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Ch1605 extends Activity {
    private static final String TAG = "Ch1605";

    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco"};
    private String[] IMAGES = new String[]{
            "profile1.png", "profile2.png", "profile3.png",
            "profile4.png", "profile5.png", "profile6.png"
    };
    // @formatter:on

    private ListView mListView;
    private CursorAdapter mAdapter;
    private ProfileDbOpenHelper mHelper = null;
    private SQLiteDatabase mDatabase = null;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1605_main);

        mListView = (ListView) findViewById(R.id.listView1);

        final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number
                // of items.
                return bitmap.getByteCount();
            }
        };

        // ProfileDbOpenHelperÇê∂ê¨
        mHelper = new ProfileDbOpenHelper(this);
        // ®˙±o•iºg§J™∫SQLiteDatabaseπÍ®“
        mDatabase = mHelper.getWritableDatabase();

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // ´ÿ•ﬂ∏ÍÆ∆
                        createData(mDatabase);
                        setCursorAdapter();
                    }
                });

        setCursorAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAdapter != null && mAdapter.getCursor() != null) {
            mAdapter.getCursor().close();
        }

        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    private void createData(SQLiteDatabase db) {
        // ±N≤{¶b¿x¶s™∫∏ÍÆ∆•˛≥°ßR∞£
        db.delete(Profile.TBNAME, null, null);

        for (int i = 0; i < NAMES.length; i++) {
            // ´ÿ•ﬂ¿x¶s∏ÍÆ∆™∫ContentValues
            ContentValues values = new ContentValues();
            values.put(Profile.NAME, NAMES[i]);
            values.put(Profile.PHOTOGRAPHMAGE, getImage(IMAGES[i]));
            // ¶b∏ÍÆ∆Æw´ÿ•ﬂProfile™∫∏ÍÆ∆
            db.insert(Profile.TBNAME, null, values);
        }
    }

    private byte[] getImage(String fileName) {
        InputStream fileInputStream;
        try {
            fileInputStream = getAssets().open("profile/" + fileName);
            byte[] readBytes = new byte[fileInputStream.available()];
            fileInputStream.read(readBytes);
            return readBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setCursorAdapter() {
        Cursor cursor = mDatabase.query(Profile.TBNAME, null, null, null, null,
                null, null);
        if (mAdapter == null) {
            mAdapter = new MyCursorAdapter(this, cursor);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(cursor);
        }
    }

    class MyCursorAdapter extends CursorAdapter {

        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndex(Profile.NAME));
            byte[] bytes = cursor.getBlob(cursor
                    .getColumnIndex(Profile.PHOTOGRAPHMAGE));
            TextView textName = (TextView) view.findViewById(R.id.textName);
            ImageView imagePhotograph = (ImageView) view
                    .findViewById(R.id.imagePhotograph);

            if (name != null) {
                Bitmap bm = mMemoryCache.get(name);
                if (bm == null && bytes != null) {
                    bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    mMemoryCache.put(name, bm);
                }

                textName.setText(name);
                imagePhotograph.setImageBitmap(bm);
            } else {
                textName.setText(null);
                imagePhotograph.setImageBitmap(null);
            }

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater
                    .inflate(R.layout.ch1605_listrow, parent, false);
            return view;
        }

    }
}
