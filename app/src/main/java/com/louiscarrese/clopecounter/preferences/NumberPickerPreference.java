package com.louiscarrese.clopecounter.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by loule on 07/08/2015.
 */
public class NumberPickerPreference extends DialogPreference {
    private NumberPicker numberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);

        //We will handle persistence ourselves
        setPersistent(false);

    }


    @Override
    protected View onCreateDialogView() {
        numberPicker = new NumberPicker(getContext());
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9999);
        return numberPicker;
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        SharedPreferences prefs = getSharedPreferences();
        numberPicker.setValue(prefs.getInt(this.getKey(), 40));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        numberPicker.clearFocus();
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(this.getKey(), numberPicker.getValue());
            editor.commit();
        }
    }
}
