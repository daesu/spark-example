
package app.user;

import app.Config;
import spark.*;
import app.util.*;

import com.google.gson.*;

/**
* Handles logic for requests to User endpoints
*/
public class UserController {
    
    /**
     * Accepts User data in JSON form {"id": <int>, "name": <string>}
     * @return JSON body to Client.
     */
    public static Route handleUserPost = (Request request, Response res) -> {


        // Verify post params
        if (!RequestUtils.IsUserPostValid(request)){
            return ResponseUtils.InvalidPostRequest(res, "Must include `id` and `name`");  
        }

        PojoUser pjUser;
        try{
            Gson gson = new GsonBuilder().create();
            pjUser = gson.fromJson(request.body(), PojoUser.class);
        }catch(JsonSyntaxException e){
            return ResponseUtils.InvalidPostRequest(res, String.format("JsonSyntaxException: %s", e));  
        }catch(JsonParseException e){
            return ResponseUtils.InvalidPostRequest(res, String.format("JsonParseException: %s", e));  
        }

        // Add user to List & save on disk
        boolean bool_added = UserDao.AddToUserList(Config.config.FILEPATH, pjUser.getName(), pjUser.getId());

        if (!bool_added){
            return ResponseUtils.InternalError(res, "Could not save user");  
        }
        
        return ResponseUtils.SuccessResponse(res, String.format("{\"id\":%s,\"name\":\"%s\"}", pjUser.getId(), pjUser.getName()));

    };

    /**
     * Gets list of Users saved to disk with google proto buffer if it exists
     * @return JSON Array of Users
     */
    public static Route HandleUserGetAll = (Request request, Response res) -> {
         PojoUser[] userListArr = UserDao.GetUserList(Config.config.FILEPATH);

         Gson gson = new GsonBuilder().create();
         String jsonInString = gson.toJson(userListArr);

         return ResponseUtils.SuccessResponse(res, jsonInString);

    };
}

