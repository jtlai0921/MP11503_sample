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
     * �bPreference�ҨϥΪ��ȡA�Y�S���b���n�]�w��XML���w�qandroid:defaultValue�A
     * �N�i�H�����ϥγo�ӭ�
     */
    private String mPreferenceValue = "";

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public Ch0235DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // ���w������ܮت������t�m�귽
        setDialogLayoutResource(R.layout.ch0235_main);

        // ���w[OK]�s������
        setPositiveButtonText(android.R.string.ok);
        // ���w[Cancel]�s������
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * �Y�S���I�sandroid:defaultValue���ܡA�i��Ȫ���l��
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

        // ���o��
        setTimeToView(mPreferenceValue);
    }

    /** �N�ȤϬM��e���W */
    private void setTimeToView(String preferenceValue) {
        // �N�ҫ��w���r���ܴ���Time��
        Time time = new Time(TimeZone.getDefault().getID());
        try {
            time.parse(preferenceValue);
        } catch (TimeFormatException e) {
            // �Y�Ȫ��ܴ����Ѯ�(�Ҧp�ҫ��w���Ȥ����T����)�A�N��ܲ{�b�ɨ�
            time.setToNow();
        }

        // �N�ȳ]�w��e��
        mDatePicker.updateDate(time.year, time.month, time.monthDay);
        mTimePicker.setCurrentHour(time.hour);
        mTimePicker.setCurrentMinute(time.minute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // �N�ɶ��ܧ󬰦r��
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

        // �x�s��
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

        // ���o���x�s����
        PreferenceSavedState myState = (PreferenceSavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // �Q�έ쥻���ȨӺc���e��
        setTimeToView(myState.value);
    }

    /**
     *  �Ψ��x�s�Ȫ����O
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
