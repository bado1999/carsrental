package com.example.carsrental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String email,password;

    BottomNavigationView navigationView;
    Window window;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FragmentContainerView fragmentContainerView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= this.getSharedPreferences("login", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("email","");
        password=sharedPreferences.getString("password","");
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        window=this.getWindow();
        setupWindow();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        fragmentContainerView= findViewById(R.id.fragment_container_view);

        navigationView= findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId()==R.id.home){
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, Home.class, null)
                        .commit();
            } else if (item.getItemId()==R.id.reservation) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, Reservation.class, null)
                        .commit();
            } else if (item.getItemId()==R.id.about) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, About.class, null)
                        .commit();
            } else if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, Profile.class, null)
                        .commit();
            }else {
                return false;
            }
            return true;
        });
    }


    public void setupWindow(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.blue));
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.white));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void signin() {
        if (!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true)
                                    .add(R.id.fragment_container_view, Home.class, null)
                                    .commit();
                        } else {
                            startActivity(new Intent(MainActivity.this,SigninActivity.class));
                        }
                    });
        }else{
            startActivity(new Intent(MainActivity.this,SigninActivity.class));
        }

    }

    @Override
    public void onStateNotSaved() {
        signin();
        super.onStateNotSaved();
    }
}