package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Locale;

//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, PhoneButton, codingCoffeButton;
    private ImageView googlesigninbutton;
    private ImageView facbooksigninbutton;
    private EditText userName,UserPassword;
    private TextView needNewAccountLink, forgetPasswordLink;
    //private ProgressDialog loadingBar;
    //private AlertDialog waitingBar;
    private Dialog mydialog;

    private FirebaseAuth mAuth;
    private Boolean emilladdresChecker;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    private static final String TAG = "LoginActivity";
    private CallbackManager mCallbackManager;
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private String la= Locale.getDefault().getDisplayLanguage();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GetUserDateTime getUserDateTime = new GetUserDateTime(LoginActivity.this);
        //getUserDateTime.setMyAppDisplayLanguage();
        String la = Locale.getDefault().getDisplayLanguage();
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
        setContentView(R.layout.activity_login_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_login_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/


        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.login_button);
        PhoneButton = (Button) findViewById(R.id.login_phone_number_button);
        codingCoffeButton = (Button) findViewById(R.id.login_phone_number_button_coding_cofe);
        userName = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_pass);
        needNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        forgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        googlesigninbutton = (ImageView) findViewById(R.id.google_signin_button);
        mainRoot = (RelativeLayout) findViewById(R.id.login_parent_relatev);
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog(LoginActivity.this);
        //waitingBar = new SpotsDialog.Builder().setContext(LoginActivity.this).build();
        la = Locale.getDefault().getDisplayLanguage();

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });
        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });
        PhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToPhoneNumberLoginActivity();
            }
        });
        codingCoffeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToPhoneNumberLoginActivitycoding();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1041947753537-hr3jt9v85heh8dibts2f81qufuigh7o6.apps.googleusercontent.com")
                //.requestIdToken(getString(R.string.default_web_client_id))
                //.requestScopes(new Scope(Scopes.EMAIL))
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_google_sign_in_connection_faild),Toast.LENGTH_SHORT).show();
                        //Snackbar.make(mainRoot,getResources().getString(R.string.login_google_sign_in_connection_faild),Snackbar.LENGTH_SHORT).show();
                        String las = Locale.getDefault().getDisplayLanguage();
                        if (las.equals("العربية")){
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_connection_faild),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                        } else {
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_connection_faild),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                        }
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        googlesigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        facbooksigninbutton = (ImageView) findViewById(R.id.facebook_signin_button);
        //LoginButton loginButton = findViewById(R.id.fbook_signin_button);

        facbooksigninbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        ///Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        //Log.d(TAG, "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Log.d(TAG, "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
        /*loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });*/
        //userName.setText(BuildConfig.APPLICATION_ID);
        //loginButton.requestFocus();
        //String dis_language = Locale.getDefault().getDisplayLanguage();
        //Toast.makeText(LoginActivity.this, dis_language, Toast.LENGTH_LONG).show();
    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            /*loadingBar.setTitle(getResources().getString(R.string.login_google_sign_in_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.login_google_sign_in_loadingbar_message));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();*/
            //waitingBar.setTitle(getResources().getString(R.string.login_google_sign_in_loadingbar_title));
            /*waitingBar.setMessage(getResources().getString(R.string.login_google_sign_in_loadingbar_title));
            waitingBar.setCanceledOnTouchOutside(false);
            waitingBar.show();*/
            mydialog = new Dialog(LoginActivity.this);
            mydialog.setTitle(getResources().getString(R.string.login_google_sign_in_loadingbar_title));
            mydialog.setCancelable(false);
            mydialog.setContentView(R.layout.myprogressbr_layout);
            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mydialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                //Toast.makeText(this,getResources().getString(R.string.login_google_sign_in_result_success),Toast.LENGTH_SHORT).show();
                //Snackbar.make(mainRoot,getResources().getString(R.string.login_google_sign_in_result_success),Snackbar.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_result_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_result_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else {
                String ss = result.getStatus().toString();
                //Toast.makeText(this, ss,Toast.LENGTH_SHORT).show();
                //needNewAccountLink.setText(ss);
                //Snackbar.make(mainRoot,getResources().getString(R.string.login_google_sign_in_result_not_success),Snackbar.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_result_not_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_google_sign_in_result_not_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
                //loadingBar.dismiss();
                //waitingBar.dismiss();
                mydialog.dismiss();
            }
        }

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "signInWithCredential:success");
                            sendUserToMainActivity();
                            //loadingBar.dismiss();
                            //waitingBar.dismiss();
                            mydialog.dismiss();
                        } else {
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            String message1 = task.getException().getMessage();
                            String message2 = getResources().getString(R.string.login_google_sign_in_not_auth);
                            sendUserToLoginActivity();
                            //Toast.makeText(LoginActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                            //Snackbar.make(mainRoot,message2 + message1,Snackbar.LENGTH_SHORT).show();
                            //loadingBar.dismiss();
                            //waitingBar.dismiss();
                            mydialog.dismiss();
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            /*Toast.makeText(LoginActivity.this, "face book login success.",
                                    Toast.LENGTH_SHORT).show();*/
                            /*Snackbar.make(mainRoot, "face book login success.",
                                    Snackbar.LENGTH_SHORT).show();*/
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "face book login success.",
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "face book login success.",
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                            sendUserToMainActivity();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            /*Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();*/
                            /*Snackbar.make(mainRoot, "Authentication failed.",
                                    Snackbar.LENGTH_SHORT).show();*/
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "Authentication failed.",
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "Authentication failed.",
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                            //updateUI(null);
                            sendUserToLoginActivity();
                        }

                        // ...
                    }
                });
    }

    private void AllowingUserToLogin() {
        String Email = userName.getText().toString();
        String Password = UserPassword.getText().toString();

        if(TextUtils.isEmpty(Email)){
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }


            //userEmail.setFocusable(true);
        } else if(TextUtils.isEmpty(Password)){
            //Toast.makeText(this,getResources().getString(R.string.login_toast_error_password),Toast.LENGTH_SHORT).show();
            //Snackbar.make(mainRoot,getResources().getString(R.string.login_toast_error_password),Snackbar.LENGTH_SHORT).show();
            //userPassword.setFocusable(true);
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_password),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_password),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }

        } else {
            /*loadingBar.setTitle(getResources().getString(R.string.login_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.login_loadingbar_message));
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();*/
            //waitingBar.setTitle(getResources().getString(R.string.login_loadingbar_title));
            /*waitingBar.setMessage(getResources().getString(R.string.login_loadingbar_title));
            waitingBar.setCanceledOnTouchOutside(false);
            waitingBar.show();*/
            mydialog = new Dialog(LoginActivity.this);
            mydialog.setTitle(getResources().getString(R.string.posts_loadingbar_title));
            mydialog.setCancelable(false);
            mydialog.setContentView(R.layout.myprogressbr_layout);
            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mydialog.show();

            mAuth.signInWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //sendUserToMainActivity();
                                //Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_toast_success),Toast.LENGTH_SHORT).show();
                                VerifyEmailAddress();
                                //loadingBar.dismiss();
                                //waitingBar.dismiss();
                                mydialog.dismiss();
                            } else {
                                //String message1 = task.getException().getMessage();
                                //String message2 = getResources().getString(R.string.login_toast_not_success);
                                //Toast.makeText(LoginActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                //Snackbar.make(mainRoot,message2 + message1,Snackbar.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                mydialog.dismiss();
                                //waitingBar.dismiss();



                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_INVALID_CUSTOM_TOKEN":
                                        //Toast.makeText(LoginActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invald_customtoken),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invald_customtoken),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                        //Toast.makeText(LoginActivity.this, "my The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_customtoken_mismatch),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_customtoken_mismatch),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_INVALID_CREDENTIAL":
                                        //Toast.makeText(LoginActivity.this, "my The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_credential),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_credential),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        //Toast.makeText(LoginActivity.this, "my The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_email),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_email),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        userName.setError(getResources().getString(R.string.login_errorcode_invaild_email_error));
                                        userName.requestFocus();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        //Toast.makeText(LoginActivity.this, "my The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_wrong_password),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_wrong_password),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        UserPassword.setError(getResources().getString(R.string.login_errorcode_wrong_password_error));
                                        UserPassword.requestFocus();
                                        UserPassword.setText("");
                                        break;

                                    case "ERROR_USER_MISMATCH":
                                        //Toast.makeText(LoginActivity.this, "my The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_mismatch),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_mismatch),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_REQUIRES_RECENT_LOGIN":
                                        //Toast.makeText(LoginActivity.this, "my This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_requiers_recent_login),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_requiers_recent_login),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        //Toast.makeText(LoginActivity.this, "my An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_exist_acountwithcredentials),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_exist_acountwithcredentials),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        //Toast.makeText(LoginActivity.this, "my The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_email_inuse),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_email_inuse),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        userName.setError(getResources().getString(R.string.login_errorcode_email_inuse_error));
                                        userName.requestFocus();
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        //Toast.makeText(LoginActivity.this, "my This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_credential_inuse),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_credential_inuse),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        //Toast.makeText(LoginActivity.this, "my The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_disabled),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_disabled),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_USER_TOKEN_EXPIRED":
                                        //Toast.makeText(LoginActivity.this, "my The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_tokkenexpired),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_tokkenexpired),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        //Toast.makeText(LoginActivity.this, "my There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_notfound),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_user_notfound),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_INVALID_USER_TOKEN":
                                        //Toast.makeText(LoginActivity.this, "my The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_user_tokken),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_invaild_user_tokken),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_OPERATION_NOT_ALLOWED":
                                        //Toast.makeText(LoginActivity.this, "my This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_operation_notallowed),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_operation_notallowed),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        break;

                                    case "ERROR_WEAK_PASSWORD":
                                        //Toast.makeText(LoginActivity.this, "my The given password is invalid.", Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_weak_password),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_weak_password),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                        UserPassword.setError(getResources().getString(R.string.login_errorcode_weak_password_error));
                                        UserPassword.requestFocus();
                                        break;

                                        default:
                                            if (la.equals("العربية")){
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_default),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                            } else {
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_errorcode_default),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                            }
                                            break;

                                }
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
            myAppLanguage.IsArabic(LoginActivity.this, dis_language);*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Runtime.getRuntime().gc();
    }

    private void sendUserToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    private void SendToPhoneNumberLoginActivity() {
        //Intent intent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
        Intent intent = new Intent(LoginActivity.this,PhoneLoginPickerActivity.class);
        startActivity(intent);
    }
    private void SendToPhoneNumberLoginActivitycoding() {
        Intent intent = new Intent(LoginActivity.this,PhoneLoginActivity.class);
        //Intent intent = new Intent(LoginActivity.this,PhoneLoginPickerActivity.class);
        startActivity(intent);
    }
    private void sendUserToMainActivity() {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToLoginActivity() {
        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void VerifyEmailAddress(){
        FirebaseUser user = mAuth.getCurrentUser();
        emilladdresChecker = user.isEmailVerified();
        if(emilladdresChecker){
            sendUserToMainActivity();
        } else {
            //Toast.makeText(LoginActivity.this,getResources().getString(R.string.login_verify_emaill_account),Toast.LENGTH_LONG).show();
            //Snackbar.make(mainRoot,getResources().getString(R.string.login_verify_emaill_account),Snackbar.LENGTH_LONG).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_verify_emaill_account),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_verify_emaill_account),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
            mAuth.signOut();
        }
    }
}
