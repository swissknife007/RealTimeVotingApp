package com.google.sps.servlets;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.*;
import java.util.Enumeration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
@WebServlet("/search")
public class Search extends HttpServlet {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String returnedValue = request.getParameter("searchBar");
        String [] arrOfStr = returnedValue.split(" ",0);
        for (int i = 0; i < arrOfStr.length;i++)
            arrOfStr[i] = arrOfStr[i].toLowerCase();
        List<String> itemList = Arrays.asList(arrOfStr);
        final String entity_questionDB = "survey";
        final String questionTitle = "question";
        final String questionTitleIndex = "questionIndex";
        final String roomID = "roomID";
        final String id = "ID";
        final String title = "title";
        Query query = new Query(entity_questionDB).addFilter(questionTitleIndex,FilterOperator.IN,itemList);
        PreparedQuery Results = datastore.prepare(query);
        List<String> resultValues = new ArrayList<>();
        List<String> questionTitles = new ArrayList<>();
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;  

        //Get the room ID & titles and insert into JSON
        for (Entity entity:Results.asIterable()){
            jsonObj = new JSONObject();
            resultValues.add((String) entity.getProperty(roomID));
            jsonObj.put(id,(String) entity.getProperty(roomID));
            jsonObj.put(title, (String) entity.getProperty(questionTitle));
            jsonArray.put(jsonObj);
        }
        json.put(roomID,jsonArray);
        if (resultValues.size() == 0)
        {
            String error = null;
            response.getWriter().println(error);
        }
        else{
            response.setContentType("application/json");
            response.getWriter().println(json.toString());
        }
    }
}