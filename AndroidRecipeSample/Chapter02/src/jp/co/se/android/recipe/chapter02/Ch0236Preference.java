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
        // 設定自訂的版面配置
        setWidgetLayoutResource(R.layout.ch0236_preference);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // 當Preference第一次被呼叫時，返回所設定的預設值
        return a.getFloat(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        // 設定Preferencen的預設值
        if (restorePersistedValue) {
            // 當restorePersistedValue為true時，從SharedPreference取得值
            mCurrentRating = getPersistedFloat(mCurrentRating);
        } else {
            // 當restorePersistedValue為false時，在Preference設定預設值
            mCurrentRating = (Float) defaultValue;
            persistFloat(mCurrentRating);
        }
        mOldRating = mCurrentRating;
    }

    @Override
    protected void onBindView(View view) {
        // 將Preference與自訂的View進行Bind
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
        // 當使用者變更設定時，就會被呼叫
        mCurrentRating = (callChangeListener(value)) ? value : mOldRating;
        persistFloat(mCurrentRating);
        mOldRating = mCurrentRating;
    }

}
