package com.shata.myeducationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.ModelExams.ModelExamID;
import com.shata.myeducationapp.R;

import java.util.List;

public class Adapter_All_Exams_Name extends RecyclerView.Adapter<Adapter_All_Exams_Name.ExamViewHolder> {

    private Context mContext;
    private List<ModelExamID> examIDList;
    private OnItemClickListener mListener;

    public Adapter_All_Exams_Name(Context mContext, List<ModelExamID> examIDList) {
        this.mContext = mContext;
        this.examIDList = examIDList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.exam_layout, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {

        ModelExamID examID = examIDList.get(position);
        holder.ExamName.setText(examID.getExamName());
        holder.ExamDate.setText(examID.getExamDate());
    }

    @Override
    public int getItemCount() {
        return examIDList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItem_RecyclerView_Click(int position);
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {

        public TextView ExamName, ExamDate;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);

            ExamName = itemView.findViewById(R.id.examName);
            ExamDate = itemView.findViewById(R.id.examDate);
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

