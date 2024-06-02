package com.example.noteit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.noteit.R;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout arabicLayout , englishLayout ;
    private ImageView gmailIcon , instagramIcon ;
    private TextView contactTxt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary,getApplicationContext().getTheme()));
        }
        init();
        loadAnimation();

        arabicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("ar");
                recreate();

            }
        });
        englishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                recreate();

            }
        });

        gmailIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "";
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject"); // Optional: Specify the email subject
                intent.putExtra(Intent.EXTRA_TEXT, "Body"); // Optional: Specify the email body
                startActivity(intent);
            }
        });

        instagramIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
        private void init(){
        arabicLayout = findViewById(R.id.arabicLayout);
        englishLayout = findViewById(R.id.englishLayout);
        instagramIcon = findViewById(R.id.instagramIcon);
        gmailIcon = findViewById(R.id.gmailIcon);
        contactTxt = findViewById(R.id.contactTxt);
    }

    private void setLocale(String ar) {
        Locale locale = new Locale(ar);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale ;
        getBaseContext().getResources().updateConfiguration(configuration , getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings" , MODE_PRIVATE ).edit();
        editor.putString("My_Lang" , ar);
        editor.apply();
    }

    public void LoadLocal(){
        SharedPreferences per = getSharedPreferences("Settings" , Activity.MODE_PRIVATE);
        String lang = per.getString("My_Lang" , "");
        setLocale(lang);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this , MainActivity.class));
    }

    private void loadAnimation(){
        Animation slideLeft = AnimationUtils.loadAnimation(this , R.anim.slide_from_left);
        Animation slideRight = AnimationUtils.loadAnimation(this , R.anim.slid_from_right);
        Animation zoomOut = AnimationUtils.loadAnimation(this , R.anim.zoom_out);
        Animation slideBottom = AnimationUtils.loadAnimation(this , R.anim.slid_from_bottom);
        englishLayout.setAnimation(slideLeft);
        arabicLayout.setAnimation(slideRight);
        instagramIcon.setAnimation(slideBottom);
        gmailIcon.setAnimation(slideBottom);
        contactTxt.setAnimation(slideBottom);

        slideLeft.start();
        slideRight.start();
        zoomOut.start();
        slideBottom.start();
    }
}