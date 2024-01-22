package com.example.carsrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {
    EditText emailEdit,passwordEdit;
    FirebaseAuth mAuth;
    FirebaseFirestore db ;
    SharedPreferences sharedPreferences;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        emailEdit=findViewById(R.id.email);
        passwordEdit=findViewById(R.id.password);
        checkBox=findViewById(R.id.remember_me);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = this.getSharedPreferences(
                "login", Context.MODE_PRIVATE);

    }

    public void signin(View view) {
        String email,password;
        email=emailEdit.getText().toString();
        password=passwordEdit.getText().toString();

        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (email.isEmpty()){
            showMessage("Email is empty");
        }else if (!Pattern.matches(emailPattern,email)){
            showMessage("Not an email Address!");
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SigninActivity.this,"Successfully login",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SigninActivity.this,MainActivity.class));
                            if (checkBox.isChecked()){
                            sharedPreferences.edit().putString("email",email).commit();
                            sharedPreferences.edit().putString("password",password).commit();}
                        } else {
                            Exception exception = task.getException();
                            Toast.makeText(SigninActivity.this,"Bad credentials",Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }
    public void  showMessage(String message){
        Toast.makeText(SigninActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void signup(View view) {
        startActivity(new Intent(SigninActivity.this,SignupActivity.class));
    }
}