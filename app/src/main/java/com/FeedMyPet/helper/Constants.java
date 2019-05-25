package com.FeedMyPet.helper;

public class Constants {
    //TODO Put these into a resource file
    public static final Integer HOME_ID = 5;


    public static final String IP = "60.226.1.142";
    //public static final String PORT = "61899";
    public static final String PORT = "60792";

    public static final String POST_URL = "http://" + IP + ":" + PORT + "/api/homelocal";
    public static final String GET_URL_HOME_ID = "http://" + IP + ":" + PORT + "/api/homelocal/" + HOME_ID.toString();
    public static final String POST_USER_LOGIN = "http://" + IP + ":" + PORT + "/api/user";
    public static final String POST_PET_CREATE = "http://" + IP + ":" + PORT + "/api/pet";
    public static final String POST_USER_CREATE = "http://" + IP + ":" + PORT + "/api/user";

    public static final String METHOD_USER_CREATE = "create";
    public static final String METHOD_USER_LOGIN = "login";
    public static final String USER_SUCCESS_LOGIN = "User successfully logged in";


    public static final Integer HEALTHY_FEED_HOURS = 6;
    public static final Integer MINIMUM_HUNGER_START_HOURS = 8;
    public static final Integer MINIMUM_STARVING_START_HOURS=11;

    public static final Boolean CONNECTION_ERROR = true;
}
