package app.user;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

import app.user.UserProtos.User;
import app.user.UserProtos.UserList;

/**
* Handles User CRUD on disk with google proto buffer access.
*/
public class UserDao {

  /**
   * Creates a new User
   * @param name User's name
   * @param id  Users id
   * @return user obj
   */
  static User AddUser(String name, int id) throws IOException {
    User.Builder user = User.newBuilder();

    user.setId(id);
    user.setName(name);
                                
    return user.build();
  }

  /**
   * Gets all users saved to specified file on disk
   * @param filename filename of file on disk (Path is relative)
   * @return Array of PJUser POJO objects
   */
  static PojoUser[] GetUserList(String filename) throws IOException{

      // Read the existing user list.
      File f = new File(filename);
      if(!f.exists() && !f.isDirectory()) { 
        return new PojoUser[0];
      }

      // Read the existing user list from disk.
      UserList userList = UserList.parseFrom(new FileInputStream(filename));
      
      PojoUser[] userListArr = new PojoUser[userList.getUserCount()];
      for (int i =0; i < userList.getUserCount(); i++){
        User user = userList.getUser(i);
        PojoUser pjUser = new PojoUser(user.getName(), user.getId());

        userListArr[i] = pjUser;
      }

      return userListArr;
  }

  /**
   * Creates or Updates the UserList which is saved to disk.
   * @param filename filename of file on disk (Path is relative)
   * @param name User's name
   * @param id User's id
   * @return boolean for success or failure
   */
  public static boolean AddToUserList(String filename, String name, int id) throws IOException {

    UserList.Builder userList = UserList.newBuilder();

    // Read the existing user list.
    File f = new File(filename);
    if(f.exists() && !f.isDirectory()) { 
        FileInputStream fis = null;
        fis = new FileInputStream(filename);
        userList.mergeFrom(fis);
        fis.close();
    } else {
        System.out.println(filename + "UserList not found. Will create new");
    }

    try{
        userList.addUser(UserDao.AddUser(name, id));
    }catch (IOException e){
        return false;
    }

    //write to disk
    try {
        // Write the new userlist back to disk.
        FileOutputStream output = new FileOutputStream(filename);                      
        userList.build().writeTo(output);     
        output.close();   
    } catch (Exception e) {  
        System.out.println(e);
        return false;
    }

    return true;

  }

}