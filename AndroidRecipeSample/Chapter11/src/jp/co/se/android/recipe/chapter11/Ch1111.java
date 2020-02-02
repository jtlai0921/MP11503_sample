package jp.co.se.android.recipe.chapter11;

import java.io.IOException;
import java.util.List;

import jp.co.se.android.recipe.utils.CameraUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class Ch1111 extends Activity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera = null;
    private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private FaceMarkerView mFaceMarkerView;

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
                // �����y������
                mCamera.stopFaceDetection();

                // �y�����Ѫ������B�z
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

                        // �]�w�Ӭ۾����w���ؤo
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);
                        mCamera.setParameters(params);
                    }

                    // �}�l�w��
                    mCamera.startPreview();

                    // ���o�Ӭ۾��i�H���Ѫ��y���ƶq
                    int maxFaces = params.getMaxNumDetectedFaces();

                    if (maxFaces > 0) {
                        // �]�w�i�H��ť�y�����Ѫ���ť��
                        mCamera.setFaceDetectionListener(new FaceDetectionListener() {
                            @Override
                            public void onFaceDetection(Face[] faces,
                                    Camera camera) {
                                // �������y������h�N�ȶǻ����y�����ѼХܾ����View
                                mFaceMarkerView.faces = faces;
                                mFaceMarkerView.invalidate();
                            }
                        });
                        Toast.makeText(
                                Ch1111.this,
                                getString(R.string.ch1111_camera_maxface,
                                        maxFaces), Toast.LENGTH_SHORT).show();
                    }

                    // �}�l�y������
                    mCamera.startFaceDetection();
                }
            }
        });

        // �s�W����y�����ѼХܾ����z��view�����t�m
        mFaceMarkerView = new FaceMarkerView(this);
        addContentView(mFaceMarkerView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    /**
     * ����y�����ѼХܾ���View
     */
    @SuppressLint("DrawAllocation")
    private class FaceMarkerView extends View {
        Face[] faces;

        public FaceMarkerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // ���F���|��ܦӱN�I���]���z��
            canvas.drawColor(Color.TRANSPARENT);

            // �]�w�y�����ѼХܾ����C��P�~��
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(30);

            // �Y���Ѩ��y���hø�s�Хܾ�
            if (faces != null) {
                for (int i = 0; i < faces.length; i++) {
                    // �x�s�ܧ�e��Canvas���A
                    int saveState = canvas.save();

                    // �q�Ӭ۾��X�ʵ{���Ҩ��o���ȬO���-1000~1000�����A�ҥH�n�ϥ�Matrix�Ϯy�Ц����i�ܴ������A
                    Matrix matrix = new Matrix();
                    matrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
                    matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
                    canvas.concat(matrix);

                    // �H�ҿ��Ѫ��y�������ߡAø�s�x�λP���Ѻ�ǫ�
                    float x = (faces[i].rect.right + faces[i].rect.left) / 2;
                    float y = (faces[i].rect.top + faces[i].rect.bottom) / 2;
                    String score = String.valueOf(faces[i].score);
                    canvas.drawText(score, x, y, paint);
                    canvas.drawRect(faces[i].rect, paint);

                    // �NCanvas�^�_���쥻�����A
                    canvas.restoreToCount(saveState);
                }
            }
        }
    }
}
