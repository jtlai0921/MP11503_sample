package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Ch0214 extends Activity {
    /**Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 設定自訂的View
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(
                R.layout.ch0214_dialog_contents_item,
                (ViewGroup) findViewById(R.id.dialogcustom));

        // 產生警示對話框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("視窗標題");
        builder.setView(layout);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // [OK]鈕的點擊處理
                EditText id = (EditText) layout.findViewById(R.id.customDialog);
                String strId = id.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // [Cancel]鈕的點擊處理
			}
        });

        // 顯示
        builder.create().show();
    }
}