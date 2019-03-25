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

public class RecivedFriendsRequestActivity extends AppCompatActivity {
    private RecyclerView myRecivedFriendsRequestList;
    private TextView recivedFriendsRequestText;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, RecivedFriendsRequestRef, FriendRequestCountRef, FriendRequestRef, FriendRequestCountRecivedRef, FriendsRef;
    private ValueEventListener UsersRefValue, RecivedFriendsRequestRefValue, FriendRequestCountRefValue, FriendRequestRefValue, FriendRequestCountRecivedRefValue, FriendsRefValue;
    private String current_user_id, transfer = "false";

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
        setContentView(R.layout.activity_recived_friends_request_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_recived_friends_request_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RecivedFriendsRequestRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id).child("FriendRequests");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRequestCountRecivedRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myRecivedFriendsRequestList = (RecyclerView) findViewById(R.id.recived_friends_request_list);
        recivedFriendsRequestText = (TextView) findViewById(R.id.recived_f_r_text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myRecivedFriendsRequestList.setLayoutManager(linearLayoutManager);
        //waitingBar = new SpotsDialog.Builder().setContext(RecivedFriendsRequestActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_recivedfriendsrequests_root);

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
        UsersRef.child(current_user_id).updateChildren(hashMap);
    }

    private void SetCountOfRecivedFriendsRequests() {
        FriendRequestCountRecivedRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("FriendRequestsCount")){
                    if (dataSnapshot.child("FriendRequestsCount").hasChild("recived_count")){
                        int count = (int)dataSnapshot.child("FriendRequestsCount").child("recived_count").getChildrenCount();
                        String partone = getResources().getString(R.string.recived_friends_request_text_view_before);
                        String parttwo = getResources().getString(R.string.recived_friends_request_text_view_after);
                        recivedFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                    } else {
                        int count = 0;
                        String partone = getResources().getString(R.string.recived_friends_request_text_view_before);
                        String parttwo = getResources().getString(R.string.recived_friends_request_text_view_after);
                        recivedFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                    }
                } else {
                    int count = 0;
                    String partone = getResources().getString(R.string.recived_friends_request_text_view_before);
                    String parttwo = getResources().getString(R.string.recived_friends_request_text_view_after);
                    recivedFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendRequestCountRecivedRef.child(current_user_id).addValueEventListener(FriendRequestCountRecivedRefValue);
    }

    private void DisplayAllRecivedFriendsRequests() {
        FirebaseRecyclerOptions<RecivedFriendsRequests> options = new FirebaseRecyclerOptions.Builder<RecivedFriendsRequests>().setQuery(RecivedFriendsRequestRef.orderByChild("request_type").startAt("received").endAt("received" + "\uf8ff"), RecivedFriendsRequests.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RecivedFriendsRequests, RecivedFriendsRequestActivity.RecivedFriendsRequestsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull RecivedFriendsRequestActivity.RecivedFriendsRequestsViewHolder holder, int position, @NonNull RecivedFriendsRequests model) {
                final String usersIds = getRef(position).getKey();
                holder.SetData(getApplicationContext(),usersIds);
                holder.mView.findViewById(R.id.person_decline_recived_friends_request_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendRequestRef.child(current_user_id).child("FriendRequests").child(usersIds).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FriendRequestRef.child(usersIds).child("FriendRequests").child(current_user_id).removeValue();
                                }
                            }
                        });

                        FriendRequestCountRef.child(current_user_id).child("FriendRequestsCount").child("recived_count").child(usersIds).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FriendRequestCountRef.child(usersIds).child("FriendRequestsCount").child("sent_count").child(current_user_id).removeValue();
                            }
                        });
                    }
                });

                holder.mView.findViewById(R.id.person_accept_recived_friends_request_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

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

                        Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        //calForDate.add(Calendar.HOUR, 2);
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String savecurrentdate = currentDate.format(calForDate.getTime());

                        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                        String order_date = currentorderDate.format(calFororderDate.getTime());

                        String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
                        final HashMap hashMap = new HashMap();
                        hashMap.put("date", savecurrentdate);
                        hashMap.put("date_time", date_time);
                        hashMap.put("order_date", order_date);
                        FriendsRef.child(current_user_id).child("Friends").child(usersIds).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    FriendsRef.child(usersIds).child("Friends").child(current_user_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                FriendRequestRef.child(current_user_id).child("FriendRequests").child(usersIds).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            FriendRequestRef.child(usersIds).child("FriendRequests").child(current_user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        FriendRequestCountRef.child(current_user_id).child("FriendRequestsCount").child("recived_count").child(usersIds).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FriendRequestCountRef.child(usersIds).child("FriendRequestsCount").child("sent_count").child(current_user_id).removeValue();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            }



                                        }
                                    });
                                }
                            }
                        });
                    }
                });

                holder.mView.findViewById(R.id.all_users_recived_friends_request_profile_full_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_user_id.equals(usersIds)){
                            transfer = "true";
                            Intent intent = new Intent(RecivedFriendsRequestActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(RecivedFriendsRequestActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", usersIds);
                            startActivity(intent);
                        }

                    }
                });
                holder.mView.findViewById(R.id.all_users_recived_friends_request_profile_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_user_id.equals(usersIds)){
                            transfer = "true";
                            Intent intent = new Intent(RecivedFriendsRequestActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(RecivedFriendsRequestActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", usersIds);
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public RecivedFriendsRequestActivity.RecivedFriendsRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_recived_friends_request_display_layout_normal, parent, false);

                return new RecivedFriendsRequestActivity.RecivedFriendsRequestsViewHolder(view);
            }


        };
        firebaseRecyclerAdapter.startListening();
        myRecivedFriendsRequestList.setAdapter(firebaseRecyclerAdapter);

    }

    public class RecivedFriendsRequestsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        private DatabaseReference UserssRefs;
        String type;
        public RecivedFriendsRequestsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            UserssRefs = FirebaseDatabase.getInstance().getReference().child("Users");

        }
        public void SetData(final Context ctx, String uid){
            UserssRefs.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                        TextView myFullname = (TextView) mView.findViewById(R.id.all_users_recived_friends_request_profile_full_name);
                        myFullname.setText(fullname);
                        CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_recived_friends_request_profile_image);
                        Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
                SetCountOfRecivedFriendsRequests();
                DisplayAllRecivedFriendsRequests();
            } else {
                Toast.makeText(RecivedFriendsRequestActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
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

        if (FriendRequestCountRecivedRefValue != null && FriendRequestCountRecivedRef != null) {
            FriendRequestCountRecivedRef.removeEventListener(FriendRequestCountRecivedRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DisplayAllRecivedFriendsRequests();
        updateUserState("Online");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserState("Online");
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
    }
}
