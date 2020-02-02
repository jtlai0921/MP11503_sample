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

        // �N�����]�w�ͮ�
        mWebView.setLongClickable(true);
        // �]�w���I���ɪ���ť��
        mWebView.setOnLongClickListener(this);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://www.google.co.jp/");
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mWebView) {
            // ���o���I������m��T
            HitTestResult hitTestResult = mWebView.getHitTestResult();
            switch (hitTestResult.getType()) {
            case HitTestResult.SRC_ANCHOR_TYPE:
                // ����I������m�O�W�s����(��<a>���ҩҥ]�򪺳���)
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
