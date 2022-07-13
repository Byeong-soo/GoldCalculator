package com.goldcalculator;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.goldcalculator.history.HistoryFeedReaderDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNV;
    Calculator fragmentCalculator;
    GoodsList fragmentGoodsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentCalculator = new Calculator();
        fragmentGoodsList = new GoodsList();

        bottomNV = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer, fragmentCalculator).commit();

        bottomNV.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.calculator) {
                getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer, fragmentCalculator).commit();
            }
            if (item.getItemId() == R.id.goodsList) {
                getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer, fragmentGoodsList).commit();
            }
            return true;
        });

        //History DB
//        HistoryFeedReaderDBHelper DBHelper;
//        SQLiteDatabase db;
//        DBHelper = new HistoryFeedReaderDBHelper(MainActivity.this,"history.db",null,1);
//        db = DBHelper.getWritableDatabase();
//        DBHelper.onCreate(db);

    }

    public void onFragmentChange(int index){
        if(index == 0){
            getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer, fragmentCalculator).commit();
        } else if (index == 1 ){
            getSupportFragmentManager().beginTransaction().replace(R.id.menuContainer, fragmentGoodsList).commit();
        }
    }

}