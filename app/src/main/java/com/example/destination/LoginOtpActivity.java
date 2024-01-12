package com.example.destination;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.destination.utils.AndroidUtil;
import com.example.destination.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;




public class LoginOtpActivity extends AppCompatActivity {
 String phoneNumber;
 Long timeoutSeconds = 60L;

 EditText otpInput;
 Button nextBtn;
 ProgressBar progressBar;
 TextView resendOtpTextView;
 FirebaseAuth mAuth = FirebaseAuth.getInstance();
 String verificationCode;
 PhoneAuthProvider.ForceResendingToken resendingToken;

 @SuppressLint("MissingInflatedId")
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login_otp);

  otpInput = findViewById(R.id.login_input);
  nextBtn = findViewById(R.id.login_next_btn);
  progressBar = findViewById(R.id.login_progress_bar2);
  resendOtpTextView = findViewById(R.id.resend_code_textview);

  phoneNumber = getIntent().getExtras().getString("phone");
  sendOtp(phoneNumber, false);
  nextBtn.setOnClickListener(v -> {
   String enteredCode = otpInput.getText().toString();
   PhoneAuthCredential credential =  PhoneAuthProvider.getCredential(verificationCode,enteredCode);
   signIn(credential);
   setInProgress(true);


  });
  resendOtpTextView.setOnClickListener(v -> {
   sendOtp(phoneNumber,true);
  });
 }


 void sendOtp(String phoneNumber, boolean isResend) {
  setInProgress(true);
  startResendTimer();

  PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
          .setPhoneNumber(phoneNumber)
          .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
          .setActivity(this)
          .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           @Override
           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signIn(phoneAuthCredential);
            setInProgress(false);
           }

           @Override
           public void onVerificationFailed(@NonNull FirebaseException e) {
            AndroidUtil.showToast(getApplicationContext(), "OTP verification is failed");
            setInProgress(false);
           }

           @Override
           public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
            resendingToken = forceResendingToken;
            AndroidUtil.showToast(getApplicationContext(), "OTP sent successfully");
            setInProgress(false);
           }
          });

  if (isResend) {
   // Вызываем метод verifyPhoneNumber с использованием ForceResendingToken для повторной отправки кода
   PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
  } else {
   // Обычный вызов метода verifyPhoneNumber
   PhoneAuthProvider.verifyPhoneNumber(builder.build());
  }
 }

 void setInProgress(boolean inProgress) {
  if (inProgress) {
   progressBar.setVisibility(View.VISIBLE);
   nextBtn.setVisibility(View.GONE);
  } else {
   progressBar.setVisibility(View.GONE);
   nextBtn.setVisibility(View.VISIBLE);
  }
 }

 void signIn(PhoneAuthCredential phoneAuthCredential) {
  setInProgress(true);
  mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
   @Override
   public void onComplete(@NonNull Task<AuthResult> task) {
    setInProgress(false);
    if(task.isSuccessful()){

     Intent intent = new Intent(LoginOtpActivity.this, LoginUsernameActivity.class);
     intent.putExtra("phone",phoneNumber);
     startActivity(intent);

    }else{


     AndroidUtil.showToast(getApplicationContext(), "OTP verafication failed");
    }


   }
  });

 }
 void startResendTimer(){
  resendOtpTextView.setEnabled(false);
  Timer timer = new Timer();
  timer.scheduleAtFixedRate(new TimerTask() {
   @Override
   public void run() {
    timeoutSeconds--;
    resendOtpTextView.setText("Resend OTP in " + timeoutSeconds + "seconds");
    if(timeoutSeconds <= 0){
     timeoutSeconds = 60L;
     timer.cancel();
     runOnUiThread(() -> {
      resendOtpTextView.setEnabled(true);

     });
    }

   }
  },0 , 1000);
 }
}