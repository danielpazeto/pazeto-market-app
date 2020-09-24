package com.pazeto.comercio.widgets;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

public abstract class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et)
    {
        df = new DecimalFormat("#,###.##");
        df.setRoundingMode(RoundingMode.DOWN);
        df.setDecimalSeparatorAlwaysShown(true);


        dfnd = new DecimalFormat("#,###");
        dfnd.setRoundingMode(RoundingMode.DOWN);
        this.et = et;
        hasFractionalPart = false;
    }

    protected abstract void textChangedCallback(double number);

    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);
        String strNumber = "0";
        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString()
                    .replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "")
                    .replace(String.valueOf(df.getCurrency()), "");
            Number n = df.parse(v);

            int cp = et.getSelectionStart();

            if (hasFractionalPart) {
                strNumber = df.format(n);
            } else {
                strNumber = dfnd.format(n);
            }
            et.setText(strNumber);
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }

            textChangedCallback(df.parse(strNumber).doubleValue());
        } catch (NumberFormatException nfe) {
            Log.e(TAG, nfe.getMessage());

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(@NotNull CharSequence s, int start, int before, int count)
    {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}
