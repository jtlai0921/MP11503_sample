package jp.co.se.android.recipe.chapter04;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class Ch0410 extends ListActivity {

    private ICh0410Service mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder ibinder) {
            // 取得Ch0410Service的AIDL的介面
            mService = ICh0410Service.Stub.asInterface(ibinder);
            // 更新總覽內容
            reloadList();
        }

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0410_main);
        findViewById(R.id.buttonAdd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editString = (EditText) findViewById(R.id.editString);
                if (mService != null) {
                    try {
                        mService.addString(editString.getText().toString());
                        reloadList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 將Ch0410Service進行Bind����
        Intent service = new Intent(this, Ch0410Service.class);
        bindService(service, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除Ch0410Service的Bind
        unbindService(mServiceConnection);
    }

    private void reloadList() {
        try {
            // 從Ch0410Service取得字串陣列
            String[] list = mService.getString();
            // 將字串陣列設定在ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            setListAdapter(adapter);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
