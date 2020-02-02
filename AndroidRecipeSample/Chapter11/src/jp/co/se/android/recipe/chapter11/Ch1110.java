package jp.co.se.android.recipe.chapter11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class Ch1110 extends Activity {
    private static final String SAVE_PATH = "/AndroidRecipe";
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Switch mSwTorch;
    private Button mBtnFlash;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private List<String> mFlashType = null;
    private boolean mIsSave = false;
    private boolean mIsTorch = false;
    private int mFlashIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 變更為全螢幕顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1110_main);

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
                        // 從裝置顯示器的尺寸選擇最適合相機預覽的尺寸
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // 確認閃光燈的支援類型
                        List<String> flashLists = CameraUtil
                                .getSupportFlash(mCamera);
                        if (flashLists != null && flashLists.size() > 0) {
                            mFlashType = new ArrayList<String>();
                            for (int i = 0; i < flashLists.size(); i++) {
                                String type = flashLists.get(i);
                                if (type.equals(Parameters.FLASH_MODE_TORCH)) {
                                    // 可使用Torch模式
                                    mIsTorch = true;
                                    mSwTorch.setVisibility(View.VISIBLE);
                                } else {
                                    // 可使用其他閃光燈模式
                                    mFlashType.add(type);
                                    mBtnFlash.setVisibility(View.VISIBLE);
                                }
                            }
                            // 將照相機的閃光燈模式設為OFF
                            if (mFlashType != null && mFlashType.size() > 0) {
                                mBtnFlash
                                        .setText(getString(R.string.ch1110_label_flash)
                                                + mFlashType.get(0));
                            }
                        }

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
                        // 確認上次拍攝的照片是否在正在儲存中
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

        // 使用Torch照明模式
        mSwTorch = (Switch) findViewById(R.id.torchSwitch);
        mSwTorch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // 設定Torch
                setTorch(isChecked);
            }
        });

        // 設定閃光燈
        mBtnFlash = (Button) findViewById(R.id.flash);
        mBtnFlash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera != null && mFlashType != null
                        && mFlashType.size() > 0) {
                    // 設定照相機閃光燈
                    setCameraFlash();
                }
            }
        });
    }

    /**
     * Torch照明模式的ON/OFF.
     * 
     * @param isUse
     */
    private void setTorch(boolean isUse) {
        if (mCamera != null && mIsTorch) {
            String torchMode = null;
            if (isUse) {
                // Torch ON
                torchMode = Parameters.FLASH_MODE_TORCH;
            } else {
                // Torch OFF
                torchMode = Parameters.FLASH_MODE_OFF;
            }
            Parameters params = mCamera.getParameters();
            params.setFlashMode(torchMode);
            mCamera.setParameters(params);
        }
    }

    /**
     * 切換照相機的閃光燈
     */
    private void setCameraFlash() {
        mFlashIndex++;
        if (mFlashIndex >= mFlashType.size()) {
            mFlashIndex = 0;
        }
        String flashMode = mFlashType.get(mFlashIndex);
        Parameters params = mCamera.getParameters();
        params.setFlashMode(flashMode);
        mCamera.setParameters(params);
        mBtnFlash.setText(getString(R.string.ch1110_label_flash) + flashMode);
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
