package com.sakibuddinbhuiyan.zikir.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sakibuddinbhuiyan.zikir.PublicVariables;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Zikir";

    public static final String TABLE_ZIKIR = "ZikirRecord";
    public static final String ID = "Id";
    public static final String ZIKIR = "Zikir";
    public static final String READ_TODAY = "ReadToday";
    public static final String READ_TOTAL = "ReadTotal";
    public static final String FAVOURITE = "favourite";

    public static final String TABLE_DATE = "DateRecord";
    public static final String DATE = "Date";

    public static LinkedList<String> zikrData = new LinkedList<>(Arrays.asList(
            "Quran: Ultimately, the best dhikr is reading the Quran.\n" +
                    "Reward:\n" +
                    "\n" +
                    "You will be rewarded 10 rewards for each letter read.",
            "SubhanAllah or SubhanAllah wa bihamdihi\n" +
                    "(“I praise Allah (or All praise if to Allah) above all attributes that do not suit His Majesty.”)\n" +
                    "\n" +
                    "Reward: \n" +
                    "\n" +
                    "A tree will be planted for you in Paradise. ",
            "whoever says \"SubhanAllah or SubhanAllah wa bihamdihi\" 100 times a day, his/her sins will be forgiven even if they were as much as the foam of the sea",
            "Alhamdulillah\n" +
                    "(“All praise is for Allah”)\n" +
                    "\n" +
                    "Reward:\n" +
                    "\n" +
                    "Your scales will be tipped on the Day of Judgment, full of rewards!\n" +
                    "\n",
            "La hawla wa la quwwata illa billah \n" +
                    "(“There is no power or might except (by) Allah.”)\n" +
                    "\n" +
                    "Reward:\n" +
                    "\n" +
                    "You will enter through a special door in Paradise for those who oft use this remembrance.",
            "SubhanAllah (x33), Alhamdulillah (x33), Allahu akbar (x34)\n" +
                    "Can be recited after salat and before you go to bed/sleep. (“I praise Allah (or All praise if to Allah) above all attributes that do not suit His Majesty.  All praise is to Allah.  Allah is Great.”)\n" +
                    "\n" +
                    "Reward:\n" +
                    "\n" +
                    "We know that this dhikr is said after each salah, but when Fatima raḍyAllāhu anha (may Allāh be pleased with her) the daughter of the Prophet came to her father requesting a servant to help with the household, the Messenger of Allah ṣallallāhu alayhi wa sallam (peace and blessings of Allāh be upon him) told her to repeat the dhikr before her sleep and the results would be better than having a servant."
    ));


    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createZikirTable(sqLiteDatabase);
        createDateTable(sqLiteDatabase);
        initilizeDate(sqLiteDatabase);
        for (String zikir : zikrData) {
            fillZikirTable(sqLiteDatabase, zikir);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ZIKIR + ";");

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    void createZikirTable(SQLiteDatabase db) {
        String CREATE_Zikir_TABLE = "CREATE TABLE " + TABLE_ZIKIR + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                ZIKIR + " TEXT," +
                READ_TODAY + " INTEGER," +
                READ_TOTAL + " INTEGER," +
                FAVOURITE + " INTEGER" +
                ")";
        Log.d("TableCreation", "zikir table sql = " + CREATE_Zikir_TABLE);
        db.execSQL(CREATE_Zikir_TABLE);
    }

    void createDateTable(SQLiteDatabase db) {
        String CREATE_Date_TABLE = "CREATE TABLE " + TABLE_DATE + "(" +
                DATE + " TEXT" +
                ")";
        Log.d("TableCreation", "date table sql = " + CREATE_Date_TABLE);
        db.execSQL(CREATE_Date_TABLE);
    }

    //initializing table
    void fillZikirTable(SQLiteDatabase sqLiteDatabase, String zikir) {
        ContentValues values = new ContentValues();
        values.put(ZIKIR, zikir);
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
        String query = "Select "+ ID +" From " + TABLE_ZIKIR +" Where "+ZIKIR + " = \'"+ inputZikir + "\'";
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
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4))
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
        String selectQuery = "SELECT * FROM " + TABLE_ZIKIR + " Where "+ FAVOURITE + " = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Zikir zikirObj = new Zikir(
                        //skipping column index(to keep query short) 0 as its answerSetId
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4))
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
        Log.d(DatabaseHandler.class.getName(),"update favourite query: "+query);
        db.close();
    }
}
