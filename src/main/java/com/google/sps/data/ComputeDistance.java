package com.google.sps.data;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class ComputeDistance {

    public void compute() {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        System.out.println(l.distance("My string", "My $tring"));
    }

}