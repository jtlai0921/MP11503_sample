package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class Ch0226 extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 在設定setContentView之前先設定Window的ActionBar顯示
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.ch0226_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ch0226_menu, menu);

        //新增選單的元素
        MenuItem actionItem = menu.add("從程式中新增的項目");

        // SHOW_AS_ACTION_IF_ROOM：顯示是否有剩餘；SHOW_AS_ACTION_WITH_TEXT：同時顯示文字
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        //圖示設定
        actionItem.setIcon(android.R.drawable.ic_menu_compass);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "選擇的項目: " + item.getTitle(), Toast.LENGTH_SHORT)
                .show();
        return true;
    }
}