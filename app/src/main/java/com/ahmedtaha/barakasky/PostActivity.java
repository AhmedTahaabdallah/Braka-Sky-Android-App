package com.ahmedtaha.barakasky;

import android.app.ActionBar;
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
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

//import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PostActivity extends AppCompatActivity {
    private Toolbar mToolBar;
    private ImageButton selectpostimage;
    private Button sharepostbutton;
    private EditText postDescription;
    final static int Garrely_pick = 1;
    private Uri imageUri;
    private String Description;
    private String savecurrentdate_order,savecurrentdate,savecurrenttime,savecurrenttimesecond, saveNextDay,postrendomname,mydownloadUri,current_user_id, order_date;
    //private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private StorageReference postImageRef;
    private DatabaseReference userRef,postRef;
    private ValueEventListener userRefValue,postRefValue;
    private String transfer = "false";
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;
    private Dialog mydialog;
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
        setContentView(R.layout.activity_post_normal);
        /*if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setContentView(R.layout.activity_post_normal);
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

        }
        else {

        }*/
        mToolBar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolBar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.post_toolbr_title));
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

        selectpostimage = (ImageButton) findViewById(R.id.select_post_image);
        sharepostbutton = (Button) findViewById(R.id.share_post_button);
        postDescription = (EditText) findViewById(R.id.post_description);
        //loadingBar = new ProgressDialog(this);
        //waitingBar = new SpotsDialog.Builder().setContext(PostActivity.this).build();
        mainRoot = (RelativeLayout) findViewById(R.id.main_addpost_root);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        postImageRef = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        selectpostimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
                }
        });
        sharepostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VaildatePostInfo();
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
        userRef.child(current_user_id).updateChildren(hashMap);
    }

    private void VaildatePostInfo() {
        Description = postDescription.getText().toString();
        if(imageUri == null){
            //Toast.makeText(PostActivity.this, getResources().getString(R.string.post_image_not_selected),Toast.LENGTH_LONG).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_image_not_selected),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_image_not_selected),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else if(TextUtils.isEmpty(Description)){
            //Toast.makeText(PostActivity.this, getResources().getString(R.string.post_description_is_empty),Toast.LENGTH_LONG).show();
            if (la.equals("العربية")){
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_description_is_empty),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
            } else {
                SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_description_is_empty),
                        Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
            }
        } else {
            /*loadingBar.setTitle(getResources().getString(R.string.post_new_post_loadingbar_title));
            loadingBar.setMessage(getResources().getString(R.string.post_new_post_loadingbar_message));
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);*/

            /*waitingBar.setMessage(getResources().getString(R.string.post_new_post_loadingbar_title));
            waitingBar.setCanceledOnTouchOutside(false);
            waitingBar.show();*/
            mydialog = new Dialog(PostActivity.this);
            mydialog.setTitle(getResources().getString(R.string.post_new_post_loadingbar_title));
            mydialog.setCancelable(false);
            mydialog.setContentView(R.layout.myprogressbr_layout);
            mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mydialog.show();
            StoreImageToFirebaseStorage();
        }
    }

    private void StoreImageToFirebaseStorage() {

        Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate_order.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate_order = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate_order = currentDate_order.format(calForDate_order.getTime());
        calForDate_order.add(Calendar.DATE, 1);
        saveNextDay = currentDate_order.format(calForDate_order.getTime());

        Calendar calForTimesecond = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTimesecond.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTimesecond = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        currentTimesecond.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrenttimesecond = currentTimesecond.format(calForTimesecond.getTime());

        Calendar calForTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForTime.add(Calendar.HOUR, 2);
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrenttime = currentTime.format(calForTime.getTime());

        Calendar calForDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        //calForDate.add(Calendar.HOUR, 2);
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        savecurrentdate = currentDate.format(calForDate.getTime());

        Calendar calFororderDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat currentorderDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
        currentorderDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        order_date = currentorderDate.format(calFororderDate.getTime());

        postrendomname = savecurrentdate + savecurrenttimesecond;

        final StorageReference filePath = postImageRef.child("Post Images").child(imageUri.getLastPathSegment() + postrendomname + ".jpg");
        filePath.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    mydownloadUri = task.getResult().toString();
                    //Toast.makeText(PostActivity.this,"Image Uploded", Toast.LENGTH_LONG).show();
                    SavingPostInfoToFirebaseDatabase();
                } else {
                    //Toast.makeText(PostActivity.this,"Image Not Uploded", Toast.LENGTH_LONG).show();
                    //Toast.makeText(PostActivity.this, getResources().getString(R.string.post_not_saved),Toast.LENGTH_LONG).show();

                    //loadingBar.dismiss();
                    //waitingBar.dismiss();
                    mydialog.dismiss();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_not_saved),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_not_saved),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });
    }

    private void SavingPostInfoToFirebaseDatabase() {
        /*userRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //String userFullName = dataSnapshot.child("fullname").getValue().toString();
                    //String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String date_time = savecurrentdate_order + " " + savecurrenttime;
                    //SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    //sfd.format(new Date(ServerValue.TIMESTAMP));
                    HashMap postMap = new HashMap();
                    postMap.put("uid",current_user_id);
                    postMap.put("date",savecurrentdate);
                    postMap.put("time",savecurrenttime);
                    postMap.put("dateandtime",date_time);
                    //postMap.put("dateandtimeserver", ServerValue.TIMESTAMP);
                    postMap.put("description",Description);
                    postMap.put("postimage",mydownloadUri);
                    //postMap.put("profileimage",userProfileImage);
                    //postMap.put("fullname",userFullName);

                    postRef.child(current_user_id + postrendomname).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PostActivity.this, getResources().getString(R.string.post_saved),Toast.LENGTH_LONG).show();
                                sendUserToMainActivaty();
                                loadingBar.dismiss();
                            } else {
                                Toast.makeText(PostActivity.this, getResources().getString(R.string.post_not_saved),Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        String date_time = savecurrentdate_order + " " + savecurrenttimesecond;
        String next_date_time = saveNextDay + " " + savecurrenttimesecond;
        HashMap postMap = new HashMap();
        postMap.put("uid",current_user_id);
        postMap.put("date",savecurrentdate);
        postMap.put("time",savecurrenttime);
        postMap.put("dateandtime",date_time);
        postMap.put("order_date",order_date);
        postMap.put("next_date_time",next_date_time);
        postMap.put("description",Description);
        postMap.put("postimage",mydownloadUri);

        postRef.child(current_user_id + postrendomname).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostActivity.this, getResources().getString(R.string.post_saved),Toast.LENGTH_LONG).show();
                    sendUserToMainActivaty();
                    //loadingBar.dismiss();
                    //waitingBar.dismiss();
                    mydialog.dismiss();
                } else {
                    //Toast.makeText(PostActivity.this, getResources().getString(R.string.post_not_saved),Toast.LENGTH_LONG).show();
                    //loadingBar.dismiss();
                    //waitingBar.dismiss();
                    mydialog.dismiss();
                    if (la.equals("العربية")){
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_not_saved),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                    } else {
                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, getResources().getString(R.string.post_not_saved),
                                Color.WHITE, getResources().getColor(R.color.snacbar_background_color),tf,getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                    }
                }
            }
        });
    }

    private void openGallery() {
        transfer = "true";
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Garrely_pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Garrely_pick && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            selectpostimage.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            sendUserToMainActivaty();
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendUserToMainActivaty() {
        transfer = "true";
        Intent intent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(intent);
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("post_desc", postDescription.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        postDescription.setText(savedInstanceState.getString("post_desc"));
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transfer = "true";
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            int timeisoff = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
            if (timeisoff == 1) {
                transfer = "false";
                updateUserState("Online");
            } else {
                Toast.makeText(PostActivity.this, "Please Select  automatic date & time from Settings", Toast.LENGTH_LONG).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                finish();
                System.exit(0);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUserState("Online");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //updateUserState("Online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!transfer.equals("true")){
            updateUserState("Offline");
        }
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if(!transfer.equals("true")){
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
    public boolean onSupportNavigateUp() {
        transfer = "true";
        return super.onSupportNavigateUp();
    }
}
