package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class Ch0128 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0128_main);

        // ���]�Q�]�w���ɶ��O2010�~6��30��
        int year = 2010;
        int month = 6 - 1;// �]���O�q0�}�l,�ҥH�]��-1
        int day = 30;

        // �]�w���
        DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);
        dp.updateDate(year, month, day);
    }
}
