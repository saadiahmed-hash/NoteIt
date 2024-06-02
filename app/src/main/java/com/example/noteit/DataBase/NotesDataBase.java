package com.example.noteit.DataBase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteit.Dao.NoteDao;
import com.example.noteit.entities.Note;

@Database(entities = Note.class , version = 1 , exportSchema = false)
public abstract class NotesDataBase extends RoomDatabase {

    public static NotesDataBase instance ;
    public abstract NoteDao noteDao() ;

    public static synchronized NotesDataBase getInstance(Context context){
        if(instance == null){

            instance = Room.databaseBuilder(context.getApplicationContext() , NotesDataBase.class , "note_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance ;
    }
}
