package jp.co.se.android.recipe.chapter16;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Ch1608 extends Activity {
    private static final String TAG = "Test";
    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco",
            "Lorenzio", "Maryvonne", "Siv",
            "Georgie", "Casimir", "Catharine",
            "Joker"};
    // @formatter:on

    private Random mRand = new Random(System.currentTimeMillis());
    private ListView mListView;
    private CursorAdapter mAdapter;
    private ContactDbOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private boolean isStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1608_main);

        mListView = (ListView) findViewById(R.id.listView1);
        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadList();
                    }
                });
        mHelper = new ContactDbOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        // WAL設定生效
        mDb.enableWriteAheadLogging();

        startWriteTest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
        if (mAdapter != null && mAdapter.getCursor() != null) {
            mAdapter.getCursor().close();
        }
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

    protected void loadList() {
        Log.d(TAG, "loadList start WAL:" + mDb.isWriteAheadLoggingEnabled());
        Cursor cursor = mDb.query(Contact.TBNAME, null, null, null, null, null,
                null);
        if (mAdapter == null) {
            mAdapter = new MyCursorAdapter(this, cursor);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(cursor);
        }
        Log.d(TAG, "loadList end");
    }

    private void testInsert(SQLiteDatabase db) {
        Log.d(TAG, "create index one");
        String name = NAMES[mRand.nextInt(NAMES.length)];
        int age = mRand.nextInt(80);
        ContentValues values = new ContentValues();
        values.put(Contact.NAME, name);
        values.put(Contact.AGE, age);

        db.insert(Contact.TBNAME, null, values);
    }

    private void startWriteTest() {
        isStop = false;

        // 到應用程式結束為止，每間隔1秒就產生測試資料，並在這段期間維持鎖定
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mDb.beginTransaction();

                    // 先將資料全部刪除
                    mDb.delete(Contact.TBNAME, null, null);

                    do {
                        // 建立一筆資料
                        testInsert(mDb);

                        // 為了進行測試，故意停止程式1秒
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (!isStop);
                    if (mDb != null) {
                        mDb.setTransactionSuccessful();
                    }
                } finally {
                    if (mDb != null) {
                        mDb.endTransaction();
                    }
                }
            }
        });
        t.start();
    }

    private class MyCursorAdapter extends CursorAdapter {
        public MyCursorAdapter(Context context, Cursor c) {
            super(context, c, false);
        }

        @Override
        public void bindView(View arg0, Context arg1, Cursor arg2) {
            String name = arg2.getString(arg2.getColumnIndex(Contact.NAME));
            String age = arg2.getString(arg2.getColumnIndex(Contact.AGE));

            TextView textName = (TextView) arg0.findViewById(R.id.textName);
            TextView textAge = (TextView) arg0.findViewById(R.id.textAge);

            textName.setText(name);
            textAge.setText(age);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater
                    .inflate(R.layout.ch1608_listrow, parent, false);
            return view;
        }
    }
}
