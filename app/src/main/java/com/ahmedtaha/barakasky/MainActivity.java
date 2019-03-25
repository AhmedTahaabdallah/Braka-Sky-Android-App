package com.ahmedtaha.barakasky;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList, home_comments_list;
    private LinearLayoutManager linearLayoutManager;
    private Toolbar mToolBar;
    private TextView notif_home, notif_messages, notif_friends_count, notif_sent_request_friends_count, notif_recived_request_friends_count, notif_book_friends, notif_goldPosts, notif_notif_LikesPots, notif_notif_disLikesPosts, notif_notif_GoldPosts;
    private View navView;
    private CircleImageView navProfileImage;
    private TextView navProfileName;
    private ImageButton add_new_post_imagebutton, goToGoldPostsButton;
    private TextView allMessageofUser;

    private FirebaseAuth mAuth;
    private DatabaseReference LangRef, useresRef, useres_postRef, PostsRef, FriendsRef, FriendRequestCountSentRef, FriendRequestCountReciveRef, Users_onlineRef, AllMessagesRef, subAllMessagesRef, FriendsSignRef, BookFriendsRef, GoldPostssRef, notif_likePostsRef, notif_dislikePostsRef, notif_GoldPostsRef, DailyRef, UrgentRef;
    private ValueEventListener useresRefValue, useres_postRefValue, PostsRefValue, FriendsRefValues, FriendRequestCountSentRefValue,FriendRequestCountReciveRefValue, Users_onlineRefValue, AllMessagesRefValue, subAllMessagesRefValue, FriendsSignRefValue, BookFriendsRefValue, GoldPostssRefValue, notif_likePostsRefValue, notif_dislikePostsRefValue, notif_GoldPostsRefValue, DailyRefValue, UrgentRefValue;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    String currentUserId, transfer = "false";
    Boolean likeChecker = false, dislikeChecker = false, goldPostChecker = false;
    String current_user_country_Lives;
    //private ProgressDialog loadingBar;
    //String[] ss = {"0"};

    //private AlertDialog waitingBar;
    ACProgressFlower dialogs;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private TextView nodata;
    private Dialog mydialog;

    private int notif_textsize = 0
                , notif_paddingleft = 0
                , notif_paddingright = 0
                , notif_paddingtop = 0
                , notif_paddingbottom = 0;

    private int usermessage_textsize = 0
            , usermessage_paddingleft = 0
            , usermessage_paddingright = 0
            , usermessage_paddingtop = 0
            , usermessage_paddingbottom = 0
            , usermessage_marginleft = 0
            , usermessage_marginright = 0
            , usermessage_margintop = 0
            , usermessage_marginbottom = 0;
    private GlobalVar globalVar;
    private int mytoolbar_textsize = 0;
    //private String first_open;
    /*private final String RECYCLER_POSITION_KEY = "recycler_position";
    private int mPosition = RecyclerView.NO_POSITION;
    private static Bundle mBundleState;

    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;*/

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GetUserDateTime getUserDateTime = new GetUserDateTime(MainActivity.this);
        //getUserDateTime.setMyAppDisplayLanguage();
        //Toast.makeText(MainActivity.this, "from oncreate", Toast.LENGTH_LONG).show();
        //String la = Locale.getDefault().getDisplayLanguage();
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

        setContentView(R.layout.activity_main_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_main_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        globalVar = (GlobalVar) getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            currentUserId = mAuth.getCurrentUser().getUid();
        }

        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        LangRef = FirebaseDatabase.getInstance().getReference().child("Users");
        Users_onlineRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useres_postRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountSentRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountReciveRef = FirebaseDatabase.getInstance().getReference().child("Users");
        AllMessagesRef = FirebaseDatabase.getInstance().getReference().child("Users");
        subAllMessagesRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");
        BookFriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GoldPostssRef = FirebaseDatabase.getInstance().getReference().child("GoldPosts");
        notif_likePostsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        notif_dislikePostsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        notif_GoldPostsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DailyRef = FirebaseDatabase.getInstance().getReference().child("BarakaSky_Messages").child("Daily");
        UrgentRef = FirebaseDatabase.getInstance().getReference().child("BarakaSky_Messages").child("Urgent");
        //loadingBar = new ProgressDialog(this);

        //waitingBar = new SpotsDialog.Builder().setContext(MainActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_mainactivity_root);

        mToolBar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.nav_menu_home));
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

        if (la.equals("العربية")){
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                usermessage_textsize = 42;
                usermessage_paddingbottom = -5;
                usermessage_paddingleft = 50;
                usermessage_paddingright = 50;
                usermessage_paddingtop = -5;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 25;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                usermessage_textsize = 28;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 40;
                usermessage_paddingright = 40;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 40;
                usermessage_marginright = 0;
                usermessage_margintop = 10;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                usermessage_textsize = 20;
                usermessage_paddingbottom = 1;
                usermessage_paddingleft = 13;
                usermessage_paddingright = 13;
                usermessage_paddingtop = 1;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 13;
                usermessage_marginright = 0;
                usermessage_margintop = 5;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                usermessage_textsize = 42;
                usermessage_paddingbottom = -5;
                usermessage_paddingleft = 55;
                usermessage_paddingright = 55;
                usermessage_paddingtop = -5;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 25;
                usermessage_marginright = 0;
                usermessage_margintop = 45;
            }
        } else {
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                usermessage_textsize = 50;
                usermessage_paddingbottom = 5;
                usermessage_paddingleft = 35;
                usermessage_paddingright = 35;
                usermessage_paddingtop = 5;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 25;
                usermessage_marginright = 0;
                usermessage_margintop = 35;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                usermessage_textsize = 36;
                usermessage_paddingbottom = 2;
                usermessage_paddingleft = 30;
                usermessage_paddingright = 30;
                usermessage_paddingtop = 2;
                usermessage_marginbottom = 8;
                usermessage_marginleft = 20;
                usermessage_marginright = 0;
                usermessage_margintop = 25;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                usermessage_textsize = 24;
                usermessage_paddingbottom = 3;
                usermessage_paddingleft = 10;
                usermessage_paddingright = 10;
                usermessage_paddingtop = 3;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 10;
                usermessage_marginright = 0;
                usermessage_margintop = 8;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                usermessage_textsize = 60;
                usermessage_paddingbottom = 15;
                usermessage_paddingleft = 55;
                usermessage_paddingright = 55;
                usermessage_paddingtop = 15;
                usermessage_marginbottom = 5;
                usermessage_marginleft = 25;
                usermessage_marginright = 0;
                usermessage_margintop = 55;
            }
        }
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ff0000'>ActionBarTitle </font>"));

        add_new_post_imagebutton = (ImageButton) findViewById(R.id.add_new_post_imagebutton);
        goToGoldPostsButton = (ImageButton) findViewById(R.id.go_to_gold_post_imagebutton);

        allMessageofUser = (TextView) findViewById(R.id.all_messge_of_user_text_view);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            notif_textsize = 32;
            notif_paddingleft = 25;
            notif_paddingright = 25;
            notif_paddingtop = 1;
            notif_paddingbottom = 1;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            notif_textsize = 24;
            notif_paddingleft = 25;
            notif_paddingright = 25;
            notif_paddingtop = 3;
            notif_paddingbottom = 3;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            notif_textsize = 18;
            notif_paddingleft = 8;
            notif_paddingright = 8;
            notif_paddingtop = 3;
            notif_paddingbottom = 3;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            notif_textsize = 36;
            notif_paddingleft = 25;
            notif_paddingright = 25;
            notif_paddingtop = 0;
            notif_paddingbottom = 0;
        }
        navView = navigationView.inflateHeaderView(R.layout.navigation_header_normal);
        navProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        navProfileName = (TextView) navView.findViewById(R.id.nav_user_full_name);
        nodata = (TextView) findViewById(R.id.nodata_mainactivity);

        postList = (RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);



        if(currentUserId != null){

            useresRefValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.hasChild("fullname")){
                            String full_Name = dataSnapshot.child("fullname").getValue().toString();
                            navProfileName.setText(full_Name);
                        } else {
                            //Toast.makeText(MainActivity.this,getResources().getString(R.string.home_toast_profilename_not_exist),Toast.LENGTH_SHORT).show();
                            sendUserToSetupActivaty();
                        }
                        if(dataSnapshot.hasChild("profileimage")){
                            String img = dataSnapshot.child("profileimage").getValue().toString();
                            Picasso.with(MainActivity.this).load(img).placeholder(R.drawable.profile).into(navProfileImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            useresRef.child(currentUserId).addValueEventListener(useresRefValue);
        }

        navProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                    transfer = "true";
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);

            }
        });
        navProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                transfer = "true";
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });
        // here to home And Messages Notifactions
        /*notif_home=(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_home));*/
        notif_messages = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_messages));

        notif_goldPosts = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_gold_post));

        notif_friends_count = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_friends));
        notif_sent_request_friends_count = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_sent_friends_requests));
        notif_recived_request_friends_count = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_recived_friends_requests));
        notif_book_friends = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_book_friends_requests));

        notif_notif_LikesPots = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_notifications_posts_likes));
        notif_notif_disLikesPosts = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_notifications_posts_dislikes));
        notif_notif_GoldPosts = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_notifications_posts_gold));
        //initializeNotifcationDrawer();
        /*SetCountsOfFriends();
        SetCountOfSentFriendsRequests();
        SetCountOfRecivedFriendsRequests();*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });


       add_new_post_imagebutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendUserToPostActivaty();
           }
       });
       goToGoldPostsButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendUserToGoldPostsActivaty();
           }
       });
       allMessageofUser.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendUserToAllMessagesActivaty();
               drawerLayout.closeDrawers();
           }
       });

        //DisplayAllUsersPosts();



        /*getCurrentUserCountryLives();
        SetCountsOfFriends();
        SetCountOfSentFriendsRequests();
        SetCountOfRecivedFriendsRequests();
        SetCountOfAllMessageRecived();
        SetCountOfBookedFriends();
        SetCountsOfGoldPosts();
        SetCountsOfNotification_GoldPosts();
        SetCountsOfNotification_LikePosts();
        SetCountsOfNotification_disLikePosts();
        DisplayAllUsersPosts();*/
    }

    /*private void setMyAppLanguage(Context context, String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
    }*/

    private void getCurrentUserCountryLives(){
        useresRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("country_lives")){
                    current_user_country_Lives = dataSnapshot.child("country_lives").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void SetCountOfRecivedFriendsRequests() {

        FriendRequestCountReciveRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("FriendRequestsCount")){
                    if (dataSnapshot.child("FriendRequestsCount").hasChild("recived_count")){
                        int counts = (int)dataSnapshot.child("FriendRequestsCount").child("recived_count").getChildrenCount();
                        notif_recived_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                        notif_recived_request_friends_count.setTypeface(null, Typeface.BOLD);
                        notif_recived_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_recived_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_recived_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_recived_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_recived_request_friends_count.setTextSize(notif_textsize);
                        notif_recived_request_friends_count.setVisibility(View.VISIBLE);
                        notif_recived_request_friends_count.setText(Integer.toString(counts));
                    } else {
                        notif_recived_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                        notif_recived_request_friends_count.setTypeface(null, Typeface.BOLD);
                        notif_recived_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_recived_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_recived_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_recived_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_recived_request_friends_count.setTextSize(notif_textsize);
                        notif_recived_request_friends_count.setText("");
                        notif_recived_request_friends_count.setVisibility(View.GONE);
                    }
                } else {
                    notif_recived_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                    notif_recived_request_friends_count.setTypeface(null, Typeface.BOLD);
                    notif_recived_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_recived_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_recived_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_recived_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_recived_request_friends_count.setTextSize(notif_textsize);
                    notif_recived_request_friends_count.setText("");
                    notif_recived_request_friends_count.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendRequestCountReciveRef.child(currentUserId).addValueEventListener(FriendRequestCountReciveRefValue);
    }

    /*private void IncriseSentRequestFriend(){
        s_r_f_count++;
    }*/
    private void SetCountOfSentFriendsRequests() {
        //int sent_request_friends_count = 0;
        FriendRequestCountSentRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("FriendRequestsCount")){
                    if (dataSnapshot.child("FriendRequestsCount").hasChild("sent_count")){
                        int count = (int)dataSnapshot.child("FriendRequestsCount").child("sent_count").getChildrenCount();
                        notif_sent_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                        notif_sent_request_friends_count.setTypeface(null, Typeface.BOLD);
                        notif_sent_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_sent_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_sent_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_sent_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_sent_request_friends_count.setTextSize(notif_textsize);
                        notif_sent_request_friends_count.setVisibility(View.VISIBLE);
                        notif_sent_request_friends_count.setText(Integer.toString(count));
                    } else {
                        notif_sent_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                        notif_sent_request_friends_count.setTypeface(null, Typeface.BOLD);
                        notif_sent_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_sent_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_sent_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_sent_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_sent_request_friends_count.setTextSize(notif_textsize);
                        notif_sent_request_friends_count.setText("");
                        notif_sent_request_friends_count.setVisibility(View.GONE);
                    }
                } else {
                    notif_sent_request_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                    notif_sent_request_friends_count.setTypeface(null, Typeface.BOLD);
                    notif_sent_request_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_sent_request_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_sent_request_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_sent_request_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_sent_request_friends_count.setTextSize(notif_textsize);
                    notif_sent_request_friends_count.setText("");
                    notif_sent_request_friends_count.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendRequestCountSentRef.child(currentUserId).addValueEventListener(FriendRequestCountSentRefValue);

    }

    private void SetCountsOfFriends() {
        FriendsRefValues = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int friends_count = (int) dataSnapshot.getChildrenCount();
                    notif_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                    notif_friends_count.setTypeface(null, Typeface.BOLD);
                    notif_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_friends_count.setText(Integer.toString(friends_count));
                    notif_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_friends_count.setTextSize(notif_textsize);
                    notif_friends_count.setVisibility(View.VISIBLE);
                } else {
                    int friends_count = 0;
                    notif_friends_count.setGravity(Gravity.CENTER_VERTICAL);
                    notif_friends_count.setTypeface(null, Typeface.BOLD);
                    notif_friends_count.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_friends_count.setText(Integer.toString(friends_count));
                    notif_friends_count.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_friends_count.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_friends_count.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_friends_count.setTextSize(notif_textsize);
                    notif_friends_count.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendsRef.child(currentUserId).child("Friends").addValueEventListener(FriendsRefValues);
    }

    private void SetCountOfAllMessageRecived() {

        AllMessagesRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("NotSeenMessageCountUsers")){
                    subAllMessagesRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                notif_messages.setText("0");
                                for (DataSnapshot usermessages : dataSnapshot.getChildren()){
                                    int oldcounttxt = Integer.parseInt(notif_messages.getText().toString());
                                    int ucount = (int) usermessages.getChildrenCount();
                                    int newcount = oldcounttxt + ucount;
                                    notif_messages.setGravity(Gravity.CENTER_VERTICAL);
                                    notif_messages.setTypeface(null, Typeface.BOLD);
                                    notif_messages.setTextColor(getResources().getColor(R.color.notifacation_color));
                                    notif_messages.setText(Integer.toString(newcount));
                                    notif_messages.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                                    //int padding_left = getResources().getDimension(R.dimen.mainactivity_navigationview_nitific_textsize);
                                    notif_messages.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                                    notif_messages.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                                    notif_messages.setTextSize(notif_textsize);
                                    notif_messages.setVisibility(View.VISIBLE);
                                    /*if (la.equals("العربية")){
                                        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                                            usermessage_textsize = 20;
                                            usermessage_paddingbottom = 1;
                                            usermessage_paddingleft = 15;
                                            usermessage_paddingright = 15;
                                            usermessage_paddingtop = 1;
                                        }
                                        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                                            usermessage_textsize = 20;
                                            usermessage_paddingbottom = 1;
                                            usermessage_paddingleft = 15;
                                            usermessage_paddingright = 15;
                                            usermessage_paddingtop = 1;
                                        }
                                        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                                            usermessage_textsize = 20;
                                            usermessage_paddingbottom = 1;
                                            usermessage_paddingleft = 15;
                                            usermessage_paddingright = 15;
                                            usermessage_paddingtop = 1;
                                        }
                                        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                                            usermessage_textsize = 20;
                                            usermessage_paddingbottom = 1;
                                            usermessage_paddingleft = 15;
                                            usermessage_paddingright = 15;
                                            usermessage_paddingtop = 1;
                                        }
                                    }*/
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) allMessageofUser.getLayoutParams();
                                    params.setMargins(usermessage_marginleft,usermessage_margintop,usermessage_marginright,usermessage_marginbottom);

                                    allMessageofUser.setLayoutParams(params);

                                    allMessageofUser.setPadding(usermessage_paddingleft,usermessage_paddingtop,usermessage_paddingright,usermessage_paddingbottom);
                                    allMessageofUser.setTextSize(usermessage_textsize);
                                    allMessageofUser.setText(Integer.toString(newcount));
                                    allMessageofUser.setVisibility(View.VISIBLE);
                                }
                                int txt = Integer.parseInt(notif_messages.getText().toString());
                                if (txt == 0) {
                                    notif_messages.setVisibility(View.GONE);
                                    allMessageofUser.setVisibility(View.GONE);
                                } else {
                                    notif_messages.setVisibility(View.VISIBLE);
                                    allMessageofUser.setVisibility(View.VISIBLE);
                                }
                            } else {
                                notif_messages.setVisibility(View.GONE);
                                allMessageofUser.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    subAllMessagesRef.child(currentUserId).child("NotSeenMessageCountUsers").addValueEventListener(subAllMessagesRefValue);
                } else {
                    notif_messages.setVisibility(View.GONE);
                    allMessageofUser.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        AllMessagesRef.child(currentUserId).addValueEventListener(AllMessagesRefValue);
    }

    private void SetCountOfBookedFriends() {

        BookFriendsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("BookPersons")){
                    if (dataSnapshot.child("BookPersons").hasChildren()){
                        int counts = (int)dataSnapshot.child("BookPersons").getChildrenCount();
                        notif_book_friends.setGravity(Gravity.CENTER_VERTICAL);
                        notif_book_friends.setTypeface(null, Typeface.BOLD);
                        notif_book_friends.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_book_friends.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_book_friends.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_book_friends.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_book_friends.setTextSize(notif_textsize);
                        notif_book_friends.setVisibility(View.VISIBLE);
                        notif_book_friends.setText(Integer.toString(counts));
                    } else {
                        notif_book_friends.setGravity(Gravity.CENTER_VERTICAL);
                        notif_book_friends.setTypeface(null, Typeface.BOLD);
                        notif_book_friends.setTextColor(getResources().getColor(R.color.notifacation_color));
                        notif_book_friends.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                        notif_book_friends.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                        notif_book_friends.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                        notif_book_friends.setTextSize(notif_textsize);
                        notif_book_friends.setText("");
                        notif_book_friends.setVisibility(View.GONE);
                    }
                } else {
                    notif_book_friends.setGravity(Gravity.CENTER_VERTICAL);
                    notif_book_friends.setTypeface(null, Typeface.BOLD);
                    notif_book_friends.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_book_friends.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_book_friends.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_book_friends.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_book_friends.setTextSize(notif_textsize);
                    notif_book_friends.setText("");
                    notif_book_friends.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        BookFriendsRef.child(currentUserId).addValueEventListener(BookFriendsRefValue);
    }

    private void SetCountsOfGoldPosts() {
        GoldPostssRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int goldPosts_count = (int) dataSnapshot.getChildrenCount();
                    notif_goldPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_goldPosts.setTypeface(null, Typeface.BOLD);
                    notif_goldPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_goldPosts.setText(Integer.toString(goldPosts_count));
                    notif_goldPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_goldPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_goldPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_goldPosts.setTextSize(notif_textsize);
                    notif_goldPosts.setVisibility(View.VISIBLE);
                } else {
                    int goldPosts_count = 0;
                    notif_goldPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_goldPosts.setTypeface(null, Typeface.BOLD);
                    notif_goldPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_goldPosts.setText(Integer.toString(goldPosts_count));
                    notif_goldPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_goldPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_goldPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_goldPosts.setTextSize(notif_textsize);
                    notif_goldPosts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        GoldPostssRef.addValueEventListener(GoldPostssRefValue);
    }

    private void SetCountsOfNotification_LikePosts() {
        notif_likePostsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int likePosts_count = (int) dataSnapshot.getChildrenCount();
                    notif_notif_LikesPots.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_LikesPots.setTypeface(null, Typeface.BOLD);
                    notif_notif_LikesPots.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_LikesPots.setText(Integer.toString(likePosts_count));
                    notif_notif_LikesPots.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_LikesPots.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_LikesPots.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_LikesPots.setTextSize(notif_textsize);
                    notif_notif_LikesPots.setVisibility(View.VISIBLE);
                } else {
                    int likePosts_count = 0;
                    notif_notif_LikesPots.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_LikesPots.setTypeface(null, Typeface.BOLD);
                    notif_notif_LikesPots.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_LikesPots.setText(Integer.toString(likePosts_count));
                    notif_notif_LikesPots.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_LikesPots.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_LikesPots.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_LikesPots.setTextSize(notif_textsize);
                    notif_notif_LikesPots.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notif_likePostsRef.child(currentUserId).child("Notifications").child("LikePosts").addValueEventListener(notif_likePostsRefValue);
    }

    private void SetCountsOfNotification_disLikePosts() {
        notif_dislikePostsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int likePosts_count = (int) dataSnapshot.getChildrenCount();
                    notif_notif_disLikesPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_disLikesPosts.setTypeface(null, Typeface.BOLD);
                    notif_notif_disLikesPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_disLikesPosts.setText(Integer.toString(likePosts_count));
                    notif_notif_disLikesPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_disLikesPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_disLikesPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_disLikesPosts.setTextSize(notif_textsize);
                    notif_notif_disLikesPosts.setVisibility(View.VISIBLE);
                } else {
                    int likePosts_count = 0;
                    notif_notif_disLikesPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_disLikesPosts.setTypeface(null, Typeface.BOLD);
                    notif_notif_disLikesPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_disLikesPosts.setText(Integer.toString(likePosts_count));
                    notif_notif_disLikesPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_disLikesPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_disLikesPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_disLikesPosts.setTextSize(notif_textsize);
                    notif_notif_disLikesPosts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notif_dislikePostsRef.child(currentUserId).child("Notifications").child("disLikePosts").addValueEventListener(notif_dislikePostsRefValue);
    }

    private void SetCountsOfNotification_GoldPosts() {
        notif_GoldPostsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int likePosts_count = (int) dataSnapshot.getChildrenCount();
                    notif_notif_GoldPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_GoldPosts.setTypeface(null, Typeface.BOLD);
                    notif_notif_GoldPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_GoldPosts.setText(Integer.toString(likePosts_count));
                    notif_notif_GoldPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_GoldPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_GoldPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_GoldPosts.setTextSize(notif_textsize);
                    notif_notif_GoldPosts.setVisibility(View.VISIBLE);
                } else {
                    int likePosts_count = 0;
                    notif_notif_GoldPosts.setGravity(Gravity.CENTER_VERTICAL);
                    notif_notif_GoldPosts.setTypeface(null, Typeface.BOLD);
                    notif_notif_GoldPosts.setTextColor(getResources().getColor(R.color.notifacation_color));
                    notif_notif_GoldPosts.setText(Integer.toString(likePosts_count));
                    notif_notif_GoldPosts.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
                    notif_notif_GoldPosts.setPadding(notif_paddingleft,notif_paddingtop,notif_paddingright,notif_paddingbottom);
                    notif_notif_GoldPosts.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
                    notif_notif_GoldPosts.setTextSize(notif_textsize);
                    notif_notif_GoldPosts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notif_GoldPostsRef.child(currentUserId).child("Notifications").child("GoldPosts").addValueEventListener(notif_GoldPostsRefValue);
    }

    private void GetBarakaSkyMessage() {
        globalVar.setFirst_open("false");
        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat currentorderDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String da = currentorderDate.format(calFororderDate.getTime());
        GetUserDateTime getUserDateTime = new GetUserDateTime(MainActivity.this);
        final String date_time_value = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy HH:mm:ss");
        String date_valuess = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy");
        String yy = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "yyyy");
        String dd = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "dd");
        String mm = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "MM");
        /*SimpleDateFormat currentorde = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Toast.makeText(MainActivity.this, date_valuess, Toast.LENGTH_LONG).show();
        Date dat = null;
        try {
            dat = currentorde.parse(date_valuess);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }*/
        //String yy = date_time_value.substring(0, 4);
        //String mm = date_time_value.substring(4, 2);
        //String dd = date_time_value.substring(8, 2);
