package jp.co.se.android.recipe.chapter08;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0816 extends Activity implements OnClickListener {
    private static final String TAG = Ch0816.class.getSimpleName();
    private TextView mTextView;
    private Button mBtnRun;
    private Button mBtnCancel;
    private AsyncTask<Uri, Void, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0817_main);

        mBtnRun = (Button) findViewById(R.id.btnRun);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mTextView = (TextView) findViewById(R.id.text);

        mBtnRun.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void sendText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.append(text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRun) {
            requestGetMethod();
        } else if (id == R.id.btnCancel) {
            if (mTask != null) {
                mTask.cancel(false);
                mTextView.append("\n 正在取消通訊...");
            } else {
                mTextView.append("\n 通訊沒有回應或是已取消通訊");
            }
        }
    }

    private void requestGetMethod() {
        mTextView.setText("");
        sendText("準備通訊");

        // 使用容易操作的Uri型態來建構URL
        Uri baseUri = Uri
                .parse("http://android-recipe.herokuapp.com/samples/ch08/json");

        // 賦予參數
        Uri uri = baseUri.buildUpon().appendQueryParameter("param1", "hoge")
                .build();

        if (mTask == null) {
            mTask = new AsyncTask<Uri, Void, String>() {
                /** 通訊中發生錯誤 */
                private Throwable mError = null;

                @Override
                protected String doInBackground(Uri... params) {
                    String result = request(params[0]);

                    if (isCancelled()) {
                        sendText("\n 已經取消通訊");
                        return result;
                    }

                    return result;
                }

                private String request(Uri uri) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();

                    // 設定Time Out
                    HttpParams httpParams = httpClient.getParams();
                    // 設定確認連線為止的Time Out（毫秒）
                    HttpConnectionParams.setConnectionTimeout(httpParams,
                            5 * 1000);
                    // 設定連線之後的Time Out（毫秒）
                    HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);

                    String result = null;
                    HttpGet request = new HttpGet(uri.toString());
                    try {
                        sendText("\n 開始通訊");
                        result = httpClient.execute(request,
                                new ResponseHandler<String>() {
                                    @Override
                                    public String handleResponse(
                                            HttpResponse response)
                                            throws ClientProtocolException,
                                            IOException {
                                        int statusCode = response
                                                .getStatusLine()
                                                .getStatusCode();
                                        sendText("\n 狀態碼 : " + statusCode);
                                        if (statusCode == HttpStatus.SC_OK) {
                                            String result = EntityUtils
                                                    .toString(response
                                                            .getEntity());
                                            return result;
                                        } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                                            throw new RuntimeException(
                                                    "404 NOT FOUND");
                                        } else {
                                            throw new RuntimeException(
                                                    "其它的通訊錯誤");
                                        }
                                    }
                                });
                        sendText("\n 通訊結束");
                    } catch (RuntimeException e) {
                        mError = e;
                        sendText("\n 通訊失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通訊失敗", e);
                    } catch (ClientProtocolException e) {
                        mError = e;
                        sendText("\n 通訊失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通訊失敗", e);
                    } catch (IOException e) {
                        mError = e;
                        sendText("\n 通訊失敗" + e.getClass().getSimpleName());
                        Log.e(TAG, "通訊失敗", e);
                    } finally {
                        // 釋放資源
                        httpClient.getConnectionManager().shutdown();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    sendText("\nonPostExecute(String result)");

                    if (mError == null) {
                        sendText("\n 通訊成功");
                        sendText("\n 接收的資料: " + result);
                    } else {
                        sendText("\n 通訊失敗");
                        sendText("\n 錯誤: " + mError.getMessage());
                    }

                    mTask = null;
                }

                @Override
                protected void onCancelled() {
                    onCancelled(null);
                }

                @Override
                protected void onCancelled(String result) {
                    sendText("\nonCancelled(String result), result=" + result);

                    mTask = null;
                }
            }.execute(uri);
        } else {
            // 現在通訊的Task在執行中。需要控制使其不會被重複執行
        }
    }
}
