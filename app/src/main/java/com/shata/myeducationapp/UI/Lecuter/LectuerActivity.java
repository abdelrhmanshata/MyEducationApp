package com.shata.myeducationapp.UI.Lecuter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shata.myeducationapp.Model.ModelLecuter.ModelLecuter;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.YouTubeConfig;
import com.shata.myeducationapp.databinding.ActivityLecuterBinding;

import es.dmoral.toasty.Toasty;

public class LectuerActivity extends YouTubeBaseActivity {

    public Uri dataUriPdf;

    ActivityLecuterBinding lecuterBinding;
    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseLectuerRef = database.getReference("Lectuers");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference("Lectuers");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ModelLecuter lecuter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lecuterBinding = ActivityLecuterBinding.inflate(getLayoutInflater());
        setContentView(lecuterBinding.getRoot());

        lecuter = (ModelLecuter) getIntent().getSerializableExtra("ModelLecuter");

        mYouTubePlayerView = findViewById(R.id.youTubePLayer);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(getIDYouTubeVideo(lecuter.getLecURL()));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        lecuterBinding.FABPlayVideo.setOnClickListener(v -> {
            mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(), mOnInitializedListener);
            lecuterBinding.FABPlayVideo.setVisibility(View.GONE);
        });

        if (lecuter.getLecUrlPdf().isEmpty()) {
            lecuterBinding.layoutDownLoad.setVisibility(View.GONE);
        }

        lecuterBinding.lectuerNameTV.setText(lecuter.getLecName() + "");
        lecuterBinding.lectuerLinkTV.setText(lecuter.getLecURL() + "");
        lecuterBinding.lectuerDateTV.setText(lecuter.getLecDate() + "");
        lecuterBinding.lecPdfName.setText(lecuter.getLecUrlPdf() + "");
        lecuterBinding.lecPdfNameDownLoad.setText(lecuter.getLecName() + ".pdf");

        lecuterBinding.selectFile.setOnClickListener(v -> {
            selectPdf();
        });

        lecuterBinding.downLoadPdf.setOnClickListener(v -> {
            downloadPdfLectuer(lecuter);
        });

        lecuterBinding.btnSave.setOnClickListener(v -> {

            lecuter.setLecName(lecuterBinding.lectuerNameTV.getText().toString().trim());
            lecuter.setLecURL(lecuterBinding.lectuerLinkTV.getText().toString().trim());
            lecuter.setLecDate(lecuterBinding.lectuerDateTV.getText().toString().trim());

            if (lecuter.getLecUrlPdf().equals(lecuterBinding.lecPdfName.getText().toString())) {
                mDatabaseLectuerRef
                        .child(mAuth.getUid())
                        .child(lecuter.getLecID()).setValue(lecuter)
                        .addOnSuccessListener(aVoid -> {
                            Toasty.success(LectuerActivity.this, "" + getString(R.string.updata_success), Toast.LENGTH_SHORT).show();
                        });
            } else {
                uploadPDFFile(getLecUriUPdf(), lecuter);
            }
        });
    }

    @SuppressLint("IntentReset")
    private void downloadPdfLectuer(ModelLecuter lec) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/pdf");
        intent.setData(Uri.parse(lec.getLecUrlPdf()));
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
            lecuterBinding.lecPdfName.setText(data.getDataString());
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
                .child(lecuter.getLecID())
                .child("Lecture.pdf");
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
                                .child(lecuter.getLecID())
                                .setValue(lecuter)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toasty.success(LectuerActivity.this, "" + getString(R.string.updata_success), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("Add Lecture Activity", e.getMessage());
                                });


                    }
                }).addOnFailureListener(e -> Log.d("Add Lecture Activity", e.getMessage())).addOnProgressListener(snapshot -> {

            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            dialog.setMessage(getString(R.string.file_upload) + (int) progress + " % ");

        });
    }


    public String getIDYouTubeVideo(String url) {
        if (url.contains("https://youtu.be/")) {
            url = url.substring(url.lastIndexOf("/") + 1);
            return url;
        } else if (url.contains("=")) {
            url = url.substring(url.lastIndexOf("=") + 1);
            return url;
        }
        return url;

    }

    public Uri getLecUriUPdf() {
        return dataUriPdf;
    }

    public void setLecUrlPdf(Uri lecUrlPdf) {
        this.dataUriPdf = lecUrlPdf;
    }
}