package jp.co.se.android.recipe.chapter04;

import jp.co.se.android.recipe.chapter04.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class Ch0401 extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0401_main);

        mTextView = (TextView) findViewById(R.id.text);

        PackageManager pm = getPackageManager();
        int versionCode = 0;
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

            // AndroidManifest.xml ªº android:versionCode
            versionCode = packageInfo.versionCode;

            // AndroidManifest.xml ªº versionName
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        String msg = String.format("versionCode:%1$s, versionName:%2$s",
                versionCode, versionName);
        mTextView.setText(msg);
    }
}
