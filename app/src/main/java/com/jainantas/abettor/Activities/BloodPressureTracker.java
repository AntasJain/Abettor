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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.BPAdapter;
import com.jainantas.abettor.Adapters.BPHolder;
import com.jainantas.abettor.Models.BPModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class BloodPressureTracker extends AppCompatActivity {
    FloatingActionButton fabAdd;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    TextView noNotes;
    BPAdapter adapter;

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

    HashMap<String, Object> bpMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp);
        bpMap = new HashMap<>();

        recyclerView = findViewById(R.id.recyclerBp);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        db = FirebaseFirestore.getInstance();
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
        Query query = db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null)).document("Health")
                .collection("BP").orderBy("Date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BPModel> options = new FirestoreRecyclerOptions.Builder<BPModel>().setQuery(query, BPModel.class).build();
        adapter = new BPAdapter(options);

        recyclerView.setAdapter(adapter);
        adapter.startListening();

        fabAdd = findViewById(R.id.addBP);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    public void showAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_bp, null);
        builder.setView(customLayout);
        builder.setTitle("Add BP Data Here.");
        builder.setMessage("Add Low and High Values..");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText low = customLayout.findViewById(R.id.low);
                EditText high = customLayout.findViewById(R.id.high);
                DateFormat df = new SimpleDateFormat("d-MM-yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
               /* Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
                Toast.makeText(BloodPressureTracker.this,low.getText().toString()+" "+high.getText().toString(),Toast.LENGTH_SHORT).show();*/
                String low_ = low.getText().toString();
                String high_ = high.getText().toString();
                createDataSnapshot(low_, high_, date);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void createDataSnapshot(String low, String high, String date) {
        bpMap.put("Low", low);
        bpMap.put("High", high);
        bpMap.put("Date", date);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null))
                .document("Health").collection("BP").document(Date)
                .set(bpMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Data not saved", Toast.LENGTH_SHORT).show();
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

}
