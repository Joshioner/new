package com.blk.health_tool.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class AlarmDbHelper extends SQLiteOpenHelper {


       private Context context;
       public static final String CREATE_ALARM = "CREATE TABLE IF NOT EXISTS alarm(" +
               "alarm_id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "drug_name VARCHAR(50)," +
               "times INTEGER ," +
               "nums INTEGER," +
               "time VARCHAR(50),"+
               "interval_time INTEGER," +
               "date VARCHAR(50)," +
               "days INTEGER," +
               "status SMALLINT DEFAULT 1)";

    public AlarmDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public AlarmDbHelper(Context context){
        super(context, "my.db", null, 1);
        this.context = context;
    }

    @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_ALARM);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
