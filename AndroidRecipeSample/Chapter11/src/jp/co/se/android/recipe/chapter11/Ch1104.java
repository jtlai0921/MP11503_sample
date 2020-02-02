package jp.co.se.android.recipe.chapter11;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1104 extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    TextView mTvAzimuth;
    private float[] mAcMatrix = new float[3];
    private float[] mMgMatrix = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1104_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTvAzimuth = (TextView) findViewById(R.id.Azimuth);
    }

    @Override
    protected void onPause() {
        // 解除感應器
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 註冊感應器
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
        case Sensor.TYPE_ACCELEROMETER: // 取得加速度感應器的值
            mAcMatrix = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:// 取得磁場感應器的值
            mMgMatrix = event.values.clone();
            break;
        }

        if (mMgMatrix != null && mAcMatrix != null) {
            float[] orientation = new float[3];
            float[] R = new float[16];
            float[] I = new float[16];

            // 以加速度感應器與磁場感應器的值為基礎來計算旋轉矩陣
            SensorManager.getRotationMatrix(R, I, mAcMatrix, mMgMatrix);

            // 配合裝置設備面對的方向來計算旋轉矩陣
            SensorManager.getOrientation(R, orientation);

            // 將弧度變換為角度
            float angle = (float) Math.floor(Math.toDegrees(orientation[0]));

            // 將角度的範圍調整為0~360度
            if (angle >= 0) {
                orientation[0] = angle;
            } else if (angle < 0) {
                orientation[0] = 360 + angle;
            }

            // 在畫面中顯示所得到的角度
            mTvAzimuth.setText(String.valueOf(orientation[0]));
        }
    }
}