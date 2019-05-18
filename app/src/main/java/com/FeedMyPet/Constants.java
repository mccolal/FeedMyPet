package com.FeedMyPet;

public class Constants {
    public static final String IP = "60.226.1.142";
    public static final String PORT = "61899";
    public static final Integer HOME_ID = 5;
    public static final String POST_URL = "http://" + IP + ":" + PORT + "/api/homelocal";
    public static final String GET_URL_HOME_ID = "http://" + IP + ":" + PORT + "/api/homelocal/" + HOME_ID.toString();
    public static final Integer HEALTHY_FEED_HOURS = 6;
    public static final Integer MINIMUM_HUNGER_START_HOURS = 8;
    public static final Integer MINIMUM_STARVING_START_HOURS=11;
}
