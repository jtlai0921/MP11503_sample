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

        //´ÿ•ﬂ•Œ®”≈„•‹ListView™∫Adapter
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        // ´ÿ•ﬂ¥˙∏’∏ÍÆ∆
        for (int i = 1; i < 10; i++) {
            mAdapter.add("Item" + i);
        }

        // ≤£•Õ≈™®˙§§™∫≠∂ß¿
        mFooter = getLayoutInflater().inflate(
                R.layout.ch0207_list_progress_item, null);

        // ¶bListView≥]©w≠∂ß¿
        mListView.addFooterView(mFooter);

        //≥]©wAdapter
        mListView.setAdapter(mAdapter);

        // ≥]©w±≤∞ ∫ ≈•æπ
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // ®Ïß¿∫›Æ…¶A≈™®˙±µ§U®”™∫∏Í∞T
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    private void additionalReading() {
        // ≠Y§w∏g¶b≈™®˙§§¥N≤§πL
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        //≥q±`≥£¨O±q∫Ù∏Ù©Œ¿…Æ◊®”≈™®˙∏Í∞T°A©“•HπÍß@´D¶P®B≈™®˙≥B≤z
        //•ªΩd®“¶]¬≤≤§§∆™∫√ˆ´Y°A¶b´D¶P®B≥B≤z™∫±°™p°A≥£¨O∫˚´˘≠Ï•ª™∫§Ë¶°
        //≈™®˙∏Í∞T¨O¨∞§F≠n®Ë∑N≤£•Õ©µø™∫πÍß@
        mTask = new MyAsyncTask(this).execute("text");
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public MyAsyncTask(Ch0207 androidAsyncTaskActivity) {
        }

        protected String doInBackground(String... params) {
            // 2ïbé~ÇﬂÇÈ
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String text) {
            // ÉfÅ[É^í«â¡
            for (int n = 0; n < 10; n++) {
                mAdapter.add(text + n);
            }
        }

    }
}