package jp.co.se.android.recipe.chapter04;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class Ch0409Service extends Service {

    private static final int NOTIFY_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Toast.makeText(getBaseContext(), "Task end.",
                        Toast.LENGTH_SHORT).show();
                stopSelf();
            }
        };
        asyncTask.execute();

        Toast.makeText(getBaseContext(), "Task start.", Toast.LENGTH_SHORT)
                .show();

        return Service.START_STICKY;
    }

    /**
     * 顯示Notification，並將Service做Foreground
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private void showNotification() {
        Notification.Builder builder = new Notification.Builder(
                getApplicationContext()).setContentTitle("Running Service.")
                .setContentText("Ch0409").setSmallIcon(R.drawable.ic_launcher)
                .setOngoing(true);
        final Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        startForeground(NOTIFY_ID, notification);
    }
}
