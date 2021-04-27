package com.jainantas.abettor.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DetailsActivity extends AppCompatActivity {
Button saveDetailsBtn;
EditText age,dob,ht,wt,addr,city,contact,bgroup;
HashMap<String,String> details;

FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
         db=FirebaseFirestore.getInstance();
       details=new HashMap<>();
        saveDetailsBtn=findViewById(R.id.saveDetails);
        age=findViewById(R.id.ageInput);
        dob=findViewById(R.id.dobInput);
        ht=findViewById(R.id.heightInput);
        wt=findViewById(R.id.weightInput);
        addr=findViewById(R.id.addressInput);
        city=findViewById(R.id.cityInput);
        contact=findViewById(R.id.contactInput);
        bgroup=findViewById(R.id.bgInput);
        doSet();
        //saveDetailsBtn.setText(msg);
        saveDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(age.getText().toString().isEmpty())
                    age.setError("Input Age");
                else if(dob.getText().toString().isEmpty())
                    dob.setError("Input Date Of Birth");
                else if(bgroup.getText().toString().isEmpty())
                    dob.setError("Input Blood Group");
                else if(ht.getText().toString().isEmpty())
                    ht.setError("Input Height");
                else if(wt.getText().toString().isEmpty())
                    wt.setError("Input Weight");
                else if(addr.getText().toString().isEmpty())
                    addr.setError("Input Address");
                else if(city.getText().toString().isEmpty())
                    city.setError("Input City");
                else if(contact.getText().toString().length()!=10)
                    contact.setError("Incorrect Number Format");
                else if(contact.getText().toString().isEmpty())
                    contact.setError("Input Contact Number");

                else{
                    details.put("Age",age.getText().toString());
                    details.put("dob",dob.getText().toString());
                    details.put("bloodgroup",bgroup.getText().toString());
                    details.put("wt",wt.getText().toString());
                    details.put("ht",ht.getText().toString());
                    details.put("addr",addr.getText().toString());
                    details.put("city",city.getText().toString());
                    details.put("contact",contact.getText().toString());
                    db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document(PrefsData.userDetails).set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Error Saving Data",Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent intent=new Intent(DetailsActivity.this,HomeActivity.class);
                    startActivity(intent);
                    DetailsActivity.this.finish();
                }
            }
        });
    }
    public void doSet(){

        DocumentReference reference=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document(PrefsData.userDetails);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists())
                    {   // msg="Update Details";
                        age.setText(documentSnapshot.get(PrefsData.Age).toString());
                        addr.setText(documentSnapshot.get(PrefsData.Address).toString());
                        bgroup.setText(documentSnapshot.get(PrefsData.BG).toString());
                        city.setText(documentSnapshot.get(PrefsData.City).toString());
                        contact.setText(documentSnapshot.get(PrefsData.Contact).toString());
                        dob.setText(documentSnapshot.get(PrefsData.DOB).toString());
                        ht.setText(documentSnapshot.get(PrefsData.Heigth).toString());
                        wt.setText(documentSnapshot.get(PrefsData.Weight).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.Age,documentSnapshot.get(PrefsData.Age).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.Address,documentSnapshot.get(PrefsData.Address).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.BG,documentSnapshot.get(PrefsData.BG).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.City,documentSnapshot.get(PrefsData.City).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.Contact,documentSnapshot.get(PrefsData.Contact).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.DOB,documentSnapshot.get(PrefsData.DOB).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.Heigth,documentSnapshot.get(PrefsData.Heigth).toString());
                        SharedPreferencesHelper.setUserInfo(PrefsData.Weight,documentSnapshot.get(PrefsData.Weight).toString());
                    }
                    // map=documentSnapshot.getData();
                    else{
                        //msg="Save Details";
                        Log.e("Data", "no document found");}}
                else {
                    Log.d("Data",task.getException().toString());
                    age.setText(SharedPreferencesHelper.getUserInfo(PrefsData.Age,null));
                    addr.setText(SharedPreferencesHelper.getUserInfo(PrefsData.Address,null));
                    bgroup.setText(SharedPreferencesHelper.getUserInfo(PrefsData.BG,null));
                    city.setText(SharedPreferencesHelper.getUserInfo(PrefsData.City,null));
                    contact.setText(SharedPreferencesHelper.getUserInfo(PrefsData.Contact,null));
                    dob.setText(SharedPreferencesHelper.getUserInfo(PrefsData.DOB,null));
                    ht.setText(SharedPreferencesHelper.getUserInfo(PrefsData.Heigth,null));
                    wt.setText(SharedPreferencesHelper.getUserInfo(PrefsData.Weight,null));
                }
            }

        });
        //Log.e("Map",map.toString());
    }


}
