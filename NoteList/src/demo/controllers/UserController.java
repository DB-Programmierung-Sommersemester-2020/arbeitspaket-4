package demo.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import demo.data.entities.User;
import demo.data.facade.UserNotesFacade;


public class UserController
{
   private static UserController instance = new UserController();
   private UserNotesFacade facade = UserNotesFacade.getInstance();
   
   public static UserController getInstance()
   {
      return UserController.instance;
   }

   private Map<String, User> users = new HashMap<>();
   

   private UserController()
   {
      super();
      fillUsersMap();
   }
   
   private void fillUsersMap() {
	   facade.getAllUsers().forEach(user->users.put(user.getName(), user));
   }

   public Optional<User> lookupUser(String username)
   {
      return this.users.values().stream().filter(user -> user.getName().equals(username)).findFirst();
   }

   public User register(String username, String password, String email)
   {
      if (this.lookupUser(username).isPresent())
      {
         RuntimeException exce = new RuntimeException("User " + username + " exists");
         throw exce;
      }
      else
      {
         User user = new User(username, password, email);
         this.users.put(username, user);
         facade.createUser(user);
         return user;
      }
   }
}
