package com.FeedMyPet.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateManager  {

    Date dt = new Date();
    String year = "";
    String month;
    String day;
    String hour;
    String min = "";
    String sec = "";
    String msec = "";
    String combinedDate = "";


    public DateManager(String str){
        StringToDate(str);
    }

    public String GetPresentableDate(){
        String result = "Error";
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date tempD = sdf.parse(this.year + "-" + this.month + "-" + this.day);
            sdf.applyPattern("EEEE");
            String dayOfWeek = sdf.format(tempD);
            result = dayOfWeek + " - " + hour + ":" + min;
        } catch (ParseException parseException){
            result = "Parse Exception";
        }


        return result;
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public void StringToDate(String str){


        String pattern = "(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)T(\\d\\d):(\\d\\d):(\\d\\d)\\.(\\d+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);

        try {
            if (m.find()){
                this.year = m.group(1);
                this.month = m.group(2);
                this.day = m.group(3);
                this.hour = m.group(4);
                this.min = m.group(5);
                this.sec = m.group(6);
                this.msec = m.group(7);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
                //dt = new Date(df.format(dateString));
                dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
            }
        } catch (Exception e){
            combinedDate = "2000/01/01 00:00:00";
        }



    }
}
