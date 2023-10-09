package com.shata.myeducationapp.UI.Exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shata.myeducationapp.Model.ModelExams.ModelExamID;
import com.shata.myeducationapp.Model.ModelExams.ModelQuestion;
import com.shata.myeducationapp.R;
import com.shata.myeducationapp.databinding.ActivityQuestionsBinding;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class QuestionsActivity extends AppCompatActivity {
    ActivityQuestionsBinding questionsBinding;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseExamsRef = database.getReference("Exams");
    ArrayList<ModelQuestion> modelQuestionList;
    ModelExamID examID;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int indexQuestion = 0;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionsBinding = ActivityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(questionsBinding.getRoot());

        //
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.questions_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //

        examID = (ModelExamID) getIntent().getSerializableExtra("ModelExamID");

        Initialize_variables();

        if (examID != null) {
            loadExamQuestions(examID.getExamID());
        }

        questionsBinding.next.setOnClickListener(v -> {
            {
                int Answer = 0;
                int answerNumber = questionsBinding.radioGroup.getCheckedRadioButtonId();
                if (answerNumber == R.id.answer1) {
                    Answer = 1;
                } else if (answerNumber == R.id.answer2) {
                    Answer = 2;
                } else if (answerNumber == R.id.answer3) {
                    Answer = 3;
                } else if (answerNumber == R.id.answer4) {
                    Answer = 4;
                }
                modelQuestionList.get(indexQuestion).setChoiceNumber(Answer);
                if (indexQuestion == modelQuestionList.size())
                    indexQuestion = modelQuestionList.size();
                else {
                    indexQuestion = indexQuestion + 1;
                }
                loadQuestion(indexQuestion);
            }
        });
        questionsBinding.previous.setOnClickListener(v -> {
            {
                int Answer = 0;
                int answerNumber = questionsBinding.radioGroup.getCheckedRadioButtonId();
                if (answerNumber == R.id.answer1) {
                    Answer = 1;
                } else if (answerNumber == R.id.answer2) {
                    Answer = 2;
                } else if (answerNumber == R.id.answer3) {
                    Answer = 3;
                } else if (answerNumber == R.id.answer4) {
                    Answer = 4;
                }
                modelQuestionList.get(indexQuestion).setChoiceNumber(Answer);
                if (indexQuestion == 0)
                    indexQuestion = 0;
                else {
                    indexQuestion = indexQuestion - 1;
                }
                loadQuestion(indexQuestion);
            }
        });

        questionsBinding.FABFinishExam.setOnClickListener(v -> {
            {
                AutomaticCorrection();
            }
        });
    }

    private void Initialize_variables() {
        modelQuestionList = new ArrayList<>();
    }

    public void loadExamQuestions(String ExamID) {
        questionsBinding.progressCircular.setVisibility(View.VISIBLE);
        mDatabaseExamsRef
                .child(mAuth.getUid())
                .child(ExamID)
                .child("questions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        modelQuestionList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ModelQuestion question = snapshot.getValue(ModelQuestion.class);
                            if (question != null) {
                                modelQuestionList.add(question);
                            }
                        }
                        // Load First Question
                        loadQuestion(0);
                        questionsBinding.progressCircular.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionsActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("QuestionsActivity", error.getMessage());
                    }
                });
    }

    public void loadQuestion(int index) {
        if (!modelQuestionList.isEmpty()) {

            if (modelQuestionList.size() == 1) {
                questionsBinding.FABFinishExam.setVisibility(View.VISIBLE);
                questionsBinding.next.setVisibility(View.INVISIBLE);
                questionsBinding.previous.setVisibility(View.INVISIBLE);
            } else if (index == 0) {
                questionsBinding.previous.setVisibility(View.INVISIBLE);
                questionsBinding.next.setVisibility(View.VISIBLE);
                questionsBinding.FABFinishExam.setVisibility(View.INVISIBLE);
            } else if (index == modelQuestionList.size() - 1) {
                questionsBinding.next.setVisibility(View.INVISIBLE);
                questionsBinding.previous.setVisibility(View.VISIBLE);
                questionsBinding.FABFinishExam.setVisibility(View.VISIBLE);
            } else {
                questionsBinding.previous.setVisibility(View.VISIBLE);
                questionsBinding.next.setVisibility(View.VISIBLE);
                questionsBinding.FABFinishExam.setVisibility(View.INVISIBLE);
            }

            questionsBinding.textID.setText(index + 1 + " : ");
            questionsBinding.textQuestion.setText(modelQuestionList.get(index).getQuestionText());
            questionsBinding.answer1.setText(modelQuestionList.get(index).getChoice_1());
            questionsBinding.answer2.setText(modelQuestionList.get(index).getChoice_2());
            questionsBinding.answer3.setText(modelQuestionList.get(index).getChoice_3());
            questionsBinding.answer4.setText(modelQuestionList.get(index).getChoice_4());

            if (modelQuestionList.get(index).getChoiceNumber() == 1) {
                questionsBinding.radioGroup.check(R.id.answer1);
            } else if (modelQuestionList.get(index).getChoiceNumber() == 2) {
                questionsBinding.radioGroup.check(R.id.answer2);
            } else if (modelQuestionList.get(index).getChoiceNumber() == 3) {
                questionsBinding.radioGroup.check(R.id.answer3);
            } else if (modelQuestionList.get(index).getChoiceNumber() == 4) {
                questionsBinding.radioGroup.check(R.id.answer4);
            } else {
                questionsBinding.radioGroup.check(-1);
            }
        }
    }

    public void AutomaticCorrection() {

        int Answer = 0;
        int answerNumber = questionsBinding.radioGroup.getCheckedRadioButtonId();
        if (answerNumber == R.id.answer1) {
            Answer = 1;
        } else if (answerNumber == R.id.answer2) {
            Answer = 2;
        } else if (answerNumber == R.id.answer3) {
            Answer = 3;
        } else if (answerNumber == R.id.answer4) {
            Answer = 4;
        }
        modelQuestionList.get(indexQuestion).setChoiceNumber(Answer);

        int correctNumQuestion = 0;

        for (int item = 0; item < modelQuestionList.size(); item++) {

            if (modelQuestionList.get(item).getChoiceNumber() == 0) {
                Toasty.info(this, "" + getString(R.string.didnot_choose) + (item + 1), Toast.LENGTH_SHORT).show();
                return;
            }
            if (modelQuestionList.get(item).getAnswerNumber() == modelQuestionList.get(item).getChoiceNumber())
                correctNumQuestion++;
        }
        double degree = ((double) correctNumQuestion / (double) modelQuestionList.size());
        int score = (int) (degree * (examID.getExamDegree()));
        showDialogExamScore(score, examID.getExamDegree());
    }

    private void showDialogExamScore(int score, int Total) {
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView;
        if (score == 0) {
            dialogView = inflater.inflate(R.layout.exam_fail_layout, null);
        } else if (score < (Total / 2)) {
            dialogView = inflater.inflate(R.layout.exam_not_excees_layout, null);
        } else {
            dialogView = inflater.inflate(R.layout.exam_degree_layout, null);
        }
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        //
        TextView examDegree = dialogView.findViewById(R.id.yourScore);
        examDegree.setText(score + " / " + Total);
        Button ok = dialogView.findViewById(R.id.btnOK);

        ok.setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });
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
        AlertDialog alertDialog = new AlertDialog.Builder(QuestionsActivity.this).create();
        alertDialog.setTitle("" + getString(R.string.delete));
        alertDialog.setMessage(getString(R.string.are_you_sure));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                (dialog, which) -> {
                    delete();
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancle),
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void delete() {
        mDatabaseExamsRef
                .child(mAuth.getUid())
                .child(examID.getExamID()).removeValue();
        Toasty.success(this, "" + getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}