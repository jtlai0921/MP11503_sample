package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0204 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        View header = (View) getLayoutInflater().inflate(
                R.layout.ch0204_list_header_item, null);
        View footer = (View) getLayoutInflater().inflate(
                R.layout.ch0204_list_fotter_item, null);

        ListView listView = (ListView) findViewById(R.id.ListView);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //當某項目被點擊時, 註冊所要呼叫的回呼(callback)
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // 在頁首時不做任何動作
                if (position == 0) {
                    return;
                }
            }
        });

        // 建立Adapter
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, DAYS));
        // 設定使它不會被聚焦（Focus）
        listView.setItemsCanFocus(false);
    }

    // 要在ListView顯示的字串
    private static final String[] DAYS = new String[] { "Sunday", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
            "Thursday", "Friday", "Saturday", "Thursday", "Friday", "Saturday",
            "Thursday", "Friday", "Saturday", };

}
