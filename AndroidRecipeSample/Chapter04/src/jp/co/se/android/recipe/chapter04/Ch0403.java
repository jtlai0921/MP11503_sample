package jp.co.se.android.recipe.chapter04;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0403 extends Activity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0403_main);

        mListView = (ListView) findViewById(android.R.id.list);

        ArrayList<String> appList = createAppList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, appList);
        mListView.setAdapter(adapter);
    }

    private ArrayList<String> createAppList() {
        ArrayList<String> list = new ArrayList<String>();

        // 取得對應ACTION_VIEW動作的應用程式總覽
        PackageManager pm = getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (Iterator<ResolveInfo> i = activities.iterator(); i.hasNext();) {
            ResolveInfo activity = i.next();

            // 做為字串輸出
            list.add(String.format(
                    "應用程式名稱: %1$s\nActivity: %2$s",
                    pm.getApplicationLabel(activity.activityInfo.applicationInfo),
                    activity.activityInfo.name));
        }

        return list;
    }
}
