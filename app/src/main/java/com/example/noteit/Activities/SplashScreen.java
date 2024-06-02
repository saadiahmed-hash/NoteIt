package com.example.noteit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.noteit.R;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        LoadLocal();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary,getApplicationContext().getTheme()));
        }

        LottieAnimationView animationView = findViewById(R.id.noteLottie);
        animationView.setAnimation(R.raw.note_lottie); // Set the animation source
        animationView.loop(true); // Set loop mode
        animationView.playAnimation();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    startActivity(new Intent(SplashScreen.this , MainActivity.class));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
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
}