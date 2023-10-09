package com.shata.myeducationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.Student.ModelStudentExam;
import com.shata.myeducationapp.R;

import java.util.List;

public class Adapter_Student_Exam extends RecyclerView.Adapter<Adapter_Student_Exam.StudentViewHolder> {

    private Context mContext;
    private List<ModelStudentExam> studentExams;

    public Adapter_Student_Exam(Context mContext, List<ModelStudentExam> studentExams) {
        this.mContext = mContext;
        this.studentExams = studentExams;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.student_exam_layout, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        ModelStudentExam studentExam = studentExams.get(position);
        holder.ExamName.setText(studentExam.getExamName());
        holder.StudentScore.setText(studentExam.getExamScore());
    }

    @Override
    public int getItemCount() {
        return studentExams.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        public TextView ExamName, StudentScore;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            ExamName = itemView.findViewById(R.id.examName);
            StudentScore = itemView.findViewById(R.id.examScore);
        }
    }
}

