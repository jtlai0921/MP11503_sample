package jp.co.se.android.recipe.chapter08;

import jp.co.se.android.recipe.chapter08.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Ch0802 extends Activity implements OnClickListener {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0802_main);

        mWebView = (WebView) findViewById(R.id.web);
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        Button btnNext = (Button) findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://android-recipe.herokuapp.com/samples/ch08/history");
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
        if (id == R.id.btnPrev) {
            // 回前一頁
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
        } else if (id == R.id.btnNext) {
            // 到下一頁
            if (mWebView.canGoForward()) {
                mWebView.goForward();
            }
        } else {
            throw new RuntimeException("no match button id.");
        }
    }
}
