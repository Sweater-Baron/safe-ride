package com.uoregoncis422.saferide;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
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
                    URL url = new URL("http://ix.cs.uoregon.edu:6666/_createFromApp?="+jString[0]);
                    Log.i("upload", "URL is " + url.toString());

                    URLConnection conn = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.connect();
                    int code = ((HttpURLConnection) conn).getResponseCode();
                    Log.i("upload", "code: " + code);

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
