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
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SetupActivity extends AppCompatActivity {
    private Button saveSetup;
    private EditText userName,fullNme,countryName;
    private CircleImageView profileImage;
    //private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference useresRef, usereexistsRef;
    private ValueEventListener useresRefValue, usereexistsRefValue;
    private StorageReference UserProfileImageRef;


    String currentUserId;
    final static int Garrely_pick = 1;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private LinearLayout mainRoot;
    private Typeface tf = null;
    private Dialog mydialog;

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
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_setup_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_setup_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        usereexistsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        saveSetup = (Button) findViewById(R.id.setup_save_button);
        userName = (EditText) findViewById(R.id.setup_user_name);
        fullNme = (EditText) findViewById(R.id.setup_full_name);
        countryName = (EditText) findViewById(R.id.setup_country);
        profileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog.Builder().setContext(SetupActivity.this).build();
        mainRoot = (LinearLayout) findViewById(R.id.main_setup_root);

        saveSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Garrely_pick);
            }
        });


    }

    private void getUserProfileImage(){
        useresRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(SetupActivity.this).load(image).placeholder(R.drawable.profile).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        useresRef.addListenerForSingleValueEvent(useresRefValue);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Garrely_pick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                /*loadingBar.setTitle(getResources().getString(R.string.setup_profilrimage_loadingbar_title));
                loadingBar.setMessage(getResources().getString(R.string.setup_profilrimage_loadingbar_message));
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);*/

                /*waitingBar.setMessage(getResources().getString(R.string.setup_profilrimage_loadingbar_title));
                waitingBar.setCanceledOnTouchOutside(false);
                waitingBar.show();*/
                mydialog = new Dialog(SetupActivity.this);
                mydialog.setTitle(getResources().getString(R.string.setup_profilrimage_loadingbar_title));
                mydialog.setCancelable(false);
                mydialog.setContentView(R.layout.myprogressbr_layout);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mydialog.show();
                Uri Uriresult = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(Uriresult).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_success_image_store),Toast.LENGTH_SHORT).show();
                            Uri downUri = task.getResult();
                            useresRef.child("profileimage").setValue(downUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                    startActivity(selfIntent);
                                    if(task.isSuccessful()){
                                        Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_success_image_store_database),Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();
                                        //waitingBar.dismiss();
                                        mydialog.dismiss();
                                    } else {
                                        String message1 = task.getException().getMessage();
                                        String message2 = getResources().getString(R.string.setup_toast_not_success_profileimage_store);
                                        Toast.makeText(SetupActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();
                                        //waitingBar.dismiss();
                                        mydialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                });
            } else {
                Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_not_success_image_store),Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
                //waitingBar.dismiss();
                mydialog.dismiss();
            }
        }
    }

    private void SaveAccountSetupInformation() {
        final String username = userName.getText().toString().trim();
        final String fullname = fullNme.getText().toString().trim();
        //String country = countryName.getText().toString().trim();
        if(TextUtils.isEmpty(username)){
            //Toast.makeText(this,getResources().getString(R.string.setup_toast_error_username),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_username),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_username),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(TextUtils.isEmpty(fullname)){
            //Toast.makeText(this,getResources().getString(R.string.setup_toast_error_fullname),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(!TextUtils.isEmpty(fullname) && fullname.trim().length() > 30){
            //Toast.makeText(this,getResources().getString(R.string.setup_toast_error_fullname_sobig),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sobig),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sobig),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(!TextUtils.isEmpty(fullname) && fullname.trim().length() < 10){
            //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_error_fullname_sobig),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sosmall),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sosmall),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(!TextUtils.isEmpty(username) && username.trim().length() <= 3){
            //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_error_fullname_sobig),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_username_sosmall),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_username_sosmall),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else {
            usereexistsRefValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String noo = "false";
                        String ne = "";
                        for (DataSnapshot child: dataSnapshot.getChildren())
                        {
                            ne = child.child("username").getValue().toString();
                            if (ne.equals(username)){
                                noo = "true";
                            }
                        }
                        if (noo.equals("true")){
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_username_isexists),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_username_isexists),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                        } else {
                            useresRefValue = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(dataSnapshot.hasChild("profileimage")) {
                            /*loadingBar.setTitle(getResources().getString(R.string.setup_loadingbar_title));
                            loadingBar.setMessage(getResources().getString(R.string.setup_loadingbar_message));
                            loadingBar.show();
                            loadingBar.setCanceledOnTouchOutside(false);*/

                                            /*waitingBar.setMessage(getResources().getString(R.string.setup_loadingbar_title));
                                            waitingBar.setCanceledOnTouchOutside(false);
                                            waitingBar.show();*/
                                            mydialog = new Dialog(SetupActivity.this);
                                            mydialog.setTitle(getResources().getString(R.string.setup_loadingbar_title));
                                            mydialog.setCancelable(false);
                                            mydialog.setContentView(R.layout.myprogressbr_layout);
                                            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            mydialog.show();
                                            HashMap userMap = new HashMap();
                                            userMap.put("username",username);
                                            userMap.put("fullname",fullname);
                                            userMap.put("country","none");
                                            userMap.put("country_lives","none");
                                            userMap.put("status","Hi There, I'm Using Ahmed Taha Baraka Sky App!!!");
                                            userMap.put("gender","none");
                                            userMap.put("dob","none");
                                            userMap.put("language","English");
                                            userMap.put("relationshipstatus","none");
                                            useresRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){

                                                        //waitingBar.dismiss();
                                                        mydialog.dismiss();
                                                        Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_success),Toast.LENGTH_SHORT).show();
                                                        //loadingBar.dismiss();
                                                        sendUserToMainActivity();
                                                    } else {
                                                        String message1 = task.getException().getMessage();
                                                        String message2 = getResources().getString(R.string.setup_toast_not_success);
                                                        Toast.makeText(SetupActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                                        //loadingBar.dismiss();
                                                        //waitingBar.dismiss();
                                                        mydialog.dismiss();
                                                    }
                                                }
                                            });
                                        } else {
                                            //Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_not_slect_profileimage),Toast.LENGTH_SHORT).show();
                                            if (la.equals("العربية")){
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_not_slect_profileimage),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                            } else {
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_not_slect_profileimage),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            useresRef.addListenerForSingleValueEvent(useresRefValue);
                        }

                    } else {
                        useresRefValue = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.hasChild("profileimage")) {
                            /*loadingBar.setTitle(getResources().getString(R.string.setup_loadingbar_title));
                            loadingBar.setMessage(getResources().getString(R.string.setup_loadingbar_message));
                            loadingBar.show();
                            loadingBar.setCanceledOnTouchOutside(false);*/

                                        /*waitingBar.setMessage(getResources().getString(R.string.setup_loadingbar_title));
                                        waitingBar.setCanceledOnTouchOutside(false);
                                        waitingBar.show();*/
                                        mydialog = new Dialog(SetupActivity.this);
                                        mydialog.setTitle(getResources().getString(R.string.setup_loadingbar_title));
                                        mydialog.setCancelable(false);
                                        mydialog.setContentView(R.layout.myprogressbr_layout);
                                        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        mydialog.show();
                                        HashMap userMap = new HashMap();
                                        userMap.put("username",username);
                                        userMap.put("fullname",fullname);
                                        userMap.put("country","none");
                                        userMap.put("country_lives","none");
                                        userMap.put("status","Hi There, I'm Using Ahmed Taha Baraka Sky App!!!");
                                        userMap.put("gender","none");
                                        userMap.put("dob","none");
                                        userMap.put("language","English");
                                        userMap.put("relationshipstatus","none");
                                        useresRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){

                                                    //waitingBar.dismiss();
                                                    mydialog.dismiss();
                                                    Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_success),Toast.LENGTH_SHORT).show();
                                                    //loadingBar.dismiss();
                                                    sendUserToMainActivity();
                                                } else {
                                                    String message1 = task.getException().getMessage();
                                                    String message2 = getResources().getString(R.string.setup_toast_not_success);
                                                    Toast.makeText(SetupActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                                    //loadingBar.dismiss();
                                                    //waitingBar.dismiss();
                                                    mydialog.dismiss();
                                                }
                                            }
                                        });
                                    } else {
                                        //Toast.makeText(SetupActivity.this,getResources().getString(R.string.setup_toast_not_slect_profileimage),Toast.LENGTH_SHORT).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_not_slect_profileimage),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_not_slect_profileimage),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        useresRef.addListenerForSingleValueEvent(useresRefValue);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            usereexistsRef.orderByChild("username").startAt(username).endAt(username + "\uf8ff").addListenerForSingleValueEvent(usereexistsRefValue);


        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(SetupActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                /*String dis_language = Locale.getDefault().getDisplayLanguage();
                MyAppLanguage myAppLanguage = new MyAppLanguage();
                myAppLanguage.IsArabic(SetupActivity.this, dis_language);
                getUserProfileImage();*/
            } else {
                Toast.makeText(SetupActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (useresRefValue != null && useresRef != null) {
            useresRef.removeEventListener(useresRefValue);
        }
        Runtime.getRuntime().gc();
    }
}
