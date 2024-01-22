package com.example.carsrental.firebase;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.carsrental.SigninActivity;
import com.example.carsrental.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authentication {
    final static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static void login(String email,String password,Context context){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(context,"Successfully login",Toast.LENGTH_LONG).show();

                    } else {
                        Exception exception = task.getException();
                        Toast.makeText(context,"Bad credentials",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public static void signup(String email,String password,String phone,String fullName,Context context){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user=new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(documentReference -> Toast.makeText(context,"Signup successfully",Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e -> Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show());
                    } else {
                        Exception exception = task.getException();
                        Toast.makeText(context,"Signup failed. Please try again",Toast.LENGTH_LONG).show();
                    }
                });
        
    }

}
