package com.goldcalculator.textwatcher;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class CustomTextWatcherMoney implements TextWatcher {

    private EditText editText;
    String strAmount = "";

    public CustomTextWatcherMoney(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!TextUtils.isEmpty(editable.toString()) && !editable.toString().equals(strAmount)) {
            strAmount = makeStringComma(editable.toString().replace(",", ""));
            editText.setText(strAmount);
            Editable editable2 = editText.getText();
            Selection.setSelection(editable2, strAmount.length());
        }
    }

    protected String makeStringComma(String str) {    // 천단위 콤마설정.
        if (str.length() == 0) {
            return "";
        }
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }

}
