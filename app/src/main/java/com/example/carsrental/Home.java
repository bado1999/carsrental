package com.example.carsrental;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsrental.entities.Brand;
import com.example.carsrental.entities.Car;
import com.example.carsrental.firebase.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {

    RecyclerView carsRecyclerview;
    RecyclerView brandsRecyclerview;
    BrandAdapter brandAdapter;
    CarAdapter carAdapter;
    Context context;
    Database database;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        brandsRecyclerview=view.findViewById(R.id.brand_recycle_view);
        carsRecyclerview=view.findViewById(R.id.cars_recycle_view);
        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        carAdapter=new CarAdapter();
        brandAdapter=new BrandAdapter();
        brandAdapter.setContext(getContext());
        fetchBrands(brandAdapter);
        fetchCars(carAdapter);
        carsRecyclerview.setAdapter(carAdapter);
        brandsRecyclerview.setAdapter(brandAdapter);
        carsRecyclerview.setLayoutManager(new GridLayoutManager(context,2));
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        brandsRecyclerview.setLayoutManager(manager);
        carAdapter.setOnClickListener((position, car) -> {
            Intent intent = new Intent(context, CarDetails.class);
            intent.putExtra("carReference", car.getReference());
            startActivity(intent);
        });
        brandAdapter.setOnClickListener((brand)->{
            if (brand.getNom().equals("All")){
                fetchCars(carAdapter);
                return;
            }
            fetchCarsByMarque(carAdapter,brand.getNom() );
        });
        super.onViewCreated(view, savedInstanceState);
    }
    public void fetchCars( CarAdapter carAdapter){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
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
                            carAdapter.setCars(cars);
                        }
                    }
                });

    }
    public void fetchCarsByMarque( CarAdapter carAdapter,String marque){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("cars")
                .whereEqualTo("marque",marque)
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
                            carAdapter.setCars(cars);
                        }
                    }
                });

    }
    public void fetchBrands(BrandAdapter brandAdapter){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("marques")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Brand> brands=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Brand brand=document.toObject(Brand.class);
                            brand.setReference(document.getId());
                            brands.add(brand);
                        }
                        brandAdapter.setBrands(brands);
                    }
                });

    }

}