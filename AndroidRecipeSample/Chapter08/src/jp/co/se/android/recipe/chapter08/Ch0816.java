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
                mTextView.append("\n ���b�����q�T...");
            } else {
                mTextView.append("\n �q�T�S���^���άO�w�����q�T");
            }
        }
    }

    private void requestGetMethod() {
        mTextView.setText("");
        sendText("�ǳƳq�T");

        // �ϥήe���ާ@��Uri���A�ӫغcURL
        Uri baseUri = Uri
                .parse("http://android-recipe.herokuapp.com/samples/ch08/json");

        // �ᤩ�Ѽ�
        Uri uri = baseUri.buildUpon().appendQueryParameter("param1", "hoge")
                .build();

        if (mTask == null) {
            mTask = new AsyncTask<Uri, Void, String>() {
                /** �q�T���o�Ϳ��~ */
                private Throwable mError = null;

                @Override
                protected String doInBackground(Uri... params) {
                    String result = request(params[0]);

                    if (isCancelled()) {
                        sendText("\n �w�g�����q�T");
                        return result;
                    }

                    return result;
                }

                private String request(Uri uri) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();

                    // �]�wTime Out
                    HttpParams httpParams = httpClient.getParams();
                    // �]�w�T�{�s�u���Time Out�]�@��^
                    HttpConnectionParams.setConnectionTimeout(httpParams,
                            5 * 1000);
                    // �]�w�s�u���᪺Time Out�]�@��^
                    HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);

                    String result = null;
                    HttpGet request = new HttpGet(uri.toString());
                    try {
                        sendText("\n �}�l�q�T");
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
                                        sendText("\n ���A�X : " + statusCode);
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
                                                    "�䥦���q�T���~");
                                        }
                                    }
                                });
                        sendText("\n �q�T����");
                    } catch (RuntimeException e) {
                        mError = e;
                        sendText("\n �q�T����" + e.getClass().getSimpleName());
                        Log.e(TAG, "�q�T����", e);
                    } catch (ClientProtocolException e) {
                        mError = e;
                        sendText("\n �q�T����" + e.getClass().getSimpleName());
                        Log.e(TAG, "�q�T����", e);
                    } catch (IOException e) {
                        mError = e;
                        sendText("\n �q�T����" + e.getClass().getSimpleName());
                        Log.e(TAG, "�q�T����", e);
                    } finally {
                        // ����귽
                        httpClient.getConnectionManager().shutdown();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    sendText("\nonPostExecute(String result)");

                    if (mError == null) {
                        sendText("\n �q�T���\");
                        sendText("\n ���������: " + result);
                    } else {
                        sendText("\n �q�T����");
                        sendText("\n ���~: " + mError.getMessage());
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
            // �{�b�q�T��Task�b���椤�C�ݭn����Ϩ䤣�|�Q���ư���
        }
    }
}
