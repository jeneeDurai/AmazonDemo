package com.example.amazondemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button loginBtn;
    private Button signUpBtn;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.main_login_btn);
        signUpBtn = (Button) findViewById(R.id.main_signup_btn);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = loginPreferences.getString("username", null);

                if(uname != null){
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }

//                Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(i);


            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUPActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
