package com.example.firebaseproductlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    //FirebaseFirestore db = FirebaseFirestore
    private FirebaseAuth mAuth;
    EditText etEmail,etPassword;
    Button mLoginRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mLoginRegister = findViewById(R.id.login_register);



        mLoginRegister.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                etEmail = findViewById(R.id.et_email);
                etPassword = findViewById(R.id.et_password);
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                mAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                                    Toast.makeText(LoginActivity.this, "User successfully Registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginActivity.this, "signup failed." +task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
             return true;
            }
        });


        mLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail = findViewById(R.id.et_email);
                etPassword = findViewById(R.id.et_password);
                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "User Logged in"+ user, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, ListStoreActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "sign in failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(this, "User is loggen in" + currentUser, Toast.LENGTH_SHORT).show();


    }
}