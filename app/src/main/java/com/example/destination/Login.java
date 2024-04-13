package com.example.destination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class Login extends AppCompatActivity {
    EditText Email,Password;
    Button loginBtn;
    TextView createBtn,forgetTextLink;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    UserModel userModel;
    FirebaseUser user;
    FirebaseUser fuser;

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


                                CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
                                reference.whereEqualTo("id", fuser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (queryDocumentSnapshots.isEmpty()) {
                                            Toast.makeText(Login.this, "Accaunt created", Toast.LENGTH_SHORT).show();
                                            // Аккаунт с таким email не существует
                                            DocumentReference userDoc = reference.document(fuser.getUid());
                                            userModel.setUserName(getIntent().getExtras().getString("name"));
                                            userModel.setFolowers(0);
                                            userModel.setUserId(user.getUid());
                                            userModel.setFolowing(123);
                                            userModel.setPhone("null");
                                            userModel.setPhone("0");
                                            userModel.setCreatedTimesetap(new Timestamp(new Date(System.currentTimeMillis())));
                                            userModel.setStatus("");

                                            userDoc.set(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    progressBar.setVisibility(View.GONE);
                                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                }
                                            });



                                        } else {
                                            // Аккаунт с таким email уже существует
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            // Вы можете обработать это здесь, например, показать сообщение пользователю
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Обработка ошибки
                                    }
                                });



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
                startActivity(new Intent(Login.this,Registration.class));
            }
        });
    }
}