package com.example.kasperkofoed.tappy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Implements the "OnClickListener functionality
public class MainActivity extends AppCompatActivity implements OnClickListener{
    //
    // Fields for various frontend stuff
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    //
    // Field for auth of Firebase
    private FirebaseAuth firebaseAuth;
    //
    // Fields for gradient Background
    private AnimationDrawable animationDrawable;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.RelativeMain);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        //
        // If the user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        buttonRegister = findViewById(R.id.buttonRegisterUser);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewSignIn = findViewById(R.id.textViewSignIn);

        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

        @Override
        protected void onResume() {
            super.onResume();
            if (animationDrawable != null && !animationDrawable.isRunning())
                animationDrawable.start();
        }

        @Override
        protected void onPause() {
            super.onPause();
            if (animationDrawable != null && animationDrawable.isRunning())
                animationDrawable.stop();
        }

    //
    // Method to register new users, if neither email or password is null.
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            // Email is empty.
            Toast.makeText(this,"Please enter Email.", Toast.LENGTH_SHORT).show();
            // Stops the function
            return;

        }
        if (TextUtils.isEmpty(password)){
            // Password is empty.
            Toast.makeText(this,"Please enter Password", Toast.LENGTH_SHORT).show();
            // Stops the function
            return;
        }
        //
        // If validation is ok, show progressbar / loadscreen
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //
        // Here the actual user is created in the Firebase if the task is successful
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //
                            // Screen Pop up, if task is successful
                            Toast.makeText(MainActivity.this,"Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                        }else{
                            //
                            // Screen Pop up, if task is NOT successful
                            Toast.makeText(MainActivity.this,"Could not register", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            registerUser();
        }
        if (view == textViewSignIn){
            // This opens login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
