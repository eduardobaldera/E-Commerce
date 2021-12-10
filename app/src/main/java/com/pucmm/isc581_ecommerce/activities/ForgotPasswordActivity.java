package com.pucmm.isc581_ecommerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.Utils.Constants;
import com.pucmm.isc581_ecommerce.Utils.Response;
import com.pucmm.isc581_ecommerce.Utils.SendDataTask;
import com.pucmm.isc581_ecommerce.Utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mLoginNow;
    private TextView mRegisterNow;

    private EditText emailET;
    private EditText passwordET;
    private ExtendedFloatingActionButton resetPassBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        mLoginNow = findViewById(R.id.forgot_password_login_now);
        mRegisterNow = findViewById(R.id.forgot_password_register_now);
        emailET = findViewById(R.id.forgot_password_email);
        passwordET = findViewById(R.id.forgot_password);
        resetPassBtn = findViewById(R.id.forgot_password_button);

        mLoginNow.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        mRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });

        resetPassBtn.setOnClickListener( v-> {
            if(Validator.isEmpty(emailET)) {
                return;
            }
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString();


            final SendDataTask retrievePasswordTask = new SendDataTask(Constants.API_FORGOT_PASSWORD, "PUT", (Response.Listener<JSONObject>) response -> {
                Toast.makeText(ForgotPasswordActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();

            }, error -> {
                Toast.makeText(ForgotPasswordActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            });

            try {
                JSONObject forgotPasswordData = new JSONObject();
                forgotPasswordData.put("email", email);
                forgotPasswordData.put("password", password);

                retrievePasswordTask.execute(forgotPasswordData);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ForgotPasswordActivity.this, "There was an error", Toast.LENGTH_LONG).show();
            }

//            mAuth.sendPasswordResetEmail(email)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("FORGOT PASSWORD ACT", "Email sent.");
//                                Toast.makeText(ForgotPasswordActivity.this, "Email sent, check your inbox.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
//                            }else {
//                                Toast.makeText(ForgotPasswordActivity.this, "Error sending email", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

        });

    }
}