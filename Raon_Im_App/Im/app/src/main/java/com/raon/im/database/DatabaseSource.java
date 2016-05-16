package com.raon.im.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Na Young on 2016-01-20.
 *
 * This class is used for the communication with the database
 */
public class DatabaseSource {
    private SQLiteDatabase database;
    private DatabaseHelper helper;
    long insertID_1, insertID_2;

    //  data fields for personal information table
    private String[] allColumns_1 = {
            DatabaseHelper.FIELD_ID_1
            , DatabaseHelper.FIELD_EMAIL
            , DatabaseHelper.FIELD_PASSWORD
            , DatabaseHelper.FIELD_NAME
            , DatabaseHelper.FIELD_COUNTRY
            , DatabaseHelper.FIELD_CITY
            , DatabaseHelper.FIELD_ADDRESS
            , DatabaseHelper.FIELD_PHONE
            , DatabaseHelper.FIELD_BIRTHDAYYEAR
            , DatabaseHelper.FIELD_BIRTHDAYMONTH
            , DatabaseHelper.FIELD_BIRTHDAYDAY
    };

    //  data fields for application lock password table
    private String[] allColumns_2 = {
            DatabaseHelper.FIELD_ID_2
            , DatabaseHelper.FIELD_APPPW
    };

    public DatabaseSource(Context context) {
        helper = new DatabaseHelper(context);
    }

    // used to open database
    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    // used to close database
    public void close() {
        database.close();
    }

    // used to store application lock password
    public DataField insertAppPW(String appPW) {
        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FIELD_APPPW, appPW);

            insertID_2 = database.insert(DatabaseHelper.TABLE_2_NAME, null, values);

            Log.i("APP PW", appPW);

            cursor = database.query(DatabaseHelper.TABLE_2_NAME
                    , allColumns_2, DatabaseHelper.FIELD_ID_2 + "=" + insertID_2
                    , null, null, null, null);

            cursor.moveToFirst();

            return cursorToData_2(cursor);
        } finally {
            closeCursor(cursor);
        }
    }

    // used to update the application lock password
    public void updateAppPW(String appPW, String newAppPW) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FIELD_APPPW, newAppPW);

        database.update(DatabaseHelper.TABLE_2_NAME, values, "appPW=?", new String[]{appPW});
    }

    // used to store email and password when signing up
    public DataField insertEmailData(String email, String password) {
        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.FIELD_EMAIL, email);
            values.put(DatabaseHelper.FIELD_PASSWORD, password);

            insertID_1 = database.insert(DatabaseHelper.TABLE_1_NAME, null, values);

            cursor = database.query(DatabaseHelper.TABLE_1_NAME
                    , allColumns_1, DatabaseHelper.FIELD_ID_1 + "=" + insertID_1
                    , null, null, null, null);

            cursor.moveToFirst();

            return cursorToData_1(cursor);
        } finally {
            closeCursor(cursor);
        }
    }

    // used to update personal data
    public void updateData(String name, String country, String city, String address, String phone, String birthday_year, String birthday_month, String birthday_day, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FIELD_NAME, name);
        values.put(DatabaseHelper.FIELD_COUNTRY, country);
        values.put(DatabaseHelper.FIELD_CITY, city);
        values.put(DatabaseHelper.FIELD_ADDRESS, address);
        values.put(DatabaseHelper.FIELD_PHONE, phone);
        values.put(DatabaseHelper.FIELD_BIRTHDAYYEAR, birthday_year);
        values.put(DatabaseHelper.FIELD_BIRTHDAYMONTH, birthday_month);
        values.put(DatabaseHelper.FIELD_BIRTHDAYDAY, birthday_day);

        database.update(DatabaseHelper.TABLE_1_NAME, values, "email=?", new String[]{email});
    }

    // used to check whether email and password are right
    public boolean isRightPassword(String email, String password) {
        Cursor cursor = null;
        int emailIndex = 0;
        int passwdIndex = 0;

        try {
            cursor = database.query(DatabaseHelper.TABLE_1_NAME
                    , allColumns_1
                    , null, null, null, null, null);
            cursor.moveToFirst();

            emailIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_EMAIL);
            passwdIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_PASSWORD);
            if (cursor.getString(emailIndex).equals(email)) {
                Log.i("EMAIL FOR CHECK", cursor.getString(emailIndex));
                if (cursor.getString(passwdIndex).equals(password)) {
                    Log.i("PASSWORD FOR CHECK", cursor.getString(passwdIndex));
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    // used to fetch record from personal information table
    public DataField getData_1() {
        DataField data = new DataField();
        Cursor cursor = null;

        try {
            cursor = database.query(DatabaseHelper.TABLE_1_NAME
                    , allColumns_1
                    , null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                data = cursorToData_1(cursor);
                cursor.moveToNext();
            }

            return data;
        } finally {
            closeCursor(cursor);
        }
    }

    // used to fetch record from application lock password table
    public DataField getData_2() {
        DataField data = new DataField();
        Cursor cursor = null;

        try {
            cursor = database.query(DatabaseHelper.TABLE_2_NAME
                    , allColumns_2
                    , null, null, null, null, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                data = cursorToData_2(cursor);
                cursor.moveToNext();
            }

            return data;
        } finally {
            closeCursor(cursor);
        }
    }

    // used to change the value of the cursor to data type, and return the data from personal information table
    private DataField cursorToData_1(Cursor cursor) {
        DataField field = new DataField();

        int idIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_ID_1);
        int emailIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_EMAIL);
        int passwdIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_PASSWORD);
        int nameIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_NAME);
        int countryIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_COUNTRY);
        int cityIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_CITY);
        int addressIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_ADDRESS);
        int phoneIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_PHONE);
        int birthdayYearIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_BIRTHDAYYEAR);
        int birthdayMonthIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_BIRTHDAYMONTH);
        int birthdayDayIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_BIRTHDAYDAY);

        field.setId_1(cursor.getLong(idIndex));
        field.setEmail(cursor.getString(emailIndex));
        field.setPassword(cursor.getString(passwdIndex));
        field.setName(cursor.getString(nameIndex));
        field.setCountry(cursor.getString(countryIndex));
        field.setCity(cursor.getString(cityIndex));
        field.setAddress(cursor.getString(addressIndex));
        field.setPhone(cursor.getString(phoneIndex));
        field.setBirthday_year(cursor.getString(birthdayYearIndex));
        field.setBirthday_month(cursor.getString(birthdayMonthIndex));
        field.setBirthday_day(cursor.getString(birthdayDayIndex));

        return field;
    }

    // used to change the value of the cursor to data type, and return the data from application lock password
    private DataField cursorToData_2(Cursor cursor) {
        DataField field = new DataField();

        int idIndex_2 = cursor.getColumnIndex(DatabaseHelper.FIELD_ID_2);
        int appPWIndex = cursor.getColumnIndex(DatabaseHelper.FIELD_APPPW);

        field.setId_2(cursor.getLong(idIndex_2));
        field.setAppPW(cursor.getString(appPWIndex));

        return field;
    }

    // method to close cursor
    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
        }
    }
}
