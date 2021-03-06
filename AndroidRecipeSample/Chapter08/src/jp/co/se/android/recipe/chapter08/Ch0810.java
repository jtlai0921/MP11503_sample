package jp.co.se.android.recipe.chapter08;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;

public class Ch0810 extends Activity {
    public static final String BASIC_USERNAME = "u";
    public static final String BASIC_PASSWORD = "p";

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0810_main);

        mWebView = (WebView) findViewById(R.id.web);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            private BasicAuth mAuthedData = null;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mAuthedData = null;
            }

            /** 需要Basic認證時，就執行動作��*/
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                    HttpAuthHandler handler, String host, String realm) {
                String[] usernamePassword = view.getHttpAuthUsernamePassword(
                        host, realm);
                if (usernamePassword == null) {
                    if (mAuthedData == null) {
                        // 首次認證，嘗試進行認證
                        mAuthedData = new BasicAuth(host, realm,
                                BASIC_USERNAME, BASIC_PASSWORD);
                        handler.proceed(mAuthedData.username,
                                mAuthedData.password);
                    } else {
                        // 上述的認證失敗時，取消認證
                        handler.cancel();
                    }
                } else {
                    // 認證成功通過的情形
                    // 已儲存使用者名稱／密碼，直接使用（自動認證）�B
                    if (mAuthedData == null) {
                        // 第一次先嘗試自動認證
                        String username = usernamePassword[0];
                        String password = usernamePassword[1];

                        handler.proceed(username, password);

                        mAuthedData = new BasicAuth(host, realm, username,
                                password);
                    } else {
                        // 自動認證失敗的情形
                        // 因為儲存的使用者名稱／密碼有誤，刪除後重新認證
                        WebViewDatabase.getInstance(getApplicationContext())
                                .clearHttpAuthUsernamePassword();
                        mAuthedData = null;
                        onReceivedHttpAuthRequest(view, handler, host, realm);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mAuthedData != null) {
                    view.setHttpAuthUsernamePassword(mAuthedData.host,
                            mAuthedData.realm, mAuthedData.username,
                            mAuthedData.password);
                    mAuthedData = null;
                }
            }
        });
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/basicauth_ready");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        WebViewDatabase.getInstance(this).clearHttpAuthUsernamePassword();

        mWebView.stopLoading();
        ViewGroup webParent = (ViewGroup) mWebView.getParent();
        if (webParent != null) {
            webParent.removeView(mWebView);
        }
        mWebView.destroy();
        super.onDestroy();
    }

    private static class BasicAuth {
        final String host;
        final String realm;
        final String username;
        final String password;

        public BasicAuth(String host, String realm, String username,
                String password) {
            this.host = host;
            this.realm = realm;
            this.username = username;
            this.password = password;
        }
    }
}
