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

        // 變更為全螢幕顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1109_main);

        // 設定在SurfaceView可以使用的照相機
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // 相機預覽的處理
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 照相機的結束處理
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 照相機的初始化處理
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 取得可使用的預覽尺寸
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
                        // 從裝置顯示器的尺寸選擇最適合相機預覽的尺寸����
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // 確認是否可以使用自動對焦
                        mIsSupportFocus = CameraUtil
                                .isSupportFocus(Ch1109.this);
                        if (mIsSupportFocus) {
                            mBtnFocus.setVisibility(View.VISIBLE);
                        }

                        // 確認曝光的值
                        mMinExposure = mCamera.getParameters()
                                .getMinExposureCompensation();
                        mMaxExposure = mCamera.getParameters()
                                .getMaxExposureCompensation();
                        mExposureValue = mCamera.getParameters()
                                .getExposureCompensation();
                        mBtnExposure.setText(Ch1109.this
                                .getString(R.string.ch1109_label_exposure)
                                + mExposureValue);

                        // 設定照相機的預覽尺寸
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);

                        // 設定照相機的攝影尺寸
                        Size pictureSize = CameraUtil
                                .getSupportPictureSize(mCamera);
                        if (pictureSize != null) {
                            params.setPictureSize(pictureSize.width,
                                    pictureSize.height);
                        }
                        mCamera.setParameters(params);
                    }

                    // 開始預覽
                    mCamera.startPreview();
                }
            }
        });

        // 點擊相機預覽時
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCamera != null) {
                        // 確認上次拍攝的圖片是否正在儲存中
                        if (!mIsSave) {
                            // 拍攝照片
                            mCamera.takePicture(null, null, mPictureCallBack);
                            mIsSave = true;
                        }
                    }
                }
                return true;
            }
        });

        // 使用對焦
        mBtnFocus = (Button) findViewById(R.id.focus);
        mBtnFocus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraAutoFocus();

            }
        });

        // 使用曝光
        mBtnExposure = (Button) findViewById(R.id.exposure);
        mBtnExposure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setExposure();
            }
        });
    }

    /**
     * 設定照相機的曝光
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
    * 開始照相機的自動對焦
     */
    private void setCameraAutoFocus() {
        if (mCamera != null && mIsSupportFocus) {
            mCamera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    // 先重新設定自動對焦
                    mCamera.cancelAutoFocus();
                    // 開始自動對焦
                    mCamera.autoFocus(null);
                }
            });
        }
    }

    /**
     * 當所拍攝的JPEG圖片建立完畢時所呼叫的回呼
     */
    @SuppressLint("SimpleDateFormat")
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // 沒有資料時就不進行處理
            if (data == null) {
                return;
            }

            // 設定存檔目標位置
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

            // 儲存所建立的圖片資料
            try {
                FileOutputStream fos = new FileOutputStream(imgPath, true);
                fos.write(data);
                fos.close();

                // 更新ContentProvider
                ContentValues values = new ContentValues();
                values.put(Images.Media.MIME_TYPE, "image/jpeg");
                values.put("_data", imgPath);
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
            }

            // 再次執行相機預覽
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}
