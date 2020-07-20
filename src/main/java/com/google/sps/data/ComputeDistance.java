package com.google.sps.data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class ComputeDistance {

    public double compute(String string1, String string2) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        return l.distance(string1, string2);
    }

    public List<String> findSimilarStrings(String query) {
        List<String> result = new ArrayList<String>();

        String filename = "train.csv";
        // class with CSV file as a parameter.
        try {
            FileReader filereader = new FileReader(filename);

            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();

            // print Data
            for (String[] row : allData) {
                this.compute(query, row[3]);
                this.compute(query, row[4]);

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}