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
        checkDlg.setTitle("���D");
        checkDlg.setMultiChoiceItems(chkItems, chkSts,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which,
                            boolean flag) {
                        // ���ؿ�ܮɪ��B�z
                        // i�O�Q��ܶ��ت�����
                        // flag�O�֨��Ŀ諸���A
                    }
                });
        checkDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //���
        checkDlg.create().show();
    }
}