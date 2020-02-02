package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

public class Ch0235 extends Activity {
    protected static final String PREF_CUSTOMIZE_DIALOG = "customizedialog_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            fm.beginTransaction()
                    .replace(android.R.id.content, new MyPreferencesFragment())
                    .commit();
        }
    }

    public static class MyPreferencesFragment extends PreferenceFragment
            implements OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.ch0235_pref);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(
                SharedPreferences sharedPreferences, String key) {
            if (PREF_CUSTOMIZE_DIALOG.equals(key)) {
                String date = sharedPreferences.getString(key, null);
                Toast.makeText(getActivity(), "所選擇的時間: " + date,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
