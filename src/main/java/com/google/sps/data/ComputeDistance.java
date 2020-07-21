package com.google.sps.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class ComputeDistance {

    /*
     * Helper method to compute the distance between two strings
     */
    public double compute(String string1, String string2) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        return l.distance(string1, string2);
    }

    // Main method to return the most similar string
    public String findSimilarStrings(String query) {
        String result = "";
        String filename = "../../train.csv";
        // class with CSV file as a parameter.
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            double minDist = Double.POSITIVE_INFINITY;

            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(",");

                String url1String = "https://www.quora.com/"
                        + splitLine[3].replace(' ', '-').substring(1, splitLine[3].length() - 1);
                URL url1 = new URL(url1String);
                HttpURLConnection huc1 = (HttpURLConnection) url1.openConnection();
                int responseCode1 = huc1.getResponseCode();

                String url2String = "https://www.quora.com/"
                        + splitLine[4].replace(' ', '-').substring(1, splitLine[4].length() - 1);
                URL url2 = new URL(url2String);
                HttpURLConnection huc2 = (HttpURLConnection) url2.openConnection();
                int responseCode2 = huc2.getResponseCode();

                if ((responseCode1 == HttpURLConnection.HTTP_NOT_FOUND)
                        || (responseCode2 == HttpURLConnection.HTTP_NOT_FOUND)) {
                    continue;
                }
                if (splitLine.length != 6) {
                    continue;
                }

                String firstStr = splitLine[3].substring(1, splitLine[3].length() - 1);
                double firstComp = this.compute(query, firstStr);
                if (firstComp < minDist) {
                    minDist = firstComp;
                    result = firstStr;
                }

                String secondStr = splitLine[4].substring(1, splitLine[4].length() - 1);
                double secondComp = this.compute(query, secondStr);
                if (secondComp < minDist) {
                    minDist = secondComp;
                    result = secondStr;
                }
            }
            br.close();
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}