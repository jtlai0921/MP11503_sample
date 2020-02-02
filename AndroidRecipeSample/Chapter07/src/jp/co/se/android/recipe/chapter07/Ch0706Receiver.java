package jp.co.se.android.recipe.chapter07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class Ch0706Receiver extends BroadcastReceiver {
    public static final String TAG = "Chapter07";
    private static final int KEYCODE_MEDIA_PLAY = 126;
    private static final int KEYCODE_MEDIA_PAUSE = 127;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                switch (keyEvent.getKeyCode()) {
                case KEYCODE_MEDIA_PAUSE:
                case KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    // 播放或暫停或保留鈕
                    Intent service = new Intent(context, Ch0706Service.class);
                    service.setAction("playpause");
                    context.startService(service);
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    // 往下一首
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    // 回前一首
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    // 停止
                    break;
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                    // 快轉
                    break;
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                    // 倒帶
                    break;
                default:
                    Log.w(TAG, "呼叫了預期之外的程式碼: " + keyEvent.getKeyCode());
                }
            }
        }
    }
}
