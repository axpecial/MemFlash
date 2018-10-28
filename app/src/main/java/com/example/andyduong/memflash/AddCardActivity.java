package com.example.andyduong.memflash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


public class AddCardActivity extends AppCompatActivity {

    // MARK: Properties
    EditText    new_flashcard_question,
                new_flashcard_answer;
    ImageButton cancel_card_button,
                save_card_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        new_flashcard_question = findViewById(R.id.new_flashcard_question);
        new_flashcard_answer   = findViewById(R.id.new_flashcard_answer);
        cancel_card_button     = findViewById(R.id.cancel_card_button);
        save_card_button       = findViewById(R.id.save_card_button);

        // Sets onClick for add card button
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
                String question = new_flashcard_question.getText().toString();
                String answer = new_flashcard_answer.getText().toString();

                data.putExtra("question", question);
                data.putExtra("answer", answer);
                setResult(RESULT_OK, data);

                finish();
            }
        });
    }
}
