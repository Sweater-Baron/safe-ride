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
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "safeRideDB";

//    //Table names
    private static final String TABLE_USER_INFO = "userinfo";

    private static final String COL_ID = "idx";
    private static final String COL_NAME = "name";
    private static final String COL_STUDENT_ID = "studentid";
    private static final String COL_PHONE_NUMBER = "phonenumber";

    private static final String CREATE_TABLE_USER_INFO = "CREATE TABLE "
            + TABLE_USER_INFO + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME
            + " VARCHAR(250)," + COL_STUDENT_ID + " VARCHAR(250)," + COL_PHONE_NUMBER + " VARCHAR(250)" + ")";

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

    public void createUser(String name, String studentID, String phoneNumber){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE_NUMBER, studentID);
        values.put(COL_STUDENT_ID, phoneNumber);

        long idx = db.insert(TABLE_USER_INFO, null, values);
    }

    public void updateUser(String name, String studentID, String phoneNumber){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE_NUMBER, studentID);
        values.put(COL_STUDENT_ID, phoneNumber);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_USER_INFO, null);

        if(c.moveToFirst()){
            db.update(TABLE_USER_INFO, values, COL_ID + "=" + 1, null);
        }

        //long idx = db.update(TABLE_USER_INFO, values, " WHERE 1=1", null);
    }

    public ArrayList<String> getUserInfo(){
        ArrayList<String> returnList = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_USER_INFO, null);
        while(c.moveToNext()){
            for(int i = 1; i < c.getColumnCount(); i++){
                returnList.add(c.getString(i));
                //Log.i("JSON",c.getString(i)+"");
            }
        }
        c.close();
        return returnList;
    }





//    public String createJSONFromSeq(String table, int seq, String macAddress){
//        JSONArray jsonArray = new JSONArray();
//        // Query "SELECT * FROM EVENTS WHERE ID > SEQ
//        Cursor c = db.rawQuery("SELECT * FROM "+table+" WHERE "+COL_ID+" > "+seq, null);
//        Log.i("JSONCreate", "number selected from " + table + " where id is > " + seq + " is " + c.getCount());
//        int count = 0;
//        //loop through every result from query, limited to 500
//        while(c.moveToNext()&&count<500){
//            JSONObject obj = new JSONObject();
//            try {
//                //loops through all the columns for each row
//                for(int i = 0; i<c.getColumnCount(); i++){
//                    if(c.getColumnName(i).equals(COL_ID)){
//                        int id = Integer.parseInt(c.getString(i))+getOffset(table);
//                        obj.put(c.getColumnName(i), ""+id);
//                    }else {
//                        obj.put(c.getColumnName(i), c.getString(i));
//                    }
//                }
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                Log.i("JSONCreate","exception while creating json");
//                e.printStackTrace();
//            }
//            jsonArray.put(obj);
//            count++;
//        }
//
//        c.close();
//
//        JSONObject returnTable = new JSONObject();
//        try {
//            returnTable.put("authentication",getAuthentication());
//            returnTable.put("mac",macAddress);
//            returnTable.put("table", table);
//            returnTable.put("data", jsonArray);
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//
//        String jsonStr = returnTable.toString();
//
//        return jsonStr;
//    }
//
//    private JSONObject getAuthentication(){
//        JSONObject authentication = new JSONObject();
//        String username = "memorywalksuser";
//        String password = "i@masuper$ecretPassword";
//        try {
//            authentication.put("username", username);
//            authentication.put("password", password);
//        }catch(JSONException e){
//
//        }
//
//        return authentication;
//    }

//    public void recordGPS(double latitude, double longitude, Date time, long event_id){
//        String stamp = dateTime(time);
//
//        ContentValues values = new ContentValues();
//        values.put(COL_LAT,latitude);
//        values.put(COL_LNG, longitude);
//        values.put(COL_EVENT_ID, event_id);
//        values.put(COL_STAMP, stamp);
//
//        // insert row
//        long gps_id = db.insert(TABLE_GPS, null, values);
//
//        Log.i("SQL", "added gps id: " + gps_id + ", latxlng: " + latitude + "x" + longitude + ", stamp: " + stamp + ", event id: " + event_id);
//    }

//    public void recordVisibility(int activityID, int visibilityType, Date time, long event_id){
//        String stamp = dateTime(time);
//
//        ContentValues values = new ContentValues();
//        values.put(COL_ACTIVITY_ID,activityID);
//        values.put(COL_VISIBILITY_TYPE_ID, visibilityType);
//        values.put(COL_EVENT_ID,event_id);
//        values.put(COL_STAMP, stamp);
//
//        // insert row
//        long vis_id = db.insert(TABLE_VISIBILITY_EVENT, null, values);
//
//        Log.i("SQL", "added vis id: " + vis_id + ", " + ActivityDict.get(activityID) + " is now " + VisibilityDict.get(visibilityType) + ", stamp: " + stamp + ", event id: " + event_id);
//    }

//    public void recordAudioEvent(String filePath, int type,int pathID, int markerID, int groupID, Date timestamp, int eventTypeSequence){
//        long event_id = recordEvent(pathID, markerID, groupID, timestamp, eventTypeSequence);
//        recordAudio(filePath, timestamp, event_id, type);
//    }

