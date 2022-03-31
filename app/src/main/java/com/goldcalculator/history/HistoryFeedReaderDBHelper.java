package com.goldcalculator.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HistoryFeedReaderDBHelper extends SQLiteOpenHelper{

    public static final int version = 1;
    public static final String name = "history.db";


    public HistoryFeedReaderDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists history ("
                + "goldMarketCondition integer," +
                "goldKind string,"+
                "goldMount double," +
                "goldWage integer," +
                "goldMargin integer," +
                "goldTotalPrice integer," +
                "datetime datetime);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists history";

        db.execSQL(sql);
        onCreate(db);
    }
}
