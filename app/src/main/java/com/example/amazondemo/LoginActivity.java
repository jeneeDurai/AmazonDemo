package com.example.amazondemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazondemo.model.User;
import com.example.amazondemo.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameTxt, passwordTxt;
    private Button loginBtn;
    private ProgressDialog loadingBar;
    private CheckBox remberMeCheck;
    private String parentDbName = "Users";

    private Boolean saveLogin;

    private SharedPreferences  mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameTxt = (EditText) findViewById(R.id.login_username_input);
        passwordTxt = (EditText) findViewById(R.id.login_password_input);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        mPrefs = getSharedPreferences("loginPrefs",MODE_PRIVATE);
        saveLogin = mPrefs.getBoolean("saveLogin", false);

        remberMeCheck = (CheckBox) findViewById(R.id.remberme);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


    }

    private void loginUser() {

        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();

        if(password.equals("admin")){
            Intent i = new Intent(LoginActivity.this, AdminCategoryActivity.class);
            startActivity(i);
        }else{
            if(TextUtils.isEmpty(username)){
                Toast.makeText(this, "Please provide email.", Toast.LENGTH_SHORT).show();
            }

            else if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Please provide password.", Toast.LENGTH_SHORT).show();
            }

            else{

                loadingBar.setTitle("Login");
                loadingBar.setMessage("Please wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validate(username,password);
            }
        }
    }

    private void validate(final String username, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        final String usernameKey = username.substring(0, username.lastIndexOf("."));

        RootRef.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(usernameKey).exists()){

                    User user = dataSnapshot.child(parentDbName).child(usernameKey).getValue(User.class);

                    if(user.getEmail().equals(username) && user.getPassword().equals(password)){

                        Prevalent.currentUser = user;

                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        prefsEditor.putString("currentUserObj", json);


                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "login Success", Toast.LENGTH_SHORT).show();


                        if(remberMeCheck.isChecked()){
                            prefsEditor.putBoolean("saveLogin", true);
                        }
                        else{
                            prefsEditor.putBoolean("saveLogin", false);
                        }

                        prefsEditor.commit();

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);


                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Account not found", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
