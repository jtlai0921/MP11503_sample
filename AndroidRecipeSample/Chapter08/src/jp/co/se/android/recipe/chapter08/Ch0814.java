package jp.co.se.android.recipe.chapter08;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class Ch0814 extends Activity implements OnClickListener {

    private WebView mWebView;
    private TextView mText;
    private Button mBtnViewSource;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0814_main);

        mText = (TextView) findViewById(R.id.text);
        mBtnViewSource = (Button) findViewById(R.id.btnViewSource);
        mBtnViewSource.setOnClickListener(this);

        // ³]©wWebView
        mWebView = (WebView) findViewById(R.id.web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void text(final String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mText.append(text);
                        mText.append("\n");
                    }
                });
            }
        }, "injectedObject");

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/javascriptinterface");
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnViewSource) {
            mWebView.loadUrl("javascript:alert(document.getElementsByTagName('html')[0].outerHTML);");
        }
    }
}
