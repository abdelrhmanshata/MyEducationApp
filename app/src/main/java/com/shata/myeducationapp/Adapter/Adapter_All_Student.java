package com.shata.myeducationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.Student.ModelStudent;
import com.shata.myeducationapp.R;

import java.util.List;

public class Adapter_All_Student extends RecyclerView.Adapter<Adapter_All_Student.StudentViewHolder> {

    private Context mContext;
    private List<ModelStudent> students;
    private OnItemClickListener mListener;

    public Adapter_All_Student(Context mContext, List<ModelStudent> students) {
        this.mContext = mContext;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.student_layout, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        ModelStudent student = students.get(position);
        holder.StudentName.setText(student.getStudentName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItem_RecyclerView_Click(int position);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView StudentName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            StudentName = itemView.findViewById(R.id.studentName);
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

