package com.example.firebaseproductlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    ListActivity listActivity;
    List<ProductModel> productModelList;
    Context context;

    public CustomAdapter(ListActivity listActivity, List<ProductModel> productModelList) {
        this.listActivity = listActivity;
        this.productModelList = productModelList;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_model_layout,parent,false);

        ProductViewHolder productViewHolder = new ProductViewHolder(itemView);
        // handle item clicks here
        productViewHolder.setOnClickListener(new ProductViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String productName = productModelList.get(position).getpName();
                String productPrice = productModelList.get(position).getpPrice();
                String productQuantity = productModelList.get(position).getpQuantity();
                Toast.makeText(listActivity, productName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // creating alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                // options to display in dialog
                String [] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {// update is clicked
                            // get data
                            String pId = productModelList.get(position).getpId();
                            String pName = productModelList.get(position).getpName();
                            String pPrice = productModelList.get(position).getpPrice();
                            String pQuantity = productModelList.get(position).getpQuantity();

                            // intent to start activity
                            Intent intent = new Intent(listActivity,MainActivity.class);
                            // put data in intent
                            intent.putExtra("pId",pId);
                            intent.putExtra("pName",pName);
                            intent.putExtra("pPrice",pPrice);
                            intent.putExtra("pQuantity",pQuantity);

                            // start activity
                            listActivity.startActivity(intent);
                        }
                        if(i == 1){
                            // delete is clicked
                            listActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }
        });

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        // bind views / set data
        holder.mProductName.setText(productModelList.get(position).getpName());
        holder.mProductPrice.setText(productModelList.get(position).getpPrice());
        holder.mProductQuantity.setText(productModelList.get(position).getpQuantity());
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }
}
