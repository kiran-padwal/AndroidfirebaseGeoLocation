package com.example.firebaseproductlist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    TextView mStoreName,mStoreDesc,mStoreRadius,mStoreLat,mStoreLong;
    View mView;
    public StoreViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        // on item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

        // initialize view with model_layout.xml
        mStoreName = itemView.findViewById(R.id.rStoreNameTv);
        mStoreDesc = itemView.findViewById(R.id.rStoreDescTv);
        mStoreRadius = itemView.findViewById(R.id.rStoreRadiusTv);
        mStoreLat = itemView.findViewById(R.id.rStoreLatTv);
        mStoreLong = itemView.findViewById(R.id.rStoreLongTv);


    }

    private StoreViewHolder.ClickListener mClickListener;
    // interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(StoreViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
