package com.example.kasperkofoed.tappy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Room5Overview extends AppCompatActivity implements GestureDetector.OnGestureListener {

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room5overview);

        firebaseAuth = FirebaseAuth.getInstance();

        gestureDetector = new GestureDetector(this);

        GetReservationsTask task = new GetReservationsTask();
        task.execute("https://anbo-roomreservation.azurewebsites.net/api/reservations/room/5");
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
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
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
        Toast.makeText(this,"Swipe down, returning home.", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }

    private void onSwipeLeft() {
        Toast.makeText(this,"For more room ID's, please contact support", Toast.LENGTH_SHORT).show();
    }

    private void onSwipeRight()
    {
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class GetReservationsTask extends AsyncTask<String, Void, String> {
        @Override
        // "(String..."" means, that there will be more strings
        protected String doInBackground(String... strings) {
            Request.Builder builder = new Request.Builder();
            String uri = strings[0];
            builder.url(uri);
            Request request = builder.build();

            try {
                OkHttpClient client = new OkHttpClient.Builder()

                        .build();
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Log.d("ROOMS", jsonString);
                return jsonString;

            } catch (IOException ex) {
                Log.e("SHIT", ex.getMessage());
            }


            return null;
        }

        protected void onPostExecute(String jsonString) {
            //jsonString -> reservations
            Gson gson = new GsonBuilder().create();
            final Reservation[] reservations = gson.fromJson(jsonString, Reservation[].class);

            ArrayAdapter<Reservation> adapter =
                    new ArrayAdapter<>(getBaseContext(),
                            android.R.layout.simple_list_item_1, reservations);

            ListView listView = findViewById(R.id.main_listView5);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Reservation reservation = (Reservation) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(getBaseContext(), SingleReservationActivity.class);
                    intent.putExtra(SingleReservationActivity.RESERVATION, reservation);
                    startActivity(intent);
                }
            });
        }
    }
}