package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1301 extends Activity {
    /** Notification���ѧO��ID */
    int ID_NOTIFICATION_SAMPLE_ACTIVITY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1301_main);

        Switch notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        notificationSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        // �t�X�}�����������ܧ󪬺A�C��ON/OFF
                        showNotification(isChecked);
                    }
                });

    }

    /**
     * ��ܪ��A�C
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(boolean isShow) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isShow) {
            // �q�����A�C
            // �إ߱Ұ��s������PendingIntent
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.url_shoeisha)));
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    ID_NOTIFICATION_SAMPLE_ACTIVITY, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification���]�w
            Notification.Builder nb = new Notification.Builder(this);
            // �q���ƥ󪺮ɶ��W�O
            nb.setWhen(System.currentTimeMillis());
            // �]�w���e
            nb.setContentIntent(contentIntent);
            // �]�w�ϥ�
            nb.setSmallIcon(android.R.drawable.stat_notify_chat);
            // �q���ɭn��ܪ��r��
            nb.setTicker(getString(R.string.label_status_ticker));
            // �n��ܦb���A�C�����D
            nb.setContentTitle(getString(R.string.label_launch_browser));
            // �n���B���ʡB�O��
            nb.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS);
            // �I����۰ʮ������
            nb.setAutoCancel(true);
            Notification notification = nb.build();

            // Notification�q��
            notificationManager.notify(ID_NOTIFICATION_SAMPLE_ACTIVITY,
                    notification);
        } else {
            // �R�����A�C
            // ����Notification
            notificationManager.cancel(ID_NOTIFICATION_SAMPLE_ACTIVITY);
        }
    }
}
