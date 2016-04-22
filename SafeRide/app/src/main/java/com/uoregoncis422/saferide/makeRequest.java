package com.uoregoncis422.saferide;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class makeRequest extends Activity {

    mSQLiteOpenHelper DB;
    private HttpHelper httpHelper;

    private TimePicker timePicker;
    private Calendar calendar;
    private String format = "";

    private EditText editTexts;
    private EditText editTexte;
    private EditText editTextp;
    private Resources system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        DB = new mSQLiteOpenHelper(this);
        httpHelper = new HttpHelper(DB);

        setTimeStuff();
        setEditTexts();
        set_timepicker_text_colour();
    }

    private void setTimeStuff(){
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();
    }

    private void setEditTexts(){
        editTexts = (EditText) findViewById(R.id.startAddy);
        editTexte = (EditText) findViewById(R.id.endAddy);
        editTextp = (EditText) findViewById(R.id.peopleNumber);
    }

    public void createJSON(ArrayList<String> requestInfo){
        ArrayList<String> userInfo = DB.getUserInfo();
        String[] keys = new String[]{"name", "phonenumber", "studentid", "pickup", "dropoff", "numberOfPassengers","time"};
        JSONObject jObj = new JSONObject();
        try {
            for (int i = 0; i < userInfo.size()-1; i++) {
                jObj.put(keys[i], userInfo.get(i));
            }

            Log.i("JSON", "" + requestInfo.size());
            for (int i = 0; i < requestInfo.size(); i++) {
                jObj.put(keys[i + userInfo.size()-1], requestInfo.get(i));
            }
        }catch(Exception e){

        }

        sendJSON(jObj);
        printJSON(jObj);
    }

    public void printJSON(JSONObject jsonArray){
        Log.i("JSON", jsonArray.toString());
    }

    private ArrayList<String> generateInputList(int hour, int min){
        ArrayList<String> returnList = new ArrayList<>();
        returnList.add(editTexts.getText().toString());
        returnList.add(editTexte.getText().toString());
        returnList.add(editTextp.getText().toString());
        returnList.add(getTimeString(hour, min));
        return returnList;
    }

    public void checkInfo(View view){
        boolean validInput = true;
        boolean tooManyPassengers = true;
        int missingInput = -1;

        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        ArrayList<String> inputList = generateInputList(hour,min);

        for(int i = 0; i < inputList.size(); i++){
            if(inputList.get(i).length()<=0){
                validInput = false;
                missingInput = i;
            }
        }

        //check passenger size
        if(inputList.get(2).length() > 0){
            if (Integer.parseInt(inputList.get(2)) > 3) {
                validInput = false;
                tooManyPassengers = true;
            }
        }

        if(!validInput){
            //Toast incorrect input
            inputList.clear();
            if(missingInput == 0){
                Toast.makeText(getApplicationContext(), "Invalid Start Address", Toast.LENGTH_LONG).show();
            }else if(missingInput == 1){
                Toast.makeText(getApplicationContext(), "Invalid End Address", Toast.LENGTH_LONG).show();
            }else if(missingInput == 2){
                Toast.makeText(getApplicationContext(), "Invalid Number of Passengers", Toast.LENGTH_LONG).show();
            }
            if(tooManyPassengers){
                Toast.makeText(getApplicationContext(), "SafeRide can only schedule rides for 3 passengers or less", Toast.LENGTH_LONG).show();
            }

        }else{
            createJSON(inputList);
        }
    }

    private String getTimeString(int hour, int min){
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        return hour+":"+min+ " " + format;
    }

    public void editProfile(View view){
        startActivityForResult(new Intent(this, CreateAccount.class), 1);
    }

    public void sendJSON(JSONObject jArray){
        httpHelper.uploadJSON(jArray);
        startActivity(new Intent(this, confirmationActivity.class));
    }


    public void homeAddressFillStart(View view){
        editTexts.setText(DB.getUserAddress());
    }

    public void homeAddressFillEnd(View view){
        editTexte.setText(DB.getUserAddress());
    }

    //below code is from stack overflow, all it does is  change the text of the timePicker
    private void set_timepicker_text_colour(){
        system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        int ampm_numberpicker_id = system.getIdentifier("amPm", "id", "android");

        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        NumberPicker ampm_numberpicker = (NumberPicker) timePicker.findViewById(ampm_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
        set_numberpicker_text_colour(ampm_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker){
        final int count = number_picker.getChildCount();
        final int color = Color.WHITE;

        for(int i = 0; i < count; i++){
            View child = number_picker.getChildAt(i);

            try{
                Field wheelpaint_field = number_picker.getClass().getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint)wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText)child).setTextColor(color);
                number_picker.invalidate();
            }
            catch(NoSuchFieldException e){
                Log.w("setNumberPickerTextColo", e);
            }
            catch(IllegalAccessException e){
                Log.w("setNumberPickerTextColo", e);
            }
            catch(IllegalArgumentException e){
                Log.w("setNumberPickerTextColo", e);
            }
        }
    }


}
