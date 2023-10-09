package com.shata.myeducationapp.UI.Lecuter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.shata.myeducationapp.Model.ModelLecuter.ModelLecuter;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityAddLecuterBinding;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddLectuerActivity extends AppCompatActivity {

    public String lectuerID;
    public Uri dataUriPdf;


    ActivityAddLecuterBinding addLecuterBinding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseLectuerRef = database.getReference("Lectuers");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference("Lectuers");

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLecuterBinding = ActivityAddLecuterBinding.inflate(getLayoutInflater());
        setContentView(addLecuterBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.homework_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        lectuerID = mDatabaseLectuerRef.push().getKey();

        addLecuterBinding.selectFile.setOnClickListener(v -> {
            selectPdf();
        });


        addLecuterBinding.saveLectuer.setOnClickListener(v -> {
            {
                String lectuerName = Objects.requireNonNull(addLecuterBinding.lectuerName.getText()).toString().trim();
                if (lectuerName.isEmpty()) {
                    addLecuterBinding.lectuerName.setError(getString(R.string.enter_lec_name));
                    addLecuterBinding.lectuerName.setFocusable(true);
                    addLecuterBinding.lectuerName.requestFocus();
                    return;
                }

                String lectuerURl = Objects.requireNonNull(addLecuterBinding.lecURL.getText()).toString().trim();
                if (lectuerURl.isEmpty()) {
                    addLecuterBinding.lecURL.setError(getString(R.string.enter_lec_link));
                    addLecuterBinding.lecURL.setFocusable(true);
                    addLecuterBinding.lecURL.requestFocus();
                    return;
                }

                String lectuerDate = Objects.requireNonNull(addLecuterBinding.lecDate.getText()).toString().trim();
                if (lectuerDate.isEmpty()) {
                    addLecuterBinding.lecDate.setError(getString(R.string.enter_lec_date));
                    addLecuterBinding.lecDate.setFocusable(true);
                    addLecuterBinding.lecDate.requestFocus();
                    return;
                }


                ModelLecuter lecuter = new ModelLecuter();

                lecuter.setLecID(lectuerID);
                lecuter.setLecName(lectuerName);
                lecuter.setLecURL(lectuerURl);
                lecuter.setLecDate(lectuerDate);

                if (getLecUriUPdf() != null) {
                    uploadPDFFile(getLecUriUPdf(), lecuter);
                } else {
                    lecuter.setLecUrlPdf("");
                    mDatabaseLectuerRef
                            .child(mAuth.getUid())
                            .child(lectuerID)
                            .setValue(lecuter)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toasty.success(AddLectuerActivity.this, "" + getString(R.string.add_lecture_successfully), Toast.LENGTH_SHORT).show();
                                    addLecuterBinding.lectuerName.setText("");
                                    addLecuterBinding.lecURL.setText("");
                                    addLecuterBinding.lecDate.setText("");
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.d("Add Lectuer Activity", e.getMessage());
                            });
                }
            }
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
            addLecuterBinding.lecPdfName.setText(data.getDataString());
            setLecUrlPdf(data.getData());
        }
    }


    private void uploadPDFFile(Uri dataUri, ModelLecuter lecuter) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.file_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StorageReference reference = storageReference
                .child(mAuth.getUid())
                .child(lectuerID)
                .child("Lectuer.pdf");
        reference
                .putFile(dataUri)
                .addOnSuccessListener(taskSnapshot -> {
                    {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        lecuter.setLecUrlPdf(uri.toString());
                        mDatabaseLectuerRef
                                .child(mAuth.getUid())
                                .child(lectuerID)
                                .setValue(lecuter)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toasty.success(AddLectuerActivity.this, "" + getString(R.string.add_lecture_successfully), Toast.LENGTH_SHORT).show();
                                        addLecuterBinding.lectuerName.setText("");
                                        addLecuterBinding.lecURL.setText("");
                                        addLecuterBinding.lecDate.setText("");
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("Add Lectuer Activity", e.getMessage());
                                });


                    }
                }).addOnFailureListener(e -> Log.d("Add Lectuer Activity", e.getMessage())).addOnProgressListener(snapshot -> {

                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    dialog.setMessage(getString(R.string.file_upload) + (int) progress + " % ");

                });
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


    public Uri getLecUriUPdf() {
        return dataUriPdf;
    }

    public void setLecUrlPdf(Uri lecUrlPdf) {
        this.dataUriPdf = lecUrlPdf;
    }

}