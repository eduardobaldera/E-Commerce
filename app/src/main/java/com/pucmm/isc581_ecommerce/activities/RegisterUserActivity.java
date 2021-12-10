package com.pucmm.isc581_ecommerce.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyalbira.loadingdots.LoadingDots;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.Utils.Constants;
import com.pucmm.isc581_ecommerce.Utils.Response;
import com.pucmm.isc581_ecommerce.Utils.SendDataTask;
import com.pucmm.isc581_ecommerce.Utils.Validator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView mLoginNow;
    private TextView mForgotPassword;
    private ExtendedFloatingActionButton registerBtn;

    private String imageUrl;


    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText passwordConfET;
    private EditText lastNameET;
    private EditText phoneET;
    private EditText birthdayET;
    private CheckBox roleX;
    private LoadingDots progress;

    private Uri imageUri;
    private CircularImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        mLoginNow = findViewById(R.id.reg_login_now);
        mForgotPassword = findViewById(R.id.reg_forgot_password);
        registerBtn = findViewById(R.id.reg_user_button);
        emailET = findViewById(R.id.reg_user_email);
        passwordET = findViewById(R.id.reg_user_password);
        nameET = findViewById(R.id.reg_user_name);
        progress = findViewById(R.id.reg_user_progress);
        imageView = findViewById(R.id.reg_user_image);
        lastNameET = findViewById(R.id.reg_user_lastname);
        passwordConfET = findViewById(R.id.reg_user_confirm_password);
        phoneET = findViewById(R.id.reg_user_phone);
        birthdayET = findViewById(R.id.reg_user_birthday);
        roleX = findViewById(R.id.reg_user_role);

        imageView.setOnClickListener( v-> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        });

        registerBtn.setOnClickListener( v-> {
            Toast.makeText(this, "Creating user...", Toast.LENGTH_SHORT).show();
            registerUser();

        });

        mForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        mLoginNow.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageView.setImageURI(imageUri);
            imageUrl = "";

        }
    }


    private void registerUser() {
        if(!validateData()) return;
        Toast.makeText(this, "Data validated", Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.VISIBLE);

        final String name = nameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String lastName = lastNameET.getText().toString().trim();
        final String phone = phoneET.getText().toString().trim();
        final Long birthday = 723772800000L;
        final boolean role = roleX.isChecked();

        final SendDataTask regUserTask = new SendDataTask(Constants.API_REGISTER_USER, "POST", (Response.Listener<JSONObject>) response -> {
            try { ;
                StorageReference usersImageRef = FirebaseStorage.getInstance().
                        getReference("UserPics/" + response.getInt("uid") + ".jpg");
                if (imageUri != null) {
                    usersImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

                        //GET IMAGE URL
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.wtf("user created", "CREATED IMAGE ON STORAGE, URL : " + uri.toString());
                            imageUrl = uri.toString();
                            Toast.makeText(RegisterUserActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
                        });
                        finish();
                    })
                            .addOnFailureListener(e -> Toast.makeText(RegisterUserActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show());
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisterUserActivity.this, "There was an error", Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            Toast.makeText(RegisterUserActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            progress.setVisibility(View.GONE);
        });

        try {
            JSONObject registerData = new JSONObject();
            registerData.put("contact", phone);
            registerData.put("email", email);
            registerData.put("rol", role ? "SELLER" : "CUSTOMER");
            registerData.put("firstName", name);
            registerData.put("photo", "https://firebasestorage.googleapis.com/v0/b/isc581-ecommerce.appspot.com/o/UserPics%2F122.jpg?alt=media&token=81eea13d-c716-41cc-82b8-1aed3cc08ee0");
            registerData.put("lastName", lastName);
            registerData.put("password", password);
            registerData.put("birthday", birthday);

            regUserTask.execute(registerData);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RegisterUserActivity.this, "There was an error", Toast.LENGTH_LONG).show();
            progress.setVisibility(View.GONE);
        }
    }

    private boolean validateData() {

        return (!Validator.isEmpty(nameET, lastNameET,emailET,passwordET, phoneET)
                && Validator.isEmailValid(emailET));
    }

    private final SendDataTask updateUserTask = new SendDataTask(Constants.API_UPDATE_USER, "PUT",  (Response.Listener<JSONObject>) response -> {
        Log.i("EVENT", "User updated successfully");
        finish();
    }, error -> {
        Log.i("ERROR", "Error updating user in api");
        Toast.makeText(RegisterUserActivity.this, "Error updating user", Toast.LENGTH_LONG).show();
        progress.setVisibility(View.GONE);
    });
}