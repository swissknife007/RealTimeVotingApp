package com.google.sps.data;

public final class Survey {
    private final String question;
    private final String[] option;
    private final String mostSimilarQuestion;
    private final String questionType;

    public Survey(String question, String[] option, String mostSimilar, String questionType) {
        this.question = question;
        this.option = new String[option.length];
        this.mostSimilarQuestion = mostSimilar;
        this.questionType = questionType;
        for (int i = 0; i < option.length; i++)
            this.option[i] = option[i];
    }

    public String getQuestionType()
    {
        return questionType;
    }
    public String getQuestion() {
        return question;
    }

    public String[] getOption() {
        return option;
    }

    public String getMostSimilar() {
        return mostSimilarQuestion;
    }
}