package com.example.destination.Activityes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destination.R;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    TextView editProfileOption;
    TextView logoutOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

         editProfileOption = findViewById(R.id.editProfileOption);
         logoutOption = findViewById(R.id.logoutOption);

        editProfileOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Edit Profile click
                Intent intent = new Intent(Settings.this, Edit_Profile.class);
                startActivity(intent);
            }
        });

        logoutOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              //  Toast.makeText(Settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
}