package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.ExpenseModel;
import com.jainantas.abettor.R;

import java.util.HashMap;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<ExpenseModel, ExpenseHolder> {

    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions<ExpenseModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseHolder holder, int position, @NonNull ExpenseModel model) {
        holder.setDate(model.getDate());
        holder.setDescription(model.getDescription());
        holder.setINR(model.getINR());

    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseHolder(view);
    }

    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();

    }
}
