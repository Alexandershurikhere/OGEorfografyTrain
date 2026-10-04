package com.example.orthotrainer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orthotrainer.model.Rule;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RuleAdapter ruleAdapter;
    private List<Rule> listRules;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View mainRoot = findViewById(R.id.mainRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                mainRoot,
                (v, insets) -> {

                    int topInset = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    ).top;

                    v.setPadding(
                            v.getPaddingLeft(),
                            topInset,
                            v.getPaddingRight(),
                            v.getPaddingBottom()
                    );

                    return insets;
                }
        );

        progressText = findViewById(R.id.progressText);

        RecyclerView recyclerView =
                findViewById(R.id.rulesRecyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        DataRepository dataRepository =
                new DataRepository(this);

        listRules = dataRepository.getRules();

        ruleAdapter = new RuleAdapter(
                this,
                listRules
        );

        recyclerView.setAdapter(ruleAdapter);

        updateProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ruleAdapter != null) {
            ruleAdapter.notifyDataSetChanged();
        }

        if (listRules != null) {
            updateProgress();
        }
    }

    private void updateProgress() {

        SharedPreferences preferences =
                getSharedPreferences(
                        "progress",
                        MODE_PRIVATE
                );

        int completedCount = 0;

        for (Rule rule : listRules) {

            boolean completed = preferences.getBoolean(
                    "rule_" + rule.getId() + "_completed",
                    false
            );

            if (completed) {
                completedCount++;
            }
        }

        progressText.setText(
                "Пройдено " +
                        completedCount +
                        " из " +
                        listRules.size()
        );
    }
}