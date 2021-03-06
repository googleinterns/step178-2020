package com.google.sps.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.*;
import java.io.PrintWriter;
import java.io.*; 
import java.util.*; 


@WebServlet("/userlogin")
public class LoginServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    response.setContentType("text/html");
    
    // If user is not logged in, return the login button 
    if (!userService.isUserLoggedIn()) {
      String urlRedirectLogin = "/index.html";
      String loginUrl = userService.createLoginURL(urlRedirectLogin);

      response.getWriter().println("<h1>Welcome!</h1>");
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
      return;
    }

    String nickname = getUserNickname(userService.getCurrentUser().getUserId());
    
    // Check if user has a nickname, and if not, redirect to the nickname page
    if (nickname == null) {
      response.sendRedirect("/nickname");
      return;
    }

    String urlRedirectLogout = "/index.html";
    String logoutUrl = userService.createLogoutURL(urlRedirectLogout);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    User user = userService.getCurrentUser();
    String userId = user.getUserId();
    Query query = 
        new Query("UserInfo")
        .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    String topics = (String) entity.getProperty("topics");
    String topicsOutput = "";
    String[] topicsArray = topics.split(",");
    
    // For each topic the user already has, print add them to the list of things to print
    for (String topic : topicsArray) {
      String[] words_topic = topic.split(" ");
      String topicOutput = "<p>";
      for (String word : words_topic) {
	if (word.length() == 1) {
          topicOutput += word.substring(0,1).toUpperCase();
	  continue;
	} 
	if (word.length() == 0) {
          continue;
        }
        topicOutput+= word.substring(0,1).toUpperCase() + word.substring(1) + " ";
      }
      topicOutput += "</p>";
      topicsOutput += topicOutput;
    }
    
    // Print to the screen a welcome message, logout button, and the users topics 
    response.getWriter().println("<h1>Welcome " + nickname  + "!</h1>");
    response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    if (topicsOutput.equals("") || topicsOutput.equals("<p></p>")) {
      response.getWriter().println("<p> You have no topics yet! </p>");
    } else {
      response.getWriter().println("<p>Your current interests are: </p>");
      response.getWriter().println(topicsOutput);
    }
  }

  // Gets the users chosen nickname 
  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = 
        new Query("UserInfo")
	.setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    User user = userService.getCurrentUser();
    String userId = user.getUserId();
    Query query = 
	    new Query("UserInfo")
	    .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    datastore.delete(entity.getKey());
    String urlRedirectLogout = "/index.html";
    String logoutUrl = userService.createLogoutURL(urlRedirectLogout);
    response.sendRedirect(logoutUrl);
  }
}

