package com.example.orthotrainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.orthotrainer.model.Rule;

import java.util.List;

public class RuleDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_detail);
        View rootView = findViewById(R.id.detailRoot);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            v.setPadding(v.getPaddingLeft(), topInset, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });


        int ruleId = getIntent().getIntExtra("RULE_ID", -1);
        DataRepository dataRepository=new DataRepository(this);
        List<Rule> listRules=dataRepository.getRules();

        Rule foundRule = null;
        for (Rule r : listRules) {
            if (r.getId() == ruleId) {
                foundRule = r;
                break;
            }
        }
        TextView titleView = findViewById(R.id.detailTitle);
        TextView descriptionView = findViewById(R.id.detailDescription);

        if (foundRule != null) {
            titleView.setText(foundRule.getTitle());
            descriptionView.setText(foundRule.getDescription());
        }
        Button practiceButton = findViewById(R.id.practiceButton);
        practiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(RuleDetailActivity.this, QuizActivity.class);
            intent.putExtra("RULE_ID", ruleId);
            startActivity(intent);
        });



    }


}
