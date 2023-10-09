package com.shata.myeducationapp.UI.Exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shata.myeducationapp.Model.ModelExams.ModelExam;
import com.shata.myeducationapp.Model.ModelExams.ModelQuestion;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityAddExamBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AddExamActivity extends AppCompatActivity {

    ActivityAddExamBinding addExamBinding;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Exams");
    List<ModelQuestion> questions;
    String Question, Choose1, Choose2, Choose3, Choose4;
    int Answer;
    private Toolbar toolbar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addExamBinding = ActivityAddExamBinding.inflate(getLayoutInflater());
        setContentView(addExamBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getString(R.string.add_exam_page));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //
        questions = new ArrayList<>();

        addExamBinding.addQuestion.setOnClickListener(v -> {

            Question = Objects.requireNonNull(addExamBinding.questionText.getText()).toString().trim();
            if (Question.isEmpty()) {
                addExamBinding.questionText.setError(getString(R.string.enter_question));
                addExamBinding.questionText.setFocusable(true);
                addExamBinding.questionText.requestFocus();
                return;
            }

            Choose1 = Objects.requireNonNull(addExamBinding.choose1.getText()).toString().trim();
            if (Choose1.isEmpty()) {
                addExamBinding.choose1.setError(getString(R.string.enter_choice_1));
                addExamBinding.choose1.setFocusable(true);
                addExamBinding.choose1.requestFocus();
                return;
            }

            Choose2 = Objects.requireNonNull(addExamBinding.choose2.getText()).toString().trim();
            if (Choose2.isEmpty()) {
                addExamBinding.choose2.setError(getString(R.string.enter_choice_2));
                addExamBinding.choose2.setFocusable(true);
                addExamBinding.choose2.requestFocus();
                return;
            }

            Choose3 = Objects.requireNonNull(addExamBinding.choose3.getText()).toString().trim();
            if (Choose3.isEmpty()) {
                addExamBinding.choose3.setError(getString(R.string.enter_choice_3));
                addExamBinding.choose3.setFocusable(true);
                addExamBinding.choose3.requestFocus();
                return;
            }

            Choose4 = Objects.requireNonNull(addExamBinding.choose4.getText()).toString().trim();
            if (Choose4.isEmpty()) {
                addExamBinding.choose4.setError(getString(R.string.enter_choice_4));
                addExamBinding.choose4.setFocusable(true);
                addExamBinding.choose4.requestFocus();
                return;
            }

            if (addExamBinding.radioGroupAnswer.getCheckedRadioButtonId() == -1) {
                Toasty.info(AddExamActivity.this, "" + getString(R.string.choose_the_answer_number), Toast.LENGTH_SHORT).show();
                return;
            } else {
                int answerNumber = addExamBinding.radioGroupAnswer.getCheckedRadioButtonId();
                if (answerNumber == R.id.answer1) {
                    Answer = 1;
                } else if (answerNumber == R.id.answer2) {
                    Answer = 2;
                } else if (answerNumber == R.id.answer3) {
                    Answer = 3;
                } else if (answerNumber == R.id.answer4) {
                    Answer = 4;
                }
            }

            ModelQuestion modelQuestion = new ModelQuestion();

            int QuestionID = questions.size();
            modelQuestion.setQuestionID(QuestionID);
            modelQuestion.setQuestionText(Question + "");
            modelQuestion.setChoice_1(Choose1 + "");
            modelQuestion.setChoice_2(Choose2 + "");
            modelQuestion.setChoice_3(Choose3 + "");
            modelQuestion.setChoice_4(Choose4 + "");
            modelQuestion.setAnswerNumber(Answer);
            modelQuestion.setChoiceNumber(0);
            questions.add(modelQuestion);

            if ((!Question.isEmpty()) && (!Choose1.isEmpty()) && (!Choose2.isEmpty()) && (!Choose3.isEmpty()) && (!Choose4.isEmpty()) && (addExamBinding.radioGroupAnswer.getCheckedRadioButtonId() != -1))
                Toasty.success(this, "" + getString(R.string.add_question_successfully), Toast.LENGTH_SHORT).show();

            addExamBinding.questionText.setText("");
            addExamBinding.choose1.setText("");
            addExamBinding.choose2.setText("");
            addExamBinding.choose3.setText("");
            addExamBinding.choose4.setText("");
            addExamBinding.radioGroupAnswer.check(-1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.finish:
                showDialogExamDetails();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialogExamDetails() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exam_details_layout, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        //
        TextInputEditText examName = dialogView.findViewById(R.id.examName);
        TextInputEditText examDegree = dialogView.findViewById(R.id.examDegree);
        TextInputEditText examDate = dialogView.findViewById(R.id.examDate);
        examDate.setText(getCurrentData());
        //examDate.setEnabled(false);
        Button addExam = dialogView.findViewById(R.id.addExam);

        addExam.setOnClickListener(v -> {

            if (!questions.isEmpty()) {

                String ExamName = Objects.requireNonNull(examName.getText()).toString().trim();
                if (ExamName.isEmpty()) {
                    examName.setError(getString(R.string.enter_exam_name));
                    examName.setFocusable(true);
                    examName.requestFocus();
                    return;
                }
                String ExamDegree = Objects.requireNonNull(examDegree.getText()).toString().trim();
                if (ExamDegree.isEmpty()) {
                    examDegree.setError(getString(R.string.enter_exam_degree));
                    examDegree.setFocusable(true);
                    examDegree.requestFocus();
                    return;
                }
                String ExamDate = Objects.requireNonNull(examDate.getText()).toString().trim();
                if (ExamDate.isEmpty()) {
                    examDate.setError(getString(R.string.enter_exam_date));
                    examDate.setFocusable(true);
                    examDate.requestFocus();
                    return;
                }

                ModelExam modelExam = new ModelExam();

                String ID = databaseReference.push().getKey();
                modelExam.setExamID(ID);
                modelExam.setExamName(ExamName);
                modelExam.setExamDegree(Integer.parseInt(ExamDegree));
                modelExam.setExamDate(ExamDate);
                modelExam.setQuestions(questions);

                databaseReference
                        .child(mAuth.getUid())
                        .child(ID)
                        .setValue(modelExam)
                        .addOnSuccessListener(aVoid -> {
                            Toasty.success(AddExamActivity.this, "" + getString(R.string.add_exam_successfully), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }).addOnFailureListener(e ->
                        Log.d("AddExamActivity", e.getMessage())
                );
            } else {
                Toasty.warning(this, "" + getString(R.string.check_question_box), Toast.LENGTH_SHORT).show();
            }
            alertDialog.dismiss();

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public String getCurrentData() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy/M/dd", Locale.ENGLISH);
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }
}