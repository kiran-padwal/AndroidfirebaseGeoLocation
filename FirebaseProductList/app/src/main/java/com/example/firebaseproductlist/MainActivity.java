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

public class MainActivity extends AppCompatActivity {
    //Views
    EditText productName,productPrice,productQuantity;
    Button saveProduct,listBtn;


    // progress dialog
    ProgressDialog progressDialog;

    // firestore instance
    FirebaseFirestore db;

    // when update is clicked
    String pId,pName,pPrice,pQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();


        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        saveProduct = findViewById(R.id.save_product);
        listBtn = findViewById(R.id.show_list);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            // update data
            actionBar.setTitle("Update Product");
            saveProduct.setText("Update Product");
            pId = bundle.getString("pId");
            pName = bundle.getString("pName");
            pPrice = bundle.getString("pPrice");
            pQuantity = bundle.getString("pQuantity");
            // set data
            productName.setText(pName);
            productPrice.setText(pPrice);
            productQuantity.setText(pQuantity);

        }
        else {
            // new data
            actionBar.setTitle("Add Product");
            saveProduct.setText("Add Product");
        }

        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if(bundle1!=null){
                    // update data
                    String productId = pId;
                    String productNameString = productName.getText().toString().trim();
                    String productPriceString = productPrice.getText().toString().trim();
                    String productQuantityString = productQuantity.getText().toString().trim();

                    // update data to firestore
                    updateProduct(productId,productNameString,productPriceString,productQuantityString );

                }
                else{
                    // input data
                    String productNameString = productName.getText().toString().trim();
                    String productPriceString = productPrice.getText().toString().trim();
                    String productQuantityString = productQuantity.getText().toString().trim();

                    // add data to firestore
                    addProduct(productNameString,productPriceString,productQuantityString );
                }

            }
        });
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                finish();
            }
        });
    }

    private void updateProduct(String productId, String productNameString, String productPriceString, String productQuantityString) {
        progressDialog.setTitle("Updating Product...");
        progressDialog.show();
        db.collection("Products").document(productId)
                .update("pName",productNameString,"pPrice",productPriceString,"pQuantity",productQuantityString)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated successfully
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // called when there is any error while updating
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct(String productNameString, String productPriceString, String productQuantityString) {
        progressDialog.setTitle("Adding Product to Firestore");
        progressDialog.show();
        String pId = UUID.randomUUID().toString();
        Map<String, Object> product = new HashMap<>();
        product.put("pId",pId);
        product.put("pName",productNameString);
        product.put("pPrice",productPriceString);
        product.put("pQuantity",productQuantityString);

        db.collection("Products").document(pId).set(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Product is added to firestore", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}