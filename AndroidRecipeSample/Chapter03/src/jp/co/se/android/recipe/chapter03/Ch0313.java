package jp.co.se.android.recipe.chapter03;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Ch0313 extends Activity implements OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0313_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTextView = (TextView) findViewById(R.id.text);

        setupNavigationDrawer();

        // 資料總覽設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);
        adapter.add("Menu1");
        adapter.add("Menu2");
        adapter.add("Menu3");
        mDrawerList.setAdapter(adapter);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void setupNavigationDrawer() {
        // 指定要設定為NavigationDrawer陰影的Drawable
        mDrawerLayout.setDrawerShadow(R.drawable.ch0313_drawer_shadow,
                GravityCompat.START);
        mDrawerList.setOnItemClickListener(this);

        // 在ActionBar圖示的左側顯示DrawerToggle
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // 將ActionBar的Home鈕設定生效
        getActionBar().setHomeButtonEnabled(true);

        // 取得Drawer開關時的事件
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.ch0313_drawer_open,
                R.string.ch0313_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 使DrawerToggle可以監聽選單選項中的選擇
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 使DrawerToggle可以控制選項選單
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 使DrawerToggle可以控制上下的變更
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View parent,
            int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        ListAdapter adapter = mDrawerList.getAdapter();
        String item = (String) adapter.getItem(position);
        mTextView.setText("所選擇的項目: " + item);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
