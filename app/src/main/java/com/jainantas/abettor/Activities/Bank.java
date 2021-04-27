package com.jainantas.abettor.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.BankAdapter;
import com.jainantas.abettor.Models.BankModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Bank extends AppCompatActivity {
    FirebaseFirestore db;
    HashMap<String,Object> banks;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    BankAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bank);
        db=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.RecyclerBank);
        fab=findViewById(R.id.addBank);
        banks=new HashMap<>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBank();
            }
        });
        Query query=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth").collection("Bank")
                .orderBy("doc_id", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<BankModel> options=new FirestoreRecyclerOptions.Builder<BankModel>().setQuery(query,BankModel.class).build();
        adapter=new BankAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public void addBank()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.add_bank,null);
        builder.setTitle("Add Bank Details");
        builder.setMessage("Your data will be encrypted for security.");
        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText bName=view.findViewById(R.id.bank_Name);
                String bank=bName.getText().toString();
                EditText bAccount=view.findViewById(R.id.accountNum);
                String account=bAccount.getText().toString();
                EditText bIfsc=view.findViewById(R.id.ifsc);
                String ifsc=bIfsc.getText().toString();
                setData(bank,account,ifsc);
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    public void setData(String a,String b, String c)
    {
        String enc_b="";
        try {
            enc_b=AESCrypt.encrypt(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null),b);
        }
        catch (GeneralSecurityException e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        banks.put("Bank",a);
        banks.put("Account",enc_b);
        banks.put("Ifsc",c);
        String date=new SimpleDateFormat("dMMyyyyHHmm").format(Calendar.getInstance().getTime());
        banks.put("doc_id",date);
        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth").collection("Bank")
                .document(date)
                .set(banks).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Bank Saved",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Not Saved",Toast.LENGTH_LONG).show();
            }
        });

    }
}
