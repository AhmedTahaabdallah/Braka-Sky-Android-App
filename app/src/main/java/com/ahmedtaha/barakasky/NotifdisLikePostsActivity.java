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
import android.provider.Settings;
import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotifdisLikePostsActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private RecyclerView myPostsList, home_comments_list;

    private FirebaseAuth mAuth;
    private DatabaseReference useresRef, LikesPostsRef, PostsRef, postUserRef, FriendsSignRef, GoldPostssRef, adRef;
    private ValueEventListener useresRefValue, LikesPostsRefValue, PostsRefValue, postUserRefValue, FriendsSignRefValue, GoldPostssRefValue, adRefValue;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    //String currentUserId, transfer = "false";
    String PostUserId, currentUserId, transfer = "false";
    Boolean likeChecker = false, dislikeChecker = false, goldPostChecker = false;
    String current_user_country_Lives;
    //private ProgressDialog loadingBar;
    //String[] ss = {"0"};
    //private String yy = "0";

    //private AlertDialog waitingBar;
    private Dialog mydialog;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
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
        setContentView(R.layout.activity_notifdis_like_posts_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_notifdis_like_posts_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        LikesPostsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        adRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("Notifications").child("disLikePosts");
        postUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GoldPostssRef = FirebaseDatabase.getInstance().getReference().child("GoldPosts");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog.Builder().setContext(NotifdisLikePostsActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_notifidislikeposts_root);

        mtoolbar = (Toolbar) findViewById(R.id.notif_dislikes_posts_layout_bar);
        setSupportActionBar(mtoolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.notif_dislikes_posts_toolbar_title));
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

        myPostsList = (RecyclerView) findViewById(R.id.all_notif_dislikes_posts_list);
        myPostsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostsList.setLayoutManager(linearLayoutManager);
    }

    private void getCurrentUserCountryLives(){
        useresRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
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
        useresRef.child(currentUserId).updateChildren(hashMap);
        //loadingBar.dismiss();
    }

    private void DisplayAllLikesPosts() {
        final String[] ss = {"0"};
        FirebaseRecyclerOptions<MyNotifications> options = new FirebaseRecyclerOptions.Builder<MyNotifications>().setQuery(adRef.orderByChild("order_date"), MyNotifications.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyNotifications, NotifdisLikePostsActivity.NotifLikesPostsViewHolder>(options) {

            @NonNull
            @Override
            public NotifdisLikePostsActivity.NotifLikesPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_post_layout_normal, parent, false);

                return new NotifdisLikePostsActivity.NotifLikesPostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final NotifdisLikePostsActivity.NotifLikesPostsViewHolder holder, int position, @NonNull final MyNotifications model) {
                //yy = "1";
                if(ss[0].equals("0")){
                    /*loadingBar.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                    loadingBar.setMessage(getResources().getString(R.string.posts_loadingbar_message));
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();*/

                    /*waitingBar.setMessage(getResources().getString(R.string.posts_loadingbar_title));
                    waitingBar.setCanceledOnTouchOutside(false);
                    waitingBar.show();*/
                    mydialog = new Dialog(NotifdisLikePostsActivity.this);
                    mydialog.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                    mydialog.setCancelable(false);
                    mydialog.setContentView(R.layout.myprogressbr_layout);
                    mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mydialog.show();
                }
                final String postKey = getRef(position).getKey();
                //Toast.makeText(NotifLikePostsActivity.this, "post Key : " + postKey, Toast.LENGTH_LONG).show();
                LikesPostsRefValue = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                        if (dataSnapshots.child("disLikePosts").hasChild(postKey) ){
                            PostsRefValue = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(postKey)){
                                        final String description = dataSnapshot.child(postKey).child("description").getValue().toString();
                                        final String imageUrl = dataSnapshot.child(postKey).child("postimage").getValue().toString();
                                        final String post_uid = dataSnapshot.child(postKey).child("uid").getValue().toString();
                                        final String next_day = dataSnapshot.child(postKey).child("next_date_time").getValue().toString();
                                        final String post_date_time = dataSnapshot.child(postKey).child("dateandtime").getValue().toString();
                                        //final String post_date = dataSnapshot.child(postKey).child("date").getValue().toString();
                                        //final String post_time = dataSnapshot.child(postKey).child("time").getValue().toString();

                                        //holder.setDate(post_date);
                                        //holder.setTime(post_time);
                                        holder.setDateandtime(post_date_time);
                                        holder.setDescription(description);
                                        holder.setPostimage(getApplicationContext(), imageUrl);
                                        //holder.setProfileimage(getApplicationContext(), model.getProfileimage());
                                        holder.setUid(getApplicationContext(), post_uid);
                                        holder.setLikeButtonStatus(postKey);
                                        holder.setDisplay_comments(postKey);
                                        holder.setdisLikeButtonStatus(postKey);
                                        holder.setGoldPostsButtonStatus(postKey, currentUserId);

                                        Button editPost = (Button) holder.mview.findViewById(R.id.edit_post_button_allpost);
                                        Button deletePost = (Button) holder.mview.findViewById(R.id.delete_post_button_allpost);
                                        final String uidPost = dataSnapshot.child(postKey).child("uid").getValue().toString();
                                        if (currentUserId.equals(uidPost)) {
                                            editPost.setVisibility(View.VISIBLE);
                                            deletePost.setVisibility(View.VISIBLE);
                                        } else {
                                            editPost.setVisibility(View.GONE);
                                            deletePost.setVisibility(View.GONE);
                                        }

                                        if (!currentUserId.equals(uidPost)) {
                                            FriendsSignRefValue = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        if (dataSnapshot.hasChild(uidPost)) {
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
                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ClickPostActivity.class);
                                                clickpostIntent.putExtra("PostKey", postKey);
                                                clickpostIntent.putExtra("Activity", "notif_dislikePosts");
                                                clickpostIntent.putExtra("PostUid", post_uid);
                                                clickpostIntent.putExtra("current_user_id", currentUserId);
                                                startActivity(clickpostIntent);
                                            }
                                        });*/
                                        holder.mview.findViewById(R.id.comment_button).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transfer = "true";
                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ClickPostActivity.class);
                                                clickpostIntent.putExtra("PostKey", postKey);
                                                clickpostIntent.putExtra("Activity", "notif_dislikePosts");
                                                clickpostIntent.putExtra("PostUid", post_uid);
                                                clickpostIntent.putExtra("current_user_id", currentUserId);
                                                startActivity(clickpostIntent);
                                            }
                                        });
                                        holder.mview.findViewById(R.id.post_image).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                transfer = "true";
                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ClickPostActivity.class);
                                                clickpostIntent.putExtra("PostKey", postKey);
                                                clickpostIntent.putExtra("Activity", "notif_dislikePosts");
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
                                                DeleteCurrentPost(postKey, imageUrl);
                                            }
                                        });
                                        holder.likePostButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                /*if(!currentUserId.equals(uidPost)) {
                                                    likeChecker = true;
                                                    PostsRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if(likeChecker.equals(true)){
                                                                if(dataSnapshot.child(postKey).child("LikesPosts").hasChild(currentUserId)){
                                                                    PostsRef.child(postKey).child("LikesPosts").child(currentUserId).removeValue();
                                                                    likeChecker = false;
                                                                } else {
                                                                    PostsRef.child(postKey).child("LikesPosts").child(currentUserId).setValue(true);
                                                                    likeChecker = false;
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }*/
                                                if (!currentUserId.equals(uidPost)) {
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
                                                            if (likeChecker.equals(true)) {
                                                                if (dataSnapshot.child(postKey).child("LikesPosts").hasChild(currentUserId)) {
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
                                                if (!currentUserId.equals(uidPost)) {
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
                                                            if (dislikeChecker.equals(true)) {
                                                                if (dataSnapshot.child(postKey).child("disLikesPosts").hasChild(currentUserId)) {
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
                                                                            useresRef.child(currentUserId).child("Notifications").child("disLikePosts").updateChildren(dislikespostMap);
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
                                                                                            if (dataSnapshot.exists()) {
                                                                                                if (dataSnapshot.hasChild("disLikesPosts") && dataSnapshot.hasChild("LikesPosts")) {
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
                                                if (!currentUserId.equals(uidPost)) {
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
                                                            if (goldPostChecker.equals(true)) {
                                                                //if(dataSnapshot.child(postKey).hasChild("LikesGoldPosts")){
                                                                if (dataSnapshot.child(postKey).child("LikesGoldPosts").hasChild(currentUserId)) {
                                                                    GoldPostssRef.child(postKey).child("LikesGoldPosts").child(currentUserId).removeValue();
                                                                    GoldPostssRef.child(postKey).child("post_key").setValue(postKey);
                                                                    GoldPostssRef.child(postKey).child("post_uid").setValue(post_uid);
                                                                    useresRef.child(currentUserId).child("Notifications").child("GoldPosts").child(postKey).removeValue();

                                                                    GoldPostssRef.child(postKey).child("LikesGoldPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            int count_gold = (int) dataSnapshot.getChildrenCount();
                                                                            if (count_gold == 0) {
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
                                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowLikesPostsUsersActivity.class);
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
                                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowdisLikesPostsUsersActivity.class);
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
                                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowGoldPostsUsersActivity.class);
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
                                                                Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowCommentsPostsUsersActivity.class);
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
                                                if (currentUserId.equals(post_uid)) {
                                                    transfer = "true";
                                                    Intent intent = new Intent(NotifdisLikePostsActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    transfer = "true";
                                                    Intent intent = new Intent(NotifdisLikePostsActivity.this, PersonProfileActivity.class);
                                                    intent.putExtra("visit_user_id", post_uid);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        holder.mview.findViewById(R.id.post_user_name).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (currentUserId.equals(post_uid)) {
                                                    transfer = "true";
                                                    Intent intent = new Intent(NotifdisLikePostsActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    transfer = "true";
                                                    Intent intent = new Intent(NotifdisLikePostsActivity.this, PersonProfileActivity.class);
                                                    intent.putExtra("visit_user_id", post_uid);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        //MainCommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Maincomments");
                                        home_comments_list = (RecyclerView) holder.mview.findViewById(R.id.post_home_comments_list);
                                        LinearLayoutManager linearLayoutManagerc = new LinearLayoutManager(NotifdisLikePostsActivity.this);
                                        linearLayoutManagerc.setReverseLayout(true);
                                        linearLayoutManagerc.setStackFromEnd(true);
                                        home_comments_list.setLayoutManager(linearLayoutManagerc);
                                        //DisplayAllPostComment(postKey, post_uid);
                                        holder.DisplayAllLikesPostComment(postKey, post_uid);
                                        //loadingBar.dismiss();
                                        //waitingBar.dismiss();
                                        mydialog.dismiss();
                                        holder.mview.findViewById(R.id.all_posts_continer).setVisibility(View.VISIBLE);
                                        holder.mview.findViewById(R.id.all_post_layout_allcont).setVisibility(View.VISIBLE);
                                    } else {
                                        //LikesPostsRef.child("LikePosts").child(postKey).removeValue();
                                        //Toast.makeText(NotifLikePostsActivity.this, "sd",Toast.LENGTH_LONG).show();
                                        useresRef.child(currentUserId).child("Notifications").child("disLikePosts").child(postKey).removeValue();
                                        holder.mview.findViewById(R.id.all_posts_continer).setVisibility(View.GONE);
                                        holder.mview.findViewById(R.id.all_post_layout_allcont).setVisibility(View.GONE);
                                        //loadingBar.dismiss();
                                        //waitingBar.dismiss();
                                        mydialog.dismiss();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            PostsRef.addValueEventListener(PostsRefValue);

                        } else {
                            holder.mview.findViewById(R.id.all_posts_continer).setVisibility(View.GONE);

                        }
                        /*if (!dataSnapshots.child("disLikePosts").exists()) {
                            loadingBar.dismiss();
                        }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                LikesPostsRef.child(currentUserId).child("Notifications").addValueEventListener(LikesPostsRefValue);
                ss[0] = "1";
                //Toast.makeText(NotifdisLikePostsActivity.this, ss[0], Toast.LENGTH_LONG).show();
            }


        };
        firebaseRecyclerAdapter.startListening();
        //loadingBar.dismiss();
        myPostsList.setAdapter(firebaseRecyclerAdapter);

        /*adRef.child("disLikePosts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //Toast.makeText(NotifdisLikePostsActivity.this, yy, Toast.LENGTH_LONG).show();
        /*if (ss[0].equals("0")){
            loadingBar.dismiss();
        }*/
        /*adRef.orderByChild("date_time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("disLikePosts").exists()){
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }
    public class NotifLikesPostsViewHolder extends RecyclerView.ViewHolder{
        View mview;
        ImageButton likePostButton, commentPostButton, dislikePostButton, goldPostButton;
        TextView display_likes, display_comments, display_dislikes, display_gold_posts;
        int countLikes, countdisLikes,countComm, countGoldPosts;
        String currentUserIdh;
        DatabaseReference LikeRefs, CommentsRefs, MainsCommentsRefsc, GoldPostsRef;
        public FirebaseRecyclerAdapter firebaseRecyclerAdapter_maincommentsc;
        public NotifLikesPostsViewHolder(View itemView) {
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
        public void DisplayAllLikesPostComment(final String p_key, final String p_uid) {
            //if(MainCommentsRef.equals("")){

            //}
            MainsCommentsRefsc = FirebaseDatabase.getInstance().getReference().child("Posts").child(p_key).child("Maincomments");
            FirebaseRecyclerOptions<MainComments> options = new FirebaseRecyclerOptions.Builder<MainComments>().setQuery(MainsCommentsRefsc.orderByChild("order_date").limitToLast(2), MainComments.class).build();

            firebaseRecyclerAdapter_maincommentsc = new FirebaseRecyclerAdapter<MainComments, NotifdisLikePostsActivity.MainCommentsViewHolder>(options) {

                @Override
                protected void onBindViewHolder(@NonNull final NotifdisLikePostsActivity.MainCommentsViewHolder holder, int position, @NonNull MainComments model) {
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
                                                HashMap likemaincommentpostMap = new HashMap();
                                                likemaincommentpostMap.put("order_date",order_date);
                                                likemaincommentpostMap.put("date_time",date_times);
                                                MainsCommentsRefsc.child(maincommentsKey).child("LikesMainComments").child(currentUserId).updateChildren(likemaincommentpostMap);
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
                                            Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowLikeMainCommentsUsersActivity.class);
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
                                            Intent clickpostIntent = new Intent(NotifdisLikePostsActivity.this, ShowReplaysMainCommentsUsersActivity.class);
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
                                Intent intent = new Intent(NotifdisLikePostsActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(NotifdisLikePostsActivity.this, PersonProfileActivity.class);
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
                                Intent intent = new Intent(NotifdisLikePostsActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(NotifdisLikePostsActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", uidMainComments);
                                startActivity(intent);
                            }
                        }
                    });
                }

                @NonNull
                @Override
                public NotifdisLikePostsActivity.MainCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.all_home_comments_layout_normal, parent, false);
                    return new NotifdisLikePostsActivity.MainCommentsViewHolder(view);
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
                        }
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
                        display_gold_posts.setText((Integer.toString(countGoldPosts)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
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
        public void setDateandtime(String dateandtime){
            TextView postdate = (TextView) mview.findViewById(R.id.post_date);
            //String la = Locale.getDefault().getDisplayLanguage();
            GetUserDateTime getUserDateTime = new GetUserDateTime(NotifdisLikePostsActivity.this);
            String date_value = getUserDateTime.getDateToShow(dateandtime, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            postdate.setText(date_value);
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
            GetUserDateTime getUserDateTime = new GetUserDateTime(NotifdisLikePostsActivity.this);
            String date_value = getUserDateTime.getDateToShow(date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            commentdate.setText(date_value);

        }
    }

    private void EditCurrentPost(String descriptions, final String post_key) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(NotifdisLikePostsActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_post_dailog_title));
        final EditText inputFaild = new EditText(NotifdisLikePostsActivity.this);
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
                    //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(NotifdisLikePostsActivity.this);
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(NotifdisLikePostsActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_post_dailog_title));
        final TextView inputFaild = new TextView(NotifdisLikePostsActivity.this);
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
                        //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_post_deleteted),Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(NotifdisLikePostsActivity.this);
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(NotifdisLikePostsActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_title));
        final EditText inputFaild = new EditText(NotifdisLikePostsActivity.this);
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
                    //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(NotifdisLikePostsActivity.this);
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(NotifdisLikePostsActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_title));
        final TextView inputFaild = new TextView(NotifdisLikePostsActivity.this);
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
                //Toast.makeText(NotifdisLikePostsActivity.this,getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(NotifdisLikePostsActivity.this);
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

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseRecyclerAdapter.startListening();

        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                /*loadingBar.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                loadingBar.setMessage(getResources().getString(R.string.posts_loadingbar_message));
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();*/
                transfer = "false";
                updateUserState("Online");
                getCurrentUserCountryLives();
                DisplayAllLikesPosts();
            } else {
                Toast.makeText(NotifdisLikePostsActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
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
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }

        if (LikesPostsRefValue != null && LikesPostsRef != null){
            LikesPostsRef.removeEventListener(LikesPostsRefValue);
        }
        if (PostsRefValue != null && PostsRef != null){
            PostsRef.removeEventListener(PostsRefValue);
        }
        if (FriendsSignRefValue != null && FriendsSignRef != null){
            FriendsSignRef.removeEventListener(FriendsSignRefValue);
        }
        if (GoldPostssRefValue != null && GoldPostssRef != null){
            GoldPostssRef.removeEventListener(GoldPostssRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }
    @Override
    public boolean onSupportNavigateUp() {
        transfer = "true";
        //onBackPressed();
        return super.onSupportNavigateUp();
    }
}
