package com.google.sps.data;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public final class Survey{
    private final String question;
    private final String[] option;
    private final int length;
    public Survey(String question, String[] option)
    {
        this.question = question;
        length = option.length;
        this.option = new String[length];
        for (int i = 0; i < option.length;i++)
            this.option[i] = option[i];
    }
    public int getOptionLength()
    {
        return length;
    }

    public String getQuestion(){
        return question;
    }

    public String[] getOption(){
        return option;
    }
}