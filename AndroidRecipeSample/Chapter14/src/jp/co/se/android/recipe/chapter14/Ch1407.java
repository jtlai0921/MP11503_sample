package jp.co.se.android.recipe.chapter14;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Ch1407 extends Activity {
    private static final int REQ_CAMERA = 0x00;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1407_main);

        findViewById(R.id.linkCamera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 呼叫照相機應用程式
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String path = Environment.getExternalStorageDirectory() + "/"
                        + "capture.jpg";
                mUri = Uri.fromFile(new File(path));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                startActivityForResult(intent, REQ_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            try {
                // 顯示圖片
                ImageView ivPreview = (ImageView) findViewById(R.id.preview);
                ivPreview.setImageURI(mUri);
            } catch (Exception e) {
            }
        }
    }
}
