package jp.co.se.android.recipe.chapter04;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class Ch0410Service extends Service {

    private ArrayList<String> mStringList = new ArrayList<String>();

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    // ¹ê§@AIDL Binder
    private ICh0410Service.Stub mBinder = new ICh0410Service.Stub() {

        @Override
        public int addString(String value) throws RemoteException {
            mStringList.add(value);
            return mStringList.size();
        }

        @Override
        public String[] getString() throws RemoteException {
            return mStringList.toArray(new String[mStringList.size()]);
        }
    };

}
