package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Ch0205 extends ListActivity {
    /***
     * 索引資訊
     * 
     * @author yokmama
     * 
     */
    private class BindData {
        String title;
        String line1;
        String line2;

        public BindData(String string0, String string1, String string2) {
            this.title = string0;
            this.line1 = string1;
            this.line2 = string2;
        }
    }

    // 顯示索引用的範例資訊
    private BindData[] INDEX_DATA = new BindData[] {
            new BindData("標題1", null, null),
            new BindData(null, "foo", "bar"),
            new BindData("標題2", null, null),
            new BindData(null, "hoge", "fuga"),
            new BindData(null, "null", "po"),
            new BindData("標題3", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題4", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題5", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題6", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題7", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題8", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題9", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("標題10", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"), };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0205_main);

        // 新增索引資訊
        List<BindData> list = new ArrayList<BindData>();
        for (int i = 0; i < INDEX_DATA.length; i++) {
            list.add(INDEX_DATA[i]);
        }

        // 建立顯示索引的Adapter
        Ch0205Adapter adapter = new Ch0205Adapter(this, list);

        // 設定Adapter
        setListAdapter(adapter);
    }

    /***
     * 為了使清單高速顯示而保留的View
     * 
     * @author yokmama
     * 
     */
    private class ViewHolder {
        TextView title;
        TextView line1;
        TextView line2;
    }

    private class Ch0205Adapter extends ArrayAdapter<BindData> {
        private LayoutInflater mInflater;

        public Ch0205Adapter(Context context, List<BindData> objects) {
            super(context, 0, objects);
            this.mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public boolean isEnabled(int position) {
            // 設為不可選擇
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // 讀取建立清單項目顯示用的版面配置
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ch0205_list_item,
                        parent, false);
                // 為了高速清單顯示，建立儲存View用途的類別，並在Tag做設定
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.line1 = (TextView) convertView.findViewById(R.id.line1);
                holder.line2 = (TextView) convertView.findViewById(R.id.line2);
                convertView.setTag(holder);
            } else {
                // 從Tag取得保存View用途的實例
                holder = (ViewHolder) convertView.getTag();
            }

            // 從設定在Adapter的清單取得BindData
            BindData data = getItem(position);

            if (getItem(position).title != null) {
                // 從索引用的索引資訊顯示索引標題
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(data.title);
                holder.line1.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                // line1,2
            } else {
                // 若非索引用的資訊就只顯示文字
                holder.title.setVisibility(View.GONE);
                holder.line1.setVisibility(View.VISIBLE);
                holder.line1.setText(data.line1);
                holder.line2.setVisibility(View.VISIBLE);
                holder.line2.setText(data.line2);
            }
            return convertView;
        }
    }
}