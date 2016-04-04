package com.uoregoncis422.saferide;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity {

    public mSQLiteOpenHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new mSQLiteOpenHelper(this);

        if(DB.isUser()){
            startActivity(new Intent(this, makeRequest.class));
        }else{
            goToCreateAccount();
        }
    }

    private void goToCreateAccount(){
        startActivity(new Intent(this, CreateAccount.class));
    }
}
