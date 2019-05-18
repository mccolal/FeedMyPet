package com.FeedMyPet;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_details);

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
        new GetUrlContentTask().execute(Constants.GET_URL_HOME_ID);
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

            try{
                results = new JSONObject(result);
                //dateTime = results.getString("timeLastFed");
                //Date dateFed = new Date();
                DateManager obj = new DateManager(results.getString("timeLastFed"));

                //LocalDateTime localDateTime = LocalDateTime.parse(results.getString("timeLastFed"));
                dateTime = obj.GetPresentableDate();

                 /*simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

                try {
                    Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
                    Date date2 = simpleDateFormat.parse("13/10/2013 20:35:55");

                    obj.printDifference(date1, date2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
*/

                /*LocalDateTime fromDateTime = localDateTime;
                LocalDateTime toDateTime = LocalDateTime.now();

                LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );

                long years = tempDateTime.until( toDateTime, ChronoUnit.YEARS);
                tempDateTime = tempDateTime.plusYears( years );

                long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS);
                tempDateTime = tempDateTime.plusMonths( months );

                long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS);
                tempDateTime = tempDateTime.plusDays( days );


                long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS);
                tempDateTime = tempDateTime.plusHours( hours );

                long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES);
                tempDateTime = tempDateTime.plusMinutes( minutes );

                long seconds = tempDateTime.until( toDateTime, ChronoUnit.SECONDS);

                System.out.println( years + " years " +
                        months + " months " +
                        days + " days " +
                        hours + " hours " +
                        minutes + " minutes " +
                        seconds + " seconds.");

                Integer h = Math.toIntExact(hours);
                Integer d = Math.toIntExact(days*24);
                h=h+d;
                //dateTime = (h).toString();
                */
                Integer h = 1;

                if (h < Constants.HEALTHY_FEED_HOURS){
                    colour = Color.GREEN;
                }else if (h < Constants.MINIMUM_HUNGER_START_HOURS){
                    colour = Color.YELLOW;
                } else {
                    colour = Color.RED;
                }







            } catch (Exception e) {
                dateTime = "Json Error";
            }




            final TextView txtView = findViewById(R.id.txtTimeLastFed);
            txtView.setText(dateTime);
            txtView.setTextColor(colour);
        }




    }
}





