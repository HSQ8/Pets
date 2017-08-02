package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_BREED;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_GENDER;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_ID;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_NAME;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_WEIGHT;
import static com.example.android.pets.data.PetContract.PetEntry.TABLE_NAME;

/**
 * Created by HQ on 7/24/2017.
 */

public class PetDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Shelter";
    public static final int DATABASE_VERSION = 1;

    private final String SQL_CREATE_ENTRY = "CREATE TABLE " +
            TABLE_NAME + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME_NAME + " TEXT," +
            COLUMN_NAME_BREED + " TEXT," +
            COLUMN_NAME_GENDER + " INTEGER," +
            COLUMN_NAME_WEIGHT + " INTEGER);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_NAME;

    public PetDBHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("sql make", SQL_CREATE_ENTRY);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


}
