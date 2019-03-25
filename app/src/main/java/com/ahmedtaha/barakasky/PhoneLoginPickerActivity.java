package com.ahmedtaha.barakasky;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneLoginPickerActivity extends AppCompatActivity {
    private static final String TAG = "PhoneAuth";

    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;

    private TextView statusText, errortxt;
    String number;
    private ProgressDialog loadingBar;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_picker_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_phone_login_picker_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        loadingBar = new ProgressDialog(PhoneLoginPickerActivity.this);
        statusText = (TextView) findViewById(R.id.statusText);
        errortxt = (TextView) findViewById(R.id.errorText);


        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneText);

        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);

        statusText.setText("Signed Out");

        fbAuth = FirebaseAuth.getInstance();
    }

    public void sendCode(View view) {
        String Phone_Number = phoneText.getText().toString();
        if (TextUtils.isEmpty(Phone_Number)){
            Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_phone_number_text_isempty), Toast.LENGTH_LONG).show();
        } else{
            loadingBar.setTitle(getResources().getString(R.string.phone_login_verification_code_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.phone_login_verification_code_loadingbar_message));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            number = ccp.getFullNumberWithPlus();

            setUpVerificatonCallbacks();

            //Toast.makeText(PhoneLoginPickerActivity.this, "Number : " + number, Toast.LENGTH_LONG).show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    PhoneLoginPickerActivity.this,               // Activity (for callback binding)
                    verificationCallbacks);
        }


    }

    private void setUpVerificatonCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {


                        statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);
                    }


                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        loadingBar.dismiss();
                        Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_phone_number_text_invaild), Toast.LENGTH_LONG).show();
                        //Toast.makeText(PhoneLoginPickerActivity.this, "err mes : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        //codeText.setText(e.getMessage());
                        errortxt.setText(e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;
                        loadingBar.dismiss();
                        Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_phone_number_text_sent), Toast.LENGTH_LONG).show();
                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);
                    }
                };
    }

    public void verifyCode(View view) {


        String code = codeText.getText().toString();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_phone_numberverfication_code_text_isempty), Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle(getResources().getString(R.string.phone_login_verify_verification_code_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.phone_login_verify_verification_code_loadingbar_message));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            PhoneAuthCredential credential =
                    PhoneAuthProvider.getCredential(phoneVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhoneLoginPickerActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_verify_success), Toast.LENGTH_LONG).show();
                            codeText.setText("");
                            statusText.setText("Signed In");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();

                            Intent intent = new Intent(PhoneLoginPickerActivity.this, MainActivity.class);
                            intent.putExtra("phone", phoneNumber);
                            startActivity(intent);
                            finish();

                        } else {
                            loadingBar.dismiss();
                            String message1 = task.getException().getMessage();
                            String message2 = getResources().getString(R.string.phone_login_toast_not_success);
                            Toast.makeText(PhoneLoginPickerActivity.this,message2 + " " + message1,Toast.LENGTH_SHORT).show();

                            /*if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }*/
                        }
                    }
                });
    }

    public void resendCode(View view) {
        String Phone_Number = phoneText.getText().toString();
        if (TextUtils.isEmpty(Phone_Number)){
            Toast.makeText(PhoneLoginPickerActivity.this, getResources().getString(R.string.phone_login_phone_number_text_isempty), Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle(getResources().getString(R.string.phone_login_verification_code_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.phone_login_verification_code_loadingbar_message));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            number = ccp.getFullNumberWithPlus();

            setUpVerificatonCallbacks();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number,
                    60,
                    TimeUnit.SECONDS,
                    PhoneLoginPickerActivity.this,
                    verificationCallbacks,
                    resendToken);
        }
    }



}
