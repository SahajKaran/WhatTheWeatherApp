package com.example.whatstheweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        resultTextView=findViewById(R.id.resultTextView);

    }
    public void getWeather(View view){

        DownloadTask task=new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+ "&appid=4c0bb73c988b97d261897507084aa270");
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection= null;
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);
                int data=reader.read();
                while(data!=1){
                    char current=(char) data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            }catch (Exception e ){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                Log.i("Weather Content",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);
                String message="";
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String main=jsonPart.getString(("main"));
                            String description=jsonPart.getString("Description");
                            if(!main.equals("") && !description.equals("")){
                                message+=main+ ":" +description;
                            }
                }
                if(!message.equals("")){
                    resultTextView.setText(message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}