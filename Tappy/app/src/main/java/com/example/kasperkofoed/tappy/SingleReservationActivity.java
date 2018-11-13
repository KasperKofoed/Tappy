package com.example.kasperkofoed.tappy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SingleReservationActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    Reservation reservation;
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;
    String uri = "https://anbo-roomreservation.azurewebsites.net/api/reservations/";

    public static final String RESERVATION = "reservation";

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_reservation);
        Intent intent = getIntent();
        reservation = (Reservation) intent.getSerializableExtra(RESERVATION);
        Log.d(CommonStuff.TAG, reservation.toString());



        TextView reservationIdView = findViewById(R.id.singleReservation_reservationId_textview);
        reservationIdView.setText(reservation.getId());

        TextView userIdView = findViewById(R.id.singleReservation_userId_textview);
        userIdView.setText(reservation.getUserId());

        TextView purposeView = findViewById(R.id.singleReservation_purpose_textview);
        purposeView.setText(reservation.getPurpose());

        gestureDetector = new GestureDetector(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        Button btnDel = findViewById(R.id.buttonDelete);
        if (reservation.getUserId().equals(user.getEmail())) {
            btnDel.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX,
                           float velocityY) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        //which was greater, movement across X or Y?

        if (Math.abs(diffX) > Math.abs(diffY)) {
            //left or right swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                result = true;
            }
        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        }
        return result;
    }

    private void onSwipeTop() {
    }

    private void onSwipeBottom() {
    }

    private void onSwipeLeft() {
    }

    private void onSwipeRight() {
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void deleteReservationButtonClicked(View view) {
        DeleteResTask task = new DeleteResTask();
        task.execute();
    }

    private class DeleteResTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String code = "";
            HttpUrl route = HttpUrl.parse(uri)
                    .newBuilder()
                    .addPathSegment(reservation.getId())
                    .build();
            Request request = new Request.Builder().url(route).delete().build();

            try {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Response response = client.newCall(request).execute();
                if (response.code() == 204) code = "complete";
                else if (response.code() == 404) code = "not found";
                else code = String.valueOf(response.code());
                return code;
            } catch (IOException ex) {
                Log.e("BUILDINGS", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "Reservation deleted.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getBaseContext(), MyReservations.class));
        }
    }
}

