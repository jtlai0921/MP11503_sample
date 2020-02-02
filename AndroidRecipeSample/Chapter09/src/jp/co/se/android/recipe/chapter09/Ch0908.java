package jp.co.se.android.recipe.chapter09;

import java.io.IOException;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;

public class Ch0908 extends Activity implements OnClickListener {
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    public static final String PREF_ACCESS_TOKEN = "pref.accessToken";

    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0908_main);

        Button btnAuth = (Button) findViewById(R.id.btnAuth);

        btnAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAuth) {
            getUsername();
        }
    }

    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            doAuth(mEmail);
        }
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[] { "com.google" };
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "必須選擇帳號", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR
                && resultCode == RESULT_OK) {
            getUsername();
        }
    }

    private void doAuth(String email) {
        // 開始取得accessToken
        new AuthorizationTask(this, email, SCOPE) {
            @Override
            protected void onPostExecute(String accessToken) {
                Exception e = this.getException();
                if (e == null) {
                    // 保存accessToken
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString(PREF_ACCESS_TOKEN, accessToken)
                            .commit();

                    Toast.makeText(getApplicationContext(), "認證成功了",
                            Toast.LENGTH_SHORT).show();
                } else if (e instanceof UserRecoverableAuthException) {
                    UserRecoverableAuthException userAuthEx = (UserRecoverableAuthException) e;
                    startActivityForResult(userAuthEx.getIntent(),
                            REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
                    return;
                } else {
                    Toast.makeText(getApplicationContext(),
                            String.format("認證失敗了:\n%1$s", e),
                            Toast.LENGTH_SHORT).show();
                    Log.e(Ch0908.class.getSimpleName(), "認證失敗", e);
                }
            }

        }.execute();
    }

    /**
     * 為了取得accessToken的Task
     */
    public static class AuthorizationTask extends AsyncTask<Void, Void, String> {

        private Context mContext;
        private String mEmail;
        private String mScope;
        private Exception mException;

        public AuthorizationTask(Context context, String email, String scope) {
            mContext = context;
            mEmail = email;
            mScope = scope;
        }

        @Override
        protected String doInBackground(Void... params) {
            String accessToken = null;
            try {
            	  // 取得accessToken
                accessToken = GoogleAuthUtil.getToken(mContext, mEmail, mScope);
            } catch (UserRecoverableAuthException e) {
                mException = e;
            } catch (GoogleAuthException e) {
                mException = e;
            } catch (IOException e) {
                mException = e;
            }
            return accessToken;
        }

        public Exception getException() {
            return mException;
        }
    }
}
