package jp.co.se.android.recipe.chapter02;

import java.util.TimeZone;

import jp.co.se.android.recipe.chapter02.R;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Ch0235DialogPreference extends DialogPreference {

    /**
     * 在Preference所使用的值，若沒有在偏好設定的XML中定義android:defaultValue，
     * 就可以直接使用這個值
     */
    private String mPreferenceValue = "";

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public Ch0235DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 指定做為對話框的版面配置資源
        setDialogLayoutResource(R.layout.ch0235_main);

        // 指定[OK]鈕的標籤
        setPositiveButtonText(android.R.string.ok);
        // 指定[Cancel]鈕的標籤
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * 若沒有呼叫android:defaultValue的話，進行值的初始化
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        if (restorePersistedValue) {
            mPreferenceValue = getPersistedString(mPreferenceValue);
        } else {
            mPreferenceValue = (String) defaultValue;
            persistString(mPreferenceValue);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // 取得值
        setTimeToView(mPreferenceValue);
    }

    /** 將值反映到畫面上 */
    private void setTimeToView(String preferenceValue) {
        // 將所指定的字串變換到Time中
        Time time = new Time(TimeZone.getDefault().getID());
        try {
            time.parse(preferenceValue);
        } catch (TimeFormatException e) {
            // 若值的變換失敗時(例如所指定的值不正確等等)，就顯示現在時刻
            time.setToNow();
        }

        // 將值設定到畫面
        mDatePicker.updateDate(time.year, time.month, time.monthDay);
        mTimePicker.setCurrentHour(time.hour);
        mTimePicker.setCurrentMinute(time.minute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // 將時間變更為字串
            Time time = new Time(TimeZone.getDefault().getID());
            time.year = mDatePicker.getYear();
            time.month = mDatePicker.getMonth();
            time.monthDay = mDatePicker.getDayOfMonth();
            time.hour = mTimePicker.getCurrentHour();
            time.minute = mTimePicker.getCurrentMinute();
            String newValue = time.format2445();
            if (callChangeListener(newValue)) {
                mPreferenceValue = newValue;
                persistString(mPreferenceValue);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        // 儲存值
        final PreferenceSavedState myState = new PreferenceSavedState(
                superState);
        myState.value = mPreferenceValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null
                || !state.getClass().equals(PreferenceSavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        // 取得所儲存的值
        PreferenceSavedState myState = (PreferenceSavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // 利用原本的值來構成畫面
        setTimeToView(myState.value);
    }

    /**
     *  用來儲存值的類別
     */
    private static class PreferenceSavedState extends BaseSavedState {
        String value;

        public PreferenceSavedState(Parcelable superState) {
            super(superState);
        }

        public PreferenceSavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<PreferenceSavedState> CREATOR = new Parcelable.Creator<PreferenceSavedState>() {

            public PreferenceSavedState createFromParcel(Parcel in) {
                return new PreferenceSavedState(in);
            }

            public PreferenceSavedState[] newArray(int size) {
                return new PreferenceSavedState[size];
            }
        };
    }
}
