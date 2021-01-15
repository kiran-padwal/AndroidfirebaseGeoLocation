package com.example.firebaseproductlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListStoreActivity extends AppCompatActivity {
    List<StoreModel> storeModelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    // layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mAddBtn;
    Button mMapsBtn;

    // firestore instance
    FirebaseFirestore db;
    StoreAdapter adapter;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Stores Data");
        }
        // init firebase firestore
        db = FirebaseFirestore.getInstance();

        // initialize views
        mRecyclerView = findViewById(R.id.store_recycler_view);

        mAddBtn = findViewById(R.id.storeAddBtn);

        // set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        pd = new ProgressDialog(this);
        // show data in recycler view
        showData();

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListStoreActivity.this,AddStoreActivity.class));
                finish();
            }
        });

    }

    private void showData() {
        // set title of progress bar
        pd.setTitle("getting Stores data from firebase firestore.....");
        pd.show();
        db.collection("Stores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        storeModelList.clear();
                        // called when data is retrived
                        pd.dismiss();
                        // loop the data
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            StoreModel storeModel = new StoreModel(
                                    documentSnapshot.getString("sId"),
                                    documentSnapshot.getString("sName"),
                                    documentSnapshot.getString("sDesc"),
                                    documentSnapshot.getString("sRadius"),
                                    documentSnapshot.getString("sLat"),
                                    documentSnapshot.getString("sLong"));
                            storeModelList.add(storeModel);
                        }
                        // adapter
                        adapter = new StoreAdapter(ListStoreActivity.this,storeModelList);

                        // set adapter to recycler view
                        mRecyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // called if there is any error
                pd.dismiss();
                Toast.makeText(ListStoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteData(int index){
        // set title of progress bar
        pd.setTitle("Deleting Store data from firebase firestore.....");
        pd.show();
        db.collection("Stores").document(storeModelList.get(index).getsId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ListStoreActivity.this, "Store Deleted", Toast.LENGTH_SHORT).show();
                        // update data list
                        showData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListStoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
}