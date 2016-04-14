package com.uoregoncis422.saferide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class makeRequest extends Activity {

    mSQLiteOpenHelper DB;
    private HttpHelper httpHelper;

    private TimePicker timePicker;
    private Calendar calendar;
    private TextView time;
    private String format = "";

    private EditText editTexts;
    private EditText editTexte;
    private EditText editTextp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        DB = new mSQLiteOpenHelper(this);
        httpHelper = new HttpHelper(DB);

        setTimeStuff();
        setEditTexts();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
    }

    private void setTimeStuff(){
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        time = (TextView) findViewById(R.id.timeText);
        calendar = Calendar.getInstance();
    }

    private void setEditTexts(){
        editTexts = (EditText) findViewById(R.id.startAddy);
        editTexte = (EditText) findViewById(R.id.endAddy);
        editTextp = (EditText) findViewById(R.id.peopleNumber);
    }

    public void setTime(View view) {
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
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
        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    public void createJSON(ArrayList<String> requestInfo){
        ArrayList<String> userInfo = DB.getUserInfo();
        String[] keys = new String[]{"name", "studentid", "phonenumber", "pickup", "dropoff", "numberOfPassengers", "time"};
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < userInfo.size(); i++){
            jsonArray.put(createJSONobj(keys[i],userInfo.get(i)));
        }

        Log.i("JSON", "" + requestInfo.size());
        for(int i = 0; i < requestInfo.size(); i++){
            jsonArray.put(createJSONobj(keys[i+userInfo.size()],requestInfo.get(i)));
        }

        sendJSON(jsonArray);
        printJSON(jsonArray);
    }

    public JSONObject createJSONobj(String key, String val){
        JSONObject jObj = new JSONObject();
        try {
            jObj.put(key,val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObj;
    }

    public void printJSON(JSONArray jsonArray){
        Log.i("JSON", jsonArray.toString());
    }

    public void checkInfo(View view){
        boolean validInput = true;
        boolean tooManyPassengers = true;
        int missingInput = -1;
        ArrayList<String> arrayList = new ArrayList<>();

        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        Toast.makeText(getApplicationContext(), "time input h: "+hour+" m: "+min, Toast.LENGTH_LONG).show();

        arrayList.add(editTexts.getText().toString());
        arrayList.add(editTexte.getText().toString());
        arrayList.add(editTextp.getText().toString());

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).length()<=0){
                validInput = false;
                missingInput = i;
            }
        }

        if(Integer.parseInt(arrayList.get(2))>3){
            validInput =false;
            tooManyPassengers = true;
        }

        //Validate both addresses, validate that time is within schedule. Addresses must both be real addresses and in bounds

        if(!validInput){
            //Toast incorrect input
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
            createJSON(arrayList);
        }
    }

    public void editProfile(View view){
        startActivityForResult(new Intent(this, CreateAccount.class), 1);
    }

    public void sendJSON(JSONArray jArray){
        httpHelper.uploadJSON(jArray.toString());
    }


    public void homeAddressFillStart(View view){
        editTexts.setText(DB.getUserAddress());
    }

    public void homeAddressFillEnd(View view){
        editTexte.setText(DB.getUserAddress());
    }


}
