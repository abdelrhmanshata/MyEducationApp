package com.shata.myeducationapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shata.myeducationapp.Model.ModelHomeWork.ModelHomeWork;
import com.shata.myeducationapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Adapter_All_HomeWorks extends RecyclerView.Adapter<Adapter_All_HomeWorks.HomeWorkViewHolder> {

    private Context mContext;
    private List<ModelHomeWork> homeWorks;
    private OnItemClickListener mListener;

    public Adapter_All_HomeWorks(Context mContext, List<ModelHomeWork> homeWorks) {
        this.mContext = mContext;
        this.homeWorks = homeWorks;
    }

    @NonNull
    @Override
    public HomeWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_homework_layout, parent, false);
        return new HomeWorkViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HomeWorkViewHolder holder, int position) {

        ModelHomeWork homeWork = homeWorks.get(position);
        holder.HomeWorkName.setText(homeWork.getHW_Name());
        holder.HomeWorkDate.setText(homeWork.getHW_EndDate());

        try {
            if (checkIsAllowed(homeWork.getHW_EndDate())) {
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.prim));
            } else {
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.gray));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return homeWorks.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public boolean checkIsAllowed(String endDate) throws ParseException {

        SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
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


    public interface OnItemClickListener {
        void onItem_RecyclerView_Click(int position);
    }

    public class HomeWorkViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView HomeWorkName, HomeWorkDate;
        public CardView cardView;

        public HomeWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            HomeWorkName = itemView.findViewById(R.id.homeWorkName);
            HomeWorkDate = itemView.findViewById(R.id.homeWorkDate);
            cardView = itemView.findViewById(R.id.cardViewHomeWork);
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

