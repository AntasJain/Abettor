package com.jainantas.abettor.Activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jainantas.abettor.Adapters.RandomAdapter;
import com.jainantas.abettor.Adapters.SugarAdapter;
import com.jainantas.abettor.Adapters.SugarHolder;
import com.jainantas.abettor.Models.RandomModel;
import com.jainantas.abettor.Models.SugarModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BloodSugarTracker extends AppCompatActivity {
    SugarAdapter adapter;
    RandomAdapter randomAdapter;
    RecyclerView recyclerView1,recyclerView2;
    FloatingActionButton fab;
    HashMap<String ,Object> sugarMap,sugarRandom;

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bs);
        sugarMap=new HashMap<>();
        sugarRandom=new HashMap<>();
        recyclerView1=findViewById(R.id.recycler_1);
        recyclerView2=findViewById(R.id.recycler_2);
        fab=findViewById(R.id.add_bloodSugar_data);
        Query query = FirebaseFirestore.getInstance().collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null))
                .document("Health").collection("BSN").orderBy("DateRecord", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<SugarModel> options=new FirestoreRecyclerOptions.Builder<SugarModel>()
                .setQuery(query,SugarModel.class)
                .build();
        Query rQuery = FirebaseFirestore.getInstance().collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Health").collection("BSR").orderBy("DateRandom", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<RandomModel> rOption=new FirestoreRecyclerOptions.Builder<RandomModel>()
                .setQuery(rQuery,RandomModel.class)
                .build();


        adapter=new SugarAdapter(options);
        randomAdapter=new RandomAdapter(rOption);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView1);
        new ItemTouchHelper(callback1).attachToRecyclerView(recyclerView2);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(adapter);
        recyclerView2.setAdapter(randomAdapter);
        adapter.startListening();
        randomAdapter.startListening();
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               chooseType();
           }
       });
    }
    public void chooseType(){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Select Record Type");
            builder.setMessage("• Select Random if you want to input the results of random testing.\n• Select Scheduled if you want to input the results of Fasting Blood Sugar (FBS) and PostPrandial Blood Sugar (PPBS).");
            builder.setNegativeButton("Random", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startRandomProcedure();
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Scheduled", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startProcedure();
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
            //startProcedure();
        }
    public void startProcedure()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enter Data");
        final View customView=getLayoutInflater().inflate(R.layout.add_bs, null);
        builder.setView(customView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText t1=customView.findViewById(R.id.fbs);
                EditText t2=customView.findViewById(R.id.ppbs);
                String date= new SimpleDateFormat("d-MM-yyyy, HH:mm").format(Calendar.getInstance().getTime());
                String fbs=t1.getText().toString();
                String ppbs=t2.getText().toString();
                setUp(fbs,ppbs,date);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void setUp(String fbs, String ppbs, String date)
    {
        sugarMap.put("DateRecord",date);
        sugarMap.put("Fasting",fbs);
        sugarMap.put("PostPrandial",ppbs);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        FirebaseFirestore.getInstance().collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Health").collection("BSN").document(Date)
                .set(sugarMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Data Not Added",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void startRandomProcedure(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Random Testing Data...");
        final View customView=getLayoutInflater().inflate(R.layout.add_random, null);
        builder.setView(customView);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText=customView.findViewById(R.id.randomInput);
                String date= new SimpleDateFormat("d-MM-yyyy, HH:mm").format(Calendar.getInstance().getTime());
                String random_input=editText.getText().toString();
                setUpRandom(random_input,date);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void setUpRandom(String random,String date)
    {
        sugarRandom.put("Values",random);
        sugarRandom.put("DateRandom",date);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        FirebaseFirestore.getInstance().collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Health").collection("BSR").document(Date)
                .set(sugarRandom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Data Not Added",Toast.LENGTH_SHORT).show();
            }
        });

    }
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.parseColor("#ff0055"))
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
                    .addSwipeRightBackgroundColor(Color.parseColor("#ff0055")).addSwipeRightActionIcon(R.drawable.ic_trash).create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int docId = viewHolder.getAdapterPosition();
            adapter.deleteItem(docId);

        }
    };
    ItemTouchHelper.SimpleCallback callback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.parseColor("#ff0055"))
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
                    .addSwipeRightBackgroundColor(Color.parseColor("#ff0055")).addSwipeRightActionIcon(R.drawable.ic_trash).create().decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int docId = viewHolder.getAdapterPosition();
            randomAdapter.deleteItem(docId);

        }
    };
}