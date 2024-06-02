package com.example.noteit.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteit.DataBase.NotesDataBase;
import com.example.noteit.R;
import com.example.noteit.entities.Note;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateNoteActivity extends AppCompatActivity {

    private ImageView backIcon, saveIcon, defaultImage, yellowImage, bleuImage, blackImage, redImage;

    private TextView textDateTime, webLinkTxt;
    private EditText notesTitleEdit, subTitleEdit, noteBody;
    private String selectedColor;
    private ImageView selectedImage, imageNote, deleteImage, deleteWebLink;
    private String imagePath;
    private View view;
    private GradientDrawable shapeDrawable;
    private LinearLayout addImageLayout, addWebLinkLayout, deleteNote , chooseColor;
    private final static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ConstraintLayout container;
    private ScrollView scrollBar;
    private Note noteAlreadyExist;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        init();
        i = getIntent().getIntExtra("pos", 0);
        if (i == -1) {
        } else {
            noteAlreadyExist = (Note) getIntent().getSerializableExtra("Note");
            settingNoteContent();
        }

        loadAnimation();
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textDateTime.setText(getTime());
        saveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
        yellowImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                selectedColor = "#FDBE3B";
                selectedImage.setImageDrawable(null);
                yellowImage.setImageDrawable(getDrawable(R.drawable.done_icon));
                selectedImage = yellowImage;
                shapeDrawable.setColor(getColor(R.color.colorNote2));
                changeStatesColor(R.color.colorNote2);

            }
        });
        redImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                selectedColor = "#FF4842";
                selectedImage.setImageDrawable(null);
                redImage.setImageDrawable(getDrawable(R.drawable.done_icon));
                selectedImage = redImage;
                shapeDrawable.setColor(getColor(R.color.colorNote3));
                changeStatesColor(R.color.colorNote3);
            }
        });
        blackImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                selectedColor = "#FF000000";
                selectedImage.setImageDrawable(null);
                blackImage.setImageDrawable(getDrawable(R.drawable.done_icon));
                selectedImage = blackImage;
                shapeDrawable.setColor(getColor(R.color.colorNote5));
                changeStatesColor(R.color.colorNote5);
            }
        });
        defaultImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                selectedColor = "#333333";
                selectedImage.setImageDrawable(null);
                defaultImage.setImageDrawable(getDrawable(R.drawable.done_icon));
                selectedImage = defaultImage;
                shapeDrawable.setColor(getColor(R.color.colorDefaultNote));
                changeStatesColor(R.color.colorDefaultNote);

            }
        });
        bleuImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                selectedColor = "#3A52FC";
                selectedImage.setImageDrawable(null);
                bleuImage.setImageDrawable(getDrawable(R.drawable.done_icon));
                selectedImage = bleuImage;
                shapeDrawable.setColor(getColor(R.color.colorNote4));
                changeStatesColor(R.color.colorNote4);


            }
        });
        addImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readImageFromStorage();
            }
        });
        addWebLinkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        webLinkTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = webLinkTxt.getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        deleteWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webLinkTxt.setText("");
                webLinkTxt.setVisibility(View.GONE);
                deleteWebLink.setVisibility(View.GONE);
            }
        });
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageNote.setImageURI(null);
                deleteImage.setVisibility(View.GONE);
                imageNote.setVisibility(View.GONE);
                imagePath = null;
            }
        });
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
            }
        });
    }


    private void init() {
        imageNote = findViewById(R.id.imageNote);
        chooseColor = findViewById(R.id.chooseColor);
        deleteNote = findViewById(R.id.deleteNote);
        deleteWebLink = findViewById(R.id.deleteWebLink);
        deleteImage = findViewById(R.id.deleteImage);
        deleteImage.setVisibility(View.INVISIBLE);
        deleteWebLink.setVisibility(View.INVISIBLE);
        container = findViewById(R.id.container);
        scrollBar = findViewById(R.id.scrollBar);
        webLinkTxt = findViewById(R.id.webLinkTxt);
        addWebLinkLayout = findViewById(R.id.addWebLinkLayout);
        addImageLayout = findViewById(R.id.addImageLayout);
        view = findViewById(R.id.view);
        shapeDrawable = (GradientDrawable) view.getBackground();
        backIcon = findViewById(R.id.backIcon);
        saveIcon = findViewById(R.id.saveIcon);
        textDateTime = findViewById(R.id.textDateTime);
        notesTitleEdit = findViewById(R.id.notesTitleEdit);
        subTitleEdit = findViewById(R.id.subTitleEdit);
        noteBody = findViewById(R.id.noteBody);
        defaultImage = findViewById(R.id.defaultImage);
        yellowImage = findViewById(R.id.yellowImage);
        bleuImage = findViewById(R.id.bleuImage);
        blackImage = findViewById(R.id.blackImage);
        redImage = findViewById(R.id.redImage);
        selectedImage = defaultImage;
        selectedColor = "#333333";
        shapeDrawable.setColor(getColor(R.color.colorDefaultNote));
        changeStatesColor(R.color.colorDefaultNote);
        imagePath = "";

    }

    private String getTime() {
        return new SimpleDateFormat("EEEE , dd , MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date());
    }

    private boolean checkFields() {
        if (notesTitleEdit.getText().toString().isEmpty()) {
            return false;
        } else
            return !subTitleEdit.getText().toString().isEmpty() || !noteBody.getText().toString().isEmpty();
    }

    private void saveNote() {
        if (i == -1) {
            if (checkFields()) {
                Note note = new Note();
                note.setNoteText(noteBody.getText().toString());
                note.setTitle(notesTitleEdit.getText().toString());
                note.setDateTime(textDateTime.getText().toString());
                note.setSubTitle(subTitleEdit.getText().toString());
                note.setColor(selectedColor);
                note.setImagePath(imagePath);
                if (webLinkTxt.getVisibility() == View.VISIBLE) {
                    note.setWebLink(webLinkTxt.getText().toString());
                }
                NotesDataBase notesDataBase = NotesDataBase.getInstance(this);
                if (i == -1) {
                    notesDataBase.noteDao().insertNote(note).subscribeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
                } else {
                    Toast.makeText(this, "gonna update", Toast.LENGTH_SHORT).show();
                    updateNote();


                }
            }
        } else {
            confirmation();
        }
    }

    @Override
    public void onBackPressed() {
        if (i == -1) {
            Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            confirmation();
        }
    }

    private void changeStatesColor(int id) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(id, getApplicationContext().getTheme()));
        }
    }


    private void readImageFromStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
            }
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            Toast.makeText(this, "Permission denied ! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                imageNote.setImageURI(imageUri);
                imageNote.setVisibility(View.VISIBLE);
                imagePath = getFilePathFromUri(this, imageUri);
            }
        }
    }

    private String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }


    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_link);
        dialog.setTitle(null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        EditText editText = dialog.findViewById(R.id.noteLink);
        TextView addTxt = dialog.findViewById(R.id.addTxt);
        TextView cancelTxt = dialog.findViewById(R.id.cancelTxt);
        addTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(CreateNoteActivity.this, R.string.please_entre_a_weblink, Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(text).matches()) {
                    Toast.makeText(CreateNoteActivity.this, R.string.please_enter_a_valid_weblink, Toast.LENGTH_SHORT).show();
                } else {
                    webLinkTxt.setText(text);
                    webLinkTxt.setVisibility(View.VISIBLE);
                    editText.clearFocus();
                    hideKeyboard();
                    dialog.dismiss();
                }
            }
        });
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                editText.clearFocus();
                hideKeyboard();
            }
        });
        dialog.show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(container.getWindowToken(), 0);
    }

    private void settingNoteContent() {
        notesTitleEdit.setText(noteAlreadyExist.getTitle());
        textDateTime.setText(noteAlreadyExist.getDateTime());
        if (noteAlreadyExist.getSubTitle() != null) {
            subTitleEdit.setText(noteAlreadyExist.getSubTitle());
        }
        if (noteAlreadyExist.getWebLink() != null) {
            webLinkTxt.setText(noteAlreadyExist.getWebLink());
            webLinkTxt.setVisibility(View.VISIBLE);
            deleteWebLink.setVisibility(View.VISIBLE);
        }
        if (noteAlreadyExist.getNoteText() != null) {
            noteBody.setText(noteAlreadyExist.getNoteText());
        }
        if (noteAlreadyExist.getImagePath() != null) {
            if (!noteAlreadyExist.getImagePath().equals("")) {
                imagePath = noteAlreadyExist.getImagePath();
                imageNote.setImageURI(Uri.fromFile(new File(noteAlreadyExist.getImagePath())));
                imageNote.setVisibility(View.VISIBLE);
                deleteImage.setVisibility(View.VISIBLE);
            }
        }
        shapeDrawable.setColor(Color.parseColor(noteAlreadyExist.getColor()));
        switch (noteAlreadyExist.getColor()) {
            case "#FDBE3B":
                updateChooserColor(yellowImage);
                selectedImage = yellowImage;
                selectedColor = "#FDBE3B";
                break;
            case "#FF4842":
                updateChooserColor(redImage);
                selectedImage = redImage;
                selectedColor = "#FF4842";

                break;
            case "#FF000000":
                updateChooserColor(blackImage);
                selectedImage = blackImage;
                selectedColor = "#FF000000";

                break;
            case "#333333":
                updateChooserColor(defaultImage);
                selectedImage = defaultImage;
                selectedColor = "#333333";

                break;
            case "#3A52FC":
                updateChooserColor(bleuImage);
                selectedImage = bleuImage;
                selectedColor = "#3A52FC";
                break;

        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateChooserColor(ImageView img) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(noteAlreadyExist.getColor()));
        }
        selectedImage.setImageDrawable(null);
        img.setImageDrawable(getDrawable(R.drawable.done_icon));
        shapeDrawable.setColor(Color.parseColor(noteAlreadyExist.getColor()));
    }
    private void updateNote() {
        Note note = new Note();
        note.setNoteText(noteBody.getText().toString());
        note.setTitle(notesTitleEdit.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setSubTitle(subTitleEdit.getText().toString());
        note.setColor(selectedColor);
        note.setImagePath(imagePath);
        note.setId(noteAlreadyExist.getId());
        if (webLinkTxt.getVisibility() == View.VISIBLE) {
            note.setWebLink(webLinkTxt.getText().toString());
        }
        NotesDataBase notesDataBase = NotesDataBase.getInstance(this);
        notesDataBase.noteDao().updateNote(note).subscribeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onComplete() {
                Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    private void confirmation() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.onback_confi_dialog);
        dialog1.setTitle(null);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog1.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        TextView saveTxt = dialog1.findViewById(R.id.saveTxt);
        TextView cancelTxt = dialog1.findViewById(R.id.cancelTxt);
        saveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
                dialog1.dismiss();
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    private void deleteConfirmation() {


        Dialog dialog1 = new Dialog(this);
        dialog1.setContentView(R.layout.delete_confi_dialog);
        dialog1.setTitle(null);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog1.getWindow();
        if (window != null) {
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        TextView deleteTxt = dialog1.findViewById(R.id.deleteTxt);
        TextView cancelTxt = dialog1.findViewById(R.id.cancelTxt);
        deleteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
                dialog1.dismiss();
            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    private void deleteNote() {
        NotesDataBase notesDataBase = NotesDataBase.getInstance(this);
        notesDataBase.noteDao().deleteNote(noteAlreadyExist).subscribeOn(Schedulers.computation()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onComplete() {
                Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }


    private void loadAnimation(){
        Animation slideLeft = AnimationUtils.loadAnimation(this , R.anim.slide_from_left);
        Animation slideRight = AnimationUtils.loadAnimation(this , R.anim.slid_from_right);
        Animation zoomOut = AnimationUtils.loadAnimation(this , R.anim.zoom_out);
        Animation slideBottom = AnimationUtils.loadAnimation(this , R.anim.slid_from_bottom);
        backIcon.setAnimation(slideLeft);
        saveIcon.setAnimation(slideRight);
        notesTitleEdit.setAnimation(zoomOut);
        textDateTime.setAnimation(zoomOut);
        subTitleEdit.setAnimation(zoomOut);
        noteBody.setAnimation(zoomOut);
        imageNote.setAnimation(slideBottom);

        webLinkTxt.setAnimation(slideLeft);

        chooseColor.setAnimation(slideBottom);
        slideLeft.start();
        slideRight.start();
        zoomOut.start();
        slideBottom.start();
    }
}