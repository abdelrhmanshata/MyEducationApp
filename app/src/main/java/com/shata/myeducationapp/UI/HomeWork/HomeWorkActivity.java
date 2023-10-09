package com.shata.myeducationapp.UI.HomeWork;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.shata.myeducationapp.databinding.ActivityHomeWorkBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class HomeWorkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityHomeWorkBinding homeWorkBinding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseHomeWorkRef = database.getReference("HomeWorks");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference("HomeWorks");
    boolean sDate = true, eDate = true;
    ModelHomeWork homeWork;
    private Uri dataUriPdf;
    private Toolbar toolbar;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeWorkBinding = ActivityHomeWorkBinding.inflate(getLayoutInflater());
        setContentView(homeWorkBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.assignment_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        homeWork = (ModelHomeWork) getIntent().getSerializableExtra("ModelHomeWork");

        homeWorkBinding.homeworkName.setText(homeWork.getHW_Name() + "");
        homeWorkBinding.hkStartDate.setText(homeWork.getHW_StartDate() + "");
        homeWorkBinding.hkEndDate.setText(homeWork.getHW_EndDate() + "");
        homeWorkBinding.hwPdfName.setText(homeWork.getHW_PDF_URL() + "");
        homeWorkBinding.homeWorkPdfDownLoad.setText(homeWork.getHW_PDF_URL() + "");

        homeWorkBinding.BtnHKStartDate.setOnClickListener(v -> {
            sDate = true;
            eDate = false;
            showDatePickerDialog();
        });

        homeWorkBinding.BtnHKEndDate.setOnClickListener(v -> {
            sDate = false;
            eDate = true;
            showDatePickerDialog();
        });
        homeWorkBinding.selectFile.setOnClickListener(v -> {
            selectPdf();
        });

        homeWorkBinding.downLoadPdf.setOnClickListener(v -> {
            downloadPdfHomeWork(homeWork);
        });

        homeWorkBinding.saveHomeWork.setOnClickListener(v -> {

            String HomeWorkName = Objects.requireNonNull(homeWorkBinding.homeworkName.getText()).toString().trim();
            if (HomeWorkName.isEmpty()) {
                homeWorkBinding.homeworkName.setError(getString(R.string.enter_homework_name));
                homeWorkBinding.homeworkName.setFocusable(true);
                homeWorkBinding.homeworkName.requestFocus();
                return;
            }

            homeWork.setHW_Name(homeWorkBinding.homeworkName.getText().toString().trim());
            homeWork.setHW_StartDate(homeWorkBinding.hkStartDate.getText().toString().trim());
            homeWork.setHW_EndDate(homeWorkBinding.hkEndDate.getText().toString().trim());

            try {
                homeWork.setAllowed(checkIsAllowed(homeWork.getHW_EndDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (homeWork.getHW_PDF_URL().equals(homeWorkBinding.hwPdfName.getText().toString())) {
                mDatabaseHomeWorkRef
                        .child(mAuth.getUid())
                        .child(homeWork.getHW_ID())
                        .setValue(homeWork)
                        .addOnSuccessListener(aVoid -> {
                            Toasty.success(HomeWorkActivity.this, "" + getString(R.string.updata_success), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        })
                        .addOnFailureListener(e -> {
                            Log.d("HomeWorkActivity", e.getMessage());
                        });
            } else {
                uploadPDFFile(getHomeWorkUriUPdf(), homeWork);
            }
        });
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


    @SuppressLint("IntentReset")
    private void downloadPdfHomeWork(ModelHomeWork work) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        intent.setData(Uri.parse(work.getHW_PDF_URL()));
        startActivity(intent);
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pdf File Select"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            homeWorkBinding.hwPdfName.setText(data.getDataString());
            setHomeWorkUrlPdf(data.getData());
        }
    }

    private void uploadPDFFile(Uri dataUri, ModelHomeWork homeWork) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.file_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StorageReference reference = storageReference
                .child(mAuth.getUid())
                .child(homeWork.getHW_ID())
                .child("HomeWork.pdf");

        reference
                .putFile(dataUri)
                .addOnSuccessListener(taskSnapshot -> {
                    {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        homeWork.setHW_PDF_URL(uri.toString());
                        mDatabaseHomeWorkRef
                                .child(mAuth.getUid())
                                .child(homeWork.getHW_ID())
                                .setValue(homeWork)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toasty.success(HomeWorkActivity.this, ""+getString(R.string.updata_lec_success), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("Add Lectuer Activity", e.getMessage());
                                });
                    }
                }).addOnFailureListener(e -> Log.d("Add Lectuer Activiyt", e.getMessage())).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                dialog.setMessage(getString(R.string.file_upload) + (int) progress + " % ");
            }
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
            homeWorkBinding.hkStartDate.setText(date);
        else if (eDate)
            homeWorkBinding.hkEndDate.setText(date);
        else {
            homeWorkBinding.hkStartDate.setHint("Start Date");
            homeWorkBinding.hkEndDate.setHint("End Date");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.delete:
                deleteExam();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteExam() {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeWorkActivity.this).create();
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are you sure to delete?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    delete();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancle",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void delete() {
        StorageReference deleteFile = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(homeWork.getHW_PDF_URL());
        deleteFile
                .delete()
                .addOnSuccessListener(aVoid -> {
                    mDatabaseHomeWorkRef
                            .child(mAuth.getUid())
                            .child(homeWork.getHW_ID()).removeValue();
                    Toasty.success(this, ""+getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> Toasty.success(HomeWorkActivity.this, "Error->" + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public Uri getHomeWorkUriUPdf() {
        return dataUriPdf;
    }

    public void setHomeWorkUrlPdf(Uri lecUrlPdf) {
        this.dataUriPdf = lecUrlPdf;
    }
}