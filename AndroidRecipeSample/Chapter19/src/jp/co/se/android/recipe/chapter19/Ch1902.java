package jp.co.se.android.recipe.chapter19;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * 做為函式庫專案必須與Volley進行相關連結
 */
public class Ch1902 extends Activity implements OnClickListener {
    private ImageView mImageView;
    private TextView mTextView;

    private ImageLoader mImageLoader;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1902_main);

        Button btnText = (Button) findViewById(R.id.btnText);
        Button btnJson = (Button) findViewById(R.id.btnJson);
        Button btnImage = (Button) findViewById(R.id.btnImage);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        mImageView = (ImageView) findViewById(R.id.img);
        mTextView = (TextView) findViewById(R.id.text);

        btnText.setOnClickListener(this);
        btnJson.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mQueue, new BitmapLruCache(cacheSize));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQueue.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQueue.stop();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnText) {
            mImageView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            requestText();
        } else if (id == R.id.btnJson) {
            mImageView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            requestJson();
        } else if (id == R.id.btnImage) {
            mImageView.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.GONE);
            requestImage();
        } else if (id == R.id.btnClear) {
            mTextView.setText("");
            mImageView.setImageBitmap(null);
        }
    }

    private void requestText() {
        String url = "http://android-recipe.herokuapp.com/samples/ch19/text";

        // 建立請求物件
        StringRequest reqeust = new StringRequest(Method.GET, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 保存接收的結果
                        mTextView.append(response);
                    }
                }, null);
        // 開始請求
        mQueue.add(reqeust);
    }

    private void requestJson() {
        String url = "http://android-recipe.herokuapp.com/samples/ch19/json";
        JsonObjectRequest reqeust = new JsonObjectRequest(Method.GET, url,
                null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mTextView.append(response.toString());
                    }
                }, mErrorListener);
        mQueue.add(reqeust);
    }

    private void requestImage() {
        String url = "http://android-recipe.herokuapp.com/img/autumn.jpg";
        // 也確認錯誤情形下的動作反應
        // String url = "http://example.com/notfound";
        mImageLoader.get(url, ImageLoader.getImageListener(mImageView, 0,
                R.drawable.get_error));
    }

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String text = String.format("通訊錯誤: %1$s", error.getMessage());
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                    .show();
        }
    };

    public static class BitmapLruCache extends LruCache<String, Bitmap>
            implements ImageCache {
        public BitmapLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}
