package com.uoregoncis422.saferide;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Charlie on 4/6/16.
 */
public class HttpHelper {

    mSQLiteOpenHelper DB;

    public HttpHelper(mSQLiteOpenHelper database){
        Log.i("Upload", "uploader created");
        DB = database;
    }

    public void uploadJSON(JSONObject jObj){
        String URLString = buildURLString(jObj);
        request(URLString);;
    }

    private String buildURLString(JSONObject jObj){
        String URLString = "?";
        Iterator keys = jObj.keys();
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                URLString = URLString + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(jObj.getString(key), "UTF-8");
                if(keys.hasNext()){
                    URLString = URLString + "&";
                }
            }
        }catch(Exception e){
            Log.i("upload","problem with building URLString "+ e.toString());
        }
        return URLString;
    }

    private void request(String URLString){
        new OtherThread().execute(URLString);
        Log.i("upload","starting post thread");
    }

    private class OtherThread extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... URLString) {
                String data = "";
                try {
                    URL url = new URL("http://ix.cs.uoregon.edu:6666/_createFromApp"+URLString[0]);
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

    private class PostRequestThread extends AsyncTask<JSONObject, Void, String> {
        protected String doInBackground(JSONObject... jObj) {
            String data = "";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://ix.cs.uoregon.edu:6666/_createFromApp");

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();

                Iterator keys = jObj[0].keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    pairs.add(new BasicNameValuePair(key, jObj[0].getString(key)));
                }

                Log.i("upload","getting here");

                post.setEntity(new UrlEncodedFormEntity(pairs));

                HttpResponse response = client.execute(post);

                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();

                BufferedReader br = new BufferedReader((new InputStreamReader(
                        instream)));
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
