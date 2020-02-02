package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class Ch0215 extends Activity {
    private Dialog mDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 設定自訂View
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View buttons = inflater.inflate(
                R.layout.ch0215_dialog_button_item,
                (ViewGroup) findViewById(R.id.layout_root));

        // 產生警示對話框
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.Ch0215_DialogTheme);
        builder.setTitle("自訂對話框");
        builder.setMessage("已自訂對話框的主題");
        builder.setView(buttons);
        buttons.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialog != null) {
                    Toast.makeText(Ch0215.this, getString(android.R.string.ok),
                            Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
        buttons.findViewById(R.id.cancel).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (mDialog != null) {
                            Toast.makeText(Ch0215.this,
                                    getString(android.R.string.cancel),
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });

        mDialog = builder.create();
        mDialog.show();
    }
}
