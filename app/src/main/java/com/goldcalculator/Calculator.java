package com.goldcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Layout;
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
import android.widget.TableLayout;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Calculator extends Fragment {

    MainActivity activity;
    private View calculatorView;

    //    계산기 종류
    private Button calculatorMarket, calculatorRealMargin;

    // 가격계산기
    private MaterialButtonToggleGroup goldKind, checkListButtonGroup;

    private TextInputLayout salePrice, goldMount, goldMarketCondition, goldMargin, goldWage;
    private TextInputLayout changeGoldMount, changeGoldMarketCondition, changeGoldWage;

    private Button calculate, reset, gold14kButton, gold18kButton, gold24kButton;
    private TextView calculatedPrice, storePrice, realMargin;
    private LinearLayout calculatedPriceLayout, storePriceLayout, realMarginLayout, calculateHistory, checkList;

    // 실마진 계산
    private LinearLayout salePriceLayout, marginLayout;
    // 실마진 버튼
    private Button checkMarketConditionButton, checkMountButton, checkWageButton;
    // 실마진 인풋


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

        //계산기 종류
        calculatorMarket = calculatorView.findViewById(R.id.calculator_marketcondition);
        calculatorRealMargin = calculatorView.findViewById(R.id.calculator_realmargin);


        //가격 계산기 InputText
        goldMarketCondition = calculatorView.findViewById(R.id.gold_market_condition);
        goldMount = calculatorView.findViewById(R.id.gold_mount);
        goldMargin = calculatorView.findViewById(R.id.gold_margin);
        goldWage = calculatorView.findViewById(R.id.gold_wage);
        //금종류
        goldKind = calculatorView.findViewById(R.id.gold_kind);
        gold14kButton = calculatorView.findViewById(R.id.gold_14k_button);
        gold18kButton = calculatorView.findViewById(R.id.gold_18k_button);
        gold24kButton = calculatorView.findViewById(R.id.gold_24k_button);
        //계산 리셋 버튼
        calculate = calculatorView.findViewById(R.id.calculate);
        reset = calculatorView.findViewById(R.id.reset);
        //결과 텍스트뷰
        calculatedPrice = calculatorView.findViewById(R.id.calculatedPrice);
        storePrice = calculatorView.findViewById(R.id.storePrice);
        calculatedPriceLayout = calculatorView.findViewById(R.id.calculatedPriceLayout);
        storePriceLayout = calculatorView.findViewById(R.id.storePriceLayout);

        //실마진

        salePriceLayout = calculatorView.findViewById(R.id.sale_price_layout);
        marginLayout = calculatorView.findViewById(R.id.marginLayout);
        checkList = calculatorView.findViewById(R.id.checkListLayout);

        //실마진 변경 textInput
        salePrice = calculatorView.findViewById(R.id.gold_sale_price);
        changeGoldMarketCondition = calculatorView.findViewById(R.id.change_gold_market_condition);
        changeGoldMount = calculatorView.findViewById(R.id.change_gold_mount);
        changeGoldWage = calculatorView.findViewById(R.id.change_gold_wage);

        // 실마진 버튼
        checkListButtonGroup = calculatorView.findViewById(R.id.checkListButtonGroup);
        checkMarketConditionButton = calculatorView.findViewById(R.id.checkMarketConditionButton);
        checkMountButton = calculatorView.findViewById(R.id.checkMountButton);
        checkWageButton = calculatorView.findViewById(R.id.checkWageButton);


        realMarginLayout = calculatorView.findViewById(R.id.realMarginLayout);
        realMargin = calculatorView.findViewById(R.id.realMargin);


        // 히스토리
//        calculateHistory = calculatorView.findViewById(R.id.calculateHistory);


        // db

//        HistoryFeedReaderDBHelper DBHelper;
//        SQLiteDatabase db;
//        DBHelper = new HistoryFeedReaderDBHelper(activity.getApplicationContext(), "history.db", null, 1);
//        db = DBHelper.getWritableDatabase();


        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        // 각 텍스트 필드 변수에 저장
        // 가격 계산기
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


        //           콤마 위한 텍스트 워쳐
        EditText salePriceEditText;
        salePriceEditText = salePrice.getEditText();
        CustomTextWatcherMoney salePriceTextWatcher = new CustomTextWatcherMoney(salePriceEditText);
        salePriceEditText.addTextChangedListener(salePriceTextWatcher);

        EditText changeMountEditText, changeMarketConditionEditText, changeWageEditText;

        changeMarketConditionEditText = changeGoldMarketCondition.getEditText();
        changeWageEditText = changeGoldWage.getEditText();
        changeMountEditText = changeGoldMount.getEditText();

        CustomTextWatcherMoney changeMarketConditionTextWatcher = new CustomTextWatcherMoney(changeMarketConditionEditText);
        changeMarketConditionEditText.addTextChangedListener(changeMarketConditionTextWatcher);

        CustomTextWatcherMoney changeWageTextWatcher = new CustomTextWatcherMoney(changeWageEditText);
        changeWageEditText.addTextChangedListener(changeWageTextWatcher);

        CustomTextWatcherGoldMount changeMountTextWatcher = new CustomTextWatcherGoldMount(changeMountEditText);
        changeMountEditText.addTextChangedListener(changeMountTextWatcher);




        // 계산 디폴트
        calculate.setOnClickListener(view -> {
            goodsPriceCalculate(imm, goldMountEditText, goldMarketConditionEditText, goldMarginEditText, goldWageEditText);
        });

        // 가격 계산기 일때 계산
        calculatorMarket.setOnClickListener(isChecked -> {
            calculate.setOnClickListener(null);

            checkList.setVisibility(View.GONE);
            salePriceLayout.setVisibility(View.GONE);
            changeGoldWage.setVisibility(View.GONE);
            changeGoldMarketCondition.setVisibility(View.GONE);
            changeGoldMount.setVisibility(View.GONE);


            marginLayout.setVisibility(View.VISIBLE);

            calculate.setOnClickListener(view -> {
                goodsPriceCalculate(imm, goldMountEditText, goldMarketConditionEditText, goldMarginEditText, goldWageEditText);
            });

        });

        // 실마진 계산기
        calculatorRealMargin.setOnClickListener(isChecked -> {
            calculate.setOnClickListener(null);

            checkList.setVisibility(View.VISIBLE);
            salePriceLayout.setVisibility(View.VISIBLE);
            marginLayout.setVisibility(View.GONE);

            List<Integer> checkedButtonIds = checkListButtonGroup.getCheckedButtonIds();

            if (checkedButtonIds.contains(R.id.checkWageButton)) {
                changeGoldWage.setVisibility(View.VISIBLE);
            }
            if (checkedButtonIds.contains(R.id.checkMarketConditionButton)) {
                changeGoldMarketCondition.setVisibility(View.VISIBLE);
            }
            if (checkedButtonIds.contains(R.id.checkMountButton)) {
                changeGoldMount.setVisibility(View.VISIBLE);
            }


            // 실마진 버튼 클릭시 보이게하기
            checkListButtonGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                @Override
                public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                    if (isChecked) {
                        if (checkedId == R.id.checkMarketConditionButton) {
                            changeGoldMarketCondition.setVisibility(View.VISIBLE);
                        }
                        if (checkedId == R.id.checkWageButton) {
                            changeGoldWage.setVisibility(View.VISIBLE);
                        }
                        if (checkedId == R.id.checkMountButton) {
                            changeGoldMount.setVisibility(View.VISIBLE);
                        }
                    }
                    if (!isChecked) {
                        if (checkedId == R.id.checkMarketConditionButton) {
                            changeGoldMarketCondition.setVisibility(View.GONE);
                        }
                        if (checkedId == R.id.checkWageButton) {
                            changeGoldWage.setVisibility(View.GONE);
                        }
                        if (checkedId == R.id.checkMountButton) {
                            changeGoldMount.setVisibility(View.GONE);
                        }
                    }
                }
            });


            calculate.setOnClickListener(view -> {
                realMarginCalculate(imm, checkListButtonGroup, salePriceEditText,
                        goldMountEditText, changeMountEditText,
                        goldMarketConditionEditText, changeMarketConditionEditText,
                        goldWageEditText, changeWageEditText);
            });

        });


