package io.tanjundang.chat.base.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author: TanJunDang
 * Email: TanJunDang@126.com
 * Date:2017/4/16
 */

public class SqlTool extends SQLiteOpenHelper {

    public static int version = 1;
    private static String DB_NAME = "tjd.db";

    public SqlTool(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table person if not exists(id integer primary key,name varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("alter table person add column age integer");
        }
    }
}
