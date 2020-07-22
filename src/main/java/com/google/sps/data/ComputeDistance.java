package com.google.sps.data;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.servlet.ServletContext;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class ComputeDistance {

    private ServletContext context;

    public ComputeDistance(ServletContext context) {
        this.context = context;
    }

    /*
     * Helper method to compute the distance between two strings
     */
    public double compute(String string1, String string2) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        return l.distance(string1, string2);
    }

    public boolean testConnection(String forTest) {
        String url1String = "https://www.quora.com/" + forTest.replace(' ', '-');
        URL url1;
        try {
            url1 = new URL(url1String);
            HttpURLConnection huc1 = (HttpURLConnection) url1.openConnection();
            int responseCode1 = huc1.getResponseCode();
            if (responseCode1 == HttpURLConnection.HTTP_NOT_FOUND) {
                return false;
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return false;
        }

    }

    // Main method to return the most similar string
    public String findSimilarStrings(String query) {
        String result = "";
        // class with CSV file as a parameter.
        try {

            PriorityQueue<QuoraQuestion> q = new PriorityQueue<>();
            HashSet<String> h = new HashSet<String>();

            List<String> listOfData = new ArrayList<String>();
            listOfData.add("/WEB-INF/data/segmentaa.csv");
            listOfData.add("/WEB-INF/data/segmentab.csv");
            listOfData.add("/WEB-INF/data/segmentac.csv");

            for (String fileName : listOfData) {

                Scanner scanner = new Scanner(context.getResourceAsStream(fileName));

                while (scanner.hasNextLine()) {
                    String[] splitLine = scanner.nextLine().split(",");
                    if (splitLine.length != 6) {
                        continue;
                    }
                    String firstStr = splitLine[3].substring(1, splitLine[3].length() - 1);
                    double firstComp = this.compute(query, firstStr);
                    if (q.size() < 10) {
                        if (!h.contains(firstStr)) {
                            QuoraQuestion toAdd = new QuoraQuestion(firstStr, firstComp);
                            q.add(toAdd);
                            h.add(firstStr);

                        }
                    } else {
                        if (!h.contains(firstStr)) {
                            QuoraQuestion toAdd = new QuoraQuestion(firstStr, firstComp);
                            if (firstComp < q.peek().getScore()) {
                                q.remove();
                                q.add(toAdd);
                            }
                            h.add(firstStr);

                        }

                    }
                    String secondStr = splitLine[4].substring(1, splitLine[4].length() - 1);
                    double secondComp = this.compute(query, secondStr);
                    if (q.size() < 10) {
                        if (!h.contains(secondStr)) {
                            QuoraQuestion toAdd = new QuoraQuestion(secondStr, secondComp);
                            q.add(toAdd);
                            h.add(secondStr);
                        }
                    } else {
                        if (!h.contains(secondStr)) {
                            QuoraQuestion toAdd = new QuoraQuestion(secondStr, secondComp);
                            if (secondComp < q.peek().getScore()) {
                                q.remove();
                                q.add(toAdd);
                            }
                            h.add(secondStr);

                        }

                    }
                }
                scanner.close();
            }
            Double minVal = Double.MAX_VALUE;
            while (q.size() > 0) {
                QuoraQuestion ques = q.remove();
                System.out.println(this.testConnection(ques.getUrl()));
                if (this.testConnection(ques.getUrl())) {
                    if (ques.getScore() < minVal) {
                        result = ques.getUrl();
                        minVal = ques.getScore();
                    }
                }
                Thread.sleep(1000);
            }

            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}