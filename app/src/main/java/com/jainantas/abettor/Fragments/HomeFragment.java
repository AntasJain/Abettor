package com.jainantas.abettor.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.telecom.Call;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.jainantas.abettor.Activities.DetailsActivity;
import com.jainantas.abettor.Activities.HomeActivity;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
ImageView dp,edit;
TextView name,email,age,weight,height,address,city,contact,dob,bgrp;
CardView editDetails;
Button detailsbutton;
FirebaseFirestore db;
ProgressBar progressBar;
    Map<String,String> map;

    public HomeFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseFirestore.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_home,container,false);
        progressBar=rootView.findViewById(R.id.progressbar);
        dp=rootView.findViewById(R.id.dpImage);

        editDetails=rootView.findViewById(R.id.editDetails);
        age=rootView.findViewById(R.id.ageUser);
        dob=rootView.findViewById(R.id.dateUser);
        height=rootView.findViewById(R.id.heightUser);
        weight=rootView.findViewById(R.id.weightUser);
        address=rootView.findViewById(R.id.addressUser);
        city=rootView.findViewById(R.id.cityUser);
        contact=rootView.findViewById(R.id.contactUser);
        bgrp=rootView.findViewById(R.id.bloodgrp);
        map=new HashMap<>();
        String img=SharedPreferencesHelper.getUserInfo(PrefsData.picLink,null);
        if(img!=null){
            Glide.with(getContext()).load(img).into(dp);
        }
        name=rootView.findViewById(R.id.textName);
        email=rootView.findViewById(R.id.emailText);
        name.setText(SharedPreferencesHelper.getUserInfo(PrefsData.userName,null));
        email.setText(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null));


        doSet();
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getActivity(), DetailsActivity.class);
               startActivity(intent);
            }
        });
        return rootView;
    }
    public void doSet(){

        DocumentReference reference=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document(PrefsData.userDetails);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists())
                    {
                        age.setText(documentSnapshot.get(PrefsData.Age).toString

                                ());
                        address.setText(documentSnapshot.get(PrefsData.Address).toString());
                        bgrp.setText(documentSnapshot.get(PrefsData.BG).toString());
                        city.setText(documentSnapshot.get(PrefsData.City).toString());
                        contact.setText(documentSnapshot.get(PrefsData.Contact).toString());
                        dob.setText(documentSnapshot.get(PrefsData.DOB).toString());
                        height.setText(documentSnapshot.get(PrefsData.Heigth).toString()+" Feets");
                        weight.setText(documentSnapshot.get(PrefsData.Weight).toString()+" Kg");
                        progressBar.setVisibility(View.GONE);
                    }
                       // map=documentSnapshot.getData();
                        else
                            Log.e("Data", "no document found");}
                    else {
                        Log.d("Data",task.getException().toString());
                    }
                }

        });
        //Log.e("Map",map.toString());
    }


}