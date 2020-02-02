package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ch1508 extends Activity {
    private EditText mEditComment;
    private ListView mListComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1508_main);

        mEditComment = (EditText) findViewById(R.id.editComment);
        mListComments = (ListView) findViewById(R.id.listComments);

        findViewById(R.id.buttonSubmit).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        insertComment(mEditComment.getText().toString());
                        mEditComment.setText("");
                        loadComments();
                    }
                });

        loadComments();
    }

    private void insertComment(String comment) {
        // 藉由ContentProvider來建立資料
        ContentValues values = new ContentValues();
        values.put(MySQLiteOpenHelper.COLUMN_COMMENT, comment);
        getContentResolver().insert(MyContentProvider.COMMENTS_CONTENT_URI,
                values);

    }

    private void loadComments() {
        // 藉由ContentProvider來搜尋資料 Cursor cursor = getContentResolver().query(
                MyContentProvider.COMMENTS_CONTENT_URI, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1508_listrow, cursor,
                new String[] { MySQLiteOpenHelper.COLUMN_COMMENT },
                new int[] { R.id.textComment }, 0);

        mListComments.setAdapter(adapter);
    }
}
