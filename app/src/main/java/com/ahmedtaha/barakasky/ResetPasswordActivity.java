package com.ahmedtaha.barakasky;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResetPasswordActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private Button resetPasswordEmailSedButton;
    private EditText ResetEmailInput;

    private FirebaseAuth mAuth;
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private LinearLayout mainRoot;
    private Typeface tf = null;
    private int mytoolbar_textsize = 0;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (la.equals("العربية")){
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidKufi-Regular.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        } else {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/aleo-bold.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_reset_password_normal);
        MobileAds.initialize(this, "ca-app-pub-1847297911542220/2987165732");
        AdView madview = (AdView) findViewById(R.id.reset_adview1);
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_reset_password_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mtoolbar = (Toolbar) findViewById(R.id.forget_password_toolbar);
        setSupportActionBar(mtoolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.reset_passwodrd_toolbar_title));
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            mytoolbar_textsize = 50;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            mytoolbar_textsize = 32;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            mytoolbar_textsize = 22;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            mytoolbar_textsize = 55;
        }
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        if(bar!=null){
            TextView tv = new TextView(getApplicationContext());
            /*RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lp);*/
            tv.setText(bar.getTitle());
            tv.setGravity(Gravity.BOTTOM);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(0,0,0,0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mytoolbar_textsize);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(tv);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);

        }

        //waitingBar = new SpotsDialog.Builder().setContext(ResetPasswordActivity.this).build();
        mainRoot = (LinearLayout) findViewById(R.id.main_resetpassword_root);
        resetPasswordEmailSedButton = (Button) findViewById(R.id.forget_password_send_button);
        ResetEmailInput = (EditText) findViewById(R.id.forget_password_email);

        mAuth = FirebaseAuth.getInstance();
        resetPasswordEmailSedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = ResetEmailInput.getText().toString();
                if(TextUtils.isEmpty(userEmail)){
                    //Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.reset_passwodrd_email_empty),Toast.LENGTH_LONG).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.reset_passwodrd_email_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.reset_passwodrd_email_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.reset_passwodrd_email_success),Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                            } else {
                                String message1 = task.getException().getMessage();
                                String message2 = getResources().getString(R.string.reset_passwodrd_email_error);
                                //Toast.makeText(ResetPasswordActivity.this,message2 + " " + message1,Toast.LENGTH_SHORT).show();
                                if (la.equals("العربية")){
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + " " + message1,
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                } else {
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + " " + message1,
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*String dis_language = Locale.getDefault().getDisplayLanguage();
        MyAppLanguage myAppLanguage = new MyAppLanguage();
        myAppLanguage.IsArabic(ResetPasswordActivity.this, dis_language);*/
    }
}
