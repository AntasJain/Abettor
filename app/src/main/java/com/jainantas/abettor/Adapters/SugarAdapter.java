package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.SugarModel;
import com.jainantas.abettor.R;

public class SugarAdapter  extends FirestoreRecyclerAdapter<SugarModel,SugarHolder> {
    public SugarAdapter(@NonNull FirestoreRecyclerOptions<SugarModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SugarHolder holder, int position, @NonNull SugarModel model) {
        holder.setDateRecord(model.getDateRecord());
        holder.setFasting(model.getFasting());
        holder.setPostPrandial(model.getPostPrandial());
    }

    @NonNull
    @Override
    public SugarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bloodsugar,parent,false);
        return new SugarHolder(view);
    }
    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();

    }
}
