package com.company;

/**
 * Created by Yoo on 2016-04-21.
 */
public class Response {
    private String userID;
    private String res;
    private String data;

    public Response(String userID, String res, String data) {
        this.userID = userID;
        this.res = res;
        this.data = data;
    }

    public String getUserID() {
        return userID;
    }

    public String getRes() {
        return res;
    }

    public String getData() {
        return data;
    }

}
