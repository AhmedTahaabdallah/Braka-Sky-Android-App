package com.ahmedtaha.barakasky;

import android.app.ActionBar;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private EditText userName, userProfileName, userStatus;
    private Spinner userCountry_sp, userCountry_lives_sp, userLanguage_sp, userGender_sp, userRelation_sp, select_year_sp;
    private CalendarView userDob_cv;
    private Button updateAccountSettingsButton, selectyearButton;
    private CircleImageView userProfileImage;
    //private ProgressDialog loadingBar;

    private DatabaseReference settingsuserRef, settingsuserRefs, UsersRef, usereexistsRef;
    private ValueEventListener settingsuserRefValue, UsersRefValue, usereexistsRefValue;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileImageRef;
    private String currentUserid, dateofb, transfer = "false";

    final static int Garrely_pick = 1;
    private List<String> selectedyearList;
    ArrayAdapter<String> CountryArrayAdapter, CountryLivesArrayAdapter, LanguageArrayAdapter, genderArrayAdapter, relationArrayAdapter, selectyearArrayAdapter;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private int startedDate = 1970;
    private int endDate;
    private String CountryLives;
    private int mytoolbar_textsize = 0;
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
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/aleo-bold.ttf")
                    .setFontAttrId(R.attr.fontPath).build());
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_settings_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_settings_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usereexistsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        settingsuserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);
        settingsuserRefs = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        mToolBar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings_toolbar_title));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        userName = (EditText) findViewById(R.id.settings_username);
        userProfileName = (EditText) findViewById(R.id.settings_profile_fullname);
        userStatus = (EditText) findViewById(R.id.settings_status);
        //userCountry = (EditText) findViewById(R.id.settings_profile_country);
        //userGender = (EditText) findViewById(R.id.settings_profile_gender);
        //userRelation = (EditText) findViewById(R.id.settings_profile_relationship_status);
        userCountry_sp = (Spinner) findViewById(R.id.settings_profile_country_sp);
        userCountry_lives_sp = (Spinner) findViewById(R.id.settings_profile_country_lives_sp);
        userGender_sp = (Spinner) findViewById(R.id.settings_profile_gender_sp);
        userRelation_sp = (Spinner) findViewById(R.id.settings_profile_relationship_status_sp);
        userLanguage_sp = (Spinner) findViewById(R.id.settings_profile_language_sp);
        select_year_sp = (Spinner) findViewById(R.id.settings_profile_select_year_sp);
        selectedyearList = new ArrayList<String>();
        //userDob = (EditText) findViewById(R.id.settings_profile_dob);
        userDob_cv = (CalendarView) findViewById(R.id.settings_profile_dob_cv);
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog.Builder().setContext(SettingsActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_settings_root);

        updateAccountSettingsButton = (Button) findViewById(R.id.update_account_settings_button);
        selectyearButton = (Button) findViewById(R.id.go_to_selected_year_button);
        userProfileImage = (CircleImageView) findViewById(R.id.settings_profile_image);

        CountryLives = getIntent().getExtras().get("current_user_country_Lives").toString();

        String la = Locale.getDefault().getDisplayLanguage();
        if (la.equals("العربية")){
            CountryArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_array_ar));
            CountryLivesArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_lives_array_ar));
            genderArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.gender_ar));
            relationArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.relationship_ar));
        } else {
            CountryArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_array));
            CountryLivesArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_lives_array));
            genderArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.gender));
            relationArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.relationship));
        }
        //CountryArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_array));
        CountryArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        userCountry_sp.setAdapter(CountryArrayAdapter);

        //CountryLivesArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.countries_lives_array));
        CountryLivesArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        userCountry_lives_sp.setAdapter(CountryLivesArrayAdapter);

        LanguageArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.user_language));
        LanguageArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        userLanguage_sp.setAdapter(LanguageArrayAdapter);

        //genderArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.gender));
        genderArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        userGender_sp.setAdapter(genderArrayAdapter);

        //relationArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getResources().getStringArray(R.array.relationship));
        relationArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        userRelation_sp.setAdapter(relationArrayAdapter);



        userDob_cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateofb = dayOfMonth + "/" + (month + 1)+ "/" + year;
                //Toast.makeText(SettingsActivity.this, dateofb, Toast.LENGTH_LONG).show();
            }


        });

        updateAccountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccountInfo();
                //userCountry_lives_sp.setSelection(0);
                //String myPos = Integer.toString(userCountry_lives_sp.getSelectedItemPosition());
                //Toast.makeText(SettingsActivity.this, myPos, Toast.LENGTH_LONG).show();

                //Toast.makeText(SettingsActivity.this, se, Toast.LENGTH_LONG).show();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer = "true";
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Garrely_pick);
            }
        });

        /// fill selecte year spiner with yers
        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calFororderDate.add(Calendar.YEAR, -5);
        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyy/MM/dd");
        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String da = currentorderDate.format(calFororderDate.getTime());
        //Toast.makeText(SettingsActivity.this, "date : " + da, Toast.LENGTH_LONG).show();
        GetUserDateTime getUserDateTime = new GetUserDateTime(SettingsActivity.this);
        String date_value = getUserDateTime.getDateToShow(da, CountryLives, "yyyy/MM/dd", "yyyy");
        //Toast.makeText(SettingsActivity.this, "year : " + date_value, Toast.LENGTH_LONG).show();
        endDate =Integer.parseInt(date_value);
        for (int i = endDate; startedDate <= i; i--){
            selectedyearList.add(Integer.toString(i));
        }
        //Toast.makeText(SettingsActivity.this, "start date List : " + selectedyearList.get(selectedyearList.size() - 1), Toast.LENGTH_LONG).show();
        //Toast.makeText(SettingsActivity.this, "Ended date List : " + selectedyearList.get(0), Toast.LENGTH_LONG).show();
        selectyearArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, selectedyearList);
        selectyearArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        select_year_sp.setAdapter(selectyearArrayAdapter);


        selectyearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String selectedyer = select_year_sp.getSelectedItem().toString();
                    //Toast.makeText(SettingsActivity.this, "year : " + selectedyer, Toast.LENGTH_LONG).show();
                    String selectedDate = "01/01/" + selectedyer;
                    //Toast.makeText(SettingsActivity.this, "date : " + selectedDate, Toast.LENGTH_LONG).show();
                    userDob_cv.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserInfo(){
        settingsuserRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileFullName = dataSnapshot.child("fullname").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myDOB = dataSnapshot.child("dob").getValue().toString();
                    String myCountry = dataSnapshot.child("country").getValue().toString();
                    String myCountryLives = dataSnapshot.child("country_lives").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();
                    String myRelationShipStatus = dataSnapshot.child("relationshipstatus").getValue().toString();
                    //String mylanguage = dataSnapshot.child("language").getValue().toString();

                    Picasso.with(SettingsActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText(myUserName);
                    userProfileName.setText(myProfileFullName);
                    userStatus.setText(myProfileStatus);
                    // userDob.setText(myDOB);

                    /*userCountry.setText(myCountry);
                    userGender.setText(myGender);
                    userRelation.setText(myRelationShipStatus);*/
                    if (!myCountry.equals("none")){
                        //ArrayAdapter myAdap = (ArrayAdapter) userCountry_sp.getAdapter(); //cast to an ArrayAdapter
                        //int spinnerPosition = CountryArrayAdapter.getPosition(myCountry);
                        userCountry_sp.setSelection(Integer.parseInt(myCountry));
                    }
                    if (!myCountryLives.equals("none")){
                        //ArrayAdapter myAdap = (ArrayAdapter) userCountry_sp.getAdapter(); //cast to an ArrayAdapter
                        //int spinnerPosition = CountryLivesArrayAdapter.getPosition(myCountryLives);
                        userCountry_lives_sp.setSelection(Integer.parseInt(myCountryLives));
                    }
                    //userCountry_lives_sp.setSelection(8);
                    if (!myGender.equals("none")){
                        //ArrayAdapter myAdap = (ArrayAdapter) userGender_sp.getAdapter(); //cast to an ArrayAdapter
                        //int spinnerPosition = genderArrayAdapter.getPosition(myGender);
                        userGender_sp.setSelection(Integer.parseInt(myGender));
                    }
                    if (!myRelationShipStatus.equals("none")){
                        //ArrayAdapter myAdap = (ArrayAdapter) userRelation_sp.getAdapter(); //cast to an ArrayAdapter
                        //int spinnerPosition = relationArrayAdapter.getPosition(myRelationShipStatus);
                        userRelation_sp.setSelection(Integer.parseInt(myRelationShipStatus));
                    }

                    /*int languagePosition = LanguageArrayAdapter.getPosition(mylanguage);
                    userLanguage_sp.setSelection(languagePosition);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        settingsuserRef.addValueEventListener(settingsuserRefValue);



        settingsuserRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String myDOBs = dataSnapshot.child("dob").getValue().toString();
                    if (!myDOBs.equals("none")){
                        try {
                            final String selectedDate = myDOBs;
                            dateofb = myDOBs;
                            userDob_cv.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            final String selectedDate = "11/05/1991";
                            dateofb = selectedDate;
                            userDob_cv.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUserState(String state){
        String savecurrentdate,savecurrentdate_order,savecurrenttime,savecurrenttimesecond,date_time;

        Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd-yyyy", Locale.ENGLISH);
        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate = currentDate.format(calForDate.getTime());

        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate_order.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

        Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTime.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrenttime = currentTime.format(calForTime.getTime());

        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTimesecond.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

        date_time = savecurrentdate_order + " " + savecurrenttimesecond;
        HashMap hashMap = new HashMap();
        hashMap.put("state_date", savecurrentdate);
        hashMap.put("state_time", savecurrenttime);
        hashMap.put("state_type", state);
        hashMap.put("state_date_time", date_time);
        UsersRef.child(currentUserid).updateChildren(hashMap);
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
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();*/

                /*waitingBar.setMessage(getResources().getString(R.string.setup_profilrimage_loadingbar_title));
                waitingBar.setCanceledOnTouchOutside(false);
                waitingBar.show();*/
                mydialog = new Dialog(SettingsActivity.this);
                mydialog.setTitle(getResources().getString(R.string.setup_profilrimage_loadingbar_title));
                mydialog.setCancelable(false);
                mydialog.setContentView(R.layout.myprogressbr_layout);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mydialog.show();
                Uri Uriresult = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserid + ".jpg");
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
                            Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_success_image_store),Toast.LENGTH_SHORT).show();
                            Uri downUri = task.getResult();
                            settingsuserRef.child("profileimage").setValue(downUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    transfer = "true";
                                    Intent selfIntent = new Intent(SettingsActivity.this,SettingsActivity.class);
                                    startActivity(selfIntent);
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_success_image_store_database),Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();
                                        //waitingBar.dismiss();
                                        mydialog.dismiss();
                                    } else {
                                        String message1 = task.getException().getMessage();
                                        String message2 = getResources().getString(R.string.setup_toast_not_success_profileimage_store);
                                        Toast.makeText(SettingsActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_not_success_image_store),Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
                //waitingBar.dismiss();
                mydialog.dismiss();
            }
        }
    }

    private void validateAccountInfo(){
        try {
            String username = userName.getText().toString().trim();
            String profilename = userProfileName.getText().toString().trim();
            String status = userStatus.getText().toString().trim();

            //String country = userCountry_sp.getSelectedItem().toString();
            String country = Integer.toString(userCountry_sp.getSelectedItemPosition());
            //String country_lives = userCountry_lives_sp.getSelectedItem().toString();
            String country_lives = Integer.toString(userCountry_lives_sp.getSelectedItemPosition());
            //String gender = userGender_sp.getSelectedItem().toString();
            String gender = Integer.toString(userGender_sp.getSelectedItemPosition());
            //String relation = userRelation_sp.getSelectedItem().toString();
            String relation = Integer.toString(userRelation_sp.getSelectedItemPosition());
            //String lan = userLanguage_sp.getSelectedItem().toString();
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //String dob = sdf.format(new Date(userDob_cv.getDate()));
            String dob = dateofb;

            Calendar calFororderDates = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            calFororderDates.add(Calendar.YEAR, -5);
            SimpleDateFormat currentorderDate = new SimpleDateFormat("dd/MM/yyyy");
            currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            String da = currentorderDate.format(calFororderDates.getTime());
            GetUserDateTime getUserDateTime = new GetUserDateTime(SettingsActivity.this);
            String ended_bd = getUserDateTime.getDateToShow(da, CountryLives, "dd/MM/yyyy", "dd/MM/yyyy");
            //Toast.makeText(SettingsActivity.this,ended_bd,Toast.LENGTH_SHORT).show();
            //Toast.makeText(SettingsActivity.this,dob,Toast.LENGTH_SHORT).show();
            String started_bd = "01/01/1970";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date endeddate = dateFormat.parse(ended_bd);
            Date starteddate = dateFormat.parse(started_bd);
            Date dateofBirth = dateFormat.parse(dob);
            if(TextUtils.isEmpty(username)){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_username_empty),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_username_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_username_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else if(TextUtils.isEmpty(profilename)){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_fullname_empty),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_fullname_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_fullname_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else if(!TextUtils.isEmpty(profilename) && profilename.trim().length() > 30){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.setup_toast_error_fullname_sobig),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sobig),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_toast_error_fullname_sobig),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else if(!TextUtils.isEmpty(profilename) && profilename.trim().length() < 10){
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
            } else if(TextUtils.isEmpty(status)){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_status_empty),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_status_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_status_empty),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } /*else if(TextUtils.isEmpty(dob)){
            //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_dob_empty),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(TextUtils.isEmpty(country)){
            Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_country_empty),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(TextUtils.isEmpty(gender)){
            Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_gender_empty),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(TextUtils.isEmpty(relation)){
            Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_relation_empty),Toast.LENGTH_SHORT).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.login_toast_error_email),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        }*/
            else if(dateofBirth.after(endeddate)){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_status_empty),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_date_of_birth_big) + " " + ended_bd,
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_date_of_birth_big) + " " + ended_bd,
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else if(dateofBirth.before(starteddate)){
                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_status_empty),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_date_of_birth_small) + " " + started_bd,
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_date_of_birth_small) + " " + started_bd,
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            } else {
                updateAccountInfo(username, profilename, status, dob, country, country_lives, gender, relation);
            }
        } catch (Exception ex){

        }


    }

    private void updateAccountInfo(final String username, final String profilename, final String status, final String dob, final String country, final String country_lives, final String gender, final String relation) {
        usereexistsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //String usid = Integer.toString((int)dataSnapshot.getChildrenCount());
                    String myParentNode = "", noo = "";
                    String ne = "";

                    for (DataSnapshot child: dataSnapshot.getChildren())
                    {
                        ne = child.child("username").getValue().toString();
                        String smyParentNode = child.getKey().toString();
                        if (smyParentNode.equals(currentUserid) && ne.equals(username)){
                            myParentNode = child.getKey().toString();
                            } else if(!smyParentNode.equals(currentUserid) && !ne.equals(username)) {
                            noo = "true";
                        } else if(!smyParentNode.equals(currentUserid) && ne.equals(username)) {
                            noo = "false";
                        }
                    }
                    if (currentUserid.equals(myParentNode) || noo.equals("true")){
                        HashMap userMap = new HashMap();
                        userMap.put("username", username);
                        userMap.put("fullname", profilename);
                        userMap.put("status", status);
                        userMap.put("dob", dob);
                        userMap.put("country", country);
                        userMap.put("country_lives", country_lives);
                        //userMap.put("language", lan);
                        userMap.put("gender", gender);
                        userMap.put("relationshipstatus", relation);

                        settingsuserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_account_update_success),Toast.LENGTH_SHORT).show();
                                    sendUserToMainActivity();
                                } else {
                                    //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_account_update_not_success),Toast.LENGTH_SHORT).show();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_account_update_not_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_account_update_not_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            }
                        });
                    } else {
                        if (la.equals("العربية")){
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_username_isexists),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                        } else {
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.setup_username_isexists),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                        }
                    }
                } else {
                    HashMap userMap = new HashMap();
                    userMap.put("username", username);
                    userMap.put("fullname", profilename);
                    userMap.put("status", status);
                    userMap.put("dob", dob);
                    userMap.put("country", country);
                    userMap.put("country_lives", country_lives);
                    //userMap.put("language", lan);
                    userMap.put("gender", gender);
                    userMap.put("relationshipstatus", relation);

                    settingsuserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_account_update_success),Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity();
                            } else {
                                //Toast.makeText(SettingsActivity.this,getResources().getString(R.string.settings_account_update_not_success),Toast.LENGTH_SHORT).show();
                                if (la.equals("العربية")){
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_account_update_not_success),
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                } else {
                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.settings_account_update_not_success),
                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usereexistsRef.orderByChild("username").startAt(username).addListenerForSingleValueEvent(usereexistsRefValue);

    }

    private void sendUserToMainActivity() {
        transfer = "true";
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }

    @Override
    public boolean onSupportNavigateUp() {
        transfer = "true";
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                getUserInfo();
            } else {
                Toast.makeText(SettingsActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUserState("Online");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateUserState("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }

        if (settingsuserRefValue != null && settingsuserRef != null) {
            settingsuserRef.removeEventListener(settingsuserRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }
}
