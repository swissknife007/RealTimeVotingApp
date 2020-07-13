
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

@WebServlet("/show")
public class RetrieveVotes extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

 @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try{
      final String id = "id";
      final String roomID = "roomID";
      final String idValue = request.getParameter(id);
      final String entity_questionDB = "survey";
      Query Firstquery = new Query(entity_questionDB).addFilter(roomID,FilterOperator.EQUAL,idValue);
      PreparedQuery FirstResults = datastore.prepare(Firstquery);
     String questionValue = null;
     List<String> optionsAvailable = new ArrayList<>();

     //Retrieve all the OPTIONS available
      for (Entity FirstEntity:FirstResults.asIterable()){
        questionValue = (String) FirstEntity.getProperty("question");
        optionsAvailable = (List<String>) FirstEntity.getProperty("option");
      }

      if (questionValue == null) 
        throw new Exception("Error! Invalid ID");
        final String entity_voteDB = "vote";
        Query Secondquery = new Query(entity_voteDB).addFilter(roomID,FilterOperator.EQUAL,idValue);
        PreparedQuery SecondResults = datastore.prepare(Secondquery);
        String retur = new String();
        
        //Assign all options available to 0 to start counting 
        Map<String, Integer> hm = new HashMap<String, Integer>();
        for (int i = 0; i < optionsAvailable.size(); i++)
        {
            hm.put(optionsAvailable.get(i),0);
        }

        //Retrieve all votes computed and iterate to count repeated votes.
        for (Entity SecondEntity:SecondResults.asIterable()){
            retur = (String) SecondEntity.getProperty("choice");
            for (int j = 0; j < optionsAvailable.size();j++){
                if (optionsAvailable.get(j).contains(retur))
                {
                    hm.put(optionsAvailable.get(j),(hm.get(optionsAvailable.get(j))+1));
                }
            }
        }
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        // JSONArray jsonArray2 = new JSONArray();
        JSONObject jsonObj;

        for (int j =0; j < optionsAvailable.size();j++)
        {
            jsonObj = new JSONObject();
            jsonObj.put("OptionName", optionsAvailable.get(j));
            jsonObj.put("NumberOfVotes", hm.get(optionsAvailable.get(j)));
            jsonArray.put(jsonObj);
        }
        json.put("question",questionValue);
        json.put("options",jsonArray);
        response.setContentType("application/json");
        response.getWriter().println(json.toString());
      }
        catch(Exception error)
        {
            response.getWriter().println(error);
        }
}
}