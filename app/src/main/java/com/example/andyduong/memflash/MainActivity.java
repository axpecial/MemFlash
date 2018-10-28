package com.example.andyduong.memflash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int correct_answer_id = R.id.flashcard_mc_answer_4; // TODO

    // MARK: Properties
    TextView flashcard_question,
             flashcard_answer,
             flashcard_mc_answer_1,
             flashcard_mc_answer_2,
             flashcard_mc_answer_3,
             flashcard_mc_answer_4;
    ImageButton show_hint_button,
                hide_hint_button,
                add_card_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links the XML layout file to this Java file, which handles the user interaction for the
        // corresponding activity
        setContentView(R.layout.activity_main);

        flashcard_question    = findViewById(R.id.flashcard_question);
        flashcard_answer      = findViewById(R.id.flashcard_answer);
        flashcard_mc_answer_1 = findViewById(R.id.flashcard_mc_answer_1);
        flashcard_mc_answer_2 = findViewById(R.id.flashcard_mc_answer_2);
        flashcard_mc_answer_3 = findViewById(R.id.flashcard_mc_answer_3);
        flashcard_mc_answer_4 = findViewById(R.id.flashcard_mc_answer_4);
        show_hint_button      = findViewById(R.id.show_hint_button);
        hide_hint_button      = findViewById(R.id.hide_hint_button);
        add_card_button       = findViewById(R.id.add_card_button);

        // Sets onClick for flashcard question
        flashcard_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard_question.setVisibility(View.INVISIBLE);
                flashcard_answer.setVisibility(View.VISIBLE);
            }
        });

        // Sets onClick for flashcard answer
        flashcard_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard_question.setVisibility(View.VISIBLE);
                flashcard_answer.setVisibility(View.INVISIBLE);
            }
        });

        // Sets onClick for flashcard answer 1 button
        flashcard_mc_answer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_1);
            }
        });

        // Sets onClick for flashcard answer 2 button
        flashcard_mc_answer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_2);
            }
        });

        // Sets onClick for flashcard answer 3 button
        flashcard_mc_answer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_3);
            }
        });

        // Sets onClick for flashcard answer 1 button
        flashcard_mc_answer_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_4);
            }
        });

        // Sets onClick for show hint button
        show_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_hint_button.setVisibility(View.INVISIBLE);
                hide_hint_button.setVisibility(View.VISIBLE);
                showAllAnswers();
            }
        });

        // Sets onClick for hide hint button
        hide_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_hint_button.setVisibility(View.INVISIBLE);
                show_hint_button.setVisibility(View.VISIBLE);
                hideAllAnswers();
                resetAllAnswers();
            }
        });

        // Sets onClick for add card button
        add_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate from main activity to add card activity
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 200);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            flashcard_question.setText(question);
            flashcard_answer.setText(answer);
            resetCard();
        }
    }

    private void resetCard() {
        flashcard_question.setVisibility(View.VISIBLE);
        flashcard_answer.setVisibility(View.INVISIBLE);
    }

    private void resetAllAnswers() {
        flashcard_mc_answer_1.setBackgroundResource(R.drawable.card_background);
        flashcard_mc_answer_1.setTextColor(
                getResources().getColor(R.color.colorDarkGrey, null)
        );
        flashcard_mc_answer_2.setBackgroundResource(R.drawable.card_background);
        flashcard_mc_answer_2.setTextColor(
                getResources().getColor(R.color.colorDarkGrey, null)
        );
        flashcard_mc_answer_3.setBackgroundResource(R.drawable.card_background);
        flashcard_mc_answer_3.setTextColor(
                getResources().getColor(R.color.colorDarkGrey, null)
        );
        flashcard_mc_answer_4.setBackgroundResource(R.drawable.card_background);
        flashcard_mc_answer_4.setTextColor(
                getResources().getColor(R.color.colorDarkGrey, null)
        );
    }

    private void showAllAnswers() {
        flashcard_mc_answer_1.setVisibility(View.VISIBLE);
        flashcard_mc_answer_2.setVisibility(View.VISIBLE);
        flashcard_mc_answer_3.setVisibility(View.VISIBLE);
        flashcard_mc_answer_4.setVisibility(View.VISIBLE);
    }

    private void hideAllAnswers() {
        flashcard_mc_answer_1.setVisibility(View.INVISIBLE);
        flashcard_mc_answer_2.setVisibility(View.INVISIBLE);
        flashcard_mc_answer_3.setVisibility(View.INVISIBLE);
        flashcard_mc_answer_4.setVisibility(View.INVISIBLE);
    }

    private void checkAnswer(int id) {
        if (this.correct_answer_id != id) {
            findViewById(id).setBackgroundResource(R.drawable.card_background_incorrect);
            ((TextView) findViewById(id)).setTextColor(
                    getResources().getColor(R.color.bsWhite, null)
            );
        }

        findViewById(this.correct_answer_id).setBackgroundResource(R.drawable.card_background_correct);
        ((TextView) findViewById(this.correct_answer_id)).setTextColor(
                getResources().getColor(R.color.bsWhite, null)
        );
    }
}
