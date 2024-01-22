package com.example.carsrental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carsrental.entities.User;
import com.example.carsrental.firebase.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText fullNameEdit,phoneEdit,emailEdit,passwordEdit,confirmPasswordEdit;
    FirebaseAuth mAuth;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fullNameEdit=findViewById(R.id.fullName);
        phoneEdit=findViewById(R.id.phone);
        emailEdit=findViewById(R.id.email);
        passwordEdit=findViewById(R.id.password);
        confirmPasswordEdit=findViewById(R.id.confirm_password);

        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }
    public void signup(View view) {
        String fullName,phone,email,password,confirmPassword;
        fullName=fullNameEdit.getText().toString();
        phone=phoneEdit.getText().toString();
        email=emailEdit.getText().toString();
        password=passwordEdit.getText().toString();
        confirmPassword=confirmPasswordEdit.getText().toString();

        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        String phonePattern="^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

        if (fullName.isEmpty()){
            showMessage("Full name is empty?");
        }else if (email.isEmpty()){
            showMessage("Email is empty");
        }else if (!Pattern.matches(emailPattern,email)){
            showMessage("Not an email Address!");
        }else if (phone.isEmpty()){
            showMessage("Phone is empty!");
        } else if (!Pattern.matches(phonePattern,phone)) {
            showMessage("Not a phone number!");
        }else if (password.isEmpty()){
            showMessage("Password don't match!");
        }else if (password.length()<6){
            showMessage("Password must be at least 6 characters!");
        }else if (confirmPassword.isEmpty() || !confirmPassword.equals(password)){
            showMessage("Password don't match!");
        }else {
            User user=new User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> Toast.makeText(SignupActivity.this,"Signup successfully",Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(e -> Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_LONG).show());
                            startActivity(new Intent(SignupActivity.this,SigninActivity.class));
                        } else {
                            Exception exception = task.getException();
                            Toast.makeText(SignupActivity.this,"Signup failed. Please try again",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    public void  showMessage(String message){
        Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void signin(View view) {
        startActivity(new Intent(SignupActivity.this,SigninActivity.class));
    }
}