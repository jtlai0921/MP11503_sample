package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;

public class Ch0210 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0210_main);

        // 建立清單
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        for (int i = 0; i < 100; i++) {
            adapter.add("hoge" + i);
        }
        final ListView lv = (ListView) findViewById(R.id.ListView);
        lv.setAdapter(adapter);

        // 切換高速捲軸
        final Switch swFastScroll = (Switch) findViewById(R.id.FastScroller);
        swFastScroll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {

                lv.setFastScrollEnabled(isChecked);
                lv.setFastScrollAlwaysVisible(isChecked);
            }
        });
    }
}
