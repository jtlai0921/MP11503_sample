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

        //�إߥΨ����ListView��Adapter
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        // �إߴ��ո��
        for (int i = 1; i < 10; i++) {
            mAdapter.add("Item" + i);
        }

        // ����Ū����������
        mFooter = getLayoutInflater().inflate(
                R.layout.ch0207_list_progress_item, null);

        // �bListView�]�w����
        mListView.addFooterView(mFooter);

        //�]�wAdapter
        mListView.setAdapter(mAdapter);

        // �]�w���ʺ�ť��
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // ����ݮɦAŪ�����U�Ӫ���T
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    private void additionalReading() {
        // �Y�w�g�bŪ�����N���L
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        //�q�`���O�q�������ɮר�Ū����T�A�ҥH��@�D�P�BŪ���B�z
        //���d�Ҧ]²���ƪ����Y�A�b�D�P�B�B�z�����p�A���O�����쥻���覡
        //Ū����T�O���F�n��N���ͩ��𪺹�@
        mTask = new MyAsyncTask(this).execute("text");
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public MyAsyncTask(Ch0207 androidAsyncTaskActivity) {
        }

        protected String doInBackground(String... params) {
            // 2�b�~�߂�
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String text) {
            // �f�[�^�ǉ�
            for (int n = 0; n < 10; n++) {
                mAdapter.add(text + n);
            }
        }

    }
}