package com.ahmedtaha.barakasky;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FriendsActivity extends AppCompatActivity {
    private RecyclerView myFriendsList;
    private TextView simpletextid, friendsCountOnlineText;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, useresRef, FriendsRef, FrindsCountRef, FrindsCountonlineRef;
    private ValueEventListener UsersRefValue, FriendsRefValue,FrindsCountRefVlue, FrindsCountonlineRefValue;
    private String online_user_id, transfer = "false";

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    String current_user_country_Lives;

    private AlertDialog waitingBar;
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
        setContentView(R.layout.activity_friends_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_friends_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id).child("Friends");
        FrindsCountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FrindsCountonlineRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myFriendsList = (RecyclerView) findViewById(R.id.friends_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendsList.setLayoutManager(linearLayoutManager);
        simpletextid = (TextView) findViewById(R.id.all_friends_textsimple);
        friendsCountOnlineText = (TextView) findViewById(R.id.all_friends_count_online);




        //updateUserState("Online");
    }

    private void getCurrentUserCountryLives(){
        useresRef.child(online_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void setCountOfUsersOnline(){
        FrindsCountRefVlue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final int count = (int)dataSnapshot.getChildrenCount();
                    final Map<String,String> online_friendsMap = new HashMap<>();
                    for (DataSnapshot fUser : dataSnapshot.getChildren()){
                        final String friend_id = fUser.getRef().getKey();
                        FrindsCountonlineRefValue = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                if (dataSnapshots.exists()){
                                    if (dataSnapshots.hasChild("state_type")){
                                        String ty = dataSnapshots.child("state_type").getValue().toString();
                                        if (ty.equals("Online")){
                                            if (!online_friendsMap.containsKey(friend_id)){
                                                online_friendsMap.put(friend_id,"Online");
                                            }
                                        } else {
                                            if (online_friendsMap.containsKey(friend_id)){
                                                online_friendsMap.remove(friend_id);
                                            }
                                        }

                                    }
                                    String countonline = Integer.toString(online_friendsMap.size());
                                    simpletextid.setText(getResources().getString(R.string.friends_text_view_friends) + " " + countonline + " " + getResources().getString(R.string.friends_text_view_friends_online) + " " + count + " " + getResources().getString(R.string.friends_text_view_friends_online_after));

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        FrindsCountonlineRef.child(friend_id).addValueEventListener(FrindsCountonlineRefValue);
                    }


                    //String countonline = friendsCountOnlineText.getText().toString();
                    //  Toast.makeText(FriendsActivity.this, countonline, Toast.LENGTH_LONG).show();
                    //    simpletextid.setText(getResources().getString(R.string.friends_text_view_friends) + " " + countonline + " " + getResources().getString(R.string.friends_text_view_friends_online) + " " + count + " " + getResources().getString(R.string.friends_text_view_friends_online_after));
                } else {
                    simpletextid.setText(getResources().getString(R.string.friends_text_view_friends) + " " + "0" + " " + getResources().getString(R.string.friends_text_view_friends_online_after));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FrindsCountRef.child(online_user_id).child("Friends").addValueEventListener(FrindsCountRefVlue);
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
        UsersRef.child(online_user_id).updateChildren(hashMap);
    }

    private void DisplayAllFriends() {
        FirebaseRecyclerOptions<Friends> options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(FriendsRef.orderByChild("order_date"), Friends.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(options) {

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout_normal, parent, false);

                return new FriendsActivity.FriendsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Friends model) {
                final String usersIds = getRef(position).getKey();
                //holder.setDate(model.getDate());
                holder.setDate_time(model.getDate_time());
                UsersRefValue = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            final String fullname = dataSnapshot.child("fullname").getValue().toString();
                            final String profile_image = dataSnapshot.child("profileimage").getValue().toString();
                            final String type;
                            /*if(dataSnapshot.hasChild("UserState")){
                                type = dataSnapshot.child("UserState").child("type").getValue().toString();
                                if(type.equals("Online")){
                                    holder.onlineStateView.setVisibility(View.VISIBLE);
                                } else {
                                    holder.onlineStateView.setVisibility(View.GONE);
                                }
                            }*/
                            //holder.onlineStatues(usersIds);
                            if(dataSnapshot.hasChild("state_type")){
                                type = dataSnapshot.child("state_type").getValue().toString();
                                if(type.equals("Online")){
                                    holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.VISIBLE);
                                } else {
                                    holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                                }
                            } else {
                                holder.mView.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                            }
                            holder.setFullname(fullname);
                            holder.setProfileimage(getApplicationContext(), profile_image);
                            holder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CharSequence options[] = new CharSequence[]{
                                            fullname + getResources().getString(R.string.friends_visit_friend_profile),
                                            getResources().getString(R.string.friends_send_message_to_friend)
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                                    builder.setTitle(getResources().getString(R.string.friends_dailog_title));
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which == 0){
                                                transfer = "true";
                                                Intent intent = new Intent(FriendsActivity.this, PersonProfileActivity.class);
                                                intent.putExtra("visit_user_id", usersIds);
                                                startActivity(intent);
                                                //transfer = "false";
                                            }
                                            if(which == 1){
                                                transfer = "true";
                                                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                                                intent.putExtra("visit_user_id", usersIds);
                                                intent.putExtra("username", fullname);
                                                intent.putExtra("country_lives", current_user_country_Lives);
                                                startActivity(intent);
                                                //transfer = "false";
                                            }
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                UsersRef.child(usersIds).addValueEventListener(UsersRefValue);
            }
        };
        firebaseRecyclerAdapter.startListening();
        myFriendsList.setAdapter(firebaseRecyclerAdapter);

    }

    public class FriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CircleImageView onlineStateView;
        private DatabaseReference UsersRefs;
        String type;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            UsersRefs = FirebaseDatabase.getInstance().getReference().child("Users");
            onlineStateView = (CircleImageView) itemView.findViewById(R.id.all_user_online_icon);
        }
        public void onlineStatues(String user_id){

            UsersRefs.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("state_type")){
                        type = dataSnapshot.child("state_type").getValue().toString();
                        if(type.equals("Online")){
                            mView.findViewById(R.id.all_user_online_icon).setVisibility(View.VISIBLE);
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
        /*public void setDate(String date) {
            TextView friendsDate = (TextView) mView.findViewById(R.id.all_users_profile_status);
            friendsDate.setText(getResources().getString(R.string.friends_text_view_friends_date) + " " + date);
            friendsDate.setVisibility(View.VISIBLE);

        }*/

        public void setDate_time(String date_time) {
            TextView friendsDate = (TextView) mView.findViewById(R.id.all_users_profile_status);
            GetUserDateTime getUserDateTime = new GetUserDateTime(FriendsActivity.this);
            String date_value = getUserDateTime.getDateToShow(date_time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "MMM dd-yyyy");
            friendsDate.setText(getResources().getString(R.string.friends_text_view_friends_date) + " " + date_value);
            friendsDate.setVisibility(View.VISIBLE);



        }
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
                setCountOfUsersOnline();
                DisplayAllFriends();
            } else {
                Toast.makeText(FriendsActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
        //firebaseRecyclerAdapter.startListening();

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

        if (UsersRefValue != null && UsersRef != null) {
            UsersRef.removeEventListener(UsersRefValue);
        }
        if (FrindsCountRefVlue != null && FrindsCountRef != null) {
            FrindsCountRef.removeEventListener(FrindsCountRefVlue);
        }
        if (FrindsCountonlineRefValue != null && FrindsCountonlineRef != null) {
            FrindsCountonlineRef.removeEventListener(FrindsCountonlineRefValue);
        }
        Runtime.getRuntime().gc();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //firebaseRecyclerAdapter.stopListening();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* DisplayAllFriends();
        updateUserState("Online");
        transfer = "false";*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*firebaseRecyclerAdapter.startListening();
        //DisplayAllFriends();
        updateUserState("Online");
        transfer = "false";*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/

    }

}
