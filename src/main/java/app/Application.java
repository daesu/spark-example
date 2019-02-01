package app;

import static spark.Spark.*;

import app.util.*;
import app.user.*;

/**
* implements a simple Spark http server with two endpoints for
* Saving Users to a User List and Listing all Saved Users.
*/
public class Application { 
    public static UserDao userDao;  
     
    public static void main(String[] args) {

        // Initialise User data handler
        userDao = new UserDao();

        // Configure Spark port etc
        port(Config.PORT);

        // Config response for all endpoints)
        before("*", ResponseUtils.ConfigResponse);
        
        // handles POST to /users endpoint. 
        post(Config.api.USERS, UserController.handleUserPost);

        // handles GET to /users endpoint
        get(Config.api.USERS, UserController.HandleUserGetAll);
    }
}