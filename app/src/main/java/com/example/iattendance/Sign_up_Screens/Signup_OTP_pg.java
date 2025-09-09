package com.example.iattendance.Sign_up_Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.iattendance.R;
import com.example.iattendance.Sign_up_Screens.Admin_signup.Admin_signup_pg2;
import com.example.iattendance.Sign_up_Screens.Faculty_signup.Faculty_signup_pg2;
import com.example.iattendance.Sign_up_Screens.Student_signup.Student_signup_pg3;
import com.example.iattendance.Utils.Admin.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Signup_OTP_pg extends AppCompatActivity {

    Intent prevIntentRole;
    String role;
    String coll_code;

    static Long timeout_sec = 60L;
    static String verification_code;
    public static int flag = 0;

    ConstraintLayout pg_bg;
    ProgressBar progress_bar;
    ImageButton back_btn;
    CountryCodePicker country_code;
    TextInputLayout phone_num_tb, otp_tb;
    MaterialButton get_otp, resend_btn, loginBtn;
    LinearLayout ll;
    TextView otp_not_rec;

    Utils utils;
    Timer timer;

    //Firebase auth initialization
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //To resend OTP to the user
    PhoneAuthProvider.ForceResendingToken resending_token;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_otp_pg);

        initializeViews();

        setInProgress(false);

        country_code.registerCarrierNumberEditText(phone_num_tb.getEditText());
        //phoneNumber = country_code.getFullNumberWithPlus();

        back_btn.setOnClickListener(v -> finish());

        get_otp.setOnClickListener(v -> {
            if (!country_code.isValidFullNumber()) {
                phone_num_tb.setError("Invalid phone number");
                return;
            }

            // Get the phone number from EditText
            EditText phoneNumberEditText = phone_num_tb.getEditText();
            if (phoneNumberEditText == null) {
                phone_num_tb.setError("Please enter phone number");
                return;
            }

            String phoneNumberString = String.valueOf(phoneNumberEditText.getText());

            Utils.checkPhoneNumberExists(phoneNumberString)
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Phone number exists
                            Utils.showToast(getApplicationContext(), "Phone number already exists!");
//                            sendOTP(country_code.getFullNumberWithPlus(), false);
                        } else {
                            // Phone number does not exist
                            if (flag == 0) {
                                sendOTP(country_code.getFullNumberWithPlus(), false);
                            } else if (flag == 1) {
                                otp_tb.setFocusable(true);
                                String entered_otp = Objects.requireNonNull(otp_tb.getEditText()).getText().toString();
                                if (entered_otp.isEmpty()) {
                                    otp_tb.setError("Please enter OTP!");
                                } else {
                                    if (verification_code != null) {
                                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, entered_otp);
                                        phoneCredential(credential);
                                        setInProgress(true);
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Utils.showToast(getApplicationContext(), "Failed to retrieve phone number: " + e.getMessage());
                    });
        });

        resend_btn.setOnClickListener(v -> sendOTP(country_code.getFullNumberWithPlus(), true));
    }

    void initializeViews() {
//        Get data passed from previous intent
        prevIntentRole = getIntent();
        role = prevIntentRole.getStringExtra("role");
        coll_code = prevIntentRole.getStringExtra("collegeCode");

        Toast.makeText(this, "ROLE : " + role, Toast.LENGTH_SHORT).show();

//        Hooks
        pg_bg = findViewById(R.id.pg_bg);
        back_btn = findViewById(R.id.back_btn);
        progress_bar = findViewById(R.id.progress_bar);
        country_code = findViewById(R.id.country_code);
        phone_num_tb = findViewById(R.id.phone_num_tb);
        otp_tb = findViewById(R.id.otp_tb);
        get_otp = findViewById(R.id.get_otp);
        resend_btn = findViewById(R.id.resend_btn);
        ll = findViewById(R.id.ll);
        otp_not_rec = findViewById(R.id.otp_not_rec);
        loginBtn = findViewById(R.id.loginBtn);

        utils = new Utils();
    }

    private void phoneCredential(PhoneAuthCredential credential) {
        setInProgress(true);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
//                Intent intent = new Intent(Signup_OTP_pg.this, Admin_signup_pg2.class);
//                intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
//
//                startActivity(intent);
                if (role.equals("Faculty")) {
                    Intent intent = new Intent(Signup_OTP_pg.this, Faculty_signup_pg2.class);
                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
                    intent.putExtra("collegeCode", coll_code);
                    startActivity(intent);
                } else if (role.equals("Student")) {
                    Intent intent = new Intent(Signup_OTP_pg.this, Student_signup_pg3.class);
                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
                    intent.putExtra("collegeCode", coll_code);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), Admin_signup_pg2.class);
                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
                    startActivity(intent);
                }


            } else {
                Utils.showToast(getApplicationContext(), "Incorrect OTP");
            }
        });
    }

    void sendOTP(String phoneNumber, boolean isResend) {
//        startResendTimer();

        setInProgress(true);

        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeout_sec, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                setInProgress(false);
                                get_otp.setText(R.string.nxt);

                                //When OTP sent & verified automatically
                                Toast.makeText(Signup_OTP_pg.this, "ROLE : " + role, Toast.LENGTH_SHORT).show();
                                if (role.equals("Faculty")) {
                                    Intent intent = new Intent(Signup_OTP_pg.this, Faculty_signup_pg2.class);
                                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
                                    intent.putExtra("collegeCode", coll_code);
                                    startActivity(intent);
                                } else if (role.equals("Student")) {
                                    Intent intent = new Intent(Signup_OTP_pg.this, Faculty_signup_pg2.class);
                                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());

                                    startActivity(intent);
                                }

