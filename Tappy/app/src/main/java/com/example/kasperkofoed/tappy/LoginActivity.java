package com.example.kasperkofoed.tappy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //
    // Fields for various frontend stuff
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewRegister;
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
        setContentView(R.layout.activity_login);

        relativeLayout = findViewById(R.id.RelativeMain1);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        // If the user is already logged in
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        buttonSignIn = findViewById(R.id.buttonUserLogin);
        editTextEmail = findViewById(R.id.editTextEmailLogin);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);
        textViewRegister = findViewById(R.id.textViewRegisterHere);

        buttonSignIn.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
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

    private void userLogin() {
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
        // If validation is ok, show progressbar / loadscreen
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Start profile activity here
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            userLogin();
        }
        if (view == textViewRegister){
            // This opens login activity
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
