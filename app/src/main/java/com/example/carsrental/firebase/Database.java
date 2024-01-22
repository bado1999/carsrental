package com.example.carsrental.firebase;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.carsrental.BookingAdapter;
import com.example.carsrental.MainActivity;
import com.example.carsrental.entities.Booking;
import com.example.carsrental.entities.Car;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Database extends ViewModel {
    private MutableLiveData<List<Car>> listCars;
    private static FirebaseFirestore db=FirebaseFirestore.getInstance();
    private static MutableLiveData<Car> carToBook;
    private static MutableLiveData<List<Booking>> myBookings;
    public  void loadCars(){
        db.collection("cars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Car> cars=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Car car=document.toObject(Car.class);
                                car.setReference(document.getId());
                                cars.add(car);
                            }
                            listCars=new MutableLiveData<>();
                            listCars.setValue(cars);
                        }
                    }
                });

    }
    public List<Car> getCars(){
        if (listCars!=null)
            return listCars.getValue();
        listCars=new MutableLiveData<>();
        loadCars();
        return listCars.getValue();
    }

    public void loadCarById(String reference){
        db.collection("cars").document(reference)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Car car=documentSnapshot.toObject(Car.class);
                        car.setReference(documentSnapshot.getId());
                        carToBook.setValue(car);
                    }
                });
    }

    public Car getCarToBook(String reference){
        if (carToBook!=null)
            return carToBook.getValue();
        carToBook=new MutableLiveData<>();
        loadCarById(reference);
        return carToBook.getValue();
    }

    public List<Car> getCarsByMarque(String marque){
        return listCars.getValue()
                .stream()
                .filter(car -> car.getMarque().equals(marque))
                .collect(Collectors.toList());
    }

    public static void loadReservations(String email, BookingAdapter adapter){
        db.collection("reservation")
                .whereEqualTo("client",email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Booking> bookings=new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Booking booking=documentSnapshot.toObject(Booking.class);
                            booking.setId(documentSnapshot.getId());
                            bookings.add(booking);
                        }
                        adapter.setBrands(bookings);
                    }
                });
    }

    //Get my reservations

    //Booking

    public static void book(String email, String carReference, String book_date, String return_date, Context context){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("cars").document(carReference)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Car car=documentSnapshot.toObject(Car.class);
                    Booking reservation=new Booking();

                    reservation.setClient(email);
                    reservation.setCar(car);
                    reservation.setReservationDate(book_date);
                    reservation.setReturnDate(return_date);
                    db.collection("reservation")
                            .add(reservation)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(context,"Your reservation is successfully registered",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                }
                                else
                                    Toast.makeText(context,"An error has occurred please try again later",Toast.LENGTH_LONG).show();
                            });
                });
    }

    public static void cancelReservation(String booking_reference,BookingAdapter adapter) {
        db.collection("reservation")
                .document(booking_reference)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        List<Booking> newBooking = adapter.getBrandList()
                                .stream()
                                .filter(booking -> !booking.getId().equals(booking_reference))
                                .collect(Collectors.toList());
                        adapter.setBrands((ArrayList<Booking>) newBooking);
                    }
                });
    }

}
