package com.google.sps.data;

public final class Survey{
    private final String question;
    private final String option;
    
    public Survey(String question, String option)
    {
        this.question = question;
        this.option = option;
    }

    public String getQuestion(){
        return question;
    }

    public String getOption(){
        return option;
    }
}