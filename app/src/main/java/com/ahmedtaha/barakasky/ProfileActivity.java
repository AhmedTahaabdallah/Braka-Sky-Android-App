package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {
    private TextView userName, userProfileName, userStatus, userCountry, userCountry_lives, userGender, userRelation, userDob;
    private CircleImageView profileImage;
    private Button myPost, myFriends;

    private FirebaseAuth mAuth;
    private DatabaseReference profileUserRef, FriendsRef, PostsRef, UsersRef;
    private ValueEventListener profileUserRefValue, FriendsRefValue, PostsRefValue, UsersRefValue;
    String currentUserId;
    private String transfer = "false";
    int countFriends = 0, countPsts = 0;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;


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
        setContentView(R.layout.activity_profile_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_profile_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Friends");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        //waitingBar = new SpotsDialog.Builder().setContext(ProfileActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_book_root);

        userName = (TextView) findViewById(R.id.my_profile_user_name);
        userProfileName = (TextView) findViewById(R.id.my_profile_full_name);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userCountry = (TextView) findViewById(R.id.my_profile_country);
        userCountry_lives = (TextView) findViewById(R.id.my_profile_country_lives);
        userGender = (TextView) findViewById(R.id.my_profile_gender);
        userRelation = (TextView) findViewById(R.id.my_profile_relationship_status);
        userDob = (TextView) findViewById(R.id.my_profile_dob);
        profileImage = (CircleImageView) findViewById(R.id.my_profile_pic);
        myPost = (Button) findViewById(R.id.my_post_button);
        myFriends = (Button) findViewById(R.id.my_friends_button);

        myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToFriendsActivaty();
            }
        });
        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMyPostsActivaty();
            }
        });


    }

    private void setUserProfileInfo(){
        PostsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    countPsts = (int) dataSnapshot.getChildrenCount();
                    myPost.setText(Integer.toString(countPsts) + " " + getResources().getString(R.string.profile_profile_number_of_posts));
                } else {
                    myPost.setText(0 + " " + getResources().getString(R.string.profile_profile_number_of_posts));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        PostsRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff").addValueEventListener(PostsRefValue);
        FriendsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    countFriends = (int) dataSnapshot.getChildrenCount();
                    myFriends.setText(Integer.toString(countFriends) + " " + getResources().getString(R.string.profile_profile_number_of_friends));
                } else {
                    myFriends.setText(0 + " " + getResources().getString(R.string.profile_profile_number_of_friends));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendsRef.addValueEventListener(FriendsRefValue);
        profileUserRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileFullName = dataSnapshot.child("fullname").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myDOB = dataSnapshot.child("dob").getValue().toString();
                    String myCountry = dataSnapshot.child("country").getValue().toString();
                    String myCountry_lives = dataSnapshot.child("country_lives").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();
                    String myRelationShipStatus = dataSnapshot.child("relationshipstatus").getValue().toString();


                    Picasso.with(ProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(profileImage);
                    userName.setText("@" + myUserName);
                    userProfileName.setText(myProfileFullName);
                    userStatus.setText(myProfileStatus);

                    String la = Locale.getDefault().getDisplayLanguage();
                    String country_name = "", countryLives_name = "", gender_name = "", RelationShipStatus_name = "";
                    if (la.equals("العربية")){
                        String[] country_array = getResources().getStringArray(R.array.countries_array_ar);
                        if (myCountry.equals("none")) {
                            country_name = "لا يوجد";
                        } else {
                            country_name = country_array[Integer.parseInt(myCountry)];
                        }

                        String[] countryLives_array = getResources().getStringArray(R.array.countries_lives_array_ar);
                        if (myCountry_lives.equals("none")) {
                            countryLives_name = "لا يوجد";
                        } else {
                            countryLives_name = countryLives_array[Integer.parseInt(myCountry_lives)];
                        }

                        String[] gender_array = getResources().getStringArray(R.array.gender_ar);
                        if (myGender.equals("none")) {
                            gender_name = "لا يوجد";
                        } else {
                            gender_name = gender_array[Integer.parseInt(myGender)];
                        }

                        String[] rel_array = getResources().getStringArray(R.array.relationship_ar);
                        if (myRelationShipStatus.equals("none")) {
                            RelationShipStatus_name = "لا يوجد";
                        } else {
                            RelationShipStatus_name = rel_array[Integer.parseInt(myRelationShipStatus)];
                        }
                    } else {
                        String[] country_array = getResources().getStringArray(R.array.countries_array);
                        if (myCountry.equals("none")) {
                            country_name = "none";
                        } else {
                            country_name = country_array[Integer.parseInt(myCountry)];
                        }
                        String[] countryLives_array = getResources().getStringArray(R.array.countries_lives_array);
                        if (myCountry_lives.equals("none")) {
                            countryLives_name = "none";
                        } else {
                            countryLives_name = countryLives_array[Integer.parseInt(myCountry_lives)];
                        }

                        String[] gender_array = getResources().getStringArray(R.array.gender);
                        if (myGender.equals("none")) {
                            gender_name = "none";
                        } else {
                            gender_name = gender_array[Integer.parseInt(myGender)];
                        }

                        String[] rel_array = getResources().getStringArray(R.array.relationship);
                        if (myRelationShipStatus.equals("none")) {
                            RelationShipStatus_name = "none";
                        } else {
                            RelationShipStatus_name = rel_array[Integer.parseInt(myRelationShipStatus)];
                        }
                    }
                    userCountry.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_p) + country_name);
                    userCountry_lives.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_lives_p) + countryLives_name);
                    userGender.setText(getApplicationContext().getResources().getString(R.string.profile_profile_gender_p) + gender_name);
                    userRelation.setText(getApplicationContext().getResources().getString(R.string.profile_profile_relationship_status_p) + RelationShipStatus_name);
                    userDob.setText( getApplicationContext().getResources().getString(R.string.profile_profile_dob_p) + myDOB);
                    //userCountry.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_p) +myCountry);
                    //userGender.setText(getApplicationContext().getResources().getString(R.string.profile_profile_gender_p) +myGender);
                    //userRelation.setText(getApplicationContext().getResources().getString(R.string.profile_profile_relationship_status_p) +myRelationShipStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        profileUserRef.addValueEventListener(profileUserRefValue);
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
        UsersRef.child(currentUserId).updateChildren(hashMap);
    }

    private void sendUserToFriendsActivaty() {
        transfer = "true";
        Intent intent = new Intent(ProfileActivity.this,FriendsActivity.class);
        startActivity(intent);
    }
    private void sendUserToMyPostsActivaty() {
        transfer = "true";
        Intent intent = new Intent(ProfileActivity.this,MyPostsActivity.class);
        intent.putExtra("post_uid", currentUserId);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                setUserProfileInfo();
            } else {
                Toast.makeText(ProfileActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateUserState("Online");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUserState("Online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }

        if (profileUserRefValue != null && profileUserRef != null) {
            profileUserRef.removeEventListener(profileUserRefValue);
        }
        if (FriendsRefValue != null && FriendsRef != null) {
            FriendsRef.removeEventListener(FriendsRefValue);
        }
        if (PostsRefValue != null && PostsRef != null) {
            PostsRef.removeEventListener(PostsRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }
}
