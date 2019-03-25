package com.ahmedtaha.barakasky;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.directionalsnackbar.SnackbarUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
//import dmax.dialog.SpotsDialog;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private List<Messages> userMessageList;
    private FirebaseAuth mAuthh;
    public DatabaseReference usersRefs, useresRef;
    public String old_date = "";
    public String current_user_country_Lives;
    private Context contexts;
    //private AlertDialog waitingBar;
    private String la = Locale.getDefault().getDisplayLanguage();
    private RelativeLayout mainRoot;
    private Typeface tf = null;


    public MessagesAdapter(List<Messages> userMessageList, Context context, String current_user_country_Liv, RelativeLayout mainRoots){
        this.userMessageList = userMessageList;
        this.contexts = context;
        this.current_user_country_Lives = current_user_country_Liv;
        this.mainRoot = mainRoots;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_of_users_normal, parent,false);
        mAuthh = FirebaseAuth.getInstance();
        return new MessageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String messagesSenderId = mAuthh.getCurrentUser().getUid();
        final Messages messages = userMessageList.get(position);

        String fromuserId = messages.getFrom();
        String fromMessageType = messages.getType();
        usersRefs = FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserId);
        useresRef = FirebaseDatabase.getInstance().getReference().child("Users");


        usersRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String im = dataSnapshot.child("profileimage").getValue().toString();
                    //current_user_country_Lives = dataSnapshot.child("country_lives").getValue().toString();
                    Picasso.with(holder.reciverProfileImage.getContext()).load(im).placeholder(R.drawable.profile).into(holder.reciverProfileImage);
                } else {
                    Picasso.with(holder.reciverProfileImage.getContext()).load(R.drawable.profile).into(holder.reciverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*useresRef.child(messagesSenderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    current_user_country_Lives = dataSnapshot.child("country_lives").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        //String new_date = messages.getDate();
        //holder.displyMessgeDate.setText(new_date);
        /*if (old_date.equals(new_date)){
            holder.displyMessgeDate.setVisibility(View.GONE);
        } else {
            holder.displyMessgeDate.setText(new_date);
            old_date = new_date;
            holder.displyMessgeDate.setVisibility(View.VISIBLE);
        }*/
        //current_user_country_Lives = "0";
        //Toast.makeText(contexts, current_user_country_Lives, Toast.LENGTH_LONG).show();
        if(fromMessageType.equals("text")){
            holder.ReciverMessageText.setVisibility(View.GONE);
            holder.reciverProfileImage.setVisibility(View.GONE);
            holder.ReciverMessageTime.setVisibility(View.GONE);

            holder.sneded_Image.setVisibility(View.GONE);
            holder.reciveded_Image.setVisibility(View.GONE);
            //holder.ReciverimageDownload.setVisibility(View.GONE);
            //holder.SenderimageDownload.setVisibility(View.GONE);
            if(fromuserId.equals(messagesSenderId)){
                holder.SenderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.SenderMessageText.setTextColor(Color.WHITE);
                holder.SenderMessageText.setGravity(Gravity.LEFT);
                holder.SenderMessageText.setText(messages.getMessage());
                holder.SenderMessageTime.setBackgroundResource(R.color.messagedatebg_sender);
                holder.SenderMessageTime.setTextColor(Color.WHITE);
                holder.SenderMessageTime.setGravity(Gravity.LEFT);

                GetUserDateTime getUserDateTime = new GetUserDateTime(contexts);
                String date_value = getUserDateTime.getDateToShow(messages.getDate_time(), current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                //holder.SenderMessageTime.setText(messages.getDate_time() + " " + messages.getTime());
                holder.SenderMessageTime.setText(date_value);
            } else {
                holder.SenderMessageText.setVisibility(View.GONE);
                holder.SenderMessageTime.setVisibility(View.GONE);
                holder.ReciverMessageText.setVisibility(View.VISIBLE);
                holder.ReciverMessageTime.setVisibility(View.VISIBLE);
                holder.reciverProfileImage.setVisibility(View.VISIBLE);
                holder.ReciverMessageText.setBackgroundResource(R.drawable.reciver_message_text_background);
                holder.ReciverMessageText.setTextColor(Color.WHITE);
                holder.ReciverMessageText.setGravity(Gravity.LEFT);
                holder.ReciverMessageText.setText(messages.getMessage());
                holder.ReciverMessageTime.setBackgroundResource(R.color.messagedatebg_reciver);
                holder.ReciverMessageTime.setTextColor(Color.WHITE);
                holder.ReciverMessageTime.setGravity(Gravity.LEFT);
                GetUserDateTime getUserDateTime = new GetUserDateTime(contexts);
                String date_value = getUserDateTime.getDateToShow(messages.getDate_time(), current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                //holder.ReciverMessageTime.setText(messages.getDate_time() + " " + messages.getTime());
                holder.ReciverMessageTime.setText(date_value);
            }
        } else if(fromMessageType.equals("image")){
            holder.ReciverMessageText.setVisibility(View.GONE);
            holder.reciverProfileImage.setVisibility(View.GONE);
            holder.ReciverMessageTime.setVisibility(View.GONE);
            holder.reciveded_Image.setVisibility(View.GONE);

            holder.SenderMessageText.setVisibility(View.GONE);
            holder.SenderMessageTime.setVisibility(View.GONE);
            holder.sneded_Image.setVisibility(View.GONE);

            //holder.ReciverimageDownload.setVisibility(View.GONE);
            //holder.SenderimageDownload.setVisibility(View.GONE);
            if(fromuserId.equals(messagesSenderId)){
                Picasso.with(holder.sneded_Image.getContext()).load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.sneded_Image);
                holder.sneded_Image.setVisibility(View.VISIBLE);
                holder.SenderMessageTime.setBackgroundResource(R.color.messagedatebg_sender);
                holder.SenderMessageTime.setTextColor(Color.WHITE);
                holder.SenderMessageTime.setGravity(Gravity.LEFT);
                GetUserDateTime getUserDateTime = new GetUserDateTime(contexts);
                String date_value = getUserDateTime.getDateToShow(messages.getDate_time(), current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                //holder.SenderMessageTime.setText(messages.getDate_time() + " " + messages.getTime());
                holder.SenderMessageTime.setText(date_value);
                holder.SenderMessageTime.setVisibility(View.VISIBLE);
                /*holder.SenderimageDownload.setVisibility(View.VISIBLE);
                holder.SenderimageDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            waitingBar = new SpotsDialog.Builder().setContext(contexts).build();
                            waitingBar.setMessage(contexts.getResources().getString(R.string.download_image_title));
                            waitingBar.setCanceledOnTouchOutside(false);
                            waitingBar.show();
                            Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForDate_order.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentDate_order = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.ENGLISH);
                            currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String savedate = currentDate_order.format(calForDate_order.getTime());
                            StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(messages.getMessage());
                            File localFile = null;
                            localFile = File.createTempFile("barakaskyimage" + savedate, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                            UserProfileImageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    //Toast.makeText(ClickPostActivity.this, "downloaded", Toast.LENGTH_LONG).show();
                                    waitingBar.dismiss();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_success),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_success),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    //Toast.makeText(ClickPostActivity.this, "not", Toast.LENGTH_LONG).show();
                                    waitingBar.dismiss();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_notsuccess),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_notsuccess),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            } else {
                holder.reciverProfileImage.setVisibility(View.VISIBLE);
                Picasso.with(holder.reciveded_Image.getContext()).load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.reciveded_Image);
                holder.reciveded_Image.setVisibility(View.VISIBLE);
                holder.ReciverMessageTime.setBackgroundResource(R.color.messagedatebg_reciver);
                holder.ReciverMessageTime.setTextColor(Color.WHITE);
                holder.ReciverMessageTime.setGravity(Gravity.LEFT);
                GetUserDateTime getUserDateTime = new GetUserDateTime(contexts);
                String date_value = getUserDateTime.getDateToShow(messages.getDate_time(), current_user_country_Lives, "dd-MM-yyyy HH:mm:ss", "hh:mm a  MMM dd-yyyy");
                //holder.ReciverMessageTime.setText(messages.getDate_time() + " " + messages.getTime());
                holder.ReciverMessageTime.setText(date_value);
                holder.ReciverMessageTime.setVisibility(View.VISIBLE);
                /*holder.ReciverimageDownload.setVisibility(View.VISIBLE);

                holder.ReciverimageDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            waitingBar = new SpotsDialog.Builder().setContext(contexts).build();
                            waitingBar.setMessage(contexts.getResources().getString(R.string.download_image_title));
                            waitingBar.setCanceledOnTouchOutside(false);
                            waitingBar.show();
                            Calendar calForDate_order = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                            //calForDate_order.add(Calendar.HOUR, 2);
                            SimpleDateFormat currentDate_order = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.ENGLISH);
                            currentDate_order.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String savedate = currentDate_order.format(calForDate_order.getTime());
                            StorageReference UserProfileImageRef = FirebaseStorage.getInstance().getReferenceFromUrl(messages.getMessage());
                            File localFile = null;
                            localFile = File.createTempFile("barakaskyimage" + savedate, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                            UserProfileImageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    //Toast.makeText(ClickPostActivity.this, "downloaded", Toast.LENGTH_LONG).show();
                                    waitingBar.dismiss();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_success),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_success),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    //Toast.makeText(ClickPostActivity.this, "not", Toast.LENGTH_LONG).show();
                                    waitingBar.dismiss();
                                    if (la.equals("العربية")){
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_notsuccess),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.RTL_DIRECTION);
                                    } else {
                                        SnackbarUtil.setSnackBarWithNoActionButton(mainRoot, contexts.getResources().getString(R.string.download_image_notsuccess),
                                                Color.WHITE, contexts.getResources().getColor(R.color.snacbar_background_color),tf,contexts.getResources().getDimension(R.dimen.snackbar_size), SnackbarUtil.LTR_DIRECTION);
                                    }
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });*/
            }
        }
        holder.displyMessgeDate.setVisibility(View.GONE);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView SenderMessageText, ReciverMessageText, SenderMessageTime, ReciverMessageTime, displyMessgeDate;
        //public TextView ReciverimageDownload, SenderimageDownload;
        public CircleImageView reciverProfileImage;
        public ImageView sneded_Image, reciveded_Image;
        private DatabaseReference useresRef;
        private String old_date = "";
        String cc;
        public MessageViewHolder(View itemView) {
            super(itemView);
            SenderMessageText = (TextView) itemView.findViewById(R.id.sender_message_text);
            ReciverMessageText = (TextView) itemView.findViewById(R.id.reciver_message_text);
            SenderMessageTime = (TextView) itemView.findViewById(R.id.sender_message_date);
            ReciverMessageTime = (TextView) itemView.findViewById(R.id.reciver_message_date);
            displyMessgeDate = (TextView) itemView.findViewById(R.id.message_display_date);
            //ReciverimageDownload = (TextView) itemView.findViewById(R.id.reciver_message_image_download);
            //SenderimageDownload = (TextView) itemView.findViewById(R.id.sender_message_image_download);
            reciverProfileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            sneded_Image = (ImageView) itemView.findViewById(R.id.sender_message_image);
            reciveded_Image = (ImageView) itemView.findViewById(R.id.reciver_message_image);
            useresRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        public String getOld_date() {
            return old_date;
        }

        public void setOld_date(String old_date) {
            this.old_date = old_date;
        }
        public String getCurrentLives(String cuid){

            useresRef.child(cuid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        cc = dataSnapshot.child("country_lives").getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return cc;
        }
    }
}
