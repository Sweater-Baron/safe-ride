package com.uoregoncis422.saferide;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    public mSQLiteOpenHelper DB;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new mSQLiteOpenHelper(this);

        final Intent intent = new Intent(this, makeRequest.class);


        mHandler.postDelayed(new Runnable() {
            public void run() {
                if(DB.isUser()){
                    startActivity(intent);
                }else{
                    goToCreateAccount();
                }
            }
        }, 5000);


    }

    private void goToCreateAccount(){
        startActivity(new Intent(this, CreateAccount.class));
    }
}
