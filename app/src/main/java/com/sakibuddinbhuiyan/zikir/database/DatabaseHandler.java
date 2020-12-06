package com.sakibuddinbhuiyan.zikir.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sakibuddinbhuiyan.zikir.models.Zikir;
import com.sakibuddinbhuiyan.zikir.utils.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Zikir";

    public static final String TABLE_ZIKIR = "ZikirRecord";
    public static final String ID = "Id";
    public static final String ZIKIR = "Zikir";
    public static final String ZIKIR_BANGLA = "zikirBangla";
    public static final String READ_TODAY = "ReadToday";
    public static final String READ_TOTAL = "ReadTotal";
    public static final String FAVOURITE = "favourite";

    public static final String TABLE_DATE = "DateRecord";
    public static final String DATE = "Date";

    public static final String TABLE_SETTINGS = "Settings";
    public static final String VIBRATE = "Vibrate";
    public static final String LANGUAGE = "Language";
    public static final String MODE = "Mode";


    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createZikirTable(sqLiteDatabase);
        createSettingsTable(sqLiteDatabase);
        initilizeSettings(sqLiteDatabase);
        createDateTable(sqLiteDatabase);
        initilizeDate(sqLiteDatabase);
        for (Map.Entry<String, String> entry : Data.zikrData.entrySet()) {
            fillZikirTable(sqLiteDatabase, entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ZIKIR + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE + ";");

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    void createZikirTable(SQLiteDatabase db) {
        String CREATE_Zikir_TABLE = "CREATE TABLE " + TABLE_ZIKIR + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ZIKIR + " TEXT," +
                ZIKIR_BANGLA + " TEXT," +
                READ_TODAY + " INTEGER," +
                READ_TOTAL + " INTEGER," +
                FAVOURITE + " INTEGER" +
                ")";
        Log.d("TableCreation", "zikir table sql = " + CREATE_Zikir_TABLE);
        db.execSQL(CREATE_Zikir_TABLE);
    }

    void createDateTable(SQLiteDatabase db) {
        String CREATE_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + "(" +
                DATE + " TEXT" +
                ")";
        Log.d("TableCreation", "date table sql = " + CREATE_DATE_TABLE);
        db.execSQL(CREATE_DATE_TABLE);
    }

    void createSettingsTable(SQLiteDatabase db) {
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "(" +
                VIBRATE + " INTEGER," +
                LANGUAGE + " TEXT" +
                ")";
        Log.d("TableCreation", "settings table sql = " + CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
    }

    //initializing table
    void fillZikirTable(SQLiteDatabase sqLiteDatabase, String zikir, String zikirBangla) {
        ContentValues values = new ContentValues();
        values.put(ZIKIR, zikir);
        values.put(ZIKIR_BANGLA, zikirBangla);
        values.put(READ_TODAY, 0);
        values.put(READ_TOTAL, 0);
        values.put(FAVOURITE, 0);

        Log.d("TableInsert", "zikir data: " + values.toString());
        // Inserting Row
        sqLiteDatabase.insert(TABLE_ZIKIR, null, values);
        //2nd argument is String containing nullColumnHack
    }

    //adding first date when app opened
    void initilizeDate(SQLiteDatabase sqLiteDatabase) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(DATE, String.valueOf(formatter.format(date)));

        Log.d("TableInsert", "date data: " + values.toString());
        // Inserting Row
        sqLiteDatabase.insert(TABLE_DATE, null, values);
        //2nd argument is String containing nullColumnHack
    }

    void initilizeSettings(SQLiteDatabase sqLiteDatabase) {
        ContentValues values = new ContentValues();
        values.put(VIBRATE, 0);
        values.put(LANGUAGE, "English");

        Log.d("TableInsert", "settings data: " + values.toString());
        // Inserting Row
        sqLiteDatabase.insert(TABLE_SETTINGS, null, values);
        //2nd argument is String containing nullColumnHack
    }

    public int getVibrateStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + VIBRATE + " From " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        int vibrate = cursor.getInt(0);

        cursor.close();
        // return vibrate
        return vibrate;
    }

    public void updateVibrateStatusInDatabase(int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VIBRATE, status);

        Log.d("TableUpdate", "update vibrate status: " + values.toString());
        // Updating Row
        db.update(TABLE_SETTINGS, values, null, null);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void updateLanguageStatusInDatabase(String language) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LANGUAGE, language);

        Log.d("TableUpdate", "update language status: " + values.toString());
        // Updating Row
        db.update(TABLE_SETTINGS, values, null, null);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public String getLanguageStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + LANGUAGE + " From " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        String language = cursor.getString(0);

        cursor.close();
        // return language
        return language;
    }

    public String getDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * From " + TABLE_DATE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        String date = cursor.getString(0);

        cursor.close();
        // return date
        return date;
    }

    public Integer getIdOfZikir(String inputZikir) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + ID + " From " + TABLE_ZIKIR + " Where " + ZIKIR + " = \'" + inputZikir + "\'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        Integer id = cursor.getInt(0);

        cursor.close();
        // return id
        return id;
    }

    public void updateDate(String today) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, today);

        Log.d("TableUpdate", "update date data: " + values.toString());
        // Updating Row
        db.update(TABLE_DATE, values, null, null);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void refreshTodaysColumn() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(READ_TODAY, 0);

        Log.d("TableUpdate", "update zikir data: " + values.toString());
        // Updating Row
        db.update(TABLE_ZIKIR, values, null, null);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public LinkedList<Zikir> getAllZikirData() {
        LinkedList<Zikir> zikirList = new LinkedList<Zikir>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ZIKIR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Zikir zikirObj = new Zikir(
                        //skipping column index(to keep query short) 0 as its answerSetId
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5))
                );
                // Adding answer set to list
                zikirList.add(zikirObj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return zikir list
        return zikirList;
    }

    public LinkedList<Zikir> getAllfavouritedZikirData() {
        LinkedList<Zikir> favouritedZikirList = new LinkedList<Zikir>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ZIKIR + " Where " + FAVOURITE + " = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Zikir zikirObj = new Zikir(
                        //skipping column index(to keep query short) 0 as its answerSetId
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5))
                );
                // Adding answer set to list
                favouritedZikirList.add(zikirObj);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return zikir list
        return favouritedZikirList;
    }

    public void incrementToday(int today, int inputId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Update " + TABLE_ZIKIR + " Set " + READ_TODAY + " = " + today + " Where " + ID + " = " + inputId);
        db.close();
    }

    public void incrementTotal(int now, int inputId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Update " + TABLE_ZIKIR + " Set " + READ_TOTAL + " = " + READ_TOTAL + " + " + now + " Where " + ID + " = " + inputId);
        db.close();
    }

    public void updateFavourite(int inputId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Update " + TABLE_ZIKIR + " Set " + FAVOURITE + " = " + status + " Where " + ID + " = " + inputId;
        db.execSQL(query);
        Log.d(DatabaseHandler.class.getName(), "update favourite query: " + query);
        db.close();
    }

}
