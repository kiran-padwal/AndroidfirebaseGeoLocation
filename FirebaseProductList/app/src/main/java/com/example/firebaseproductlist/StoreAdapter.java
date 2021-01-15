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

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {
    ListStoreActivity listStoreActivity;
    List<StoreModel> storeModelList;
    Context context;

    public StoreAdapter(ListStoreActivity listStoreActivity, List<StoreModel> storeModelList) {
        this.listStoreActivity = listStoreActivity;
        this.storeModelList = storeModelList;

    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_model_layout,parent,false);

        StoreViewHolder storeViewHolder = new StoreViewHolder(itemView);
        // handle item clicks here
        storeViewHolder.setOnClickListener(new StoreViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String storeName = storeModelList.get(position).getsName();
//                String productPrice = productModelList.get(position).getpPrice();
//                String productQuantity = productModelList.get(position).getpQuantity();
                Toast.makeText(listStoreActivity, storeName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // creating alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listStoreActivity);
                // options to display in dialog
                String [] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {// update is clicked
                            // get data
                            String sId = storeModelList.get(position).getsId();
                            String sName = storeModelList.get(position).getsName();
                            String sDesc = storeModelList.get(position).getsDes();
                            String sRadius = storeModelList.get(position).getsRadius();
                            String sLat = storeModelList.get(position).getsLat();
                            String sLong = storeModelList.get(position).getsLong();

                            // intent to start activity
                            Intent intent = new Intent(listStoreActivity,AddStoreActivity.class);
                            // put data in intent
                            intent.putExtra("sId",sId);
                            intent.putExtra("sName",sName);
                            intent.putExtra("sDesc",sDesc);
                            intent.putExtra("sRadius",sRadius);
                            intent.putExtra("sLat",sLat);
                            intent.putExtra("sLong",sLong);

                            // start activity
                            listStoreActivity.startActivity(intent);
                        }
                        if(i == 1){
                            // delete is clicked
                            listStoreActivity.deleteData(position);
                        }
                    }
                }).create().show();
            }
        });

        return storeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {

        // bind views / set data
        holder.mStoreName.setText(storeModelList.get(position).getsName());
        holder.mStoreDesc.setText(storeModelList.get(position).getsDes());
        holder.mStoreRadius.setText(storeModelList.get(position).getsRadius());
        holder.mStoreLat.setText(storeModelList.get(position).getsLat());
        holder.mStoreLong.setText(storeModelList.get(position).getsLong());
    }

    @Override
    public int getItemCount() {
        return storeModelList.size();
    }
}
