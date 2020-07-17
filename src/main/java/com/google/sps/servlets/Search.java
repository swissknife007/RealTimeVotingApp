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
        System.out.println("This is get request");
    }

    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // System.out.println("this is post request");
        // Enumeration enumeration = request.getParameterNames();
        // Map<String, Object> modelMap = new HashMap<>();
        // while(enumeration.hasMoreElements()){
        //     String parameterName = (String) enumeration.nextElement();
        //     modelMap.put(parameterName, request.getParameter(parameterName));
        // }
        // JSONObject json = new JSONObject();
        // for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
        //     json.put(entry.getKey(), entry.getValue());
        // }
        // response.setContentType("application/json");
        // response.getWriter().println(json.toString());
       
        final String questionTitle = "question";
        String questionTitleValue = request.getParameter("questionTitle");
        final String entity_questionDB = "survey";
        Query query = new Query(entity_questionDB).addFilter(questionTitle,FilterOperator.EQUAL,questionTitleValue);
        PreparedQuery Results = datastore.prepare(query);
        // String questionValue = null;
        List<String> questionValue = new ArrayList<>();
        //Retrieve all the OPTIONS available
        for (Entity entity:Results.asIterable()){
            questionValue.add((String) entity.getProperty("roomID"));
        }
        // questionValue = (String) entity.getProperty("roomID");
        response.setContentType("application/json");
        // response.getWriter().println("Question asked: " + questionTitleValue);
        // response.getWriter().println("Numbers of room existent: " + questionValue.size());
        // response.getWriter().println("Room ID: " + questionValue);


        
        // response.getWriter().println("The room is " + parameter);

        //  response.getWriter().println(json);

    }
}