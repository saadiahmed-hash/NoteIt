package com.example.noteit.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.noteit.Adapters.NotesAdapter;
import com.example.noteit.DataBase.NotesDataBase;
import com.example.noteit.R;
import com.example.noteit.entities.Note;
import com.example.noteit.entities.NotificationUtils;
import com.example.noteit.entities.YourBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "channelID";
    public static final int CODE = 1;
    private List<Note> myNotes;
    private ImageView addNoteBtn, paramsImage;

    private RecyclerView notesRecycler;
    private NotesAdapter notesAdapter;
    private EditText searchNoteEdit;
    private TextView allTxt, myNotesText;
    private CardView defaultCard, yellowCard, redCard, blueCard, blackCard;
    private ArrayList<Note> notesSearched;

    private String filterType;
    private String searchedWord;

    private LottieAnimationView empty;

    private LinearLayout searchLayout, filterLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary, getApplicationContext().getTheme()));
        }

        loadAnimation();

        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateNoteActivity.class);
                i.putExtra("pos", -1);
                startActivityForResult(i, CODE);
                finish();
            }
        });
        getNotesList();

        searchNoteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchedWord = editable.toString();
                updateSearch(editable.toString());
            }
        });
        allTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "all";
                searchedWord = "";
                updateSearch(searchedWord);
            }
        });
        redCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "#FF4842";
                updateSearch(searchedWord);
            }
        });
        yellowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "#FDBE3B";
                updateSearch(searchedWord);
            }
        });
        defaultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "#333333";
                updateSearch(searchedWord);
            }
        });
        blackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "#FF000000";
                updateSearch(searchedWord);
            }
        });
        blueCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterType = "#3A52FC";
                updateSearch(searchedWord);
            }
        });

        paramsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                finish();
            }
        });
        myNotesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });
    }

    public void openTimePicker(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Calendar chosenTime = Calendar.getInstance();
                chosenTime.set(Calendar.HOUR , i);
                chosenTime.set(Calendar.MINUTE , i1);
                scheduleFunctionExecution(getApplicationContext() , chosenTime);


            }
        }, hour, min, false);
        timePickerDialog.show();
    }

    private void scheduleFunctionExecution(Context context, Calendar chosenTime) {
        Intent intent = new Intent(context, YourBroadcastReceiver.class); // Replace YourBroadcastReceiver with the BroadcastReceiver class that will execute your function
        intent.putExtra("id" , "this is a string");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Set the alarm to run at the chosen time
        }
    }



    private void displayNotif() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("this is my notification title")
                .setContentText("this is my notification description")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(getColor(R.color.colorNote3))
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(0, notificationBuilder.build());
        }


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="myChannelName";
            String description = "channel desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }


    private void init(){
        filterType = "all";
        searchedWord = "";
        notesSearched = new ArrayList<>();
        defaultCard = findViewById(R.id.defaultCard);
        paramsImage = findViewById(R.id.paramsImage);
        searchLayout = findViewById(R.id.searchLayout);
        myNotesText = findViewById(R.id.myNotesText);
        filterLayout = findViewById(R.id.filterLayout);
        yellowCard = findViewById(R.id.yellowCard);
        redCard = findViewById(R.id.redCard);
        blueCard = findViewById(R.id.blueCard);
        blackCard = findViewById(R.id.blackCard);
        allTxt = findViewById(R.id.allTxt);
        addNoteBtn = findViewById(R.id.addNoteBtn);
        searchNoteEdit = findViewById(R.id.searchNoteEdit);
        notesRecycler = findViewById(R.id.notesRecycler);
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
        myNotes = new ArrayList<>();
        notesAdapter = new NotesAdapter(this , myNotes);
        notesRecycler.setAdapter(notesAdapter);
        empty   = findViewById(R.id.empty);
        empty.setAnimation(R.raw.empty); // Set the animation source
        empty.loop(true); // Set loop mode
        empty.playAnimation();
        empty.setVisibility(View.GONE);
    }

    private void getNotesList(){
        NotesDataBase notesDataBase = NotesDataBase.getInstance(this);
        notesDataBase.noteDao().getNotes().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Note>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onNext(@NonNull List<Note> notes) {
                notesAdapter.setNotes(notes);
                if (notes.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
                notesAdapter.notifyDataSetChanged();
                myNotes= notes;
            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateSearch(String searchedWord){
        notesSearched.clear();
        for (Note note: myNotes) {
            if (note.getTitle().toLowerCase().contains(searchedWord.toLowerCase()) ||
                    note.getSubTitle().toLowerCase().contains(searchedWord.toLowerCase())
            ){
                notesSearched.add(note);
            }
        }
        filterSearch(filterType);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterSearch(String type){
        ArrayList<Note> filtredArr = new ArrayList<>();
        filtredArr.clear();
        if (type.equals("all")){
            notesAdapter.setNotes(notesSearched);
            if (notesSearched.isEmpty()){
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        } else {
            for (Note note : notesSearched) {
                if (note.getColor().equals(type)){
                    filtredArr.add(note);
                }
            }
            notesAdapter.setNotes(filtredArr);
            if (filtredArr.isEmpty()){
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        }
        notesAdapter.notifyDataSetChanged();
    }
    private void loadAnimation(){
        Animation slideLeft = AnimationUtils.loadAnimation(this , R.anim.slide_from_left);
        Animation slideRight = AnimationUtils.loadAnimation(this , R.anim.slid_from_right);
        Animation zoomOut = AnimationUtils.loadAnimation(this , R.anim.zoom_out);
        Animation slideBottom = AnimationUtils.loadAnimation(this , R.anim.slid_from_bottom);
        myNotesText.setAnimation(slideLeft);
        paramsImage.setAnimation(slideRight);
        searchLayout.setAnimation(zoomOut);
        filterLayout.setAnimation(zoomOut);
        addNoteBtn.setAnimation(slideBottom);

        slideLeft.start();
        slideRight.start();
        zoomOut.start();
        slideBottom.start();
    }
}