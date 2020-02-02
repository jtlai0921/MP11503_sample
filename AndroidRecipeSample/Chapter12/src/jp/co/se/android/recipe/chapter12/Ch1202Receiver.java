package jp.co.se.android.recipe.chapter12;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Ch1202Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Toast.makeText(context, "Ch1202Receiver³Q©I¥s¤F", Toast.LENGTH_SHORT)
                .show();
    }

}
