package com.jainantas.abettor.Fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.MedicineTTAdapter;
import com.jainantas.abettor.Models.MedicineTTModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Timetable extends Fragment {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    MedicineTTAdapter adapter;
    Query query;

    public Timetable() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        query = db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null))
                .document("Health").collection("TimeTable").orderBy("StartDate", Query.Direction.ASCENDING);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timetable, container, false);

        recyclerView = rootView.findViewById(R.id.recyclertt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirestoreRecyclerOptions<MedicineTTModel> options = new FirestoreRecyclerOptions
                .Builder<MedicineTTModel>().setQuery(query, MedicineTTModel.class).build();
        adapter = new MedicineTTAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        new ItemTouchHelper(callback1).attachToRecyclerView(recyclerView);
        Toast.makeText(getContext(),query.toString(),Toast.LENGTH_SHORT).show();
        return rootView;
    }
    ItemTouchHelper.SimpleCallback callback1 = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                 //   .addSwipeLeftBackgroundColor(Color.parseColor("#ff0055"))
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
                    .addSwipeRightBackgroundColor(Color.parseColor("#001122"))
                    .addSwipeRightActionIcon(R.drawable.ic_trash).create().decorate();

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