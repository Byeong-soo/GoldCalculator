package com.goldcalculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    MaterialButtonToggleGroup goldKind;
    TextInputLayout goldMount, goldMarketCondition, goldMargin, goldWage;
    Button calculate, reset, gold14kButton, gold18kButton, gold24kButton;
    TextView calculatedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        // 금 종류 눌렀을때


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

            Log.d("checkedButtonId",String.valueOf(checkedButtonId));
            Log.d("goldKind",goldKind);
            Log.d("goldRate",Double.toString(goldRate));

            String strGoldMount = goldMount.getEditText().getText().toString();
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
            double ceilPrice = Math.ceil(doubleCalculatedPrice);
            String lastPrice = new BigDecimal(ceilPrice).toPlainString();
            calculatedPrice.setText(lastPrice);

        });

        reset.setOnClickListener(view -> {
            goldMount.getEditText().getText().clear();
            goldMargin.getEditText().getText().clear();
            goldWage.getEditText().getText().clear();

        });



    }


    public double strToIntAndCheckNull(String str, TextInputLayout textInputLayout,String message) {

        if(TextUtils.isEmpty(str)){
            textInputLayout.requestFocus();
            Toast.makeText(this.getApplicationContext(),message+"을(를) 입력해주세요!",Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            textInputLayout.clearFocus();
            return Double.parseDouble(str);
        }
    }


}