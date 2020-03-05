package com.example.amazondemo.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amazondemo.R;
import com.example.amazondemo.interf.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pNameTxt, pDescTxt,pPriceTxt;
    //public ImageView pImageView;
    ItemClickListener itemClickListener;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        //pImageView = (ImageView) itemView.findViewById(R.id.card_product_image);
        pNameTxt = (TextView) itemView.findViewById(R.id.card_product_name);
        pDescTxt = (TextView) itemView.findViewById(R.id.card_product_description);
        pPriceTxt = (TextView) itemView.findViewById(R.id.card_product_price);
    }


    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
