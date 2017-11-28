package com.stan.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by stan on 27/11/2017.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    protected static final String TABLE_NAME = "Messages";
    protected static final String KEY_ID = "ID";
    protected static final String COLUMN_MESSAGE = "message";
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 2;


    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_MESSAGE + " VARCHAR(50));"
        );
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, old verion was " + oldVersion + " new version is " + newVersion);
    }
}
