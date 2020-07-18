package com.google.sps.servlets;
import org.json.JSONObject;

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


        //CASE 1: RESPONSE RETURNED AS TITLE CATEGORY
        System.out.println("this is post request");
        String returnedValue = request.getParameter("searchBar");
        final String entity_questionDB = "survey";
        System.out.println("Returned value is " + returnedValue);


        //If is question TITLE
        final String questionTitle = "question";
        Query query = new Query(entity_questionDB).addFilter(questionTitle,FilterOperator.EQUAL,returnedValue);
        PreparedQuery Results = datastore.prepare(query);
        List<String> resultValues = new ArrayList<>();

        //Get the room ID
        for (Entity entity:Results.asIterable()){
            resultValues.add((String) entity.getProperty("roomID"));
        }
        if (resultValues.size() == 0)
            response.getWriter().println("No matching room");
        else{
        //JSON setup for return
        JSONObject json = new JSONObject();
        for(int i =0; i < resultValues.size() ;i++)
        {
            json.put("RoomID", resultValues);
        }
        json.put("RoomID", resultValues);
        response.setContentType("application/json");
        response.getWriter().println(json.toString());
        }

        
        // // response.getWriter().println("The room is " + parameter);

        // //  response.getWriter().println(json);

    }
}