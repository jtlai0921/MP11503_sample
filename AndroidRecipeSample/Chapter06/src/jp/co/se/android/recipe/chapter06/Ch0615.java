package jp.co.se.android.recipe.chapter06;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Ch0615 extends Activity implements OnClickListener {
    private static final int REQ_SELECT_GALLERY = 100;
    private static final int MAX_IMAGE_SIZE = 1024;

    private TextView mTextSrcSize;
    private TextView mTextDestSize;
    private ImageView mImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0615_main);

        mTextSrcSize = (TextView) findViewById(R.id.textSrcSize);
        mTextDestSize = (TextView) findViewById(R.id.textDestSize);
        mImageView1 = (ImageView) findViewById(R.id.imageView1);

        findViewById(R.id.button1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQ_SELECT_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SELECT_GALLERY && resultCode == RESULT_OK) {
            setLargeImage(data.getData());
        }
    }

    private void setLargeImage(Uri imageuri) {
        InputStream input = null;
        try {
            // 取得圖像資訊
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            input = getContentResolver().openInputStream(imageuri);
            BitmapFactory.decodeStream(input, null, options);
            // 計算重新取樣的值
            int inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE,
                    MAX_IMAGE_SIZE);
            input.close();
            input = null;
            mTextSrcSize.setText(options.outWidth + "x" + options.outHeight);
            // 進行重新取樣並讀取圖像
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            input = getContentResolver().openInputStream(imageuri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
            mTextDestSize.setText(options.outWidth + "x" + options.outHeight);

            mImageView1.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

}
