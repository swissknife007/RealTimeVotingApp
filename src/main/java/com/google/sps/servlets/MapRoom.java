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
import java.net.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.Survey;

@WebServlet("/mapRoom")
public class MapRoom extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Query to datastore
    throw new IOException("Implement Get");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Posting to datastore
    // get all parameter names and its values from HTTP request
    final String question = "question";
    final String lats = "lats";
    final String lngs = "lngs";
    final String contents = "contents";

    final String questionValue = request.getParameter(question);
    String [] questionValueIndex = questionValue.split(" ",0);
    String[] latValues = request.getParameter(lats).split(",",0);
    String[] lngValues = request.getParameter(lngs).split(",",0);
    String[] contentValues = request.getParameter(contents).split(",",0);

    // Lower case all string before send to DB
    for (int i = 0; i < questionValueIndex.length;i++)
        questionValueIndex[i] = questionValueIndex[i].toLowerCase();
    
    // Create entity to store data into database
    final String surveyDataName = "map-survey";
    final String roomID = "roomID";
    final String timestamp = "timestamp";
    final String questionIndex = "questionIndex";
    final String latitude = "latitude";
    final String longitude = "longitude";
    final String content = "content";

    //Add timestamp to database
    ZonedDateTime time = ZonedDateTime.now(ZoneId.of("US/Eastern"));
    String timestampValue = time.toString();
    UUID id = UUID.randomUUID();

    for (int ms = 0; ms < latValues.length; ms++) {
        Entity MapData = new Entity(surveyDataName);
        MapData.setProperty(roomID, id.toString());
        MapData.setProperty(timestamp,timestampValue);
        MapData.setProperty(question, questionValue);
        MapData.setProperty(questionIndex, Arrays.asList(questionValueIndex));
        MapData.setProperty(latitude, latValues[ms]);
        MapData.setProperty(longitude, lngValues[ms]);
        MapData.setProperty(content, contentValues[ms]);
        datastore.put(MapData);
    }

    // Return JSON to testing
    response.setContentType("text/html");
    String html = "<h1>Loading...</h1> <meta http-equiv='refresh' content='1; url=https://summer20-sps-20.ue.r.appspot.com/votePage.html?id=" 
        + id + "' />";
    response.getWriter().println(html);
  }
}
