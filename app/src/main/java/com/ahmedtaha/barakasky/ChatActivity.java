package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChatActivity extends AppCompatActivity {
    private Toolbar chattoolbar;
    private ImageButton sendMessageButton, sendImageFileButton;
    private EditText userMessageInput;
    private RecyclerView userMessageList;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter messagesAdapter;
    //private ProgressDialog loadingBar;

    private String messageReciverId, messageReciverName, messageSenderId;
    private String savecurrentdate, savecurrentdate_order, savecurrenttime, savecurrenttimesecond;
    private TextView reciverName, userLastSeen;
    private CircleImageView reciverProfileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference useresRef, RootRef, UsersRef, MessagesRef, countMessagesRef, notseenMessagesRef, FriendsRef;
    private ValueEventListener RootRefValue, UsersRefValue, MessagesRefValue, countMessagesRefValue, notseenMessagesRefValue, FriendsRefValue;
    private StorageReference UserChatImageRef;
    String transfer = "false";
    final static int Garrely_pick = 1;
    String current_user_country_Lives, current_user_country_Liv;
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private int kk = 0;
    private String isSt = "start";
    private InterstitialAd interstitialAd;
    private int sendCount = 0;
    private int reciveCount = 0;
    private Dialog mydialog;

    private int username_marginleft = 0
            , username_marginright = 0
            , username_margintop = 0
            , username_marginbottom = 0
            , userlastseen_marginleft = 0
            , userlastseen_marginright = 0
            , userlastseen_margintop = 0
            , userlastseen_marginbottom = 0;
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
        setContentView(R.layout.activity_chat_normal);
        /*interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-1847297911542220/8517149429");
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
            }
        });*/
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_chat_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("ShowAllMessage");
        countMessagesRef = FirebaseDatabase.getInstance().getReference().child("Users");
        notseenMessagesRef = FirebaseDatabase.getInstance().getReference().child("Users");
        messageReciverId = getIntent().getExtras().get("visit_user_id").toString();
        messageReciverName = getIntent().getExtras().get("username").toString();
        current_user_country_Liv = getIntent().getExtras().get("country_lives").toString();
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserChatImageRef = FirebaseStorage.getInstance().getReference().child("Chat Images");

        chattoolbar = (Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(chattoolbar);
        getSupportActionBar().setTitle("");

        if (la.equals("العربية")){
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = -25;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = -25;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 0;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = -8;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 0;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = -5;

            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = -25;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = -35;
            }
        } else {
            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 10;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = 10;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 10;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = 10;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 4;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = 1;
            }
            else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
                username_marginbottom = 0;
                username_marginleft = 2;
                username_marginright = 2;
                username_margintop = 10;
                userlastseen_marginbottom = 0;
                userlastseen_marginleft = 2;
                userlastseen_marginright = 2;
                userlastseen_margintop = 10;
            }
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar_normal, null);
        actionBar.setCustomView(action_bar_view);

        //waitingBar = new SpotsDialog.Builder().setContext(ChatActivity.this).build();
        //la = Locale.getDefault().getDisplayLanguage();
        mainRoot = (RelativeLayout) findViewById(R.id.main_chat_root);

        reciverName = (TextView) findViewById(R.id.custom_chat_profile_name);
        userLastSeen = (TextView) findViewById(R.id.custom_chat_user_last_seen);

        reciverProfileImage = (CircleImageView) findViewById(R.id.custom_chat_profile_image);

        sendMessageButton = (ImageButton) findViewById(R.id.send_message_button_chat);
        sendImageFileButton = (ImageButton) findViewById(R.id.send_image_file_button_chat);
        userMessageInput = (EditText) findViewById(R.id.input_message_chat);
        //loadingBar = new ProgressDialog(this);

        messagesAdapter = new MessagesAdapter(messagesList, ChatActivity.this, current_user_country_Liv, mainRoot);
        userMessageList = (RecyclerView) findViewById(R.id.messages_list_users);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
       // userMessageList.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        //userMessageList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        userMessageList.setHasFixedSize(true);
        userMessageList.setLayoutManager(linearLayoutManager);
        messagesAdapter.notifyItemRangeInserted(messagesAdapter.getItemCount(), messagesList.size() - 1);
        userMessageList.setAdapter(messagesAdapter);
        //getCurrentUserCountryLives();
        //DisplayAllReciverInfo();
        //updateUserState("Online");
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        //FetchMessages();
        /*UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersRef.child(messageSenderId).child("NotSeenMessageCountUsers").child(messageReciverId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        /*notseenMessagesRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notseenMessagesRef.child(messageSenderId).child("NotSeenMessageCountUsers").child(messageReciverId).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notseenMessagesRef.addValueEventListener(notseenMessagesRefValue);*/
        /*if(userMessageInput.isFocused()) {
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            Toast.makeText(ChatActivity.this, "focus", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ChatActivity.this, "Not focus", Toast.LENGTH_LONG).show();
        }*/
        sendImageFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer = "true";
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                messagesList.clear();
                messagesAdapter.notifyDataSetChanged();
                startActivityForResult(galleryIntent,Garrely_pick);

            }
        });
    }

    private void getCurrentUserCountryLives(){
        useresRef.child(messageSenderId).addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Garrely_pick && resultCode == RESULT_OK && data != null){
            Uri ImageUri = data.getData();

            if (resultCode == RESULT_OK){
                /*loadingBar.setTitle(getResources().getString(R.string.chat_loadingbar_title));
                loadingBar.setMessage(getResources().getString(R.string.chat_loadingbar_message));
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();*/
                /*waitingBar.setMessage(getResources().getString(R.string.chat_loadingbar_title));
                waitingBar.setCanceledOnTouchOutside(false);
                waitingBar.show();*/
                mydialog = new Dialog(ChatActivity.this);
                mydialog.setTitle(getResources().getString(R.string.chat_loadingbar_title));
                mydialog.setCancelable(false);
                mydialog.setContentView(R.layout.myprogressbr_layout);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mydialog.show();
                Uri Uriresult = ImageUri;

                Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                //calForDate_order.add(Calendar.HOUR, 2);
                SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                String savecurrentdates = currentDate_order.format(calForDate_order.getTime());

                Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                //calForTimesecond.add(Calendar.HOUR, 2);
                SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
                String savecurrenttimeseconds = currentTimesecond.format(calForTimesecond.getTime());

                String unic_imagename = messageSenderId + savecurrentdates + savecurrenttimeseconds;
                final StorageReference filePath = UserChatImageRef.child(unic_imagename + ".jpg");
                filePath.putFile(Uriresult).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){
                            //Toast.makeText(ChatActivity.this,getResources().getString(R.string.chat_toast_success_image_store),Toast.LENGTH_SHORT).show();
                            final Uri downUri = task.getResult();
                            /*settingsuserRef.child("profileimage").setValue(downUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    transfer = "true";
                                    Intent selfIntent = new Intent(ChatActivity.this,SettingsActivity.class);
                                    startActivity(selfIntent);
                                    if(task.isSuccessful()){
                                        //Toast.makeText(ChatActivity.this,getResources().getString(R.string.chat_toast_success_image_store_database),Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    } else {
                                        String message1 = task.getException().getMessage();
                                        String message2 = getResources().getString(R.string.chat_toast_not_success_profileimage_store);
                                        Toast.makeText(ChatActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });*/
                            FriendsRefValue = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()){
                                        if (dataSnapshot.hasChild(messageReciverId)){
                                            //updateUserState("Online");
                                            //final String messageText = userMessageInput.getText().toString();
                                            String message_sender_ref = "Messages/" + messageSenderId + "/" + messageReciverId;
                                            String message_reciver_ref = "Messages/" + messageReciverId + "/" + messageSenderId;
                                            DatabaseReference user_message_key = RootRef.child("Messages").child(message_sender_ref).child(message_reciver_ref).push();
                                            final String message_push_id = user_message_key.getKey();

                                            Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                            //calForDate.add(Calendar.HOUR, 2);
                                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                                            currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                            savecurrentdate = currentDate.format(calForDate.getTime());

                                            Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                            //calForTime.add(Calendar.HOUR, 2);
                                            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                            currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                                            savecurrenttime = currentTime.format(calForTime.getTime());


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

                                            Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                                            SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                                            currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                                            String order_date = currentorderDate.format(calFororderDate.getTime());

                                            final String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
                                            Map messageTextBody = new HashMap();
                                            messageTextBody.put("message", downUri.toString());
                                            messageTextBody.put("time", savecurrenttime);
                                            messageTextBody.put("date", savecurrentdate);
                                            messageTextBody.put("date_time", date_time);
                                            messageTextBody.put("order_date", order_date);
                                            messageTextBody.put("type", "image");
                                            messageTextBody.put("from", messageSenderId);
                                            Map messageBodyDetails = new HashMap();
                                            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);
                                            messageBodyDetails.put(message_reciver_ref + "/" + message_push_id, messageTextBody);
                                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if(task.isSuccessful()){
                                                        userMessageInput.setText("");
                                                        final Map mess = new HashMap();
                                                        mess.put("message", getResources().getString(R.string.chat_sended_image));
                                                        mess.put("date_time", date_time);
                                                        MessagesRef.child(messageReciverId).child(messageSenderId).updateChildren(mess).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                MessagesRef.child(messageSenderId).child(messageReciverId).updateChildren(mess).addOnCompleteListener(new OnCompleteListener() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task task) {
                                                                        countMessagesRef.child(messageReciverId).child("NotSeenMessageCountUsers").child(messageSenderId).child(message_push_id).setValue("notseen");

                                                                        /*transfer = "true";
                                                                        Intent selfIntent = new Intent(ChatActivity.this,ChatActivity.class);
                                                                        selfIntent.putExtra("visit_user_id", messageReciverId);
                                                                        selfIntent.putExtra("username", messageReciverName);
                                                                        startActivity(selfIntent);*/
                                                                        if(task.isSuccessful()){
                                                                            //Toast.makeText(ChatActivity.this,getResources().getString(R.string.chat_toast_success_image_store_database),Toast.LENGTH_SHORT).show();
                                                                            //loadingBar.dismiss();
                                                                            //waitingBar.dismiss();
                                                                            mydialog.dismiss();
                                                                            FetchMessages();
                                                                            //sendCount +=1;
                                                                        } else {
                                                                            String message1 = task.getException().getMessage();
                                                                            String message2 = getResources().getString(R.string.chat_toast_not_success_profileimage_store);
                                                                            //Toast.makeText(ChatActivity.this,message2 + message1,Toast.LENGTH_SHORT).show();
                                                                            //loadingBar.dismiss();
                                                                            //waitingBar.dismiss();
                                                                            mydialog.dismiss();
                                                                            if (la.equals("العربية")){
                                                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                                                            } else {
                                                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, message2 + message1,
                                                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                                                            }
                                                                        }
                                                                        updateUserState("Online");

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            //Toast.makeText(ChatActivity.this, "send : " + Integer.toString(sendCount), Toast.LENGTH_LONG).show();


                                        } else {
                                            //Toast.makeText(ChatActivity.this, getResources().getString(R.string.chat_message_notfriend),Toast.LENGTH_LONG).show();
                                            if (la.equals("العربية")){
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                            } else {
                                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                            }
                                        }
                                    } else {
                                        //Toast.makeText(ChatActivity.this, getResources().getString(R.string.chat_message_notfriend),Toast.LENGTH_LONG).show();
                                        if (la.equals("العربية")){
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                        } else {
                                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            FriendsRef.child(messageSenderId).child("Friends").addValueEventListener(FriendsRefValue);
                        }
                    }
                });
            } else {
                //Toast.makeText(ChatActivity.this,getResources().getString(R.string.chat_toast_not_success_image_store),Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
                //waitingBar.dismiss();
                mydialog.dismiss();
                if (la.equals("العربية")){
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_toast_not_success_image_store),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                } else {
                    SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_toast_not_success_image_store),
                            Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                }
            }
        }

    }

    private void FetchMessages() {
        //messagesAdapter = new MessagesAdapter(messagesList, ChatActivity.this, current_user_country_Lives);
        RootRef.child("Messages").child(messageSenderId).child(messageReciverId).orderByChild("order_date").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Messages mes = dataSnapshot.getValue(Messages.class);
                    /*if (messagesList.size() > 0) {
                        messagesList.clear();
                        messagesList.add(mes);
                    } else {
                        messagesList.add(mes);
                    }*/
                    messagesList.add(mes);
                    //messagesAdapter.setHasStableIds(true);

                    //messagesAdapter.notifyDataSetChanged();
                    //kk++;
                    //messagesAdapter.notifyItemChanged(messagesList.size());
                    linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    linearLayoutManager.setStackFromEnd(true);
                    userMessageList.setHasFixedSize(true);
                    userMessageList.setLayoutManager(linearLayoutManager);
                    //Toast.makeText(ChatActivity.this,Integer.toString(kk),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*if (sendCount == 7){
            MobileAds.initialize(ChatActivity.this, "ca-app-pub-1847297911542220/3702972297");
            AdView madview = (AdView) findViewById(R.id.chat_adviewsend);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
            madview.loadAd(adRequest);
        }*/
    }

    private void SendMessage() {
        FriendsRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild(messageReciverId)){
                        updateUserState("Online");
                        final String messageText = userMessageInput.getText().toString();
                        if(TextUtils.isEmpty(messageText)){
                            //Toast.makeText(ChatActivity.this, getResources().getString(R.string.chat_message_empty), Toast.LENGTH_LONG).show();
                            if (la.equals("العربية")){
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_empty),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                            } else {
                                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_empty),
                                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                            }
                        } else {
                            String message_sender_ref = "Messages/" + messageSenderId + "/" + messageReciverId;
                            String message_reciver_ref = "Messages/" + messageReciverId + "/" + messageSenderId;
                            DatabaseReference user_message_key = RootRef.child("Messages").child(message_sender_ref).child(message_reciver_ref).push();
                            final String message_push_id = user_message_key.getKey();

                            Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForDate.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
                            currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                            savecurrentdate = currentDate.format(calForDate.getTime());

                            Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForTime.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                            currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                            savecurrenttime = currentTime.format(calForTime.getTime());


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

                            Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
                            currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String order_date = currentorderDate.format(calFororderDate.getTime());

                            final String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
                            Map messageTextBody = new HashMap();
                            messageTextBody.put("message", messageText);
                            messageTextBody.put("time", savecurrenttime);
                            messageTextBody.put("date", savecurrentdate);
                            messageTextBody.put("date_time", date_time);
                            messageTextBody.put("order_date", order_date);
                            messageTextBody.put("type", "text");
                            messageTextBody.put("from", messageSenderId);
                            Map messageBodyDetails = new HashMap();
                            messageBodyDetails.put(message_sender_ref + "/" + message_push_id, messageTextBody);
                            messageBodyDetails.put(message_reciver_ref + "/" + message_push_id, messageTextBody);
                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        userMessageInput.setText("");
                                        final Map mess = new HashMap();
                                        mess.put("message", messageText);
                                        mess.put("date_time", date_time);
                                        MessagesRef.child(messageReciverId).child(messageSenderId).updateChildren(mess).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                MessagesRef.child(messageSenderId).child(messageReciverId).updateChildren(mess).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        countMessagesRef.child(messageReciverId).child("NotSeenMessageCountUsers").child(messageSenderId).child(message_push_id).setValue("notseen");
                                                        messagesList.clear();
                                                        messagesAdapter.notifyDataSetChanged();
                                                        FetchMessages();
                                                        sendCount += 1;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {
                        //Toast.makeText(ChatActivity.this, getResources().getString(R.string.chat_message_notfriend),Toast.LENGTH_LONG).show();
                        if (la.equals("العربية")){
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                        } else {
                            SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                    Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                        }
                    }
                } else {
                    //Toast.makeText(ChatActivity.this, getResources().getString(R.string.chat_message_notfriend),Toast.LENGTH_LONG).show();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.chat_message_notfriend),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FriendsRef.child(messageSenderId).child("Friends").addValueEventListener(FriendsRefValue);

        if (sendCount == 5){
            //Toast.makeText(ChatActivity.this, "out : " + Integer.toString(sendCount), Toast.LENGTH_LONG).show();
            //if (interstitialAd.isLoaded()){
                //Toast.makeText(ChatActivity.this, "in : " + Integer.toString(sendCount), Toast.LENGTH_LONG).show();
                //interstitialAd.show();
            //}
            MobileAds.initialize(this, "ca-app-pub-1847297911542220/2987165732");
            AdView madview = (AdView) findViewById(R.id.chat_adviewsend);
            AdRequest adRequest = new AdRequest.Builder().build();
            madview.loadAd(adRequest);
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
        UsersRef.child(messageSenderId).updateChildren(hashMap);
    }
    private void DisplayAllReciverInfo() {
        reciverName.setText(messageReciverName);
        RootRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String profileName = dataSnapshot.child("fullname").getValue().toString();
                    final String profileImage = dataSnapshot.child("profileimage").getValue().toString();

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) reciverName.getLayoutParams();
                    params.setMargins(username_marginleft, username_margintop, username_marginright, username_marginbottom);
                    reciverName.setLayoutParams(params);
                    reciverName.setText(profileName);
                    Picasso.with(ChatActivity.this).load(profileImage).placeholder(R.drawable.profile).into(reciverProfileImage);

                    if(dataSnapshot.hasChild("state_type")){
                        String type = dataSnapshot.child("state_type").getValue().toString();
                        //String lastTime = dataSnapshot.child("state_time").getValue().toString();
                        //String lastDate = dataSnapshot.child("state_date").getValue().toString();
                        String lastDate_Time = dataSnapshot.child("state_date_time").getValue().toString();
                        userLastSeen.setVisibility(View.VISIBLE);
                        userLastSeen.setText("");
                        if(type.equals("Offline")) {
                            String txs = getResources().getString(R.string.chat_last_seen_state);
                            //String tx = txs + " " + lastTime + " " + lastDate;

                            GetUserDateTime getUserDateTime = new GetUserDateTime(ChatActivity.this);
                            String date_value = getUserDateTime.getDateToShow(lastDate_Time, current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                            String tx = txs + " " + date_value;

                            RelativeLayout.LayoutParams paramsss = (RelativeLayout.LayoutParams) userLastSeen.getLayoutParams();
                            paramsss.setMargins(userlastseen_marginleft, userlastseen_margintop, userlastseen_marginright, userlastseen_marginbottom);
                            userLastSeen.setLayoutParams(paramsss);
                            userLastSeen.setText(tx);
                        }
                        if(type.equals("Online")){
                            String ol = getResources().getString(R.string.chat_online_state);

                            RelativeLayout.LayoutParams paramsss = (RelativeLayout.LayoutParams) userLastSeen.getLayoutParams();
                            paramsss.setMargins(userlastseen_marginleft, userlastseen_margintop, userlastseen_marginright, userlastseen_marginbottom);
                            userLastSeen.setLayoutParams(paramsss);
                            userLastSeen.setText(ol);
                        }

                    } else {
                        userLastSeen.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        RootRef.child("Users").child(messageReciverId).addValueEventListener(RootRefValue);
    }

    private void notseenMessages(){
        notseenMessagesRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(messageSenderId).child("NotSeenMessageCountUsers").child(messageReciverId).hasChildren()){
                    notseenMessagesRef.child(messageSenderId).child("NotSeenMessageCountUsers").child(messageReciverId).removeValue();
                    //Toast.makeText(ChatActivity.this,"hh", Toast.LENGTH_LONG).show();
                    if (isSt.equals("notstart")){
                        //Toast.makeText(ChatActivity.this,"hi", Toast.LENGTH_LONG).show();
                        messagesList.clear();
                        //messagesAdapter.notifyDataSetChanged();
                        FetchMessages();
                        reciveCount += 1;
                        //Toast.makeText(ChatActivity.this, "send : " + Integer.toString(reciveCount), Toast.LENGTH_LONG).show();
                        if (reciveCount == 7){
                            /*if (interstitialAd.isLoaded()){
                                interstitialAd.show();
                            }*/
                            MobileAds.initialize(ChatActivity.this, "ca-app-pub-1847297911542220/2987165732");
                            AdView madview = (AdView) findViewById(R.id.chat_adviewrecived);
                            AdRequest adRequest = new AdRequest.Builder().build();
                            madview.loadAd(adRequest);
                        }
                    } else {
                        isSt = "notstart";
                    }

                } else {
                    isSt = "notstart";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notseenMessagesRef.addValueEventListener(notseenMessagesRefValue);
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
                DisplayAllReciverInfo();
                FetchMessages();
                notseenMessages();
            } else {
                Toast.makeText(ChatActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
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
    public boolean onSupportNavigateUp() {
        transfer = "true";
        return super.onSupportNavigateUp();

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
    protected void onStop() {

        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
            messagesList.clear();
            messagesAdapter.notifyDataSetChanged();
        }
        if (notseenMessagesRefValue != null && notseenMessagesRef!=null) {
            notseenMessagesRef.removeEventListener(notseenMessagesRefValue);
        }
        /*notseenMessagesRefValue = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notseenMessagesRef.addValueEventListener(notseenMessagesRefValue);*/
        if (RootRefValue != null && RootRef!=null) {
            RootRef.removeEventListener(RootRefValue);
        }
        if (FriendsRefValue != null && FriendsRef!=null) {
            FriendsRef.removeEventListener(FriendsRefValue);
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        /*if (valueEventListener != null && notseenMessagesRef!=null) {
            notseenMessagesRef.removeEventListener(valueEventListener);
        }*/
        super.onPause();
        /*if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/

    }

    @Override
    protected void onDestroy() {
        /*if (valueEventListener != null && notseenMessagesRef!=null) {
            notseenMessagesRef.removeEventListener(valueEventListener);
        }*/
        super.onDestroy();
       /* if(!transfer.equals("true")){
            updateUserState("Offline");
        }*/

    }
}
