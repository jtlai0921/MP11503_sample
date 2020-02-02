package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ch1509 extends Activity {
    private ListView mListComments;

    private String[] SAMPLE_COMMENTS = new String[] { "蘋果", "橘子", "梨子",
            "草莓", "西瓜", "番茄", "柿子" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1509_main);

        mListComments = (ListView) findViewById(R.id.listComments);

        findViewById(R.id.buttonSubmit).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        insertComment();
                        loadComments();
                    }
                });

        loadComments();
    }

    private void insertComment() {
        ContentProviderClient client = null;
        try {
            // 取得ContentProviderClient實例
            client = getContentResolver().acquireContentProviderClient(
                    MyContentProvider.COMMENTS_CONTENT_URI);
            for (int i = 0; i < SAMPLE_COMMENTS.length; i++) {
                ContentValues values = new ContentValues();
                values.put(MySQLiteOpenHelper.COLUMN_COMMENT,
                        SAMPLE_COMMENTS[i]);
                // 藉由ContentProvider來建立資料
                client.insert(MyContentProvider.COMMENTS_CONTENT_URI, values);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            // 釋放ContentProviderClient實例
            if (client != null) {
                client.release();
            }
        }

    }

    private void loadComments() {
        Cursor cursor = getContentResolver().query(
                MyContentProvider.COMMENTS_CONTENT_URI, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1508_listrow, cursor,
                new String[] { MySQLiteOpenHelper.COLUMN_COMMENT },
                new int[] { R.id.textComment }, 0);

        mListComments.setAdapter(adapter);
    }
}
