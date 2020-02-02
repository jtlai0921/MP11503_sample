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

        // �ܧ󬰥��ù����
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

        // �]�w�bSurfaceView�i�H�ϥΪ��Ӭ۾��悤�ɐݒ�
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
                        Log.e("Chapter833", e.toString(), e);
                    }
                    // ���o�i�ϥΪ��w���ؤo��
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
                        // �T�{�W�����᪺�Ϥ��O�_���b�x�s��
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

            // �x�s�Ҳ��ͪ��Ϥ����
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
