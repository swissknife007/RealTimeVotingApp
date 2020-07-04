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
import java.io.PrintWriter;
import com.google.sps.data.UserProfile;
import com.google.gson.Gson;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


    //Query to datastore
    throw new IOException("Implement Get");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //Posting to datastore

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // get all parameter names
        Set<String> paramNames = request.getParameterMap().keySet();
        String question = request.getParameter("question");
        String option = request.getParameter("option");

        //Instantiate object for JSON
        UserProfile profile = new UserProfile(question,option);
        Gson gson = new Gson();
        String json = gson.toJson(profile);


        //Instantiate database object

        Entity UserEntity = new Entity("User");
        UserEntity.setProperty("Question", question);
        UserEntity.setProperty("Option",option);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(UserEntity);

        //Return JSON to testing
        response.setContentType("application/json;");
        response.getWriter().println(json);
  }
}

