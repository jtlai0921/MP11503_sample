package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class Ch0203 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0203_main);

        // 父清單
        ArrayList<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
        // 子清單
        ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 在父清單新增元素
        HashMap<String, String> groupA = new HashMap<String, String>();
        groupA.put("group", "猴");
        HashMap<String, String> groupB = new HashMap<String, String>();
        groupB.put("group", "鳥");

        groupData.add(groupA);
        groupData.add(groupB);

        // 在子清單新增元素(1)
        ArrayList<HashMap<String, String>> childListA = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childAA = new HashMap<String, String>();
        childAA.put("group", "猴");
        childAA.put("name", "日本猴");
        HashMap<String, String> childAB = new HashMap<String, String>();
        childAB.put("group", "猴");
        childAB.put("name", "長臂猿");
        HashMap<String, String> childAC = new HashMap<String, String>();
        childAC.put("group", "猴");
        childAC.put("name", "眼鏡猴");

        childListA.add(childAA);
        childListA.add(childAB);
        childListA.add(childAC);

        childData.add(childListA);

        // 在子清單新增元素(2)
        ArrayList<HashMap<String, String>> childListB = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childBA = new HashMap<String, String>();
        childBA.put("group", "鳥");
        childBA.put("name", "雞");
        HashMap<String, String> childBB = new HashMap<String, String>();
        childBB.put("group", "鳥");
        childBB.put("name", "雀鳥");

        childListB.add(childBA);
        childListB.add(childBB);

        childData.add(childListB);

        // 產生含有父清單、子清單的Adapter
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getApplicationContext(), groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "group" }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { "name", "group" }, new int[] {
                        android.R.id.text1, android.R.id.text2 });

        // 在ExpandableListView設定Adapter
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ExpandablelistView);
        listView.setAdapter(adapter);
    }
}