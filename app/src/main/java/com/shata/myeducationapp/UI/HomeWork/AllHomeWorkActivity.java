package com.shata.myeducationapp.UI.HomeWork;

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
import com.shata.myeducationapp.Adapter.Adapter_All_HomeWorks;
import com.shata.myeducationapp.Model.ModelHomeWork.ModelHomeWork;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityAllHomeWorkBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllHomeWorkActivity extends AppCompatActivity implements Adapter_All_HomeWorks.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    ActivityAllHomeWorkBinding allHomeWorkBinding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseHomeWorkRef = database.getReference("HomeWorks");

    ValueEventListener mDBListener;
    List<ModelHomeWork> homeworksList;
    Adapter_All_HomeWorks adapterAllHomeWorks;
    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allHomeWorkBinding = ActivityAllHomeWorkBinding.inflate(getLayoutInflater());
        setContentView(allHomeWorkBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.assignments_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        Initialize_variables();
        loadingAllHomeWorks();

        allHomeWorkBinding.FABaddHomeWork.setOnClickListener(v -> {
            startActivity(new Intent(AllHomeWorkActivity.this, AddHomeWorkActivity.class));
        });
    }

    private void Initialize_variables() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        allHomeWorkBinding.recyclerViewHomeWork.setHasFixedSize(true);
        allHomeWorkBinding.recyclerViewHomeWork.setLayoutManager(new LinearLayoutManager(this));
        homeworksList = new ArrayList<>();
        adapterAllHomeWorks = new Adapter_All_HomeWorks(this, homeworksList);
        allHomeWorkBinding.recyclerViewHomeWork.setAdapter(adapterAllHomeWorks);
        adapterAllHomeWorks.setOnItemClickListener(this);
    }

    public void loadingAllHomeWorks() {
        allHomeWorkBinding.progressCircular.setVisibility(View.VISIBLE);
        mDBListener = mDatabaseHomeWorkRef
                .child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        homeworksList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot != null) {
                                ModelHomeWork homeWork = snapshot.getValue(ModelHomeWork.class);
                                try {
                                    homeWork.setAllowed(checkIsAllowed(homeWork.getHW_EndDate()));
                                    homeworksList.add(homeWork);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        adapterAllHomeWorks.notifyDataSetChanged();
                        allHomeWorkBinding.progressCircular.setVisibility(View.GONE);
                        if (homeworksList.isEmpty())
                            allHomeWorkBinding.patientRecyclerviewImage.setVisibility(View.VISIBLE);
                        else
                            allHomeWorkBinding.patientRecyclerviewImage.setVisibility(View.GONE);
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
            loadingAllHomeWorks();
        }, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseHomeWorkRef
                .removeEventListener(mDBListener);
        homeworksList.clear();
    }

    @Override
    public void onItem_RecyclerView_Click(int position) {
        ModelHomeWork homeWork = homeworksList.get(position);
        Intent intentViewExam = new Intent(this, HomeWorkActivity.class);
        intentViewExam.putExtra("ModelHomeWork", homeWork);
        startActivity(intentViewExam);
    }

    public boolean checkIsAllowed(String endDate) throws ParseException {

        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date d1 = sdformat.parse(getCurrentData());
        Date d2 = sdformat.parse(endDate);
        if (d1.compareTo(d2) > 0) {
            return false;
        }
        return true;
    }

    public String getCurrentData() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
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