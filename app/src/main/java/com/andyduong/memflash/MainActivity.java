package com.andyduong.memflash;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int correct_answer_id = R.id.flashcard_mc_answer_4; // TODO: will not be constant later
    private static final int INCORRECT_ANSWERS_MAX = 3;

    // MARK: Properties
    TextView flashcard_question,
             flashcard_answer,
             flashcard_mc_answer_1,
             flashcard_mc_answer_2,
             flashcard_mc_answer_3,
             flashcard_mc_answer_4;
    ImageButton show_hint_button,
                hide_hint_button,
                add_card_button,
                edit_card_button;
    ArrayList<String> incorrect_answers;

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
        edit_card_button      = findViewById(R.id.edit_card_button);
        incorrect_answers     = new ArrayList<String>(INCORRECT_ANSWERS_MAX);

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

        // Sets onClick for edit card button
        edit_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate from main activity to add card activity
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                String question = flashcard_question.getText().toString();
                String answer = flashcard_answer.getText().toString();

                // TODO: This should be removed in later iterations when we only display user answers.
                // Store the set of incorrect answers.
                if (R.id.flashcard_mc_answer_1 != correct_answer_id) {
                    incorrect_answers.add(flashcard_mc_answer_1.getText().toString());
                }
                if (R.id.flashcard_mc_answer_2 != correct_answer_id) {
                    incorrect_answers.add(flashcard_mc_answer_2.getText().toString());
                }
                if (R.id.flashcard_mc_answer_3 != correct_answer_id) {
                    incorrect_answers.add(flashcard_mc_answer_3.getText().toString());
                }
                if (R.id.flashcard_mc_answer_4 != correct_answer_id) {
                    incorrect_answers.add(flashcard_mc_answer_4.getText().toString());
                }

                intent.putExtra("question", question);
                intent.putExtra("answer", answer);

                // TODO: Figure out use an array to pass incorrect answers.
                intent.putExtra("incorrect_answer_1", incorrect_answers.get(0));
                intent.putExtra("incorrect_answer_2", incorrect_answers.get(1));
                intent.putExtra("incorrect_answer_3", incorrect_answers.get(2));

                MainActivity.this.startActivityForResult(intent, 300);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle data response from add card activity
        if ((requestCode == 200 || requestCode == 300) && resultCode == RESULT_OK) {
            // Display appropriate message via snackbars
            if (requestCode == 200) {
                Snackbar.make(findViewById(R.id.flashcard_question),
                        "Card created.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
            else if (requestCode == 300) {
                Snackbar.make(findViewById(R.id.flashcard_question),
                        "Card saved.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

            String question = data.getExtras().getString("question");
            String answer = data.getExtras().getString("answer");

            // TODO: Array of incorrects
            String incorrect_answer_1 = data.getExtras().getString("incorrect_answer_1");
            String incorrect_answer_2 = data.getExtras().getString("incorrect_answer_2");
            String incorrect_answer_3 = data.getExtras().getString("incorrect_answer_3");

            flashcard_question.setText(question);
            flashcard_answer.setText(answer);

            // TODO: Array of incorrects
            incorrect_answers.clear();
            incorrect_answers.add(incorrect_answer_1);
            incorrect_answers.add(incorrect_answer_2);
            incorrect_answers.add(incorrect_answer_3);

            // TODO: Figure out the randomization logic
            flashcard_mc_answer_1.setText(incorrect_answer_1);
            flashcard_mc_answer_2.setText(incorrect_answer_2);
            flashcard_mc_answer_3.setText(incorrect_answer_3);
            flashcard_mc_answer_4.setText(answer);

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
