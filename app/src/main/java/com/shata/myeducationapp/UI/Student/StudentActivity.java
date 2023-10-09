package com.shata.myeducationapp.UI.Student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shata.myeducationapp.Adapter.Adapter_All_Student;
import com.shata.myeducationapp.Model.ModelLecuter.ModelLecuter;
import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.UI.Lecuter.LectuerActivity;
import com.shata.myeducationapp.databinding.ActivityStudentBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements Adapter_All_Student.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActivityStudentBinding studentBinding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseStudentRef = database.getReference("Students");
    ValueEventListener mDBListener;
    List<ModelStudent> studentList;
    Adapter_All_Student adapterAllStudent;
    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityStudentBinding.inflate(getLayoutInflater());
        setContentView(studentBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.student_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        Initialize_variables();
        loadingAllStudent();
    }

    private void Initialize_variables() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        studentBinding.recyclerViewStudent.setHasFixedSize(true);
        studentBinding.recyclerViewStudent.setLayoutManager(new LinearLayoutManager(this));
        studentList = new ArrayList<>();
        adapterAllStudent = new Adapter_All_Student(this, studentList);
        studentBinding.recyclerViewStudent.setAdapter(adapterAllStudent);
        adapterAllStudent.setOnItemClickListener(this);
    }

    public void loadingAllStudent() {
        studentBinding.progressCircular.setVisibility(View.VISIBLE);
        mDBListener = mDatabaseStudentRef
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        studentList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot != null) {
                                ModelStudent student = snapshot.getValue(ModelStudent.class);
                                studentList.add(student);
                            }
                        }
                        adapterAllStudent.notifyDataSetChanged();
                        studentBinding.progressCircular.setVisibility(View.GONE);
                        if (studentList.isEmpty())
                            studentBinding.studentRecyclerviewImage.setVisibility(View.VISIBLE);
                        else
                            studentBinding.studentRecyclerviewImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("ExamsActivity", error.getMessage());
                    }
                });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            loadingAllStudent();
        }, 1000);
    }

    @Override
    public void onItem_RecyclerView_Click(int position) {
        ModelStudent student= studentList.get(position);
        Intent intentViewExam = new Intent(this, StudentSelectActivity.class);
        intentViewExam.putExtra("ModelStudent", student);
        startActivity(intentViewExam);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseStudentRef.removeEventListener(mDBListener);
        studentList.clear();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}