package com.andyduong.memflash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class AddCardActivity extends AppCompatActivity {

    // MARK: Properties
    EditText    new_flashcard_question,
                new_flashcard_answer,
                new_flashcard_incorrect_answer_1,
                new_flashcard_incorrect_answer_2,
                new_flashcard_incorrect_answer_3;
    ImageButton cancel_card_button,
                save_card_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        new_flashcard_question           = findViewById(R.id.new_flashcard_question);
        new_flashcard_answer             = findViewById(R.id.new_flashcard_answer);
        new_flashcard_incorrect_answer_1 = findViewById(R.id.new_flashcard_incorrect_answer_1);
        new_flashcard_incorrect_answer_2 = findViewById(R.id.new_flashcard_incorrect_answer_2);
        new_flashcard_incorrect_answer_3 = findViewById(R.id.new_flashcard_incorrect_answer_3);
        cancel_card_button               = findViewById(R.id.cancel_card_button);
        save_card_button                 = findViewById(R.id.save_card_button);

        // Handles editing the current card
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");

        // TODO: Use an array for storing incorrect answers. Figure out how to populate those fields by iterating.
        String incorrect_answer_1 = getIntent().getStringExtra("incorrect_answer_1");
        String incorrect_answer_2 = getIntent().getStringExtra("incorrect_answer_2");
        String incorrect_answer_3 = getIntent().getStringExtra("incorrect_answer_3");

        if (question != null && answer != null
                && incorrect_answer_1 != null
                && incorrect_answer_2 != null
                && incorrect_answer_3 != null) {
            new_flashcard_question.setText(question);
            new_flashcard_answer.setText(answer);
            new_flashcard_incorrect_answer_1.setText(incorrect_answer_1);
            new_flashcard_incorrect_answer_2.setText(incorrect_answer_2);
            new_flashcard_incorrect_answer_3.setText(incorrect_answer_3);
        }

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
                String incorrect_answer_1 = new_flashcard_incorrect_answer_1.getText().toString().trim();
                String incorrect_answer_2 = new_flashcard_incorrect_answer_2.getText().toString().trim();
                String incorrect_answer_3 = new_flashcard_incorrect_answer_3.getText().toString().trim();

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
                else if (incorrect_answer_1.length() == 0
                         || incorrect_answer_2.length() == 0
                         || incorrect_answer_3.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Incorrect answer field is empty.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
                else {
                    data.putExtra("question", question);
                    data.putExtra("answer", answer);
                    data.putExtra("incorrect_answer_1", incorrect_answer_1);
                    data.putExtra("incorrect_answer_2", incorrect_answer_2);
                    data.putExtra("incorrect_answer_3", incorrect_answer_3);
                    setResult(RESULT_OK, data);

                    finish();
                }
            }
        });
    }
}
