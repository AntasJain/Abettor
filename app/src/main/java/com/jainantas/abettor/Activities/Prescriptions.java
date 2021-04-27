package com.jainantas.abettor.Activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.ImageAdapter;
import com.jainantas.abettor.Models.ImageModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Prescriptions extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ImageAdapter adapter;
    TextView heading;
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
        setContentView(R.layout.layout_images_add);
        db=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.recyclerImage);
        fab=findViewById(R.id.addNew);
        SharedPreferencesHelper.init(this);
        heading=findViewById(R.id.headd);
        heading.setText("Prescription");
        String doctype="Prescription";
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UploadImage.class);
                SharedPreferencesHelper.setUserInfo("From",doctype);
                SharedPreferencesHelper.setUserInfo("To","Health");
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Health").collection("Prescription")
                .orderBy("Time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ImageModel> options=new FirestoreRecyclerOptions.Builder<ImageModel>().setQuery(query, ImageModel.class).build();
        adapter=new ImageAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }
    ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.parseColor("#ff0055"))
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
                    .create().decorate();
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
