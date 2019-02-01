package app.util;

import spark.*;

/**
* Helper methods to handle Responses
*/
public class ResponseUtils {

    public static String SuccessResponse(Response res, String message){
        res.status(200);

        return message; 
    }

    public static String InvalidPostRequest(Response res, String message){
        res.status(400);

        return String.format("{\"message\":\" %s`\"}", message); 
    }

    public static String InternalError(Response res, String message){
        res.status(500);

        return String.format("{\"message\":\"Internal Server Error. %s`\"}", message); 
    }

    // set application/json for all responses
    public static Filter ConfigResponse = (Request request, Response res) -> {
        res.type("application/json");
    };

}