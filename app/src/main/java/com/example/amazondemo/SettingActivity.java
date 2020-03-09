package com.example.amazondemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazondemo.model.User;
import com.example.amazondemo.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private ImageView proImg;
    private EditText nameTxt, emailTxt, addTxt;
    private TextView cancelBtn, updateBtn, changeProBtn;
    private SharedPreferences loginPreferences;
    private Uri imageUri;
    private String myUri = "";
    private StorageReference proPicStorageRef;
    private String checker = "";

    private StorageTask uploadTask;

    private SharedPreferences  mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        proImg = (ImageView) findViewById(R.id.user_profile_image_setting);
        proPicStorageRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        mPrefs = getSharedPreferences("loginPrefs",MODE_PRIVATE);

        nameTxt = (EditText) findViewById(R.id.setting_name_txt);
        emailTxt = (EditText) findViewById(R.id.setting_email_txt);
        addTxt = (EditText) findViewById(R.id.setting_address_txt);
        cancelBtn = (TextView) findViewById(R.id.cancel_setting_btn);
        updateBtn = (TextView) findViewById(R.id.update_acc_setting_btn);
        changeProBtn = (TextView) findViewById(R.id.update_profile_img_text);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        userInfoDisplay();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                }
                else{
                    updateOnlyUserInfo();
                }
            }
        });

        changeProBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingActivity.this);
            }
        });
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(nameTxt.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailTxt.getText().toString()))
        {
            Toast.makeText(this, "Email is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addTxt.getText().toString()))
        {
            Toast.makeText(this, "Address is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final String  email = loginPreferences.getString("email","").toString();

        if (imageUri != null)
        {
            final StorageReference fileRef = proPicStorageRef
                    .child(email.substring(0, email.lastIndexOf(".")) + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUri = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", nameTxt.getText().toString());
                                userMap. put("address", addTxt.getText().toString());
                                userMap. put("email", emailTxt.getText().toString());
                                userMap. put("image", myUri);
                                ref.child(email.substring(0, email.lastIndexOf("."))).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                                Toast.makeText(SettingActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        String email = Prevalent.currentUser.getEmail();

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", nameTxt.getText().toString());
        userMap. put("address", addTxt.getText().toString());
        userMap. put("email", emailTxt.getText().toString());
        ref.child(email.substring(0, email.lastIndexOf("."))).updateChildren(userMap);

        User user = Prevalent.currentUser;
        user.setName(nameTxt.getText().toString());
        user.setAddress(addTxt.getText().toString());
        user.setEmail(emailTxt.getText().toString());

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("currentUserObj", json);
        prefsEditor.commit();
        Prevalent.currentUser = user;

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        Toast.makeText(SettingActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoDisplay() {
        String email = Prevalent.currentUser.getEmail();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            proImg.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }
    }
}
