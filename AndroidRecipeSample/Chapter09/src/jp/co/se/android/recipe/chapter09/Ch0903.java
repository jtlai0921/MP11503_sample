package jp.co.se.android.recipe.chapter09;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ch0903 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private Button mBtnPost;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0903_main);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);
        mBtnPost = (Button) findViewById(R.id.post);
        mBtnPost.setOnClickListener(this);
        mEtInput = (EditText) findViewById(R.id.input);

        // 取得Session
        Session session = Session.getActiveSession();
        if (session == null) {
            // 若Session已儲存則復原
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, mStatusCallback,
                        savedInstanceState);
            }
            // 若沒有Session則重新建立
            if (session == null) {
                session = new Session(this);
            }
            // 設定Session的狀態
            Session.setActiveSession(session);
            // 若TOKEN已存在則要求SessionStatus
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this)
                        .setCallback(mStatusCallback));
            }
        } else {
            // 若已有Status則取得基本資料
            getMyProfile(session);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 註冊SessionStatus回呼
        Session.getActiveSession().addCallback(mStatusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 銷毀SessionStatus回呼
        Session.getActiveSession().removeCallback(mStatusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 將登入結果交給FacebookSDK
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存Session資訊
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    /**
     * SessionStatus回呼
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(final Session session, SessionState state,
                Exception exception) {
            // 以所取得的session為基礎，來取得基本資料
            getMyProfile(session);
        }
    }

    /**
     * 取得基本資料
     * 
     * @param session
     */
    private void getMyProfile(final Session session) {
        if (session != null && session.isOpened()) {
            // 取得自身的使用者資訊
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                Response response) {
                            if (session == Session.getActiveSession()) {
                                if (user != null) {
                                    mGraphUser = user;
                                    updateView();
                                }
                            }
                        }
                    });
            Request.executeBatchAsync(request);
        }
    }

    /**
     * 更新畫面
     */
    private void updateView() {
        Session session = Session.getActiveSession();
        if (mGraphUser != null && session.isOpened()) {
            mBtnLogin.setText("登入");
            mBtnPost.setEnabled(true);
            mEtInput.setEnabled(true);
        } else {
            mBtnLogin.setText("登出");
            mBtnPost.setEnabled(false);
            mEtInput.setEnabled(false);
            mGraphUser = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
            Session session = Session.getActiveSession();
            if (session.isOpened()) {
                // 登出處理
                onClickLogout();
            } else {
                // 登入處理
                onClickLogin();
            }
        } else if (id == R.id.post) {
            String message = mEtInput.getText().toString();
            postWall(message);
        }
    }

    /**
     * 登入Facebook
     */
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setCallback(mStatusCallback));
        } else {
            Session.openActiveSession(this, true, mStatusCallback);
        }
    }

    /**
     * 登出Facebook
     */
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            updateView();
        }
    }

    /**
     * 將近況更新
     * 
     * @param message
     */
    private void postWall(String message) {
        Request.newStatusUpdateRequest(Session.getActiveSession(), message,
                null, null, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        mEtInput.setText("");
                        Toast.makeText(Ch0903.this, "已將近況更新",
                                Toast.LENGTH_SHORT).show();
                    }
                }).executeAsync();
    }
}
