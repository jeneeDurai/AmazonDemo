package com.example.amazondemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button loginBtn;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button) findViewById(R.id.main_login_btn);
        signUpBtn = (Button) findViewById(R.id.main_signup_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
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
