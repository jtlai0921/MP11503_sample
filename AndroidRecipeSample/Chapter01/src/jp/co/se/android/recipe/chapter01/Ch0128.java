package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class Ch0128 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0128_main);

        // 假設想設定的時間是2010年6月30日
        int year = 2010;
        int month = 6 - 1;// 因為是從0開始,所以設為-1
        int day = 30;

        // 設定日期
        DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);
        dp.updateDate(year, month, day);
    }
}
