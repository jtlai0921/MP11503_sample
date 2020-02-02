package jp.co.se.android.recipe.chapter09;

import jp.co.se.android.recipe.chapter09.R;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0902 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private ProfilePictureView mPpvImage;
    private TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0902_main);

        mPpvImage = (ProfilePictureView) findViewById(R.id.image);
        mTvName = (TextView) findViewById(R.id.name);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);

        // 取得Session
        Session session = Session.getActiveSession();
        if (session == null) {
            // 若Session已儲存則復原
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, mStatusCallback,
                        savedInstanceState);
            }
            // 若沒有Session則重新產生
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

        // 設定當ACCESS_TOKEN被請求時，其LOG輸出為ON
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
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
        // 儲存Session資訊
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
            // 以所取得的session為基礎來獲取基本資料
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
            // 取得本身的使用者情報
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
            mBtnLogin.setText(getString(R.string.label_logout));
            mPpvImage.setProfileId(mGraphUser.getId());
            mTvName.setText(mGraphUser.getName());
        } else {
            mBtnLogin.setText(getString(R.string.label_login));
            mPpvImage.setProfileId(null);
            mTvName.setText("");
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
     * 從Facebook登出
     */
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            updateView();
        }
    }
}
