package com.example.orthotrainer;

import android.content.Context;

import com.example.orthotrainer.model.Question;
import com.example.orthotrainer.model.Rule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataRepository {

    private List<Rule> rules;
    private List<Question> questions;

    public DataRepository(Context context) {
        loadData(context);
    }

    private void loadData(Context context) {

        try {

            InputStream is = context.getAssets().open("rules.json");

            InputStreamReader reader = new InputStreamReader(is);

            JsonObject root = new Gson().fromJson(
                    reader,
                    JsonObject.class
            );

            Type ruleListType =
                    new TypeToken<List<Rule>>() {}.getType();

            rules = new Gson().fromJson(
                    root.get("rules"),
                    ruleListType
            );

            Type questionListType =
                    new TypeToken<List<Question>>() {}.getType();

            questions = new Gson().fromJson(
                    root.get("questions"),
                    questionListType
            );

            reader.close();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<Question> getQuestionsForRule(int ruleId) {

        List<Question> result = new ArrayList<>();

        for (Question q : questions) {

            if (q == null) {
                continue;
            }

            if (q.getRuleId() == ruleId) {
                result.add(q);
            }
        }

        result.sort(new Comparator<Question>() {

            @Override
            public int compare(
                    Question o1,
                    Question o2
            ) {

                return Integer.compare(
                        o1.getDifficulty().ordinal(),
                        o2.getDifficulty().ordinal()
                );
            }
        });

        return result;
    }
}