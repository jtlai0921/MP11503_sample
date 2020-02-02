package jp.co.se.android.recipe.chapter09;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

public class Ch0909 extends Activity implements View.OnClickListener {

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0909_main);

        mText = (TextView) findViewById(R.id.text);
        Button login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
            requestProfile();
        }
    }

    private void requestProfile() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String accessToken = prefs.getString(Ch0908.PREF_ACCESS_TOKEN, null);
        if (accessToken == null) {
            Toast.makeText(this, "ª{√“©|•ºµ≤ßÙ", Toast.LENGTH_SHORT).show();
            return;
        }

        new RequestProfileTask(this, accessToken) {
            @Override
            protected void onPostExecute(String jsonResult) {
                Exception e = this.getException();
                if (e == null) {
                    try {
                        // ±µ¶¨µ≤™Gâ ÇÃéÛÇØéÊÇË
                        JSONObject jsRoot = new JSONObject(jsonResult);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < jsRoot.names().length(); i++) {
                            if (sb.length() > 0) {
                                sb.append("\n");
                            }
                            Object object = jsRoot.names().get(i);
                            sb.append(object.toString() + ":"
                                    + jsRoot.getString(object.toString()));
                        }
                        mText.setText(sb.toString());
                        Log.v(Ch0909.class.getSimpleName(), jsRoot.toString());
                    } catch (JSONException jsonEx) {
                        jsonEx.printStackTrace();
                    }
                } else {
                    int responseCode = this.getResponseCode();
                    if (responseCode == 401) {
                        SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());
                        prefs.edit().remove(Ch0908.PREF_ACCESS_TOKEN).commit();
                        Toast.makeText(Ch0909.this,
                                "accessToken•¢Æƒ§F°AΩ–¶A¶∏ª{√“",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Ch0909.this, "ø˘ª~:" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    Log.e(Ch0909.class.getSimpleName(), e.getMessage(), e);
                }
            }

        }.execute();
    }

    /**
     * ÉAÉNÉZÉXÉgÅ[ÉN?ÇéÊìæÇ∑ÇÈÇΩÇﬂÇÃÉ^ÉXÉN
     */
    public static class RequestProfileTask extends
            AsyncTask<Void, String, String> {

        private Context mContext;
        private Exception mException;
        private String mAccessToken;
        private int mResponseCode;

        public RequestProfileTask(Context context, String accessToken) {
            mContext = context;
            mAccessToken = accessToken;
        }

        @Override
        protected String doInBackground(Void... params) {
            // ¶s®˙≠n®˙±o®œ•Œ™Ã∏Í∞T™∫API
            try {
                URL url = new URL(
                        "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
                                + mAccessToken);
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();

                // JSON
                int sc = con.getResponseCode();
                mResponseCode = sc;
                if (sc == 200) {
                    InputStream is = con.getInputStream();
                    String readResponse = readResponse(is);
                    is.close();

                    return readResponse;
                } else if (sc == 401) {
                    GoogleAuthUtil.invalidateToken(mContext, mAccessToken);
                    // ª{√“ø˘ª~
                    mException = new RuntimeException(
                            readResponse(con.getErrorStream()));
                } else {
                    mException = new RuntimeException(String.format(
                            "¶¯™Aæπ%1$s¶^∂«§Fø˘ª~", sc));
                }
            } catch (MalformedURLException e) {
                mException = e;
            } catch (IOException e) {
                mException = e;
            }
            return null;
        }

        public Exception getException() {
            return mException;
        }

        public int getResponseCode() {
            return mResponseCode;
        }

        private static String readResponse(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int len = 0;
            while ((len = is.read(data, 0, data.length)) >= 0) {
                bos.write(data, 0, len);
            }
            return new String(bos.toByteArray(), "UTF-8");
        }

    }
}
