package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class PersonProfileActivity extends AppCompatActivity {
    private TextView userName, userProfileName, userStatus, userCountry, userCountry_Lives, userGender, userRelation, userDob, sendedText, recivededText;
    private CircleImageView profileImage;
    private Button sendFriendRequestButton, declineFriendRequestButton, gotoPostsButton, gotoSendMessageButton, bookPersonButton;

    private FirebaseAuth mAuth;
    private DatabaseReference useresRef, FriendRequestRef, FriendRequestCountRef, UsersRef, FriendsRef, PostsRef, BookRef, BookButtonRef, FriendRequestCountReciveRef, FriendRequestCountSenderRef;
    private ValueEventListener FriendRequestRefValue, FriendRequestCountRefValue, UsersRefValue, FriendsRefValue, PostsRefValue, BookRefValue, BookButtonRefValue, FriendRequestCountReciveRefValue, FriendRequestCountSenderRefValue;
    private String senderUserId, receiverUserId, current_state, savecurrentdate, savecurrentdate_order, savecurrenttimesecond;
    private String transfer = "false";
    private String out_current_state = "";

    private String request_type_reciver;
    private  int countPsts = 0;
    private Boolean BookState = false;
    String current_user_country_Lives;

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
        setContentView(R.layout.activity_person_profile_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_person_profile_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        userName = (TextView) findViewById(R.id.person_profile_user_name);
        userProfileName = (TextView) findViewById(R.id.person_profile_full_name);
        userStatus = (TextView) findViewById(R.id.person_profile_status);
        userCountry = (TextView) findViewById(R.id.person_profile_country);
        userCountry_Lives = (TextView) findViewById(R.id.person_profile_country_lives);
        userGender = (TextView) findViewById(R.id.person_profile_gender);
        userRelation = (TextView) findViewById(R.id.person_profile_relationship_status);
        userDob = (TextView) findViewById(R.id.person_profile_dob);
        sendedText = (TextView) findViewById(R.id.count_of_sended_friends_requests_text);
        recivededText = (TextView) findViewById(R.id.count_of_reciveded_friends_requests_text);
        profileImage = (CircleImageView) findViewById(R.id.person_profile_pic);
        sendFriendRequestButton = (Button) findViewById(R.id.person_send_friend_request_button);
        declineFriendRequestButton = (Button) findViewById(R.id.person_decline_friend_request_button);
        gotoPostsButton = (Button) findViewById(R.id.person_user_post_button);
        gotoSendMessageButton = (Button) findViewById(R.id.person_user_send_messge_button);
        bookPersonButton = (Button) findViewById(R.id.person_book_friend_button);
        //waitingBar = new SpotsDialog.Builder().setContext(PersonProfileActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_book_root);

        current_state = "not_friends";

        mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();
        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        BookRef = FirebaseDatabase.getInstance().getReference().child("Users");
        BookButtonRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountReciveRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountSenderRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");



        PostsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    countPsts = (int) dataSnapshot.getChildrenCount();
                    gotoPostsButton.setText(Integer.toString(countPsts) + " " + getResources().getString(R.string.profile_profile_number_of_posts));
                } else {
                    gotoPostsButton.setText(0 + " " + getResources().getString(R.string.profile_profile_number_of_posts));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        PostsRef.orderByChild("uid").startAt(receiverUserId).endAt(receiverUserId + "\uf8ff").addValueEventListener(PostsRefValue);
        gotoPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer = "true";
                Intent intent = new Intent(PersonProfileActivity.this,MyPostsActivity.class);
                intent.putExtra("post_uid", receiverUserId);
                startActivity(intent);
            }
        });
        gotoSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer = "true";
                Intent intent = new Intent(PersonProfileActivity.this, ChatActivity.class);
                intent.putExtra("visit_user_id", receiverUserId);
                intent.putExtra("username", userName.getText().toString());
                intent.putExtra("country_lives", current_user_country_Lives);
                startActivity(intent);
            }
        });
        declineFriendRequestButton.setVisibility(View.GONE);
        declineFriendRequestButton.setEnabled(false);
        //MaintananceofButtons();
        //MaintananceofButtons();
        if(!senderUserId.equals(receiverUserId)){
            sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //sendFriendRequestButton.setEnabled(false);
                    if(current_state.equals("not_friends")){
                        //sendFriendRequestButton.setEnabled(true);
                        FriendRequestCountSenderRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("FriendRequestsCount")){
                                    if (dataSnapshot.child("FriendRequestsCount").hasChild("sent_count")){
                                        int sen = (int) dataSnapshot.child("FriendRequestsCount").child("sent_count").getChildrenCount();
                                        if (sen < 21){
                                            FriendRequestCountReciveRef.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.hasChild("FriendRequestsCount")){
                                                        if (dataSnapshot.child("FriendRequestsCount").hasChild("recived_count")){
                                                            int rec = (int) dataSnapshot.child("FriendRequestsCount").child("recived_count").getChildrenCount();
                                                            if (rec < 21) {
                                                                sendFriendRequestToPerson();
                                                                //Toast.makeText(PersonProfileActivity.this, "here", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                Toast.makeText(PersonProfileActivity.this, getResources().getString(R.string.person_profile_recived_cross), Toast.LENGTH_LONG).show();
                                                                //current_state = "not_friends";
                                                                sendFriendRequestButton.setEnabled(true);
                                                            }
                                                         }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        } else {
                                            //Toast.makeText(PersonProfileActivity.this, getResources().getString(R.string.person_profile_sended_cross), Toast.LENGTH_LONG).show();
                                            if (la.equals("العربية")){
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_sended_cross),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                            } else {
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_sended_cross),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                            }
                                            //current_state = "not_friends";
                                            sendFriendRequestButton.setEnabled(true);
                                        }
                                        //sendedText.setText(Integer.toString(sen));
                                        //Toast.makeText(PersonProfileActivity.this, "sent : " + sendedText.getText().toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        //sendedText.setText(Integer.toString(0));
                                        FriendRequestCountReciveRef.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("FriendRequestsCount")){
                                                    if (dataSnapshot.child("FriendRequestsCount").hasChild("recived_count")){
                                                        int rec = (int) dataSnapshot.child("FriendRequestsCount").child("recived_count").getChildrenCount();
                                                        if (rec < 21) {
                                                            sendFriendRequestToPerson();
                                                            //Toast.makeText(PersonProfileActivity.this, "here", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            //Toast.makeText(PersonProfileActivity.this, getResources().getString(R.string.person_profile_recived_cross), Toast.LENGTH_LONG).show();
                                                            if (la.equals("العربية")){
                                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_recived_cross),
                                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                            } else {
                                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_recived_cross),
                                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                            }
                                                            //current_state = "not_friends";
                                                            sendFriendRequestButton.setEnabled(true);
                                                        }
                                                    } else {
                                                        sendFriendRequestToPerson();
                                                    }
                                                } else {
                                                    sendFriendRequestToPerson();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                } else {
                                    //sendedText.setText(Integer.toString(0));
                                    FriendRequestCountReciveRef.child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("FriendRequestsCount")){
                                                if (dataSnapshot.child("FriendRequestsCount").hasChild("recived_count")){
                                                    int rec = (int) dataSnapshot.child("FriendRequestsCount").child("recived_count").getChildrenCount();
                                                    if (rec < 21) {
                                                        sendFriendRequestToPerson();
                                                        //Toast.makeText(PersonProfileActivity.this, "here", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        //Toast.makeText(PersonProfileActivity.this, getResources().getString(R.string.person_profile_recived_cross), Toast.LENGTH_LONG).show();
                                                        if (la.equals("العربية")){
                                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_recived_cross),
                                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                        } else {
                                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.person_profile_recived_cross),
                                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                        }
                                                        //current_state = "not_friends";
                                                        sendFriendRequestButton.setEnabled(true);
                                                    }
                                                } else {
                                                    sendFriendRequestToPerson();
                                                }
                                            } else {
                                                sendFriendRequestToPerson();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    if(current_state.equals("request_sent")){
                        cancleFriendRequest();
                        FriendRequestCountRef.child(senderUserId).child("FriendRequestsCount").child("sent_count").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FriendRequestCountRef.child(receiverUserId).child("FriendRequestsCount").child("recived_count").child(senderUserId).removeValue();
                            }
                        });
                    }
                    if (current_state.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(current_state.equals("friends")){
                        UnFriendExistingFriend();
                    }
                    //Toast.makeText(PersonProfileActivity.this, "st : " + current_state, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            declineFriendRequestButton.setVisibility(View.GONE);
            sendFriendRequestButton.setVisibility(View.GONE);
        }
        /*sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!senderUserId.equals(receiverUserId)){
                    sendFriendRequestButton.setEnabled(false);
                    if(current_state.equals("friends")){
                        UnFriendExistingFriend();
                    }
                    if (current_state.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(current_state.equals("request_sent")){
                        cancleFriendRequest();
                        //Toast.makeText(PersonProfileActivity.this, "bt cl current State : " + current_state, Toast.LENGTH_LONG).show();
                    }
                    if(current_state.equals("not_friends")){
                        sendFriendRequestToPerson();
                        //Toast.makeText(PersonProfileActivity.this, "bt cl current State : " + current_state, Toast.LENGTH_LONG).show();
                    }

                } else {
                    declineFriendRequestButton.setVisibility(View.GONE);
                    sendFriendRequestButton.setVisibility(View.GONE);
                }
            }
        });*/
        /*Toast.makeText(PersonProfileActivity.this, "State after method: " + current_state, Toast.LENGTH_LONG).show();
        if(!senderUserId.equals(receiverUserId)){
            sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(out_current_state.equals("")){
                        out_current_state = current_state;
                    }
                    sendFriendRequestButton.setEnabled(false);
                    Toast.makeText(PersonProfileActivity.this, "button clicked : " + current_state, Toast.LENGTH_LONG).show();
                    if(out_current_state.equals("request_sent")){
                        cancleFriendRequest();
                    }

                    if (out_current_state.equals("request_received")){
                        AcceptFriendRequest();
                    }
                    if(out_current_state.equals("friends")){
                        UnFriendExistingFriend();
                    }
                    if(out_current_state.equals("not_friends")){
                        //Toast.makeText(PersonProfileActivity.this, "State before send: " + current_state, Toast.LENGTH_LONG).show();
                        sendFriendRequestToPerson();

                        //Toast.makeText(PersonProfileActivity.this, "State after send: " + current_state, Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            declineFriendRequestButton.setVisibility(View.GONE);
            sendFriendRequestButton.setVisibility(View.GONE);
        }*/


        bookPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (BookState.equals(true)){
                            BookButtonRef.child(senderUserId).child("BookPersons").child(receiverUserId).removeValue();
                           // bookPersonButton.setText(getResources().getString(R.string.person_book_friend_button));
                            //bookPersonButton.setBackgroundColor(getResources().getColor(R.color.send_friend_request_color));
                        } else {

                            Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForDate_order.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                            savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

                            Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForTimesecond.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                            currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                            savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

                            String date_time = savecurrentdate_order + savecurrenttimesecond;
                            Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                            currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String order_date = currentorderDate.format(calFororderDate.getTime());

                            HashMap bookMap = new HashMap();
                            bookMap.put("date_time",date_time);
                            bookMap.put("order_date",order_date);
                            BookButtonRef.child(senderUserId).child("BookPersons").child(receiverUserId).updateChildren(bookMap);
                            //BookButtonRef.child(senderUserId).child("BookPersons").child(receiverUserId).child("date_time").setValue(date_time);
                            //bookPersonButton.setText(getResources().getString(R.string.person_unbook_friend_button));
                            //bookPersonButton.setBackgroundColor(getResources().getColor(R.color.cancle_friend_request_color));
                        }
            }
        });
    }

    private void getCurrentUserCountryLives(){
        useresRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    current_user_country_Lives = dataSnapshot.child("country_lives").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getUserInfo(){
        UsersRefValue = new ValueEventListener() {
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

                    Picasso.with(PersonProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(profileImage);

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

                    userName.setText("@" + myUserName);
                    userProfileName.setText(myProfileFullName);
                    userStatus.setText(myProfileStatus);
                    userDob.setText( getApplicationContext().getResources().getString(R.string.profile_profile_dob_p) + myDOB);
                    //userCountry.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_p) +myCountry);
                    //userGender.setText(getApplicationContext().getResources().getString(R.string.profile_profile_gender_p) +myGender);
                    //userRelation.setText(getApplicationContext().getResources().getString(R.string.profile_profile_relationship_status_p) +myRelationShipStatus);
                    userCountry.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_p) + country_name);
                    userCountry_Lives.setText(getApplicationContext().getResources().getString(R.string.profile_profile_country_lives_p) + countryLives_name);
                    userGender.setText(getApplicationContext().getResources().getString(R.string.profile_profile_gender_p) + gender_name);
                    userRelation.setText(getApplicationContext().getResources().getString(R.string.profile_profile_relationship_status_p) + RelationShipStatus_name);

                    MaintananceofButtons();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        UsersRef.child(receiverUserId).addValueEventListener(UsersRefValue);
    }
    private void getUserBook(){
        BookRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild(receiverUserId)){
                        bookPersonButton.setText(getResources().getString(R.string.person_unbook_friend_button));
                        bookPersonButton.setBackgroundColor(getResources().getColor(R.color.cancle_friend_request_color));
                        BookState = true;
                    } else {
                        bookPersonButton.setText(getResources().getString(R.string.person_book_friend_button));
                        bookPersonButton.setBackgroundColor(getResources().getColor(R.color.send_friend_request_color));
                        BookState = false;
                    }
                } else {
                    bookPersonButton.setText(getResources().getString(R.string.person_book_friend_button));
                    bookPersonButton.setBackgroundColor(getResources().getColor(R.color.send_friend_request_color));
                    BookState = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        BookRef.child(senderUserId).child("BookPersons").addValueEventListener(BookRefValue);
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
        UsersRef.child(senderUserId).updateChildren(hashMap);
    }

    private void UnFriendExistingFriend() {
        FriendsRef.child(senderUserId).child("Friends").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendRequestRef.child(receiverUserId).child("Friends").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "not_friends";
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
        //MaintananceofButtons();
    }

    private void AcceptFriendRequest() {
        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate_order.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTimesecond.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

        Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate = currentDate.format(calForDate.getTime());

        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String order_date = currentorderDate.format(calFororderDate.getTime());

        String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
        final HashMap hashMap = new HashMap();
        hashMap.put("date", savecurrentdate);
        hashMap.put("date_time", date_time);
        hashMap.put("order_date", order_date);
        FriendsRef.child(senderUserId).child("Friends").child(receiverUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendsRef.child(receiverUserId).child("Friends").child(senderUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FriendRequestRef.child(senderUserId).child("FriendRequests").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FriendRequestRef.child(receiverUserId).child("FriendRequests").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        sendFriendRequestButton.setEnabled(true);
                                                        current_state = "friends";
                                                        sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                                        declineFriendRequestButton.setVisibility(View.GONE);
                                                        declineFriendRequestButton.setEnabled(false);
                                                        FriendRequestCountRef.child(senderUserId).child("FriendRequestsCount").child("recived_count").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FriendRequestCountRef.child(receiverUserId).child("FriendRequestsCount").child("sent_count").child(senderUserId).removeValue();
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                /*FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("Sent").hasChild(receiverUserId)){
                                            request_type_sender = "Sent";
                                        }
                                        if(dataSnapshot.child("Received").hasChild(receiverUserId)){
                                            request_type_sender = "Received";
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                FriendRequestRef.child(receiverUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child("Sent").hasChild(senderUserId)){
                                            request_type_reciver = "Sent";
                                        }
                                        if(dataSnapshot.child("Received").hasChild(senderUserId)){
                                            request_type_reciver = "Received";
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                FriendRequestRef.child(senderUserId).child("FriendRequests").child(request_type_sender).child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FriendRequestRef.child(receiverUserId).child("FriendRequests").child(request_type_reciver).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        sendFriendRequestButton.setEnabled(true);
                                                        current_state = "friends";
                                                        sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                                        declineFriendRequestButton.setVisibility(View.GONE);
                                                        declineFriendRequestButton.setEnabled(false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });*/
                            }
                           /* FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild("Sent")){
                                        FriendRequestRef.child(senderUserId).child("FriendRequests").child("Sent").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(receiverUserId)){
                                                    FriendRequestRef.child(senderUserId).child("FriendRequests").child("Sent").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                FriendRequestRef.child(receiverUserId).child("FriendRequests").child("Received").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            sendFriendRequestButton.setEnabled(true);
                                                                            out_current_state = "friends";
                                                                            sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                                                            declineFriendRequestButton.setVisibility(View.GONE);
                                                                            declineFriendRequestButton.setEnabled(false);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else if(dataSnapshot.hasChild("Received")){
                                        FriendRequestRef.child(senderUserId).child("FriendRequests").child("Received").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChild(receiverUserId)){
                                                    FriendRequestRef.child(senderUserId).child("FriendRequests").child("Received").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                FriendRequestRef.child(receiverUserId).child("FriendRequests").child("Sent").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            sendFriendRequestButton.setEnabled(true);
                                                                            out_current_state = "friends";
                                                                            sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                                                            declineFriendRequestButton.setVisibility(View.GONE);
                                                                            declineFriendRequestButton.setEnabled(false);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/




                        }
                    });
                }
            }
        });
        //MaintananceofButtons();
    }

    private void cancleFriendRequest() {
        FriendRequestRef.child(senderUserId).child("FriendRequests").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendRequestRef.child(receiverUserId).child("FriendRequests").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendFriendRequestButton.setEnabled(true);
                                out_current_state = "not_friends";
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);

                            }
                        }
                    });
                }
            }
        });


        /*FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Sent")){
                    if(dataSnapshot.child("Sent").hasChild(receiverUserId)){
                        request_type_sender = "Sent";
                    }
                }
                if(dataSnapshot.hasChild("Received")){
                    if(dataSnapshot.child("Received").hasChild(receiverUserId)){
                        request_type_sender = "Received";
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FriendRequestRef.child(receiverUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Sent")){
                    if(dataSnapshot.child("Sent").hasChild(senderUserId)){
                        request_type_reciver = "Sent";
                    }
                }
                if(dataSnapshot.hasChild("Received")){
                    if(dataSnapshot.child("Received").hasChild(senderUserId)){
                        request_type_reciver = "Received";
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(PersonProfileActivity.this, "type sender : " + request_type_sender, Toast.LENGTH_LONG).show();
        Toast.makeText(PersonProfileActivity.this, "type reciver : " + request_type_reciver, Toast.LENGTH_LONG).show();*/
       /* FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Sent")){
                    FriendRequestRef.child(senderUserId).child("FriendRequests").child("Sent").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(receiverUserId)){
                                FriendRequestRef.child(senderUserId).child("FriendRequests").child("Sent").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FriendRequestRef.child(receiverUserId).child("FriendRequests").child("Received").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        sendFriendRequestButton.setEnabled(true);
                                                        out_current_state = "not_friends";
                                                        sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                                        declineFriendRequestButton.setVisibility(View.GONE);
                                                        declineFriendRequestButton.setEnabled(false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else if(dataSnapshot.hasChild("Received")){
                    FriendRequestRef.child(senderUserId).child("FriendRequests").child("Received").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(receiverUserId)){
                                FriendRequestRef.child(senderUserId).child("FriendRequests").child("Received").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FriendRequestRef.child(receiverUserId).child("FriendRequests").child("Sent").child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        sendFriendRequestButton.setEnabled(true);
                                                        out_current_state = "not_friends";
                                                        sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                                        declineFriendRequestButton.setVisibility(View.GONE);
                                                        declineFriendRequestButton.setEnabled(false);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        /*FriendRequestRef.child(senderUserId).child("FriendRequests").child(request_type_sender).child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendRequestRef.child(receiverUserId).child("FriendRequests").child(request_type_reciver).child(senderUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "not_friends";
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });*/
        //MaintananceofButtons();
    }

    private void MaintananceofButtons() {
        FriendRequestRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiverUserId)){
                    String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();
                    if(request_type.equals("sent")){
                        current_state = "request_sent";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                        sendFriendRequestButton.setBackgroundColor(getResources().getColor(R.color.cancle_friend_request_color));
                        declineFriendRequestButton.setVisibility(View.GONE);
                        gotoPostsButton.setVisibility(View.GONE);
                        gotoSendMessageButton.setVisibility(View.GONE);
                        declineFriendRequestButton.setEnabled(false);
                    } else if(request_type.equals("received")){
                        current_state = "request_received";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_accept_friend_request_button));
                        declineFriendRequestButton.setVisibility(View.VISIBLE);
                        gotoPostsButton.setVisibility(View.GONE);
                        gotoSendMessageButton.setVisibility(View.GONE);
                        declineFriendRequestButton.setEnabled(true);
                        declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancleFriendRequest();
                                FriendRequestCountRef.child(senderUserId).child("FriendRequestsCount").child("recived_count").child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FriendRequestCountRef.child(receiverUserId).child("FriendRequestsCount").child("sent_count").child(senderUserId).removeValue();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    FriendsRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiverUserId)){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "friends";
                                //Toast.makeText(PersonProfileActivity.this, "request friend  :" + current_state, Toast.LENGTH_LONG).show();
                                //Toast.makeText(PersonProfileActivity.this, "current State from friends: " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                sendFriendRequestButton.setBackgroundColor(getResources().getColor(R.color.unfriend_friend_request_color));
                                gotoPostsButton.setVisibility(View.VISIBLE);
                                gotoSendMessageButton.setVisibility(View.VISIBLE);
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            } else {
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "not_friends";
                                //Toast.makeText(PersonProfileActivity.this, "request unfriend  :" + current_state, Toast.LENGTH_LONG).show();
                                //Toast.makeText(PersonProfileActivity.this, "current State from not friend : " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                sendFriendRequestButton.setBackgroundColor(getResources().getColor(R.color.send_friend_request_color));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                gotoPostsButton.setVisibility(View.GONE);
                                gotoSendMessageButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    FriendsRef.child(senderUserId).child("Friends").addValueEventListener(FriendsRefValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(FriendRequestRefValue);


        /*FriendRequestRef.child(senderUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("FriendRequests")) {
                    FriendRequestRef.child(senderUserId).child("FriendRequests").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                            if (dataSnapshots.child("Sent").hasChild(receiverUserId)){*/
                                //String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();
                    /*if(request_type.equals("sent")){
                        current_state = "request_sent";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                        declineFriendRequestButton.setVisibility(View.GONE);
                        declineFriendRequestButton.setEnabled(false);
                    } else if(request_type.equals("received")){
                        current_state = "request_received";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_accept_friend_request_button));
                        declineFriendRequestButton.setVisibility(View.VISIBLE);
                        declineFriendRequestButton.setEnabled(true);
                        declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancleFriendRequest();
                            }
                        });
                    }*/
                               /* current_state = "request_sent";
                                Toast.makeText(PersonProfileActivity.this, "request send  :" + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            } else if(dataSnapshots.child("Received").hasChild(receiverUserId)){
                                current_state = "request_received";
                                Toast.makeText(PersonProfileActivity.this, "request recived     :" + current_state, Toast.LENGTH_LONG).show();
                                //Toast.makeText(PersonProfileActivity.this, "current State from recived: " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_accept_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.VISIBLE);
                                declineFriendRequestButton.setEnabled(true);
                                declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancleFriendRequest();
                                    }
                                });
                            } else {
                                FriendsRef.child(senderUserId).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(receiverUserId)){
                                            sendFriendRequestButton.setEnabled(true);
                                            current_state = "friends";
                                            Toast.makeText(PersonProfileActivity.this, "request friend  :" + current_state, Toast.LENGTH_LONG).show();
                                            //Toast.makeText(PersonProfileActivity.this, "current State from friends: " + current_state, Toast.LENGTH_LONG).show();
                                            sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                            declineFriendRequestButton.setVisibility(View.GONE);
                                            declineFriendRequestButton.setEnabled(false);
                                        } else {
                                            sendFriendRequestButton.setEnabled(true);
                                            current_state = "not_friends";
                                            Toast.makeText(PersonProfileActivity.this, "request unfriend  :" + current_state, Toast.LENGTH_LONG).show();
                                            //Toast.makeText(PersonProfileActivity.this, "current State from not friend : " + current_state, Toast.LENGTH_LONG).show();
                                            sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                            declineFriendRequestButton.setVisibility(View.GONE);
                                            declineFriendRequestButton.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    FriendsRef.child(senderUserId).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiverUserId)){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "friends";
                                Toast.makeText(PersonProfileActivity.this, "out of request friend    : " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            } else {
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "not_friends";
                                Toast.makeText(PersonProfileActivity.this, "out of request unfriend     : " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //Toast.makeText(PersonProfileActivity.this, "state : " + current_state, Toast.LENGTH_LONG).show();
    }

    /*private void MaintananceofButtons() {
        FriendRequestRef.child(senderUserId).child("FriendRequests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Sent").hasChild(receiverUserId)){*/
                    //String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();
                    /*if(request_type.equals("sent")){
                        current_state = "request_sent";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                        declineFriendRequestButton.setVisibility(View.GONE);
                        declineFriendRequestButton.setEnabled(false);
                    } else if(request_type.equals("received")){
                        current_state = "request_received";
                        sendFriendRequestButton.setText(getResources().getString(R.string.person_accept_friend_request_button));
                        declineFriendRequestButton.setVisibility(View.VISIBLE);
                        declineFriendRequestButton.setEnabled(true);
                        declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancleFriendRequest();
                            }
                        });
                    }*/
                    /*current_state = "request_sent";
                    //Toast.makeText(PersonProfileActivity.this, "current State from sent: " + current_state, Toast.LENGTH_LONG).show();
                    sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                    declineFriendRequestButton.setVisibility(View.GONE);
                    declineFriendRequestButton.setEnabled(false);
                } else if(dataSnapshot.child("Received").hasChild(receiverUserId)){
                    current_state = "request_received";
                    //Toast.makeText(PersonProfileActivity.this, "current State from recived: " + current_state, Toast.LENGTH_LONG).show();
                    sendFriendRequestButton.setText(getResources().getString(R.string.person_accept_friend_request_button));
                    declineFriendRequestButton.setVisibility(View.VISIBLE);
                    declineFriendRequestButton.setEnabled(true);
                    declineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancleFriendRequest();
                        }
                    });
                } else {
                    FriendsRef.child(senderUserId).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(receiverUserId)){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "friends";
                                //Toast.makeText(PersonProfileActivity.this, "current State from friends: " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_unfriend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            } else {
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "not_friends";
                                //Toast.makeText(PersonProfileActivity.this, "current State from not friend : " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_send_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(PersonProfileActivity.this, "state : " + current_state, Toast.LENGTH_LONG).show();
    }*/

    private void sendFriendRequestToPerson() {

        /*sFriendRequestRef.child(senderUserId).child("FriendRequests").child("Sent").child(receiverUserId).child("uid").setValue(receiverUserId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sFriendRequestRef.child(receiverUserId).child("FriendRequests").child("Received").child(senderUserId).child("uid").setValue(senderUserId).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendFriendRequestButton.setEnabled(true);
                                out_current_state = "request_sent";
                                Toast.makeText(PersonProfileActivity.this, "from sendFriendRequest : " + current_state, Toast.LENGTH_LONG).show();
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });*/
        //MaintananceofButtons();
        //final int rec = 0, sen = 0;

        FriendRequestRef.child(senderUserId).child("FriendRequests").child(receiverUserId).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FriendRequestRef.child(receiverUserId).child("FriendRequests").child(senderUserId).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                sendFriendRequestButton.setEnabled(true);
                                current_state = "request_sent";
                                sendFriendRequestButton.setText(getResources().getString(R.string.person_cancle_friend_request_button));
                                declineFriendRequestButton.setVisibility(View.GONE);
                                declineFriendRequestButton.setEnabled(false);
                                FriendRequestCountRef.child(senderUserId).child("FriendRequestsCount").child("sent_count").child(receiverUserId).setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FriendRequestCountRef.child(receiverUserId).child("FriendRequestsCount").child("recived_count").child(senderUserId).setValue("received");
                                    }
                                });

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

        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                getCurrentUserCountryLives();
                getUserInfo();
                getUserBook();
            } else {
                Toast.makeText(PersonProfileActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }

        if (FriendRequestRefValue != null && FriendRequestRef != null) {
            FriendRequestRef.removeEventListener(FriendRequestRefValue);
        }
        if (UsersRefValue != null && UsersRef != null) {
            UsersRef.removeEventListener(UsersRefValue);
        }
        if (FriendsRefValue != null && FriendsRef != null) {
            FriendsRef.removeEventListener(FriendsRefValue);
        }
        if (PostsRefValue != null && PostsRef != null) {
            PostsRef.removeEventListener(PostsRefValue);
        }
        if (BookRefValue != null && BookRef != null) {
            BookRef.removeEventListener(BookRefValue);
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
}
