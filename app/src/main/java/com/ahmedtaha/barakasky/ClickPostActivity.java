package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ClickPostActivity extends AppCompatActivity {
   // private ImageView postimage;
    //private TextView postdesc;
    //private Button editPost, deletePost;
    private CircleImageView profileImage;
    private TextView userFullname,postDate,postTime,postDescription,display_no_likes,display_no_comment,display_no_dislikes,display_no_gold_post;
    private Button editPost_Button,deletePost_Button;
    private ImageView postImage;
    private ImageButton postLike_Button,postComment_Button,add_comment_button, friends_signButton, postdisLike_Button, post_goldpost_Button;
    private EditText commentInputText;
    private RecyclerView mainCommentList, replaysList;
    private String post_key, post_Uid, current_userid, imageurl, activityName, next_day;
    Boolean likeChecker = false, dislikeChecker = false, goldPostChecker = false;;;

    private DatabaseReference useresRef, clickpostRef, LikeRefss, CommentsRef, MainCommentsRef, UsersRef, FriendsSignRef, GoldPostssRef, DisLikeRefss, PostsRef;
    private ValueEventListener clickpostRefValue, LikeRefssValue, CommentsRefValue, MainCommentsRefValue, UsersRefValue, FriendsSignRefValue, GoldPostssRefValue, DisLikeRefssValue, PostsRefValue;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter_maincomments;
    private String transfer = "false";
    String current_user_country_Lives;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;

    private Dialog mydialog;
    //private TextView downloadImageButton;
    private String post_image;
    //private TextView shareImagepost;

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
        setContentView(R.layout.activity_click_post_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_click_post_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        /*postimage = (ImageView) findViewById(R.id.click_post_image);
        postdesc = (TextView) findViewById(R.id.click_post_descrip);
        editPost = (Button) findViewById(R.id.edit_post_button);
        deletePost = (Button) findViewById(R.id.delete_post_button);
        editPost.setVisibility(View.INVISIBLE);
        deletePost.setVisibility(View.INVISIBLE);*/

        profileImage = (CircleImageView) findViewById(R.id.CP_post_profile_image);
        userFullname = (TextView) findViewById(R.id.CP_post_user_name);
        postDate = (TextView) findViewById(R.id.CP_post_date);
        postTime = (TextView) findViewById(R.id.CP_post_time);
        postDescription = (TextView) findViewById(R.id.CP_post_desc);
        display_no_likes = (TextView) findViewById(R.id.CP_display_no_of_likes);
        display_no_comment = (TextView) findViewById(R.id.CP_display_no_of_comment);
        display_no_dislikes = (TextView) findViewById(R.id.CP_display_no_of_dislikes);
        display_no_gold_post = (TextView) findViewById(R.id.CP_display_no_of_gold_post);
        editPost_Button = (Button) findViewById(R.id.CP_edit_post_button_allpost);
        deletePost_Button = (Button) findViewById(R.id.CP_delete_post_button_allpost);
        postImage = (ImageView) findViewById(R.id.CP_post_image);
        postLike_Button = (ImageButton) findViewById(R.id.CP_like_button);
        //postComment_Button = (ImageButton) findViewById(R.id.CP_comment_button);
        add_comment_button = (ImageButton) findViewById(R.id.post_comment_button);
        friends_signButton = (ImageButton) findViewById(R.id.post_post_friend_sign);
        commentInputText = (EditText) findViewById(R.id.main_comment_input);

        post_goldpost_Button = (ImageButton) findViewById(R.id.CP_gold_post_button);
        postdisLike_Button = (ImageButton) findViewById(R.id.CP_dislike_button);
        //downloadImageButton = (TextView) findViewById(R.id.cp_image_download_buttons);
        //shareImagepost = (TextView) findViewById(R.id.ccpost_share_image);

        //waitingBar = new SpotsDialog.Builder().setContext(ClickPostActivity.this).build();
        //la = Locale.getDefault().getDisplayLanguage();
        mainRoot = (RelativeLayout) findViewById(R.id.main_clickpost_root);

        mainCommentList = (RecyclerView) findViewById(R.id.main_comments_list);
        mainCommentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mainCommentList.setLayoutManager(linearLayoutManager);



        post_key = getIntent().getExtras().get("PostKey").toString();
        post_Uid = getIntent().getExtras().get("PostUid").toString();
        activityName = getIntent().getExtras().get("Activity").toString();
        current_userid = getIntent().getExtras().get("current_user_id").toString();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        clickpostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key);
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LikeRefss = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("LikesPosts");
        DisLikeRefss = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("disLikesPosts");
        CommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments");
        MainCommentsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments");
        FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GoldPostssRef = FirebaseDatabase.getInstance().getReference().child("GoldPosts");
        getCurrentUserCountryLives();
        clickpostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("description")){
                    final String description = dataSnapshot.child("description").getValue().toString();
                    final String image = dataSnapshot.child("postimage").getValue().toString();
                    post_image = dataSnapshot.child("postimage").getValue().toString();
                    String p_date_time = dataSnapshot.child("dateandtime").getValue().toString();
                    //String p_date = dataSnapshot.child("date").getValue().toString();
                    //String p_time = dataSnapshot.child("time").getValue().toString();
                    final String user_id = dataSnapshot.child("uid").getValue().toString();
                    imageurl = dataSnapshot.child("postimage").getValue().toString();
                    next_day = dataSnapshot.child("next_date_time").getValue().toString();

                    postDescription.setText(description);

                    GetUserDateTime getUserDateTime = new GetUserDateTime(ClickPostActivity.this);
                    String date_value = getUserDateTime.getDateToShow(p_date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                    postDate.setText(date_value);
                    //Toast.makeText(ClickPostActivity.this, current_user_country_Lives, Toast.LENGTH_LONG).show();
                   // postTime.setText(p_time);
                    Picasso.with(ClickPostActivity.this).load(image).into(postImage);
                    if (user_id.equals(current_userid)){
                        editPost_Button.setVisibility(View.VISIBLE);
                        deletePost_Button.setVisibility(View.VISIBLE);
                    } else {
                        editPost_Button.setVisibility(View.INVISIBLE);
                        deletePost_Button.setVisibility(View.INVISIBLE);
                    }

                    if (!current_userid.equals(user_id)){
                        FriendsSignRefValue = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.hasChild(user_id)){
                                        friends_signButton.setVisibility(View.VISIBLE);
                                    } else {
                                        friends_signButton.setVisibility(View.GONE);
                                    }
                                } else {
                                    friends_signButton.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        FriendsSignRef.child(current_userid).child("Friends").addValueEventListener(FriendsSignRefValue);
                    } else {
                        friends_signButton.setVisibility(View.GONE);
                    }

                    DatabaseReference useresRefpost = FirebaseDatabase.getInstance().getReference().child("Users");
                    useresRefpost.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String full_Name = dataSnapshot.child("fullname").getValue().toString();
                                userFullname.setText(full_Name);

                                String img = dataSnapshot.child("profileimage").getValue().toString();
                                Picasso.with(ClickPostActivity.this).load(img).into(profileImage);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    LikeRefssValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int countLikes;
                            int final_count = 10000;
                            if(dataSnapshot.hasChild(current_userid)){
                                countLikes = (int) dataSnapshot.getChildrenCount();
                                postLike_Button.setImageResource(R.drawable.like);

                                if (countLikes < final_count) {
                                    display_no_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                                } else if (countLikes >= final_count) {
                                    display_no_likes.setText((Integer.toString(countLikes)));
                                }
                            } else {
                                countLikes = (int) dataSnapshot.getChildrenCount();
                                postLike_Button.setImageResource(R.drawable.dislike);
                                if (countLikes < final_count) {
                                    display_no_likes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                                } else if (countLikes >= final_count) {
                                    display_no_likes.setText((Integer.toString(countLikes)));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    LikeRefss.addValueEventListener(LikeRefssValue);

                    DisLikeRefssValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int countLikes;
                            int final_count = 10000;
                            if(dataSnapshot.hasChild(current_userid)){
                                countLikes = (int) dataSnapshot.getChildrenCount();
                                postdisLike_Button.setImageResource(R.drawable.dislike_after);
                                if (countLikes < final_count) {
                                    display_no_dislikes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_dislike));
                                } else if (countLikes >= final_count) {
                                    display_no_dislikes.setText((Integer.toString(countLikes)));
                                }
                            } else {
                                countLikes = (int) dataSnapshot.getChildrenCount();
                                postdisLike_Button.setImageResource(R.drawable.dislike_before);
                                if (countLikes < final_count) {
                                    display_no_dislikes.setText((Integer.toString(countLikes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_dislike));
                                } else if (countLikes >= final_count) {
                                    display_no_dislikes.setText((Integer.toString(countLikes)));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    DisLikeRefss.addValueEventListener(DisLikeRefssValue);

                    GoldPostssRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int countgold;
                            int final_count = 10000;
                            if (dataSnapshot.exists()){
                                if(dataSnapshot.child("LikesGoldPosts").hasChild(current_userid)){
                                    countgold = (int) dataSnapshot.child("LikesGoldPosts").getChildrenCount();
                                    post_goldpost_Button.setImageResource(R.drawable.gold_star_after);
                                    display_no_gold_post.setText((Integer.toString(countgold)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                                    if (countgold < final_count) {
                                        display_no_gold_post.setText((Integer.toString(countgold)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                                    } else if (countgold >= final_count) {
                                        display_no_gold_post.setText((Integer.toString(countgold)));
                                    }
                                } else {
                                    countgold = (int) dataSnapshot.child("LikesGoldPosts").getChildrenCount();
                                    post_goldpost_Button.setImageResource(R.drawable.gold_star_before);
                                    if (countgold < final_count) {
                                        display_no_gold_post.setText((Integer.toString(countgold)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                                    } else if (countgold >= final_count) {
                                        display_no_gold_post.setText((Integer.toString(countgold)));
                                    }
                                }
                            } else {
                                countgold = 0;
                                post_goldpost_Button.setImageResource(R.drawable.gold_star_before);
                                display_no_gold_post.setText((Integer.toString(countgold)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_golds_after_gold));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    GoldPostssRef.child(post_key).addValueEventListener(GoldPostssRefValue);

                    CommentsRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int countComments;
                            int final_count = 10000;
                            if(dataSnapshot.hasChildren()){
                                countComments = (int) dataSnapshot.getChildrenCount();

                                if (countComments < final_count) {
                                    display_no_comment.setText((Integer.toString(countComments)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment));
                                } else if (countComments >= final_count) {
                                    display_no_comment.setText((Integer.toString(countComments)));
                                }
                            } else {
                                countComments = 0;
                                display_no_comment.setText((Integer.toString(countComments)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    CommentsRef.addValueEventListener(CommentsRefValue);
                    postLike_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*if(!current_userid.equals(user_id)) {
                                likeChecker = true;
                                LikeRefss.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(likeChecker.equals(true)){
                                            if(dataSnapshot.hasChild(current_userid)){
                                                LikeRefss.child(current_userid).removeValue();
                                                likeChecker = false;
                                            } else {
                                                LikeRefss.child(current_userid).setValue(true);
                                                likeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }*/
                            if(!current_userid.equals(post_Uid)) {
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
                                LikeRefssValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(likeChecker.equals(true)){
                                            if(dataSnapshot.hasChild(current_userid)){
                                                LikeRefss.child(current_userid).removeValue();
                                                UsersRef.child(current_userid).child("Notifications").child("LikePosts").child(post_key).removeValue();
                                                likeChecker = false;
                                            } else {
                                                HashMap likespostMap = new HashMap();
                                                likespostMap.put("order_date",order_date);
                                                likespostMap.put("date_time",date_times);
                                                LikeRefss.child(current_userid).updateChildren(likespostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        DisLikeRefss.child(current_userid).removeValue();
                                                        HashMap likepostMap = new HashMap();
                                                        likepostMap.put("order_date",order_date);
                                                        likepostMap.put("date_time",date_times);
                                                        UsersRef.child(current_userid).child("Notifications").child("LikePosts").child(post_key).updateChildren(likepostMap);
                                                        UsersRef.child(current_userid).child("Notifications").child("disLikePosts").child(post_key).removeValue();
                                                        postdisLike_Button.setImageResource(R.drawable.dislike_before);
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
                                LikeRefss.addValueEventListener(LikeRefssValue);
                            }
                        }
                    });

                    postdisLike_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!current_userid.equals(post_Uid)) {
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
                                DisLikeRefssValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dislikeChecker.equals(true)){
                                            if(dataSnapshot.hasChild(current_userid)){
                                                DisLikeRefss.child(current_userid).removeValue();
                                                UsersRef.child(current_userid).child("Notifications").child("disLikePosts").child(post_key).removeValue();
                                                dislikeChecker = false;
                                            } else {
                                                HashMap dislikespostMap = new HashMap();
                                                dislikespostMap.put("order_date",order_date);
                                                dislikespostMap.put("date_time",date_times);
                                                DisLikeRefss.child(current_userid).updateChildren(dislikespostMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        LikeRefss.child(current_userid).removeValue();
                                                        UsersRef.child(current_userid).child("Notifications").child("LikePosts").child(post_key).removeValue();
                                                        HashMap dislikepostMap = new HashMap();
                                                        dislikepostMap.put("order_date",order_date);
                                                        dislikepostMap.put("date_time",date_times);
                                                        UsersRef.child(current_userid).child("Notifications").child("disLikePosts").child(post_key).updateChildren(dislikepostMap);
                                                        postLike_Button.setImageResource(R.drawable.dislike);
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
                                                                PostsRef.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                                                    StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageurl);
                                                                                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            // File deleted successfully
                                                                                            PostsRef.child(post_key).removeValue();
                                                                                            GoldPostssRef.child(post_key).removeValue();
                                                                                            sendUserToMainActivity();
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    Calendar calForDate_order = Calendar.getInstance();
                                                                                    SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy");
                                                                                    calForDate_order.add(Calendar.DATE, 1);
                                                                                    String saveNextDay = currentDate_order.format(calForDate_order.getTime());
                                                                                    SimpleDateFormat tody = new SimpleDateFormat("HH:mm:ss");
                                                                                    String saveNextDay_seconds = tody.format(next_date);
                                                                                    String last_next_date = saveNextDay + " " + saveNextDay_seconds;
                                                                                    PostsRef.child(post_key).child("next_date_time").setValue(last_next_date);
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
                                DisLikeRefss.addValueEventListener(DisLikeRefssValue);
                            }
                        }
                    });

                    post_goldpost_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!current_userid.equals(post_Uid)) {
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

                                date_times = savecurrentdate_orders + " " + savecurrenttimeseconds;
                                GoldPostssRefValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(goldPostChecker.equals(true)){
                                            //if(dataSnapshot.child(postKey).hasChild("LikesGoldPosts")){
                                            if(dataSnapshot.child(post_key).child("LikesGoldPosts").hasChild(current_userid)){
                                                GoldPostssRef.child(post_key).child("LikesGoldPosts").child(current_userid).removeValue();
                                                GoldPostssRef.child(post_key).child("post_key").setValue(post_key);
                                                GoldPostssRef.child(post_key).child("post_uid").setValue(post_Uid);
                                                UsersRef.child(current_userid).child("Notifications").child("GoldPosts").child(post_key).removeValue();

                                                GoldPostssRef.child(post_key).child("LikesGoldPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        int count_gold = (int) dataSnapshot.getChildrenCount();
                                                        if (count_gold == 0){
                                                            GoldPostssRef.child(post_key).removeValue();
                                                        } else {
                                                            GoldPostssRef.child(post_key).child("count_gold").setValue(Integer.toString(count_gold));
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                goldPostChecker = false;
                                            } else {
                                                Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                                SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                                currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                                String order_date = currentorderDate.format(calFororderDate.getTime());

                                                HashMap likegoldpostMap = new HashMap();
                                                likegoldpostMap.put("order_date",order_date);
                                                likegoldpostMap.put("date_time",date_times);
                                                GoldPostssRef.child(post_key).child("LikesGoldPosts").child(current_userid).updateChildren(likegoldpostMap);
                                                GoldPostssRef.child(post_key).child("post_key").setValue(post_key);
                                                GoldPostssRef.child(post_key).child("post_uid").setValue(post_Uid);
                                                HashMap likegoldpostNotifMap = new HashMap();
                                                likegoldpostNotifMap.put("order_date",order_date);
                                                likegoldpostNotifMap.put("date_time",date_times);
                                                UsersRef.child(current_userid).child("Notifications").child("GoldPosts").child(post_key).updateChildren(likegoldpostNotifMap);

                                                GoldPostssRef.child(post_key).child("LikesGoldPosts").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        int count_gold = (int) dataSnapshot.getChildrenCount();
                                                        GoldPostssRef.child(post_key).child("count_gold").setValue(Integer.toString(count_gold));
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
                    editPost_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditCurrentPost(description);
                        }
                    });

                    profileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current_userid.equals(user_id)){
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", user_id);
                                startActivity(intent);
                            }

                        }
                    });
                    userFullname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current_userid.equals(user_id)){
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", user_id);
                                startActivity(intent);
                            }
                        }
                    });
                    display_no_likes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");
                            PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("LikesPosts")){
                                        int count = (int) dataSnapshot.child("LikesPosts").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowLikesPostsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", post_key);
                                            //clickpostIntent.putExtra("Activity", "main");
                                            clickpostIntent.putExtra("PostUid", post_Uid);
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

                    display_no_dislikes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");
                            PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("disLikesPosts")){
                                        int count = (int) dataSnapshot.child("disLikesPosts").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowdisLikesPostsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", post_key);
                                            //clickpostIntent.putExtra("Activity", "main");
                                            clickpostIntent.putExtra("PostUid", post_Uid);
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
                    display_no_gold_post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("GoldPosts");
                            PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("LikesGoldPosts")){
                                        int count = (int) dataSnapshot.child("LikesGoldPosts").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowGoldPostsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", post_key);
                                            //clickpostIntent.putExtra("Activity", "main");
                                            clickpostIntent.putExtra("PostUid", post_Uid);
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
                    display_no_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");
                            PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("Maincomments")){
                                        int count = (int) dataSnapshot.child("Maincomments").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowCommentsPostsUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", post_key);
                                            //clickpostIntent.putExtra("Activity", "main");
                                            clickpostIntent.putExtra("PostUid", post_Uid);
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
                    /*shareImagepost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ClickPostActivity.this, imageurl, Toast.LENGTH_LONG).show();
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            sharingIntent.setType("image/*");
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                            Uri uri = Uri.parse(imageurl);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }
                    });*/

                } else{
                    sendUserToMainActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClickPostActivity.this, "image", Toast.LENGTH_LONG);
            }
        });*/
        /*downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String ss = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                //Toast.makeText(ClickPostActivity.this, "downloaded : " + ss, Toast.LENGTH_LONG).show();
                Toast.makeText(ClickPostActivity.this, "failure", Toast.LENGTH_LONG);

                try {*/
                                /*waitingBar.setMessage(getResources().getString(R.string.download_image_title));
                                waitingBar.setCanceledOnTouchOutside(false);
                                waitingBar.show();*/
                                /*mydialog = new Dialog(ClickPostActivity.this);
                                mydialog.setTitle(getResources().getString(R.string.posts_loadingbar_title));
                                mydialog.setCancelable(false);
                                mydialog.setContentView(R.layout.myprogressbr_layout);
                                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                mydialog.show();*/
                    /*Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    //calForDate_order.add(Calendar.HOUR, 2);
                    SimpleDateFormat currentDate_order = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.ENGLISH);
                    currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                    String savedate = currentDate_order.format(calForDate_order.getTime());
                    StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(post_image);
                    File localFile = null;
                    Toast.makeText(ClickPostActivity.this, "failure becuse22 : ", Toast.LENGTH_LONG);
                    localFile = File.createTempFile("barakaskyimage" + savedate, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    UserProfileImageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            //Toast.makeText(ClickPostActivity.this, "downloaded", Toast.LENGTH_LONG).show();
                            //waitingBar.dismiss();
                            //mydialog.dismiss();
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.download_image_success),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.download_image_success),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            //Toast.makeText(ClickPostActivity.this, "not", Toast.LENGTH_LONG).show();
                            //waitingBar.dismiss();
                            //mydialog.dismiss();
                            Toast.makeText(ClickPostActivity.this, "failure becuse : " + exception.getMessage().toString(), Toast.LENGTH_LONG);
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.download_image_notsuccess),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.download_image_notsuccess),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(ClickPostActivity.this, "err becuse : " + e.getMessage().toString(), Toast.LENGTH_LONG);
                }



            }
        });*/

        /*downloadImageButton.setEnabled(true);
        downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClickPostActivity.this, "ggg", Toast.LENGTH_LONG);
            }
        });*/
        deletePost_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCurrentPost(imageurl);
            }
        });

        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_Input = commentInputText.getText().toString();
                if(!TextUtils.isEmpty(comment_Input)){
                    AddPostComment(comment_Input);
                    commentInputText.setText("");
                    firebaseRecyclerAdapter_maincomments.startListening();
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_comment_input_empty),Toast.LENGTH_LONG).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_input_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_input_empty),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });

        //DisplayAllUsersMainComments();

    }

    /*public void Ima(View view){
        Toast.makeText(ClickPostActivity.this, "image", Toast.LENGTH_LONG);
    }*/
    private void getCurrentUserCountryLives(){
        useresRef.child(current_userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    current_user_country_Lives = dataSnapshot.child("country_lives").getValue().toString();
                    //Toast.makeText(ClickPostActivity.this, current_user_country_Lives, Toast.LENGTH_LONG).show();
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
        UsersRef.child(current_userid).updateChildren(hashMap);
    }

    private void DisplayAllUsersMainComments() {
        FirebaseRecyclerOptions<MainComments> options = new FirebaseRecyclerOptions.Builder<MainComments>().setQuery(MainCommentsRef.orderByChild("order_date"), MainComments.class).build();

        firebaseRecyclerAdapter_maincomments = new FirebaseRecyclerAdapter<MainComments, ClickPostActivity.MainCommentsViewHolder>(options) {
            @NonNull
            @Override
            public MainCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_comments_layout_normal, parent, false);

                return new ClickPostActivity.MainCommentsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MainCommentsViewHolder holder, int position, @NonNull MainComments model) {
                final String maincommentsKey = getRef(position).getKey();
                final String c_uid = model.getUid();
                final String comm_desc = model.getComment();

                //holder.setDate(model.getDate());
                //holder.setTime(model.getTime());
                holder.setDate_time(model.getDate_time());
                holder.setComment(model.getComment());
                holder.setUid(getApplicationContext(),model.getUid());
                holder.setLikeMainCommentButtonStatus(post_key,maincommentsKey);
                holder.setDisplay_main_comments(post_key,maincommentsKey);

                Button editMainComments = (Button) holder.mview.findViewById(R.id.edit_comment_button_allcomments);
                Button deleteMainComments = (Button) holder.mview.findViewById(R.id.delete_comment_button_allcomments);
                final String uidMainComments = model.getUid();
                if(current_userid.equals(uidMainComments)){
                    editMainComments.setVisibility(View.VISIBLE);
                    deleteMainComments.setVisibility(View.VISIBLE);
                } else {
                    if(current_userid.equals(post_Uid)){
                        deleteMainComments.setVisibility(View.VISIBLE);
                    } else {
                        deleteMainComments.setVisibility(View.GONE);
                    }
                    editMainComments.setVisibility(View.INVISIBLE);

                }

                if (!current_userid.equals(uidMainComments)){
                    FriendsSignRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.hasChild(uidMainComments)){
                                    holder.mview.findViewById(R.id.comment_post_friend_sign).setVisibility(View.VISIBLE);
                                } else {
                                    holder.mview.findViewById(R.id.comment_post_friend_sign).setVisibility(View.GONE);
                                }
                            } else {
                                holder.mview.findViewById(R.id.comment_post_friend_sign).setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    FriendsSignRef.child(current_userid).child("Friends").addValueEventListener(FriendsSignRefValue);
                } else {
                    holder.mview.findViewById(R.id.comment_post_friend_sign).setVisibility(View.GONE);
                }

                holder.mview.findViewById(R.id.edit_comment_button_allcomments).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditCurrentMainComments(comm_desc,post_key,maincommentsKey);
                    }
                });
                holder.mview.findViewById(R.id.delete_comment_button_allcomments).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteCurrentMainComments(post_key,maincommentsKey);
                    }
                });
                holder.likeMainCommentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!current_userid.equals(uidMainComments)) {
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
                            MainCommentsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(likeChecker.equals(true)){
                                        if(dataSnapshot.child(maincommentsKey).child("LikesMainComments").hasChild(current_userid)){
                                            MainCommentsRef.child(maincommentsKey).child("LikesMainComments").child(current_userid).removeValue();
                                            likeChecker = false;
                                        } else {
                                            HashMap likecommentMap = new HashMap();
                                            likecommentMap.put("order_date",order_date);
                                            likecommentMap.put("date_time",date_times);
                                            MainCommentsRef.child(maincommentsKey).child("LikesMainComments").child(current_userid).updateChildren(likecommentMap);
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

                holder.mview.findViewById(R.id.display_no_of_likes_comments).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                        PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Maincomments").child(maincommentsKey).hasChild("LikesMainComments")){
                                    int count = (int) dataSnapshot.child("Maincomments").child(maincommentsKey).child("LikesMainComments").getChildrenCount();
                                    if (count > 0){
                                        transfer = "true";
                                        Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowLikeMainCommentsUsersActivity.class);
                                        clickpostIntent.putExtra("PostKey", post_key);
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
                holder.mview.findViewById(R.id.display_no_of_comment_comments).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                        PostsRefss.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Maincomments").child(maincommentsKey).hasChild("Replays")){
                                    int count = (int) dataSnapshot.child("Maincomments").child(maincommentsKey).child("Replays").getChildrenCount();
                                    if (count > 0){
                                        transfer = "true";
                                        Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowReplaysMainCommentsUsersActivity.class);
                                        clickpostIntent.putExtra("PostKey", post_key);
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
                holder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
                        builder.setTitle(getResources().getString(R.string.click_post_page_add_replay_dailog_title));
                        final EditText inputFaild = new EditText(ClickPostActivity.this);
                        inputFaild.setPadding(5, 5, 5, 5);
                        inputFaild.setTextSize(15);
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //params.setMargins(10,10,10,10);
                        //inputFaild.setLayoutParams(params);
                        inputFaild.setBackgroundResource(R.drawable.inputs);
                        builder.setView(inputFaild);
                        builder.setPositiveButton(getResources().getString(R.string.click_post_page_add_replay_dailog_add_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!TextUtils.isEmpty(inputFaild.getText().toString())){
                                    Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForDate_order.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                    currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

                                    Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForTimesecond.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                    currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

                                    Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForTime.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                    currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrenttime = currentTime.format(calForTime.getTime());

                                    Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForDate.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                                    currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrentdate = currentDate.format(calForDate.getTime());

                                    String postrendomname = savecurrentdate + savecurrenttimesecond;
                                    String post_date_time = savecurrentdate_order + " " + savecurrenttimesecond;

                                    Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                    currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String order_date = currentorderDate.format(calFororderDate.getTime());

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("uid", current_userid);
                                    hashMap.put("replay", inputFaild.getText().toString().trim());
                                    hashMap.put("date", savecurrentdate);
                                    hashMap.put("time", savecurrenttime);
                                    hashMap.put("date_time", post_date_time);
                                    hashMap.put("order_date", order_date);
                                    MainCommentsRef.child(maincommentsKey).child("Replays").child(current_userid + postrendomname).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                                if (la.equals("العربية")){
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                } else {
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                }
                                            } else {
                                                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),Toast.LENGTH_SHORT).show();
                                                if (la.equals("العربية")){
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                } else {
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),Toast.LENGTH_SHORT).show();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.click_post_page_add_replay_dailog_cancle_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
                        final Dialog dialog = new Dialog(ClickPostActivity.this);
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
                        mDialogtitle.setText(getResources().getString(R.string.click_post_page_add_replay_dailog_title));
                        mDialogmessage.setVisibility(View.INVISIBLE);
                        mDialogdescription.setVisibility(View.VISIBLE);
                        FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
                        TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
                        mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_add_replay_dailog_cancle_button));
                        mDialogcancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
                        TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
                        mDialogok_text.setText(getResources().getString(R.string.click_post_page_add_replay_dailog_add_button));
                        mDialogok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(mDialogdescription.getText().toString())){
                                    Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForDate_order.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                    currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

                                    Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForTimesecond.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                    currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

                                    Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForTime.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                    currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrenttime = currentTime.format(calForTime.getTime());

                                    Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    //calForDate.add(Calendar.HOUR, 2);
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                                    currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String savecurrentdate = currentDate.format(calForDate.getTime());

                                    String postrendomname = savecurrentdate + savecurrenttimesecond;
                                    String post_date_time = savecurrentdate_order + " " + savecurrenttimesecond;

                                    Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                    SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                    currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    String order_date = currentorderDate.format(calFororderDate.getTime());

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("uid", current_userid);
                                    hashMap.put("replay", mDialogdescription.getText().toString().trim());
                                    hashMap.put("date", savecurrentdate);
                                    hashMap.put("time", savecurrenttime);
                                    hashMap.put("date_time", post_date_time);
                                    hashMap.put("order_date", order_date);
                                    MainCommentsRef.child(maincommentsKey).child("Replays").child(current_userid + postrendomname).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                                if (la.equals("العربية")){
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                } else {
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                }
                                            } else {
                                                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),Toast.LENGTH_SHORT).show();
                                                if (la.equals("العربية")){
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                } else {
                                                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_not_success),
                                                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),Toast.LENGTH_SHORT).show();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_add_replay_dailog_replay_empty),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                });

                replaysList = (RecyclerView) holder.mview.findViewById(R.id.sub_comments_list);
                LinearLayoutManager linearLayoutManagerc = new LinearLayoutManager(ClickPostActivity.this);
                linearLayoutManagerc.setReverseLayout(true);
                linearLayoutManagerc.setStackFromEnd(true);
                replaysList.setLayoutManager(linearLayoutManagerc);
                holder.DisplayAllReplays(post_key, maincommentsKey, post_Uid, c_uid);

                holder.mview.findViewById(R.id.all_users_profile_image_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_userid.equals(uidMainComments)){
                            transfer = "true";
                            Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", uidMainComments);
                            startActivity(intent);
                        }

                    }
                });
                holder.mview.findViewById(R.id.all_users_profile_full_name_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_userid.equals(uidMainComments)){
                            transfer = "true";
                            Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", uidMainComments);
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        firebaseRecyclerAdapter_maincomments.startListening();
        mainCommentList.setAdapter(firebaseRecyclerAdapter_maincomments);
        //updateUserState("Online");
    }

    public class MainCommentsViewHolder extends RecyclerView.ViewHolder{
        View mview;

        ImageButton likeMainCommentButton, commentButton;
        TextView display_main_comments_likes, display_main_comments;
        int count_main_comments_Likes,count_main_Comm;
        String currentUserIdh;
        DatabaseReference Like_main_comments_Refs, MainsCommentsRefs, MainsCommentsRefsc;
        FirebaseRecyclerAdapter firebaseRecyclerAdapter_maincommentsc;
        public MainCommentsViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            likeMainCommentButton = (ImageButton) mview.findViewById(R.id.like_button_comment);
            commentButton = (ImageButton) mview.findViewById(R.id.comment_button_comments);
            display_main_comments_likes = (TextView) mview.findViewById(R.id.display_no_of_likes_comments);
            display_main_comments = (TextView) mview.findViewById(R.id.display_no_of_comment_comments);
            Like_main_comments_Refs = FirebaseDatabase.getInstance().getReference().child("Posts");
            MainsCommentsRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserIdh = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void DisplayAllReplays(final String p_key, final String c_key, final String p_uid, final String c_uid) {
            //if(MainCommentsRef.equals("")){

            //}
            MainsCommentsRefsc = FirebaseDatabase.getInstance().getReference().child("Posts").child(p_key).child("Maincomments").child(c_key).child("Replays");
            FirebaseRecyclerOptions<Replays> options = new FirebaseRecyclerOptions.Builder<Replays>().setQuery(MainsCommentsRefsc.orderByChild("order_date"), Replays.class).build();

            firebaseRecyclerAdapter_maincommentsc = new FirebaseRecyclerAdapter<Replays, ClickPostActivity.ReplaysViewHolder>(options) {
                @NonNull
                @Override
                public ReplaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.all_sub_comments_layout_normal, parent, false);
                    return new ClickPostActivity.ReplaysViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull final ReplaysViewHolder holder, int position, @NonNull Replays model) {
                    final String replays_Key = getRef(position).getKey();
                    final String replays_des = model.getReplay();
                    //final String comm_desc = model.getComment();
//                if(home_comments_list.equals("")){
//                    home_comments_list = (RecyclerView) holder.mview.findViewById(R.id.post_home_comments_list);
//                }



                    //holder.setDate(model.getDate());
                    //holder.setTime(model.getTime());
                    holder.setDate_time(model.getDate_time());
                    holder.setReplay(model.getReplay());
                    holder.setUid(getApplicationContext(),model.getUid());
                    holder.setLikeReplayButtonStatus(p_key, c_key, replays_Key);


                    Button editReplay = (Button) holder.mview.findViewById(R.id.edit_comment_button_all_sub_comments);
                    Button deleteReplay = (Button) holder.mview.findViewById(R.id.delete_comment_button_all_sub_comments);
                    final String uidReplays = model.getUid();
                    if(current_userid.equals(uidReplays)){
                        editReplay.setVisibility(View.VISIBLE);
                        deleteReplay.setVisibility(View.VISIBLE);
                    } else {
                        if(current_userid.equals(p_uid) || current_userid.equals(c_uid)){
                            deleteReplay.setVisibility(View.VISIBLE);
                        } else {
                            deleteReplay.setVisibility(View.GONE);
                        }
                        editReplay.setVisibility(View.INVISIBLE);

                    }

                    if (!current_userid.equals(uidReplays)){
                        FriendsSignRefValue = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    if (dataSnapshot.hasChild(uidReplays)){
                                        holder.mview.findViewById(R.id.replys_post_friend_sign).setVisibility(View.VISIBLE);
                                    } else {
                                        holder.mview.findViewById(R.id.replys_post_friend_sign).setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.mview.findViewById(R.id.replys_post_friend_sign).setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        FriendsSignRef.child(current_userid).child("Friends").addValueEventListener(FriendsSignRefValue);
                    } else {
                        holder.mview.findViewById(R.id.replys_post_friend_sign).setVisibility(View.GONE);
                    }

                    holder.mview.findViewById(R.id.edit_comment_button_all_sub_comments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //EditCurrentMainComments(comm_desc,p_key,maincommentsKey);
                        }
                    });
                    holder.mview.findViewById(R.id.delete_comment_button_all_sub_comments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //DeleteCurrentMainComments(p_key,maincommentsKey);
                        }
                    });
                    holder.likeMainCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!current_userid.equals(uidReplays)) {
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
                                            if(dataSnapshot.child(replays_Key).child("LikesReplays").hasChild(current_userid)){
                                                MainsCommentsRefsc.child(replays_Key).child("LikesReplays").child(current_userid).removeValue();
                                                likeChecker = false;
                                            } else {
                                                HashMap likereplayMap = new HashMap();
                                                likereplayMap.put("order_date",order_date);
                                                likereplayMap.put("date_time",date_times);
                                                MainsCommentsRefsc.child(replays_Key).child("LikesReplays").child(current_userid).updateChildren(likereplayMap);
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

                    holder.mview.findViewById(R.id.display_no_of_likes_sub_comments).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference PostsRefss = FirebaseDatabase.getInstance().getReference().child("Posts");

                            PostsRefss.child(p_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Maincomments").child(c_key).child("Replays").child(replays_Key).hasChild("LikesReplays")){
                                        int count = (int) dataSnapshot.child("Maincomments").child(c_key).child("Replays").child(replays_Key).child("LikesReplays").getChildrenCount();
                                        if (count > 0){
                                            transfer = "true";
                                            Intent clickpostIntent = new Intent(ClickPostActivity.this, ShowLikeReplayMainCommentUsersActivity.class);
                                            clickpostIntent.putExtra("PostKey", p_key);
                                            clickpostIntent.putExtra("cPost_key",c_key );
                                            clickpostIntent.putExtra("PostUid", uidReplays);
                                            clickpostIntent.putExtra("replay_key", replays_Key);
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
                    editReplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
                            builder.setTitle(getResources().getString(R.string.click_post_page_edit_replay_dailog_title));
                            final EditText inputFaild = new EditText(ClickPostActivity.this);
                            inputFaild.setText(replays_des);
                            inputFaild.setPadding(5, 5, 5, 5);
                            inputFaild.setTextSize(15);
                            inputFaild.setBackgroundResource(R.drawable.inputs);
                            builder.setView(inputFaild);
                            builder.setPositiveButton(getResources().getString(R.string.click_post_page_edit_replay_dailog_update_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!TextUtils.isEmpty(inputFaild.getText().toString())){
                                        MainsCommentsRefsc.child(replays_Key).child("replay").setValue(inputFaild.getText().toString());
                                        //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_success),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_success),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    } else {
                                        //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),Toast.LENGTH_SHORT).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    }
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.click_post_page_edit_replay_dailog_cancle_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            Dialog dialog = builder.create();
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/


                            final Dialog dialog = new Dialog(ClickPostActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.mydialog_layout);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
                            final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
                            TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
                            mDialogtitle.setText(getResources().getString(R.string.click_post_page_edit_replay_dailog_title));
                            mDialogmessage.setVisibility(View.INVISIBLE);
                            mDialogdescription.setVisibility(View.VISIBLE);
                            mDialogdescription.setText(replays_des);
                            FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
                            TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
                            mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_edit_replay_dailog_cancle_button));
                            mDialogcancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
                            TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
                            mDialogok_text.setText(getResources().getString(R.string.click_post_page_edit_replay_dailog_update_button));
                            mDialogok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!TextUtils.isEmpty(mDialogdescription.getText().toString())){
                                        MainsCommentsRefsc.child(replays_Key).child("replay").setValue(mDialogdescription.getText().toString());
                                        //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_success),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_success),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    } else {
                                        //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),Toast.LENGTH_SHORT).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_replay_dailog_description_empty),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });

                    deleteReplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
                            builder.setTitle(getResources().getString(R.string.click_post_page_delete_replay_dailog_title));
                            final TextView inputFaild = new TextView(ClickPostActivity.this);
                            inputFaild.setText(getResources().getString(R.string.click_post_page_delete_replay_dailog_confirm_message));
                            inputFaild.setPadding(5, 5, 5, 5);
                            inputFaild.setTextSize(16);
                            builder.setView(inputFaild);
                            builder.setPositiveButton(getResources().getString(R.string.click_post_page_delete_replay_dailog_delete_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    MainsCommentsRefsc.child(replays_Key).removeValue();
                                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_delete_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_replay_dailog_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_replay_dailog_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.click_post_page_delete_replay_dailog_cancle_button), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            Dialog dialog = builder.create();
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);*/
                            final Dialog dialog = new Dialog(ClickPostActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.mydialog_layout);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
                            final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
                            TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
                            mDialogtitle.setText(getResources().getString(R.string.click_post_page_delete_replay_dailog_title));
                            mDialogmessage.setVisibility(View.VISIBLE);
                            mDialogdescription.setVisibility(View.INVISIBLE);
                            mDialogmessage.setText(getResources().getString(R.string.click_post_page_delete_replay_dailog_confirm_message));
                            FrameLayout mDialogcancle = dialog.findViewById(R.id.mydialog_cancle);
                            TextView mDialogcancle_text = dialog.findViewById(R.id.mydialog_cancle_text);
                            mDialogcancle_text.setText(getResources().getString(R.string.click_post_page_delete_replay_dailog_cancle_button));
                            mDialogcancle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            FrameLayout mDialogok = dialog.findViewById(R.id.mydialog_ok);
                            TextView mDialogok_text = dialog.findViewById(R.id.mydialog_ok_text);
                            mDialogok_text.setText(getResources().getString(R.string.click_post_page_delete_replay_dailog_delete_button));
                            mDialogok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MainsCommentsRefsc.child(replays_Key).removeValue();
                                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_delete_replay_dailog_success),Toast.LENGTH_SHORT).show();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_replay_dailog_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_delete_replay_dailog_success),
                                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    });

                    holder.mview.findViewById(R.id.all_users_profile_image_sub_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current_userid.equals(uidReplays)){
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", uidReplays);
                                startActivity(intent);
                            }

                        }
                    });
                    holder.mview.findViewById(R.id.all_users_profile_full_name_sub_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (current_userid.equals(uidReplays)){
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                transfer = "true";
                                Intent intent = new Intent(ClickPostActivity.this, PersonProfileActivity.class);
                                intent.putExtra("visit_user_id", uidReplays);
                                startActivity(intent);
                            }
                        }
                    });
                }


            };

            firebaseRecyclerAdapter_maincommentsc.startListening();
            replaysList.setAdapter(firebaseRecyclerAdapter_maincommentsc);
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
            TextView commenttime = (TextView) mview.findViewById(R.id.comment_time);
            commenttime.setText(time);
        }
        public void setDate(String date){
            TextView commentdate = (TextView) mview.findViewById(R.id.comment_date);
            commentdate.setText(date);
        }
        public void setComment(String comment){
            TextView comm = (TextView) mview.findViewById(R.id.comment_desc);
            comm.setText(comment);
        }

        public void setUid(final Context ctx, String uid){
            DatabaseReference useresRefpost = FirebaseDatabase.getInstance().getReference().child("Users");


            useresRefpost.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String full_Name = dataSnapshot.child("fullname").getValue().toString();
                        TextView username_main_comments = (TextView) mview.findViewById(R.id.all_users_profile_full_name_comment);
                        username_main_comments.setText(full_Name);

                        String img_main_comments = dataSnapshot.child("profileimage").getValue().toString();
                        CircleImageView image = (CircleImageView) mview.findViewById(R.id.all_users_profile_image_comment);
                        Picasso.with(ctx).load(img_main_comments).into(image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        public void setDate_time(String date_time){
            TextView commentdate = (TextView) mview.findViewById(R.id.comment_date);
            GetUserDateTime getUserDateTime = new GetUserDateTime(ClickPostActivity.this);
            String date_value = getUserDateTime.getDateToShow(date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            commentdate.setText(date_value);

        }
    }

    public class ReplaysViewHolder extends RecyclerView.ViewHolder{
        View mview;

        ImageButton likeMainCommentButton;
        TextView display_replays_likes;
        int count_main_comments_Likes;
        String currentUserIdh;
        DatabaseReference Like_main_comments_Refs;
        public ReplaysViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            likeMainCommentButton = (ImageButton) mview.findViewById(R.id.sub_like_button_comment);
            //commentButton = (ImageButton) mview.findViewById(R.id.comment_button_comments);
            display_replays_likes = (TextView) mview.findViewById(R.id.display_no_of_likes_sub_comments);
            //display_main_comments = (TextView) mview.findViewById(R.id.display_no_of_comment_comments);
            Like_main_comments_Refs = FirebaseDatabase.getInstance().getReference().child("Posts");
            //MainsCommentsRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserIdh = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        public void setLikeReplayButtonStatus(final String PostKey,final String maincommentKey,final String replay_id){
            final int final_count = 10000;
            Like_main_comments_Refs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("Replays").child(replay_id).child("LikesReplays").hasChild(currentUserIdh)){
                        count_main_comments_Likes = (int) dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("Replays").child(replay_id).child("LikesReplays").getChildrenCount();
                        likeMainCommentButton.setImageResource(R.drawable.like);
                        if (count_main_comments_Likes < final_count) {
                            display_replays_likes.setText((Integer.toString(count_main_comments_Likes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (count_main_comments_Likes >= final_count) {
                            display_replays_likes.setText((Integer.toString(count_main_comments_Likes)));
                        }
                    } else {
                        count_main_comments_Likes = (int) dataSnapshot.child(PostKey).child("Maincomments").child(maincommentKey).child("Replays").child(replay_id).child("LikesReplays").getChildrenCount();
                        likeMainCommentButton.setImageResource(R.drawable.dislike);
                        if (count_main_comments_Likes < final_count) {
                            display_replays_likes.setText((Integer.toString(count_main_comments_Likes)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_likes_after_like));
                        } else if (count_main_comments_Likes >= final_count) {
                            display_replays_likes.setText((Integer.toString(count_main_comments_Likes)));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

       /* public void setDisplay_main_comments(final String postkey,final String maincommentKey){
            MainsCommentsRefs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postkey).child("Maincomments").child(maincommentKey).child("Replays").hasChildren()){
                        count_main_Comm = (int)dataSnapshot.child(postkey).child("Maincomments").child(maincommentKey).child("Replays").getChildrenCount();
                        display_main_comments.setText((Integer.toString(count_main_Comm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment_replay));
                    } else {
                        count_main_Comm = 0;
                        display_main_comments.setText((Integer.toString(count_main_Comm)) + " " + getResources().getString(R.string.all_post_layout_display_no_of_comments_after_comment_replay));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/

        public void setTime(String time){
            //TextView commenttime = (TextView) mview.findViewById(R.id.sub_comment_time);
            //commenttime.setText(time);
        }
        public void setDate(String date){
            TextView commentdate = (TextView) mview.findViewById(R.id.sub_comment_date);
            commentdate.setText(date);
        }
        public void setReplay(String replay){
            TextView comm = (TextView) mview.findViewById(R.id.sub_replay_desc);
            comm.setText(replay);
        }

        public void setUid(final Context ctx, String uid){
            DatabaseReference useresRefpost = FirebaseDatabase.getInstance().getReference().child("Users");


            useresRefpost.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String full_Name = dataSnapshot.child("fullname").getValue().toString();
                        TextView username_replay = (TextView) mview.findViewById(R.id.all_users_profile_full_name_sub_comment);
                        username_replay.setText(full_Name);

                        String img_main_comments = dataSnapshot.child("profileimage").getValue().toString();
                        CircleImageView image = (CircleImageView) mview.findViewById(R.id.all_users_profile_image_sub_comment);
                        Picasso.with(ctx).load(img_main_comments).into(image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setDate_time(String date_time){
            TextView commentdate = (TextView) mview.findViewById(R.id.sub_comment_date);
            GetUserDateTime getUserDateTime = new GetUserDateTime(ClickPostActivity.this);
            String date_value = getUserDateTime.getDateToShow(date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
            commentdate.setText(date_value);

        }
    }

    private void AddPostComment(String comment_input_text) {

        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate_order.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
        String savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());

        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTimesecond.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
        String savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

        Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTime.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        String savecurrenttime = currentTime.format(calForTime.getTime());

        Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String savecurrentdate = currentDate.format(calForDate.getTime());

        String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
        String Randomkey = current_userid + savecurrentdate + savecurrenttimesecond;

        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        String order_date = currentorderDate.format(calFororderDate.getTime());

        HashMap commentsMap = new HashMap();
        commentsMap.put("uid", current_userid);
        commentsMap.put("comment", comment_input_text);
        commentsMap.put("date", savecurrentdate);
        commentsMap.put("time", savecurrenttime);
        commentsMap.put("date_time", date_time);
        commentsMap.put("order_date", order_date);
        /*Toast.makeText(ClickPostActivity.this,post_key,Toast.LENGTH_LONG).show();
        Toast.makeText(ClickPostActivity.this,Randomkey,Toast.LENGTH_LONG).show();
        Toast.makeText(ClickPostActivity.this,comment_input_text,Toast.LENGTH_LONG).show();
        Toast.makeText(ClickPostActivity.this,savecurrentdate,Toast.LENGTH_LONG).show();*/


       CommentsRef.child(Randomkey).updateChildren(commentsMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_comment_success),Toast.LENGTH_LONG).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_comment_not_success),Toast.LENGTH_LONG).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_not_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_comment_not_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });

    }

    private void EditCurrentPost(String descriptions) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_post_dailog_title));
        final EditText inputFaild = new EditText(ClickPostActivity.this);
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
                    clickpostRef.child("description").setValue(inputFaild.getText().toString());
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),Toast.LENGTH_SHORT).show();
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
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
        dialog.show();*/
        final Dialog dialog = new Dialog(ClickPostActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                    clickpostRef.child("description").setValue(mDialogdescription.getText().toString());
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_post_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_post_dailog_description_empty),Toast.LENGTH_SHORT).show();
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


    private void DeleteCurrentPost(final String post_image) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_post_dailog_title));
        final TextView inputFaild = new TextView(ClickPostActivity.this);
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
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageurl);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        clickpostRef.removeValue();
                        GoldPostssRef.child(post_key).removeValue();
                        Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_post_deleteted),Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
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



        final Dialog dialog = new Dialog(ClickPostActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().getStorage().getReferenceFromUrl(imageurl);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        clickpostRef.removeValue();
                        GoldPostssRef.child(post_key).removeValue();
                        dialog.dismiss();
                        Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_post_deleteted),Toast.LENGTH_SHORT).show();
                        sendUserToMainActivity();
                    }
                });
            }
        });

        dialog.show();

    }

    private void EditCurrentMainComments(String descriptions, final String post_key, final String maincommentKey) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_title));
        final EditText inputFaild = new EditText(ClickPostActivity.this);
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
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),Toast.LENGTH_SHORT).show();
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



        final Dialog dialog = new Dialog(ClickPostActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mydialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogtitle = dialog.findViewById(R.id.mydialog_title);
        final EditText mDialogdescription = dialog.findViewById(R.id.mydialog_description);
        TextView mDialogmessage = dialog.findViewById(R.id.mydialog_message);
        mDialogtitle.setText(getResources().getString(R.string.click_post_page_edit_main_comments_dailog_title));
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
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.click_post_page_edit_main_comments_dailog_success),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                } else {
                    //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_edit_main_comments_dailog_description_empty),Toast.LENGTH_SHORT).show();
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle(getResources().getString(R.string.click_post_page_delete_main_comments_dailog_title));
        final TextView inputFaild = new TextView(ClickPostActivity.this);
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
                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(ClickPostActivity.this);
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
                //Toast.makeText(ClickPostActivity.this,getResources().getString(R.string.click_post_page_delete_main_comments_dailog_success),Toast.LENGTH_SHORT).show();
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
    private void sendUserToMainActivity() {
        transfer = "true";
        if(activityName.equals("main")){
            Intent intent = new Intent(ClickPostActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(activityName.equals("mypost")){
            Intent intent = new Intent(ClickPostActivity.this,MyPostsActivity.class);
            intent.putExtra("post_uid", post_Uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(activityName.equals("goldposts")){
            Intent intent = new Intent(ClickPostActivity.this,GoldPostsActivity.class);
            //intent.putExtra("PostUid", post_Uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(activityName.equals("notif_likePosts")){
            Intent intent = new Intent(ClickPostActivity.this,NotifLikePostsActivity.class);
            //intent.putExtra("PostUid", post_Uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(activityName.equals("notif_dislikePosts")){
            Intent intent = new Intent(ClickPostActivity.this,NotifdisLikePostsActivity.class);
            //intent.putExtra("PostUid", post_Uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if(activityName.equals("notif_GoldPosts")){
            Intent intent = new Intent(ClickPostActivity.this,NotifGoldPostsActivity.class);
            //intent.putExtra("PostUid", post_Uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //firebaseRecyclerAdapter_maincomments.startListening();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                DisplayAllUsersMainComments();
            } else {
                Toast.makeText(ClickPostActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }

        //commentInputText.requestFocus();
    }

    @Override
    protected void onStop() {

        firebaseRecyclerAdapter_maincomments.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }

        if (LikeRefssValue != null && LikeRefss != null) {
            LikeRefss.removeEventListener(LikeRefssValue);
        }
        if (FriendsSignRefValue != null && FriendsSignRef != null) {
            FriendsSignRef.removeEventListener(FriendsSignRefValue);
        }
        if (CommentsRefValue != null && CommentsRef != null) {
            CommentsRef.removeEventListener(CommentsRefValue);
        }
        if (GoldPostssRefValue != null && GoldPostssRef != null) {
            GoldPostssRef.removeEventListener(GoldPostssRefValue);
        }
        if (DisLikeRefssValue != null && DisLikeRefss != null) {
            DisLikeRefss.removeEventListener(DisLikeRefssValue);
        }

        super.onStop();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*firebaseRecyclerAdapter_maincomments.stopListening();
        if(!transfer.equals("true")){
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
    protected void onResume() {
        super.onResume();
        /*firebaseRecyclerAdapter_maincomments.startListening();
        updateUserState("Online");*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateUserState("Online");
        //DisplayAllUsersMainComments();

        /*commentInputText.setFocusable(true);
        commentInputText.setFocusableInTouchMode(true);
        commentInputText.setFocusable(true);
        commentInputText.setFocusableInTouchMode(true);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }
}
