
package com.google.sps.servlets;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.*;

import java.net.*; 
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gson.Gson;
import com.google.sps.data.Encryption;
import com.google.sps.data.Survey;
// import org.codehaus.jackson.map.ObjectMapper; //Jackson API

@WebServlet("/show")
public class RetrieveVotes extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

 @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try{

    //-------------------------------FIRST QUERY----------------------------
      final String id = "id";

      final String idValue = request.getParameter(id);
      final String entity_questionDB = "survey";
      Query Firstquery = new Query(entity_questionDB).addFilter("roomID",FilterOperator.EQUAL,idValue);
      PreparedQuery FirstResults = datastore.prepare(Firstquery);

      //Debug
      System.out.println("Your roomID is " + idValue);
      System.out.println("PreparedQuery: " + FirstResults);
      System.out.println("Query: " + Firstquery);


     String questionValue = null;
     List<String> optionsAvailable = new ArrayList<>();
     //Retrieve the question value to be used in another entity to filter options voted
      for (Entity FirstEntity:FirstResults.asIterable()){
        questionValue = (String) FirstEntity.getProperty("question");
        optionsAvailable = (List<String>) FirstEntity.getProperty("option");
        System.out.println("Question is " + questionValue);
        System.out.println("Options are " + optionsAvailable);
        System.out.println("You have " + optionsAvailable.size() + "option");
      }

      if (questionValue == null) 
        throw new Exception("Error! Invalid ID");

        //Debug
      for(int i = 0 ; i < optionsAvailable.size(); i++){
        System.out.println("Option " + (i+1) + " is " + optionsAvailable.get(i));
      }


        //Retrieve all options chosen
        //-------------------------------SECOND QUERY----------------------------
        final String entity_voteDB = "vote";
        Query Secondquery = new Query(entity_voteDB).addFilter("roomID",FilterOperator.EQUAL,idValue);
        PreparedQuery SecondResults = datastore.prepare(Secondquery);
         System.out.println("------------------------------SECOND QUERY------------------------------");
         System.out.println("Your roomID is " + idValue);
         System.out.println("PreparedQuery: " + SecondResults);
        System.out.println("Query: " + Secondquery);

        int a = 0;
        String retur = new String();
        Map<String, Integer> hm = new HashMap<String, Integer>();
        for (int i = 0; i < optionsAvailable.size(); i++)
        {
            hm.put(optionsAvailable.get(i),0);
            System.out.println(optionsAvailable.get(i) + " is " + hm.get(optionsAvailable.get(i)));

        }
        System.out.println("------------------------------MAP COMPLETED------------------------------");
        int i = 0;

        for (Entity SecondEntity:SecondResults.asIterable()){
            retur = (String) SecondEntity.getProperty("choice");
            // System.out.println("Options available is " + optionsAvailable.get(i));
    
            System.out.println("return is " + retur);
            for (int j = 0; j < optionsAvailable.size();j++){
                if (optionsAvailable.get(j).contains(retur))
                {
                    System.out.println("-----------------------------Passed-------------------");
                    System.out.println("optionsAvailable.get is " + optionsAvailable.get(j) + " and its value is " + (hm.get(optionsAvailable.get(j))+1 ));
                    hm.put(optionsAvailable.get(j),(hm.get(optionsAvailable.get(j))+1));
                    System.out.println(optionsAvailable.get(j) + " is equal to " + retur);
                }
            }
            i++;
        }
        System.out.println("Numbers of iterations " + i);
        System.out.println("------------------------------HASH MAP AFTER-----------------------------");
        for (int j = 0; j < optionsAvailable.size(); j++)
        {
            System.out.println(optionsAvailable.get(j) + " is " + hm.get(optionsAvailable.get(j)));

        }
       
        //JSON PART
        // Gson gson = new Gson(); 
        // String json = gson.toJson(hm); 
        // System.out.println(json);
        // System.out.println("question final is " + questionValue);

        System.out.println(hm);
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("question", questionValue);
        sampleObject.put("option",hm);
        System.out.println(sampleObject);
        System.out.println(sampleObject.get("question"));
        System.out.println(sampleObject.get("option"));
        String finalJson = sampleObject.toString();
        response.getWriter().println(finalJson);
      }
        catch(Exception error)
        {
            response.getWriter().println(error);
        }
}
}