/*String ff = "";
        if (la.equals("العربية")){
            String yy = getUserDateTime.getDateToShow(da, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "dd-MM-yyyy");
        }*/
        String fff = dd + "-" + mm + "-" + yy;
        final String date_value =  fff;
        //Toast.makeText(MainActivity.this, date_value, Toast.LENGTH_LONG).show();
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date endeddate = dateFormat.parse(ended_bd);
        Date starteddate = dateFormat.parse(started_bd);*/
        UrgentRef.orderByChild("order_number").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> isUrgent = new HashMap<>();
                isUrgent.put("isurgent", "false");
                isUrgent.put("isok", "false");
                //isUrgent.put("isurgent", "false");
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                    int urgent_cont = (int) dataSnapshot.getChildrenCount();
                    int cu_count = 0;
                    for (DataSnapshot urgents : dataSnapshot.getChildren()){
                        cu_count += 1;
                        final String UrgentKey = urgents.getKey();
                        //Toast.makeText(MainActivity.this, UrgentKey, Toast.LENGTH_LONG).show();
                        Boolean isurgentWatch = false;
                        if (urgents.hasChild("usersview")){
                            if (urgents.child("usersview").hasChild(currentUserId)){
                                isurgentWatch = true;
                            } else {
                                isurgentWatch = false;
                            }
                        } else {
                            isurgentWatch = false;
                        }

                        String startd = urgents.child("start_date").getValue().toString();
                        String endd = urgents.child("end_date").getValue().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date endeddate = null;
                        Date starteddate = null;
                        Date todaydate = null;
                        try {
                            endeddate = dateFormat.parse(endd);
                            starteddate = dateFormat.parse(startd);
                            todaydate = dateFormat.parse(date_time_value);
                        } catch (ParseException e) {
                            Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                        String isok = isUrgent.get("isok");
                        if (!isurgentWatch && isok.equals("false") && todaydate.after(starteddate) && todaydate.before(endeddate)){
                            //Toast.makeText(MainActivity.this, la, Toast.LENGTH_LONG).show();
                            isUrgent.put("isurgent", "true");
                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.barakaskydialog_layout);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            TextView messagetitle = dialog.findViewById(R.id.barakadialog_title);
                            if (la.equals("العربية")){
                                if (urgents.hasChild("title_ar")){
                                    String mt = urgents.child("title_ar").getValue().toString();
                                    messagetitle.setText(mt);
                                    messagetitle.setVisibility(View.VISIBLE);
                                } else {
                                    messagetitle.setVisibility(View.GONE);
                                }
                            } else if (la.equals("English")){
                                if (urgents.hasChild("title_en")){
                                    String mt = urgents.child("title_en").getValue().toString();
                                    messagetitle.setText(mt);
                                    messagetitle.setVisibility(View.VISIBLE);
                                } else {
                                    messagetitle.setVisibility(View.GONE);
                                }
                            } else {
                                if (urgents.hasChild("title_en")){
                                    String mt = urgents.child("title_en").getValue().toString();
                                    messagetitle.setText(mt);
                                    messagetitle.setVisibility(View.VISIBLE);
                                } else {
                                    messagetitle.setVisibility(View.GONE);
                                }
                            }

                            ImageView messageimage = dialog.findViewById(R.id.barakadialog_image);
                            if (urgents.hasChild("image_url")){
                                String mi = urgents.child("image_url").getValue().toString();
                                messageimage.setVisibility(View.VISIBLE);
                                Picasso.with(MainActivity.this).load(mi).into(messageimage);
                            } else {
                                messageimage.setVisibility(View.GONE);
                            }

                            TextView messagebody = dialog.findViewById(R.id.barakadialog_message);
                            if (la.equals("العربية")){
                                if (urgents.hasChild("message_ar")){
                                    String mt = urgents.child("message_ar").getValue().toString();
                                    messagebody.setText(mt);
                                    messagebody.setVisibility(View.VISIBLE);
                                } else {
                                    messagebody.setVisibility(View.GONE);
                                }
                            } else if (la.equals("English")){
                                if (urgents.hasChild("message_en")){
                                    String mt = urgents.child("message_en").getValue().toString();
                                    messagebody.setText(mt);
                                    messagebody.setVisibility(View.VISIBLE);
                                } else {
                                    messagebody.setVisibility(View.GONE);
                                }
                            } else {
                                if (urgents.hasChild("message_en")){
                                    String mt = urgents.child("message_en").getValue().toString();
                                    messagebody.setText(mt);
                                    messagebody.setVisibility(View.VISIBLE);
                                } else {
                                    messagebody.setVisibility(View.GONE);
                                }
                            }

                            TextView messagelink = dialog.findViewById(R.id.barakadialog_link);
                            messagelink.setVisibility(View.GONE);
                            if(urgents.hasChild("Link")){
                                String lan = "";
                                if (la.equals("العربية")){
                                    lan = "ar";
                                } else if (la.equals("English")){
                                    lan = "en";
                                } else {
                                    lan = "en";
                                }
                                String link_txt = "link_text_" + lan;

                                if (urgents.child("Link").hasChild(link_txt) && urgents.child("Link").hasChild("link_to") && urgents.child("Link").hasChild("link_type")){
                                    messagelink.setVisibility(View.VISIBLE);
                                    //Toast.makeText(MainActivity.this, urgents.child("Link").child(link_txt).getValue().toString(), Toast.LENGTH_LONG).show();
                                    messagelink.setText(urgents.child("Link").child(link_txt).getValue().toString());
                                    final String link_type = urgents.child("Link").child("link_type").getValue().toString();
                                    final String link_to = urgents.child("Link").child("link_to").getValue().toString();
                                    messagelink.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (link_type.equals("app_user")){
                                                LangRef.orderByChild("username").startAt(link_to).endAt(link_to + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshote) {
                                                        if (dataSnapshote.exists()) {
                                                            String uid = "" ;
                                                            for (DataSnapshot ddd: dataSnapshote.getChildren()) {
                                                                uid = ddd.getKey();
                                                            }
                                                            //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_LONG).show();
                                                            if (currentUserId.equals(uid)){
                                                                transfer = "true";
                                                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                transfer = "true";
                                                                Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                                                intent.putExtra("visit_user_id", uid);
                                                                startActivity(intent);
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            } else if (link_type.equals("app_post")){
                                                PostsRef.child(link_to).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                                        if (dataSnapshott.exists() && dataSnapshott.hasChildren()){
                                                            String p_uid = dataSnapshott.child("uid").getValue().toString();
                                                            transfer = "true";
                                                            Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                                            clickpostIntent.putExtra("PostKey", link_to);
                                                            clickpostIntent.putExtra("Activity", "main");
                                                            clickpostIntent.putExtra("PostUid", p_uid);
                                                            clickpostIntent.putExtra("current_user_id", currentUserId);
                                                            startActivity(clickpostIntent);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            } else if (link_type.equals("app_app")){
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                            } else if (link_type.equals("app_another")){
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_to));
                                                startActivity(browserIntent);
                                            }

                                            UrgentRef.child(UrgentKey).child("usersview").child(currentUserId).setValue("true");
                                            UrgentRef.child(UrgentKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshotse) {
                                                    if (dataSnapshotse.exists() && dataSnapshotse.hasChildren()){
                                                        int count = (int) dataSnapshotse.getChildrenCount();
                                                        UrgentRef.child(UrgentKey).child("usersview_number").setValue(Integer.toString(count));
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    messagelink.setVisibility(View.GONE);
                                }
                            }

                            FrameLayout message_ok = dialog.findViewById(R.id.barakadialog_ok);
                            TextView message_ok_text = dialog.findViewById(R.id.barakadialog_ok_text);
                            message_ok_text.setText(getResources().getString(R.string.mainactivity_dailog_ok_button));
                            message_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UrgentRef.child(UrgentKey).child("usersview").child(currentUserId).setValue("true");
                                    UrgentRef.child(UrgentKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                            if (dataSnapshots.exists() && dataSnapshots.hasChildren()){
                                                int count = (int) dataSnapshots.getChildrenCount();
                                                UrgentRef.child(UrgentKey).child("usersview_number").setValue(Integer.toString(count));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            isUrgent.put("isok", "true");

                        }

                        if (urgent_cont == cu_count){
                            String dailyok = isUrgent.get("isurgent");

                            if (dailyok.equals("false")){
                                DailyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotss) {
                                        if (dataSnapshotss.exists() && dataSnapshotss.hasChild(date_value)){
                                            //Toast.makeText(MainActivity.this, "here : ", Toast.LENGTH_LONG).show();
                                            DailyRef.child(date_value).orderByChild("order_number").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshotds) {
                                                    // start daily for loop
                                                    Map<String,String> isDaily = new HashMap<>();
                                                    isDaily.put("isDailyok", "false");
                                                    for (DataSnapshot dailys : dataSnapshotds.getChildren()) {
                                                        final String DailyKey = dailys.getKey();

                                                        Boolean isdailyWatch = false;
                                                        if (dailys.hasChild("usersview")){
                                                            if (dailys.child("usersview").hasChild(currentUserId)){
                                                                isdailyWatch = true;
                                                            } else {
                                                                isdailyWatch = false;
                                                            }
                                                        } else {
                                                            isdailyWatch = false;
                                                        }
                                                        String isdailyok = isDaily.get("isDailyok");
                                                        //Toast.makeText(MainActivity.this, "here : " + isdailyok, Toast.LENGTH_LONG).show();
                                                        if (!isdailyWatch && isdailyok.equals("false")){

                                                            isDaily.put("isDailyok", "true");
                                                            final Dialog dialog = new Dialog(MainActivity.this);
                                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                            dialog.setCancelable(false);
                                                            dialog.setContentView(R.layout.barakaskydialog_layout);
                                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                                            TextView messagetitle = dialog.findViewById(R.id.barakadialog_title);
                                                            if (la.equals("العربية")){
                                                                if (dailys.hasChild("title_ar")){
                                                                    String mt = dailys.child("title_ar").getValue().toString();
                                                                    messagetitle.setText(mt);
                                                                    messagetitle.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagetitle.setVisibility(View.GONE);
                                                                }
                                                            } else if (la.equals("English")){
                                                                if (dailys.hasChild("title_en")){
                                                                    String mt = dailys.child("title_en").getValue().toString();
                                                                    messagetitle.setText(mt);
                                                                    messagetitle.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagetitle.setVisibility(View.GONE);
                                                                }
                                                            } else {
                                                                if (dailys.hasChild("title_en")){
                                                                    String mt = dailys.child("title_en").getValue().toString();
                                                                    messagetitle.setText(mt);
                                                                    messagetitle.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagetitle.setVisibility(View.GONE);
                                                                }
                                                            }

                                                            ImageView messageimage = dialog.findViewById(R.id.barakadialog_image);
                                                            if (dailys.hasChild("image_url")){
                                                                String mi = dailys.child("image_url").getValue().toString();
                                                                messageimage.setVisibility(View.VISIBLE);
                                                                Picasso.with(MainActivity.this).load(mi).into(messageimage);
                                                            } else {
                                                                messageimage.setVisibility(View.GONE);
                                                            }

                                                            TextView messagebody = dialog.findViewById(R.id.barakadialog_message);
                                                            if (la.equals("العربية")){
                                                                if (dailys.hasChild("message_ar")){
                                                                    String mt = dailys.child("message_ar").getValue().toString();
                                                                    messagebody.setText(mt);
                                                                    messagebody.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagebody.setVisibility(View.GONE);
                                                                }
                                                            } else if (la.equals("English")){
                                                                if (dailys.hasChild("message_en")){
                                                                    String mt = dailys.child("message_en").getValue().toString();
                                                                    messagebody.setText(mt);
                                                                    messagebody.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagebody.setVisibility(View.GONE);
                                                                }
                                                            } else {
                                                                if (dailys.hasChild("message_en")){
                                                                    String mt = dailys.child("message_en").getValue().toString();
                                                                    messagebody.setText(mt);
                                                                    messagebody.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    messagebody.setVisibility(View.GONE);
                                                                }
                                                            }

                                                            TextView messagelink = dialog.findViewById(R.id.barakadialog_link);
                                                            messagelink.setVisibility(View.GONE);
                                                            if(dailys.hasChild("Link")){
                                                                String lan = "";
                                                                if (la.equals("العربية")){
                                                                    lan = "ar";
                                                                } else if (la.equals("English")){
                                                                    lan = "en";
                                                                } else {
                                                                    lan = "en";
                                                                }
                                                                String link_txt = "link_text_" + lan;
                                                                if (dailys.child("Link").hasChild(link_txt) && dailys.child("Link").hasChild("link_to") && dailys.child("Link").hasChild("link_type")){
                                                                    messagelink.setVisibility(View.VISIBLE);
                                                                    messagelink.setText(dailys.child("Link").child(link_txt).getValue().toString());
                                                                    final String link_type = dailys.child("Link").child("link_type").getValue().toString();
                                                                    final String link_to = dailys.child("Link").child("link_to").getValue().toString();
                                                                    messagelink.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (link_type.equals("app_user")){
                                                                                LangRef.orderByChild("username").startAt(link_to).endAt(link_to + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshote) {
                                                                                        if (dataSnapshote.exists()) {
                                                                                            String uid = "" ;
                                                                                            for (DataSnapshot ddd: dataSnapshote.getChildren()) {
                                                                                                uid = ddd.getKey();
                                                                                            }
                                                                                            //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_LONG).show();
                                                                                            if (currentUserId.equals(uid)){
                                                                                                transfer = "true";
                                                                                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                                                                                startActivity(intent);
                                                                                            } else {
                                                                                                transfer = "true";
                                                                                                Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                                                                                intent.putExtra("visit_user_id", uid);
                                                                                                startActivity(intent);
                                                                                            }
                                                                                        }

                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });
                                                                            } else if (link_type.equals("app_post")){
                                                                                PostsRef.child(link_to).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                                                                        if (dataSnapshott.exists() && dataSnapshott.hasChildren()){
                                                                                            String p_uid = dataSnapshott.child("uid").getValue().toString();
                                                                                            transfer = "true";
                                                                                            Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                                                                            clickpostIntent.putExtra("PostKey", link_to);
                                                                                            clickpostIntent.putExtra("Activity", "main");
                                                                                            clickpostIntent.putExtra("PostUid", p_uid);
                                                                                            clickpostIntent.putExtra("current_user_id", currentUserId);
                                                                                            startActivity(clickpostIntent);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });
                                                                            } else if (link_type.equals("app_app")){
                                                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                                try {
                                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                                }
                                                                            } else if (link_type.equals("app_another")){
                                                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_to));
                                                                                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                startActivity(Intent.createChooser(browserIntent, "Open With"));
                                                                            }

                                                                            DailyRef.child(date_value).child(DailyKey).child("usersview").child(currentUserId).setValue("true");
                                                                            DailyRef.child(date_value).child(DailyKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshotse) {
                                                                                    if (dataSnapshotse.exists() && dataSnapshotse.hasChildren()){
                                                                                        int count = (int) dataSnapshotse.getChildrenCount();
                                                                                        DailyRef.child(date_value).child(DailyKey).child("usersview_number").setValue(Integer.toString(count));
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            dialog.dismiss();
                                                                        }
                                                                    });
                                                                } else {
                                                                    messagelink.setVisibility(View.GONE);
                                                                }
                                                            }

                                                            FrameLayout message_ok = dialog.findViewById(R.id.barakadialog_ok);
                                                            TextView message_ok_text = dialog.findViewById(R.id.barakadialog_ok_text);
                                                            message_ok_text.setText(getResources().getString(R.string.mainactivity_dailog_ok_button));
                                                            message_ok.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    DailyRef.child(date_value).child(DailyKey).child("usersview").child(currentUserId).setValue("true");
                                                                    DailyRef.child(date_value).child(DailyKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshotse) {
                                                                            if (dataSnapshotse.exists() && dataSnapshotse.hasChildren()){
                                                                                int count = (int) dataSnapshotse.getChildrenCount();
                                                                                DailyRef.child(date_value).child(DailyKey).child("usersview_number").setValue(Integer.toString(count));
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            dialog.show();
                                                        }
                                                    }

                                                    // end dailly for loop
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
                        }
                    }

                } else {
                    DailyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshotss) {
                            if (dataSnapshotss.exists() && dataSnapshotss.hasChild(date_value)){
                                DailyRef.child(date_value).orderByChild("order_number").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotds) {
                                        // start daily for loop
                                        Map<String,String> isDaily = new HashMap<>();
                                        isDaily.put("isDailyok", "false");
                                        for (DataSnapshot dailys : dataSnapshotds.getChildren()) {
                                            final String DailyKey = dailys.getKey();

                                            Boolean isurgentWatch = false;
                                            if (dailys.hasChild("usersview")){
                                                if (dailys.child("usersview").hasChild(currentUserId)){
                                                    isurgentWatch = true;
                                                } else {
                                                    isurgentWatch = false;
                                                }
                                            } else {
                                                isurgentWatch = false;
                                            }
                                            String isdailyok = isDaily.get("isDailyok");
                                            if (!isurgentWatch && isdailyok.equals("false")){
                                                isDaily.put("isDailyok", "true");
                                                final Dialog dialog = new Dialog(MainActivity.this);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.setCancelable(false);
                                                dialog.setContentView(R.layout.barakaskydialog_layout);
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                                TextView messagetitle = dialog.findViewById(R.id.barakadialog_title);
                                                if (la.equals("العربية")){
                                                    if (dailys.hasChild("title_ar")){
                                                        String mt = dailys.child("title_ar").getValue().toString();
                                                        messagetitle.setText(mt);
                                                        messagetitle.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagetitle.setVisibility(View.GONE);
                                                    }
                                                } else if (la.equals("English")){
                                                    if (dailys.hasChild("title_en")){
                                                        String mt = dailys.child("title_en").getValue().toString();
                                                        messagetitle.setText(mt);
                                                        messagetitle.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagetitle.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    if (dailys.hasChild("title_en")){
                                                        String mt = dailys.child("title_en").getValue().toString();
                                                        messagetitle.setText(mt);
                                                        messagetitle.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagetitle.setVisibility(View.GONE);
                                                    }
                                                }

                                                ImageView messageimage = dialog.findViewById(R.id.barakadialog_image);
                                                if (dailys.hasChild("image_url")){
                                                    String mi = dailys.child("image_url").getValue().toString();
                                                    messageimage.setVisibility(View.VISIBLE);
                                                    Picasso.with(MainActivity.this).load(mi).into(messageimage);
                                                } else {
                                                    messageimage.setVisibility(View.GONE);
                                                }

                                                TextView messagebody = dialog.findViewById(R.id.barakadialog_message);
                                                if (la.equals("العربية")){
                                                    if (dailys.hasChild("message_ar")){
                                                        String mt = dailys.child("message_ar").getValue().toString();
                                                        messagebody.setText(mt);
                                                        messagebody.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagebody.setVisibility(View.GONE);
                                                    }
                                                } else if (la.equals("English")){
                                                    if (dailys.hasChild("message_en")){
                                                        String mt = dailys.child("message_en").getValue().toString();
                                                        messagebody.setText(mt);
                                                        messagebody.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagebody.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    if (dailys.hasChild("message_en")){
                                                        String mt = dailys.child("message_en").getValue().toString();
                                                        messagebody.setText(mt);
                                                        messagebody.setVisibility(View.VISIBLE);
                                                    } else {
                                                        messagebody.setVisibility(View.GONE);
                                                    }
                                                }

                                                TextView messagelink = dialog.findViewById(R.id.barakadialog_link);
                                                messagelink.setVisibility(View.GONE);
                                                if(dailys.hasChild("Link")){
                                                    String lan = "";
                                                    if (la.equals("العربية")){
                                                        lan = "ar";
                                                    } else if (la.equals("English")){
                                                        lan = "en";
                                                    } else {
                                                        lan = "en";
                                                    }
                                                    String link_txt = "link_text_" + lan;
                                                    if (dailys.child("Link").hasChild(link_txt) && dailys.child("Link").hasChild("link_to") && dailys.child("Link").hasChild("link_type")){
                                                        messagelink.setVisibility(View.VISIBLE);
                                                        messagelink.setText(dailys.child("Link").child(link_txt).getValue().toString());
                                                        final String link_type = dailys.child("Link").child("link_type").getValue().toString();
                                                        final String link_to = dailys.child("Link").child("link_to").getValue().toString();
                                                        messagelink.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (link_type.equals("app_user")){
                                                                    LangRef.orderByChild("username").startAt(link_to).endAt(link_to + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshote) {
                                                                            if (dataSnapshote.exists()) {
                                                                                String uid = "" ;
                                                                                for (DataSnapshot ddd: dataSnapshote.getChildren()) {
                                                                                    uid = ddd.getKey();
                                                                                }
                                                                                //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_LONG).show();
                                                                                if (currentUserId.equals(uid)){
                                                                                    transfer = "true";
                                                                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                                                                    startActivity(intent);
                                                                                } else {
                                                                                    transfer = "true";
                                                                                    Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                                                                    intent.putExtra("visit_user_id", uid);
                                                                                    startActivity(intent);
                                                                                }
                                                                            }

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                } else if (link_type.equals("app_post")){
                                                                    PostsRef.child(link_to).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                                                            if (dataSnapshott.exists() && dataSnapshott.hasChildren()){
                                                                                String p_uid = dataSnapshott.child("uid").getValue().toString();
                                                                                transfer = "true";
                                                                                Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                                                                clickpostIntent.putExtra("PostKey", link_to);
                                                                                clickpostIntent.putExtra("Activity", "main");
                                                                                clickpostIntent.putExtra("PostUid", p_uid);
                                                                                clickpostIntent.putExtra("current_user_id", currentUserId);
                                                                                startActivity(clickpostIntent);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                } else if (link_type.equals("app_app")){
                                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                                    try {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                                    }
                                                                } else if (link_type.equals("app_another")){
                                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link_to));
                                                                    startActivity(browserIntent);
                                                                }

                                                                DailyRef.child(date_value).child(DailyKey).child("usersview").child(currentUserId).setValue("true");
                                                                DailyRef.child(date_value).child(DailyKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotse) {
                                                                        if (dataSnapshotse.exists() && dataSnapshotse.hasChildren()){
                                                                            int count = (int) dataSnapshotse.getChildrenCount();
                                                                            DailyRef.child(date_value).child(DailyKey).child("usersview_number").setValue(Integer.toString(count));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                dialog.dismiss();
                                                            }
                                                        });
                                                    } else {
                                                        messagelink.setVisibility(View.GONE);
                                                    }
                                                }

                                                FrameLayout message_ok = dialog.findViewById(R.id.barakadialog_ok);
                                                TextView message_ok_text = dialog.findViewById(R.id.barakadialog_ok_text);
                                                message_ok_text.setText(getResources().getString(R.string.mainactivity_dailog_ok_button));
                                                message_ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        DailyRef.child(date_value).child(DailyKey).child("usersview").child(currentUserId).setValue("true");
                                                        DailyRef.child(date_value).child(DailyKey).child("usersview").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshotse) {
                                                                if (dataSnapshotse.exists() && dataSnapshotse.hasChildren()){
                                                                    int count = (int) dataSnapshotse.getChildrenCount();
                                                                    DailyRef.child(date_value).child(DailyKey).child("usersview_number").setValue(Integer.toString(count));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show();
                                            }
                                        }

                                        // end dailly for loop
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayAllUsersPosts() {
        final String[] ss = {"0"};
        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(PostsRef.orderByChild("order_date"), Posts.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_post_layout_normal, parent, false);

                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final PostViewHolder holder, int position, final Posts model) {
                // Bind the image_details object to the BlogViewHolder
                // ...
                if(ss[0].equals("0")){
                    /*loadingBar.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                    loadingBar.setMessage(getResources().getString(R.string.posts_loadingbar_message));
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();*/
                    //mydialog.dismiss();
                    /*waitingBar.setMessage(getResources().getString(R.string.posts_loadingbar_title));
                    waitingBar.setCanceledOnTouchOutside(false);
                    //waitingBar.show();
                    waitingBar.create();
                    waitingBar.show();*/
                    /*dialogs = new ACProgressFlower.Builder(MainActivity.this)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)
                            .text(getResources().getString(R.string.posts_loadingbar_title))
                            .fadeColor(Color.DKGRAY).build();
                    dialogs.show();*/
                    /*mydialog = new Dialog(MainActivity.this);
                    mydialog.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                    mydialog.setCancelable(false);
                    mydialog.setContentView(R.layout.myprogressbr_layout);
                    mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mydialog.show();*/
                }
                final String postKey = getRef(position).getKey();
                final String description = model.getDescription();
                final String imageUrl = model.getPostimage();
                final String post_uid = model.getUid();
                final String next_day = model.getNext_date_time();
                //Toast.makeText(MainActivity.this, model.getDateandtime() , Toast.LENGTH_LONG).show();

                useres_postRefValue = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(post_uid)){
                           //holder.setDate(model.getDate());
                            //holder.setTime(model.getTime());
                            holder.setDateandtime(model.getDateandtime());
                            holder.setDescription(model.getDescription());
                            holder.setPostimage(getApplicationContext(), model.getPostimage());
                            //holder.setProfileimage(getApplicationContext(), model.getProfileimage());
                            holder.setUid(getApplicationContext(),model.getUid());
                            holder.setLikeButtonStatus(postKey);
                            holder.setdisLikeButtonStatus(postKey);
                            holder.setDisplay_comments(postKey);
                            holder.setGoldPostsButtonStatus(postKey, currentUserId);

                            Button editPost = (Button) holder.mview.findViewById(R.id.edit_post_button_allpost);
                            Button deletePost = (Button) holder.mview.findViewById(R.id.delete_post_button_allpost);
                            final String uidPost = model.getUid();
                            if(currentUserId.equals(uidPost)){
                                editPost.setVisibility(View.VISIBLE);
                                deletePost.setVisibility(View.VISIBLE);
                            } else {
                                editPost.setVisibility(View.INVISIBLE);
                                deletePost.setVisibility(View.GONE);
                            }

                            if (!currentUserId.equals(uidPost)){
                                FriendsSignRefValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            if (dataSnapshot.hasChild(uidPost)){
                                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.VISIBLE);
                                            } else {
                                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                FriendsSignRef.child(currentUserId).child("Friends").addValueEventListener(FriendsSignRefValue);
                            } else {
                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                            }
                            /*holder.mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    transfer = "true";
                                    Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                    clickpostIntent.putExtra("PostKey", postKey);
                                    clickpostIntent.putExtra("Activity", "main");
                                    clickpostIntent.putExtra("PostUid", post_uid);
                                    clickpostIntent.putExtra("current_user_id", currentUserId);
                                    startActivity(clickpostIntent);
                                }
                            });*/
                            holder.mview.findViewById(R.id.comment_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    transfer = "true";
                                    Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                    clickpostIntent.putExtra("PostKey", postKey);
                                    clickpostIntent.putExtra("Activity", "main");
                                    clickpostIntent.putExtra("PostUid", post_uid);
                                    clickpostIntent.putExtra("current_user_id", currentUserId);
                                    startActivity(clickpostIntent);
                                }
                            });
                            holder.mview.findViewById(R.id.post_image).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    transfer = "true";
                                    Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                    clickpostIntent.putExtra("PostKey", postKey);
                                    clickpostIntent.putExtra("Activity", "main");
                                    clickpostIntent.putExtra("PostUid", post_uid);
                                    clickpostIntent.putExtra("current_user_id", currentUserId);
                                    startActivity(clickpostIntent);
                                }
                            });



                            holder.mview.findViewById(R.id.edit_post_button_allpost).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditCurrentPost(description, postKey);
                                }
                            });

                            holder.mview.findViewById(R.id.delete_post_button_allpost).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteCurrentPost(postKey,imageUrl);
                                }
                            });

                            holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        likeChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        PostsRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(likeChecker.equals(true)){
                                                    if(dataSnapshot.child(postKey).child("LikesPosts").hasChild(currentUserId)){
                                                        PostsRef.child(postKey).child("LikesPosts").child(currentUserId).removeValue();
                                                        useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).removeValue();
                                                        likeChecker = false;
                                                    } else {
                                                        HashMap likepostMap = new HashMap();
                                                        likepostMap.put("order_date",order_date);
                                                        likepostMap.put("date_time",date_times);
                                                        PostsRef.child(postKey).child("LikesPosts").child(currentUserId).updateChildren(likepostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).removeValue();
                                                                holder.dislikePostButton.setImageResource(R.drawable.dislike_before);
                                                                HashMap likespostMap = new HashMap();
                                                                likespostMap.put("order_date",order_date);
                                                                likespostMap.put("date_time",date_times);
                                                                useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).updateChildren(likespostMap);
                                                                useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).removeValue();
                                                            }
                                                        });
                                                        likeChecker = false;
                                                        //likePostButton.setImageResource(R.drawable.like);
                                                        //display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        PostsRef.addValueEventListener(PostsRefValue);
                                    }
                                }
                            });

                            holder.dislikePostButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        dislikeChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        PostsRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dislikeChecker.equals(true)){
                                                    if(dataSnapshot.child(postKey).child("disLikesPosts").hasChild(currentUserId)){
                                                        PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).removeValue();
                                                        useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).removeValue();
                                                        dislikeChecker = false;
                                                    } else {
                                                        HashMap dislikepostMap = new HashMap();
                                                        dislikepostMap.put("order_date",order_date);
                                                        dislikepostMap.put("date_time",date_times);
                                                        PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).updateChildren(dislikepostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                PostsRef.child(postKey).child("LikesPosts").child(currentUserId).removeValue();
                                                                useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).removeValue();
                                                                HashMap dislikespostMap = new HashMap();
                                                                dislikespostMap.put("order_date",order_date);
                                                                dislikespostMap.put("date_time",date_times);
                                                                useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).updateChildren(dislikespostMap);
                                                                holder.likePostButton.setImageResource(R.drawable.dislike);
                                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                //Toast.makeText(MainActivity.this, "nex Day", Toast.LENGTH_LONG).show();
                                                                try {
                                                                    final Date next_date = dateFormat.parse(next_day);

                                                                    Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                                                    //calForDate_order.add(Calendar.HOUR, 2);
                                                                    SimpleDateFormat tody = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                                                    tody.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                    final String savecurrentdate_order = tody.format(calForDate_order.getTime());

                                                                    Date todyDate = tody.parse(savecurrentdate_order);
                                                                    if (todyDate.after(next_date)) {
                                                                        //Toast.makeText(MainActivity.this, "nex Day", Toast.LENGTH_LONG).show();
                                                                        PostsRef.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()){
                                                                                    if (dataSnapshot.hasChild("disLikesPosts") && dataSnapshot.hasChild("LikesPosts")){
                                                                                        int dislikes_count = (int) dataSnapshot.child("disLikesPosts").getChildrenCount();
                                                                                        int likes_count = (int) dataSnapshot.child("LikesPosts").getChildrenCount();
                                                                                        //Toast.makeText(MainActivity.this, "likes = " + Integer.toString(likes_count) + ",  dislikes = " + Integer.toString(dislikes_count), Toast.LENGTH_LONG).show();
                                                                                        if (dislikes_count > likes_count) {
                                                                                            //PostsRef.child(postKey).removeValue();
                                                                                            //GoldPostssRef.child(postKey).removeValue();
                                                                                            StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageUrl);
                                                                                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    // File deleted successfully
                                                                                                    PostsRef.child(postKey).removeValue();
                                                                                                    GoldPostssRef.child(postKey).removeValue();
                                                                                                }
                                                                                            });
                                                                                        } else {
                                                                                            Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                                                                            //calForDate_order.add(Calendar.HOUR, 2);
                                                                                            SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                                                                            currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                                            calForDate_order.add(Calendar.DATE, 1);
                                                                                            String saveNextDay = currentDate_order.format(calForDate_order.getTime());
                                                                                            SimpleDateFormat tody = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                                                                            tody.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                                            String saveNextDay_seconds = tody.format(next_date);
                                                                                            String last_next_date = saveNextDay + " " + saveNextDay_seconds;
                                                                                            PostsRef.child(postKey).child("next_date_time").setValue(last_next_date);
                                                                                        }

                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                        dislikeChecker = false;
                                                        //likePostButton.setImageResource(R.drawable.like);
                                                        //display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        PostsRef.addValueEventListener(PostsRefValue);
                                    }
                                }
                            });

                            holder.mview.findViewById(R.id.gold_post_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        goldPostChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        GoldPostssRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(goldPostChecker.equals(true)){
                                                    //if(dataSnapshot.child(postKey).hasChild("LikesGoldPosts")){
                                                        if(dataSnapshot.child(postKey).child("LikesGoldPosts").hasChild(currentUserId)){
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").child(currentUserId).removeValue();
                                                            GoldPostssRef.child(postKey).child("post_key").setValue(postKey);
                                                            GoldPostssRef.child(postKey).child("post_uid").setValue(post_uid);
                                                            useresRef.child(currentUserId).child("Notifications").child("GoldPosts").child(postKey).removeValue();
                                                            GoldPostssRefValue = new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int count_gold = (int) dataSnapshot.getChildrenCount();
                                                                    if (count_gold == 0){
                                                                        GoldPostssRef.child(postKey).removeValue();
                                                                    } else {
                                                                        GoldPostssRef.child(postKey).child("count_gold").setValue(Integer.toString(count_gold));
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            };
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").addListenerForSingleValueEvent(GoldPostssRefValue);
                                                            goldPostChecker = false;
                                                        } else {
                                                            HashMap likegoldpostMap = new HashMap();
                                                            likegoldpostMap.put("order_date",order_date);
                                                            likegoldpostMap.put("date_time",date_times);
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").child(currentUserId).updateChildren(likegoldpostMap);
                                                            GoldPostssRef.child(postKey).child("post_key").setValue(postKey);
                                                            GoldPostssRef.child(postKey).child("post_uid").setValue(post_uid);
                                                            HashMap likegoldpostNotifMap = new HashMap();
                                                            likegoldpostNotifMap.put("order_date",order_date);
                                                            likegoldpostNotifMap.put("date_time",date_times);
                                                            useresRef.child(currentUserId).child("Notifications").child("GoldPosts").child(postKey).updateChildren(likegoldpostNotifMap);
                                                            GoldPostssRefValue = new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int count_gold = (int) dataSnapshot.getChildrenCount();
                                                                    GoldPostssRef.child(postKey).child("count_gold").setValue(Integer.toString(count_gold));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            };
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").addListenerForSingleValueEvent(GoldPostssRefValue);
                                                            goldPostChecker = false;
                                                        }
                                                    /*} else {
                                                        GoldPostssRef.child(postKey).removeValue();
                                                    }*/
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        GoldPostssRef.addValueEventListener(GoldPostssRefValue);
                                    }
                                }
                            });
                            holder.mview.findViewById(R.id.display_no_of_likes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("LikesPosts")){
                                                int count = (int) dataSnapshot.child("LikesPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowLikesPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });

                            holder.mview.findViewById(R.id.display_no_of_dislikes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("disLikesPosts")){
                                                int count = (int) dataSnapshot.child("disLikesPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowdisLikesPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            holder.mview.findViewById(R.id.display_no_of_gold_post).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("GoldPosts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("LikesGoldPosts")){
                                                int count = (int) dataSnapshot.child("LikesGoldPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowGoldPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });

                            holder.mview.findViewById(R.id.display_no_of_comment).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("Maincomments")){
                                                int count = (int) dataSnapshot.child("Maincomments").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowCommentsPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            holder.mview.findViewById(R.id.post_profile_image).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (currentUserId.equals(post_uid)){
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                        intent.putExtra("visit_user_id", post_uid);
                                        startActivity(intent);
                                    }
                                }
                            });
                            holder.mview.findViewById(R.id.post_user_name).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (currentUserId.equals(post_uid)){
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                        intent.putExtra("visit_user_id", post_uid);
                                        startActivity(intent);
                                    }
                                }
                            });
                            //MainCommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Maincomments");
                            home_comments_list = (RecyclerView) holder.mview.findViewById(R.id.post_home_comments_list);
                            LinearLayoutManager linearLayoutManagerc = new LinearLayoutManager(MainActivity.this);
                            linearLayoutManagerc.setReverseLayout(true);
                            linearLayoutManagerc.setStackFromEnd(true);
                            holder.mview.findViewById(R.id.all_post_layout_allcont).setVisibility(View.VISIBLE);
                            home_comments_list.setLayoutManager(linearLayoutManagerc);
                            //DisplayAllPostComment(postKey, post_uid);
                            holder.DisplayAllPostComment(postKey,post_uid);

                        } else if(currentUserId.equals(post_uid)){
                            holder.setDateandtime(model.getDateandtime());
                            //holder.setDate(model.getDate());
                            //holder.setTime(model.getTime());
                            holder.setDescription(model.getDescription());
                            holder.setPostimage(getApplicationContext(), model.getPostimage());
                            //holder.setProfileimage(getApplicationContext(), model.getProfileimage());
                            holder.setUid(getApplicationContext(),model.getUid());
                            holder.setLikeButtonStatus(postKey);
                            holder.setdisLikeButtonStatus(postKey);
                            holder.setDisplay_comments(postKey);
                            holder.setGoldPostsButtonStatus(postKey, post_uid);

                            Button editPost = (Button) holder.mview.findViewById(R.id.edit_post_button_allpost);
                            Button deletePost = (Button) holder.mview.findViewById(R.id.delete_post_button_allpost);
                            final String uidPost = model.getUid();
                            if(currentUserId.equals(uidPost)){
                                editPost.setVisibility(View.VISIBLE);
                                deletePost.setVisibility(View.VISIBLE);
                            } else {
                                editPost.setVisibility(View.INVISIBLE);
                                deletePost.setVisibility(View.GONE);
                            }

                            if (!currentUserId.equals(uidPost)){
                                FriendsSignRefValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            if (dataSnapshot.hasChild(uidPost)){
                                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.VISIBLE);
                                            } else {
                                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                FriendsSignRef.child(currentUserId).child("Friends").addValueEventListener(FriendsSignRefValue);
                            } else {
                                holder.mview.findViewById(R.id.post_friend_sign).setVisibility(View.GONE);
                            }
                            holder.mview.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    transfer = "true";
                                    Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                    clickpostIntent.putExtra("PostKey", postKey);
                                    clickpostIntent.putExtra("Activity", "main");
                                    clickpostIntent.putExtra("PostUid", post_uid);
                                    clickpostIntent.putExtra("current_user_id", currentUserId);
                                    startActivity(clickpostIntent);
                                }
                            });
                            holder.mview.findViewById(R.id.comment_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    transfer = "true";
                                    Intent clickpostIntent = new Intent(MainActivity.this, ClickPostActivity.class);
                                    clickpostIntent.putExtra("PostKey", postKey);
                                    clickpostIntent.putExtra("Activity", "main");
                                    clickpostIntent.putExtra("PostUid", post_uid);
                                    clickpostIntent.putExtra("current_user_id", currentUserId);
                                    startActivity(clickpostIntent);
                                }
                            });



                            holder.mview.findViewById(R.id.edit_post_button_allpost).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditCurrentPost(description, postKey);
                                }
                            });

                            holder.mview.findViewById(R.id.delete_post_button_allpost).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DeleteCurrentPost(postKey,imageUrl);
                                }
                            });
                            holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        likeChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        PostsRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(likeChecker.equals(true)){
                                                    if(dataSnapshot.child(postKey).child("LikesPosts").hasChild(currentUserId)){
                                                        PostsRef.child(postKey).child("LikesPosts").child(currentUserId).removeValue();
                                                        useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).removeValue();
                                                        likeChecker = false;
                                                    } else {
                                                        HashMap likepostMap = new HashMap();
                                                        likepostMap.put("order_date",order_date);
                                                        likepostMap.put("date_time",date_times);
                                                        PostsRef.child(postKey).child("LikesPosts").child(currentUserId).updateChildren(likepostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).removeValue();
                                                                HashMap likespostMap = new HashMap();
                                                                likespostMap.put("order_date",order_date);
                                                                likespostMap.put("date_time",date_times);
                                                                useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).updateChildren(likespostMap);
                                                                useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).removeValue();
                                                                holder.dislikePostButton.setImageResource(R.drawable.dislike_before);
                                                            }
                                                        });
                                                        likeChecker = false;
                                                        //likePostButton.setImageResource(R.drawable.like);
                                                        //display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        PostsRef.addValueEventListener(PostsRefValue);
                                    }
                                }
                            });

                            holder.dislikePostButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        dislikeChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        PostsRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dislikeChecker.equals(true)){
                                                    if(dataSnapshot.child(postKey).child("disLikesPosts").hasChild(currentUserId)){
                                                        PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).removeValue();
                                                        useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).removeValue();
                                                        dislikeChecker = false;
                                                    } else {
                                                        HashMap dislikepostMap = new HashMap();
                                                        dislikepostMap.put("order_date",order_date);
                                                        dislikepostMap.put("date_time",date_times);
                                                        PostsRef.child(postKey).child("disLikesPosts").child(currentUserId).updateChildren(dislikepostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                PostsRef.child(postKey).child("LikesPosts").child(currentUserId).removeValue();
                                                                useresRef.child(currentUserId).child("Notifications").child("LikePosts").child(postKey).removeValue();
                                                                HashMap dislikespostMap = new HashMap();
                                                                dislikespostMap.put("order_date",order_date);
                                                                dislikespostMap.put("date_time",date_times);
                                                                useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).updateChildren(dislikespostMap);
                                                                holder.likePostButton.setImageResource(R.drawable.dislike);
                                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                                                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                //Toast.makeText(MainActivity.this, "nex Day", Toast.LENGTH_LONG).show();
                                                                try {
                                                                    final Date next_date = dateFormat.parse(next_day);

                                                                    Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                                                    //calForDate_order.add(Calendar.HOUR, 2);
                                                                    SimpleDateFormat tody = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                                                                    tody.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                    final String savecurrentdate_order = tody.format(calForDate_order.getTime());

                                                                    Date todyDate = tody.parse(savecurrentdate_order);
                                                                    if (todyDate.after(next_date)) {
                                                                        //Toast.makeText(MainActivity.this, "nex Day", Toast.LENGTH_LONG).show();
                                                                        PostsRef.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()){
                                                                                    if (dataSnapshot.hasChild("disLikesPosts") && dataSnapshot.hasChild("LikesPosts")){
                                                                                        int dislikes_count = (int) dataSnapshot.child("disLikesPosts").getChildrenCount();
                                                                                        int likes_count = (int) dataSnapshot.child("LikesPosts").getChildrenCount();
                                                                                        //Toast.makeText(MainActivity.this, "likes = " + Integer.toString(likes_count) + ",  dislikes = " + Integer.toString(dislikes_count), Toast.LENGTH_LONG).show();
                                                                                        if (dislikes_count > likes_count) {
                                                                                            //PostsRef.child(postKey).removeValue();
                                                                                            //GoldPostssRef.child(postKey).removeValue();
                                                                                            StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageUrl);
                                                                                            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    // File deleted successfully
                                                                                                    PostsRef.child(postKey).removeValue();
                                                                                                    GoldPostssRef.child(postKey).removeValue();
                                                                                                }
                                                                                            });
                                                                                        } else {
                                                                                            Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                                                                            //calForDate_order.add(Calendar.HOUR, 2);
                                                                                            SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                                                                            currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                                            calForDate_order.add(Calendar.DATE, 1);
                                                                                            String saveNextDay = currentDate_order.format(calForDate_order.getTime());
                                                                                            SimpleDateFormat tody = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                                                                            tody.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                                                            String saveNextDay_seconds = tody.format(next_date);
                                                                                            String last_next_date = saveNextDay + " " + saveNextDay_seconds;
                                                                                            PostsRef.child(postKey).child("next_date_time").setValue(last_next_date);
                                                                                        }

                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }
                                                        });
                                                        dislikeChecker = false;
                                                        //likePostButton.setImageResource(R.drawable.like);
                                                        //display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        PostsRef.addValueEventListener(PostsRefValue);
                                    }
                                }
                            });

                            holder.mview.findViewById(R.id.gold_post_button).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!currentUserId.equals(uidPost)) {
                                        goldPostChecker = true;
                                        final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForDate_order.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        //calForTimesecond.add(Calendar.HOUR, 2);
                                        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                        final String order_date = currentorderDate.format(calFororderDate.getTime());

                                        date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                        GoldPostssRefValue = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(goldPostChecker.equals(true)){
                                                    //if(dataSnapshot.child(postKey).hasChild("LikesGoldPosts")){
                                                        if(dataSnapshot.child(postKey).child("LikesGoldPosts").hasChild(currentUserId)){
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").child(currentUserId).removeValue();
                                                            GoldPostssRef.child(postKey).child("post_key").setValue(postKey);
                                                            GoldPostssRef.child(postKey).child("post_uid").setValue(post_uid);
                                                            useresRef.child(currentUserId).child("Notifications").child("GoldPosts").child(postKey).removeValue();

                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int count_gold = (int) dataSnapshot.getChildrenCount();
                                                                    if (count_gold == 0){
                                                                        GoldPostssRef.child(postKey).removeValue();
                                                                    } else {
                                                                        GoldPostssRef.child(postKey).child("count_gold").setValue(Integer.toString(count_gold));
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                            goldPostChecker = false;
                                                        } else {
                                                            HashMap likegoldpostMap = new HashMap();
                                                            likegoldpostMap.put("order_date",order_date);
                                                            likegoldpostMap.put("date_time",date_times);
                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").child(currentUserId).updateChildren(likegoldpostMap);
                                                            GoldPostssRef.child(postKey).child("post_key").setValue(postKey);
                                                            GoldPostssRef.child(postKey).child("post_uid").setValue(post_uid);
                                                            HashMap likesgoldpostMap = new HashMap();
                                                            likesgoldpostMap.put("order_date",order_date);
                                                            likesgoldpostMap.put("date_time",date_times);
                                                            useresRef.child(currentUserId).child("Notifications").child("GoldPosts").child(postKey).updateChildren(likesgoldpostMap);

                                                            GoldPostssRef.child(postKey).child("LikesGoldPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    int count_gold = (int) dataSnapshot.getChildrenCount();
                                                                    GoldPostssRef.child(postKey).child("count_gold").setValue(Integer.toString(count_gold));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                            goldPostChecker = false;
                                                        }
                                                    /*} else {
                                                        GoldPostssRef.child(postKey).removeValue();
                                                    }*/
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        };
                                        GoldPostssRef.addValueEventListener(GoldPostssRefValue);
                                    }
                                }
                            });

                            holder.mview.findViewById(R.id.display_no_of_likes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("LikesPosts")){
                                                int count = (int) dataSnapshot.child("LikesPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowLikesPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });

                            holder.mview.findViewById(R.id.display_no_of_dislikes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("disLikesPosts")){
                                                int count = (int) dataSnapshot.child("disLikesPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowdisLikesPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            holder.mview.findViewById(R.id.display_no_of_gold_post).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("GoldPosts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("LikesGoldPosts")){
                                                int count = (int) dataSnapshot.child("LikesGoldPosts").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowGoldPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            holder.mview.findViewById(R.id.display_no_of_comment).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                                    PostsRefss.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild("Maincomments")){
                                                int count = (int) dataSnapshot.child("Maincomments").getChildrenCount();
                                                if (count > 0){
                                                    transfer = "true";
                                                    Intent clickpostIntent = new Intent(MainActivity.this, ShowCommentsPostsUsersActivity.class);
                                                    clickpostIntent.putExtra("PostKey", postKey);
                                                    //clickpostIntent.putExtra("Activity", "main");
                                                    clickpostIntent.putExtra("PostUid", post_uid);
                                                    //clickpostIntent.putExtra("current_user_id", currentUserId);
                                                    startActivity(clickpostIntent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            holder.mview.findViewById(R.id.post_profile_image).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (currentUserId.equals(post_uid)){
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                        intent.putExtra("visit_user_id", post_uid);
                                        startActivity(intent);
                                    }
                                }
                            });
                            holder.mview.findViewById(R.id.post_user_name).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (currentUserId.equals(post_uid)){
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                        startActivity(intent);
                                    } else {
                                        transfer = "true";
                                        Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                        intent.putExtra("visit_user_id", post_uid);
                                        startActivity(intent);
                                    }
                                }
                            });
                            //MainCommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Maincomments");
                            home_comments_list = (RecyclerView) holder.mview.findViewById(R.id.post_home_comments_list);
                            LinearLayoutManager linearLayoutManagerc = new LinearLayoutManager(MainActivity.this);
                            linearLayoutManagerc.setReverseLayout(true);
                            linearLayoutManagerc.setStackFromEnd(true);
                            home_comments_list.setLayoutManager(linearLayoutManagerc);
                            //DisplayAllPostComment(postKey, post_uid);
                            holder.DisplayAllPostComment(postKey,post_uid);
                            //loadingBar.dismiss();
                            //waitingBar.dismiss();
                            //dialogs.dismiss();
                            /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }*/
                            holder.mview.findViewById(R.id.all_post_layout_allcont).setVisibility(View.VISIBLE);
                        } else {
                            holder.mview.findViewById(R.id.all_post_layout_allcont).setVisibility(View.GONE);
                            holder.mview.findViewById(R.id.all_posts_continer).setVisibility(View.GONE);
                            //loadingBar.dismiss();
                            //waitingBar.dismiss();
                            //dialogs.dismiss();
                            /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }
                            else {
                                //waitingBar.cancel();
                                waitingBar.dismiss();
                            }*/
                        }
                        //loadingBar.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                useres_postRef.child(currentUserId).child("Friends").addValueEventListener(useres_postRefValue);
                ss[0] = "1";
                //mydialog.dismiss();
                //Toast.makeText(MainActivity.this, ss[0], Toast.LENGTH_LONG).show();
            }


        };
        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        PostsRef.orderByChild("order_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(mydialog.isShowing()){
                    mydialog.dismiss();
                }
                String fo = globalVar.getFirst_open();
                if (fo.equals("true")){
                    final String current_user_ids = mAuth.getCurrentUser().getUid();
                    useresRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(current_user_ids).hasChild("fullname")){
                                GetBarakaSkyMessage();
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
        });
        /*PostsRef.orderByChild("order_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //findViewById(R.id.loading_mainactivity).setVisibility(View.GONE);
                //waitingBar.dismiss();
                //Toast.makeText(MainActivity.this, "finished", Toast.LENGTH_LONG).show();
                //mydialog.dismiss();
                int count = (int)dataSnapshot.getChildrenCount();
                if (count == 0){
                    //mydialog.dismiss();
                    //nodata.setVisibility(View.VISIBLE);
                    //postList.setVisibility(View.GONE);
                } else {
                    //nodata.setVisibility(View.GONE);
                    //postList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        updateUserState("Online");
        /*if (ss[0].equals("0")){
            loadingBar.dismiss();
        }*/
    }


    public class PostViewHolder extends RecyclerView.ViewHolder{
        View mview;

        ImageButton likePostButton, dislikePostButton, commentPostButton, goldPostButton;
        TextView display_likes, display_dislikes, display_comments, display_gold_posts;
        int countLikes, countdisLikes,countComm, countGoldPosts;
        String currentUserIdh;
        DatabaseReference LikeRefs, CommentsRefs, MainsCommentsRefsc, GoldPostsRef;
        public FirebaseRecyclerAdapter firebaseRecyclerAdapter_maincommentsc;
        public PostViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            likePostButton = (ImageButton) mview.findViewById(R.id.like_button);
            dislikePostButton = (ImageButton) mview.findViewById(R.id.dislike_button);
            commentPostButton = (ImageButton) mview.findViewById(R.id.comment_button);
            goldPostButton = (ImageButton) mview.findViewById(R.id.gold_post_button);
            display_likes = (TextView) mview.findViewById(R.id.display_no_of_likes);
            display_dislikes = (TextView) mview.findViewById(R.id.display_no_of_dislikes);
            display_comments = (TextView) mview.findViewById(R.id.display_no_of_comment);
            display_gold_posts = (TextView) mview.findViewById(R.id.display_no_of_gold_post);
            LikeRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
            CommentsRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
            GoldPostsRef = FirebaseDatabase.getInstance().getReference().child("GoldPosts");
            currentUserIdh = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void DisplayAllPostComment(final String p_key, final String p_uid) {
            //if(MainCommentsRef.equals("")){

            //}
            MainsCommentsRefsc = FirebaseDatabase.getInstance().getReference().child("Posts").child(p_key).child("Maincomments");
            FirebaseRecyclerOptions<MainComments> options = new FirebaseRecyclerOptions.Builder<MainComments>().setQuery(MainsCommentsRefsc.orderByChild("order_date").limitToLast(2), MainComments.class).build();

            firebaseRecyclerAdapter_maincommentsc = new FirebaseRecyclerAdapter<MainComments, MainActivity.MainCommentsViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final MainCommentsViewHolder holder, int position, @NonNull MainComments model) {
                    final String maincommentsKey = getRef(position).getKey();
                    final String comm_desc = model.getComment();

//                if(home_comments_list.equals("")){
//                    home_comments_list = (RecyclerView) holder.mview.findViewById(R.id.post_home_comments_list);
//                }



                    //holder.setDate(model.getDate());
                    //holder.setTime(model.getTime());
                    holder.setDate_time(model.getDate_time());
                    holder.setComment(model.getComment());
                    holder.setUid(getApplicationContext(),model.getUid());
                    holder.setLikeMainCommentButtonStatus(p_key,maincommentsKey);
                    holder.setDisplay_main_comments(p_key,maincommentsKey);

                    Button editMainComments = (Button) holder.mview.findViewById(R.id.edit_home_comment_button_allcomments);
                    Button deleteMainComments = (Button) holder.mview.findViewById(R.id.delete_home_comment_button_allcomments);
                    final String uidMainComments = model.getUid();
                    if(currentUserId.equals(uidMainComments)){
                        editMainComments.setVisibility(View.VISIBLE);
                        deleteMainComments.setVisibility(View.VISIBLE);
                    } else {
                        if(currentUserId.equals(p_uid)){
                            deleteMainComments.setVisibility(View.VISIBLE);
                        } else {
                            deleteMainComments.setVisibility(View.GONE);
                        }
                        editMainComments.setVisibility(View.INVISIBLE);

                    }

                    if (!currentUserId.equals(uidMainComments)){
                        FriendsSignRefValue = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.hasChild(uidMainComments)){
                                        holder.mview.findViewById(R.id.post_comment_friend_sign).setVisibility(View.VISIBLE);
                                    } else {
                                        holder.mview.findViewById(R.id.post_comment_friend_sign).setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.mview.findViewById(R.id.post_comment_friend_sign).setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        FriendsSignRef.child(currentUserId).child("Friends").addValueEventListener(FriendsSignRefValue);
                    } else {
                        holder.mview.findViewById(R.id.post_comment_friend_sign).setVisibility(View.GONE);
                    }


                    holder.mview.findViewById(R.id.edit_home_comment_button_allcomments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditCurrentMainComments(comm_desc,p_key,maincommentsKey);
                        }
                    });
                    holder.mview.findViewById(R.id.delete_home_comment_button_allcomments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DeleteCurrentMainComments(p_key,maincommentsKey);
                        }
                    });
                    holder.likeMainCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!currentUserId.equals(uidMainComments)) {
                                likeChecker = true;
                                final String savecurrentdate_orders,savecurrenttimeseconds,date_times;

                                Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                //calForDate_order.add(Calendar.HOUR, 2);
                                SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                savecurrentdate_orders = currentDate_order.format(calForDate_order.getTime());

                                Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                //calForTimesecond.add(Calendar.HOUR, 2);
                                SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                                Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                final String order_date = currentorderDate.format(calFororderDate.getTime());

                                date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                MainsCommentsRefsc.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(likeChecker.equals(true)){
                                            if(dataSnapshot.child(maincommentsKey).child("LikesMainComments").hasChild(currentUserId)){
                                                MainsCommentsRefsc.child(maincommentsKey).child("LikesMainComments").child(currentUserId).removeValue();
                                                likeChecker = false;
                                            } else {
                                                HashMap likemiancommentMap = new HashMap();
                                                likemiancommentMap.put("order_date",order_date);
                                                likemiancommentMap.put("date_time",date_times);
                                                MainsCommentsRefsc.child(maincommentsKey).child("LikesMainComments").child(currentUserId).updateChildren(likemiancommentMap);
                                                likeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    });
                    holder.mview.findViewById(R.id.display_no_of_likes_home_comments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                            PostsRefss.child(p_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Maincomments").child(maincommentsKey).hasChild("LikesMainComments")){
                                        int count = (int) dataSnapshot.child("Maincomments").child(maincommentsKey).child("LikesMainComments").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(MainActivity.this, ShowLikeMainCommentsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", p_key);
                                            clickpostIntent.putExtra("cPost_key",maincommentsKey );
                                            clickpostIntent.putExtra("PostUid", uidMainComments);
                                            //clickpostIntent.putExtra("current_user_id", currentUserId);
                                            startActivity(clickpostIntent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    holder.mview.findViewById(R.id.display_no_of_home_comment_comments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                            PostsRefss.child(p_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Maincomments").child(maincommentsKey).hasChild("Replays")){
                                        int count = (int) dataSnapshot.child("Maincomments").child(maincommentsKey).child("Replays").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(MainActivity.this, ShowReplaysMainCommentsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", p_key);
                                            clickpostIntent.putExtra("cPost_key",maincommentsKey );
                                            clickpostIntent.putExtra("PostUid", uidMainComments);
                                            //clickpostIntent.putExtra("current_user_id", currentUserId);
                                            startActivity(clickpostIntent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                    holder.mview.findViewById(R.id.all_users_profile_image_home_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentUserId.equals(uidMainComments)){
                                transfer = "true";
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", uidMainComments);
                                startActivity(intent);
                            }

                        }
                    });
                    holder.mview.findViewById(R.id.all_users_profile_full_name_home_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentUserId.equals(uidMainComments)){
                                transfer = "true";
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(MainActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", uidMainComments);
                                startActivity(intent);
                            }
                        }
                    });
                }

                @NonNull
                @Override
                public MainCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.all_home_comments_layout_normal, parent, false);
                    return new MainActivity.MainCommentsViewHolder(view);
                }

            };
            //home_comments_list.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManagerc = new LinearLayoutManager(this);
