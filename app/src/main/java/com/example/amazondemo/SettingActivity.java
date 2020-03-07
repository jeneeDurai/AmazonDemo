package com.example.amazondemo;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amazondemo.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    private ImageView proImg;
    private EditText nameTxt, emailTxt, addTxt;
    private TextView cancelBtn, updateBtn, changeProBtn;
    private SharedPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        proImg = (ImageView) findViewById(R.id.user_profile_image_setting);

        nameTxt = (EditText) findViewById(R.id.setting_name_txt);
        emailTxt = (EditText) findViewById(R.id.setting_address_txt);

        cancelBtn = (TextView) findViewById(R.id.cancel_setting_btn);
        updateBtn = (TextView) findViewById(R.id.update_acc_setting_btn);
        changeProBtn = (TextView) findViewById(R.id.update_profile_img_text);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        userInfoDisplay();



    }

    private void userInfoDisplay() {
        String email = loginPreferences.getString("email","").toString();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(email.substring(0, email.lastIndexOf(".")));
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(proImg);
                        nameTxt.setText(name);
                        emailTxt.setText(email);
                        addTxt.setText(address);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
