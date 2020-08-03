package com.google.sps.servlets;

import com.google.gson.Gson;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@WebServlet("/deleteTopic")
public class DeleteTopicServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService(); 
        User user = userService.getCurrentUser();
        String userId = user.getUserId(); 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query query = 
        new Query("UserInfo")
        .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, userId));
        PreparedQuery results = datastore.prepare(query); 
        Entity entity = results.asSingleEntity(); 
        String topics = (String) entity.getProperty("topics"); 
	//ArrayList<String> urls = (ArrayList<String>) entity.getProperty("urls");
        String [] listedTopics = topics.split(",");
        //System.out.println("Listed topics from datastore: " +Arrays.toString(listedTopics));
        
        String removedTopic = request.getParameter("topic");
        //System.out.println("Topic to be removed:" + removedTopic);
	String removedTopicCopy = removedTopic;

        String editedTopics = "";
        //System.out.println("Edited Topics:");
        for (int i = 0; i < listedTopics.length; i++){
          //System.out.println("Removed  topic: " + removedTopic);
          //System.out.println("Listedtopic[i]: " + listedTopics[i]);
          if (!listedTopics[i].equals(removedTopic)){
            editedTopics += listedTopics[i]; 
            if (i+1 < listedTopics.length){
              editedTopics+=",";
              //System.out.println(editedTopics);
            }
          } else {
            removedTopic="";
          }
        }
        while (editedTopics.length() > 1 && editedTopics.substring(editedTopics.length()-1).equals(",")){
          editedTopics = editedTopics.substring(0, editedTopics.length()-1);
        }
        if (editedTopics.length() == 1 && editedTopics.equals(",")){
          editedTopics = "";
        }
        entity.setProperty("topics", editedTopics);
	datastore.put(entity);
	response.sendRedirect("/search.html");
	//response.sendRedirect("/search.html");
	//urls = deleteUrls(editedTopics);
	//entity.setProperty("urls", urls);
        //datastore.put(entity);
        //response.sendRedirect("/search.html");
    }

    private ArrayList<String> deleteUrls(String topics) throws IOException {
      ArrayList<String> newUrls = new ArrayList<>();
      String[] topicArray = topics.split(",");
      for (String topic : topicArray) {
        String google = "https://www.google.com/search";
	int num = 5;
	String searchURL = google + "?q=" + topic + "&num=" + num;

	Document doc  = Jsoup.connect(searchURL).userAgent("Chrome").get();
        Elements results = doc.select("a[href]:has(span)").select("a[href]:not(:has(div))");

        for (Element result : results) {
	  String linkHref = result.attr("href");
	  String linkText = result.text();
	  if (linkHref.contains("https")) {
	    newUrls.add(linkHref.substring(7, linkHref.indexOf("&")));
	  } 
        }
      }
      System.out.println(newUrls);
      return newUrls;
    }

}
