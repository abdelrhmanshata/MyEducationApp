package com.shata.myeducationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.Model.Student.ModelStudentHomeWork;
import com.shata.myeducationapp.R;

import java.util.List;

public class Adapter_Student_HomeWork extends RecyclerView.Adapter<Adapter_Student_HomeWork.StudentViewHolder> {

    private Context mContext;
    private List<ModelStudentHomeWork> studentHomeWorks;
    private OnItemClickListener mListener;

    public Adapter_Student_HomeWork(Context mContext, List<ModelStudentHomeWork> studentHomeWorks) {
        this.mContext = mContext;
        this.studentHomeWorks = studentHomeWorks;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.student_homework_layout, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        ModelStudentHomeWork  homeWork = studentHomeWorks.get(position);
        holder.homeworkName.setText(homeWork.getHomeworkName());
    }

    @Override
    public int getItemCount() {
        return studentHomeWorks.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItem_RecyclerView_Click(int position);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView homeworkName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            homeworkName = itemView.findViewById(R.id.homeworkName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItem_RecyclerView_Click(position);
                }
            }
        }
    }
}

