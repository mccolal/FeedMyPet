package com.FeedMyPet.helper;

import android.os.HandlerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class WebRequestManager {

    public String UserLogin(final String username, final String password){
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] value = new String[1];
        try {

            Thread uiThread = new HandlerThread("UIHandler"){
                @Override
                public void run(){
                    JSONObject jObj = new JSONObject();

                    try {
                        jObj.put("username",username);
                        jObj.put("password",password);
                        jObj.put("method",Constants.METHOD_USER_LOGIN);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    value[0] = PostHTTP(Constants.POST_USER_LOGIN,jObj);;
                    latch.countDown(); // Release await() in the test thread.
                }
            };
            uiThread.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return value[0];
        // Wait for countDown() in the UI thread. Or could uiThread.join();
        // value[0] holds 2 at this point.

        /*JSONObject jObj = new JSONObject();
        try {
            jObj.put("username",username);
            jObj.put("password",password);
            jObj.put("method",Constants.METHOD_USER_LOGIN);

        } catch (JSONException je){

        }

        return PostHTTP(Constants.POST_USER_LOGIN,jObj);
        */

    }

    public String CreatePet(final String name, final String type, final String assingedToID){
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] value = new String[1];
        try {

            Thread uiThread = new HandlerThread("UIHandler"){
                @Override
                public void run(){
                    JSONObject jObj = new JSONObject();

                    try {
                        jObj.put("petName",name);
                        jObj.put("petType",type);
                        //TODO get this value from user login
                        jObj.put("assignedToID", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    value[0] = PostHTTP(Constants.POST_PET_CREATE,jObj);
                    latch.countDown(); // Release await() in the test thread.
                }
            };
            uiThread.start();
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return value[0];
        // Wait for countDown() in the UI thread. Or could uiThread.join();
        // value[0] holds 2 at this point.

        /*JSONObject jObj = new JSONObject();
        try {
            jObj.put("username",username);
            jObj.put("password",password);
            jObj.put("method",Constants.METHOD_USER_LOGIN);

        } catch (JSONException je){

        }

        return PostHTTP(Constants.POST_USER_LOGIN,jObj);
        */

    }

    public String UserCreate(String username, String password,
                             String firstname, String lastname){
        JSONObject jObj = new JSONObject();

        try {
            jObj.put("username",username);
            jObj.put("password",password);
            jObj.put("firstname",firstname);
            jObj.put("lastname",lastname);
            jObj.put("method",Constants.METHOD_USER_CREATE);
        } catch (JSONException je){

        }
        return PostHTTP(Constants.POST_USER_CREATE,jObj);
    }

    private String PostHTTP(String pURL, JSONObject jObj ){
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        String result = "n";
        try {

            URL url = new URL(pURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.connect();


            DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
            if (jObj != null) out.writeBytes(jObj.toString());
            out.flush();
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println("" + sb.toString());


                result = sb.toString();
            }
        } catch (IOException io){
            result = "connection failed";
        }

        return  result;

    }

    private String GetHTTP(String pURL){
        String result;

        try {
            URL url = new URL(pURL);
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
        } catch (IOException io){
            result = "connection error";
        }
        return result;
    }





}