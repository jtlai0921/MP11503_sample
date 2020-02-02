package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

public class Ch0202 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0202_main);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        // 建立Adapter
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, WEEK);
        // 設定Adapter
        gridView.setAdapter(adapter);
    }

    // 要在ListView所顯示的字串
    private static final String[] WEEK = new String[] { "一", "二", "三", "四",
            "五", "六", "日" };
}