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
import java.util.Enumeration; // delete
import java.io.PrintWriter;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/id")
public class RetrieveBasedOnId extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

      //old id
    final String roomID = request.getParameter("id");

    Filter propertyFilter = new FilterPredicate("roomID", FilterOperator.EQUAL, roomID);
    Query query = new Query("survey").setFilter(propertyFilter);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String questionValue = (String) entity.getProperty("question");
      List<String> optionValue = (List<String>) entity.getProperty("option");

      Survey survey = new Survey(questionValue, optionValue.toArray(new String[optionValue.size()]));
      Gson gson = new Gson();
      String json = gson.toJson(survey);
      response.setContentType("application/json;");
      response.getWriter().println(json);
      return;
    }
    response.setContentType("application/json;");
    String str = "{\"error\":\"404\"}";
    response.getWriter().println(str);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Encryption.registerEncryption();

    final String question = "question";
    final String option = "choice";
    final String roomID = "roomID";
    final String ipAddress = "IP";
    final String questionValue = request.getParameter(question);
    final String chosenValue = request.getParameter(option);
    final String ip = request.getParameter(ipAddress);
    final String id = request.getParameter(roomID);

    Blob blob = new Blob(Encryption.encrypt(ip));

    final String votingDataName = "vote";
    Entity voteData = new Entity(votingDataName);
    voteData.setProperty(question, questionValue);
    voteData.setProperty(option, chosenValue);
    voteData.setProperty(roomID, id);
    voteData.setProperty(ipAddress, blob);
    datastore.put(voteData);

    response.setContentType("text/html;");
    String vote = "Thank you for your voting";
    response.getWriter().println(vote);
    }
}
