package com.blk.common.identify;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lzx on 2017/11/6.
 */

public class alarmDb extends SQLiteOpenHelper {
    public alarmDb(Context context) {
        super(context, "db", null, 1);
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS alarm(" +
                    "alarm_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "begin_date TEXT," +
                    "begin_time TEXT," +
                    "alarm_content TEXT)");

//        db.execSQL("DROP TABLE alarm");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
