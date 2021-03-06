package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0207 extends Activity implements OnScrollListener {

    private ArrayAdapter<String> mAdapter;
    private AsyncTask<String, Void, String> mTask;
    private ListView mListView;
    private View mFooter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        mListView = (ListView) findViewById(R.id.ListView);

        //建立用來顯示ListView的Adapter
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        // 建立測試資料
        for (int i = 1; i < 10; i++) {
            mAdapter.add("Item" + i);
        }

        // 產生讀取中的頁尾
        mFooter = getLayoutInflater().inflate(
                R.layout.ch0207_list_progress_item, null);

        // 在ListView設定頁尾
        mListView.addFooterView(mFooter);

        //設定Adapter
        mListView.setAdapter(mAdapter);

        // 設定捲動監聽器
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // 到尾端時再讀取接下來的資訊
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    private void additionalReading() {
        // 若已經在讀取中就略過
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        //通常都是從網路或檔案來讀取資訊，所以實作非同步讀取處理
        //本範例因簡略化的關係，在非同步處理的情況，都是維持原本的方式
        //讀取資訊是為了要刻意產生延遲的實作
        mTask = new MyAsyncTask(this).execute("text");
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public MyAsyncTask(Ch0207 androidAsyncTaskActivity) {
        }

        protected String doInBackground(String... params) {
            // 2�b�~����
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String text) {
            // �f�[�^����
            for (int n = 0; n < 10; n++) {
                mAdapter.add(text + n);
            }
        }

    }
}