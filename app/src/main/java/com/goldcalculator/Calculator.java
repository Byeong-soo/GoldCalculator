package com.goldcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.goldcalculator.history.HistoryFeedReaderDBHelper;
import com.goldcalculator.textwatcher.CustomTextWatcherGoldMount;
import com.goldcalculator.textwatcher.CustomTextWatcherMoney;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Date;

public class Calculator extends Fragment {

    MainActivity activity;
    private View calculatorView;

    private MaterialButtonToggleGroup goldKind;
    private TextInputLayout goldMount, goldMarketCondition, goldMargin, goldWage;
    private Button calculate, reset, gold14kButton, gold18kButton, gold24kButton;
    private TextView calculated14kMount,calculated14kPrice,calculated14kStorePrice,
            calculated18kMount,calculated18kPrice,calculated18kStorePrice,
            calculated24kMount,calculated24kPrice,calculated24kStorePrice;
    private LinearLayout calculatedPriceLayout;
    private TableRow result14k,result18k,result24k;

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
        calculated14kPrice = calculatorView.findViewById(R.id.calculated14kPrice);
        calculated14kStorePrice = calculatorView.findViewById(R.id.calculated14kStorePrice);
        calculated14kMount = calculatorView.findViewById(R.id.calculated14kMount);

        calculated18kPrice = calculatorView.findViewById(R.id.calculated18kPrice);
        calculated18kStorePrice = calculatorView.findViewById(R.id.calculated18kStorePrice);
        calculated18kMount = calculatorView.findViewById(R.id.calculated18kMount);

        calculated24kPrice = calculatorView.findViewById(R.id.calculated24kPrice);
        calculated24kStorePrice = calculatorView.findViewById(R.id.calculated24kStorePrice);
        calculated24kMount = calculatorView.findViewById(R.id.calculated24kMount);

        result14k = calculatorView.findViewById(R.id.result14k);
        result18k = calculatorView.findViewById(R.id.result18k);
        result24k = calculatorView.findViewById(R.id.result24k);


        calculatedPriceLayout = calculatorView.findViewById(R.id.calculatedPriceLayout);


        // db

        HistoryFeedReaderDBHelper DBHelper;
        SQLiteDatabase db;
        DBHelper = new HistoryFeedReaderDBHelper(activity.getApplicationContext(),"history.db",null,1);
        db = DBHelper.getWritableDatabase();


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
            double convertRate = 0;
            int checkedButtonId = goldKind.getCheckedButtonId();
            Button checkedButton = (Button) calculatorView.findViewById(checkedButtonId);
            String goldKind = checkedButton.getText().toString();

            switch (goldKind) {
                case "14k":
                    goldRate = 0.6435;
                    convertRate = 0.825;
                    break;
                case "18k":
                    goldRate = 0.825;
                    convertRate = 0.6435;
                    break;
                case "24k":
                    goldRate = 1;
                    convertRate = 1;
                    break;
            }


            String strGoldMount = goldMountEditText.getText().toString();
            String strGoldMarketCondition = goldMarketCondition.getEditText().getText().toString();
            String strGoldMargin = goldMargin.getEditText().getText().toString();
            String strGoldWage = goldWage.getEditText().getText().toString();

            Double doubleGoldMount;
            int intGoldMarketCondition, intGoldMargin, intGoldWage;

            doubleGoldMount = strToDoubleAndCheckNull(strGoldMount, goldMount, "중량");
            if (doubleGoldMount == -1) return;

            intGoldMarketCondition = strToIntAndCheckNull(strGoldMarketCondition, goldMarketCondition, "시세");
            if (intGoldMarketCondition == -1) return;

            intGoldMargin =  strToIntAndCheckNull(strGoldMargin, goldMargin, "마진");
            if (intGoldMargin == -1) return;

            intGoldWage = strToIntAndCheckNull(strGoldWage, goldWage, "공임");
            if (intGoldWage == -1) return;


                double doubleConvertMount;

                if(goldKind.equals("14k")){
                    doubleConvertMount = (double) Math.round(doubleGoldMount * 1.15 * 100) / 100;
                }else{
                    doubleConvertMount = (double) Math.round(doubleGoldMount / 1.15 * 100) / 100;
                }

                double doubleCalculatedPrice = goldRate * doubleGoldMount * (intGoldMarketCondition / 3.75) +intGoldMargin + intGoldWage;

                double doubleConvertPrice = convertRate * doubleConvertMount * (intGoldMarketCondition / 3.75) +intGoldMargin + intGoldWage;


                int ceilPrice = (int) Math.ceil(doubleCalculatedPrice/1000) * 1000;
                int ceilConvertPrice = (int) Math.ceil(doubleConvertPrice/1000) * 1000;
                DecimalFormat df = new DecimalFormat("###,###");

