package com.google.sps.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.PriorityQueue;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class ComputeDistance {

    /*
     * Helper method to compute the distance between two strings
     */
    public double compute(String string1, String string2) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        return l.distance(string1, string2);
    }

    public boolean testConnection(String forTest) {
        String url1String = "https://www.quora.com/" + forTest.replace(' ', '-');
        System.out.println(url1String);
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
        String filename = "../../train.csv";
        // class with CSV file as a parameter.
        try {
            PriorityQueue<QuoraQuestion> q = new PriorityQueue<>();
            HashSet<String> h = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(",");
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
            br.close();
            while (q.size() > 0) {
                System.out.println(this.testConnection(q.remove().getUrl()));
                Thread.sleep(1000);
            }
            // while ((line = br.readLine()) != null) {
            // String[] splitLine = line.split(",");

            // String url1String = "https://www.quora.com/"
            // + splitLine[3].replace(' ', '-').substring(1, splitLine[3].length() - 1);
            // URL url1 = new URL(url1String);
            // HttpURLConnection huc1 = (HttpURLConnection) url1.openConnection();
            // int responseCode1 = huc1.getResponseCode();

            // String url2String = "https://www.quora.com/"
            // + splitLine[4].replace(' ', '-').substring(1, splitLine[4].length() - 1);
            // URL url2 = new URL(url2String);
            // HttpURLConnection huc2 = (HttpURLConnection) url2.openConnection();
            // int responseCode2 = huc2.getResponseCode();

            // if ((responseCode1 == HttpURLConnection.HTTP_NOT_FOUND)
            // || (responseCode2 == HttpURLConnection.HTTP_NOT_FOUND)) {
            // continue;
            // }
            // if (splitLine.length != 6) {
            // continue;
            // }

            // String firstStr = splitLine[3].substring(1, splitLine[3].length() - 1);
            // double firstComp = this.compute(query, firstStr);
            // if (firstComp < minDist) {
            // minDist = firstComp;
            // result = firstStr;
            // }

            // String secondStr = splitLine[4].substring(1, splitLine[4].length() - 1);
            // double secondComp = this.compute(query, secondStr);
            // if (secondComp < minDist) {
            // minDist = secondComp;
            // result = secondStr;
            // }
            // }
            // br.close();
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}