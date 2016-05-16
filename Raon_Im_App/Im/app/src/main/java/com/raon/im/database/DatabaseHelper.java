package com.raon.im.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Na Young on 2016-01-20.
 *
 * This class is used to make application database and tables
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_1_NAME = "UserTable";
    public static final String FIELD_ID_1 = "id";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_BIRTHDAYYEAR = "birthday_year";
    public static final String FIELD_BIRTHDAYMONTH = "birthday_month";
    public static final String FIELD_BIRTHDAYDAY = "birthday_day";

    public static final String TABLE_2_NAME = "AppPasswordTable";
    public static final String FIELD_ID_2 = "id";
    public static final String FIELD_APPPW = "appPW";

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;

    // table that stores personal information
    private static final String CREATE_TABLE_1 =
            "create table " + TABLE_1_NAME + "( "
                    + FIELD_ID_1 + " integer primary key, "
                    + FIELD_EMAIL + " varchar(50), "
                    + FIELD_PASSWORD + " varchar(20), "
                    + FIELD_NAME + " varchar(30), "
                    + FIELD_COUNTRY + " varchar(30), "
                    + FIELD_CITY + " varchar(30), "
                    + FIELD_ADDRESS + " varchar(100), "
                    + FIELD_PHONE + " varchar(20), "
                    + FIELD_BIRTHDAYYEAR + " integer, "
                    + FIELD_BIRTHDAYMONTH + " integer, "
                    + FIELD_BIRTHDAYDAY + " integer"
                    +
                    "); ";

    // table that stores application lock password
    private static final String CRETAE_TABLE_2 = "create table " + TABLE_2_NAME + "("
            + FIELD_ID_2 + " integer primary key, "
            + FIELD_APPPW + " varchar(5)" +
            ");";

    // create database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CRETAE_TABLE_2);
    }

    // delete the tables if they already exist
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exist" + TABLE_1_NAME + ";");
        db.execSQL("drop table if exist" + TABLE_2_NAME + ";");
        onCreate(db);
    }
}
