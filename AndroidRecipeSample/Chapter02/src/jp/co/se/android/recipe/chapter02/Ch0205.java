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
     * ���޸�T
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

    // ��ܯ��ޥΪ��d�Ҹ�T
    private BindData[] INDEX_DATA = new BindData[] {
            new BindData("���D1", null, null),
            new BindData(null, "foo", "bar"),
            new BindData("���D2", null, null),
            new BindData(null, "hoge", "fuga"),
            new BindData(null, "null", "po"),
            new BindData("���D3", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D4", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D5", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D6", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D7", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D8", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D9", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("���D10", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"), };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0205_main);

        // �s�W���޸�T
        List<BindData> list = new ArrayList<BindData>();
        for (int i = 0; i < INDEX_DATA.length; i++) {
            list.add(INDEX_DATA[i]);
        }

        // �إ���ܯ��ު�Adapter
        Ch0205Adapter adapter = new Ch0205Adapter(this, list);

        // �]�wAdapter
        setListAdapter(adapter);
    }

    /***
     * ���F�ϲM�氪�t��ܦӫO�d��View
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
            // �]�����i���
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // Ū���إ߲M�涵����ܥΪ������t�m
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ch0205_list_item,
                        parent, false);
                // ���F���t�M����ܡA�إ��x�sView�γ~�����O�A�æbTag���]�w
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.line1 = (TextView) convertView.findViewById(R.id.line1);
                holder.line2 = (TextView) convertView.findViewById(R.id.line2);
                convertView.setTag(holder);
            } else {
                // �qTag���o�O�sView�γ~�����
                holder = (ViewHolder) convertView.getTag();
            }

            // �q�]�w�bAdapter���M����oBindData
            BindData data = getItem(position);

            if (getItem(position).title != null) {
                // �q���ޥΪ����޸�T��ܯ��޼��D
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(data.title);
                holder.line1.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                // line1,2
            } else {
                // �Y�D���ޥΪ���T�N�u��ܤ�r
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