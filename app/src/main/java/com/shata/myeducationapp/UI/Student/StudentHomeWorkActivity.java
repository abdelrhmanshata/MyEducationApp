package com.shata.myeducationapp.UI.Student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.shata.myeducationapp.Adapter.Adapter_Student_HomeWork;
import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.Model.Student.ModelStudentHomeWork;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityStudentHomeWorkBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeWorkActivity extends AppCompatActivity implements Adapter_Student_HomeWork.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ModelStudent student;
    ActivityStudentHomeWorkBinding studentHomeWorkBinding;
    List<ModelStudentHomeWork> studentHomeWorks;
    List<ModelStudentHomeWork> professorHomeWorks;
    Adapter_Student_HomeWork adapterStudentHomeWork;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentHomeWorkBinding = ActivityStudentHomeWorkBinding.inflate(getLayoutInflater());
        setContentView(studentHomeWorkBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        student = (ModelStudent) getIntent().getSerializableExtra("ModelStudent");

        studentHomeWorks = student.getStudentHomeWorks();
        professorHomeWorks = new ArrayList<>();

        for(int i=0;i<studentHomeWorks.size();i++)
        {
            if(studentHomeWorks.get(i).getProfessorID().equals(mAuth.getUid()))
            {
                professorHomeWorks.add(studentHomeWorks.get(i));
            }
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        studentHomeWorkBinding.recyclerViewStudent.setHasFixedSize(true);
        studentHomeWorkBinding.recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));
        adapterStudentHomeWork = new Adapter_Student_HomeWork(this, professorHomeWorks);
        studentHomeWorkBinding.recyclerViewStudent.setAdapter(adapterStudentHomeWork);
        adapterStudentHomeWork.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            studentHomeWorks = student.getStudentHomeWorks();
            adapterStudentHomeWork.notifyDataSetChanged();
        }, 1000);
    }

    @Override
    public void onItem_RecyclerView_Click(int position) {
        ModelStudentHomeWork homeWork = studentHomeWorks.get(position);
        downloadPdfHomeWork(homeWork);
    }

    @SuppressLint("IntentReset")
    private void downloadPdfHomeWork(ModelStudentHomeWork work) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        intent.setData(Uri.parse(work.getHomeworkPDFLink()));
        startActivity(intent);
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