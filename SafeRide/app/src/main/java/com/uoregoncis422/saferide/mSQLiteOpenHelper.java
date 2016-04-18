package com.uoregoncis422.saferide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by quinch on 7/27/2015.
 */
public class mSQLiteOpenHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "safeRideDB";

    //Table names
    private static final String TABLE_USER_INFO = "userinfo";

    //Column names
    private static final String COL_ID = "idx";
    private static final String COL_NAME = "name";
    private static final String COL_STUDENT_ID = "studentid";
    private static final String COL_PHONE_NUMBER = "phonenumber";
    private static final String COL_HOME_ADDRESS = "homeaddress";

    //create table query
    private static final String CREATE_TABLE_USER_INFO = "CREATE TABLE "
            + TABLE_USER_INFO + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME
            + " VARCHAR(250)," + COL_STUDENT_ID + " VARCHAR(250)," + COL_PHONE_NUMBER + " VARCHAR(250)," + COL_HOME_ADDRESS  + " VARCHAR(250)" + ")";

    SQLiteDatabase db;

    public mSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_USER_INFO);

        Log.i("SQL", " tables created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
        onCreate(db);
    }

    public boolean isUser(){
        if(db.rawQuery("SELECT * FROM "+ TABLE_USER_INFO, null).getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public void createUser(String name, String studentID, String phoneNumber, String address){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE_NUMBER, studentID);
        values.put(COL_STUDENT_ID, phoneNumber);
        values.put(COL_HOME_ADDRESS, address);

        db.insert(TABLE_USER_INFO, null, values);
    }

    public void updateUser(String name, String studentID, String phoneNumber, String address){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE_NUMBER, studentID);
        values.put(COL_STUDENT_ID, phoneNumber);
        values.put(COL_HOME_ADDRESS, address);

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER_INFO, null);

        if(c.moveToFirst()){
            db.update(TABLE_USER_INFO, values, COL_ID + "=" + 1, null);
        }
    }

    public ArrayList<String> getUserInfo(){
        ArrayList<String> returnList = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_USER_INFO, null);
        while(c.moveToNext()){
            for(int i = 1; i < c.getColumnCount(); i++){
                returnList.add(c.getString(i));
            }
        }
        c.close();
        return returnList;
    }

    public String getUserAddress(){
        Cursor c = db.rawQuery("SELECT "+ COL_HOME_ADDRESS+" FROM "+ TABLE_USER_INFO, null);
        while(c.moveToNext()){
            return c.getString(0);
        }
        c.close();
        return "error";
    }


}
