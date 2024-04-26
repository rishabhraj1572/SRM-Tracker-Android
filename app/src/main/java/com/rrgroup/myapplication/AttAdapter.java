package com.rrgroup.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.github.guilhe.views.CircularProgressView;
import com.github.guilhe.views.CircularProgressViewKt;

import org.w3c.dom.Text;

import java.util.List;

public class AttAdapter extends RecyclerView.Adapter<AttAdapter.HourViewHolder> {

    private List<String> sub_code;
    private List<String> sub;
    private List<String> per;
    private List<String> max;
    private List<String> att;
    private boolean isScholarship;

    public AttAdapter(List<String> sub_code,List<String> sub, List<String> per,List<String> max, List<String> att, boolean isScholarship) {
        this.sub_code = sub_code;
        this.sub = sub;
        this.per =per;
        this.max = max;
        this.att =att;
        this.isScholarship = isScholarship;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        try {
            String subjectCode = sub_code.get(position);
            holder.subCode.setText(subjectCode);
            String subject = sub.get(position);
            holder.sub.setText(subject);
            String  att_per = per.get(position);
            holder.attendance.setText(att_per+"%");
            holder.circularProgressViewKt.setProgress(Float.valueOf(att_per),true);

            double maxHrs= Integer.parseInt(max.get(position));
            double attHrs = Integer.parseInt(att.get(position));
            holder.attRatio.setText((int)attHrs+"/"+(int)maxHrs);

            int base,b;
            if(isScholarship){
                base = 90;
                b=10;
            }else {
                base = 75;
                b=25;
            }

            double m = (int)((attHrs*100)-(base*maxHrs))/base;
            double n = (int)((base*maxHrs)-(100*attHrs))/b;
            if(m>0){
                if(m==1){
                    holder.additional.setText("You are on track, You may leave next class");
                }else {
                    holder.additional.setText("You are on track, You may leave "+(int)m+" classes");
                }
            }else if(m==0){
                holder.additional.setText("You are on track, You can't miss the next class");
            }else {
                if(n==1){
                    holder.additional.setText("Attend next class to get back on track");
                }else {
                    holder.additional.setText("Attend next "+(int)n+" classes to get back on track");
                }
            }

            if(Float.valueOf(att_per)<base){
                holder.circularProgressViewKt.setColor(Color.parseColor("#FF0000"));
            }else{
                holder.circularProgressViewKt.setColor(Color.parseColor("#4A8DE6"));
            }
            if(subject.equals("TOTAL")){
                holder.attRatio.setTypeface(null, Typeface.BOLD);
            }else {
                holder.attRatio.setTypeface(null, Typeface.NORMAL);
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
        public TextView attendance;
        public CircularProgressView circularProgressViewKt;
        public TextView additional;
        public TextView attRatio;

        public HourViewHolder(View itemView) {
            super(itemView);
            subCode = itemView.findViewById(R.id.subjectCode);
            sub = itemView.findViewById(R.id.subject);
            attendance = itemView.findViewById(R.id.attPer);
            circularProgressViewKt = itemView.findViewById(R.id.attProgress);
            additional = itemView.findViewById(R.id.additional);
            attRatio = itemView.findViewById(R.id.attR);

        }
    }
}
