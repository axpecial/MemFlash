package com.andyduong.memflash;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.plattysoft.leonids.ParticleSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // MARK: Request Codes
    private static final int REQUEST_CODE_NEW_CARD = 200;
    private static final int REQUEST_CODE_EDIT_CARD = 300;

    // MARK: Properties
    TextView flashcard_question,
             flashcard_answer,
             flashcard_mc_answer_1,
             flashcard_mc_answer_2,
             flashcard_mc_answer_3,
             flashcard_mc_answer_4,
             empty_state,
             countdown;
    ImageButton show_hint_button,
                hide_hint_button,
                add_card_button,
                edit_card_button,
                prev_card_button,
                next_card_button,
                delete_card_button,
                shuffle_cards_on_button, // TODO: Refactor variable names to on_off for modes
                shuffle_cards_off_button,
                timer_on_button,
                timer_off_button;
    List<Flashcard> allFlashcards;
    List<Flashcard> shuffledFlashcards;
    // NOTE: Array over ArrayList because the size of the list of views should be immutable. This
    // activity indicates the maximum number of incorrect answers that are possible.
    TextView[] allMCAnswers;
    FlashcardDatabase flashcardDatabase;
    CountDownTimer countDownTimer;

    // MARK: States
    int currentCardDisplayedIdx = -1;
    int currentCorrectMCAnswerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Links the XML layout file to this Java file, which handles the user interaction for the
        // corresponding activity
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        flashcard_question = findViewById(R.id.flashcard_question);
        flashcard_answer = findViewById(R.id.flashcard_answer);
        flashcard_mc_answer_1 = findViewById(R.id.flashcard_mc_answer_1);
        flashcard_mc_answer_2 = findViewById(R.id.flashcard_mc_answer_2);
        flashcard_mc_answer_3 = findViewById(R.id.flashcard_mc_answer_3);
        flashcard_mc_answer_4 = findViewById(R.id.flashcard_mc_answer_4);
        empty_state = findViewById(R.id.empty_state);
        countdown = findViewById(R.id.countdown);
        show_hint_button = findViewById(R.id.show_hint_button);
        hide_hint_button = findViewById(R.id.hide_hint_button);
        add_card_button = findViewById(R.id.add_card_button);
        edit_card_button = findViewById(R.id.edit_card_button);
        prev_card_button = findViewById(R.id.previous_card_button);
        next_card_button = findViewById(R.id.next_card_button);
        delete_card_button = findViewById(R.id.delete_card_button);
        shuffle_cards_on_button = findViewById(R.id.shuffle_cards_button);
        shuffle_cards_off_button = findViewById(R.id.shuffle_cards_selected_button);
        timer_on_button = findViewById(R.id.timer_button);
        timer_off_button = findViewById(R.id.timer_selected_button);

        // Initialize external objects
        countDownTimer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                countdown.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {}
        };

        // Initialize data structures
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        shuffledFlashcards = new ArrayList<Flashcard>(allFlashcards);
        Collections.shuffle(shuffledFlashcards);
        // NOTE: Initialize the array after TextView references have been initialized, otherwise, the
        // array will hold null values
        allMCAnswers = new TextView[]{
                flashcard_mc_answer_1,
                flashcard_mc_answer_2,
                flashcard_mc_answer_3,
                flashcard_mc_answer_4,
        };

        // Decide screen to display upon opening the app
        if (allFlashcards.size() == 0) {
            currentCardDisplayedIdx = -1;
            setEmptyState();
        } else {
            showCardAt(0);
        }

        // Setup for animation
        flashcard_question.setCameraDistance(25000);
        flashcard_answer.setCameraDistance(25000);

        //======================= Set onClick listeners =======================
        // TODO: Make separate inner classes for each of these onClicks
        // Set onClick for flashcard question
        flashcard_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcard_question.setCameraDistance(25000);
                flashcard_answer.setCameraDistance(25000);
                flashcard_question.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcard_question.setVisibility(View.INVISIBLE);
                                        flashcard_answer.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcard_answer.setRotationY(-90);
                                        flashcard_answer.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                        // NOTE: Reset the rotation of the hidden side of the card
                                        // because other interactions such as card navigation will
                                        // not be able to see the card as it's stuck in the z-plane.
                                        flashcard_question.setRotationY(0);
                                    }
                                }
                        ).start();
            }
        });

        // Set onClick for flashcard answer
        flashcard_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fix the card flip animation - https://www.thedroidsonroids.com/blog/how-to-add-card-flip-animation-to-your-android-app
                flashcard_answer.animate()
                        .rotationY(-90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        flashcard_answer.setVisibility(View.INVISIBLE);
                                        flashcard_question.setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        flashcard_question.setRotationY(90);
                                        flashcard_question.animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                        // NOTE: Reset the rotation of the hidden side of the card
                                        // because other interactions such as card navigation will
                                        // not be able to see the card as it's stuck in the z-plane.
                                        flashcard_answer.setRotationY(0);
                                    }
                                }
                        ).start();
            }
        });

        // Set onClick for flashcard answer 1 button
        flashcard_mc_answer_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_1);
            }
        });

        // Set onClick for flashcard answer 2 button
        flashcard_mc_answer_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_2);
            }
        });

        // Set onClick for flashcard answer 3 button
        flashcard_mc_answer_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_3);
            }
        });

        // Set onClick for flashcard answer 1 button
        flashcard_mc_answer_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(R.id.flashcard_mc_answer_4);
            }
        });

        // Set onClick for show hint button
        show_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_hint_button.setVisibility(View.INVISIBLE);
                hide_hint_button.setVisibility(View.VISIBLE);
                showMCAnswers();
            }
        });

        // Set onClick for hide hint button
        hide_hint_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_hint_button.setVisibility(View.INVISIBLE);
                show_hint_button.setVisibility(View.VISIBLE);
                hideMCAnswers();
                resetAnswers();
            }
        });

        // Set onClick for add card button
        add_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate from main activity to add card activity
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, REQUEST_CODE_NEW_CARD);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        // Set onClick for edit card button
        edit_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate from main activity to add card activity
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                String question = flashcard_question.getText().toString();
                String answer = flashcard_answer.getText().toString();
                ArrayList<String> incorrectAnswers = new ArrayList<String>();

                // Store the set of incorrect answers.
                for (int i = 0; i < allMCAnswers.length; i++) {
                    TextView mcAnswer = allMCAnswers[i];
                    if (mcAnswer.getVisibility() != View.GONE
                            && mcAnswer.getId() != currentCorrectMCAnswerId) {
                        incorrectAnswers.add(mcAnswer.getText().toString());
                    }
                }

                intent.putExtra("question", question);
                intent.putExtra("answer", answer);
                intent.putStringArrayListExtra("incorrect_answers", incorrectAnswers);

                MainActivity.this.startActivityForResult(intent, REQUEST_CODE_EDIT_CARD);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        // Set onClick for previous card button
        prev_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lower bound check to prevent an IndexOutOfBoundsError from decrementing the index
                if (currentCardDisplayedIdx - 1 < 0) {
                    return;
                }

                // Load animation resource files
                final Animation rightOutAnim =
                        AnimationUtils.loadAnimation(v.getContext(), R.anim.right_out);
                final Animation leftInAnim =
                        AnimationUtils.loadAnimation(v.getContext(), R.anim.left_in);
                flashcard_question.startAnimation(rightOutAnim);

                // TODO: Create a container to apply the animation to the card and navigation buttons
                // TODO: PTAL at the timing for setting the text. It is a UX concern.

                // Set animation listener for right-out animation
                rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flashcard_question.startAnimation(leftInAnim);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // Safe to decrement the index so set the text for question and answer views
                showCardAt(currentCardDisplayedIdx - 1);
            }
        });

        // Set onClick for next card button
        next_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upper bound check to prevent an IndexOutOfBoundsError from incrementing the index
                if (currentCardDisplayedIdx + 1 > allFlashcards.size() - 1) {
                    return;
                }

                // Load animation resource files
                final Animation leftOutAnim =
                        AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim =
                        AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);
                flashcard_question.startAnimation(leftOutAnim);

                // TODO: Create a container to apply the animation to the card and navigation buttons
                // TODO: PTAL at the timing for setting the text. It is a UX concern.

                // Set animation listener for left-out animation
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flashcard_question.startAnimation(rightInAnim);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // Safe to increment index so set the text for question and answer views
                showCardAt(currentCardDisplayedIdx + 1);
            }
        });

        // Set onClick for delete card button
        delete_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCardAt(currentCardDisplayedIdx);

                if (currentCardDisplayedIdx >= 0
                        && currentCardDisplayedIdx == allFlashcards.size()) {
                    currentCardDisplayedIdx--;
                }

                if (allFlashcards.size() > 0) {
                    showCardAt(currentCardDisplayedIdx);
                } else {
                    setEmptyState();
                }
            }
        });

        // Set onClick for shuffle cards on button
        shuffle_cards_on_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle_cards_on_button.setVisibility(View.INVISIBLE);
                shuffle_cards_off_button.setVisibility(View.VISIBLE);
                updateShuffledCards();
                updateCards();
                showCardAt(0);
            }
        });

        // Set onClick for shuffle cards off button
        shuffle_cards_off_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle_cards_off_button.setVisibility(View.INVISIBLE);
                shuffle_cards_on_button.setVisibility(View.VISIBLE);
                updateCards();
                showCardAt(0);
            }
        });

        // Set onClick for timer on button
        timer_on_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timer_on_button.setVisibility(View.INVISIBLE);
                    timer_off_button.setVisibility(View.VISIBLE);
                    startTimer();
                }
        });

        // Set onClick for timer off button
        timer_off_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer_off_button.setVisibility(View.INVISIBLE);
                timer_on_button.setVisibility(View.VISIBLE);
                stopTimer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle data response from add card activity
        if (resultCode == RESULT_OK) {
            // Display appropriate message via snackbars
            // Modify the database accordingly
            if (requestCode == REQUEST_CODE_NEW_CARD) {
                Snackbar.make(findViewById(R.id.flashcard_question),
                        "Card created.",
                        Snackbar.LENGTH_SHORT)
                        .show();

                // Get activity results
                String question = data.getExtras().getString("question");
                String answer = data.getExtras().getString("answer");
                List<String> incorrectAnswers = data.getExtras().getStringArrayList("incorrect_answers");

                // Store new flashcard into the database
                addCard(new Flashcard(question, answer, incorrectAnswers));
            }
            else if (requestCode == REQUEST_CODE_EDIT_CARD) {
                Snackbar.make(findViewById(R.id.flashcard_question),
                        "Card saved.",
                        Snackbar.LENGTH_SHORT)
                        .show();

                // Get activity results
                String question = data.getExtras().getString("question");
                String answer = data.getExtras().getString("answer");
                List<String> incorrectAnswers = data.getExtras().getStringArrayList("incorrect_answers");

                // Edit flashcard in the database
                Flashcard flashcardToEdit = allFlashcards.get(currentCardDisplayedIdx);
                flashcardToEdit.setQuestion(question);
                flashcardToEdit.setAnswer(answer);
                flashcardToEdit.setWrongAnswers(incorrectAnswers);
                flashcardDatabase.updateCard(flashcardToEdit);
            }

            // Update the local list of flashcards
            updateCards();
            // Handle transition out of the empty state
            if (allFlashcards.size() == 1) {
                undoEmptyState();
                currentCardDisplayedIdx = 0;
            }
        }

        // Update the card display
        // Catches the cancel card option
        showCardAt(currentCardDisplayedIdx);
    }


    //======================= Handle empty state =======================
    /**
     * Set the activity to show an empty state when the database is empty.
     */
    private void setEmptyState() {
        flashcard_question.setVisibility(View.INVISIBLE);
        flashcard_question.setVisibility(View.INVISIBLE);
        edit_card_button.setVisibility(View.INVISIBLE);
        delete_card_button.setVisibility(View.INVISIBLE);
        show_hint_button.setVisibility(View.INVISIBLE);
        hide_hint_button.setVisibility(View.INVISIBLE);
        shuffle_cards_on_button.setVisibility(View.INVISIBLE);
        shuffle_cards_off_button.setVisibility(View.INVISIBLE);
        timer_on_button.setVisibility(View.INVISIBLE);
        timer_off_button.setVisibility(View.INVISIBLE);
        countdown.setVisibility(View.INVISIBLE);
        hideMCAnswers();

        empty_state.setVisibility(View.VISIBLE);
    }

    /**
     * Set the activity to revert back to the original state.
     */
    private void undoEmptyState() {
        resetCard();
        resetButtons();
        resetAnswers();

        empty_state.setVisibility(View.INVISIBLE);
    }

    //======================= Handle card navigation =======================
    /**
     * Set the activity to show all of the relevant information for the card at the input index.
     * @param {int} index - index of the card in the database
     */
    private void showCardAt(int index) {
        currentCardDisplayedIdx = index;

        if (index < 0 || index >= allFlashcards.size()) {
            return; // TODO: Throw IndexOutOfBoundsException
        }

        resetCard();
        resetHint();
        resetAnswers();

        flashcard_question.setText(allFlashcards.get(index).getQuestion());
        flashcard_answer.setText(allFlashcards.get(index).getAnswer());
        List<String> wrongAnswers = allFlashcards.get(index).getWrongAnswers();
        // NOTE: Include an extra number for the correct answer
        currentCorrectMCAnswerId = allMCAnswers[getRandomNumber(0, wrongAnswers.size())].getId();

        int wrongAnswerIdx = 0;
        for (int mcAnswerIdx = 0; mcAnswerIdx < allMCAnswers.length; mcAnswerIdx++) {
            TextView mcAnswer = allMCAnswers[mcAnswerIdx];

            // Populate the used mc answer text field(s)
            if (mcAnswerIdx < wrongAnswers.size() + 1) {
                if (mcAnswer.getId() != currentCorrectMCAnswerId) {
                    mcAnswer.setText(wrongAnswers.get(wrongAnswerIdx));
                    wrongAnswerIdx++;
                }
                else {
                    mcAnswer.setText(allFlashcards.get(index).getAnswer());
                }
                mcAnswer.setVisibility(View.INVISIBLE);
            }
            // TODO: Add adaptive layout
            // Remove the unused mc answers from taking up space so the layout can adjust
            else {
                mcAnswer.setVisibility(View.GONE);
            }
        }

        updateCardNavigationButtons();

        if (isTimerOn()) {
            startTimer();
        }
    }

    /**
     * Updates the visibility of the prev and next card buttons depending on the size of the
     * list of cards and the current card index.
     */
    private void updateCardNavigationButtons() {
        // Only consider displaying the buttons if there are multiple cards
        if (allFlashcards.size() == 0) {
            prev_card_button.setVisibility(View.INVISIBLE);
            next_card_button.setVisibility(View.INVISIBLE);
            return;
        }

        // Handle when the prev button appears
        if (currentCardDisplayedIdx > 0) {
            prev_card_button.setVisibility(View.VISIBLE);
        }
        else {
            prev_card_button.setVisibility(View.INVISIBLE);
        }

        // Handle when the next button appears
        if (currentCardDisplayedIdx < allFlashcards.size() - 1) {
            next_card_button.setVisibility(View.VISIBLE);
        }
        else {
            next_card_button.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Updates the current list of cards depending on whether it should be shuffled or in order.
     */
    private void updateCards() {
        allFlashcards = flashcardDatabase.getAllCards();
        if (isShuffled()) {
            allFlashcards = shuffledFlashcards;
        }
    }

    /**
     * Reshuffles the list of flashcards.
     */
    private void updateShuffledCards() {
        shuffledFlashcards = allFlashcards; // TODO: Not sure if I should make a shallow copy yet.
        Collections.shuffle(shuffledFlashcards);
    }

    //======================= Handle card list manipulation =======================
    /**
     * Adds a flashcard to the list and database. This is done to preserve the current shuffling order.
     * @param card - card to add to the currently displayed list
     */
    private void addCard(Flashcard card) {
        flashcardDatabase.insertCard(card);
        allFlashcards.add(card);
    }

    /**
     * Removes a flashcard to the list and database. This is done to preserve the current shuffling order.
     * @param index - index of the card to remove from the currently displayed list
     */
    private void removeCardAt(int index) {
        // TODO: Flashcard is keyed by the question string. There should be an ID instead.
        // TODO: THIS WILL CAUSE A VISUAL BUG if you input multiple questions that have the same question.
        flashcardDatabase.deleteCard(flashcard_question.getText().toString());
        allFlashcards.remove(index);
    }

    //======================= Reset to original state =======================
    /**
     * Resets the main card so that the question is visible and the answer is hidden.
     */
    private void resetCard() {
        flashcard_question.setVisibility(View.VISIBLE);
        flashcard_answer.setVisibility(View.INVISIBLE);
    }

    /**
     * Resets the buttons so that all of the buttons, except hide hint are visible. Card
     * navigation buttons are handle separately.
     */
    private void resetButtons() {
        add_card_button.setVisibility(View.VISIBLE);
        edit_card_button.setVisibility(View.VISIBLE);
        delete_card_button.setVisibility(View.VISIBLE);
        show_hint_button.setVisibility(View.VISIBLE);
        hide_hint_button.setVisibility(View.INVISIBLE);
        shuffle_cards_on_button.setVisibility(View.VISIBLE);
        shuffle_cards_off_button.setVisibility(View.INVISIBLE);
        timer_on_button.setVisibility(View.VISIBLE);
        timer_off_button.setVisibility(View.INVISIBLE);
        updateCardNavigationButtons();
    }

    /**
     * Resets the multiple choice answers so that all of them are unselected.
     */
    private void resetAnswers() {
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
        hideMCAnswers();
    }

    //======================= Handle hints =======================
    /**
     * Show all of the multiple choice answers.
     */
    private void showMCAnswers() {
        if (flashcard_mc_answer_1.getVisibility() != View.GONE) { flashcard_mc_answer_1.setVisibility(View.VISIBLE); }
        if (flashcard_mc_answer_2.getVisibility() != View.GONE) { flashcard_mc_answer_2.setVisibility(View.VISIBLE); }
        if (flashcard_mc_answer_3.getVisibility() != View.GONE) { flashcard_mc_answer_3.setVisibility(View.VISIBLE); }
        if (flashcard_mc_answer_4.getVisibility() != View.GONE) { flashcard_mc_answer_4.setVisibility(View.VISIBLE); }
    }

    /**
     * Hide all of the multiple choice answers.
     */
    private void hideMCAnswers() {
        if (flashcard_mc_answer_1.getVisibility() != View.GONE) { flashcard_mc_answer_1.setVisibility(View.INVISIBLE); }
        if (flashcard_mc_answer_2.getVisibility() != View.GONE) { flashcard_mc_answer_2.setVisibility(View.INVISIBLE); }
        if (flashcard_mc_answer_3.getVisibility() != View.GONE) { flashcard_mc_answer_3.setVisibility(View.INVISIBLE); }
        if (flashcard_mc_answer_4.getVisibility() != View.GONE) { flashcard_mc_answer_4.setVisibility(View.INVISIBLE); }
    }

    /**
     * Resets the hint buttons.
     */
    private void resetHint() {
        show_hint_button.setVisibility(View.VISIBLE);
        hide_hint_button.setVisibility(View.INVISIBLE);
    }

    //======================= Handle shuffle =======================
    /**
     * Determines if the shuffle mode is selected.
     * @return true if shuffle mode is selected, false otherwise.
     */
    private boolean isShuffled() {
        return shuffle_cards_off_button.getVisibility() == View.VISIBLE;
    }

    //======================= Handle timer =======================
    /**
     *  Starts the timer.
     */
    private void startTimer() {
        // Cancel pending timer(s) to prevent multiple timers running
        countDownTimer.cancel();
        countDownTimer.start();
        countdown.setVisibility(View.VISIBLE);
    }

    /**
     *  Stops the timer.
     */
    private void stopTimer() {
        countDownTimer.cancel();
        countdown.setVisibility(View.INVISIBLE);
    }

    /**
     * Determines if the timer mode is selected.
     * @return true if timer mode is selected, false otherwise.
     */
    private boolean isTimerOn() {
        return timer_off_button.getVisibility() == View.VISIBLE;
    }

    //======================= Handle user selection =======================
    /**
     * Highlights the selected answer and the correct answer
     * @param {int} id - id of the multiple choice answer that the user selected
     */
    private void checkAnswer(int id) {
        // If the user selects an incorrect answer, highlight it
        if (this.currentCorrectMCAnswerId != id) {
            findViewById(id).setBackgroundResource(R.drawable.card_background_incorrect);
            ((TextView) findViewById(id)).setTextColor(
                    getResources().getColor(R.color.bsWhite, null)
            );
        }
        // Display confetti if the user selects the correct answer
        else {
            new ParticleSystem(MainActivity.this, 100, R.drawable.confetti, 3000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(findViewById(this.currentCorrectMCAnswerId), 100);
        }

        // Always highlight the correct answer
        findViewById(this.currentCorrectMCAnswerId).setBackgroundResource(R.drawable.card_background_correct);
        ((TextView) findViewById(this.currentCorrectMCAnswerId)).setTextColor(
                getResources().getColor(R.color.bsWhite, null)
        );
    }

    /**
     * Returns a random number between minNumber and maxNumber, inclusive.
     * @author Codepath
     * @param {int} minNumber - minimum number
     * @param {int} maxNumber - maximum number
     */
    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }
}