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
import android.widget.LinearLayout;
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
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAllMessagesActivity extends AppCompatActivity {
    private RecyclerView allMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference useresRef ,UsersRef, FriendsRef, MessagesRef;
    private ValueEventListener UsersRefValue, MessagesRefValue;
    private String current_user_id, transfer = "false";

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    String current_user_country_Lives;

    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private int usermessage_textsize = 0
            , usermessage_paddingleft = 0
            , usermessage_paddingright = 0
            , usermessage_paddingtop = 0
            , usermessage_paddingbottom = 0
            , usermessage_marginleft = 0
            , usermessage_marginright = 0
            , usermessage_margintop = 0
            , usermessage_marginbottom = 0
            , useronlineicon_marginleft = 0
            , useronlineicon_marginright = 0
            , useronlineicon_margintop = 0
            , useronlineicon_marginbottom = 0;


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
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_show_all_messages_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_show_all_messages_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("ShowAllMessage").child(current_user_id);

        //waitingBar = new SpotsDialog.Builder().setContext(ShowAllMessagesActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_showallmessages_root);
        /*if (la.equals("العربية")){
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;

            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
        } else {
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 12;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                usermessage_textsize = 14;
                usermessage_paddingbottom = 0;
                usermessage_paddingleft = 0;
                usermessage_paddingright = 0;
                usermessage_paddingtop = 0;
                usermessage_marginbottom = 1;
                usermessage_marginleft = 0;
                usermessage_marginright = 0;
                usermessage_margintop = 15;
            }
        }*/
        allMessagesList = (RecyclerView) findViewById(R.id.show_all_messages_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        allMessagesList.setLayoutManager(linearLayoutManager);

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
    private void getCurrentUserCountryLives(){
        useresRef.child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
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
    private void DisplayAllMessages() {
        FirebaseRecyclerOptions<ShowAllMessages> options = new FirebaseRecyclerOptions.Builder<ShowAllMessages>().setQuery(MessagesRef.orderByChild("order_date"), ShowAllMessages.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShowAllMessages, ShowAllMessagesActivity.ShowAllMessagesViewHolder>(options) {

            @NonNull
            @Override
            public ShowAllMessagesActivity.ShowAllMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_show_messages_display_layout_normal, parent, false);

                return new ShowAllMessagesActivity.ShowAllMessagesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ShowAllMessagesActivity.ShowAllMessagesViewHolder holder, int position, @NonNull ShowAllMessages model) {
                final String usersIds = getRef(position).getKey();
                holder.setMessage(model.getMessage());
                holder.SetData(getApplicationContext(),usersIds);
                holder.onlineStatues(usersIds);
                holder.ShowAllcountMessagesOfUser(current_user_id,usersIds);

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fname = holder.user_full_name;
                        transfer = "true";
                        Intent intent = new Intent(ShowAllMessagesActivity.this, ChatActivity.class);
                        intent.putExtra("visit_user_id", usersIds);
                        intent.putExtra("username", fname);
                        intent.putExtra("country_lives", current_user_country_Lives);
                        startActivity(intent);
                    }
                });
            }
        };
        firebaseRecyclerAdapter.startListening();
        allMessagesList.setAdapter(firebaseRecyclerAdapter);

    }

    public class ShowAllMessagesViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CircleImageView onlineStateView;
        TextView number_not_seen;
        private DatabaseReference UsersRefs;
        String type;
        public String user_full_name;
        public ShowAllMessagesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            UsersRefs = FirebaseDatabase.getInstance().getReference().child("Users");
            onlineStateView = (CircleImageView) itemView.findViewById(R.id.all_user_show_messages_online_icon);
            number_not_seen = (TextView) itemView.findViewById(R.id.all_users_show_messages_number_of_not_seen_message);
        }
        public void onlineStatues(String user_id){

            UsersRefs.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("state_type")){
                        type = dataSnapshot.child("state_type").getValue().toString();
                        if(type.equals("Online")){
                            mView.findViewById(R.id.all_user_show_messages_online_icon).setVisibility(View.VISIBLE);
                            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(useronlineicon_marginleft, useronlineicon_margintop, useronlineicon_marginright, useronlineicon_marginbottom);
                            onlineStateView.setLayoutParams(params);*/
                        } else {
                            mView.findViewById(R.id.all_user_show_messages_online_icon).setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        public void SetData(final Context ctx, String uid){
            UsersRefs.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                        user_full_name = fullname;
                        TextView myFullname = (TextView) mView.findViewById(R.id.all_users_show_messages_profile_full_name);
                        myFullname.setText(fullname);
                        CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_show_messages_profile_image);
                        Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        public void setMessage(String message){
            TextView laset_message = (TextView) mView.findViewById(R.id.all_users_show_messages_last_message);
            laset_message.setText(message);
        }

        public void ShowAllcountMessagesOfUser(String curr_uid, String re_uid){
            UsersRefs.child(curr_uid).child("NotSeenMessageCountUsers").child(re_uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()){
                        int count = (int)dataSnapshot.getChildrenCount();
                        number_not_seen.setText(Integer.toString(count));
                        number_not_seen.setVisibility(View.VISIBLE);
                        number_not_seen.setBackground(getResources().getDrawable(R.drawable.circle_background_views_messages));

                        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) number_not_seen.getLayoutParams();
                        params.setMargins(usermessage_marginleft,usermessage_margintop,usermessage_marginright,usermessage_marginbottom);

                        number_not_seen.setLayoutParams(params);
                        number_not_seen.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        number_not_seen.setPadding(usermessage_paddingleft,usermessage_paddingtop,usermessage_paddingright,usermessage_paddingbottom);
                        number_not_seen.setTextSize(usermessage_textsize);*/
                    } else {
                        number_not_seen.setText("");
                        number_not_seen.setVisibility(View.INVISIBLE);
                        number_not_seen.setBackgroundColor(getResources().getColor(R.color.colorprofile));
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
        //firebaseRecyclerAdapter.startListening();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                getCurrentUserCountryLives();
                DisplayAllMessages();
            } else {
                Toast.makeText(ShowAllMessagesActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

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
        Runtime.getRuntime().gc();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*DisplayAllMessages();
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
