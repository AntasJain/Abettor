package com.jainantas.abettor.Activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.AssetsAdapter;
import com.jainantas.abettor.Adapters.SugarAdapter;
import com.jainantas.abettor.Models.AssetsModel;
import com.jainantas.abettor.Models.RandomModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Assets extends AppCompatActivity {
    FloatingActionButton fab;
    String[] assets = {"CryptoCurrency", "Gold", "Stock", "Property", "Silver", "Mutual Funds"};
    RecyclerView recyclerView;
    String item;
    FirebaseFirestore db;
    HashMap<String, Object> assetMap;
    AssetsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_assets);
        db = FirebaseFirestore.getInstance();
        assetMap = new HashMap<>();
        fab = findViewById(R.id.addAsset);
        recyclerView = findViewById(R.id.assetRecycler);
        Query query=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Wealth")
                .collection("Assets").orderBy("DateStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<AssetsModel> options=new FirestoreRecyclerOptions.Builder<AssetsModel>()
                .setQuery(query,AssetsModel.class)
                .build();

        adapter=new AssetsAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Asset");
        View view = getLayoutInflater().inflate(R.layout.add_asset, null);
        TextView type = view.findViewById(R.id.spinner_assets);
        EditText name = view.findViewById(R.id.asset_name);
        EditText qty = view.findViewById(R.id.quantity);
        EditText price = view.findViewById(R.id.price);

        builder.setView(view);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                assetMap.put("Type", type.getText().toString());
                assetMap.put("Name", name.getText().toString());
                assetMap.put("Quantity", qty.getText().toString());
                assetMap.put("Price", price.getText().toString());
                assetMap.put("DateStamp", new SimpleDateFormat("dMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
                setData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setData() {
        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId, null))
                .document("Wealth").collection("Assets").document(assetMap.get("DateStamp").toString())
                .set(assetMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_SHORT).show();

            }
        });
    }
    ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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
