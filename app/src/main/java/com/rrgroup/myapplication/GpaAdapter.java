package com.rrgroup.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GpaAdapter extends RecyclerView.Adapter<GpaAdapter.ViewHolder> {

    private List<String> dataList;
    String[] gradeValues = {"Grade","O", "A+", "A", "B+", "B", "C", "F"};
    String[] creditValues = {"Credit","1", "2", "3", "4", "5", "6"};
    private RecyclerView recyclerView;

    private List<String> selectedGrades = new ArrayList<>(Collections.nCopies(8, ""));
    private List<String> selectedCredits = new ArrayList<>(Collections.nCopies(7, ""));


    public GpaAdapter(List<String> dataList,RecyclerView recyclerView) {
        this.dataList = dataList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gpa_item, parent, false);
        return new ViewHolder(view);
    }

    public String getSelectedGrade(int position) {
        if (position < selectedGrades.size()) {
            return selectedGrades.get(position);
        }
        return "";
    }

    public String getSelectedCredit(int position) {
        if (position < selectedCredits.size()) {
            return selectedCredits.get(position);
        }
        return "";
    }


        @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String  sl_no = dataList.get(position);
        holder.sl.setText(sl_no);


        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(holder.itemView.getContext(), R.layout.custom_spinner, gradeValues);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.gradeSpinner.setAdapter(gradeAdapter);

        ArrayAdapter<String> creditAdapter = new ArrayAdapter<>(holder.itemView.getContext(), R.layout.custom_spinner, creditValues);
        creditAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.creditSpinner.setAdapter(creditAdapter);


        holder.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedGrades.set(position, gradeValues[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        holder.creditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCredits.set(position, creditValues[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Spinner creditSpinner;
        Spinner gradeSpinner;
        TextView sl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            creditSpinner = itemView.findViewById(R.id.credit);
            gradeSpinner = itemView.findViewById(R.id.grade);
            sl= itemView.findViewById(R.id.sl_no);
        }
    }
}

