package com.example.destination.Activityes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destination.R;
import com.example.destination.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText Email,Password;
    Button loginBtn;
    TextView createBtn,forgetTextLink;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    UserModel userModel;
    FirebaseUser user;
    FirebaseUser fuser;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        createBtn = findViewById(R.id.createText);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        forgetTextLink = findViewById(R.id.forgotPassword);
        imageView = findViewById(R.id.imageView);

       // DisplayMetrics displayMetrics = Login.this.getResources().getDisplayMetrics();
       // int screenWidth = displayMetrics.widthPixels;
       // BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inJustDecodeBounds = true;
       // BitmapFactory.decodeResource(Login.this.getResources(), R.drawable.map_icon, options);
       // float aspectRatio = options.outWidth / (float) options.outHeight;
       // int calculatedHeight = (int) (screenWidth / aspectRatio);
       // ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
       // layoutParams.height = calculatedHeight;
       // imageView.setLayoutParams(layoutParams);

        fAuth = FirebaseAuth.getInstance();
        userModel = new UserModel();
        user = fAuth.getCurrentUser();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is empty!");
                    return;
                }
                if(password.length() < 6){
                    Password.setError("Password must be => 6 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //check if user is verified
                             fuser = fAuth.getCurrentUser();
                            assert fuser != null;
                            if(fuser.isEmailVerified()){



                                progressBar.setVisibility(View.GONE);

                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();


                            }else{
                                Toast.makeText(Login.this, "please verify your email", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }


                        }else{
                            Toast.makeText(Login.this, "fail", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }
                });
            }

        });

        forgetTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
    }
}