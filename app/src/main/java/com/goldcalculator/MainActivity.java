package com.goldcalculator;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {



    private MaterialButtonToggleGroup goldKind;
    private TextInputLayout goldMount, goldMarketCondition, goldMargin, goldWage;
    private Button calculate, reset, gold14kButton, gold18kButton, gold24kButton;
    private TextView calculatedPrice;
    private LinearLayout calculatedPriceLayout;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // 글자 레이아웃 크기 조정
        // 기준
        goldMarketCondition = findViewById(R.id.gold_market_condition);
        // 나머지
        goldMount = findViewById(R.id.gold_mount);
        goldMargin = findViewById(R.id.gold_margin);
        goldWage = findViewById(R.id.gold_wage);

        int width = goldMarketCondition.getWidth();
        int height = goldMarketCondition.getHeight();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);

        goldMount.setLayoutParams(lp);
        goldMargin.setLayoutParams(lp);
        goldWage.setLayoutParams(lp);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        goldKind = findViewById(R.id.gold_kind);
        goldMount = findViewById(R.id.gold_mount);
        goldMarketCondition = findViewById(R.id.gold_market_condition);
        goldMargin = findViewById(R.id.gold_margin);
        goldWage = findViewById(R.id.gold_wage);
        gold14kButton = findViewById(R.id.gold_14k_button);
        gold18kButton = findViewById(R.id.gold_18k_button);
        gold24kButton = findViewById(R.id.gold_24k_button);
        calculate = findViewById(R.id.calculate);
        reset = findViewById(R.id.reset);
        calculatedPrice = findViewById(R.id.calculatedPrice);
        calculatedPriceLayout = findViewById(R.id.calculatedPriceLayout);



        // 각 텍스트 필드 변수에 저장
        EditText goldMountEditText, goldMarketConditionEditText,goldMarginEditText,goldWageEditText;

        goldMountEditText = goldMount.getEditText();
        goldMarketConditionEditText = goldMarketCondition.getEditText();
        goldMarginEditText = goldMargin.getEditText();
        goldWageEditText = goldWage.getEditText();


        //콤마로 자리 자르기
        CustomTextWatcher mountTextWatcher = new CustomTextWatcher(goldMountEditText);
        CustomTextWatcher marketConditionTextWatcher = new CustomTextWatcher(goldMarketConditionEditText);
        CustomTextWatcher goldMarginTextWatcher = new CustomTextWatcher(goldMarginEditText);
        CustomTextWatcher goldWageWatcher = new CustomTextWatcher(goldWageEditText);

        goldMountEditText.addTextChangedListener(mountTextWatcher);
        goldMarketConditionEditText.addTextChangedListener(marketConditionTextWatcher);
        goldMarginEditText.addTextChangedListener(goldMarginTextWatcher);
        goldWageEditText.addTextChangedListener(goldWageWatcher);




        // 계산하기
        calculate.setOnClickListener(view -> {

            // 포커스 제거

            double goldRate = 0;
            int checkedButtonId=goldKind.getCheckedButtonId();
            Button checkedButton = (Button) findViewById(checkedButtonId);
            String goldKind = checkedButton.getText().toString();

            switch (goldKind){
                case "14k": goldRate = 0.6435;
                break;
                case "18k": goldRate = 0.825;
                break;
                case "24k": goldRate = 1;
                break;
            }


            String strGoldMount = goldMountEditText.getText().toString();
            String strGoldMarketCondition = goldMarketCondition.getEditText().getText().toString();
            String strGoldMargin = goldMargin.getEditText().getText().toString();
            String strGoldWage = goldWage.getEditText().getText().toString();


            Double doubleGoldMount,doubleGoldMarketCondition,doubleGoldMargin,doubleGoldWage;


            doubleGoldMount = strToIntAndCheckNull(strGoldMount,goldMount,"중량");
            if(doubleGoldMount == -1) return;
            doubleGoldMarketCondition = strToIntAndCheckNull(strGoldMarketCondition,goldMarketCondition,"시세");
            if(doubleGoldMarketCondition == -1) return;
            doubleGoldMargin = strToIntAndCheckNull(strGoldMargin,goldMargin,"마진");
            if(doubleGoldMargin == -1) return;
            doubleGoldWage = strToIntAndCheckNull(strGoldWage,goldWage,"공임");
            if(doubleGoldWage == -1) return;



            Double doubleCalculatedPrice = goldRate * doubleGoldMount * (doubleGoldMarketCondition/3.75) + doubleGoldMargin + doubleGoldWage;
            int ceilPrice =(int) Math.ceil(doubleCalculatedPrice);
            DecimalFormat df = new DecimalFormat("###,###");

            calculatedPrice.setText(df.format(ceilPrice) + " 원");
            imm.hideSoftInputFromWindow(goldMountEditText.getWindowToken(),0);
            imm.hideSoftInputFromWindow(goldMarketConditionEditText.getWindowToken(),0);
            imm.hideSoftInputFromWindow(goldMarginEditText.getWindowToken(),0);
            imm.hideSoftInputFromWindow(goldWageEditText.getWindowToken(),0);

            calculatedPriceLayout.setVisibility(View.VISIBLE);

        });

        reset.setOnClickListener(view -> {
            goldMount.getEditText().getText().clear();
            goldMargin.getEditText().getText().clear();
            goldWage.getEditText().getText().clear();
            calculatedPriceLayout.setVisibility(View.INVISIBLE);

        });



    }


    public double strToIntAndCheckNull(String str, TextInputLayout textInputLayout,String message) {

        if(TextUtils.isEmpty(str)){
            textInputLayout.requestFocus();
            Toast.makeText(this.getApplicationContext(),message+"을(를) 입력해주세요!",Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            textInputLayout.clearFocus();
            str = str.replaceAll(",","");
            return Double.parseDouble(str);
        }
    }


}