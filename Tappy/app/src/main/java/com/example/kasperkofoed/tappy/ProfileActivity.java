package com.example.kasperkofoed.tappy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;
    private Button buttonLogOut;
    private Button buttonOverview;
    private Button buttonReserveRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

              firebaseAuth = FirebaseAuth.getInstance();

        // If the user is already logged in
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = findViewById(R.id.textViewUserEmailProfile);
        textViewUserEmail.setText("Welcome " + user.getEmail());
        buttonLogOut = findViewById(R.id.buttonLogOut);
        buttonOverview = findViewById(R.id.buttonAllReservations);
        buttonReserveRoom = findViewById(R.id.buttonReserveARoom);

        buttonLogOut.setOnClickListener(this);
        buttonOverview.setOnClickListener(this);
        buttonReserveRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogOut) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if (v == buttonOverview) {
            startActivity(new Intent(this, Overview.class));
        }
        if (v == buttonReserveRoom) {
            startActivity(new Intent(this, AddReservationActivity.class));

        }
    }
}
