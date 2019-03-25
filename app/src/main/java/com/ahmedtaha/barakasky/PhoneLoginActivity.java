package com.ahmedtaha.barakasky;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private Button sendverificationCodeButton, verifiyButton;
    private EditText phoneNumber_input, verifiy_input;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_phone_login_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();

        sendverificationCodeButton = (Button) findViewById(R.id.send_ver_code_button);
        verifiyButton = (Button) findViewById(R.id.verify_button);
        phoneNumber_input = (EditText) findViewById(R.id.phone_number_input);
        verifiy_input = (EditText) findViewById(R.id.verifiction_code_input);
        loadingBar = new ProgressDialog(this);
        /*String locale = getResources().getConfiguration().locale.getCountry();
        Toast.makeText(PhoneLoginActivity.this, "Country : " + locale, Toast.LENGTH_LONG).show();

        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        Toast.makeText(PhoneLoginActivity.this, "Countr : " + countryCodeValue, Toast.LENGTH_LONG).show();*/

        sendverificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Phone_Number = phoneNumber_input.getText().toString();
                if (TextUtils.isEmpty(Phone_Number)){
                    Toast.makeText(PhoneLoginActivity.this, getResources().getString(R.string.phone_login_phone_number_text_isempty), Toast.LENGTH_LONG).show();
                } else {
                    loadingBar.setTitle(getResources().getString(R.string.phone_login_verification_code_loadingbar_title));
                    loadingBar.setMessage(getResources().getString(R.string.phone_login_verification_code_loadingbar_message));
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            Phone_Number,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneLoginActivity.this,               // Activity (for callback binding)
                            callbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        verifiyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationCodeButton.setVisibility(View.GONE);
                phoneNumber_input.setVisibility(View.GONE);
                String verifactionCode_tx = verifiy_input.getText().toString();
                if (TextUtils.isEmpty(verifactionCode_tx)){
                    Toast.makeText(PhoneLoginActivity.this, getResources().getString(R.string.phone_login_phone_numberverfication_code_text_isempty), Toast.LENGTH_LONG).show();
                } else{
                    loadingBar.setTitle(getResources().getString(R.string.phone_login_verify_verification_code_loadingbar_title));
                    loadingBar.setMessage(getResources().getString(R.string.phone_login_verify_verification_code_loadingbar_message));
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verifactionCode_tx);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, getResources().getString(R.string.phone_login_phone_number_text_invaild), Toast.LENGTH_LONG).show();
                sendverificationCodeButton.setVisibility(View.VISIBLE);
                phoneNumber_input.setVisibility(View.VISIBLE);

                verifiyButton.setVisibility(View.GONE);
                verifiy_input.setVisibility(View.GONE);
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                loadingBar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, getResources().getString(R.string.phone_login_phone_number_text_sent), Toast.LENGTH_LONG).show();
                sendverificationCodeButton.setVisibility(View.GONE);
                phoneNumber_input.setVisibility(View.GONE);

                verifiyButton.setVisibility(View.VISIBLE);
                verifiy_input.setVisibility(View.VISIBLE);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, getResources().getString(R.string.phone_login_verify_success), Toast.LENGTH_LONG).show();
                            sendUserToMainActivity();
                        } else {
                            loadingBar.dismiss();
                            String message1 = task.getException().getMessage();
                            String message2 = getResources().getString(R.string.phone_login_toast_not_success);
                            Toast.makeText(PhoneLoginActivity.this,message2 + " " + message1,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendUserToMainActivity() {
        Intent intent = new Intent(PhoneLoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
