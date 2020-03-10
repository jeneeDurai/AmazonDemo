package com.example.amazondemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazondemo.model.Product;
import com.example.amazondemo.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    private Button addToCartButton, qtyIncBtn, qtyDecBtn;
    private ImageView productImage;
    private TextView productPrice, productDescription, productName;
    private TextView qtyTxt;
    private String productID = "", state = "Normal";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        qtyTxt = (TextView) findViewById(R.id.qty_txt);

        if(!getIntent().getStringExtra("qty").isEmpty()){
            qtyTxt.setText(getIntent().getStringExtra("qty"));
        }

        qtyIncBtn = (Button) findViewById(R.id.qty_inc_button);
        qtyDecBtn = (Button) findViewById(R.id.qty_dec_button);

        getProductDetails(productID);


        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(ProductDetailActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });

        qtyIncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int curQty = Integer.parseInt(qtyTxt.getText().toString());
                curQty++;
                qtyTxt.setText(String.valueOf(curQty));
            }
        });

        qtyDecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int curQty = Integer.parseInt(qtyTxt.getText().toString());
                if(curQty != 1){
                    curQty--;
                }
                qtyTxt.setText(String.valueOf(curQty));


            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", qtyTxt.getText());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentUser.getEmail().substring(0, Prevalent.currentUser.getEmail().lastIndexOf(".")))
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentUser.getEmail().substring(0, Prevalent.currentUser.getEmail().lastIndexOf(".")))
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Product product = dataSnapshot.getValue(Product.class);

                    productName.setText(product.getPname());
                    productPrice.setText(product.getPrice());
                    productDescription.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getEmail().substring(0, Prevalent.currentUser.getEmail().lastIndexOf(".")));

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
