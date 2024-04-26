package com.rrgroup.myapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.guilhe.views.CircularProgressView;

import java.util.List;

public class TTAdapter extends RecyclerView.Adapter<TTAdapter.HourViewHolder> {

    private List<String> sub_code;
    private List<String> sub;
    private List<String > period;

    public TTAdapter(List<String> sub_code, List<String> sub,List<String > period) {
        this.sub_code = sub_code;
        this.sub = sub;
        this.period = period;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tt_item, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        try {
            String subjectCode = sub_code.get(position);
            holder.subCode.setText(subjectCode);
            String subject = sub.get(position);
            holder.sub.setText(subject);
            String Period = period.get(position);
            holder.periodView.setText(Period);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return sub_code.size();
    }

    public static class HourViewHolder extends RecyclerView.ViewHolder {
        public TextView subCode;
        public TextView sub;
        public TextView periodView;

        public HourViewHolder(View itemView) {
            super(itemView);
            subCode = itemView.findViewById(R.id.subjectCode);
            sub = itemView.findViewById(R.id.subject);
            periodView = itemView.findViewById(R.id.period);
        }
    }
}
