package com.andyduong.memflash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AddCardActivity extends AppCompatActivity {

    // MARK: Properties
    EditText    new_flashcard_question,
                new_flashcard_answer,
                new_flashcard_incorrect_answer_1,
                new_flashcard_incorrect_answer_2,
                new_flashcard_incorrect_answer_3;
    ImageButton cancel_card_button,
                save_card_button;
    // NOTE: Array over ArrayList because the size of the list of views should be immutable. This
    // activity indicates the maximum number of incorrect answers that are possible.
    EditText[] new_flashcard_incorrect_answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        // UI elements
        new_flashcard_question           = findViewById(R.id.new_flashcard_question);
        new_flashcard_answer             = findViewById(R.id.new_flashcard_answer);
        new_flashcard_incorrect_answer_1 = findViewById(R.id.new_flashcard_incorrect_answer_1);
        new_flashcard_incorrect_answer_2 = findViewById(R.id.new_flashcard_incorrect_answer_2);
        new_flashcard_incorrect_answer_3 = findViewById(R.id.new_flashcard_incorrect_answer_3);
        cancel_card_button               = findViewById(R.id.cancel_card_button);
        save_card_button                 = findViewById(R.id.save_card_button);

        // Data Structures
        // NOTE: Initialize the array after EditView references have been initialized, otherwise, the
        // array will hold null values
        new_flashcard_incorrect_answers = new EditText[] {
            new_flashcard_incorrect_answer_1,
            new_flashcard_incorrect_answer_2,
            new_flashcard_incorrect_answer_3,
        };

        // Get intent data
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");
        // ArrayList over array because incorrect answers are optional
        List<String> incorrectAnswers = getIntent().getStringArrayListExtra("incorrect_answers");

        // Handle edit card intent by populating the text fields
        if (question != null && answer != null && incorrectAnswers != null) {
            new_flashcard_question.setText(question);
            new_flashcard_answer.setText(answer);
            for (int i = 0; i < incorrectAnswers.size(); i++) {
                new_flashcard_incorrect_answers[i].setText(incorrectAnswers.get(i));
            }
        }

        //======================= Set onClick listeners =======================
        // Sets onClick for cancel card button
        cancel_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);

                // Dismiss activity to navigate from add card activity back to main activity
                finish();
            }
        });

        // Sets onClick for save card button
        save_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String question = new_flashcard_question.getText().toString().trim();
                String answer = new_flashcard_answer.getText().toString().trim();
                ArrayList<String> incorrectAnswers = new ArrayList<String>();
                // Compile non-empty entries for incorrect answers
                for (int i = 0; i < new_flashcard_incorrect_answers.length; i++) {
                    String incorrectAnswer = new_flashcard_incorrect_answers[i].getText().toString().trim();
                    if (!incorrectAnswer.equals("")) {
                        incorrectAnswers.add(incorrectAnswer);
                    }
                }

                // Error messages via toasts
                if (question.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Question field is empty.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
                else if (answer.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Answer field is empty.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
                else {
                    data.putExtra("question", question);
                    data.putExtra("answer", answer);
                    data.putStringArrayListExtra("incorrect_answers", incorrectAnswers);
                    setResult(RESULT_OK, data);

                    finish();

                }
            }
        });
    }
}
