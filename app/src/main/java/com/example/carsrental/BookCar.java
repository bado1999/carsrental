package com.example.carsrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carsrental.entities.Car;
import com.example.carsrental.firebase.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;

public class BookCar extends AppCompatActivity  {
    private TextView capacity;
    private  TextView price;
    private TextView name;
    private TextView pick_date;
    private TextView pick_time;
    private TextView return_time;
    private TextView return_date;
    private Button booking;
    private Button cancel;
    Car car;
    Toolbar toolbar;
    Calendar calendar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_car);
        toolbar=findViewById(R.id.book_car_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        window=getWindow();
        setupWindow();
        Intent intent=getIntent();
        String carString=intent.getStringExtra("car");
        GsonBuilder builder=new GsonBuilder();
        car=builder.create().fromJson(carString, Car.class);
        name=findViewById(R.id.car_title);
        capacity=findViewById(R.id.seats);
        price=findViewById(R.id.price);
        pick_date=findViewById(R.id.pickup_date);
        return_date=findViewById(R.id.return_date);
        pick_time=findViewById(R.id.pickup_time);
        return_time=findViewById(R.id.return_time);
        booking=findViewById(R.id.book);
        name.setText(car.getNom());
        capacity.setText(car.getNom());
        price.setText(car.getRentPrice()+" dh/day");
        cancel=findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> onBackPressed());

    }

    public void setupWindow(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.white));
    }
    public void onClick(View view){

        switch (view.getId()){
            case R.id.book:
                FirebaseAuth auth=FirebaseAuth.getInstance();
                FirebaseUser user=auth.getCurrentUser();
                if (pick_date.toString().equals("Date")||pick_time.toString().equals("Time")||return_date.toString().equals("Date")||return_time.toString().equals("Time")){
                    showMessage("Invalid Date and Time!");
                    break;
                }
                Database.book(user.getEmail(),car.getReference(),pick_date.getText().toString()+" At "+pick_time.getText().toString(),return_date.getText().toString()+" At "+return_time.getText().toString(),BookCar.this);
                notify(car.getNom()+"\n"+car.getRentPrice()+"dh/day");
                break;
            case R.id.pickup_date: {
                pickDate(pick_date,Calendar.getInstance());
                break;
                }
            case R.id.return_date:{
                pickDate(return_date,Calendar.getInstance());
                break;
                }

            case R.id.pickup_time:{
                pickTime(pick_time,Calendar.getInstance());
                break;
            }
            case R.id.return_time:{
                pickTime(return_time,Calendar.getInstance());
                break;
        }
        }

    }
    public void notify(String message){
        Notification.Builder builder;
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("channel","Name",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder=new Notification.Builder(this,"channel");
        }else {
            builder=new Notification.Builder(BookCar.this);
        }
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setShowWhen(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("Car booked Successfully");
        builder.setContentText(message);
        notificationManager.notify(1,builder.build());
    }
    public  void  showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public  void pickDate(TextView view,Calendar calendar){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, month1, dayOfMonth) -> {
            view.setText(dayOfMonth+"-"+ month1 +"-"+ year1);
            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void pickTime(TextView textView,Calendar calendar){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> textView.setText(hourOfDay+":"+minute),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
}