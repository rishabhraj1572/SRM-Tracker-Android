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

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.HourViewHolder> {

    private List<String> sub_code;
    private List<String> sub;
    private List<String> mark;

    public MarkAdapter(List<String> sub_code, List<String> sub, List<String> mark) {
        this.sub_code = sub_code;
        this.sub = sub;
        this.mark =mark;

    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mark_item, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        try {
            String subjectCode = sub_code.get(position);
            holder.subCode.setText(subjectCode);
            String subject = sub.get(position);
            holder.sub.setText(subject);
            String marks =  mark.get(position);

            String marksRatio[] = marks.split("/");
            Float marksPercentage = (Float.valueOf(marksRatio[0])/Float.valueOf(marksRatio[1]))*100;
            String formattedPercentage = String.format("%.2f", marksPercentage);
            holder.markPer.setText(formattedPercentage+"%");
            holder.circularProgressViewKt.setProgress(marksPercentage,true);

            holder.markR.setText(marks);

            if(Float.valueOf(marksPercentage)<50){
                holder.circularProgressViewKt.setColor(Color.parseColor("#FF0000"));
            }else{
                holder.circularProgressViewKt.setColor(Color.parseColor("#4A8DE6"));
            }



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
        public TextView markPer;
        public CircularProgressView circularProgressViewKt;
        public TextView markR;

        public HourViewHolder(View itemView) {
            super(itemView);
            subCode = itemView.findViewById(R.id.subjectCode);
            sub = itemView.findViewById(R.id.subject);
            markPer = itemView.findViewById(R.id.MarkPer);
            circularProgressViewKt = itemView.findViewById(R.id.attProgress);
            markR = itemView.findViewById(R.id.markR);

        }
    }
}
