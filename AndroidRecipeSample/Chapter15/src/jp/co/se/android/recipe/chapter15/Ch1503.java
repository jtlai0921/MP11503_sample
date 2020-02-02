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
        // �ϥ�Private�ӫإ�CONFIG_NAME���ɮסA�è��oSharedPreferences���
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        // �HeditConfigText������Ө��o��r����
        mEditConfigText.setText(pref.getString("editConfigText", ""));
        // �HcheckConfigCheck1������Ө��oBoolean��
        mCheckConfigCheck1.setChecked(pref.getBoolean("checkConfigCheck1",
                false));
        // �HcheckConfigCheck2������Ө��oBoolean��
        mCheckConfigCheck2.setChecked(pref.getBoolean("checkConfigCheck2",
                false));
        // �HspinnerConfigSelect������Ө��o��ƭ�
        mSpinnerConfigSelect
                .setSelection(pref.getInt("spinnerConfigSelect", 0));
    }

    private void saveConfig() {
        // �ϥ�Private�ӫإ�CONFIG_NAME���ɮסA�è��oSharedPreferences���
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        // �HeditConfigText������Ө��o��r����
        editor.putString("editConfigText", mEditConfigText.getText().toString());
        // �HcheckConfigCheck1������Ө��oBoolean��
        editor.putBoolean("checkConfigCheck1", mCheckConfigCheck1.isChecked());
        // �HcheckConfigCheck2������Ө��oBoolean��
        editor.putBoolean("checkConfigCheck2", mCheckConfigCheck2.isChecked());
        // �HspinnerConfigSelect������Ө��o��ƭ�
        editor.putInt("spinnerConfigSelect",
                mSpinnerConfigSelect.getSelectedItemPosition());

        // �ϬM�]�w
        editor.commit();
    }

}
