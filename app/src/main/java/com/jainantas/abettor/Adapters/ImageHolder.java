package com.jainantas.abettor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jainantas.abettor.Activities.ViewImage;
import com.jainantas.abettor.R;

public class ImageHolder extends RecyclerView.ViewHolder {
    View mView;
    Context context;
    String url;
    String title;
    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        mView=itemView;
       mView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(mView.getContext(), ViewImage.class);
               intent.putExtra("URL",url);
               intent.putExtra("TITLE",title);
               mView.getContext().startActivity(intent);
           }
       });

    }
    public void setLink(String link)
    {   url=link;
        ImageView imageView=mView.findViewById(R.id.invoiceImage);
        Glide.with(mView.getContext()).load(link).into(imageView);
    }
    public void setTitle(String title)
    {   this.title=title;
        TextView textView=mView.findViewById(R.id.invoice_Title);
        textView.setText(title);
    }


}
