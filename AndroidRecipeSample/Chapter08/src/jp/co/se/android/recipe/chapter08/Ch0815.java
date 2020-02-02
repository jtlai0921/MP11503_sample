package jp.co.se.android.recipe.chapter08;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class Ch0815 extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0815_main);

        mTextView = (TextView) findViewById(R.id.text);

        // 使用容易操作的Uri型態來建構URL
        Uri uri = Uri
                .parse("https://user:pass@www.example.com:8080/path/to/index.html?parameter1=parameter1value&parameter2=value#fragment");
        String parsedUriMessage = getParsedUriMessage(uri);

        mTextView.setText(parsedUriMessage);
    }

    private String getParsedUriMessage(Uri uri) {
        StringBuilder sb = new StringBuilder();

        sb.append("URI: ").append(uri.toString()).append("\n\n");

        sb.append("scheme: ").append(uri.getScheme()).append("\n");
        sb.append("scheme-specific-part:").append(uri.getSchemeSpecificPart())
                .append("\n");
        sb.append("userinfo: ").append(uri.getUserInfo()).append("\n");
        sb.append("authority: ").append(uri.getAuthority()).append("\n");
        sb.append("host: ").append(uri.getHost()).append("\n");
        sb.append("port: ").append(uri.getPort()).append("\n");
        sb.append("path-segments: ").append(uri.getPathSegments()).append("\n");
        sb.append("last-path-segment: ").append(uri.getLastPathSegment())
                .append("\n");
        sb.append("query: ").append(uri.getQuery()).append("\n");
        sb.append("fragment: ").append(uri.getFragment()).append("\n");

        return sb.toString();
    }
}
