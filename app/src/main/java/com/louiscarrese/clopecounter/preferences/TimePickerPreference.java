package com.louiscarrese.clopecounter.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.louiscarrese.clopecounter.R;

/**
 * Created by loule on 06/08/2015.
 */
public class TimePickerPreference extends DialogPreference {


    private TimePicker timePicker;
    private String hourKey;
    private String minuteKey;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TimePickerPreference,
                0, 0);

        this.hourKey = a.getString(R.styleable.TimePickerPreference_hourKey);
        this.minuteKey = a.getString(R.styleable.TimePickerPreference_minuteKey);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);

        //We will handle persistence ourselves
        setPersistent(false);
    }

    @Override
    protected View onCreateDialogView() {
        timePicker = new TimePicker(getContext());
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        SharedPreferences prefs = getSharedPreferences();
        timePicker.setCurrentHour(prefs.getInt(this.hourKey, 4));
        timePicker.setCurrentMinute(prefs.getInt(this.minuteKey, 0));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        timePicker.clearFocus();
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(this.hourKey, timePicker.getCurrentHour());
            editor.putInt(this.minuteKey, timePicker.getCurrentMinute());
            editor.commit();
        }
    }

    public String getHourKey() {
        return hourKey;
    }

    public String getMinuteKey() {
        return minuteKey;
    }
}
