package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText userEmail,userPassword,userPassword_confirm;
    //private ProgressDialog loadingBar;

    FirebaseAuth mAuth;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private Dialog mydialog;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        if (la.equals("العربية")){
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidKufi-Regular.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        } else {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_register_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_register_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();

        createAccountButton = (Button) findViewById(R.id.register_create_account);
        userEmail = (EditText) findViewById(R.id.register_email);
        userPassword = (EditText) findViewById(R.id.register_pass);
        userPassword_confirm = (EditText) findViewById(R.id.register_pass_confirm);
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog.Builder().setContext(RegisterActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_register_root);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String Email = userEmail.getText().toString();
        String Password = userPassword.getText().toString();
        String Confirm_Password = userPassword_confirm.getText().toString();
        if(TextUtils.isEmpty(Email)){
            //Toast.makeText(this,getResources().getString(R.string.register_toast_error_email),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
            //userEmail.setFocusable(true);
        } else if(TextUtils.isEmpty(Password)){
            //Toast.makeText(this,getResources().getString(R.string.register_toast_error_password),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
            //userPassword.setFocusable(true);
        } else if(TextUtils.isEmpty(Confirm_Password)){
            //Toast.makeText(this,getResources().getString(R.string.register_toast_error_password_confirm),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password_confirm),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password_confirm),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
            //userPassword_confirm.setFocusable(true);
        } else if(!Password.equals(Confirm_Password)){
            //Toast.makeText(this,getResources().getString(R.string.register_toast_error_password_not_match),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password_not_match),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.register_toast_error_password_not_match),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
            //userPassword_confirm.setFocusable(true);
        } else {
            /*loadingBar.setTitle(getResources().getString(R.string.register_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.register_loadingbar_message));
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);*/
            /*waitingBar.setMessage(getResources().getString(R.string.register_loadingbar_title));
            waitingBar.setCanceledOnTouchOutside(false);
            waitingBar.show();*/
            mydialog = new Dialog(RegisterActivity.this);
            mydialog.setTitle(getResources().getString(R.string.register_loadingbar_title));
            mydialog.setCancelable(false);
            mydialog.setContentView(R.layout.myprogressbr_layout);
            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mydialog.show();

            mAuth.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //sendUserToSetupActivaty();
                                //Toast.makeText(RegisterActivity.this,getResources().getString(R.string.register_toast_success),Toast.LENGTH_SHORT).show();
                                //waitingBar.dismiss();
                                mydialog.dismiss();
                                SendEmailVerificationMessage();
                                //loadingBar.dismiss();

                            } else {
                                String message1 = task.getException().getMessage();
                                String message2 = getResources().getString(R.string.register_toast_not_success);
                                //Toast.makeText(RegisterActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                //waitingBar.dismiss();
                                mydialog.dismiss();
                                if (la.equals("العربية")){
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                } else {
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                }
                                //loadingBar.dismiss();

                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendUserToMainActivity();
        } else {
            /*String dis_language = Locale.getDefault().getDisplayLanguage();
            MyAppLanguage myAppLanguage = new MyAppLanguage();
            myAppLanguage.IsArabic(RegisterActivity.this, dis_language);*/
        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToLoginActivaty() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void SendEmailVerificationMessage(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,getResources().getString(R.string.register_send_emaill_verification_message),Toast.LENGTH_SHORT).show();
                        sendUserToLoginActivaty();
                        mAuth.signOut();
                    } else {
                        String message1 = task.getException().getMessage();
                        String message2 = getResources().getString(R.string.register_toast_not_success);
                        Toast.makeText(RegisterActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            });
        }
    }
}
