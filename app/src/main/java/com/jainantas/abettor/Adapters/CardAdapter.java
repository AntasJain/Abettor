package com.jainantas.abettor.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.CardModel;
import com.jainantas.abettor.R;

public class CardAdapter extends FirestoreRecyclerAdapter<CardModel,CardHolder> {
    public CardAdapter(@NonNull FirestoreRecyclerOptions<CardModel> options) {
        super(options);
    }
    CardHolder cardHolder; CardModel cardModel;
    @Override
    protected void onBindViewHolder(@NonNull CardHolder holder, int position, @NonNull CardModel model) {
        cardHolder=holder;
        cardModel=model;
        holder.setBank(model.getBank());
        holder.setCard(model.getCard());
        holder.setCvv(model.getCvv());
        holder.setMM(model.getMM());
        holder.setYY(model.getYY());
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        return new CardHolder(view);
    }
    public void deleteItem(int pos) {
      //  getSnapshots().getSnapshot(pos).getReference().delete();
        Log.e("Cvv",getSnapshots().getSnapshot(pos).get("Cvv").toString());
    }

}
