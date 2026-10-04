package com.example.orthotrainer.model;

import java.util.List;

public class Question {
    private int id;
    private Difficulty difficulty;

    private String explanation;

    private int ruleId;
    private String text;
    private List<String> options;
    private int correctIndex;
    public String getExplanation() {
        return explanation;
    }


    public int getId() { return id; }
    public Difficulty getDifficulty() { return difficulty; }
    public int getRuleId() { return ruleId; }
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}