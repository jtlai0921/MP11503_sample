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

        // ���oSession
        Session session = Session.getActiveSession();
        if (session == null) {
            // �YSession�w�x�s�h�_��
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, mStatusCallback,
                        savedInstanceState);
            }
            // �Y�S��Session�h���s�إ�
            if (session == null) {
                session = new Session(this);
            }
            // �]�wSession�����A
            Session.setActiveSession(session);
            // �YTOKEN�w�s�b�h�n�DSessionStatus
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this)
                        .setCallback(mStatusCallback));
            }
        } else {
            // �Y�w��Status�h���o�򥻸��
            getMyProfile(session);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ���USessionStatus�^�I
        Session.getActiveSession().addCallback(mStatusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        // �P��SessionStatus�^�I
        Session.getActiveSession().removeCallback(mStatusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // �N�n�J���G�浹FacebookSDK
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // �O�sSession��T
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    /**
     * SessionStatus�^�I
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(final Session session, SessionState state,
                Exception exception) {
            // �H�Ҩ��o��session����¦�A�Ө��o�򥻸��
            getMyProfile(session);
        }
    }

    /**
     * ���o�򥻸��
     * 
     * @param session
     */
    private void getMyProfile(final Session session) {
        if (session != null && session.isOpened()) {
            // ���o�ۨ����ϥΪ̸�T
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
     * ��s�e��
     */
    private void updateView() {
        Session session = Session.getActiveSession();
        if (mGraphUser != null && session.isOpened()) {
            mBtnLogin.setText("�n�J");
            mBtnPost.setEnabled(true);
            mEtInput.setEnabled(true);
        } else {
            mBtnLogin.setText("�n�X");
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
                // �n�X�B�z
                onClickLogout();
            } else {
                // �n�J�B�z
                onClickLogin();
            }
        } else if (id == R.id.post) {
            String message = mEtInput.getText().toString();
            postWall(message);
        }
    }

    /**
     * �n�JFacebook
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
     * �n�XFacebook
     */
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            updateView();
        }
    }

    /**
     * �N��p��s
     * 
     * @param message
     */
    private void postWall(String message) {
        Request.newStatusUpdateRequest(Session.getActiveSession(), message,
                null, null, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        mEtInput.setText("");
                        Toast.makeText(Ch0903.this, "�w�N��p��s",
                                Toast.LENGTH_SHORT).show();
                    }
                }).executeAsync();
    }
}
