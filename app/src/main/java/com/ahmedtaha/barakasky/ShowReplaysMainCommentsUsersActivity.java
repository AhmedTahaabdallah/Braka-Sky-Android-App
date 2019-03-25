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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowReplaysMainCommentsUsersActivity extends AppCompatActivity {
    private RecyclerView myFriendsList;
    private TextView simpletextid;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, usergetRef, PostssRef, UserFullnameRef, FriendsSignRef;
    private ValueEventListener UsersRefValue, usergetRefValue, PostssRefValue, UserFullnameRefValue, FriendsSignRefValue;
    private String post_key, post_uid, ccpost_key, current_uid, transfer = "false";

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
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
        setContentView(R.layout.activity_show_replays_main_comments_users_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_show_replays_main_comments_users_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        post_key = getIntent().getExtras().get("PostKey").toString();
        post_uid = getIntent().getExtras().get("PostUid").toString();
        ccpost_key = getIntent().getExtras().get("cPost_key").toString();

        mAuth = FirebaseAuth.getInstance();
        current_uid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usergetRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostssRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(post_key).child("Maincomments").child(ccpost_key).child("Replays");
        UserFullnameRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //waitingBar = new SpotsDialog.Builder().setContext(ShowReplaysMainCommentsUsersActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_showreplaysmaincomments_root);

        myFriendsList = (RecyclerView) findViewById(R.id.show_replays_main_comments_users_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendsList.setLayoutManager(linearLayoutManager);
        simpletextid = (TextView) findViewById(R.id.all_show_replays_main_comments_users_textsimple);


    }

    private void getUserFullName(){
        UserFullnameRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userfullname = dataSnapshot.child("fullname").getValue().toString();
                    String fi = getResources().getString(R.string.show_replays_main_comments_users_simpletext_before) + " " + userfullname + " " + getResources().getString(R.string.show_replays_main_comments_users_simpletext_after);
                    simpletextid.setText(fi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        UserFullnameRef.child(post_uid).addValueEventListener(UserFullnameRefValue);
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
        UsersRef.child(current_uid).updateChildren(hashMap);
    }

    private void DisplayAllLikesPostUsers() {
        //final List<String> list;
        //list = new ArrayList<>();
        FirebaseRecyclerOptions<ShowUsers> options = new FirebaseRecyclerOptions.Builder<ShowUsers>().setQuery(PostssRef.orderByChild("order_date"), ShowUsers.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShowUsers, ShowReplaysMainCommentsUsersActivity.ShowUsersViewHolder>(options) {

            @NonNull
            @Override
            public ShowReplaysMainCommentsUsersActivity.ShowUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout_normal, parent, false);

                return new ShowReplaysMainCommentsUsersActivity.ShowUsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ShowReplaysMainCommentsUsersActivity.ShowUsersViewHolder holder, int position, @NonNull ShowUsers model) {
                final String cpost_key = getRef(position).getKey();

                //final String usersIds;
                PostssRefValue = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String usersIds = dataSnapshot.child("uid").getValue().toString();

                            if (!current_uid.equals(usersIds)){
                                FriendsSignRefValue = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            if (dataSnapshot.hasChild(usersIds)){
                                                holder.mView.findViewById(R.id.find_friends_friend_sign).setVisibility(View.VISIBLE);
                                            } else {
                                                holder.mView.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.mView.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                };
                                FriendsSignRef.child(current_uid).child("Friends").addValueEventListener(FriendsSignRefValue);
                            } else {
                                holder.mView.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                            }

                            usergetRefValue = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   /* Boolean sts = false;
                                    for (String curVal : list){
                                        if (curVal.contains(usersIds)){
                                            sts = true;
                                        }
                                    }*/

                                    if(dataSnapshot.exists()){
                                        final String fullname = dataSnapshot.child("fullname").getValue().toString();
                                        final String profile_image = dataSnapshot.child("profileimage").getValue().toString();
                                        final String st = dataSnapshot.child("status").getValue().toString();
                                        holder.onlineStatues(usersIds, current_uid);
                                        //final String type;
                                        /*if(dataSnapshot.hasChild("UserState")){
                                            type = dataSnapshot.child("UserState").child("type").getValue().toString();
                                            if(type.equals("Online")){
                                                holder.onlineStateView.setVisibility(View.VISIBLE);
                                            } else {
                                                holder.onlineStateView.setVisibility(View.GONE);
                                            }
                                        }*/
                                        //holder.onlineStatues(usersIds);
                                        /*if(dataSnapshot.hasChild("state_type")){
                                            type = dataSnapshot.child("state_type").getValue().toString();
                                            if(type.equals("Online")){
                                                holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.VISIBLE);
                                            } else {
                                                holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                                        }*/
                                        holder.setFullname(fullname);
                                        holder.setProfileimage(getApplicationContext(), profile_image);
                                        holder.setStatus(st);
                                        holder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (current_uid.equals(usersIds)){
                                                    transfer = "true";
                                                    Intent intent = new Intent(ShowReplaysMainCommentsUsersActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    transfer = "true";
                                                    Intent intent = new Intent(ShowReplaysMainCommentsUsersActivity.this, PersonProfileActivity.class);
                                                    intent.putExtra("visit_user_id", usersIds);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        holder.mView.findViewById(R.id.all_user_contener).setVisibility(View.VISIBLE);
                                    } else {
                                        holder.mView.findViewById(R.id.all_user_contener).setVisibility(View.GONE);
                                        //PostssRef.child(usersIds).removeValue();
                                    }
                                    //list.add(usersIds);
                                    //sts = false;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            usergetRef.child(usersIds).addValueEventListener(usergetRefValue);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                PostssRef.child(cpost_key).addValueEventListener(PostssRefValue);


            }
        };
        firebaseRecyclerAdapter.startListening();
        myFriendsList.setAdapter(firebaseRecyclerAdapter);

    }

    public class ShowUsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        //CircleImageView onlineStateView;
        private DatabaseReference UsersRefs;
        String type;
        public ShowUsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            UsersRefs = FirebaseDatabase.getInstance().getReference().child("Users");
            //onlineStateView = (CircleImageView) itemView.findViewById(R.id.all_user_online_icon);
        }

        public void setProfileimage(Context ctx, String profileimage) {
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);
            Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
            myImage.setVisibility(View.VISIBLE);
        }

        public void setFullname(String fullname) {
            TextView myFullname = (TextView) mView.findViewById(R.id.all_users_profile_full_name);
            myFullname.setText(fullname);
            myFullname.setVisibility(View.VISIBLE);
        }
        public void setStatus(String st) {
            TextView friendsDate = (TextView) mView.findViewById(R.id.all_users_profile_status);
            friendsDate.setText(st);
            friendsDate.setVisibility(View.VISIBLE);

        }

        public void onlineStatues(final String user_id, final String cu_id){

            UsersRefs.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("state_type")){
                        type = dataSnapshot.child("state_type").getValue().toString();
                        if(type.equals("Online")){
                            if (user_id.equals(cu_id)){
                                mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                            } else {
                                mView.findViewById(R.id.all_user_online_icon).setVisibility(View.VISIBLE);
                            }

                        } else {
                            mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                        }
                    } else {
                        mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        if (UserFullnameRefValue != null && UserFullnameRef != null) {
            UserFullnameRef.removeEventListener(UserFullnameRefValue);
        }
        if (UsersRefValue != null && UsersRef != null) {
            UsersRef.removeEventListener(UsersRefValue);
        }
        if (PostssRefValue != null && PostssRef != null) {
            PostssRef.removeEventListener(PostssRefValue);
        }
        if (usergetRefValue != null && usergetRef != null) {
            usergetRef.removeEventListener(usergetRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                getUserFullName();
                DisplayAllLikesPostUsers();
            } else {
                Toast.makeText(ShowReplaysMainCommentsUsersActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
        //firebaseRecyclerAdapter.startListening();

    }
}
