package jp.co.se.android.recipe.chapter11;

import java.io.IOException;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;

public class Ch1107 extends Activity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // �ܧ󬰥��ù����
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

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
                // �Ӭ۾�������ƳB�z
                mCamera = Camera.open();
                if (mCamera != null) {
                    try {
                        mCamera.setPreviewDisplay(mSurfaceHolder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // ���o�i�H�ϥΪ��w���ؤo
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
                        mCamera.setParameters(params);
                    }

                    // �}�l�w��
                    mCamera.startPreview();
                }
            }
        });
    }
}
