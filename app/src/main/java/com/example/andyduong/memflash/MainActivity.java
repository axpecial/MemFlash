package com.example.andyduong.memflash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int correct_answer_id = R.id.flashcard_answer_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links the XML layout file to this Java file, which handles the user interaction for the
        // corresponding activity
        setContentView(R.layout.activity_main);

        // Sets onClick for flashcard question
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);
            }
        });

        // Sets onClick for flashcard answer
        findViewById(R.id.flashcard_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
            }
        });

        // Sets onClick for show hint button
        findViewById(R.id.show_hint_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.show_hint_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.hide_hint_button).setVisibility(View.VISIBLE);
                showAllAnswers();
            }
        });

        // Sets onClick for hide hint button
        findViewById(R.id.hide_hint_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.hide_hint_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.show_hint_button).setVisibility(View.VISIBLE);
                hideAllAnswers();
            }
        });

        // Sets onClick for flashcard answer 1 button
        findViewById(R.id.flashcard_answer_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correct_answer_id != R.id.flashcard_answer_1) {
                    markIncorrectAnswer(R.id.flashcard_answer_1);
                }
                showCorrectAnswer();
            }
        });

        // Sets onClick for flashcard answer 2 button
        findViewById(R.id.flashcard_answer_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correct_answer_id != R.id.flashcard_answer_2) {
                    markIncorrectAnswer(R.id.flashcard_answer_2);
                }
                showCorrectAnswer();
            }
        });

        // Sets onClick for flashcard answer 3 button
        findViewById(R.id.flashcard_answer_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correct_answer_id != R.id.flashcard_answer_3) {
                    markIncorrectAnswer(R.id.flashcard_answer_3);
                }
                showCorrectAnswer();
            }
        });

        // Sets onClick for flashcard answer 1 button
        findViewById(R.id.flashcard_answer_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correct_answer_id != R.id.flashcard_answer_4) {
                    markIncorrectAnswer(R.id.flashcard_answer_4);
                }
                showCorrectAnswer();
            }
        });

    }

    private void showAllAnswers() {
        findViewById(R.id.flashcard_answer_1).setVisibility(View.VISIBLE);
        findViewById(R.id.flashcard_answer_2).setVisibility(View.VISIBLE);
        findViewById(R.id.flashcard_answer_3).setVisibility(View.VISIBLE);
        findViewById(R.id.flashcard_answer_4).setVisibility(View.VISIBLE);
    }

    private void hideAllAnswers() {
        findViewById(R.id.flashcard_answer_1).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcard_answer_2).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcard_answer_3).setVisibility(View.INVISIBLE);
        findViewById(R.id.flashcard_answer_4).setVisibility(View.INVISIBLE);
    }

    private void showCorrectAnswer() {
        findViewById(correct_answer_id).setBackgroundResource(R.drawable.card_background_correct);
        ((TextView) findViewById(correct_answer_id)).setTextColor(
                getResources().getColor(R.color.bsWhite, null)
        );
    }

    private void markIncorrectAnswer(int id) {
        findViewById(id).setBackgroundResource(R.drawable.card_background_incorrect);
        ((TextView) findViewById(id)).setTextColor(
                getResources().getColor(R.color.bsWhite, null)
        );
    }
}
