package jp.co.se.android.recipe.chapter07;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerView;

public class Ch0712 extends YouTubeBaseActivity implements
        OnInitializedListener {
    private static final String DEVELOPER_KEY = "設定在GoogleAPIConsole所取得的API Key";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0712_main);

        // 在YoutubePlayerView中設定Developer Key
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
            YouTubeInitializationResult errorReason) {
        // 當初始化失敗時的處理
        if (errorReason.isUserRecoverableError()) {
            // 當錯誤是可回避的情況時顯示錯誤對話框
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            // 當錯誤是不可回避的情況時只顯示Toast
            String errorMessage = String.format(
                    getString(R.string.text_error_player),
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
            YouTubePlayer player, boolean wasRestored) {
        // 設定YouTube的動畫ID
        if (!wasRestored) {
            player.cueVideo("CZB-CaxLjyw");
        }
    }
}
