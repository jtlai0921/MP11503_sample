package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class Ch0213 extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CharSequence[] chkItems = { "item1", "item2", "item3" };
        final boolean[] chkSts = { true, false, false };
        AlertDialog.Builder checkDlg = new AlertDialog.Builder(this);
        checkDlg.setTitle("標題");
        checkDlg.setMultiChoiceItems(chkItems, chkSts,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which,
                            boolean flag) {
                        // 項目選擇時的處理
                        // i是被選擇項目的索引
                        // flag是核取勾選的狀態
                    }
                });
        checkDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //顯示
        checkDlg.create().show();
    }
}