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
            // ¨ú±oCh0410ServiceªºAIDLªº¤¶­±
            mService = ICh0410Service.Stub.asInterface(ibinder);
            // §ó·sÁ`Äý¤º®e
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
        // ±NCh0410Service¶i¦æBind‚·‚é
        Intent service = new Intent(this, Ch0410Service.class);
        bindService(service, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ¸Ñ°£Ch0410ServiceªºBind
        unbindService(mServiceConnection);
    }

    private void reloadList() {
        try {
            // ±qCh0410Service¨ú±o¦r¦ê°}¦C
            String[] list = mService.getString();
            // ±N¦r¦ê°}¦C³]©w¦bListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            setListAdapter(adapter);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
