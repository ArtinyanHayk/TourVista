package com.example.destination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destination.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;

public class Registration extends AppCompatActivity {
    public static final String Tag = "Tag";
    EditText fullName,Email,Password,confirmPass;
    Button registerBtn;
    TextView loginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    UserModel userModel;
    FirebaseUser user;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPass);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn = findViewById(R.id.loginText);
        fullName = findViewById(R.id.fullName);
        progressBar = findViewById(R.id.progressBar);

        userModel = new UserModel();

        fAuth = FirebaseAuth.getInstance();

        user = fAuth.getCurrentUser();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String name = fullName.getText().toString();
                String password = Password.getText().toString();
                String confirmPassword = confirmPass.getText().toString();

                if(TextUtils.isEmpty(name)){
                    fullName.setError("Full name is empty!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is empty!");
                    return;
                }
                if(password.length() < 6){
                    Password.setError("Password must be => 6 characters");
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword) || !confirmPassword.equals(password)){
                    confirmPass.setError("Invalid password");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                 fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){

                             Toast.makeText(Registration.this, "Account crated", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(Registration.this,Login.class);
                             intent.putExtra("name",name);
                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                             startActivity(intent);



                             //Send verification code
                             FirebaseUser fuser = fAuth.getCurrentUser();
                             fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     Toast.makeText(Registration.this, "Verification code has been  Sent", Toast.LENGTH_SHORT).show();

                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Log.d(Tag, "onFailure: email not sent" + e.getMessage());
                                     

                                 }
                             });



                         }
                         else {
                             Toast.makeText(Registration.this, "fail", Toast.LENGTH_SHORT).show();
                             progressBar.setVisibility(View.GONE);
                         }

                     }
                 });





            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}