package com.example.kasperkofoed.tappy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.time.Clock;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddReservationActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    //
    //DATE AND TIME PICKERS.
    private static final String TAG = "AddReservationActivity";
    private EditText mDisplayDate;
    private EditText mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2;


    //
    //FIREBASE
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserid;

    //
    //SWIPE GESTURES
    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        firebaseAuth = FirebaseAuth.getInstance();
        textViewUserid = findViewById(R.id.addReservation_userId_edittext);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        textViewUserid.setText(user.getEmail());

        gestureDetector = new GestureDetector(this);

        mDisplayDate = findViewById(R.id.addReservation_fromTime_edittext);
        mDisplayDate2 = findViewById(R.id.addReservation_toTime_edittext);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddReservationActivity.this,
                        android.R.style.Theme_Light,
                        mDateSetListener,
                        year, month, day);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddReservationActivity.this,
                        android.R.style.Theme_Light,
                        mTimeSetListener,
                        hour, minute, true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                final String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (minute < 10) {
                    String time = " " + hourOfDay + ":" + "0" + minute;
                    mDisplayDate.append(time);
                }
                else if (hourOfDay == 0) {
                    String time = " " + "00" + ":" + minute;
                    mDisplayDate.append(time);
                }
                else if (hourOfDay == 0 && minute < 10) {
                    String time = " " + "00" + ":" + "0" + minute;
                    mDisplayDate.append(time);
                }
                else if (hourOfDay < 10) {
                    String time = " " + "0" + hourOfDay + ":" + minute;
                    mDisplayDate.append(time);
                }
                else if (hourOfDay < 10 && minute < 10) {
                    String time = " " + "0" + hourOfDay + ":" + "0" + minute;
                    mDisplayDate.append(time);
                }else {
                    String time = " " + hourOfDay + ":" + minute;
                    mDisplayDate.append(time);

                }
            }
        };

        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddReservationActivity.this,
                        android.R.style.Theme_Light,
                        mDateSetListener2,
                        year, month, day);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddReservationActivity.this,
                        android.R.style.Theme_Light,
                        mTimeSetListener2,
                        hour, minute, true);

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                final String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate2.setText(date);
            }
        };
        mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (minute < 10) {
                    String time = " " + hourOfDay + ":" + "0" + minute;
                    mDisplayDate2.append(time);
                }
                else if (hourOfDay == 0) {
                    String time = " " + "00" + ":" + minute;
                    mDisplayDate2.append(time);
                }
                else if (hourOfDay == 0 && minute < 10) {
                    String time = " " + "00" + ":" + "0" + minute;
                    mDisplayDate2.append(time);
                }
                else if (hourOfDay < 10) {
                    String time = " " + "0" + hourOfDay + ":" + minute;
                    mDisplayDate2.append(time);
                }
                else if (hourOfDay < 10 && minute < 10) {
                    String time = " " + "0" + hourOfDay + ":" + "0" + minute;
                    mDisplayDate2.append(time);
                }
                else {
                    String time = " " + hourOfDay + ":" + minute;
                    mDisplayDate2.append(time);

                }
            }
        };
    }

    public void addReservationButtonClicked(View view) {
        //
        //EDITTEXTS
        EditText userIdField = findViewById(R.id.addReservation_userId_edittext);
        EditText roomIdField = findViewById(R.id.addReservation_roomId_edittext);
        EditText purposeField = findViewById(R.id.addReservation_purpose_edittext);
        TextView messageView = findViewById(R.id.addReservation_message_textview);

        //
        // STRINGS
        String userId = userIdField.getText().toString();
        String roomId = roomIdField.getText().toString();
        String fromTime = mDisplayDate.getText().toString();
        String toTime = mDisplayDate2.getText().toString();
        String purpose = purposeField.getText().toString();



        try { // create JSON document
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("roomId", roomId);
            jsonObject.put("fromTimeString", fromTime);
            jsonObject.put("toTimeString", toTime);
            jsonObject.put("purpose", purpose + " (Reservation made with Tappy®)");
            String jsonDocument = jsonObject.toString();
            messageView.setText(jsonDocument);
            AddReservationTask task = new AddReservationTask();
            task.execute("https://anbo-roomreservation.azurewebsites.net/api/reservations", jsonDocument);
        } catch (JSONException ex) {
            messageView.setText(ex.getMessage());
        }
    }

    private void done() {
        finish();
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
            if (Math.abs(diffX)> SWIPE_THRESHOLD && Math.abs(velocityX)> SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0){
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                result = true;
            }
        } else {
            //up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY)> SWIPE_VELOCITY_THRESHOLD) {
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
        Toast.makeText(this,"Returning home.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    private class AddReservationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            String jsonString = strings[1];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonString);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            } catch (IOException ex) {
                Log.e(CommonStuff.TAG, ex.getMessage());
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(CommonStuff.TAG, "Reservation added");

            done();
        }

        @Override
        protected void onCancelled(String s) {
            Log.d(CommonStuff.TAG, "Problem: Reservation add");
        }

    }

}
