package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.BankModel;
import com.jainantas.abettor.R;

public class BankAdapter extends FirestoreRecyclerAdapter<BankModel,BankHolder> {
    public BankAdapter(@NonNull FirestoreRecyclerOptions<BankModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BankHolder holder, int position, @NonNull BankModel model) {
      holder.setBank(model.getBank());
        holder.setAccount(model.getAccount());
        holder.setIfsc(model.getIfsc());
    }

    @NonNull
    @Override
    public BankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank,parent,false);
        return new BankHolder(view);

    }
}
