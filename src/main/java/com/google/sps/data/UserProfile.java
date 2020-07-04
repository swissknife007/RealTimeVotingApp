package com.google.sps.data;

public final class UserProfile{
    private final String question;
    private final String option;
    
    public UserProfile(String question, String option)
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