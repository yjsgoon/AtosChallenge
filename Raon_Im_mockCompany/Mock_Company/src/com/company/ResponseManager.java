package com.company;

import java.util.ArrayList;

/**
 * Created by Yoo on 2016-04-21.
 */
public class ResponseManager {
    private static ResponseManager responseManager;
    private ArrayList<Response> responses;

    public ResponseManager() {
        this.responses = new ArrayList<>();
    }

    public synchronized static ResponseManager getInstance() {
        if(responseManager == null) {
            responseManager = new ResponseManager();
        }
        return responseManager;
    }

    public void addResponse(Response response) {
        responses.add(response);
    }

    public ArrayList<Response> getResponses() {
        return responses;
    }

}
