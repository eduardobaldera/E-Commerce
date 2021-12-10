package com.pucmm.isc581_ecommerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.Utils.Constants;
import com.pucmm.isc581_ecommerce.Utils.Response;
import com.pucmm.isc581_ecommerce.Utils.SendDataTask;
import com.pucmm.isc581_ecommerce.Utils.Validator;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.ProductsDB;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.UsersDB;
import com.pucmm.isc581_ecommerce.models.Category;
import com.pucmm.isc581_ecommerce.models.User;
import com.pucmm.isc581_ecommerce.recievers.NotificationReciever;
import com.pucmm.isc581_ecommerce.sessions.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton mLoginBtn;
    private TextView mForgotPassword;
    private TextView mRegisterNow;

    private EditText emailET;
    private EditText passwordET;

    private UserSession session;

    private ProgressBar progress;
    private FirebaseAuth mAuth;
    private User currentUser;
    private static final DatabaseReference userRef = MainActivity.database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginBtn = findViewById(R.id.log_in_button);
        mForgotPassword = findViewById(R.id.log_in_forgot_password);
        mRegisterNow = findViewById(R.id.log_in_register_now);
        emailET = findViewById(R.id.log_in_email);
        passwordET = findViewById(R.id.log_in_password);
        progress = findViewById(R.id.log_in_progress);

        mAuth = FirebaseAuth.getInstance();
//        CategoriesDB.removeProductFromCategory();

       ArrayList<Category> categories  =  CategoriesDB.getCategories();
        Log.wtf("LOG TEST OF CAT", "CATEGORIES UPDATING" + categories.toString());


        mLoginBtn.setOnClickListener(v -> {

            logInUser();

        });

        mForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        mRegisterNow.setOnClickListener(v -> {
//            startActivity(new Intent(this, RegisterUserActivity.class));
            Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
            intent.putExtra("action", "register");
            startActivity(intent);
        });

        session = new UserSession(getApplicationContext());

    }

    private void logInUser() {
        if(!validateData()) return;

        Log.i("EVENT", "User passed validation");

        progress.setVisibility(View.VISIBLE);

                final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
//        final String email = "balder98@gmail.com";
//        final String password = "helloeduardo1";

        final SendDataTask loginTask = new SendDataTask(Constants.API_LOGIN, "POST", (Response.Listener<JSONObject>) response -> {
            try {
                Log.i("response", response.toString());
                User user = new User(
                        response.getLong("uid"),
                        Uri.parse(response.getString("photo")),
                        response.getString("firstName"),
                        response.getString("lastName"),
                        response.getString("email"),
                        response.getString("contact"),
                        response.getString("rol").equals("SELLER") ? "SELLER" : "COSTUMER",
                        response.getLong("birthday")

                );

                Log.wtf("MY_USER",
                      "" + response.getLong("uid") +
                                response.getString("firstName") +
                                response.getString("lastName") +
                                response.getString("email") +
                                response.getString("contact") +
                                response.getLong("birthday")
                );
                session.createUserSession(user);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "There was an error", Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }
        }, error -> {
            Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            progress.setVisibility(View.GONE);
        });

        try {
            JSONObject loginData = new JSONObject();
            loginData.put("email", email);
            loginData.put("password", password);

            loginTask.execute(loginData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    private void logInUser() {
//        if(!validateData()) return;
//
//
//        progress.setVisibility(View.VISIBLE);
//
////        final String email = emailET.getText().toString().trim();
////        final String password = passwordET.getText().toString().trim();
//        final String email = "estherbaldera@gmail.com";
//        final String password = "helloesther1";
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            UsersDB.getUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "Login failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//                    }
//                });
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progress.setVisibility(View.INVISIBLE);
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },2000);
//    }

    private Boolean validateData() {
        if (!Validator.isInternetConnected(LoginActivity.this)) {

            new MaterialAlertDialogBuilder(LoginActivity.this)
                    .setTitle("Network Error")
                    .setMessage("You are not connected to the internet")
                    .setPositiveButton("Try again", (dialog, which) -> logInUser())
                    .show();
            return false;
        }

        if (!Validator.isEmailValid(emailET) || Validator.isEmpty(passwordET)) {
            Log.e("ERROR", "User did not pass validation");
            return false;
        }

        return true;
    }


}