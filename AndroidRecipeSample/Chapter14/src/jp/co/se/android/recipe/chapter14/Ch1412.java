package jp.co.se.android.recipe.chapter14;

import java.util.ArrayList;
import java.util.TimeZone;

import jp.co.se.android.recipe.chapter14.Ch1411.CalendarBean;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Ch1412 extends Activity implements OnClickListener {

    private EditText mEditTitle;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1412_main);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mSpinner = (Spinner) findViewById(R.id.spnCalendars);
        Button btnDone = (Button) findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        // ���o��䪺ID�`��
        ArrayList<CalendarBean> calendarList = Ch1411.getCalendarList(this);
        ArrayAdapter<CalendarBean> calendarAdapter = new ArrayAdapter<CalendarBean>(
                this, android.R.layout.simple_spinner_dropdown_item,
                calendarList);
        mSpinner.setAdapter(calendarAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnDone) {
            CalendarBean selectedItem = (CalendarBean) mSpinner
                    .getSelectedItem();
            String title = mEditTitle.getText().toString();

            // �s�W�ƥ�
            insertEvent(selectedItem.id, title);

            Toast.makeText(this, "���U����", Toast.LENGTH_SHORT).show();

            // ���U����
            Intent intent = new Intent(this, Ch1411.class);
            startActivity(intent);
        }
    }

    private long insertEvent(long calendarId, String title) {
        // ���o�w�]���ɶ��ϰ�
        // Asia/Taipei ����
        String timezone = TimeZone.getDefault().getID();

        // �s�W�ƥ�
        long nowDate = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, "�Ӧ�Android��媺�ƥ�");
        values.put(CalendarContract.Events.DTSTART, nowDate);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
        values.put(CalendarContract.Events.DTEND, nowDate);
        values.put(CalendarContract.Events.EVENT_END_TIMEZONE, timezone);
        values.put(CalendarContract.Events.ALL_DAY, 1);

        Uri insert = getContentResolver().insert(
                CalendarContract.Events.CONTENT_URI, values);
        long eventId = ContentUris.parseId(insert);
        return eventId;
    }
}
