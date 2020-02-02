package jp.co.se.android.recipe.chapter02;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Ch0236Preference extends Preference implements
        OnRatingBarChangeListener {

    private float mCurrentRating;
    private float mOldRating;

    public Ch0236Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // �]�w�ۭq�������t�m
        setWidgetLayoutResource(R.layout.ch0236_preference);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // ��Preference�Ĥ@���Q�I�s�ɡA��^�ҳ]�w���w�]��
        return a.getFloat(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        // �]�wPreferencen���w�]��
        if (restorePersistedValue) {
            // ��restorePersistedValue��true�ɡA�qSharedPreference���o��
            mCurrentRating = getPersistedFloat(mCurrentRating);
        } else {
            // ��restorePersistedValue��false�ɡA�bPreference�]�w�w�]��
            mCurrentRating = (Float) defaultValue;
            persistFloat(mCurrentRating);
        }
        mOldRating = mCurrentRating;
    }

    @Override
    protected void onBindView(View view) {
        // �NPreference�P�ۭq��View�i��Bind
        final RatingBar rating = (RatingBar) view
                .findViewById(R.id.ratingPreference);
        if (rating != null) {
            rating.setRating(mCurrentRating);
            rating.setOnRatingBarChangeListener(this);
        }
        super.onBindView(view);
    }

    @Override
    public void onRatingChanged(RatingBar rating, float value, boolean arg2) {
        // ��ϥΪ��ܧ�]�w�ɡA�N�|�Q�I�s
        mCurrentRating = (callChangeListener(value)) ? value : mOldRating;
        persistFloat(mCurrentRating);
        mOldRating = mCurrentRating;
    }

}
