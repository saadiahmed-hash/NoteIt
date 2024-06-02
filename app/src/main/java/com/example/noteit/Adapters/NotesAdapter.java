package com.example.noteit.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteit.Activities.CreateNoteActivity;
import com.example.noteit.R;
import com.example.noteit.ViewHolders.noteViewHolder;
import com.example.noteit.entities.Note;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<noteViewHolder> {

    Context context ;
    List<Note> notes ;
    Animation zoom_out  ;
    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        zoom_out = AnimationUtils.loadAnimation(context , R.anim.zoom_out);

    }

    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new noteViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_item , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Note note = notes.get(position);
        holder.noteLayout.setAnimation(zoom_out);

        GradientDrawable gradientDrawable= (GradientDrawable) holder.noteLayout.getBackground() ;
        holder.titleTxt.setText(note.getTitle());
        if (note.getColor() !=null){
            gradientDrawable.setColor(Color.parseColor(note.getColor()));
        }
        if (note.getSubTitle().trim().isEmpty()){
            holder.subTitleTxt.setVisibility(View.GONE);
        } else {
            holder.subTitleTxt.setText(note.getSubTitle());
        }
        holder.timeDateTxt.setText(note.getDateTime());
        holder.noteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context , CreateNoteActivity.class);
                i.putExtra("Note" , note);
                i.putExtra("pos" , position);
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
