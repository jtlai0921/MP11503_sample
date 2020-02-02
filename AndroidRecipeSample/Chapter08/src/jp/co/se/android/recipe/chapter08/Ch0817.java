package jp.co.se.android.recipe.chapter08;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0817 extends Activity implements OnClickListener {
    private static final String TAG = Ch0817.class.getSimpleName();
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
        sendText("�q�T�ǳ�");

        // �ϥήe���ާ@��Uri���A�ӫغcURL
        Uri baseUri = Uri
                .parse("http://android-recipe.herokuapp.com/samples/ch08/json");

        // �ᤩ�Ѽ�
        Uri uri = baseUri.buildUpon().appendQueryParameter("param1", "hoge")
                .build();

        if (mTask == null) {
            mTask = new AsyncTask<Uri, Void, String>() {
                /** �q�T���o�ͪ����~ */
                private Throwable mError = null;

                @Override
                protected String doInBackground(Uri... params) {
                    Uri uri = params[0];
                    sendText("\n �q�T�}�l");

                    String result = request(uri);

                    return result;
                }

                private String request(Uri uri) {
                    HttpURLConnection http = null;
                    InputStream is = null;
                    String result = null;
                    try {
                        // URL�s��HTTP
                        URL url = new URL(uri.toString());
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();
                        is = http.getInputStream();

                        result = toString(is);
                        sendText(result);
                    } catch (MalformedURLException e) {
                        Log.e(TAG, "�q�T����", e);
                        sendText(e.toString());
                    } catch (IOException e) {
                        Log.e(TAG, "�q�T����", e);
                        sendText(e.toString());
                    } finally {
                        if (http != null) {
                            http.disconnect();
                        }
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "��y��������", e);
                        }
                    }
                    return result;
                }

                private String toString(InputStream is) throws IOException {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    char[] b = new char[1024];
                    int line;
                    while (0 <= (line = reader.read(b))) {
                        sb.append(b, 0, line);
                    }
                    return sb.toString();
                }

                @Override
                protected void onPostExecute(String result) {
                    sendText("\nonPostExecute(String result)");

                    if (mError == null) {
                        sendText("\n �q�T���\");
                        sendText("\n ��������� : " + result);
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
