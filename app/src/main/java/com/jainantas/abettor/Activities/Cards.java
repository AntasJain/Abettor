package com.jainantas.abettor.Activities;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jainantas.abettor.Adapters.CardAdapter;
import com.jainantas.abettor.Models.CardModel;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Cards extends AppCompatActivity {
    FloatingActionButton button;
    TextView head;
    CardAdapter adapter;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    HashMap<String,Object> cardMap;

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
        setContentView(R.layout.activity_cards);
        recyclerView=findViewById(R.id.cardsRecycler);
        cardMap=new HashMap<>();
        db=FirebaseFirestore.getInstance();
        button=findViewById(R.id.addCards);
        head=findViewById(R.id.head);
        Query query=db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth")
                .collection("Cards").orderBy("YY", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CardModel> options=new FirestoreRecyclerOptions.Builder<CardModel>().setQuery(query,CardModel.class).build();
        adapter=new CardAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert();
            }
        });
    }
    public void createAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Add Card");
        final View customView=getLayoutInflater().inflate(R.layout.add_card, null);
        builder.setView(customView);
        Button btn=customView.findViewById(R.id.encr);
        EditText bank=customView.findViewById(R.id.bank);
        EditText mm=customView.findViewById(R.id.mm);
        EditText yy=customView.findViewById(R.id.yy);
        EditText cvv=customView.findViewById(R.id.cvv);

        EditText cno=customView.findViewById(R.id.numCard);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNo=cno.getText().toString();
                String mon=mm.getText().toString();
                String yr=yy.getText().toString();
                String bankName=bank.getText().toString();
                String encryptedCVV="";
                try{
                     encryptedCVV=AESCrypt.encrypt(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null),cvv.getText().toString());
                }
                catch (GeneralSecurityException e){
                    e.printStackTrace();
                }
                toPutInMap(bankName,cardNo,encryptedCVV,mon,yr);
            }
        });
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void toPutInMap(String bankName,String cardNo, String cvv, String mon, String yr)
    {
        cardMap.put("Bank",bankName);
        cardMap.put("Card",cardNo);
        cardMap.put("Cvv",cvv);
        cardMap.put("MM",mon);
        cardMap.put("YY",yr);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth").collection("Cards").document(Date)
                .set(cardMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Card Saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error Saving Card",Toast.LENGTH_LONG).show();
            }
        });


    }
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(getApplicationContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(Color.parseColor("#ff0055"))
                    .addSwipeLeftActionIcon(R.drawable.ic_trash)
//                    .addSwipeRightActionIcon(R.drawable.ic_seepassword)
//                    .addSwipeRightBackgroundColor(Color.parseColor("#99ff99"))
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
            if(direction==ItemTouchHelper.LEFT)
            adapter.deleteItem(docId);
           // else  if(direction==ItemTouchHelper.RIGHT)


        }
    };
}
