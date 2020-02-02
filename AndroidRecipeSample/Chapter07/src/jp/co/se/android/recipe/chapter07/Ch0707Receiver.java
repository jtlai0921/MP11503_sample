package jp.co.se.android.recipe.chapter07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class Ch0707Receiver extends BroadcastReceiver {
    public static final String TAG = "Chapter07";
    private static final int KEYCODE_MEDIA_PLAY = 126;
    private static final int KEYCODE_MEDIA_PAUSE = 127;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                Intent service = new Intent(context, Ch0707Service.class);
                switch (keyEvent.getKeyCode()) {
                case KEYCODE_MEDIA_PAUSE:
                case KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    // 播放鈕或暫停鈕或保留鈕
                    service.setAction("playpause");
                    context.startService(service);
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    // 到下一首
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    // 回上一首
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    // 停止
                    service.setAction("stop");
                    context.startService(service);
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    // 快轉
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    // 回轉
                    break;
                default:
                    Log.w(TAG, "呼叫預期之外的程式碼: " + keyEvent.getKeyCode());
                }
            }
        }
    }
}
