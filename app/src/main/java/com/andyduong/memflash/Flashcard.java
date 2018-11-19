package com.andyduong.memflash;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class Flashcard {

    /* Public ctor that matches all params the database. Required by the library. */
    Flashcard(String question, String answer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.uuid = UUID.randomUUID().toString();
        this.question = question;
        this.answer = answer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
    }

    @Ignore
    Flashcard(String question, String answer, List<String> wrongAnswers) {
        this.uuid = UUID.randomUUID().toString();
        this.question = question;
        this.answer = answer;
        setWrongAnswers(wrongAnswers);
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uuid")
    private String uuid;

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @NonNull
    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "answer")
    private String answer;

    @Nullable
    @ColumnInfo(name = "wrong_answer_1")
    private String wrongAnswer1;

    @Nullable
    @ColumnInfo(name = "wrong_answer_2")
    private String wrongAnswer2;

    @Nullable
    @ColumnInfo(name = "wrong_answer_3")
    private String wrongAnswer3;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getWrongAnswers() {
        ArrayList<String> wrongAnswers = new ArrayList<>();
        if (this.wrongAnswer1 != null) { wrongAnswers.add(this.wrongAnswer1); }
        if (this.wrongAnswer2 != null) { wrongAnswers.add(this.wrongAnswer2); }
        if (this.wrongAnswer3 != null) { wrongAnswers.add(this.wrongAnswer3); }
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {

        if (wrongAnswers.size() > 0) { this.wrongAnswer1 = wrongAnswers.get(0); }
        else { this.wrongAnswer1 = null; }
        if (wrongAnswers.size() > 1) { this.wrongAnswer2 = wrongAnswers.get(1); }
        else { this.wrongAnswer2 = null; }
        if (wrongAnswers.size() > 2) { this.wrongAnswer3 = wrongAnswers.get(2); }
        else { this.wrongAnswer3 = null; }
    }

    @Nullable
    public String getWrongAnswer1() { return wrongAnswer1; }

    public void setWrongAnswer1(String wrongAnswer1) { this.wrongAnswer1 = wrongAnswer1; }

    @Nullable
    public String getWrongAnswer2() { return wrongAnswer2; }

    public void setWrongAnswer2(String wrongAnswer2) { this.wrongAnswer2 = wrongAnswer2; }

    @Nullable
    public String getWrongAnswer3() { return wrongAnswer3; }

    public void setWrongAnswer3(String wrongAnswer3) { this.wrongAnswer3 = wrongAnswer3; }
}