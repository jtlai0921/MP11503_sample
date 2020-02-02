package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1114 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1114_main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final TextView tvDescription = (TextView) findViewById(R.id.description);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            if (ni.getTypeName().equals("WIFI")) {
                tvDescription
                        .setText(getString(R.string.ch1114_label_network_wifi));
            } else if (ni.getTypeName().equals("mobile")) {
                tvDescription
                        .setText(getString(R.string.ch1114_label_network_mobile));
            }
        } else {
            tvDescription
                    .setText(getString(R.string.ch1114_label_network_unknown));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
