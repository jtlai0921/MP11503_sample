package jp.co.se.android.recipe.chapter01;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Ch0121 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0121_main);

        //建立輸入建議用的清單
        List<String> colorList = new ArrayList<String>();
        colorList.add("喜歡的顏色是紅色");
        colorList.add("喜歡的顏色是藍色");
        colorList.add("喜歡的顏色是綠色");

        //設定輸入建議
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        SuggestAdapter adapter = new SuggestAdapter(this, colorList);
        textView.setAdapter(adapter);
    }

    /**
     * �����T�W�F�X�g�\�����g�p�����A�_�v�^.
     */
    private class SuggestAdapter extends ArrayAdapter<String> implements
            SpinnerAdapter {
        private LayoutInflater mInflator;
        private List<String> mItems;

        public SuggestAdapter(Context context, List<String> objects) {
            super(context, R.layout.ch0121_item_suggest, objects);
            mInflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItems = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.ch0121_item_suggest,
                        null, false);
                holder = new ViewHolder();
                holder.text = (TextView) convertView
                        .findViewById(R.id.spinnerText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //設定建議字辭候補
            holder.text.setText(mItems.get(position));

            return convertView;
        }

        private class ViewHolder {
            TextView text;
        }
    }
}
