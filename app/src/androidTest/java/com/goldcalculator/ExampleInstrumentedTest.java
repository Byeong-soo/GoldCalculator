package com.goldcalculator;

import android.content.Context;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.runner.AndroidJUnit4;

import com.google.common.truth.Truth;

import java.io.File;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void saveDBTest() {
//        HistoryFeedReaderDBHelper DBHelper;
//        SQLiteDatabase db;
//        DBHelper = new HistoryFeedReaderDBHelper(CalculatorTest.this,"history.db",null,1);
//        db = DBHelper.getWritableDatabase();
//        DBHelper.onCreate(db);

        File dbFile = new File("/data/data/com.goldcalculator/databases/history.db");
        boolean fileExist = dbFile.exists();
        Truth.assertThat(fileExist).isEqualTo(true);
    }

}