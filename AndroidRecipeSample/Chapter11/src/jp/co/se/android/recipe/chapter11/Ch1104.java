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
        // �Ѱ��P����
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ���U�P����
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
        case Sensor.TYPE_ACCELEROMETER: // ���o�[�t�׷P��������
            mAcMatrix = event.values.clone();
            break;
        case Sensor.TYPE_MAGNETIC_FIELD:// ���o�ϳ��P��������
            mMgMatrix = event.values.clone();
            break;
        }

        if (mMgMatrix != null && mAcMatrix != null) {
            float[] orientation = new float[3];
            float[] R = new float[16];
            float[] I = new float[16];

            // �H�[�t�׷P�����P�ϳ��P�������Ȭ���¦�ӭp�����x�}
            SensorManager.getRotationMatrix(R, I, mAcMatrix, mMgMatrix);

            // �t�X�˸m�]�ƭ��諸��V�ӭp�����x�}
            SensorManager.getOrientation(R, orientation);

            // �N�����ܴ�������
            float angle = (float) Math.floor(Math.toDegrees(orientation[0]));

            // �N���ת��d��վ㬰0~360��
            if (angle >= 0) {
                orientation[0] = angle;
            } else if (angle < 0) {
                orientation[0] = 360 + angle;
            }

            // �b�e������ܩұo�쪺����
            mTvAzimuth.setText(String.valueOf(orientation[0]));
        }
    }
}