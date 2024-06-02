package com.example.noteit.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.noteit.entities.Note;

import java.util.List;


import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;


@Dao
public interface NoteDao {



    @Query("SELECT * FROM notes_table")
    Observable <List<Note>> getNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);

    @Delete
    Completable deleteNote(Note note);

    @Update
    Completable updateNote(Note note);


}
