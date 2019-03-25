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
import android.support.design.widget.Snackbar;
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

import com.bvapp.directionalsnackbar.SnackbarUtil;
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
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BookFriendsActivity extends AppCompatActivity {
    private RecyclerView mySentFriendsRequestList;
    private TextView sentFriendsRequestText;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, BookedFreindsRef, BookedFreindsCountRef, BookButtonRef;
    private ValueEventListener UsersRefValue, BookedFreindsRefValue, BookedFreindsCountRefValue, BookButtonRefValue;
    private String current_user_id, transfer = "false";
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

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
        setContentView(R.layout.activity_book_friends_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_book_friends_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        BookedFreindsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id).child("BookPersons");
        BookedFreindsCountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        BookButtonRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //waitingBar = new SpotsDialog.Builder().setContext(BookFriendsActivity.this).build();
        //la = Locale.getDefault().getDisplayLanguage();

        mainRoot = (RelativeLayout) findViewById(R.id.main_book_root);
        mySentFriendsRequestList = (RecyclerView) findViewById(R.id.booked_friends_list);
        sentFriendsRequestText = (TextView) findViewById(R.id.booked_txt);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mySentFriendsRequestList.setLayoutManager(linearLayoutManager);


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

    private void SetCountOfBookedFriends() {
        BookedFreindsCountRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("BookPersons")){
                    if (dataSnapshot.child("BookPersons").hasChildren()){
                        int count = (int)dataSnapshot.child("BookPersons").getChildrenCount();
                        String partone = getResources().getString(R.string.book_friends_text_view_before);
                        String parttwo = getResources().getString(R.string.book_friends_text_view_after);
                        sentFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                    } else {
                        int count = 0;
                        String partone = getResources().getString(R.string.book_friends_text_view_before);
                        String parttwo = getResources().getString(R.string.book_friends_text_view_after);
                        sentFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                    }
                } else {
                    int count = 0;
                    String partone = getResources().getString(R.string.book_friends_text_view_before);
                    String parttwo = getResources().getString(R.string.book_friends_text_view_after);
                    sentFriendsRequestText.setText(partone + " " + Integer.toString(count) + " " + parttwo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        BookedFreindsCountRef.child(current_user_id).addValueEventListener(BookedFreindsCountRefValue);
    }

    private void DisplayAllBookedFriends() {
        FirebaseRecyclerOptions<BookedFriends> options = new FirebaseRecyclerOptions.Builder<BookedFriends>().setQuery(BookedFreindsRef.orderByChild("order_date"), BookedFriends.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookedFriends, BookFriendsActivity.BookedFriendsViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final BookFriendsActivity.BookedFriendsViewHolder holder, int position, @NonNull BookedFriends model) {
                final String usersIds = getRef(position).getKey();
                holder.SetData(getApplicationContext(), usersIds, current_user_id);
                //holder.setBookFriendsSign(usersIds,current_user_id);

                holder.mView.findViewById(R.id.all_users_booked_friends_unbooked_friend_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BookButtonRef.child(current_user_id).child("BookPersons").child(usersIds).removeValue();
                    }
                });

                holder.mView.findViewById(R.id.all_users_booked_friends_profile_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_user_id.equals(usersIds)){
                            transfer = "true";
                            Intent intent = new Intent(BookFriendsActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(BookFriendsActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", usersIds);
                            startActivity(intent);
                        }

                    }
                });
                holder.mView.findViewById(R.id.all_users_booked_friends_profile_full_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (current_user_id.equals(usersIds)){
                            transfer = "true";
                            Intent intent = new Intent(BookFriendsActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            transfer = "true";
                            Intent intent = new Intent(BookFriendsActivity.this, PersonProfileActivity.class);
                            intent.putExtra("visit_user_id", usersIds);
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public BookFriendsActivity.BookedFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_booked_friends_display_layout_normal, parent, false);

                return new BookFriendsActivity.BookedFriendsViewHolder(view);
            }


        };
        firebaseRecyclerAdapter.startListening();
        mySentFriendsRequestList.setAdapter(firebaseRecyclerAdapter);

    }

    public class BookedFriendsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private DatabaseReference UserssRefs, FriendsSignRef;
        private ValueEventListener FriendsSignRefValue;
        String type;

        public BookedFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            UserssRefs = FirebaseDatabase.getInstance().getReference().child("Users");
            FriendsSignRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }
        public void SetData(final Context ctx, final String uid, final String cur_user_id){

            UserssRefs.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                        TextView myFullname = (TextView) mView.findViewById(R.id.all_users_booked_friends_profile_full_name);
                        myFullname.setText(fullname);
                        CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.all_users_booked_friends_profile_image);
                        Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FriendsSignRefValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.hasChild(uid)){
                            mView.findViewById(R.id.book_friend_sign).setVisibility(View.VISIBLE);
                        } else {
                            mView.findViewById(R.id.book_friend_sign).setVisibility(View.GONE);
                        }
                    } else {
                        mView.findViewById(R.id.book_friend_sign).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            FriendsSignRef.child(cur_user_id).child("Friends").addValueEventListener(FriendsSignRefValue);
        }

        /*public void setBookFriendsSign(final String usersIdss, final String cur_user_id){
            FriendsSignRefValue = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.hasChild(usersIdss)){
                            mView.findViewById(R.id.book_friend_sign).setVisibility(View.VISIBLE);
                        } else {
                            mView.findViewById(R.id.book_friend_sign).setVisibility(View.GONE);
                        }
                    } else {
                        mView.findViewById(R.id.book_friend_sign).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            FriendsSignRef.child(cur_user_id).child("Friends").addValueEventListener(FriendsSignRefValue);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
                DisplayAllBookedFriends();
                SetCountOfBookedFriends();
            } else {
                Toast.makeText(BookFriendsActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                //Snackbar.make(mainRoot, "Please Select  automatic date & time from Settings", Snackbar.LENGTH_LONG).show();
                /*if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "Please Select  automatic date & time from Settings",
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, "Please Select  automatic date & time from Settings",
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }*/
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

        if (BookedFreindsCountRefValue != null && BookedFreindsCountRef != null) {
            BookedFreindsCountRef.removeEventListener(BookedFreindsCountRefValue);
        }
        Runtime.getRuntime().gc();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.stopListening();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
    }*/


}