//        리셋 버튼 클릭시 리셋
        reset.setOnClickListener(view -> {
            resetTextInput();
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
        String sqlSelect = "SELECT goldMarketCondition,goldKind,goldMount,goldWage,goldMargin,goldTotalPrice" +
                " FROM history ORDER BY DATETIME DESC LIMIT 4;";
        Cursor c = db.rawQuery(sqlSelect, null);


        LinearLayout.LayoutParams tableLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams textViewLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        TableLayout tableLayout = new TableLayout(activity);

        int cursorCount = c.getCount();
        int textViewCount = 6 * cursorCount;

        TableRow[] tableRow = new TableRow[c.getCount()];
        Button[] button = new Button[cursorCount];

        TextView[] textView = new TextView[textViewCount];


        for (int i = 0; i < textViewCount; i++) {
            textView[i] = new TextView(activity);
        }

        for (int i = 0; i < c.getCount(); i++) {
            tableRow[i] = new TableRow(activity);
            button[i] = new Button(activity);
        }


        Log.d("cursor 크기 : ", String.valueOf(c.getCount()));

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int j = 0; j < c.getCount(); j++) {
                int num = 0;
                for (int i = 0; i < 6; i++) {
                    if (j == 0) num = i;
                    if (j > 0) num = i + 6 * j;
                    Log.d(i + "번째", c.getString(i));
                    textView[num].setText(c.getString(i));
                    tableRow[j].addView(textView[num], textViewLP);
                }
                c.moveToNext();
                tableRow[j].addView(button[j], textViewLP);
                tableLayout.addView(tableRow[j], tableLP);
            }
        }

