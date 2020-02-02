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
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;

public class Ch1109 extends Activity {
    private static final String SAVE_PATH = "/AndroidRecipe";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Button mBtnFocus;
    private Button mBtnExposure;
    private Size mPreviewSize;
    private boolean mIsSave = false;
    private List<Size> mSupportedPreviewSizes;
    private boolean mIsSupportFocus = false;
    private int mMinExposure;
    private int mMaxExposure;
    private int mExposureValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ÅÜ§ó¬°¥þ¿Ã¹õÅã¥Ü
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1109_main);

        // ³]©w¦bSurfaceView¥i¥H¨Ï¥Îªº·Ó¬Û¾÷
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // ¬Û¾÷¹wÄýªº³B²z
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
                    }
                    // ¨ú±o¥i¨Ï¥Îªº¹wÄý¤Ø¤o
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
                        // ±q¸Ë¸mÅã¥Ü¾¹ªº¤Ø¤o¿ï¾Ü³Ì¾A¦X¬Û¾÷¹wÄýªº¤Ø¤o‚·‚é
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // ½T»{¬O§_¥i¥H¨Ï¥Î¦Û°Ê¹ïµJ
                        mIsSupportFocus = CameraUtil
                                .isSupportFocus(Ch1109.this);
                        if (mIsSupportFocus) {
                            mBtnFocus.setVisibility(View.VISIBLE);
                        }

                        // ½T»{Ãn¥úªº­È
                        mMinExposure = mCamera.getParameters()
                                .getMinExposureCompensation();
                        mMaxExposure = mCamera.getParameters()
                                .getMaxExposureCompensation();
                        mExposureValue = mCamera.getParameters()
                                .getExposureCompensation();
                        mBtnExposure.setText(Ch1109.this
                                .getString(R.string.ch1109_label_exposure)
                                + mExposureValue);

                        // ³]©w·Ó¬Û¾÷ªº¹wÄý¤Ø¤o
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

                    // ¶}©l¹wÄý
                    mCamera.startPreview();
                }
            }
        });

        // ÂIÀ»¬Û¾÷¹wÄý®É
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

        // ¨Ï¥Î¹ïµJ
        mBtnFocus = (Button) findViewById(R.id.focus);
        mBtnFocus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraAutoFocus();

            }
        });

        // ¨Ï¥ÎÃn¥ú
        mBtnExposure = (Button) findViewById(R.id.exposure);
        mBtnExposure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setExposure();
            }
        });
    }

    /**
     * ³]©w·Ó¬Û¾÷ªºÃn¥ú
     */
    private void setExposure() {
        if (mCamera != null) {
            mExposureValue++;
            if (mExposureValue > mMaxExposure) {
                mExposureValue = mMinExposure;
            }
            Parameters params = mCamera.getParameters();
            params.setExposureCompensation(mExposureValue);
            mCamera.setParameters(params);
            mBtnExposure.setText(getString(R.string.ch1109_label_exposure)
                    + mExposureValue);
        }
    }

    /**
    * ¶}©l·Ó¬Û¾÷ªº¦Û°Ê¹ïµJ
     */
    private void setCameraAutoFocus() {
        if (mCamera != null && mIsSupportFocus) {
            mCamera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // ¥ý­«·s³]©w¦Û°Ê¹ïµJ
                    mCamera.cancelAutoFocus();
                    // ¶}©l¦Û°Ê¹ïµJ
                    mCamera.autoFocus(null);
                }
            });
        }
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

            // ³]©w¦sÀÉ¥Ø¼Ð¦ì¸m
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

            // Àx¦s©Ò«Ø¥ßªº¹Ï¤ù¸ê®Æ
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

            // ¦A¦¸°õ¦æ¬Û¾÷¹wÄý
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}