//                                else {
//                                    Intent intent = new Intent(getApplicationContext(), Admin_signup_pg2.class);
//                                    intent.putExtra("phoneNumber", Objects.requireNonNull(phone_num_tb.getEditText()).getText().toString());
//                                    startActivity(intent);
//                                }
                                // To stop timer
                                onPause();
                                finish();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                setInProgress(false);
                                flag = 0;
                                otp_tb.setVisibility(View.GONE);
                                Utils.showToast(getApplicationContext(), "OTP verification failed!");
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                setInProgress(false);
                                flag = 1;
                                verification_code = s;
                                resending_token = forceResendingToken;
                                Utils.showToast(getApplicationContext(), "OTP sent successfully");
                                ll.setVisibility(View.VISIBLE);
                                get_otp.setText(R.string.nxt);
                                otp_tb.setVisibility(View.VISIBLE);
                            }
                        });

        if (isResend) {
            startResendTimer();
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resending_token).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void startResendTimer() {
        ll.setVisibility(View.VISIBLE);
        resend_btn.setEnabled(false);
        Handler handler = new Handler(Looper.getMainLooper());
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                timeout_sec--;
                handler.post(() -> {
                    otp_not_rec.setText(R.string.resend_in);
                    resend_btn.setText("00:" + timeout_sec.toString() + " sec");
                });

                if (timeout_sec <= 0) {
                    onPause();
                    timeout_sec = 60L;
                    timer.cancel();

                    handler.post(() -> {
                        otp_not_rec.setText(R.string.otp_not_received);
                        resend_btn.setEnabled(true);
                        resend_btn.setText(R.string.resend_otp);
                    });
                }
            }
        }, 0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cancel the timer to stop it when the activity is paused
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            //code for progress bar visibility
            //pg_bg.setBackgroundColor(getResources().getColor(R.color.progress_bar_bg));
            progress_bar.setVisibility(View.VISIBLE);
            country_code.setEnabled(false);
            phone_num_tb.setEnabled(false);
            get_otp.setEnabled(false);
            resend_btn.setEnabled(false);
            loginBtn.setEnabled(false);
        } else {
            progress_bar.setVisibility(View.GONE);
            pg_bg.setBackgroundColor(0);
            country_code.setEnabled(true);
            phone_num_tb.setEnabled(true);
            get_otp.setEnabled(true);
            resend_btn.setEnabled(true);
            loginBtn.setEnabled(true);
        }
    }
}