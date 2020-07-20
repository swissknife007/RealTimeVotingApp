package com.google.sps.data;

import java.io.BufferedReader;
import java.io.FileReader;

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