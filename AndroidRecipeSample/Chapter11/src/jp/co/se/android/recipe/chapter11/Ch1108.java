package jp.co.se.android.recipe.chapter11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class Ch1108 extends Activity {
    private static final String SAVE_PATH = "/AndroidRecipe";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private boolean mIsSave = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ÅÜ§ó¬°¥ş¿Ã¹õÅã¥Ü
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

        // ³]©w¦bSurfaceView¥i¥H¨Ï¥Îªº·Ó¬Û¾÷‚æ‚¤‚Éİ’è
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // ¬Û¾÷¹wÄıªº³B²z
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // ·Ó¬Û¾÷ªºµ²§ô³B²z
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // ·Ó¬Û¾÷ªºªì©l¤Æ³B²z
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Chapter833", e.toString(), e);
                    }
                    // ¨ú±o¥i¨Ï¥Îªº¹wÄı¤Ø¤o“¾
                    mSupportedPreviewSizes = mCamera.getParameters()
                            .getSupportedPreviewSizes();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                    int width, int height) {
                if (mCamera != null) {
                    Parameters params = mCamera.getParameters();
                    if (mSupportedPreviewSizes != null) {
                        // ±q¸Ë¸mÅã¥Ü¾¹ªº¤Ø¤o¿ï¾Ü³Ì¾A¦X¬Û¾÷¹wÄıªº¤Ø¤o
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // ³]©w·Ó¬Û¾÷ªº¹wÄı¤Ø¤o
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);

                        // ³]©w·Ó¬Û¾÷ªºÄá¼v¤Ø¤o
                        Size pictureSize = CameraUtil
                                .getSupportPictureSize(mCamera);
                        if (pictureSize != null) {
                            params.setPictureSize(pictureSize.width,
                                    pictureSize.height);
                        }
                        mCamera.setParameters(params);
                    }

                    // ¶}©l¹wÄı
                    mCamera.startPreview();
                }
            }
        });

        // ÂIÀ»¬Û¾÷¹wÄı®É
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCamera != null) {
                        // ½T»{¤W¦¸©çÄáªº¹Ï¤ù¬O§_¥¿¦bÀx¦s¤¤
                        if (!mIsSave) {
                            // ©çÄá·Ó¤ù
                            mCamera.takePicture(null, null, mPictureCallBack);
                            mIsSave = true;
                        }
                    }
                }
                return true;
            }
        });
    }

    /**
     * ·í©Ò©çÄáªºJPEG¹Ï¤ù«Ø¥ß§¹²¦®É©Ò©I¥sªº¦^©I
     */
    @SuppressLint("SimpleDateFormat")
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // ¨S¦³¸ê®Æ®É´N¤£¶i¦æ³B²z
            if (data == null) {
                return;
            }

            // ³]©w¦sÀÉ¥Ø¼Ğ¦ì¸m
            String savePath = Environment.getExternalStorageDirectory()
                    .getPath() + SAVE_PATH;
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdir();
            }
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String imgPath = savePath + "/" + "IMG_"
                    + sdFormat.format(cal.getTime()) + ".jpg";

            // Àx¦s©Ò²£¥Íªº¹Ï¤ù¸ê®Æ
            try {
                FileOutputStream fos = new FileOutputStream(imgPath, true);
                fos.write(data);
                fos.close();

                // §ó·sContentProvider
                ContentValues values = new ContentValues();
                values.put(Images.Media.MIME_TYPE, "image/jpeg");
                values.put("_data", imgPath);
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
            }

            // ¦A¦¸°õ¦æ¬Û¾÷¹wÄı
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}
