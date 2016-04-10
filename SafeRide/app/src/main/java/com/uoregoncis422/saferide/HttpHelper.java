package com.uoregoncis422.saferide;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Charlie on 4/6/16.
 */
public class HttpHelper {

    mSQLiteOpenHelper DB;

    public HttpHelper(mSQLiteOpenHelper database){
        Log.i("Upload", "uploader created");
        DB = database;
    }

    public void uploadJSON(String jString){
        request(jString);;
    }

    private void request(String jString){
        new OtherThread().execute(jString);
    }

    private class OtherThread extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... jString) {
                String data = "";
                try {
                    URL url = new URL("http://ix.cs.uoregon.edu:6666/_createFromApp");
                    Log.i("upload", "URL is " + url.toString());

                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    os.write(jString[0]); //writes JSON to body of php
                    os.close();

                    conn.connect();
                    int code = ((HttpURLConnection) conn).getResponseCode();
                    Log.i("upload", "code: " + code);
                    if(code!=200){
                        return "server error";
                    }

                    BufferedReader br = new BufferedReader((new InputStreamReader(
                            conn.getInputStream())));
                    StringBuffer b = new StringBuffer();
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        b.append(line);
                    }
                    data = b.toString(); //response from server to string
                }catch (Exception e){
                    Log.i("upload", "error here "+ e.toString()+ " "+ e.getCause() );
                }
                return data;
        }

        protected void onPostExecute(String response) {
            Log.i("upload",response);
        }
    }
}
