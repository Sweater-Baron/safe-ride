package com.uoregoncis422.saferide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

public class CreateAccount extends ActionBarActivity {

    public mSQLiteOpenHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        DB = new mSQLiteOpenHelper(this);
    }

    public void createUser(View view){
        EditText usernameBox = (EditText) findViewById(R.id.userName);
        EditText studentBox = (EditText) findViewById(R.id.studentid);
        EditText phoneBox = (EditText) findViewById(R.id.phonenumber);

        String usernameText = usernameBox.getText().toString();
        String studentText = studentBox.getText().toString();
        String phoneText = phoneBox.getText().toString();

        if(usernameText.length() > 0 && studentText.length() > 0 && phoneText.length() > 0){//make sure they're all filled with something
            //further validate? #TODO
            DB.createUser(usernameText,studentText,phoneText);
            startActivity(new Intent(this, makeRequest.class));
        }




    }

}
