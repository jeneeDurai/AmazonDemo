package com.example.amazondemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUPActivity extends AppCompatActivity {

    private Button signUpBtn;
    private EditText nameTxt, emailTxt, passwordTxt, cPasswordTxt;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpBtn = (Button) findViewById(R.id.signUp_submit_btn);
        nameTxt = (EditText) findViewById(R.id.register_name_input);
        emailTxt = (EditText) findViewById(R.id.register_email_input);
        passwordTxt = (EditText) findViewById(R.id.register_password_input);
        cPasswordTxt = (EditText) findViewById(R.id.register_confirm_password_input);
        loadingBar = new ProgressDialog(this);
        
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String name = nameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String cPassword = cPasswordTxt.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please provide name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please provide email.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please provide password.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "Please provide confirm password.", Toast.LENGTH_SHORT).show();
        }

        else if(!TextUtils.equals(password,cPassword)){
            Toast.makeText(this, "Passwords are not matching.", Toast.LENGTH_SHORT).show();
        }

        else{
             loadingBar.setTitle("Create Account");
             loadingBar.setMessage("Please wait....");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();

             validate(name,email,password);
        }


    }

    private void validate(final String name, final String email, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(name).exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("name",name);
                    userdataMap.put("email",email);
                    userdataMap.put("password",password);

                    RootRef.child("Users").child(name).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUPActivity.this, "Success. User created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent i = new Intent(SignUPActivity.this, LoginActivity.class);
                                startActivity(i);

                            }else{
                                loadingBar.dismiss();
                                Toast.makeText(SignUPActivity.this, "Error.. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUPActivity.this, "Email - "+email+ " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(SignUPActivity.this, "Provide different email.", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(SignUPActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
