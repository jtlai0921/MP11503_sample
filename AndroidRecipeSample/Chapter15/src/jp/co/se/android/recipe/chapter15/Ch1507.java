package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Ch1507 extends Activity {

    private ListView mListAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1507_main);

        mListAddress = (ListView) findViewById(R.id.listAddress);

        loadAddress();
    }

    private void loadAddress() {
        // �ǥ�ContentProvider�Ө��o�q�T������T
        Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null,
                null, null, null);

        // �إ���ܩҨ��o��T��SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1507_listrow, cursor, new String[] {
                        Phone.DISPLAY_NAME, Phone.NUMBER }, new int[] {
                        R.id.textName, R.id.textPhone }, 0);

        // �N�إ���ܾA�t���ҫإߪ�SimpleCursorAdapter�]�w�bListView�ɐݒ�
        mListAddress.setAdapter(adapter);
    }
}
