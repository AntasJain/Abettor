package com.jainantas.abettor.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.ExpenseModel;
import com.jainantas.abettor.R;

public class ExpenseHolder extends RecyclerView.ViewHolder
{
    private View view;

    public ExpenseHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }
    public void setDate(String date)
    {
        Log.e("Date",date);
        TextView datex=view.findViewById(R.id.dateExpense);
        datex.setText(date);
    }
    public void setDescription(String description){
        TextView descriptionx=view.findViewById(R.id.details);
        descriptionx.setText(description);
    }
    public void setINR(Long inr)
    {
        TextView money=view.findViewById(R.id.money);
        money.setText("₹"+Long.toString(inr));
    }
}
