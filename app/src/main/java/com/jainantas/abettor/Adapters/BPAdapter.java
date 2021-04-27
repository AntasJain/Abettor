package com.jainantas.abettor.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.BPModel;
import com.jainantas.abettor.R;

import org.w3c.dom.Text;

import java.util.List;

public class BPAdapter extends FirestoreRecyclerAdapter<BPModel, BPHolder> {

    public BPAdapter(@NonNull FirestoreRecyclerOptions<BPModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull BPHolder holder, int position, @NonNull BPModel model) {
        holder.setDate(model.getDate());
        holder.setLow(model.getLow());
        holder.setHigh(model.getHigh());

    }

    @NonNull
    @Override
    public BPHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bp, parent, false);
        return new BPHolder(view);
    }

    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();

    }


}