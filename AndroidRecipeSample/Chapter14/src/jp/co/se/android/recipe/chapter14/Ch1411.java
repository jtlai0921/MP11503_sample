package jp.co.se.android.recipe.chapter14;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Ch1411 extends Activity {

    private ListView mListView;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1411_main);

        mSpinner = (Spinner) findViewById(R.id.spnCalendars);
        mListView = (ListView) findViewById(android.R.id.list);
        TextView textView = (TextView) findViewById(android.R.id.empty);
        mListView.setEmptyView(textView);

        // 取得日曆的ID總覽
        ArrayList<CalendarBean> calendarList = getCalendarList(this);
        ArrayAdapter<CalendarBean> calendarAdapter = new ArrayAdapter<CalendarBean>(
                this, android.R.layout.simple_spinner_dropdown_item,
                calendarList);
        mSpinner.setAdapter(calendarAdapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                // 取得選取項目的CALENDAR_ID
                CalendarBean bean = (CalendarBean) mSpinner
                        .getItemAtPosition(position);
                long calendarId = bean.id;

                // 資料的取得與顯示
                ArrayList<CalendarEvent> list = getList(calendarId);
                CalendarEventAdapter adapter = new CalendarEventAdapter(
                        Ch1411.this, list);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 取得事件所屬的日曆ID����������
     */
    public static ArrayList<CalendarBean> getCalendarList(Context context) {
        ArrayList<CalendarBean> list = new ArrayList<CalendarBean>();

        // 存取到日曆的服務提供者
        Cursor c = context.getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        // 一個接一個，反覆取得其值
                        CalendarBean b = new CalendarBean();
                        b.id = c.getLong(c
                                .getColumnIndex(CalendarContract.Calendars._ID));
                        b.name = c
                                .getString(c
                                        .getColumnIndex(CalendarContract.Calendars.NAME));

                        list.add(b);
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        return list;
    }

    /**
     * 取得日曆資訊
     */
    private ArrayList<CalendarEvent> getList(long calendarId) {
        ArrayList<CalendarEvent> list = new ArrayList<CalendarEvent>();
        String selection = CalendarContract.Events.CALENDAR_ID + " = ?";
        String[] selectionArgs = new String[] { Long.toString(calendarId) };
        Cursor c = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[] { CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.EVENT_TIMEZONE,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.EVENT_END_TIMEZONE,
                        CalendarContract.Events.ALL_DAY, }, selection,
                selectionArgs, CalendarContract.Events.DTSTART + " DESC");
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        // @formatter:off
                        CalendarEvent bean = new CalendarEvent();
                        bean.id = c.getLong(c
                                .getColumnIndex(CalendarContract.Events._ID));
                        bean.title = c.getString(c
                                .getColumnIndex(CalendarContract.Events.TITLE));
                        bean.description = c
                                .getString(c
                                        .getColumnIndex(CalendarContract.Events.DESCRIPTION));
                        bean.dtstart = c
                                .getLong(c
                                        .getColumnIndex(CalendarContract.Events.DTSTART));
                        bean.eventTimezone = c
                                .getString(c
                                        .getColumnIndex(CalendarContract.Events.EVENT_TIMEZONE));
                        bean.dtend = c.getLong(c
                                .getColumnIndex(CalendarContract.Events.DTEND));
                        bean.eventEndTimezone = c
                                .getString(c
                                        .getColumnIndex(CalendarContract.Events.EVENT_END_TIMEZONE));
                        list.add(bean);
                        // @formatter:on
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        return list;
    }

    /**
     * 置入日曆資訊
     */
    private static class CalendarEvent {
        private long id;
        private String title;
        private String description;
        private long dtstart;
        private String eventTimezone;
        private long dtend;
        private String eventEndTimezone;
    }

    /**
     * 顯示日曆資訊用的Adapter
     */
    private static class CalendarEventAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private ArrayList<CalendarEvent> mList;

        public CalendarEventAdapter(Context context,
                ArrayList<CalendarEvent> list) {
            mInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CalendarEvent getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            CalendarEvent calendarEvent = mList.get(position);
            if (calendarEvent != null) {
                return calendarEvent.id;
            }
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CalendarEventViewHolder holder;
            if (convertView == null) {
                holder = new CalendarEventViewHolder();

                View v = mInflater.inflate(R.layout.ch1411_listrow, parent,
                        false);
                holder.textTitle = (TextView) v.findViewById(R.id.textTitle);
                holder.textDescription = (TextView) v
                        .findViewById(R.id.textDescription);
                holder.textDt = (TextView) v.findViewById(R.id.textDt);
                v.setTag(holder);

                convertView = v;
            } else {
                holder = (CalendarEventViewHolder) convertView.getTag();
            }

            // 取得目標的資料
            CalendarEvent event = mList.get(position);

            // 填入各個字串
            holder.textTitle.setText(event.title);

            if (TextUtils.isEmpty(event.description)) {
                holder.textDescription.setVisibility(View.GONE);
            } else {
                holder.textDescription.setVisibility(View.VISIBLE);
            }
            holder.textDescription.setText(event.description);

            Time dtstart = new Time();
            dtstart.set(event.dtstart);
            Time dtend = new Time();
            dtstart.set(event.dtend);

            String format = "%Y-%m-%d %H:%M";
            String msg = String.format("開始: %1$s (%2$s)\n結束: %3$s (%4$s)",
                    dtstart.format(format), event.eventTimezone,
                    dtend.format(format), event.eventEndTimezone);
            holder.textDt.setText(msg);

            return convertView;
        }

        /**
         * 保存ListView各行的View
         */
        private static class CalendarEventViewHolder {
            private TextView textTitle;
            private TextView textDescription;
            private TextView textDt;
        }
    }

    public static class CalendarBean {
        long id;
        String name;

        @Override
        public String toString() {
            return this.name != null ? this.name : "";
        }
    }
}
