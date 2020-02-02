package jp.co.se.android.recipe.chapter08;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Ch0809 extends Activity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        setupWebStorage(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/webstorage");
    }

    @SuppressWarnings("deprecation")
    private void setupWebStorage(WebView webView) {
        WebSettings ws = webView.getSettings();

        // 設定資料庫生效
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);

        // 指定資料庫的儲存位置
        if (Build.VERSION_CODES.JELLY_BEAN_MR2 <= Build.VERSION.SDK_INT) {
            File databaseDir = getDir("databases", Context.MODE_PRIVATE);
            if (!databaseDir.exists()) {
                databaseDir.mkdirs();
            }
            ws.setDatabasePath(databaseDir.getPath());
        } else {
            // 因為在Android 4.4更新了WebView的原始碼
            // 所以就不需要呼叫setDatabasePath方法了
        }
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
