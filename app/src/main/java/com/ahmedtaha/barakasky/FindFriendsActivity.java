package com.ahmedtaha.barakasky;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FindFriendsActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private ImageButton searchButton;
    private EditText searchInput;
    private RecyclerView searchResultList;
    private TextView emptyInput,nouserFound, simpletextid;

    private FirebaseAuth mAuth;
    private DatabaseReference alluserRef, UsersRef, FrindsCountRef, FrindsCountonlineRef, FriendsSignRef;
    private ValueEventListener alluserRefValue, UsersRefValue,FrindsCountRefVlue, FrindsCountonlineRefValue, FriendsSignRefValue;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter_Search_Find_Friends;

    String currentUserId;
    String isRecyclEmpty = "false", isSearched = "";
    private String transfer = "false";
    //private int rowCount = 0;
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private int counts = 0;
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
        setContentView(R.layout.activity_find_friends_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_find_friends_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        alluserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FrindsCountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FrindsCountonlineRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolBar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
        setSupportActionBar(mToolBar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.find_friends_toolbar_title));
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

        searchResultList = (RecyclerView) findViewById(R.id.search_result_list);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton = (ImageButton) findViewById(R.id.search_people_friends_button);
        searchInput = (EditText) findViewById(R.id.search_box_input);
        emptyInput = (TextView) findViewById(R.id.empty_find_friends_input);
        nouserFound = (TextView) findViewById(R.id.no_user_found_find_friends_input);
        simpletextid = (TextView) findViewById(R.id.simpletextid);

        //waitingBar = new SpotsDialog.Builder().setContext(FindFriendsActivity.this).build();
        //la = Locale.getDefault().getDisplayLanguage();
        mainRoot = (RelativeLayout) findViewById(R.id.main_findfriends_root);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchInputBox = searchInput.getText().toString().trim();
                if(!TextUtils.isEmpty(searchInputBox)){
                    searchResultList.setVisibility(View.VISIBLE);
                    emptyInput.setVisibility(View.GONE);
                    if (searchInputBox.length() >= 2){
                        String isusername = searchInputBox.substring(0 , 1);
                        if (isusername.equals("@")) {
                            String result = searchInputBox.substring(1, Integer.parseInt(String.valueOf(searchInputBox.length())));
                            //Toast.makeText(FindFriendsActivity.this, result, Toast.LENGTH_LONG).show();
                            counts += 1;
                            SearchPeopleAndFriends(result, "username");
                        } else {
                            counts += 1;
                            SearchPeopleAndFriends(searchInputBox, "fullname");
                        }
                    } else {
                        counts += 1;
                        SearchPeopleAndFriends(searchInputBox, "fullname");
                    }

                } else {
                    searchResultList.setVisibility(View.GONE);
                    emptyInput.setVisibility(View.VISIBLE);
                }

                if (counts == 2) {
                    //Toast.makeText(FindFriendsActivity.this, Integer.toString(counts), Toast.LENGTH_LONG).show();
                    MobileAds.initialize(FindFriendsActivity.this, "ca-app-pub-1847297911542220/2987165732");
                    AdView madview = (AdView) findViewById(R.id.findfriends_adviews);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    madview.loadAd(adRequest);
                }
            }
        });


    }

    private void SetCountOfAllOfUsers(){
        FrindsCountRefVlue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final int count = (int)dataSnapshot.getChildrenCount();
                    FrindsCountonlineRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int counts = (int)dataSnapshot.getChildrenCount() - 1;
                            String txt = getResources().getString(R.string.find_friends_simpletextid);
                            simpletextid.setText("");
                            simpletextid.setText(txt + " " + Integer.toString(counts) + " " + getResources().getString(R.string.find_friends_simpletextid_online) + " " + Integer.toString(count) + " " + getResources().getString(R.string.find_friends_simpletextid_users));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    FrindsCountonlineRef.orderByChild("state_type").startAt("Online").endAt("Online" + "\uf8ff").addValueEventListener(FrindsCountonlineRefValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FrindsCountRef.addValueEventListener(FrindsCountRefVlue);
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

    private void SearchPeopleAndFriends(String searchInputBox, String ch) {
        //Toast.makeText(FindFriendsActivity.this,getApplicationContext().getResources().getString(R.string.find_friends_toast_searching),Toast.LENGTH_SHORT).show();
        if (la.equals("العربية")){
            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.find_friends_toast_searching),
                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
        } else {
            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.find_friends_toast_searching),
                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
        }
        isSearched = "searching";
        Query searchQuery = alluserRef.orderByChild(ch).startAt(searchInputBox).endAt(searchInputBox + "\uf8ff");
        FirebaseRecyclerOptions<FindFriends> options = new FirebaseRecyclerOptions.Builder<FindFriends>().setQuery(searchQuery, FindFriends.class).build();

        firebaseRecyclerAdapter_Search_Find_Friends = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindFriendsViewHolder holder, int position, @NonNull FindFriends model) {
                final String useridKey = getRef(position).getKey();
                holder.onlineStatues(useridKey);
                //int ff = 0;
                if(!useridKey.equals(currentUserId)){
                    holder.setFullname(model.getFullname());
                    holder.setStatus(model.getStatus());
                    holder.setProfileimage(getApplicationContext(), model.getProfileimage());
                    //ff++;
                } else {
                    holder.mview.findViewById(R.id.all_user_contener).setVisibility(View.GONE);
                   // ff++;
                }
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transfer = "true";
                        Intent intent = new Intent(FindFriendsActivity.this, PersonProfileActivity.class);
                        intent.putExtra("visit_user_id", useridKey);
                        startActivity(intent);
                    }
                });
                /*TextView nouserFound2 = (TextView) findViewById(R.id.no_user_found_find_friends_input);
                RecyclerView searchResultList2 = (RecyclerView) findViewById(R.id.search_result_list);
                if(ff != 0){
                    nouserFound2.setVisibility(View.GONE);
                    searchResultList2.setVisibility(View.VISIBLE);
                } else {
                    nouserFound2.setVisibility(View.VISIBLE);
                    searchResultList2.setVisibility(View.GONE);
                }*/

                if (!currentUserId.equals(useridKey)){
                    FriendsSignRefValue = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.hasChild(useridKey)){
                                    holder.mview.findViewById(R.id.find_friends_friend_sign).setVisibility(View.VISIBLE);
                                } else {
                                    holder.mview.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                                }
                            } else {
                                holder.mview.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    FriendsSignRef.child(currentUserId).child("Friends").addValueEventListener(FriendsSignRefValue);
                } else {
                    holder.mview.findViewById(R.id.find_friends_friend_sign).setVisibility(View.GONE);
                }
            }

            @Override
            public FindFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_display_layout_normal, parent, false);

                return new FindFriendsViewHolder(view);
            }


        };
        firebaseRecyclerAdapter_Search_Find_Friends.startListening();
        searchResultList.setAdapter(firebaseRecyclerAdapter_Search_Find_Friends);

    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder{
        View mview;
        private DatabaseReference UsersRefs;
        String type;
        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            UsersRefs = FirebaseDatabase.getInstance().getReference().child("Users");
        }
        public void setProfileimage(Context ctx,String profileimage) {
            CircleImageView myImage = (CircleImageView) mview.findViewById(R.id.all_users_profile_image);
            Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
            myImage.setVisibility(View.VISIBLE);
        }

        public void setFullname(String fullname) {
            TextView myFullname = (TextView) mview.findViewById(R.id.all_users_profile_full_name);
            myFullname.setText(fullname);
            myFullname.setVisibility(View.VISIBLE);
        }

        public void setStatus(String status) {
            TextView myStatus = (TextView) mview.findViewById(R.id.all_users_profile_status);
            myStatus.setText(status);
            myStatus.setVisibility(View.VISIBLE);

        }
        public void onlineStatues(String user_id){

            UsersRefs.child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("state_type")){
                        type = dataSnapshot.child("state_type").getValue().toString();
                        if(type.equals("Online")){
                            mview.findViewById(R.id.all_user_online_icon).setVisibility(View.VISIBLE);
                        } else {
                            mview.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                        }
                    } else {
                        mview.findViewById(R.id.all_user_online_icon).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        if(!TextUtils.isEmpty(isSearched)){
            firebaseRecyclerAdapter_Search_Find_Friends.stopListening();
        }


        if (FrindsCountRefVlue != null && FrindsCountRef != null) {
            FrindsCountRef.removeEventListener(FrindsCountRefVlue);
        }
        if (FrindsCountonlineRefValue != null && FrindsCountonlineRef != null) {
            FrindsCountonlineRef.removeEventListener(FrindsCountonlineRefValue);
        }
        if (FriendsSignRefValue != null && FriendsSignRef != null) {
            FriendsSignRef.removeEventListener(FriendsSignRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/
        //firebaseRecyclerAdapter_Search_Find_Friends.stopListening();
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
        updateUserState("Online");
        //firebaseRecyclerAdapter_Search_Find_Friends.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                SetCountOfAllOfUsers();
            } else {
                Toast.makeText(FindFriendsActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }

        //firebaseRecyclerAdapter_Search_Find_Friends.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateUserState("Online");
    }
}