//        linearLayoutManagerc.setReverseLayout(true);
//        linearLayoutManagerc.setStackFromEnd(true);
//        home_comments_list.setLayoutManager(linearLayoutManagerc);
            //firebaseRecyclerAdapter_maincomments.startListening();
            firebaseRecyclerAdapter_maincommentsc.startListening();
            home_comments_list.setAdapter(firebaseRecyclerAdapter_maincommentsc);
            ///54
        }

        public void setLikeButtonStatus(final String PostKey){
            final int final_count = 10000;
            LikeRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).child("LikesPosts").hasChild(currentUserIdh)){
                        countLikes = (int) dataSnapshot.child(PostKey).child("LikesPosts").getChildrenCount();
                        likePostButton.setImageResource(R.drawable.like);
                        if (countLikes < final_count) {
                            display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (countLikes >= final_count) {
                            display_likes.setText((Integer.toString(countLikes)));
                        }

                    } else {
                        countLikes = (int) dataSnapshot.child(PostKey).child("LikesPosts").getChildrenCount();
                        likePostButton.setImageResource(R.drawable.dislike);
                        if (countLikes < final_count) {
                            display_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (countLikes >= final_count) {
                            display_likes.setText((Integer.toString(countLikes)));
                        };
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        public void setdisLikeButtonStatus(final String PostKey){
            final int final_count = 10000;
            LikeRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).child("disLikesPosts").hasChild(currentUserIdh)){
                        countdisLikes = (int) dataSnapshot.child(PostKey).child("disLikesPosts").getChildrenCount();
                        dislikePostButton.setImageResource(R.drawable.dislike_after);
                        if (countdisLikes < final_count) {
                            display_dislikes.setText((Integer.toString(countdisLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_dislike));
                        } else if (countdisLikes >= final_count) {
                            display_dislikes.setText((Integer.toString(countdisLikes)));
                        }
                    } else {
                        countdisLikes = (int) dataSnapshot.child(PostKey).child("disLikesPosts").getChildrenCount();
                        dislikePostButton.setImageResource(R.drawable.dislike_before);
                        if (countdisLikes < final_count) {
                            display_dislikes.setText((Integer.toString(countdisLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_dislike));
                        } else if (countdisLikes >= final_count) {
                            display_dislikes.setText((Integer.toString(countdisLikes)));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setGoldPostsButtonStatus(final String PostKey, final String currentUsID){
            final int final_count = 10000;
            GoldPostsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(PostKey)){
                        if(dataSnapshot.child(PostKey).child("LikesGoldPosts").hasChild(currentUsID)){
                            countGoldPosts = (int) dataSnapshot.child(PostKey).child("LikesGoldPosts").getChildrenCount();
                            goldPostButton.setImageResource(R.drawable.gold_star_after);
                            display_gold_posts.setText((Integer.toString(countGoldPosts)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                            if (countGoldPosts < final_count) {
                                display_gold_posts.setText((Integer.toString(countGoldPosts)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                            } else if (countGoldPosts >= final_count) {
                                display_gold_posts.setText((Integer.toString(countGoldPosts)));
                            }
                        } else {
                            countGoldPosts = (int) dataSnapshot.child(PostKey).child("LikesGoldPosts").getChildrenCount();
                            goldPostButton.setImageResource(R.drawable.gold_star_before);
                            if (countGoldPosts < final_count) {
                                display_gold_posts.setText((Integer.toString(countGoldPosts)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                            } else if (countGoldPosts >= final_count) {
                                display_gold_posts.setText((Integer.toString(countGoldPosts)));
                            }
                        }
                    } else {
                        countGoldPosts = 0;
                        goldPostButton.setImageResource(R.drawable.gold_star_before);
                        if (countGoldPosts < final_count) {
                            display_gold_posts.setText((Integer.toString(countGoldPosts)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                        } else if (countGoldPosts >= final_count) {
                            display_gold_posts.setText((Integer.toString(countGoldPosts)));
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void setDisplay_comments(final String postkey){
            final int final_count = 10000;
            CommentsRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postkey).child("Maincomments").hasChildren()){
                        countComm = (int)dataSnapshot.child(postkey).child("Maincomments").getChildrenCount();
                        if (countComm < final_count) {
                            display_comments.setText((Integer.toString(countComm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment));
                        } else if (countComm >= final_count) {
                            display_comments.setText((Integer.toString(countComm)));
                        }
                    } else {
                        countComm = 0;
                        display_comments.setText((Integer.toString(countComm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setTime(String time){
            TextView posttime = (TextView) mview.findViewById(R.id.post_time);
            posttime.setText(time);
        }
        public void setDate(String date){
            TextView postdate = (TextView) mview.findViewById(R.id.post_date);
            postdate.setText(date);
        }
        public void setDateandtime(String dateandtime){
            TextView postdate = (TextView) mview.findViewById(R.id.post_date);
            //String la = Locale.getDefault().getDisplayLanguage();
            GetUserDateTime getUserDateTime = new GetUserDateTime(MainActivity.this);
            String date_value = getUserDateTime.getDateToShow(dateandtime, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            postdate.setText(date_value);
        }
        public void setDescription(String description){
            TextView postdesc = (TextView) mview.findViewById(R.id.post_desc);
            postdesc.setText(description);
        }
        public void setPostimage(Context ctx, String postimage){
            ImageView image_post = (ImageView) mview.findViewById(R.id.post_image);
            Picasso.with(ctx).load(postimage).into(image_post);
        }

        public void setUid(final Context ctx, String uid){
            DatabaseReference useresRefpost = FirebaseDatabase.getInstance().getReference().child("Users");


            useresRefpost.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                            String full_Name = dataSnapshot.child("fullname").getValue().toString();
                            TextView username = (TextView) mview.findViewById(R.id.post_user_name);
                            username.setText(full_Name);

                            String img = dataSnapshot.child("profileimage").getValue().toString();
                            CircleImageView image = (CircleImageView) mview.findViewById(R.id.post_profile_image);
                            Picasso.with(ctx).load(img).into(image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public class MainCommentsViewHolder extends RecyclerView.ViewHolder{
        View mview;

        ImageButton likeMainCommentButton, commentButton;
        TextView display_main_comments_likes, display_main_comments;
        int count_main_comments_Likes,count_main_Comm;
        String currentUserIdh;
        DatabaseReference Like_main_comments_Refs, MainsCommentsRefs;

        public MainCommentsViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            likeMainCommentButton = (ImageButton) mview.findViewById(R.id.like_button_home_comment);
            //commentButton = (ImageButton) mview.findViewById(R.id.comment_button_comments);
            display_main_comments_likes = (TextView) mview.findViewById(R.id.display_no_of_likes_home_comments);
            display_main_comments = (TextView) mview.findViewById(R.id.display_no_of_home_comment_comments);
            Like_main_comments_Refs = FirebaseDatabase.getInstance().getReference().child("Posts");
            MainsCommentsRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserIdh = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }




        public void setLikeMainCommentButtonStatus(final String PostKey,final String maincommentKey){
            final int final_count = 10000;
            Like_main_comments_Refs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("LikesMainComments").hasChild(currentUserIdh)){
                        count_main_comments_Likes = (int) dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("LikesMainComments").getChildrenCount();
                        likeMainCommentButton.setImageResource(R.drawable.like);
                        if (count_main_comments_Likes < final_count) {
                            display_main_comments_likes.setText((Integer.toString(count_main_comments_Likes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (count_main_comments_Likes >= final_count) {
                            display_main_comments_likes.setText((Integer.toString(count_main_comments_Likes)));
                        }
                    } else {
                        count_main_comments_Likes = (int) dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("LikesMainComments").getChildrenCount();
                        likeMainCommentButton.setImageResource(R.drawable.dislike);
                        if (count_main_comments_Likes < final_count) {
                            display_main_comments_likes.setText((Integer.toString(count_main_comments_Likes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (count_main_comments_Likes >= final_count) {
                            display_main_comments_likes.setText((Integer.toString(count_main_comments_Likes)));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setDisplay_main_comments(final String postkey,final String maincommentKey){
            final int final_count = 10000;
            MainsCommentsRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postkey).child("Maincomments").child(maincommentKey).child("Replays").hasChildren()){
                        count_main_Comm = (int)dataSnapshot.child(postkey).child("Maincomments").child(maincommentKey).child("Replays").getChildrenCount();
                        if (count_main_Comm < final_count) {
                            display_main_comments.setText((Integer.toString(count_main_Comm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment_replay));
                        } else if (count_main_Comm >= final_count) {
                            display_main_comments.setText((Integer.toString(count_main_Comm)));
                        }
                    } else {
                        count_main_Comm = 0;
                        display_main_comments.setText((Integer.toString(count_main_Comm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment_replay));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setTime(String time){
            TextView commenttime = (TextView) mview.findViewById(R.id.home_comment_time);
            commenttime.setText(time);
        }
        public void setDate(String date){
            TextView commentdate = (TextView) mview.findViewById(R.id.home_comment_date);
            commentdate.setText(date);
        }
        public void setComment(String comment){
            TextView comm = (TextView) mview.findViewById(R.id.home_comment_desc);
            comm.setText(comment);
        }

        public void setUid(final Context ctx, String uid){
            DatabaseReference useresRefpost = FirebaseDatabase.getInstance().getReference().child("Users");


            useresRefpost.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String full_Name = dataSnapshot.child("fullname").getValue().toString();
                        TextView username_main_comments = (TextView) mview.findViewById(R.id.all_users_profile_full_name_home_comment);
                        username_main_comments.setText(full_Name);

                        String img_main_comments = dataSnapshot.child("profileimage").getValue().toString();
                        CircleImageView image = (CircleImageView) mview.findViewById(R.id.all_users_profile_image_home_comment);
                        Picasso.with(ctx).load(img_main_comments).into(image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        public void setDate_time(String date_time){
            TextView commentdate = (TextView) mview.findViewById(R.id.home_comment_date);
            GetUserDateTime getUserDateTime = new GetUserDateTime(MainActivity.this);
            String date_value = getUserDateTime.getDateToShow(date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            commentdate.setText(date_value);

        }
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
        Users_onlineRef.child(currentUserId).updateChildren(hashMap);
    }
    private void EditCurrentPost(String descriptions, final String post_key) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_post_dailog_title));
        final EditText inputFaild = new EditText(MainActivity.this);
        inputFaild.setText(descriptions);
        inputFaild.setPadding(5, 5, 5, 5);
        inputFaild.setTextSize(15);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.setMargins(10,10,10,10);
        //inputFaild.setLayoutParams(params);
        inputFaild.setBackgroundResource(R.drawable.inputs);
        builder.setView(inputFaild);
        builder.setPositiveButton(getResources().getString(R.string.click_post_page_edit_post_dailog_update_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(inputFaild.getText().toString())){
                    DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key);
                    clickpostRef.child("description").setValue(inputFaild.getText().toString());
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.click_post_page_edit_post_dailog_cancle_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //FrameLayout mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        //FrameLayout mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        //FrameLayout mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        mDialogtitle.setText(getResources().getString(R.string.click_post_page_edit_post_dailog_title));
        mDialogmessage.setVisibility(View.INVISIBLE);
        mDialogdescription.setVisibility(View.VISIBLE);
        mDialogdescription.setText(descriptions);
        FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
        TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
        mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_edit_post_dailog_cancle_button));
        mDialogcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
        TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
        mDialogok_text.setText(getResources().getString(R.string.click_post_page_edit_post_dailog_update_button));
        mDialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mDialogdescription.getText().toString())){
                    DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key);
                    clickpostRef.child("description").setValue(mDialogdescription.getText().toString());
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void DeleteCurrentPost(final String post_key, final String imageUrls) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_post_dailog_title));
        final TextView inputFaild = new TextView(MainActivity.this);
        inputFaild.setText(getResources().getString(R.string.click_post_page_delete_post_dailog_confirm_message));
        inputFaild.setPadding(5, 5, 5, 5);
        inputFaild.setTextSize(16);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.setMargins(10,10,10,10);
        //inputFaild.setLayoutParams(params);
        //inputFaild.setBackgroundResource(R.drawable.inputs);
        builder.setView(inputFaild);
        builder.setPositiveButton(getResources().getString(R.string.click_post_page_delete_post_dailog_delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key);
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageUrls);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        clickpostRef.removeValue();
                        GoldPostssRef.child(post_key).removeValue();
                        //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_post_deleteted),Toast.LENGTH_SHORT).show();
                        if (la.equals("العربية")){
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_post_deleteted),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                        } else {
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_post_deleteted),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                        }

                    }
                });
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.click_post_page_delete_post_dailog_cancle_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //FrameLayout mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        //FrameLayout mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        //FrameLayout mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        mDialogtitle.setText(getResources().getString(R.string.click_post_page_delete_post_dailog_title));
        mDialogmessage.setVisibility(View.VISIBLE);
        mDialogdescription.setVisibility(View.INVISIBLE);
        mDialogmessage.setText(getResources().getString(R.string.click_post_page_delete_post_dailog_confirm_message));
        FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
        TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
        mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_delete_post_dailog_cancle_button));
        mDialogcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
        TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
        mDialogok_text.setText(getResources().getString(R.string.click_post_page_delete_post_dailog_delete_button));
        mDialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key);
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageUrls);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        clickpostRef.removeValue();
                        GoldPostssRef.child(post_key).removeValue();
                        //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_post_deleteted),Toast.LENGTH_SHORT).show();
                        if (la.equals("العربية")){
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_post_deleteted),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                        } else {
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_post_deleteted),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                        }

                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void EditCurrentMainComments(String descriptions, final String post_key, final String maincommentKey) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_title));
        final EditText inputFaild = new EditText(MainActivity.this);
        inputFaild.setText(descriptions);
        inputFaild.setPadding(5, 5, 5, 5);
        inputFaild.setTextSize(15);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.setMargins(10,10,10,10);
        //inputFaild.setLayoutParams(params);
        inputFaild.setBackgroundResource(R.drawable.inputs);
        builder.setView(inputFaild);
        builder.setPositiveButton(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_update_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(inputFaild.getText().toString())){
                    DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments").child(maincommentKey);
                    clickpostRef.child("comment").setValue(inputFaild.getText().toString());
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_cancle_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        mDialogtitle.setText(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_update_button));
        mDialogmessage.setVisibility(View.INVISIBLE);
        mDialogdescription.setVisibility(View.VISIBLE);
        mDialogdescription.setText(descriptions);
        FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
        TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
        mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_cancle_button));
        mDialogcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
        TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
        mDialogok_text.setText(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_update_button));
        mDialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mDialogdescription.getText().toString())){
                    DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments").child(maincommentKey);
                    clickpostRef.child("comment").setValue(mDialogdescription.getText().toString());
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void DeleteCurrentMainComments(final String post_key, final String maincommentKey) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_title));
        final TextView inputFaild = new TextView(MainActivity.this);
        inputFaild.setText(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_confirm_message));
        inputFaild.setPadding(5, 5, 5, 5);
        inputFaild.setTextSize(16);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //params.setMargins(10,10,10,10);
        //inputFaild.setLayoutParams(params);
        //inputFaild.setBackgroundResource(R.drawable.inputs);
        builder.setView(inputFaild);
        builder.setPositiveButton(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments").child(maincommentKey);
                clickpostRef.removeValue();
                //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_cancle_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        mDialogtitle.setText(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_title));
        mDialogmessage.setVisibility(View.VISIBLE);
        mDialogdescription.setVisibility(View.INVISIBLE);
        mDialogmessage.setText(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_confirm_message));
        FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
        TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
        mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_cancle_button));
        mDialogcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
        TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
        mDialogok_text.setText(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_delete_button));
        mDialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments").child(maincommentKey);
                clickpostRef.removeValue();
                //Toast.makeText(MainActivity.this,getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //outState.putInt(RECYCLER_POSITION_KEY,  linearLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState, outPersistentState);
        mListState = getListView().onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState.containsKey(RECYCLER_POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            // Scroll the RecyclerView to mPosition
            postList.smoothScrollToPosition(mPosition);
        }

        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }*/

    @Override
    protected void onStop() {
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        if (useresRefValue != null && useresRef != null) {
            useresRef.removeEventListener(useresRefValue);
        }
        if (PostsRefValue != null && PostsRef != null) {
            PostsRef.removeEventListener(PostsRefValue);
        }

        if (FriendsSignRefValue != null && FriendsSignRef != null) {
            FriendsSignRef.removeEventListener(FriendsSignRefValue);
        }
        if (useres_postRefValue != null && useres_postRef != null) {
            useres_postRef.removeEventListener(useres_postRefValue);
        }
        if (FriendsRefValues != null && FriendsRef != null) {
            FriendsRef.removeEventListener(FriendsRefValues);
        }
        if (FriendRequestCountSentRefValue != null && FriendRequestCountSentRef != null) {
            FriendRequestCountSentRef.removeEventListener(FriendRequestCountSentRefValue);
        }
        if (FriendRequestCountReciveRefValue != null && FriendRequestCountReciveRef != null) {
            FriendRequestCountReciveRef.removeEventListener(FriendRequestCountReciveRefValue);
        }
        if (AllMessagesRefValue != null && AllMessagesRef != null) {
            AllMessagesRef.removeEventListener(AllMessagesRefValue);
        }
        if (subAllMessagesRefValue != null && subAllMessagesRef != null) {
            subAllMessagesRef.removeEventListener(subAllMessagesRefValue);
        }
        if (BookFriendsRefValue != null && BookFriendsRef != null) {
            BookFriendsRef.removeEventListener(BookFriendsRefValue);
        }
        if (GoldPostssRefValue != null && GoldPostssRef != null) {
            GoldPostssRef.removeEventListener(GoldPostssRefValue);
        }

        if (notif_dislikePostsRefValue != null && notif_dislikePostsRef != null) {
            notif_dislikePostsRef.removeEventListener(notif_dislikePostsRefValue);
        }
        if (notif_likePostsRefValue != null && notif_likePostsRef != null) {
            notif_likePostsRef.removeEventListener(notif_likePostsRefValue);
        }
        if (notif_GoldPostsRefValue != null && notif_GoldPostsRef != null) {
            notif_GoldPostsRef.removeEventListener(notif_GoldPostsRefValue);
        }
        super.onStop();
        Runtime.getRuntime().gc();
        //Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        /*mBundleState = new Bundle();
        mPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        mBundleState.putInt(RECYCLER_POSITION_KEY, mPosition);*/
        /*firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        Toast.makeText(MainActivity.this, "pause", Toast.LENGTH_LONG).show();*/
        //transfer = "false";
        //firebaseRecyclerAdapter_maincomments.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        Toast.makeText(MainActivity.this, "destory", Toast.LENGTH_LONG).show();*/
        //transfer = "false";
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*updateUserState("Online");
        transfer = "false";
        SetCountsOfFriends();
        SetCountOfSentFriendsRequests();
        SetCountOfRecivedFriendsRequests();
        Toast.makeText(MainActivity.this, "restart", Toast.LENGTH_LONG).show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (mBundleState != null) {
            mPosition = mBundleState.getInt(RECYCLER_POSITION_KEY);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            // Scroll the RecyclerView to mPosition
            postList.smoothScrollToPosition(mPosition);
        }*/
        //firebaseRecyclerAdapter.startListening();
        /*updateUserState("Online");
        transfer = "false";
        DisplayAllUsersPosts();
        SetCountsOfFriends();
        SetCountOfSentFriendsRequests();
        SetCountOfRecivedFriendsRequests();
        Toast.makeText(MainActivity.this, "resume", Toast.LENGTH_LONG).show();*/
        //firebaseRecyclerAdapter_maincomments.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                FirebaseUser currntUser = mAuth.getCurrentUser();
                if(currntUser == null){
                    sendUserToLoginActivaty();
                } else {
                    CheckUserExistance();
                }

                /*LangRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChild("language")){
                            String mylan = dataSnapshot.child("language").getValue().toString();*/
                    /*MyAppLanguage myAppLanguage = new MyAppLanguage();
                    myAppLanguage.setMyAppLanguage(MainActivity.this, mylan);
                    Toast.makeText(MainActivity.this, "from " + mylan, Toast.LENGTH_LONG).show();*/
                            /*String nlan;
                            if (mylan.equals("Arabic")){
                                nlan = "ar-rEG";
                            } else {
                                nlan = "en";
                            }*/
                    /*Locale locale = new Locale(nlan);
                    Locale.setDefault(locale);
                    Configuration configuration = new Configuration();
                    configuration.setLocale(locale);
                    getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());*/
                            //Toast.makeText(MainActivity.this, "from " + nlan, Toast.LENGTH_LONG).show();


                            /*Locale locale = new Locale(nlan);
                            Locale.setDefault(locale);
                            Configuration configuration = getResources().getConfiguration();
                            configuration.setLocale(locale);
                            configuration.setLayoutDirection(locale);*/




                    /*Locale locale = new Locale(nlan);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(config,getResources().getDisplayMetrics());*/
                        /*}
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                /*loadingBar.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                loadingBar.setMessage(getResources().getString(R.string.posts_loadingbar_message));
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();*/
                //Toast.makeText(MainActivity.this, "from onstart", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, Locale.getDefault().getDisplayLanguage(), Toast.LENGTH_LONG).show();
                //GetUserDateTime getUserDateTime = new GetUserDateTime(MainActivity.this);
                //Toast.makeText(MainActivity.this, Locale.getDefault().getDisplayLanguage(), Toast.LENGTH_LONG).show();
                //getUserDateTime.setMyAppDisplayLanguage();

                /*waitingBar.setMessage(getResources().getString(R.string.posts_loadingbar_title));
                waitingBar.setCanceledOnTouchOutside(false);
                waitingBar.show();*/
                //try {
                transfer = "false";
                mydialog = new Dialog(MainActivity.this);
                mydialog.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                mydialog.setCancelable(false);
                mydialog.setContentView(R.layout.myprogressbr_layout);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mydialog.show();
                getCurrentUserCountryLives();
                SetCountsOfFriends();
                SetCountOfSentFriendsRequests();
                SetCountOfRecivedFriendsRequests();
                SetCountOfAllMessageRecived();
                SetCountOfBookedFriends();
                SetCountsOfGoldPosts();
                SetCountsOfNotification_GoldPosts();
                SetCountsOfNotification_LikePosts();
                SetCountsOfNotification_disLikePosts();
                DisplayAllUsersPosts();


                /*} catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }*/

                //Toast.makeText(MainActivity.this, Locale.getDefault().getDisplayLanguage(), Toast.LENGTH_LONG).show();
                //loadingBar.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }


        //Toast.makeText(MainActivity.this, "start", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //drawer is open
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }

    }

    private void CheckUserExistance() {
        final String current_user_ids = mAuth.getCurrentUser().getUid();
        useresRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_ids) || !dataSnapshot.child(current_user_ids).hasChild("fullname")){
                    sendUserToSetupActivaty();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        useresRef.addListenerForSingleValueEvent(useresRefValue);
    }


    private void initializeNotifcationDrawer(){
        notif_home.setGravity(Gravity.CENTER_VERTICAL);
        notif_home.setTypeface(null, Typeface.BOLD);
        notif_home.setTextColor(getResources().getColor(R.color.notifacation_color));
        notif_home.setText("96");
        notif_home.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
        notif_home.setPadding(12,3,12,3);
        notif_home.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        notif_home.setTextSize(16);
        notif_home.setVisibility(View.VISIBLE);
        notif_messages.setGravity(Gravity.CENTER_VERTICAL);
        notif_messages.setTypeface(null,Typeface.BOLD);
        notif_messages.setTextColor(getResources().getColor(R.color.notifacation_color));
        notif_messages.setText("7");
        notif_messages.setBackground(getResources().getDrawable(R.drawable.circle_background_views));
        notif_messages.setPadding(12,3,12,3);
        notif_messages.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        notif_messages.setVisibility(View.VISIBLE);
        notif_messages.setTextSize(16);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_post:
                sendUserToPostActivaty();
                /*ViewDialog alert = new ViewDialog();
                alert.showDialog(MainActivity.this);*/
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_gold_post:
                sendUserToGoldPostsActivaty();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_profile:
                sendUserToProfileActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_friends:
                sendUserToFriendsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_find_friends:
                sendUserToFindFriendsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_sent_friends_requests:
                sendUserToSentFriendsRequestsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_recived_friends_requests:
                sendUserToRecivedFriendsRequestsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_book_friends_requests:
                sendUserToBookFriendsActivaty();
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_messages:
                sendUserToAllMessagesActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_settings:
                sendUserToSettingsActivaty();
                drawerLayout.closeDrawers();
            break;

            case R.id.nav_notifications_posts_likes:
                sendUserToLikesPostsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_notifications_posts_dislikes:
                sendUserTodisLikesPostsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_notifications_posts_gold:
                sendUserToNotifGoldPostsActivaty();
                drawerLayout.closeDrawers();
            break;
            case R.id.nav_logout:
                updateUserState("Offline");
                mAuth.signOut();
                sendUserToLoginActivaty();
            break;
        }
    }

    private void sendUserToLoginActivaty() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void sendUserToSetupActivaty() {
        Intent intent = new Intent(MainActivity.this,SetupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToPostActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(intent);
    }
    private void sendUserToSettingsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        intent.putExtra("current_user_country_Lives", current_user_country_Lives);
        startActivity(intent);
    }
    private void sendUserToFriendsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,FriendsActivity.class);
        startActivity(intent);
    }
    private void sendUserToSentFriendsRequestsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,SentFriendsRequestActivity.class);
        startActivity(intent);
    }
    private void sendUserToRecivedFriendsRequestsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,RecivedFriendsRequestActivity.class);
        startActivity(intent);
    }
    private void sendUserToBookFriendsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,BookFriendsActivity.class);
        startActivity(intent);
    }

    private void sendUserToGoldPostsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,GoldPostsActivity.class);
        startActivity(intent);
    }
    private void sendUserToLikesPostsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,NotifLikePostsActivity.class);
        startActivity(intent);
    }
    private void sendUserTodisLikesPostsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,NotifdisLikePostsActivity.class);
        startActivity(intent);
    }
    private void sendUserToNotifGoldPostsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,NotifGoldPostsActivity.class);
        startActivity(intent);
    }

    private void sendUserToAllMessagesActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,ShowAllMessagesActivity.class);
        startActivity(intent);
    }
    private void sendUserToProfileActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
    private void sendUserToFindFriendsActivaty() {
        transfer = "true";
        Intent intent = new Intent(MainActivity.this,FindFriendsActivity.class);
        startActivity(intent);
    }
}
