package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import jp.co.se.android.recipe.common.MainActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1302 extends Activity {
    /** Notification™∫√—ßO•ŒID */
    private static final int ID_NOTIFICATION_SAMPLE_ACTIVITY = 0x00;

    /** Notification™∫CLICK INTENT */
    private static final String ACTION_CLICK_DIALER = "action.click.dialer";
    private static final String ACTION_CLICK_SMS = "action.click.sms";

    /** Notification™∫√˛´¨ */
    private enum NotificationType {
        BigText, BigPicture, Inbox, Button
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1302_main);

        // ≈„•‹©Œ§£≈„•‹BigText™∫NotificationÇï\é¶ÅEîÒï\é¶
        final Switch bigTextSwitch = (Switch) findViewById(R.id.bigTextSwitch);
        final Switch bigPictureSwitch = (Switch) findViewById(R.id.bigPictureSwitch);
        final Switch inboxSwitch = (Switch) findViewById(R.id.inboxSwitch);
        final Switch buttonSwitch = (Switch) findViewById(R.id.buttonSwitch);

        bigTextSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                showNotification(isChecked, NotificationType.BigText);
            }
        });

        // ≈„•‹©Œ§£≈„•‹BigPicture™∫Notification
        bigPictureSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        showNotification(isChecked, NotificationType.BigPicture);
                    }
                });

        // ≈„•‹©Œ§£≈„•‹Inbox™∫Notification
        inboxSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                showNotification(isChecked, NotificationType.Inbox);
            }
        });

        // ≈„•‹©Œ§£≈„•‹Button™∫Notification
        buttonSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                showNotification(isChecked, NotificationType.Button);
            }
        });

    }

    /**
     * ≈„•‹™¨∫A¶C
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(boolean isShow, NotificationType type) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (isShow && type != null) {
            // ≥q™æ™¨∫A¶C
            // ´ÿ•ﬂ±“∞ ¬sƒ˝æπ™∫PendingIntent
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    ID_NOTIFICATION_SAMPLE_ACTIVITY, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Notification™∫≥]©w
            Notification.Builder nb = new Notification.Builder(this);
            nb.setWhen(System.currentTimeMillis());
            nb.setContentIntent(contentIntent);
            nb.setSmallIcon(android.R.drawable.stat_notify_chat);
            nb.setTicker(getString(R.string.label_status_ticker));
            nb.setContentTitle(getString(R.string.label_status_style));
            nb.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE);

            Notification notification = null;
            if (type.equals(NotificationType.BigText)) {
                // ´ÿ•ﬂBigText™∫™¨∫A¶C
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(
                        nb);
                bigTextStyle.bigText(getString(R.string.label_status_main));
                bigTextStyle
                        .setSummaryText(getString(R.string.label_status_summary));
                notification = bigTextStyle.build();
            } else if (type.equals(NotificationType.BigPicture)) {
                // ´ÿ•ﬂBigPicture™∫™¨∫A¶C
                Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(
                        nb);
                bigPictureStyle.bigPicture(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ch1302_notification_img));
                notification = bigPictureStyle.build();
            } else if (type.equals(NotificationType.Inbox)) {
                // ´ÿ•ﬂInbox™∫™¨∫A¶C
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle(
                        nb);
                inboxStyle.addLine(getString(R.string.label_status_showline1));
                inboxStyle.addLine(getString(R.string.label_status_showline2));
                inboxStyle.addLine(getString(R.string.label_status_showline3));
                inboxStyle.addLine(getString(R.string.label_status_showline4));
                inboxStyle
                        .setSummaryText(getString(R.string.label_status_showlinemore));
                notification = inboxStyle.build();
            } else if (type.equals(NotificationType.Button)) {
                // ´ÿ•ﬂButton™∫™¨∫A¶C
                Intent dialClick = new Intent(this,
                        NotificationButtonClickListener.class);
                dialClick.setAction(ACTION_CLICK_DIALER);
                PendingIntent dialIntent = PendingIntent.getBroadcast(this, 0,
                        dialClick, 0);
                Intent smsClick = new Intent(this,
                        NotificationButtonClickListener.class);
                smsClick.setAction(ACTION_CLICK_SMS);
                PendingIntent smsIntent = PendingIntent.getBroadcast(this, 0,
                        smsClick, 0);
                nb.addAction(android.R.drawable.ic_dialog_dialer,
                        getString(R.string.label_status_button_call),
                        dialIntent);
                nb.addAction(android.R.drawable.ic_dialog_email,
                        getString(R.string.label_status_button_sms), smsIntent);
                notification = nb.build();
            }

            // Notification≥q™æ
            if (notification != null) {
                notificationManager.notify(ID_NOTIFICATION_SAMPLE_ACTIVITY,
                        notification);
            }
        } else {
            // ßR∞£™¨∫A¶C
            // ®˙Æ¯Notification
            notificationManager.cancel(ID_NOTIFICATION_SAMPLE_ACTIVITY);
        }
    }

    public static class NotificationButtonClickListener extends
            BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CLICK_DIALER)) {
                // ±“∞ º∑∏πµe≠±
                Intent launchDialer = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:090-1234-5678"));
                launchDialer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchDialer);
            } else if (action.equals(ACTION_CLICK_SMS)) {
                // ∂}±“πq§l∂l•ÛΩsøËµe≠±
                Intent launchSms = new Intent(Intent.ACTION_SENDTO);
                launchSms.setData(Uri.parse("smsto:090-1234-5678"));
                launchSms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchSms);
            }
        }
    }
}
