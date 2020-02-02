package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Ch1503 extends Activity {
    private static final String CONFIG_NAME = "appconfig";

    private EditText mEditConfigText;
    private CheckBox mCheckConfigCheck1;
    private CheckBox mCheckConfigCheck2;
    private Spinner mSpinnerConfigSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1503_main);

        mEditConfigText = (EditText) findViewById(R.id.editConfigText);
        mCheckConfigCheck1 = (CheckBox) findViewById(R.id.checkConfigCheck1);
        mCheckConfigCheck2 = (CheckBox) findViewById(R.id.checkConfigCheck2);
        mSpinnerConfigSelect = (Spinner) findViewById(R.id.spinnerConfigSelect);

        loadConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveConfig();
    }

    private void loadConfig() {
        // 使用Private來建立CONFIG_NAME的檔案，並取得SharedPreferences實例
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        // 以editConfigText做為鍵來取得文字的值
        mEditConfigText.setText(pref.getString("editConfigText", ""));
        // 以checkConfigCheck1做為鍵來取得Boolean值
        mCheckConfigCheck1.setChecked(pref.getBoolean("checkConfigCheck1",
                false));
        // 以checkConfigCheck2做為鍵來取得Boolean值
        mCheckConfigCheck2.setChecked(pref.getBoolean("checkConfigCheck2",
                false));
        // 以spinnerConfigSelect做為鍵來取得整數值
        mSpinnerConfigSelect
                .setSelection(pref.getInt("spinnerConfigSelect", 0));
    }

    private void saveConfig() {
        // 使用Private來建立CONFIG_NAME的檔案，並取得SharedPreferences實例
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        // 以editConfigText做為鍵來取得文字的值
        editor.putString("editConfigText", mEditConfigText.getText().toString());
        // 以checkConfigCheck1做為鍵來取得Boolean值
        editor.putBoolean("checkConfigCheck1", mCheckConfigCheck1.isChecked());
        // 以checkConfigCheck2做為鍵來取得Boolean值
        editor.putBoolean("checkConfigCheck2", mCheckConfigCheck2.isChecked());
        // 以spinnerConfigSelect做為鍵來取得整數值
        editor.putInt("spinnerConfigSelect",
                mSpinnerConfigSelect.getSelectedItemPosition());

        // 反映設定
        editor.commit();
    }

}
