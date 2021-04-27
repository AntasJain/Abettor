package com.jainantas.abettor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.jainantas.abettor.Models.ImageModel;
import com.jainantas.abettor.R;

public class ImageAdapter extends FirestoreRecyclerAdapter<ImageModel, ImageHolder> {
    public ImageAdapter(@NonNull FirestoreRecyclerOptions<ImageModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageHolder holder, int position, @NonNull ImageModel model) {
            holder.setLink(model.getLink());
            holder.setTitle(model.getTitle());
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new ImageHolder(view);
    }
    public void deleteItem(int pos) {
        getSnapshots().getSnapshot(pos).getReference().delete();
    }
}