//        activity.setContentView(tableLayout,tableLP);

    }

    private void resetTextInput() {
        goldMount.getEditText().getText().clear();
        goldMargin.getEditText().getText().clear();
        goldWage.getEditText().getText().clear();

        salePrice.getEditText().getText().clear();
        changeGoldMount.getEditText().getText().clear();
        changeGoldMarketCondition.getEditText().getText().clear();
        changeGoldWage.getEditText().getText().clear();

        calculatedPriceLayout.setVisibility(View.GONE);
        storePriceLayout.setVisibility(View.GONE);
        realMarginLayout.setVisibility(View.GONE);
    }

    private void goodsPriceCalculate(InputMethodManager imm,
                                     EditText goldMountEditText, EditText goldMarketConditionEditText,
                                     EditText goldMarginEditText, EditText goldWageEditText) {

        Calculate cal = new Calculate();

        int checkedButtonId = goldKind.getCheckedButtonId();
        Button checkedButton = (Button) calculatorView.findViewById(checkedButtonId);
        String goldKind = checkedButton.getText().toString();

        double goldRate = cal.goldKind(goldKind);

        String strGoldMount = goldMountEditText.getText().toString();
        String strGoldMarketCondition = goldMarketConditionEditText.getText().toString();
        String strGoldMargin = goldMarginEditText.getText().toString();
        String strGoldWage = goldWageEditText.getText().toString();

        Double doubleGoldMount;
        int intGoldMarketCondition, intGoldMargin, intGoldWage;

        doubleGoldMount = strToDoubleAndCheckNull(strGoldMount, goldMount, "중량");
        if (doubleGoldMount == -1) return;

        intGoldMarketCondition = strToIntAndCheckNull(strGoldMarketCondition, goldMarketCondition, "시세");
        if (intGoldMarketCondition == -1) return;

        intGoldMargin = strToIntAndCheckNull(strGoldMargin, goldMargin, "마진");
        if (intGoldMargin == -1) return;

        intGoldWage = strToIntAndCheckNull(strGoldWage, goldWage, "공임");
        if (intGoldWage == -1) return;


        Double doubleCalculatedPrice = cal.goodsPrice(goldRate, doubleGoldMount, intGoldMarketCondition, intGoldMargin, intGoldWage);

        int ceilPrice = (int) Math.ceil(doubleCalculatedPrice);
        DecimalFormat df = new DecimalFormat("###,###");

        double doubleStorePrice = cal.storePrice(doubleCalculatedPrice);
        int ceilStorePrice = (int) Math.ceil(doubleStorePrice);

        calculatedPrice.setText(df.format(ceilPrice) + " 원");
        storePrice.setText(df.format(ceilStorePrice) + " 원");

        imm.hideSoftInputFromWindow(goldMountEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(goldMarketConditionEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(goldMarginEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(goldWageEditText.getWindowToken(), 0);

        calculatedPriceLayout.setVisibility(View.VISIBLE);
        storePriceLayout.setVisibility(View.VISIBLE);
        realMarginLayout.setVisibility(View.GONE);


    }

    private void realMarginCalculate(InputMethodManager imm, MaterialButtonToggleGroup checkListButtonGroup,
                                     EditText salePriceEditText,
                                     EditText mountEditText, EditText changeMountEditText,
                                     EditText marketConditionEditText, EditText changeMarketConditionEditText,
                                     EditText wageEditText, EditText changeWageEditText) {

        Calculate cal = new Calculate();
        List<Integer> checkedButtonIds = checkListButtonGroup.getCheckedButtonIds();


        int checkedButtonId = goldKind.getCheckedButtonId();
        Button checkedButton = (Button) calculatorView.findViewById(checkedButtonId);
        String goldKind = checkedButton.getText().toString();

        double goldRate = cal.goldKind(goldKind);

        // 숫자 가져오기
        String strSalePrice = salePriceEditText.getText().toString();
        String strGoldMount = mountEditText.getText().toString();
        String strGoldMarketCondition = marketConditionEditText.getText().toString();
        String strGoldWage = wageEditText.getText().toString();

        //실마진 변경 숫자 가져오기
        String strChangeMount = changeMountEditText.getText().toString();
        String strChangeMarketCondition = changeMarketConditionEditText.getText().toString();
        String strChangeWage = changeWageEditText.getText().toString();

        Double doubleMount, doubleChangeMount;
        int intSalePrice, intMarketCondition, intWage, intChangeMarketCondition, intChangeWage;

        intSalePrice = strToIntAndCheckNull(strSalePrice, salePrice, "판매가격");
        if (intSalePrice == -1) return;

        doubleMount = strToDoubleAndCheckNull(strGoldMount, goldMount, "중량");
        if (doubleMount == -1) return;

        intMarketCondition = strToIntAndCheckNull(strGoldMarketCondition, goldMarketCondition, "시세");
        if (intMarketCondition == -1) return;

        intWage = strToIntAndCheckNull(strGoldWage, goldWage, "공임");
        if (intWage == -1) return;

        intChangeMarketCondition = intMarketCondition;
        doubleChangeMount = doubleMount;
        intChangeWage = intWage;

        if (checkedButtonIds.contains(R.id.checkMountButton)) {
            doubleChangeMount = strToDoubleAndCheckNull(strChangeMount, changeGoldMount, "수정된 중량");
            if (doubleChangeMount == -1) return;
        }

        if (checkedButtonIds.contains(R.id.checkMarketConditionButton)) {
            intChangeMarketCondition = strToIntAndCheckNull(strChangeMarketCondition, changeGoldMarketCondition, "수정된 시세");
            if (intChangeMarketCondition == -1) return;
        }

        if (checkedButtonIds.contains(R.id.checkWageButton)) {
            intChangeWage = strToIntAndCheckNull(strChangeWage, changeGoldMount, "수정된 공임");
            if (intChangeWage == -1) return;
        }


        Double doubleCalculatedPrice = cal.realMargin(intSalePrice, goldRate,
                intMarketCondition, intChangeMarketCondition,
                doubleMount, doubleChangeMount,
                intWage, intChangeWage);


        int ceilPrice = (int) Math.ceil(doubleCalculatedPrice);
        DecimalFormat df = new DecimalFormat("###,###");

        realMargin.setText(df.format(ceilPrice) + " 원");


        imm.hideSoftInputFromWindow(salePriceEditText.getWindowToken(), 0);

        imm.hideSoftInputFromWindow(mountEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(marketConditionEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(wageEditText.getWindowToken(), 0);

        if (checkedButtonIds.contains(R.id.checkMarketConditionButton)) {
            imm.hideSoftInputFromWindow(changeMarketConditionEditText.getWindowToken(), 0);
        }

        if (checkedButtonIds.contains(R.id.checkMountButton)) {
            imm.hideSoftInputFromWindow(changeMountEditText.getWindowToken(), 0);
        }

        if (checkedButtonIds.contains(R.id.checkWageButton)) {
            imm.hideSoftInputFromWindow(changeWageEditText.getWindowToken(), 0);
        }

        calculatedPriceLayout.setVisibility(View.GONE);
        storePriceLayout.setVisibility(View.GONE);
        realMarginLayout.setVisibility(View.VISIBLE);
    }


}


//            히스토리 작성

//    Date today = new Date();
//
//    ContentValues values = new ContentValues();
//        values.put("goldMarketCondition", intGoldMarketCondition);
//                values.put("goldKind", goldKind);
//                values.put("goldMount", doubleGoldMount);
//                values.put("goldWage", intGoldWage);
//                values.put("goldMargin", intGoldMargin);
//                values.put("goldTotalPrice", ceilPrice);
//                values.put("datetime", today.toString());
//                db.insert("history", null, values);

//            selectHistory(db);