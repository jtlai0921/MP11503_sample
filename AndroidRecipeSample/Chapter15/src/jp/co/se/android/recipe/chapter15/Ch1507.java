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
        // 藉由ContentProvider來取得通訊錄的資訊
        Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null,
                null, null, null);

        // 建立顯示所取得資訊的SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1507_listrow, cursor, new String[] {
                        Phone.DISPLAY_NAME, Phone.NUMBER }, new int[] {
                        R.id.textName, R.id.textPhone }, 0);

        // 將建立顯示適配器所建立的SimpleCursorAdapter設定在ListView������
        mListAddress.setAdapter(adapter);
    }
}
