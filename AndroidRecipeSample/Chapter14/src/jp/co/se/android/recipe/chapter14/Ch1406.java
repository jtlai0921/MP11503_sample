package jp.co.se.android.recipe.chapter14;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Ch1406 extends Activity {
    private static final int REQ_GALLERY = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1406_main);

        findViewById(R.id.linkGallery).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // �I�s��ï���ε{��
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQ_GALLERY);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GALLERY && resultCode == RESULT_OK) {
            try {
                // �q�ҿ�ܪ��Ϥ��Ӳ���Bitmap
                InputStream in = getContentResolver().openInputStream(
                        data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // ��ܹϤ�
                ImageView ivPreview = (ImageView) findViewById(R.id.preview);
                ivPreview.setImageBitmap(img);
            } catch (Exception e) {
            }
        }
    }
}
