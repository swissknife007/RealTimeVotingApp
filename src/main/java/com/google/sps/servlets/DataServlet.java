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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.ComputeDistance;
import com.google.sps.data.Survey;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@WebServlet("/data")
public class DataServlet extends HttpServlet {

  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // old id
    final String roomID = request.getParameter("id");
    
    Filter propertyFilter = new FilterPredicate("roomID", FilterOperator.EQUAL, roomID);
    Query query = new Query("survey").setFilter(propertyFilter);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String questionValue = (String) entity.getProperty("question");
      String questionTypeValue = (String) entity.getProperty("questionType");
      List<String> optionValue = (List<String>) entity.getProperty("option");
      String mostSimilar = (String) entity.getProperty("mostSimilarQuestion");

      Survey survey = new Survey(questionValue, optionValue.toArray(new String[optionValue.size()]), mostSimilar,questionTypeValue);
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
    final String question = "question";
    String questionTypeValue = request.getParameter("questionType");
    final String questionTypePicture = "questionPicture";
    final String surveyDataName = "survey";
    final String roomID = "roomID";
    final String timestamp = "timestamp";
    final String questionIndex = "questionIndex";
    final String mostSimilarQuestionLabel = "mostSimilarQuestion";
    final String questionValue = request.getParameter(question);

    String[] questionValueIndex = questionValue.split(" ", 0);
    ZonedDateTime time = ZonedDateTime.now(ZoneId.of("US/Eastern"));
    String timestampValue = time.toString();
    UUID id = UUID.randomUUID();
    final String option = "option";
    String questionType = "questionType";
    ComputeDistance computer = new ComputeDistance(getServletContext());
    String mostSimilarQuestion = computer.findSimilarStrings(questionValue);
    for (int i = 0; i < questionValueIndex.length; i++)
        questionValueIndex[i] = questionValueIndex[i].toLowerCase();
        
    //Separates two different options and insert the request data accordingly
    if (questionTypeValue.equals(questionTypePicture))
    {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("image");

        // User submitted form without selecting a file, so we can't get a URL. (devserver)
        if(blobKeys == null || blobKeys.isEmpty()) {
            System.out.println("No picture");
            }

        // Our form only contains a single file input, so get the first index.
        List<String> picsURL = new ArrayList<String>();
        for (int i = 0 ; i < blobKeys.size(); i++)
        {
            BlobKey blobKey = blobKeys.get(i);
            // User submitted form without selecting a file, so we can't get a URL. (live server)
            BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
            if (blobInfo.getSize() == 0) {
                blobstoreService.delete(blobKey);
                System.out.println("Deleting");
            }
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
            picsURL.add(imagesService.getServingUrl(options));
        }
        
        //Display what is inserted 
        for (int i = 0; i < picsURL.size();i++)
        {
            System.out.println("Picture: " + picsURL.get(i));
        }
        Entity SurveyData = new Entity(surveyDataName);

        // Insert to database
        SurveyData.setProperty(questionType,questionTypeValue);
        SurveyData.setProperty(roomID, id.toString());
        SurveyData.setProperty(question, questionValue);
        SurveyData.setProperty(timestamp, timestampValue);
        SurveyData.setProperty(option, picsURL);
        SurveyData.setProperty(questionIndex, Arrays.asList(questionValueIndex));
        SurveyData.setProperty(mostSimilarQuestionLabel, mostSimilarQuestion);

        datastore.put(SurveyData);

    }
    else 
    {
        // Retrieve the options values into string array then store into StringList for
        // datastore
        final String[] retrievedOptionValue = request.getParameterValues(option);
        final List<String> optionValue = new ArrayList<>();
        for (int i = 0; i < retrievedOptionValue.length; i++)
        optionValue.add(retrievedOptionValue[i]);

        // Add timestamp to database
        Entity SurveyData = new Entity(surveyDataName);
        System.out.println(mostSimilarQuestion);
        SurveyData.setProperty(questionType,questionTypeValue);
        SurveyData.setProperty(roomID, id.toString());
        SurveyData.setProperty(question, questionValue);
        SurveyData.setProperty(option, optionValue);
        SurveyData.setProperty(timestamp, timestampValue);
        SurveyData.setProperty(mostSimilarQuestionLabel, mostSimilarQuestion);
        SurveyData.setProperty(questionIndex, Arrays.asList(questionValueIndex));
        datastore.put(SurveyData);
  }

    // Return html to voting.js
    response.setContentType("text/html");
    String html = "<h1>Loading...</h1> <meta http-equiv='refresh' content='1; url=https://summer20-sps-20.ue.r.appspot.com/votePage.html?id="
        + id + "' />";
    response.getWriter().println(html);
    }
}
