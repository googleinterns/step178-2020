package com.google.sps.servlets;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class EmailHandler{

  private final String API_KEY = "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";
  private final String PUB_KEY = "2065063d2f679c68571c386cf8d13767";
  private final String PRIV_KEY =  "2921119afe3fec7e64e36d4677fc4a75";

  MailjetClient client = client = new MailjetClient(PUB_KEY, PRIV_KEY, new ClientOptions("v3.1"));
  MailjetRequest request;
  MailjetResponse response;
  public EmailHandler(){

  }

  public void sendWelcomeMail(String userEmail) throws MailjetException, MailjetSocketTimeoutException{

    String htmlOutput = "<h3>Welcome to <a href='https://www.learnbase-step-2020.appspot.com/'>"+
      "LearnBase</a>!</h3><br />" + "We're very happy to have you." + 
      "<br> If you haven't already done so, please choose a topic under the Search page and select a time " +
      "to recieve daily emails! ";
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
      .put(new JSONObject()
      .put(Emailv31.Message.FROM, new JSONObject()
      .put("Email", "learnbase2020@gmail.com")
      .put("Name", "Learnbase"))
      .put(Emailv31.Message.TO, new JSONArray()
      .put(new JSONObject()
      .put("Email", userEmail)
      .put("Name", "Federick")))
      .put(Emailv31.Message.SUBJECT, "Welcome to LearnBase!")
      .put(Emailv31.Message.TEXTPART, "")
      .put(Emailv31.Message.HTMLPART, htmlOutput)
      .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
    try{
      response = client.post(request);
      System.out.println(response.getStatus());
      System.out.println(response.getData());
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void sendPlainAndHTML(String email, String plaintext, String html){
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
      .put(new JSONObject()
      .put(Emailv31.Message.FROM, new JSONObject()
      .put("Email", "learnbase2020@gmail.com")
      .put("Name", "Learnbase"))
      .put(Emailv31.Message.TO, new JSONArray()
      .put(new JSONObject()
      .put("Email", email)
      .put("Name", "User")))
      .put(Emailv31.Message.SUBJECT, "Welcome to LearnBase!")
      .put(Emailv31.Message.TEXTPART, plaintext)
      .put(Emailv31.Message.HTMLPART, html)
      .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
    try{
      response = client.post(request);
      System.out.println(response.getStatus());
      System.out.println(response.getData());
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void sendMessage(String userEmail, String message){
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
      .put(new JSONObject()
      .put(Emailv31.Message.FROM, new JSONObject()
      .put("Email", "learnbase2020@gmail.com")
      .put("Name", "Learnbase"))
      .put(Emailv31.Message.TO, new JSONArray()
      .put(new JSONObject()
      .put("Email", userEmail)
      .put("Name", "Federick")))
      .put(Emailv31.Message.SUBJECT, "Welcome to LearnBase!")
      .put(Emailv31.Message.TEXTPART, message)
      .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
    try{
      response = client.post(request);
      System.out.println(response.getStatus());
      System.out.println(response.getData());
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  public void sendMail(String userEmail, String username, String[] topics, String[] info){
    String htmlOutput = "";
    for (int i = 0; i < topics.length; i++){
      htmlOutput += "<h3>" + topics[i] + "</h3>";
      htmlOutput += "<br>" + info + "<br>";
    }

    MailjetClient client = client = new MailjetClient(PUB_KEY, PRIV_KEY, new ClientOptions("v3.1"));
    MailjetRequest request;
    MailjetResponse response;
    request = new MailjetRequest(Emailv31.resource)
      .property(Emailv31.MESSAGES, new JSONArray()
      .put(new JSONObject()
      .put(Emailv31.Message.FROM, new JSONObject()
      .put("Email", "learnbase2020@gmail.com")
      .put("Name", "Learnbase"))
      .put(Emailv31.Message.TO, new JSONArray()
      .put(new JSONObject()
      .put("Email", userEmail)
      .put("Name", username)))
      .put(Emailv31.Message.SUBJECT, "Your Daily dose of info!")
      .put(Emailv31.Message.TEXTPART, "")
      .put(Emailv31.Message.HTMLPART, htmlOutput)
      .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
    try{ 
      response = client.post(request);    
      System.out.println(response.getStatus());
      System.out.println(response.getData());
    } catch (Exception e){
      System.out.println(e); 
      return; 
    }

  }
} 
