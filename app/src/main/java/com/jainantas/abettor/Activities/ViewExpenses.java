package com.jainantas.abettor.Activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.ExpenseAdapter;
import com.jainantas.abettor.Models.ExpenseModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ViewExpenses extends AppCompatActivity {
FirebaseFirestore dBase;
RecyclerView recyclerView;
ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        dBase=FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.expRec);
        Query query=dBase.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Wealth").collection("Expenses").orderBy("Date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ExpenseModel> options=new FirestoreRecyclerOptions.Builder<ExpenseModel>()
                .setQuery(query,ExpenseModel.class)
                .build();
        adapter=new ExpenseAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

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
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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