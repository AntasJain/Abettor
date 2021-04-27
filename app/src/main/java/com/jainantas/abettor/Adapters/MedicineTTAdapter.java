package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.MedicineTTModel;
import com.jainantas.abettor.R;

public  class MedicineTTAdapter extends FirestoreRecyclerAdapter<MedicineTTModel,MedicineTTHolder>
{
    public MedicineTTAdapter(@NonNull FirestoreRecyclerOptions<MedicineTTModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicineTTHolder holder, int position, @NonNull MedicineTTModel model) {
        holder.setStartDate(model.getStartDate());
        holder.setEndDate(model.getEndDate());
        holder.setMedicine(model.getMedicine());
        holder.setDescription(model.getDescription());
        holder.setTimes(model.getTimes());
        holder.setDays(model.getDays());

    }

    @NonNull
    @Override
    public MedicineTTHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine,parent,false);
        return  new MedicineTTHolder(view);
    }
    public void deleteItem(int pos){
        getSnapshots().getSnapshot(pos).getReference().delete();
    }
}
