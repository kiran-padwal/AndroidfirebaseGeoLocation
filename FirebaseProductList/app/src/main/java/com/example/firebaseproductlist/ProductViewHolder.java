package com.example.firebaseproductlist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    TextView mProductName,mProductPrice,mProductQuantity;
    View mView;
    public ProductViewHolder(@NonNull View itemView) {
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
        mProductName = itemView.findViewById(R.id.rProductNameTv);
        mProductPrice = itemView.findViewById(R.id.rProductPriceTv);
        mProductQuantity = itemView.findViewById(R.id.rProductQuantityTv);
    }

    private ProductViewHolder.ClickListener mClickListener;
    // interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ProductViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
