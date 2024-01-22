package com.example.carsrental;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carsrental.entities.User;
import com.example.carsrental.firebase.Database;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends Fragment implements View.OnClickListener {
    private Button logout;
    private EditText email;
    private EditText fullName;
    private EditText phone;
    private User user1;
    private Button save;
    SharedPreferences sharedPreferences;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        logout=view.findViewById(R.id.call);
        save=view.findViewById(R.id.save);
        phone=view.findViewById(R.id.phone);
        email=view.findViewById(R.id.email);
        fullName=view.findViewById(R.id.fullName);
        logout.setOnClickListener(this::onClick);
        save.setOnClickListener(this::onClick);
        email.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        sharedPreferences=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("email",user.getEmail())
                        .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        DocumentSnapshot documentSnapshot=queryDocumentSnapshots.getDocuments().get(0);
                                        user1=documentSnapshot.toObject(User.class);
                                        user1.setId(documentSnapshot.getId());
                                        phone.setText(user1.getPhone());
                                        email.setText(user1.getEmail());
                                        fullName.setText(user1.getFullName());
                                    }
                                });
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public  void onClick(View view){
        if (view.getId()==R.id.call){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.remove("email");
            editor.remove("password");
            editor.apply();
           Intent intent=new Intent(getContext(),SigninActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
        if (view.getId()==R.id.save){
            Map<String,Object> data=new HashMap<>();
            data.put("email",email.getText().toString());
            data.put("phone",phone.getText().toString());
            data.put("fullName",fullName.getText().toString());
            FirebaseFirestore.getInstance().collection("users")
                    .document(user1.getId())
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if (view.getId()==R.id.email){
            Toast.makeText(getContext(), "You cannot Change your email", Toast.LENGTH_SHORT).show();
        }
    }

}