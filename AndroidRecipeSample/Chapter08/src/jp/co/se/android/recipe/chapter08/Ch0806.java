package jp.co.se.android.recipe.chapter08;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Ch0806 extends Activity implements OnLongClickListener {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);

        // 將長按設定生效
        mWebView.setLongClickable(true);
        // 設定長點擊時的監聽器
        mWebView.setOnLongClickListener(this);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://www.google.co.jp/");
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mWebView) {
            // 取得長點擊的位置資訊
            HitTestResult hitTestResult = mWebView.getHitTestResult();
            switch (hitTestResult.getType()) {
            case HitTestResult.SRC_ANCHOR_TYPE:
                // 當長點擊的位置是超連結時(用<a>標籤所包圍的部分)
                String url = hitTestResult.getExtra();
                Toast.makeText(this, "url:\n" + url, Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.stopLoading();
        ViewGroup webParent = (ViewGroup) mWebView.getParent();
        if (webParent != null) {
            webParent.removeView(mWebView);
        }
        mWebView.destroy();
    }
}
