package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.AssetsModel;
import com.jainantas.abettor.R;

public class AssetsAdapter extends FirestoreRecyclerAdapter<AssetsModel,AssetsViewHolder> {
    public AssetsAdapter(@NonNull FirestoreRecyclerOptions<AssetsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AssetsViewHolder holder, int position, @NonNull AssetsModel model) {
        holder.setName(model.getName());
        holder.setPrice(model.getPrice());
        holder.setQuantity(model.getQuantity());
        holder.setType(model.getType());
    }

    @NonNull
    @Override
    public AssetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asset, parent, false);
        return new AssetsViewHolder(view);
    }
    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();

    }
}
