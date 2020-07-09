package com.google.sps.data;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public final class Survey{
    private final String question;
    private final String[] option;
    public Survey(String question, String[] option)
    {
        this.question = question;
        this.option = new String[option.length];
        for (int i = 0; i < option.length;i++)
            this.option[i] = option[i];
    }

    public String getQuestion(){
        return question;
    }

    public String[] getOption(){
        return option;
    }
}