package com.example.firebaseproductlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStoreActivity extends AppCompatActivity {
    //Views
    EditText storeName, storeDesc, storeRadius, storeLat, storeLong;
    Button saveStore,listBtn,mapsBtn;


    // progress dialog
    ProgressDialog progressDialog;

    // firestore instance
    FirebaseFirestore db;

    // when update is clicked
    String sId,sName,sDesc,sLat, sLong, sRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        ActionBar actionBar = getSupportActionBar();


        storeName = findViewById(R.id.store_name);
        storeDesc = findViewById(R.id.store_desc);
        storeRadius = findViewById(R.id.store_radius);
        storeLat = findViewById(R.id.store_lat);
        storeLong = findViewById(R.id.store_long);
        saveStore = findViewById(R.id.save_store);
        listBtn = findViewById(R.id.show_list_store);
        mapsBtn = findViewById(R.id.show_store_maps);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            // update data
            actionBar.setTitle("Update Store");
            saveStore.setText("Update Store");
            sId = bundle.getString("sId");
            sName = bundle.getString("sName");
            sDesc = bundle.getString("sDesc");
            sRadius = bundle.getString("sRadius");
            sLat = bundle.getString("sLat");
            sLong = bundle.getString("sLong");

            // set data
            storeName.setText(sName);
            storeDesc.setText(sDesc);
            storeRadius.setText(sRadius);
            storeLat.setText(sLat);
            storeLong.setText(sLong);

        }
        else {
            // new data
            actionBar.setTitle("Add Store");
            saveStore.setText("Add Store");
        }

        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        saveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if(bundle1!=null){
                    // update data
                    String storeId = sId;
                    String storeNameString = storeName.getText().toString().trim();
                    String storeDescString = storeDesc.getText().toString().trim();
                    String storeRadiusString = storeRadius.getText().toString().trim();
                    String storeLatString = storeLat.getText().toString().trim();
                    String storeLongString = storeLong.getText().toString().trim();


                    // update data to firestore
                    updateProduct(storeId,storeNameString,storeDescString,storeRadiusString,storeLatString,storeLongString);

                }
                else{
                    // input data
                    String storeNameString = storeName.getText().toString().trim();
                    String storeDescString = storeDesc.getText().toString().trim();
                    String storeRadiusString = storeRadius.getText().toString().trim();
                    String storeLatString = storeLat.getText().toString().trim();
                    String storeLongString = storeLong.getText().toString().trim();

                    // add data to firestore
                    addProduct(storeNameString,storeDescString,storeRadiusString,storeLatString,storeLongString);
                }

            }
        });
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddStoreActivity.this, ListStoreActivity.class));
                finish();
            }
        });
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddStoreActivity.this, MapsActivity.class));
                finish();
            }
        });
    }

    private void updateProduct(String storeId, String storeNameString, String storeDescString, String storeRadiusString,String storeLatString, String storeLongString) {
        progressDialog.setTitle("Updating Store...");
        progressDialog.show();
        db.collection("Stores").document(storeId)
                .update("sName",storeNameString,"sDesc",storeDescString,"sRadius",storeRadiusString,"sLat",storeLatString,"sLong",storeLongString)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated successfully
                        progressDialog.dismiss();
                        Toast.makeText(AddStoreActivity.this, "Updated store Successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AddStoreActivity.this, ListStoreActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // called when there is any error while updating
                progressDialog.dismiss();
                Toast.makeText(AddStoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct(String storeNameString,String storeDescString,String storeRadiusString,String storeLatString,String storeLongString) {
        progressDialog.setTitle("Adding Store to Firestore");
        progressDialog.show();
        String sId = UUID.randomUUID().toString();
        Map<String, Object> store = new HashMap<>();
        store.put("sId",sId);
        store.put("sName",storeNameString);
        store.put("sDesc",storeDescString);
        store.put("sRadius",storeRadiusString);
        store.put("sLat",storeLatString);
        store.put("sLong",storeLongString);

        db.collection("Stores").document(sId).set(store)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(AddStoreActivity.this, "Store is added to firestore", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddStoreActivity.this, ListStoreActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddStoreActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}