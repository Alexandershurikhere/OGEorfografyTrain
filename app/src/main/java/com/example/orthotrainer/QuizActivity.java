package com.example.orthotrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.orthotrainer.model.Question;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private int correctCount = 0;
    private boolean quizFinished = false;

    private List<Question> questions;
    private int currentIndex = 0;

    private TextView questionText;
    private LinearLayout optionsContainer;
    private TextView feedbackText;

    private Button nextButton;
    private Button homeButton;

    private int ruleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        View rootView = findViewById(R.id.quizRoot);

        ViewCompat.setOnApplyWindowInsetsListener(
                rootView,
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

        questionText = findViewById(R.id.questionText);
        optionsContainer = findViewById(R.id.optionsContainer);
        feedbackText = findViewById(R.id.feedbackText);

        nextButton = findViewById(R.id.nextButton);
        homeButton = findViewById(R.id.homeButton);

        ruleId = getIntent().getIntExtra("RULE_ID", -1);

        DataRepository dataRepository = new DataRepository(this);

        questions = dataRepository.getQuestionsForRule(ruleId);

        showQuestion();

        nextButton.setOnClickListener(v -> {

            if (quizFinished) {

                quizFinished = false;
                correctCount = 0;
                currentIndex = 0;

                Collections.shuffle(questions);

                optionsContainer.setVisibility(View.VISIBLE);

                nextButton.setText("Далее");
                nextButton.setEnabled(false);

                homeButton.setVisibility(View.GONE);

                showQuestion();

            } else {

                currentIndex++;

                if (currentIndex < questions.size()) {

                    showQuestion();

                } else {

                    finishQuiz();
                }
            }
        });

        homeButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    QuizActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);

            finish();
        });
    }

    private void showQuestion() {

        Question currentQuestion = questions.get(currentIndex);

        questionText.setText(currentQuestion.getText());

        feedbackText.setText("");

        nextButton.setEnabled(false);

        optionsContainer.removeAllViews();

        for (int i = 0; i < currentQuestion.getOptions().size(); i++) {

            Button optionButton = new Button(this);

            optionButton.setText(
                    currentQuestion.getOptions().get(i)
            );

            optionsContainer.addView(optionButton);

            int answerIndex = i;

            optionButton.setOnClickListener(v -> {

                if (answerIndex == currentQuestion.getCorrectIndex()) {

                    feedbackText.setText("Правильно!");

                    v.setBackgroundColor(
                            getColor(R.color.correct_answer)
                    );

                    correctCount++;

                } else {

                    feedbackText.setText("Неправильно!");

                    v.setBackgroundColor(
                            getColor(R.color.incorrect)
                    );
                }

                nextButton.setEnabled(true);

                for (int j = 0; j < optionsContainer.getChildCount(); j++) {

                    View child = optionsContainer.getChildAt(j);

                    child.setEnabled(false);
                }
            });
        }
    }

    private void finishQuiz() {

        optionsContainer.setVisibility(View.GONE);

        feedbackText.setText("");

        questionText.setText(
                "Квиз завершён!\n" +
                        "Правильных ответов: " +
                        correctCount +
                        " из " +
                        questions.size()
        );

        saveRuleAsCompleted();

        nextButton.setText("Пройти заново");
        nextButton.setEnabled(true);

        homeButton.setVisibility(View.VISIBLE);

        quizFinished = true;
    }

    private void saveRuleAsCompleted() {

        getSharedPreferences(
                "progress",
                MODE_PRIVATE
        )
                .edit()
                .putBoolean(
                        "rule_" + ruleId + "_completed",
                        true
                )
                .apply();
    }
}