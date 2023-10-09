package com.shata.myeducationapp.UI.HomeWork;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shata.myeducationapp.Model.ModelHomeWork.ModelHomeWork;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityAddHomeWorkBinding;

import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddHomeWorkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityAddHomeWorkBinding addHomeWorkBinding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseHomeWorkRef = database.getReference("HomeWorks");

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference("HomeWorks");
    boolean sDate = false, eDate = false;
    private Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addHomeWorkBinding = ActivityAddHomeWorkBinding.inflate(getLayoutInflater());
        setContentView(addHomeWorkBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.add_homework_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        addHomeWorkBinding.saveHomeWork.setEnabled(false);
        addHomeWorkBinding.BtnHKStartDate.setOnClickListener(v -> {
            sDate = true;
            eDate = false;
            showDatePickerDialog();
        });

        addHomeWorkBinding.BtnHKEndDate.setOnClickListener(v -> {
            sDate = false;
            eDate = true;
            showDatePickerDialog();
        });

        addHomeWorkBinding.selectFile.setOnClickListener(v -> {
            selectPdf();
        });
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pdf File Select"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            addHomeWorkBinding.hwPdfName.setText(data.getDataString());
            addHomeWorkBinding.saveHomeWork.setEnabled(true);
            addHomeWorkBinding.saveHomeWork.setOnClickListener(v -> {

                String HomeWorkName = Objects.requireNonNull(addHomeWorkBinding.homeworkName.getText()).toString().trim();
                if (HomeWorkName.isEmpty()) {
                    addHomeWorkBinding.homeworkName.setError(getString(R.string.enter_assignment_name));
                    addHomeWorkBinding.homeworkName.setFocusable(true);
                    addHomeWorkBinding.homeworkName.requestFocus();
                    return;
                }

                String startDate = Objects.requireNonNull(addHomeWorkBinding.hkStartDate.getText()).toString().trim();
                if (startDate.isEmpty()) {
                    addHomeWorkBinding.hkStartDate.setError(getString(R.string.enter_assignment_startdate));
                    addHomeWorkBinding.hkStartDate.setFocusable(true);
                    addHomeWorkBinding.hkStartDate.requestFocus();
                    return;
                }

                String endDate = Objects.requireNonNull(addHomeWorkBinding.hkEndDate.getText()).toString().trim();
                if (endDate.isEmpty()) {
                    addHomeWorkBinding.hkEndDate.setError(getString(R.string.enter_assignment_enddate));
                    addHomeWorkBinding.hkEndDate.setFocusable(true);
                    addHomeWorkBinding.hkEndDate.requestFocus();
                    return;
                }

                String homeWorkID = mDatabaseHomeWorkRef.push().getKey();

                ModelHomeWork homeWork = new ModelHomeWork();

                homeWork.setHW_ID(homeWorkID);
                homeWork.setHW_Name(HomeWorkName);
                homeWork.setHW_StartDate(startDate);
                homeWork.setHW_EndDate(endDate);
                homeWork.setAllowed(true);

                uploadPDFFile(data.getData(), homeWork);

            });
        }
    }

    private void uploadPDFFile(Uri dataUri, ModelHomeWork work) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.file_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StorageReference reference = storageReference
                .child(mAuth.getUid())
                .child(work.getHW_ID())
                .child("HomeWork.pdf");

        reference
                .putFile(dataUri)
                .addOnSuccessListener(taskSnapshot -> {
                    {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        work.setHW_PDF_URL(uri.toString());

                        mDatabaseHomeWorkRef
                                .child(mAuth.getUid())
                                .child(work.getHW_ID())
                                .setValue(work)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toasty.success(AddHomeWorkActivity.this, "" + getString(R.string.add_homework_success), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("Add HomeWork Activity", e.getMessage());
                                });
                    }
                }).addOnFailureListener(e -> Log.d("Add HomeWork Activity", e.getMessage())).addOnProgressListener(snapshot -> {
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage(getString(R.string.file_upload) + (int) progress + " % ");
                });
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
        if (sDate)
            addHomeWorkBinding.hkStartDate.setText(date);
        else if (eDate)
            addHomeWorkBinding.hkEndDate.setText(date);
        else {
            addHomeWorkBinding.hkStartDate.setHint(getString(R.string.start_date));
            addHomeWorkBinding.hkEndDate.setHint(getString(R.string.end_date));
        }
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