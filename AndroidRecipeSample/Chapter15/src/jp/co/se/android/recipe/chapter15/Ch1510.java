package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ch1510 extends Activity {
    private EditText mEditComment;
    private ListView mListComments;

    private ContentObserver mUpdateObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            loadComments();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1510_main);

        mEditComment = (EditText) findViewById(R.id.editComment);
        mListComments = (ListView) findViewById(R.id.listComments);

        findViewById(R.id.buttonSubmit).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        insertComment(mEditComment.getText().toString());
                        mEditComment.setText("");
                    }
                });

        // 註冊ContentObserver
        getContentResolver().registerContentObserver(
                MyContentProvider.COMMENTS_CONTENT_URI, true, mUpdateObserver);

        loadComments();
    }

    @Override
    public void onDestroy() {
        // 解除ContentObserver的註冊
        getContentResolver().unregisterContentObserver(mUpdateObserver);

        super.onDestroy();
    }

    private void insertComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteOpenHelper.COLUMN_COMMENT, comment);
        getContentResolver().insert(MyContentProvider.COMMENTS_CONTENT_URI,
                values);
        // 通知變更
        getContentResolver().notifyChange(
                MyContentProvider.COMMENTS_CONTENT_URI, null);

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
