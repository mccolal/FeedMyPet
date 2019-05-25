package com.FeedMyPet.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.FeedMyPet.R;
import com.FeedMyPet.helper.Constants;
import com.FeedMyPet.helper.DateManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class FeedPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_details);
        Intent intent = getIntent();
        String PetID = intent.getStringExtra("petID");

        //TODO Change this to actual logged in user
        String userID = "1";

        final Button btnUpdate = findViewById(R.id.btnUpdate);
        final Button btnSendTime = findViewById(R.id.btnFeedPet);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetUrlContentTask().execute(Constants.GET_URL_HOME_ID);
            }
        });

        btnSendTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new PostUrlContentTask().execute(Constants.POST_URL);
            }
        });

        UpdateLastFed();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        UpdateLastFed();
    }

    @Override
    protected  void onRestart(){
        super.onRestart();
        UpdateLastFed();
    }


    private void UpdateLastFed(){
        //new GetUrlContentTask().execute(Constants.GET_URL_HOME_ID);
    }



    private class PostUrlContentTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            StringBuilder sb = new StringBuilder();
            HttpURLConnection urlConnection = null;


            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                // urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();


                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("ID", Constants.HOME_ID);
                jsonParam.put("dt", "empty");
                DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                out.writeBytes(jsonParam.toString());
                out.flush();
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();

                    System.out.println("" + sb.toString());


                    result = sb.toString();
                }
            } catch (Exception e) {
                result = e.toString();
            }

            return result;
        }


        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            //final TextView txtView = findViewById(R.id.txtTimeLastFed);
            //txtView.setText(result);
            UpdateLastFed();
        }
    }


    private class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        Boolean error = null;
        protected String doInBackground(String... urls) {
            String result;

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setRequestProperty("accept", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.connect();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String content = "", line;
                while ((line = rd.readLine()) != null) {
                    content += line + "\n";
                }
                result = content;
            } catch (SocketTimeoutException e) {
                result = "Cannot Contact Server";
            } catch (Exception e) {
                result = "Error: " + e.toString();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            String dateTime = "";
            JSONObject results;
            Integer colour = Color.BLACK;
            if (error == null){
                try{
                    results = new JSONObject(result);
                    //dateTime = results.getString("timeLastFed");
                    //Date dateFed = new Date();
                    DateManager obj = new DateManager(results.getString("timeLastFed"));

                    //LocalDateTime localDateTime = LocalDateTime.parse(results.getString("timeLastFed"));
                    dateTime = obj.GetPresentableDate();


                    Integer h = 1;

                    if (h < Constants.HEALTHY_FEED_HOURS){
                        colour = Color.GREEN;
                    }else if (h < Constants.MINIMUM_HUNGER_START_HOURS){
                        colour = Color.YELLOW;
                    } else {
                        colour = Color.RED;
                    }







                } catch (Exception e) {
                    dateTime = e.toString();
                }
            } else {
                dateTime = result;
            }






            final TextView txtView = findViewById(R.id.txtTimeLastFed);
            txtView.setText(dateTime);
            txtView.setTextColor(colour);
        }




    }
}





