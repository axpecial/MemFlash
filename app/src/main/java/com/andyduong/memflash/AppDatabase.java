package com.andyduong.memflash;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.andyduong.memflash.Flashcard;
import com.andyduong.memflash.FlashcardDao;

@Database(entities = {Flashcard.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FlashcardDao flashcardDao();
}
