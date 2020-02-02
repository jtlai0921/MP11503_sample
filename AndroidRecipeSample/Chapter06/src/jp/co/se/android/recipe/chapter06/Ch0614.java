package jp.co.se.android.recipe.chapter06;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Ch0614 extends Activity implements OnClickListener {
    private static final int REQ_SELECT_GALLERY = 100;
    private static final int REFLECTIONGAP = 4;
    private static final int MAX_IMAGE_SIZE = 1024;

    private ImageView mImageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0614_main);

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
            setMirrorBitmap(data.getData());
        }
    }

    private void setMirrorBitmap(Uri imageuri) {
        // 讀取Bitmap
        Bitmap bitmapOriginal = loadBitmap(imageuri);
        // 計立能產生顛倒圖片的Matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        int height = bitmapOriginal.getHeight();
        int width = bitmapOriginal.getWidth();
        // 建立能寫入顛倒圖片的Bitmap
        Bitmap reflectionImage = Bitmap.createBitmap(bitmapOriginal, 0,
                height / 2, width, height / 2, matrix, false);
        // 建立能寫入原圖片和顛倒圖像合成image的Bitmap
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Config.ARGB_8888);

        // 建立能寫入最後完成image的Canvas
        Canvas canvas = new Canvas(bitmapWithReflection);
        // 描繪原本的圖片
        canvas.drawBitmap(bitmapOriginal, 0, 0, null);
        // 描繪原本的圖片
        Paint deafaultPaint = new Paint();
        // 為使連結處看起來不明顯，描繪原本預設的顏色
        canvas.drawRect(0, height, width, height + REFLECTIONGAP, deafaultPaint);
        // 描繪顛倒的image
        canvas.drawBitmap(reflectionImage, 0, height + REFLECTIONGAP, null);
        // 為使顛倒的部分越往下看起來越白，設定漸層
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                bitmapOriginal.getHeight(), 0, bitmapWithReflection.getHeight()
                        + REFLECTIONGAP, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // 描繪漸層
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + REFLECTIONGAP, paint);

        mImageView1.setImageBitmap(bitmapWithReflection);
    }

    private Bitmap loadBitmap(Uri imageuri) {
        InputStream input = null;
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            input = getContentResolver().openInputStream(imageuri);
            BitmapFactory.decodeStream(input, null, options);
            int inSampleSize = calculateInSampleSize(options, MAX_IMAGE_SIZE,
                    MAX_IMAGE_SIZE);
            input.close();
            input = null;

            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            input = getContentResolver().openInputStream(imageuri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);

            return bitmap;
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

        return null;
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
