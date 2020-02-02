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

        // 變更為全螢幕顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.ch1107_main);

        // 設定在SurfaceView可以使用的照相機
        mSurfaceView = (SurfaceView) findViewById(R.id.Preview);

        // 相機預覽的處理
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // 結束臉部辨識
                mCamera.stopFaceDetection();

                // 臉部辨識的結束處理
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

                        // 設定照相機的預覽尺寸
                        params.setPreviewSize(mPreviewSize.width,
                                mPreviewSize.height);
                        mCamera.setParameters(params);
                    }

                    // 開始預覽
                    mCamera.startPreview();

                    // 取得照相機可以辨識的臉部數量
                    int maxFaces = params.getMaxNumDetectedFaces();

                    if (maxFaces > 0) {
                        // 設定可以監聽臉部辨識的監聽器
                        mCamera.setFaceDetectionListener(new FaceDetectionListener() {
                            @Override
                            public void onFaceDetection(Face[] faces,
                                    Camera camera) {
                                // 偵測到臉部之後則將值傳遞給臉部辨識標示器顯示View
                                mFaceMarkerView.faces = faces;
                                mFaceMarkerView.invalidate();
                            }
                        });
                        Toast.makeText(
                                Ch1111.this,
                                getString(R.string.ch1111_camera_maxface,
                                        maxFaces), Toast.LENGTH_SHORT).show();
                    }

                    // 開始臉部辨識
                    mCamera.startFaceDetection();
                }
            }
        });

        // 新增顯示臉部辨識標示器的透明view版面配置
        mFaceMarkerView = new FaceMarkerView(this);
        addContentView(mFaceMarkerView, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    }

    /**
     * 顯示臉部辨識標示器的View
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
            // 為了重疊顯示而將背景設為透明
            canvas.drawColor(Color.TRANSPARENT);

            // 設定臉部辨識標示器的顏色與外框
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(30);

            // 若辨識到臉部則繪製標示器
            if (faces != null) {
                for (int i = 0; i < faces.length; i++) {
                    // 儲存變更前的Canvas狀態
                    int saveState = canvas.save();

                    // 從照相機驅動程式所取得的值是位於-1000~1000之間，所以要使用Matrix使座標成為可變換的狀態
                    Matrix matrix = new Matrix();
                    matrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
                    matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
                    canvas.concat(matrix);

                    // 以所辨識的臉部為中心，繪製矩形與辨識精準度
                    float x = (faces[i].rect.right + faces[i].rect.left) / 2;
                    float y = (faces[i].rect.top + faces[i].rect.bottom) / 2;
                    String score = String.valueOf(faces[i].score);
                    canvas.drawText(score, x, y, paint);
                    canvas.drawRect(faces[i].rect, paint);

                    // 將Canvas回復為原本的狀態
                    canvas.restoreToCount(saveState);
                }
            }
        }
    }
}
