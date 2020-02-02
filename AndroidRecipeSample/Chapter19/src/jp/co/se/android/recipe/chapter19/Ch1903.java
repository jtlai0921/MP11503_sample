package jp.co.se.android.recipe.chapter19;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch1903 extends Activity implements OnRefreshListener {
    private static final String TAG = Ch1903.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mListView;

    private AsyncTask<Void, Void, String[]> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1903_main);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mListView = (ListView) findViewById(android.R.id.list);

        mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2,
                R.color.color3, R.color.color4);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        request();
    }

    @Override
    public void onRefresh() {
        request();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ch1903_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_refresh) {
            request();
        }
        return super.onOptionsItemSelected(item);
    }

    private void request() {
        if (mTask != null) {
            return;
        }
        mSwipeRefreshWidget.setRefreshing(true);
        mSwipeRefreshWidget.setEnabled(false);
        mTask = new AsyncTask<Void, Void, String[]>() {

            @Override
            protected String[] doInBackground(Void... params) {
                // 因為只是範例而暫時休眠
                // 一般會進行網絡存取或較煩重的處理
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Failed sleep", e);
                }

                // 回傳做為其結果的虛擬資料
                final String[] words = { "Lorem", "ipsum", "dolor", "sit",
                        "amet,", "consectetur", "adipisicing", "elit,", "sed",
                        "do", "eiusmod", "tempor", "incididunt", "ut",
                        "labore", "et", "dolore", "magna", "aliqua.", };
                return words;
            }

            @Override
            protected void onPostExecute(String[] result) {
                mSwipeRefreshWidget.setRefreshing(false);
                mSwipeRefreshWidget.setEnabled(true);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        Ch1903.this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, result);
                mListView.setAdapter(arrayAdapter);

                mTask = null;
            }
        };
        mTask.execute();
    }

}
