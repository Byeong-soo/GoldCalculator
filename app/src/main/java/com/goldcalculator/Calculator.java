package com.goldcalculator;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldcalculator.textwatcher.CustomTextWatcherGoldMount;
import com.goldcalculator.textwatcher.CustomTextWatcherMoney;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

public class Calculator extends Fragment {

    MainActivity activity;
    private View calculatorView;

    private MaterialButtonToggleGroup goldKind;
    private TextInputLayout goldMount, goldMarketCondition, goldMargin, goldWage;
    private Button calculate, reset, gold14kButton, gold18kButton, gold24kButton;
    private TextView calculatedPrice;
    private LinearLayout calculatedPriceLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup calculatorView = (ViewGroup) inflater.inflate(R.layout.calculator, container, false);
        return calculatorView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calculatorView = getView();
        goldKind = calculatorView.findViewById(R.id.gold_kind);
        goldMount = calculatorView.findViewById(R.id.gold_mount);
        goldMarketCondition = calculatorView.findViewById(R.id.gold_market_condition);
        goldMargin = calculatorView.findViewById(R.id.gold_margin);
        goldWage = calculatorView.findViewById(R.id.gold_wage);
        gold14kButton = calculatorView.findViewById(R.id.gold_14k_button);
        gold18kButton = calculatorView.findViewById(R.id.gold_18k_button);
        gold24kButton = calculatorView.findViewById(R.id.gold_24k_button);
        calculate = calculatorView.findViewById(R.id.calculate);
        reset = calculatorView.findViewById(R.id.reset);
        calculatedPrice = calculatorView.findViewById(R.id.calculatedPrice);
        calculatedPriceLayout = calculatorView.findViewById(R.id.calculatedPriceLayout);


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        // 각 텍스트 필드 변수에 저장
        EditText goldMountEditText, goldMarketConditionEditText, goldMarginEditText, goldWageEditText;

        goldMountEditText = goldMount.getEditText();
        goldMarketConditionEditText = goldMarketCondition.getEditText();
        goldMarginEditText = goldMargin.getEditText();
        goldWageEditText = goldWage.getEditText();


        //콤마로 자리 자르기
        CustomTextWatcherGoldMount mountTextWatcher = new CustomTextWatcherGoldMount(goldMountEditText);

        CustomTextWatcherMoney marketConditionTextWatcher = new CustomTextWatcherMoney(goldMarketConditionEditText);
        CustomTextWatcherMoney goldMarginTextWatcher = new CustomTextWatcherMoney(goldMarginEditText);
        CustomTextWatcherMoney goldWageWatcher = new CustomTextWatcherMoney(goldWageEditText);

        goldMountEditText.addTextChangedListener(mountTextWatcher);

        goldMarketConditionEditText.addTextChangedListener(marketConditionTextWatcher);
        goldMarginEditText.addTextChangedListener(goldMarginTextWatcher);
        goldWageEditText.addTextChangedListener(goldWageWatcher);


        // 계산하기
        calculate.setOnClickListener(view -> {

            // 포커스 제거

            double goldRate = 0;
            int checkedButtonId = goldKind.getCheckedButtonId();
            Button checkedButton = (Button) calculatorView.findViewById(checkedButtonId);
            String goldKind = checkedButton.getText().toString();

            switch (goldKind) {
                case "14k":
                    goldRate = 0.6435;
                    break;
                case "18k":
                    goldRate = 0.825;
                    break;
                case "24k":
                    goldRate = 1;
                    break;
            }


            String strGoldMount = goldMountEditText.getText().toString();
            String strGoldMarketCondition = goldMarketCondition.getEditText().getText().toString();
            String strGoldMargin = goldMargin.getEditText().getText().toString();
            String strGoldWage = goldWage.getEditText().getText().toString();


            Double doubleGoldMount, doubleGoldMarketCondition, doubleGoldMargin, doubleGoldWage;


            doubleGoldMount = strToIntAndCheckNull(strGoldMount, goldMount, "중량");
            if (doubleGoldMount == -1) return;
            doubleGoldMarketCondition = strToIntAndCheckNull(strGoldMarketCondition, goldMarketCondition, "시세");
            if (doubleGoldMarketCondition == -1) return;
            doubleGoldMargin = strToIntAndCheckNull(strGoldMargin, goldMargin, "마진");
            if (doubleGoldMargin == -1) return;
            doubleGoldWage = strToIntAndCheckNull(strGoldWage, goldWage, "공임");
            if (doubleGoldWage == -1) return;


            Double doubleCalculatedPrice = goldRate * doubleGoldMount * (doubleGoldMarketCondition / 3.75) + doubleGoldMargin + doubleGoldWage;
            int ceilPrice = (int) Math.ceil(doubleCalculatedPrice);
            DecimalFormat df = new DecimalFormat("###,###");

            calculatedPrice.setText(df.format(ceilPrice) + " 원");

            imm.hideSoftInputFromWindow(goldMountEditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(goldMarketConditionEditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(goldMarginEditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(goldWageEditText.getWindowToken(), 0);

            calculatedPriceLayout.setVisibility(View.VISIBLE);

        });

        reset.setOnClickListener(view -> {
            goldMount.getEditText().getText().clear();
            goldMargin.getEditText().getText().clear();
            goldWage.getEditText().getText().clear();
            calculatedPriceLayout.setVisibility(View.INVISIBLE);
        });

    }

    public double strToIntAndCheckNull(String str, TextInputLayout textInputLayout, String message) {

        if (TextUtils.isEmpty(str)) {
            textInputLayout.requestFocus();
            Toast.makeText(activity.getApplicationContext(), message + "을(를) 입력해주세요!", Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            textInputLayout.clearFocus();
            str = str.replaceAll(",", "");
            return Double.parseDouble(str);
        }


    }
}