//    public void recordAudio(String filePath,Date timestamp, long event_id, int startStop){
//        String stamp = dateTime(timestamp);
//
//        ContentValues values = new ContentValues();
//        values.put(COL_FILE_PATH,filePath);
//        values.put(COL_AUDIO_TYPE, startStop);
//        values.put(COL_EVENT_ID, event_id);
//        values.put(COL_STAMP, stamp);
//
//        // insert row
//        long aud_id = db.insert(TABLE_AUDIO, null, values);
//
//        Log.i("SQL", "added aud id: " + aud_id + ", " + "Audio is " + AudioDict.get(startStop) + ", stamp: " + stamp + ", event id: " + event_id);
//
//    }

//    public void recordGPSEvent(int pathID, int markerID, int groupID, Date timestamp, int eventTypeSequence, double latitude, double longitude){
//        long event_id = recordEvent(pathID, markerID, groupID, timestamp, eventTypeSequence);
//        recordGPS(latitude, longitude, timestamp, event_id);
//    }
//
//    public void recordVisibilityEvent(int pathID, int markerID, int groupID, Date timestamp, int eventTypeSequence, int activityID, int visibilityType){
//        //visibility types 1=visible 2=invisible
//        long event_id = recordEvent(pathID,markerID,  groupID,  timestamp, eventTypeSequence);
//        recordVisibility(activityID, visibilityType, timestamp, event_id);
//    }

//    public String dateTime(Date date){
//        //DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String stamp = df.format(date);
//
//        return stamp;
//    }
//
//    public void createDictionaries(){
//        createEventDict();
//        createVDict();
//        createActivityDict();
//        createAudioDict();
//    }

//    public void createEventDict(){
//        dict.put(1,"APP_START_EVENT");
////        dict.put(2,"APP_END_EVENT");
//        dict.put(3,"APP_VISIBLE_EVENT");
//        dict.put(4,"APP_INVISIBLE_EVENT");
//        dict.put(5,"GPS_RECORD_EVENT");
//        dict.put(6,"MARKER_SHOWN_EVENT");
//        dict.put(7,"MARKER_HIDDEN_EVENT");
//        dict.put(8,"PROMPT_SHOWN_EVENT");
//        dict.put(9,"PROMPT_TIMER_EXPIRE_EVENT");
//        dict.put(10,"PROMPT_HIDDEN_EVENT");
//        dict.put(11,"LOGIN_SHOWN_EVENT");
//        dict.put(12,"LOGGED_IN_EVENT");
//        dict.put(13,"PATH_OPTIONS_SHOWN_EVENT");
//        dict.put(14,"PATH_OPTIONS_SELECT_EVENT");
//        dict.put(15,"REMINDERS_SHOWN_EVENT");
//        dict.put(16,"INCOMPLETE_REMINDERS_EVENT");
//        dict.put(17,"REMINDERS_HIDDEN_EVENT");
//        dict.put(18,"BEGIN_WALK_SHOWN_EVENT");
//        dict.put(19,"BEGIN_WALK_PRESSED_EVENT");
//        dict.put(20,"MAP_SHOWN_EVENT");
//        dict.put(21,"MAP_HIDDEN_EVENT");
//        dict.put(22,"COMPLETE_WALK_EVENT");
//        dict.put(23,"COMPLETED_WALK_RETURN_EVENT");
//        dict.put(24,"MAP_BACK_BUTTON__ATTEMPT_EVENT");
//        dict.put(25,"MARKER_BACK_BUTTON_EVENT");
//        dict.put(26,"RECORD_AUDIO_EVENT");
//        dict.put(27,"STOP_RECORDING_AUDIO_EVENT");
//
//    }

//    public void createVDict(){
//        VisibilityDict.put(1,"Visible");
//        VisibilityDict.put(2,"Invisible");
//    }
//
//    public void createAudioDict(){
//        AudioDict.put(1,"recording ");
//        AudioDict.put(2,"stopping the recording");
//    }
//
//    public void createActivityDict(){
//        ActivityDict.put(1,"Login");
//        ActivityDict.put(2,"Walk Selection");
//        ActivityDict.put(3,"Reminders");
//        ActivityDict.put(4,"Walk Start");
//        ActivityDict.put(5,"Map");
//        ActivityDict.put(6,"Marker");
//        ActivityDict.put(7,"Walk End");
//    }
//
//    public void displayMarkerTable(){
//        Cursor c = db.rawQuery("SELECT * FROM markers", null);
//        while (c.moveToNext()) {
//            Log.i("DisplayTable", "" + c.getString(0) + " | " + c.getString(1) + " | " + c.getString(2) + " | " + c.getString(3) +
//                    " | " + c.getString(4) + " | " + c.getString(5) + " | " + c.getString(6) + " | " + c.getString(7) + " | " + c.getString(8) + " | " + c.getString(9) + " | " + c.getString(10));
//        }
//        c.close();
//    }
//
//    public void displayPathTable(){
//        Cursor c = db.rawQuery("SELECT * FROM paths", null);
//        while (c.moveToNext()) {
//            Log.i("DisplayTable", "id: " + c.getString(0) + " | " + c.getString(1) + " | " + c.getString(2) + " | " + c.getString(3));
//        }
//        c.close();
//    }


}
