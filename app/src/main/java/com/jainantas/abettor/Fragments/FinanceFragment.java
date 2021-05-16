package com.jainantas.abettor.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jainantas.abettor.Activities.Alerts;
import com.jainantas.abettor.Activities.Assets;
import com.jainantas.abettor.Activities.Bank;
import com.jainantas.abettor.Activities.Cards;
import com.jainantas.abettor.Activities.Invoices;
import com.jainantas.abettor.Activities.ViewExpenses;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executor;


public class FinanceFragment extends Fragment {
    Executor executor;
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ImageButton setSpent;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView usd, cad, eur, aud, dateCurrency, lastSpent,monthlyspent,totalSpent;
    EditText spent, desc, dateSpent;
    FirebaseFirestore database;
    CardView viewExpenses,cards,invoices,asset,bank,alerts;
    String month="";
    long sum=0,sum1=0;
    public FinanceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor=ContextCompat.getMainExecutor(getContext());
        biometricPrompt=new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(new Intent(getActivity(),Cards.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate Yourself")
                .setSubtitle("Sensitive Data Ahead!")
                .setNegativeButtonText("Cancel")
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  setLastSpent();
        setLastMonthSpent();
        setTotalSpent();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootVIew = inflater.inflate(R.layout.fragment_finance, container, false);
        month=new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        database=FirebaseFirestore.getInstance();
        viewExpenses=rootVIew.findViewById(R.id.viewExpenses);
        setSpent = rootVIew.findViewById(R.id.setExpense);
        spent = rootVIew.findViewById(R.id.money);
        desc = rootVIew.findViewById(R.id.spendDesc);
        cards=rootVIew.findViewById(R.id.myCards);
        dateSpent = rootVIew.findViewById(R.id.dateExp);
        asset=rootVIew.findViewById(R.id.assetManager);
        alerts=rootVIew.findViewById(R.id.setAlerts);
        swipeRefreshLayout = rootVIew.findViewById(R.id.swipeLayout);
        aud = rootVIew.findViewById(R.id.audField);
        cad = rootVIew.findViewById(R.id.cadField);
        usd = rootVIew.findViewById(R.id.usdField);
        eur = rootVIew.findViewById(R.id.eurField);
        invoices=rootVIew.findViewById(R.id.invoices);
        dateCurrency = rootVIew.findViewById(R.id.dateCurr);
        lastSpent=rootVIew.findViewById(R.id.lastSpent);
        totalSpent=rootVIew.findViewById(R.id.totalSpent);
        monthlyspent=rootVIew.findViewById(R.id.monthSpent);
        bank=rootVIew.findViewById(R.id.openBank);
        aud.setText(SharedPreferencesHelper.getCurrency(PrefsData.AUD, "AUD 1:\t"));
        usd.setText(SharedPreferencesHelper.getCurrency(PrefsData.USD, "USD 1:\t"));
        cad.setText(SharedPreferencesHelper.getCurrency(PrefsData.CAD, "CAD 1:\t"));
        eur.setText(SharedPreferencesHelper.getCurrency(PrefsData.EUR, "EUR 1:\t"));
        dateCurrency.setText(SharedPreferencesHelper.getCurrency(PrefsData.DC, "Swipe to get data"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getExchange();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Bank.class));
            }
        });
        setSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecker();
            }
        });
        dateSpent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        viewExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openExpenseViewer();
            }
        });
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               biometricPrompt.authenticate(promptInfo);
            }
        });
        invoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Invoices.class);
            startActivity(intent);
            }
        });
        asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Assets.class));
            }
        });
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Alerts.class));
            }
        });
        setLastSpent();
        setLastMonthSpent();
        setTotalSpent();

        return rootVIew;
    }
    public void setLastSpent(){
        database.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth")
                .collection("Expenses").orderBy("Date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot snapshot:task.getResult())
                    {
                        lastSpent.setText(snapshot.get("INR").toString());
                    }
                }
            }
        });
    }
    public void setLastMonthSpent(){
        sum=0;
        String Month=Integer.toString(Integer.parseInt(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime())));
        //Toast.makeText(getContext(),Month,Toast.LENGTH_LONG).show();
        database.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth").collection("Expenses")
                .whereEqualTo("Month",Month).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot snapshot: task.getResult())
                    {   sum+=Long.parseLong(snapshot.get("INR").toString());
                    }
                }
                monthlyspent.setText(Long.toString(sum));
            }
        });
    }
    public void setTotalSpent(){
        sum1=0;
        database.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null)).document("Wealth").collection("Expenses")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        sum1+=Long.parseLong(snapshot.get("INR").toString());
                    }
                }
                totalSpent.setText(Long.toString(sum1));
            }
        });
    }
    public void openExpenseViewer(){
        Intent intent=new Intent(getActivity(), ViewExpenses.class);
        startActivity(intent);

    }

    public void getExchange() {
        String USD = "https://v6.exchangerate-api.com/v6/key/USD";
        String AUD = "https://v6.exchangerate-api.com/v6/key/AUD";
        String CAD = "https://v6.exchangerate-api.com/v6/key/CAD";
        String EUR = "https://v6.exchangerate-api.com/v6/key/EUR";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, USD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("conversion_rates");
                    String inr = String.valueOf(jsonObject.getDouble("INR"));
                    //Toast.makeText(getContext(), inr, Toast.LENGTH_LONG).show();
                    usd.setText("USD 1:\t₹" + inr);
                    SharedPreferencesHelper.setCurrency(PrefsData.USD, usd.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, AUD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("conversion_rates");
                    String inr = String.valueOf(jsonObject.getDouble("INR"));
                    aud.setText("AUD 1:\t₹" + inr);
                    SharedPreferencesHelper.setCurrency(PrefsData.AUD, aud.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, CAD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("conversion_rates");
                    String inr = String.valueOf(jsonObject.getDouble("INR"));
                    cad.setText("CAD 1:\t₹" + inr);
                    SharedPreferencesHelper.setCurrency(PrefsData.CAD, cad.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, EUR, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("conversion_rates");
                    String inr = String.valueOf(jsonObject.getDouble("INR"));
                    eur.setText("EUR 1:\t₹" + inr);
                    SharedPreferencesHelper.setCurrency(PrefsData.EUR, eur.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        queue.add(request1);
        queue.add(request2);
        queue.add(request3);
        String date = new SimpleDateFormat("d-MM-yyyy, HH:mm").format(Calendar.getInstance().getTime());
        SharedPreferencesHelper.setCurrency(PrefsData.DC, "Last Updated:\t" + date);
        dateCurrency.setText("Last Updated:\t" + date);

    }

    public void setChecker() {
        if(spent.getText().toString().isEmpty())
            spent.setError("spent?");
        if (desc.getText().toString().isEmpty())
            desc.setError("Description..");
        if(dateSpent.getText().toString().isEmpty())
            dateSpent.setError("Select Date");
        if (!spent.getText().toString().isEmpty() && !desc.getText().toString().isEmpty() && !dateSpent.getText().toString().isEmpty())
        {
            setMoneyData();
        }
    }
    public void setDate(){
        final Calendar calendar=Calendar.getInstance();
        int mYear=calendar.get(Calendar.YEAR);
       int  mMonth=calendar.get(Calendar.MONTH);
        int mDay=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               String date=Integer.toString(dayOfMonth)+"-"+Integer.toString(month+1)+"-"+Integer.toString(year);
                    dateSpent.setText(date);

            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();

    }
    public void setMoneyData(){
        HashMap<String,Object> map=new HashMap<>();
        long spentMoney=Long.parseLong(spent.getText().toString());
        String descript=desc.getText().toString();
        String date=dateSpent.getText().toString();
        String sub=date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));
        Toast.makeText(getContext(),sub,Toast.LENGTH_LONG).show();
        map.put("INR",spentMoney);
        map.put("Description",descript);
        map.put("Date",date);
        map.put("Month",sub);
        String Date = new SimpleDateFormat("d-MM-yyyy, HH:mm:ss").format(Calendar.getInstance().getTime());
        database.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document("Wealth").collection("Expenses").document(Date).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Added",Toast.LENGTH_SHORT).show();
                        doThings();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void doThings(){
        setLastSpent();
        setLastMonthSpent();
        setTotalSpent();
        /*spent.setFocusable(false);
        desc.setFocusable(false);*/
        dateSpent.setFocusable(false);
        spent.setText("");
        desc.setText("");
        dateSpent.setText("");
    }

}