                double doubleCalculatedMount = Math.ceil(doubleGoldMount * 3.75 * 100) / 100;
                double doubleCalculatedConvertMount = Math.ceil(doubleConvertMount * 3.75 * 100) / 100;

                double doubleCalculatedStorePrice;
                double doubleConvertedStorePrice;

                if(doubleCalculatedPrice >= 1500000){
                    doubleCalculatedStorePrice = doubleCalculatedPrice * 1.15;
                }else{
                    doubleCalculatedStorePrice = doubleCalculatedPrice * 1.14;
                }

                if(doubleConvertPrice >= 1500000){
                    doubleConvertedStorePrice = doubleConvertPrice * 1.15;
                }else{
                    doubleConvertedStorePrice = doubleConvertPrice * 1.14;
                }

                int ceilStorePrice = (int) Math.ceil(doubleCalculatedStorePrice/1000) * 1000;
                int ceilConvertStorePrice = (int) Math.ceil(doubleConvertedStorePrice/1000) * 1000;

                if(goldKind.equals("14k") || goldKind.equals("18k")){
                    result14k.setVisibility(View.VISIBLE);
                    result18k.setVisibility(View.VISIBLE);
                    result24k.setVisibility(View.GONE);

                    if(goldKind.equals("14k")){
                        calculated14kMount.setText(doubleGoldMount+ " ("+doubleCalculatedMount+")");
                        calculated14kPrice.setText(df.format(ceilPrice) + " 원");
                        calculated14kStorePrice.setText(df.format(ceilStorePrice) +" 원");

                        calculated18kMount.setText(doubleConvertMount+ " ("+doubleCalculatedConvertMount+")");
                        calculated18kPrice.setText(df.format(ceilConvertPrice) + " 원");
                        calculated18kStorePrice.setText(df.format(ceilConvertStorePrice) +" 원");
                    }

                    if(goldKind.equals("18k")){
                        calculated14kMount.setText(doubleConvertMount+ " ("+doubleCalculatedConvertMount+")");
                        calculated14kPrice.setText(df.format(ceilConvertPrice) + " 원");
                        calculated14kStorePrice.setText(df.format(ceilConvertStorePrice) +" 원");

                        calculated18kMount.setText(doubleGoldMount+ " ("+doubleCalculatedMount+")");
                        calculated18kPrice.setText(df.format(ceilPrice) + " 원");
                        calculated18kStorePrice.setText(df.format(ceilStorePrice) +" 원");
                    }

                }

                if(goldKind.equals("24k")){
                    result14k.setVisibility(View.GONE);
                    result18k.setVisibility(View.GONE);
                    result24k.setVisibility(View.VISIBLE);
                    calculated24kMount.setText(doubleGoldMount+ " ("+doubleCalculatedMount+")");
                    calculated24kPrice.setText(df.format(ceilPrice) + " 원");
                    calculated24kStorePrice.setText(df.format(ceilStorePrice) +" 원");
                }


//            imm.hideSoftInputFromWindow(goldMountEditText.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(goldMarketConditionEditText.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(goldMarginEditText.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(goldWageEditText.getWindowToken(), 0);

            calculatedPriceLayout.setVisibility(View.VISIBLE);

        });


//        리셋 버튼 클릭시 리셋
        reset.setOnClickListener(view -> {
            goldMount.getEditText().getText().clear();
            goldMargin.getEditText().getText().clear();
            goldWage.getEditText().getText().clear();
            calculatedPriceLayout.setVisibility(View.INVISIBLE);
        });






    } //onActivityCreated

    public int strToIntAndCheckNull(String str, TextInputLayout textInputLayout, String message) {

        if (TextUtils.isEmpty(str)) {
            textInputLayout.requestFocus();
            Toast.makeText(activity.getApplicationContext(), message + "을(를) 입력해주세요!", Toast.LENGTH_SHORT).show();
            return -1;
        } else {
            textInputLayout.clearFocus();
            str = str.replaceAll(",", "");
            return Integer.parseInt(str);
        }
    }

    public double strToDoubleAndCheckNull(String str, TextInputLayout textInputLayout, String message) {

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

    // db 에서 불러오기
    public void selectHistory(SQLiteDatabase db) {

        //        불러오기
        String sqlSelect = "SELECT * FROM history ORDER BY DATETIME ASC LIMIT 4;";
        Cursor c = db.rawQuery(sqlSelect,null);

        while(c.moveToNext()){
            Log.d("금시세", c.getString(0));
            Log.d("금종류", c.getString(1));
            Log.d("금양", c.getString(2));
            Log.d("공임", c.getString(3));
            Log.d("마진", c.getString(4));
            Log.d("가격", c.getString(5));




        }

    }




}
