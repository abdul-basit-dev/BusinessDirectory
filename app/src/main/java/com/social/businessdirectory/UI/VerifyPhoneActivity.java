package com.social.businessdirectory.UI;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.auth.User;
import com.social.businessdirectory.MainActivity;
import com.social.businessdirectory.R;


import java.util.concurrent.TimeUnit;


public class VerifyPhoneActivity extends AppCompatActivity {

    TextView txtMob;

    EditText edOtp1;

    EditText edOtp2;

    EditText edOtp3;

    EditText edOtp4;

    EditText edOtp5;

    EditText edOtp6;


    TextView btnReenter, btn_send;

    public static int isvarification = -1;

    TextView btnTimer;
    private String verificationId;
    private FirebaseAuth mAuth;
    String phonenumber;
    String phonecode;

    //SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

//        sessionManager = new SessionManager(VerifyPhoneActivity.this);
//        if (isvarification == 2) {
//            user = (User) getIntent().getSerializableExtra("user");
//        } else {
//            user = sessionManager.getUserDetails("");
//        }
        mAuth = FirebaseAuth.getInstance();
        phonenumber = getIntent().getStringExtra("phone");
        //phonecode = getIntent().getStringExtra("code");

        txtMob = findViewById(R.id.txt_mob);
        edOtp1 = findViewById(R.id.ed_otp1);
        edOtp2 = findViewById(R.id.ed_otp2);
        edOtp3 = findViewById(R.id.ed_otp3);
        edOtp4 = findViewById(R.id.ed_otp4);
        edOtp5 = findViewById(R.id.ed_otp5);
        edOtp6 = findViewById(R.id.ed_otp6);

        btn_send = findViewById(R.id.btn_send);
        btnReenter = findViewById(R.id.btn_reenter);
        btnTimer = findViewById(R.id.btn_timer);

        // sendVerificationCode("+" + 92 + phonenumber);
        sendVerificationCode("+923430542724");
        //sendVerificationCode(formatE164Number(phonecode, phonenumber));
//        sendVerificationCode(PhoneNumberUtils.formatNumberToE164(phonecode,phonenumber));

        txtMob.setText("Enter the 6-digit code sent to you \nat " + phonenumber);
        try {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    btnTimer.setText(seconds + " Second Wait");
                }

                @Override
                public void onFinish() {
                    btnReenter.setVisibility(View.VISIBLE);
                    btnTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        edOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("count", "" + count);
                Log.e("count", "" + count);
                Log.e("count", "" + count);

                if (s.length() == 1) {
                    edOtp3.requestFocus();
                } else if (count == 0) {
                    edOtp1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp4.requestFocus();
                } else if (count == 0) {
                    edOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp5.requestFocus();
                } else if (count == 0) {
                    edOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 1) {
                    edOtp6.requestFocus();
                } else if (count == 0) {
                    edOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edOtp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    edOtp6.requestFocus();
                } else if (count == 0) {
                    edOtp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });

        btnReenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reEnterCode();
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        startActivity(new Intent(VerifyPhoneActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("code", phonecode).putExtra("phone", phonenumber));

                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
//    private void signInWithCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        switch (isvarification) {
//                            case 0:
//                                Intent intent = new Intent(VerifyPhoneActivity.this, ChanegPasswordActivity.class);
//                                intent.putExtra("phone", phonenumber);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                startActivity(new Intent(VerifyPhoneActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).putExtra("code", phonecode).putExtra("phone", phonenumber));
//                                break;
//                            default:
//                                break;
//                        }
//                    } else {
//                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

    private void sendVerificationCode(String number) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                number,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallBack
//        );

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                edOtp1.setText("" + code.substring(0, 1));
                edOtp2.setText("" + code.substring(1, 2));
                edOtp3.setText("" + code.substring(2, 3));
                edOtp4.setText("" + code.substring(3, 4));
                edOtp5.setText("" + code.substring(4, 5));
                edOtp6.setText("" + code.substring(5, 6));
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // User user1 = new User();
//            user1.setId("0");
//            user1.setFname("User");
//            user1.setEmail("user@gmail.com");
//            user1.setMobile("+91 8888888888");
            // sessionManager.setUserDetails("", user1);
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    };


    public void sendCode() {
        if (validation()) {
            verifyCode(edOtp1.getText().toString() + "" + edOtp2.getText().toString() + "" + edOtp3.getText().toString() + "" + edOtp4.getText().toString() + "" + edOtp5.getText().toString() + "" + edOtp6.getText().toString());
        }
    }

    public void reEnterCode() {
        btnReenter.setVisibility(View.GONE);
        btnTimer.setVisibility(View.VISIBLE);
        try {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    btnTimer.setText(seconds + " Secound Wait");
                }

                @Override
                public void onFinish() {
                    btnReenter.setVisibility(View.VISIBLE);
                    btnTimer.setVisibility(View.GONE);
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendVerificationCode("+" + phonecode + phonenumber);
        //sendVerificationCode(formatE164Number(phonecode, phonenumber));
        //sendVerificationCode(PhoneNumberUtils.formatNumberToE164(phonecode,phonenumber));
        //sendVerificationCode("+923430542724");
    }
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_send:
//                if (validation()) {
//                    verifyCode(edOtp1.getText().toString() + "" + edOtp2.getText().toString() + "" + edOtp3.getText().toString() + "" + edOtp4.getText().toString() + "" + edOtp5.getText().toString() + "" + edOtp6.getText().toString());
//                }
//                break;
//            case R.id.btn_reenter:
//                btnReenter.setVisibility(View.GONE);
//                btnTimer.setVisibility(View.VISIBLE);
//                try {
//                    new CountDownTimer(60000, 1000) {
//                        @Override
//                        public void onTick(long millisUntilFinished) {
//                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
//                            btnTimer.setText(seconds + " Secound Wait");
//                        }
//
//                        @Override
//                        public void onFinish() {
//                            btnReenter.setVisibility(View.VISIBLE);
//                            btnTimer.setVisibility(View.GONE);
//                        }
//                    }.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                sendVerificationCode("+" + phonecode + phonenumber);
//                //sendVerificationCode(formatE164Number(phonecode, phonenumber));
//                //sendVerificationCode(PhoneNumberUtils.formatNumberToE164(phonecode,phonenumber));
//                //sendVerificationCode("+923430542724");
//                break;
//            default:
//                break;
//        }
//    }


    public String formatE164Number(String countryCode, String phNum) {

        // String e164Number = phNum;


        return PhoneNumberUtils.formatNumberToE164(phNum, countryCode);
    }

    public boolean validation() {

        if (edOtp1.getText().toString().isEmpty()) {
            edOtp1.setError("");
            return false;
        }
        if (edOtp2.getText().toString().isEmpty()) {
            edOtp2.setError("");
            return false;
        }
        if (edOtp3.getText().toString().isEmpty()) {
            edOtp3.setError("");
            return false;
        }
        if (edOtp4.getText().toString().isEmpty()) {
            edOtp4.setError("");
            return false;
        }
        if (edOtp5.getText().toString().isEmpty()) {
            edOtp5.setError("");
            return false;
        }
        if (edOtp6.getText().toString().isEmpty()) {
            edOtp6.setError("");
            return false;
        }
        return true;
    }
}
