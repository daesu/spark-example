package app.util;

import com.google.gson.*;
import spark.*;

/**
* Helper methods to handle Requests
*/
public class RequestUtils {

    public static boolean IsUserPostValid(Request request){
        try{
            JsonObject parsedBody = new JsonParser().parse(request.body()).getAsJsonObject();
            if (parsedBody.get("id") == null || parsedBody.get("name") == null){
                return false;
            }
        }catch(Exception e){
            return false;
        }

        return true;
    }

}