package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1606 extends Activity {
    private static final String TAG = "Ch1606";
    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco",
            "Lorenzio", "Maryvonne", "Siv",
            "Georgie", "Casimir", "Catharine",
            "Joker"};
    // @formatter:on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1606_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ContactDbOpenHelper helper = null;
                        SQLiteDatabase db = null;
                        try {
                            // ContactDbOpenHelper�𐶐�
                            helper = new ContactDbOpenHelper(Ch1606.this);
                            // ���o�i�g�J��SQLiteDatabase���
                            db = helper.getWritableDatabase();

                            // �إ߸�ƃf�[�^�̍쐬
                            createDataByTransaction(db);
                        } finally {
                            if (db != null) {
                                db.close();
                            }
                            if (helper != null) {
                                helper.close();
                            }
                        }
                    }
                });
    }

    private void createDataByTransaction(SQLiteDatabase db) {
        try {
            // �}�lTransaction
            db.beginTransaction();
            Log.d(TAG, "�}�lTransaction");

            // �N�{�b���x�s����ƥ����R��
            db.delete(Contact.TBNAME, null, null);

            for (int i = 0; i < NAMES.length; i++) {
                // �إ��x�s��ƪ�ContentValues
                ContentValues values = new ContentValues();
                values.put(Contact.NAME, NAMES[i]);
                values.put(Contact.AGE, 20);
                // �b��Ʈw���إ�Contact�����
                db.insert(Contact.TBNAME, null, values);
                Log.d(TAG, String.format("�إ߸��[%d]", i + 1));
            }
            // �T�wTransaction
            db.setTransactionSuccessful();
            Log.d(TAG, "�T�wTransaction");
        } finally {
            // ����Transaction
            db.endTransaction();
            Log.d(TAG, "����Transaction");
        }
    }

}
