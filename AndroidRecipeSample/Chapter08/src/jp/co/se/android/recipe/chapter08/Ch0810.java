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

            /** ª›≠nBasicª{√“Æ…°A¥N∞ı¶Ê∞ ß@ÇΩ*/
            @Override
            public void onReceivedHttpAuthRequest(WebView view,
                    HttpAuthHandler handler, String host, String realm) {
                String[] usernamePassword = view.getHttpAuthUsernamePassword(
                        host, realm);
                if (usernamePassword == null) {
                    if (mAuthedData == null) {
                        // ≠∫¶∏ª{√“°Aπ¡∏’∂i¶Êª{√“
                        mAuthedData = new BasicAuth(host, realm,
                                BASIC_USERNAME, BASIC_PASSWORD);
                        handler.proceed(mAuthedData.username,
                                mAuthedData.password);
                    } else {
                        // §W≠z™∫ª{√“•¢±—Æ…°A®˙Æ¯ª{√“
                        handler.cancel();
                    }
                } else {
                    // ª{√“¶®•\≥qπL™∫±°ßŒ
                    // §w¿x¶s®œ•Œ™Ã¶W∫Ÿ°˛±KΩX°A™Ω±µ®œ•Œ°]¶€∞ ª{√“°^ÅB
                    if (mAuthedData == null) {
                        // ≤ƒ§@¶∏•˝π¡∏’¶€∞ ª{√“
                        String username = usernamePassword[0];
                        String password = usernamePassword[1];

                        handler.proceed(username, password);

                        mAuthedData = new BasicAuth(host, realm, username,
                                password);
                    } else {
                        // ¶€∞ ª{√“•¢±—™∫±°ßŒ
                        // ¶]¨∞¿x¶s™∫®œ•Œ™Ã¶W∫Ÿ°˛±KΩX¶≥ª~°AßR∞£´·≠´∑sª{√“
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
