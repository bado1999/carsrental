package com.example.carsrental;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carsrental.entities.Car;
import com.example.carsrental.firebase.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

public class CarDetails extends AppCompatActivity {
    private TextView name;
    private TextView capacity;
    private TextView status;
    private  TextView price;
    private TextView engine;
    private TextView maxSpeed;
    private FirebaseFirestore db ;
    private ShapeableImageView imageView;
    private Button bookingBtn;
    private Toolbar toolbar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        name=findViewById(R.id.car_title);
        status=findViewById(R.id.status);
        capacity=findViewById(R.id.capacity);
        imageView=findViewById(R.id.car_photo);
        price=findViewById(R.id.price);
        maxSpeed=findViewById(R.id.speed);
        engine=findViewById(R.id.engine);
        toolbar=findViewById(R.id.details_toolbar);
        window=getWindow();
        setupWindow();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Intent intent=getIntent();
        String carReference=intent.getStringExtra("carReference");
        db=FirebaseFirestore.getInstance();
        db.collection("cars").document(carReference)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Car car=documentSnapshot.toObject(Car.class);
                        car.setReference(documentSnapshot.getId());
                        name.setText(car.getNom());
                        status.setText(car.getStatus()?"Available":"Unavailable");
                        Resources resources=getResources();
                        int red=resources.getColor(R.color.red),green=resources.getColor(R.color.green);
                        status.setTextColor(car.getStatus()?green:red);
                        capacity.setText(car.getCapacity());
                        engine.setText(car.getType());
                        maxSpeed.setText(car.getMaxSpeed());
                        price.setText(car.getRentPrice()+" dh/day");
                        Storage.getImage(car.getImage(),imageView,CarDetails.this);
                        bookingBtn=findViewById(R.id.book);
                        bookingBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(car.getStatus()){
                                    Intent intent=new Intent(CarDetails.this,BookCar.class);
                                    GsonBuilder builder=new GsonBuilder();
                                    String carString= builder.create().toJson(car,Car.class);
                                    intent.putExtra("car",carString);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(CarDetails.this, "Sorry this car is not available", Toast.LENGTH_SHORT).show();
                                };
                            }
                        });
                    }
                });
    }

    public void setupWindow(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.white));
    }
}