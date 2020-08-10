// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.util.*;
import java.net.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

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
import com.google.sps.data.Survey;

@WebServlet("/id")
public class RetrieveBasedOnId extends HttpServlet {
  private String roomID;

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try{
      final String id = "id";
      final String roomID = "roomID";
      final String idValue = request.getParameter(id);
      final String entity_questionDB = "survey";
      String questionTypeValue = null;
      Query Firstquery = new Query(entity_questionDB).addFilter(roomID,FilterOperator.EQUAL,idValue);
      PreparedQuery FirstResults = datastore.prepare(Firstquery);
      String questionValue = null;
      List<String> optionsAvailable = new ArrayList<>();

     //Retrieve all the OPTIONS available
      for (Entity FirstEntity:FirstResults.asIterable()){
        questionValue = (String) FirstEntity.getProperty("question");
        questionTypeValue = (String)FirstEntity.getProperty("questionType");
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
            retur = retur.substring(retur.lastIndexOf("/")+1,retur.length());
            for (int j = 0; j < optionsAvailable.size();j++){
                if (optionsAvailable.get(j).contains(retur)) // not triggering this function
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
            if (questionTypeValue.equals("questionMap")) {
                jsonObj.put("OptionName", optionsAvailable.get(j).split(",",0)[2]);  
            } else {  
                jsonObj.put("OptionName", optionsAvailable.get(j));
            }
            jsonObj.put("NumberOfVotes", hm.get(optionsAvailable.get(j)));
            jsonArray.put(jsonObj);
        }
        json.put("question",questionValue);
        json.put("questionType", questionTypeValue);
        json.put("options",jsonArray);
        response.setContentType("application/json");
        response.getWriter().println(json.toString());
      }
        catch(Exception error)
        {
            String sendingError = null;
            response.getWriter().println(sendingError);
        }
}

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    final String question = "question";
    final String option = "choice";
    final String roomID = "roomID";
    final String ipAddress = "IP";
    final String questionValue = request.getParameter(question);
    final String chosenValue = request.getParameter(option);
    List<String> test = new ArrayList<>();
    test.add(chosenValue);
    final String ip = request.getParameter(ipAddress);
    final String id = request.getParameter(roomID);
    Filter propertyFilter = new FilterPredicate("roomID", FilterOperator.EQUAL, id);
    Query query = new Query("vote").setFilter(propertyFilter);
    PreparedQuery voteResults = datastore.prepare(query);
    for (Entity entity : voteResults.asIterable()) {
      String ipValue = (String) entity.getProperty("IP");
      if (ipValue.equals(ip)) {
        response.setContentType("text/html;");
        String vote = "<h1>You have already voted for this survey!</h1> <meta http-equiv='refresh' content='2; url = https://summer20-sps-20.ue.r.appspot.com/showVotes.html?id="
            + id + "' />";
        response.getWriter().println(vote);
        return;
      }
    }

    final String votingDataName = "vote";
    Entity voteData = new Entity(votingDataName);
    voteData.setProperty(question, questionValue);
    voteData.setProperty(option, chosenValue); 
    voteData.setProperty(roomID, id);
    voteData.setProperty(ipAddress, ip);
    datastore.put(voteData);

    response.setContentType("text/html;");
    String vote = "<h1>Thank you for voting!</h1> <meta http-equiv='refresh' content='2; url=https://summer20-sps-20.ue.r.appspot.com/showVotes.html?id="
        + id + "' />";
    response.getWriter().println(vote);
  }
}
