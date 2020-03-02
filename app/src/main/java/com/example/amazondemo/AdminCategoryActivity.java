package com.example.amazondemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {


    private ImageView tShirt, sportsTshirt, femaleDress, sweather;
    private ImageView glass,hatsCap, walletsBag, shoe;
    private ImageView headPhone, laptop, watch, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tShirt = (ImageView) findViewById(R.id.t_shirts);
        sportsTshirt = (ImageView) findViewById(R.id.sportsTshirt);
        femaleDress = (ImageView) findViewById(R.id.female_dress);
        sweather = (ImageView) findViewById(R.id.sweathers);

        glass = (ImageView) findViewById(R.id.glasses);
        hatsCap = (ImageView) findViewById(R.id.caps);
        walletsBag = (ImageView) findViewById(R.id.purses_bags);
        shoe = (ImageView) findViewById(R.id.shoes);

        headPhone = (ImageView) findViewById(R.id.headphones);
        laptop = (ImageView) findViewById(R.id.laptops);
        watch = (ImageView) findViewById(R.id.watches);
        mobile = (ImageView) findViewById(R.id.mobiles);

        tShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","tShirt");
                startActivity(i);
            }
        });

        sportsTshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","sportsTshirt");
                startActivity(i);
            }
        });

        femaleDress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","femaleDress");
                startActivity(i);
            }
        });

        sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","sweather");
                startActivity(i);
            }
        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","glass");
                startActivity(i);
            }
        });

        walletsBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","walletsBag");
                startActivity(i);
            }
        });

        hatsCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","hatsCap");
                startActivity(i);
            }
        });

        shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","shoe");
                startActivity(i);
            }
        });


        headPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","headPhone");
                startActivity(i);
            }
        });


        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","laptop");
                startActivity(i);
            }
        });


        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","mobile");
                startActivity(i);
            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                i.putExtra("category","watch");
                startActivity(i);
            }
        });

    }


}
