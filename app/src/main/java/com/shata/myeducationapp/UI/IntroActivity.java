package com.shata.myeducationapp.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.MainActivity;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.UI.Auth.LoginActivity;

public class IntroActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        firebaseAuth = FirebaseAuth.getInstance();
        Thread intro = new Thread(() -> {
            // Sleep UI 3 Seconds
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (firebaseAuth.getCurrentUser() != null) {
                //data is valid
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
                finish();
            }
        });
        intro.start();

    }
}