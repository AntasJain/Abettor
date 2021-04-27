package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.RandomModel;
import com.jainantas.abettor.R;

public class RandomAdapter extends FirestoreRecyclerAdapter<RandomModel,RandomHolder> {
    public RandomAdapter(@NonNull FirestoreRecyclerOptions<RandomModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RandomHolder holder, int position, @NonNull RandomModel model) {
        holder.setDateRandom(model.getDateRandom());
        holder.setValues(model.getValues());
    }

    @NonNull
    @Override
    public RandomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bloodsugar_random,parent,false);
        return new RandomHolder(view);
    }
    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();

    }
}
