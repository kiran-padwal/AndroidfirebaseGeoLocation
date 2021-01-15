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

public class ListActivity extends AppCompatActivity {
    List<ProductModel> productModelList = new ArrayList<>();
    RecyclerView mRecyclerView;
    // layout manager for recycler view
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton mAddBtn;

    // firestore instance
    FirebaseFirestore db;
    CustomAdapter adapter;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("List Data");
        }
        // init firebase firestore
        db = FirebaseFirestore.getInstance();

        // initialize views
        mRecyclerView = findViewById(R.id.recycler_view);

        mAddBtn = findViewById(R.id.addBtn);

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
                startActivity(new Intent(ListActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    private void showData() {
        // set title of progress bar
        pd.setTitle("getting data from firebase firestore.....");
        pd.show();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        productModelList.clear();
                    // called when data is retrived
                        pd.dismiss();
                        // loop the data
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            ProductModel productModel = new ProductModel(
                                    documentSnapshot.getString("pId"),
                                    documentSnapshot.getString("pName"),
                            documentSnapshot.getString("pPrice"),
                            documentSnapshot.getString("pQuantity"));
                            productModelList.add(productModel);
                        }
                        // adapter
                        adapter = new CustomAdapter(ListActivity.this,productModelList);

                        // set adapter to recycler view
                        mRecyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    // called if there is any error
                    pd.dismiss();
                Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteData(int index){
        // set title of progress bar
        pd.setTitle("Deleting data from firebase firestore.....");
        pd.show();
        db.collection("Products").document(productModelList.get(index).getpId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                        // update data list
                        showData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void onStop() {

        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }
}