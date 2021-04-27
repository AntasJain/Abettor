package com.jainantas.abettor.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.jainantas.abettor.BuildConfig;
import com.jainantas.abettor.R;

import java.io.File;
import java.io.FileOutputStream;

public class ViewImage extends AppCompatActivity {
    ImageView img;
    String TITLE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);
        Intent intent=getIntent();
        String URL=intent.getStringExtra("URL");
         TITLE=intent.getStringExtra("TITLE");
       img =findViewById(R.id.setLargeImage);
        TextView title=findViewById(R.id.setTitle);
        ImageButton btn=findViewById(R.id.share);
        Glide.with(this).load(URL).into(img);
        title.setText(TITLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();

            }
        });
    }
    public void shareImage(){
        Bitmap bitmap=getBitmapFrom(img);
        try{
            File file=new File(this.getExternalCacheDir(),TITLE+".jpeg");
            FileOutputStream f=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,f);
            f.flush();
            f.close();
            file.setReadable(true,false);
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ViewImage.this, BuildConfig.APPLICATION_ID+".provider",file));
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent,"Share Document To"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private Bitmap getBitmapFrom(View view)
    {
        Bitmap returnBitmap=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(returnBitmap);
        Drawable bgDrawable=view.getBackground();
        if(bgDrawable!=null)
        {
            bgDrawable.draw(canvas);
        }
        else {
            canvas.drawColor(Color.BLACK);
        }
        view.draw(canvas);
        return returnBitmap;
    }
}
