package com.shata.myeducationapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.ModelLecuter.ModelLecuter;
import com.shata.myeducationapp.R;

import java.util.List;

public class Adapter_All_Lecuters extends RecyclerView.Adapter<Adapter_All_Lecuters.ProductViewHolder> {

    private Context mContext;
    private List<ModelLecuter> lecuters;
    private OnItemClickListener mListener;

    public Adapter_All_Lecuters(Context mContext, List<ModelLecuter> lecuters) {
        this.mContext = mContext;
        this.lecuters = lecuters;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_lectuer_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ModelLecuter lecuter = lecuters.get(position);
        holder.LectuerName.setText(lecuter.getLecName());
    }

    @Override
    public int getItemCount() {
        return lecuters.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItem_RecyclerView_Click(int position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {

        public TextView LectuerName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            LectuerName = itemView.findViewById(R.id.lectuerName);
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

