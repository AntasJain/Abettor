package com.jainantas.abettor.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jainantas.abettor.R;

public class AssetsViewHolder extends RecyclerView.ViewHolder {
    View v;
    public AssetsViewHolder(@NonNull View itemView) {
        super(itemView);
        v=itemView;
    }
    public void setPrice(String p){
        TextView t=v.findViewById(R.id.assetPrice);
        t.setText("Price: ₹"+p);
    }
    public void setName(String t){
        TextView n=v.findViewById(R.id.nameAsset);
        n.setText(t);
    }
    public void setQuantity(String q){
        TextView aq=v.findViewById(R.id.assetQuantity);
        aq.setText("Quantity: "+q);
    }
    public void setType(String t){
        TextView ty=v.findViewById(R.id.type);
        ty.setText(t);
    }
}
