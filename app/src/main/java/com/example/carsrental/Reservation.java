package com.example.carsrental;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carsrental.entities.Booking;
import com.example.carsrental.firebase.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Reservation extends Fragment {

    public Reservation() {
        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BookingAdapter adapter=new BookingAdapter();
        RecyclerView bookingRecycler=view.findViewById(R.id.myBookings);
        bookingRecycler.setAdapter(adapter);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        Database.loadReservations(user.getEmail(),adapter);
        bookingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOnClickListener(brand -> Database.cancelReservation(brand.getId(),adapter));
        super.onViewCreated(view, savedInstanceState);
    }
}