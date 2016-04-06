package com.uoregoncis422.saferide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class makeRequest extends ActionBarActivity {

    mSQLiteOpenHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        DB = new mSQLiteOpenHelper(this);
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
        int missingInput = -1;
        ArrayList<String> arrayList = new ArrayList<>();

        EditText editTexts = (EditText) findViewById(R.id.startAddy);
        EditText editTexte = (EditText) findViewById(R.id.endAddy);
        EditText editTextp = (EditText) findViewById(R.id.peopleNumber);

        arrayList.add(editTexts.getText().toString());
        arrayList.add(editTexte.getText().toString());
        arrayList.add(editTextp.getText().toString());

        for(int i = 0; i < arrayList.size(); i++){
            if(arrayList.get(i).length()<=0){
                validInput = false;
                missingInput = i;
            }
        }

        if(!validInput){
            //Toast incorrect input
            if(missingInput == 0){
                Toast.makeText(getApplicationContext(), "Invalid Start Address", Toast.LENGTH_LONG).show();
            }else if(missingInput == 1){
                Toast.makeText(getApplicationContext(), "Invalid End Address", Toast.LENGTH_LONG).show();
            }else if(missingInput == 2){
                Toast.makeText(getApplicationContext(), "Invalid Number of Passengers", Toast.LENGTH_LONG).show();
            }

        }else{
            createJSON(arrayList);
        }
    }

    public void editProfile(View view){
        startActivityForResult(new Intent(this, CreateAccount.class), 1);
    }

    public void sendJSON(JSONArray jArray){
        HttpHelper httpHelper = new HttpHelper(DB);
        httpHelper.uploadJSON(jArray.toString());
    }



}
