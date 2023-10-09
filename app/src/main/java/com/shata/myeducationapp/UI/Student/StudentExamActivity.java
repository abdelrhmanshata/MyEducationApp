package com.shata.myeducationapp.UI.Student;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.Adapter.Adapter_Student_Exam;
import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.Model.Student.ModelStudentExam;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityStudentExamBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentExamActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityStudentExamBinding studentExamBinding;
    ModelStudent student;
    List<ModelStudentExam> studentExamList;
    List<ModelStudentExam> professorExamList;
    Adapter_Student_Exam adapterStudentExam;
    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentExamBinding = ActivityStudentExamBinding.inflate(getLayoutInflater());
        setContentView(studentExamBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        student = (ModelStudent) getIntent().getSerializableExtra("ModelStudent");

        studentExamList = student.getStudentExams();
        professorExamList = new ArrayList<>();

        for (int i = 0; i < studentExamList.size(); i++) {
            if (studentExamList.get(i).getProfessorID().equals(mAuth.getUid())) {
                professorExamList.add(studentExamList.get(i));
            }
        }


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        studentExamBinding.recyclerViewStudent.setHasFixedSize(true);
        studentExamBinding.recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));
        adapterStudentExam = new Adapter_Student_Exam(this, professorExamList);
        studentExamBinding.recyclerViewStudent.setAdapter(adapterStudentExam);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            studentExamList = student.getStudentExams();
            adapterStudentExam.notifyDataSetChanged();
        }, 1000);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}