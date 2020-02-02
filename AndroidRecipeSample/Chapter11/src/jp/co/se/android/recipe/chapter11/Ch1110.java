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

        // �ܧ󬰥��ù����
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1110_main);

        // �]�w�bSurfaceView�i�H�ϥΪ��Ӭ۾�
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // �۾��w�����B�z
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // �Ӭ۾��������B�z
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // �Ӭ۾�����l�ƳB�z
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // ���o�i�ϥΪ��w���ؤo
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
                        // �q�˸m��ܾ����ؤo��ܳ̾A�X�۾��w�����ؤo
                        mPreviewSize = CameraUtil.getOptimalPreviewSize(
                                mSupportedPreviewSizes, height, width);

                        // �T�{�{���O���䴩����
                        List<String> flashLists = CameraUtil
                                .getSupportFlash(mCamera);
                        if (flashLists != null && flashLists.size() > 0) {
                            mFlashType = new ArrayList<String>();
                            for (int i = 0; i < flashLists.size(); i++) {
                                String type = flashLists.get(i);
                                if (type.equals(Parameters.FLASH_MODE_TORCH)) {
                                    // �i�ϥ�Torch�Ҧ�
                                    mIsTorch = true;
                                    mSwTorch.setVisibility(View.VISIBLE);
                                } else {
                                    // �i�ϥΨ�L�{���O�Ҧ�
                                    mFlashType.add(type);
                                    mBtnFlash.setVisibility(View.VISIBLE);
                                }
                            }
                            // �N�Ӭ۾����{���O�Ҧ��]��OFF
                            if (mFlashType != null && mFlashType.size() > 0) {
                                mBtnFlash
                                        .setText(getString(R.string.ch1110_label_flash)
                                                + mFlashType.get(0));
                            }
                        }

                        // �]�w�Ӭ۾����w���ؤo
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);

                        // �]�w�Ӭ۾�����v�ؤo
                        Size pictureSize = CameraUtil
                                .getSupportPictureSize(mCamera);
                        if (pictureSize != null) {
                            params.setPictureSize(pictureSize.width,
                                    pictureSize.height);
                        }
                        mCamera.setParameters(params);
                    }

                    // �}�l�w��
                    mCamera.startPreview();
                }
            }
        });

        // �I���۾��w����
        mSurfaceView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCamera != null) {
                        // �T�{�W�����᪺�Ӥ��O�_�b���b�x�s��
                        if (!mIsSave) {
                            // ����Ӥ�
                            mCamera.takePicture(null, null, mPictureCallBack);
                            mIsSave = true;
                        }
                    }
                }
                return true;
            }
        });

        // �ϥ�Torch�ө��Ҧ�
        mSwTorch = (Switch) findViewById(R.id.torchSwitch);
        mSwTorch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                // �]�wTorch
                setTorch(isChecked);
            }
        });

        // �]�w�{���O
        mBtnFlash = (Button) findViewById(R.id.flash);
        mBtnFlash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCamera != null && mFlashType != null
                        && mFlashType.size() > 0) {
                    // �]�w�Ӭ۾��{���O
                    setCameraFlash();
                }
            }
        });
    }

    /**
     * Torch�ө��Ҧ���ON/OFF.
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
     * �����Ӭ۾����{���O
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
     * ��ҩ��᪺JPEG�Ϥ��إߧ����ɩҩI�s���^�I
     */
    @SuppressLint("SimpleDateFormat")
    private Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // �S����ƮɴN���i��B�z
            if (data == null) {
                return;
            }

            // �]�w�s�ɥؼЦ�m
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

            // �x�s�ҫإߪ��Ϥ����
            try {
                FileOutputStream fos = new FileOutputStream(imgPath, true);
                fos.write(data);
                fos.close();

                // ��sContentProvider
                ContentValues values = new ContentValues();
                values.put(Images.Media.MIME_TYPE, "image/jpeg");
                values.put("_data", imgPath);
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            } catch (Exception e) {
            }

            // �A������۾��w��
            mIsSave = false;
            mCamera.startPreview();
        }
    };
}
