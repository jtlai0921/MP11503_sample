package jp.co.se.android.recipe.chapter19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;

public class Ch1904 extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1904_main);

        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    /**
     * jsoup‚É‚ÄElements‚ðŽæ“¾
     */
    private class MyAsyncTask extends AsyncTask<Void, Void, Elements> {

        @Override
        protected Elements doInBackground(Void... params) {
            Elements result = null;
            try {
                Document doc = Jsoup
                        .connect("http://books.shoeisha.co.jp")
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.116 Safari/537.36")
                        .get();
                // ¨Ï¥ÎCSS¿ï¾Ü¾¹¨ú±oimgÁ`Äý
                result = doc.select("#mainBookList img[src^=/images/book]");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Elements result) {
            if (result != null) {
                List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
                for (Element element : result) {
                    Map<String, String> m = new HashMap<String, String>();
                    m.put("src", element.attr("src"));
                    m.put("alt", element.attr("alt"));
                    dataList.add(m);
                }
                SimpleAdapter adapter = new SimpleAdapter(Ch1904.this,
                        dataList, android.R.layout.simple_list_item_2,
                        new String[] { "alt", "src" }, new int[] {
                                android.R.id.text1, android.R.id.text2 });
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        }
    }
}
