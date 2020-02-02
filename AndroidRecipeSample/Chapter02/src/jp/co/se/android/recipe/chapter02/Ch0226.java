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

        // �b�]�wsetContentView���e���]�wWindow��ActionBar���
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.ch0226_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ch0226_menu, menu);

        //�s�W��檺����
        MenuItem actionItem = menu.add("�q�{�����s�W������");

        // SHOW_AS_ACTION_IF_ROOM�G��ܬO�_���Ѿl�FSHOW_AS_ACTION_WITH_TEXT�G�P����ܤ�r
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        //�ϥܳ]�w
        actionItem.setIcon(android.R.drawable.ic_menu_compass);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "��ܪ�����: " + item.getTitle(), Toast.LENGTH_SHORT)
                .show();
        return true;
    }
}