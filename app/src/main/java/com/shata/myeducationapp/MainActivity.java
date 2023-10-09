package com.shata.myeducationapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.UI.Auth.LoginActivity;
import com.shata.myeducationapp.UI.Exam.ExamsActivity;
import com.shata.myeducationapp.UI.HomeWork.AllHomeWorkActivity;
import com.shata.myeducationapp.UI.Lecuter.AllLectuersActivity;
import com.shata.myeducationapp.UI.Student.StudentActivity;
import com.shata.myeducationapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //

        mainBinding.exams.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExamsActivity.class));
        });
        mainBinding.homework.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AllHomeWorkActivity.class));
        });
        mainBinding.lectuers.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AllLectuersActivity.class));
        });
        mainBinding.student.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StudentActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}