package com.shata.myeducationapp.UI.Student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.R;

public class StudentSelectActivity extends AppCompatActivity {

    ModelStudent student;
    Button studentExam , studentHomeWork;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_select);

        student = (ModelStudent) getIntent().getSerializableExtra("ModelStudent");
        studentExam = findViewById(R.id.exams);
        studentHomeWork = findViewById(R.id.homework);

        studentExam.setOnClickListener(v->{
            Intent intentViewExam = new Intent(this, StudentExamActivity.class);
            intentViewExam.putExtra("ModelStudent", student);
            startActivity(intentViewExam);
        });

        studentHomeWork.setOnClickListener(v->{
            Intent intentViewExam = new Intent(this, StudentHomeWorkActivity.class);
            intentViewExam.putExtra("ModelStudent", student);
            startActivity(intentViewExam);
        });
    }
}