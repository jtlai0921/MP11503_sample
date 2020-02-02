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

        // ���M��
        ArrayList<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
        // �l�M��
        ArrayList<ArrayList<HashMap<String, String>>> childData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // �b���M��s�W����
        HashMap<String, String> groupA = new HashMap<String, String>();
        groupA.put("group", "�U");
        HashMap<String, String> groupB = new HashMap<String, String>();
        groupB.put("group", "��");

        groupData.add(groupA);
        groupData.add(groupB);

        // �b�l�M��s�W����(1)
        ArrayList<HashMap<String, String>> childListA = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childAA = new HashMap<String, String>();
        childAA.put("group", "�U");
        childAA.put("name", "�饻�U");
        HashMap<String, String> childAB = new HashMap<String, String>();
        childAB.put("group", "�U");
        childAB.put("name", "���u��");
        HashMap<String, String> childAC = new HashMap<String, String>();
        childAC.put("group", "�U");
        childAC.put("name", "����U");

        childListA.add(childAA);
        childListA.add(childAB);
        childListA.add(childAC);

        childData.add(childListA);

        // �b�l�M��s�W����(2)
        ArrayList<HashMap<String, String>> childListB = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> childBA = new HashMap<String, String>();
        childBA.put("group", "��");
        childBA.put("name", "��");
        HashMap<String, String> childBB = new HashMap<String, String>();
        childBB.put("group", "��");
        childBB.put("name", "����");

        childListB.add(childBA);
        childListB.add(childBB);

        childData.add(childListB);

        // ���ͧt�����M��B�l�M�檺Adapter
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getApplicationContext(), groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "group" }, new int[] { android.R.id.text1 },
                childData, android.R.layout.simple_expandable_list_item_2,
                new String[] { "name", "group" }, new int[] {
                        android.R.id.text1, android.R.id.text2 });

        // �bExpandableListView�]�wAdapter
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ExpandablelistView);
        listView.setAdapter(adapter);
    }
}