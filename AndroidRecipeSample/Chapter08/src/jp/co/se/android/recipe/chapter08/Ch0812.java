package jp.co.se.android.recipe.chapter08;

import org.apache.http.impl.cookie.BasicClientCookie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Ch0812 extends Activity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CookieSyncManager.createInstance(getApplicationContext());
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeExpiredCookie();

        setCookie();

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/header");
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }

    /** ³]©wCookie */
    private void setCookie() {
        CookieManager cm = CookieManager.getInstance();
        BasicClientCookie cookie = getSampleCookie();
        cm.setCookie(cookie.getDomain(), toHeaderCookie(cookie));
        CookieSyncManager.getInstance().sync();
    }

    private BasicClientCookie getSampleCookie() {
        BasicClientCookie cookie = new BasicClientCookie("CookieSampleKey",
                "CookieSampleValue");
        cookie.setDomain("android-recipe.herokuapp.com");
        cookie.setPath("/");

        return cookie;
    }

    // ex.) Set-Cookie: NAME=VALUE; expires=DATE; path=PATH; domain=DOMAIN_NAME;
    // secure
    private String toHeaderCookie(BasicClientCookie c) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.getName()).append("=").append(c.getValue()).append("; ");
        sb.append("domain").append("=").append(c.getDomain()).append("; ");
        sb.append("path").append("=").append(c.getPath()).append("; ");

        return sb.toString();
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
