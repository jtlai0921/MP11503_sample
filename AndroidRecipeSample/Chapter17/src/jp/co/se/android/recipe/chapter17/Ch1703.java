package jp.co.se.android.recipe.chapter17;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Ch1703 extends Activity {
    private AdView mAdView;
    private static final String AD_UNIT_ID = "取得的廣告單元ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1703_main);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(AD_UNIT_ID);
        mAdView.setAdSize(AdSize.BANNER);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);
        layout.addView(mAdView);

        mAdView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
