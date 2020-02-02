package jp.co.se.android.recipe.chapter14;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Ch1413 extends Activity implements OnClickListener {

    private static final String TAG = Ch1413.class.getSimpleName();
    private EditText mEditHours;
    private EditText mEditMinutes;
    private EditText mEditText;
    private Switch mSwitchSkipUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1413_main);

        mEditHours = (EditText) findViewById(R.id.editHours);
        mEditMinutes = (EditText) findViewById(R.id.editMinutes);
        mEditText = (EditText) findViewById(R.id.editText);
        mSwitchSkipUi = (Switch) findViewById(R.id.switchSkipUi);
        Button btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(this);

        // �]�w�w�]���e
        Time time = new Time();
        time.setToNow();
        time.minute += 2; // ���w2������
        mEditHours.setText(Integer.toString(time.hour));
        mEditMinutes.setText(Integer.toString(time.minute));
        mEditText.setText("�O�ǥ�Android�Z¬���Ӧ���ĵ��");
    }

    @Override
    public void onClick(View v) {
        String message = mEditText.getText().toString();
        String minutesStr = mEditMinutes.getText().toString();
        String hoursStr = mEditHours.getText().toString();
        boolean isSkip = mSwitchSkipUi.isChecked();

        try {
            setAlarm(Integer.parseInt(hoursStr), Integer.parseInt(minutesStr),
                    message, isSkip);
        } catch (NumberFormatException e) {
            // �Y�ҿ�J���Ȥ��O�ƭȮ�
            Toast.makeText(this, "�п�J�ƭ�", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // �Yĵ�����]�w���Ѯ�
            Toast.makeText(this, "�]�w����", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "�]�w����", e);
        }
    }

    /**
     * �]�wĵ��
     */
    private void setAlarm(int hours, int minutes, String message, boolean isSkip)
            throws Exception {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        // ���w��
        intent.putExtra(AlarmClock.EXTRA_HOUR, hours);
        // ���w��
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
        // ���w�T��
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        // �]�w��i�H���w�O�_���Lĵ���e�������
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, isSkip);

        startActivity(intent);
    }
}
