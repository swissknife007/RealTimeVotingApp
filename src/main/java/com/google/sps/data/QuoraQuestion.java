package com.google.sps.data;

public class QuoraQuestion implements Comparable<QuoraQuestion> {

    private String url;
    private Double score;

    public QuoraQuestion(String url, Double score) {
        this.url = url;
        this.score = score;
    }

    public String getUrl() {
        return this.url;
    }

    public Double getScore() {
        return this.score;
    }

    @Override
    public int compareTo(QuoraQuestion o) {
        if (this.getScore() > o.getScore()) {
            return -1;
        } else if (this.getScore() < o.getScore()) {
            return 1;
        } else {
            return 0;
        }
    }
}