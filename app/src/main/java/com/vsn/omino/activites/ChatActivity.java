package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.CircledImageView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.Adapters.MessagesAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    LinearLayout selectIMG,selectMP4,selectMP3;

    MessagesAdapter messagesAdapter;
    List<MessageModel> messagelList;

    String FUserID;
    LinearLayout lyt_back;
    com.mikhaellopez.circularimageview.CircularImageView image;
    TextView username,IsOnline;
    EditText text_content;
    ImageView attachment;
    FloatingActionButton btn_send;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    RecyclerView recyclerView;
    private View bottom_sheet;

    private static final int REQUEST_PERMISSIONS = 100;
    private static final String PERMISSIONS_REQUIRED[] = new String[]{
            android.Manifest.permission.READ_PHONE_STATE
    };
    SendChatFilesActivity sendChatFilesActivity;
    Uri imageUri,videoUri,audioUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(ChatActivity.this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
        setContentView(R.layout.activity_chat);
        View findViewById = findViewById(R.id.bottom_sheet);
        bottom_sheet = findViewById;
        mBehavior = BottomSheetBehavior.from(findViewById);
        View inflate = getLayoutInflater().inflate(R.layout.sheet_menu, null);
        mBottomSheetDialog = new BottomSheetDialog(ChatActivity.this);
        mBottomSheetDialog.setContentView(inflate);
        selectIMG = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectIMG);
        selectMP3 = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectMP3);
        selectMP4 = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectMP4);
        seenAllMessages();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        FUserID = getIntent().getStringExtra("frontuserid");
        messagelList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(ChatActivity.this,messagelList);
        lyt_back = (LinearLayout)findViewById(R.id.lyt_back);
        image = (com.mikhaellopez.circularimageview.CircularImageView)findViewById(R.id.image);
        username = (TextView)findViewById(R.id.username);
        IsOnline = (TextView)findViewById(R.id.onlinestatus);
        text_content = (EditText)findViewById(R.id.text_content);
        attachment = (ImageView)findViewById(R.id.attachment);
        btn_send = (FloatingActionButton)findViewById(R.id.btn_send);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        getAllMessages();
        getFrontUsrDetails();





        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text_content.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this, "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                else{
                    SendMessage();
                }
            }
        });
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        lyt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    private void SendMessage() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(currentTime);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatMessages");
        String MessageID = reference.push().getKey();

        HashMap<String , String> messageMap = new HashMap<>();
        messageMap.put("Sender",prefs.getString("userID",null));
        messageMap.put("Reciver",FUserID);
        messageMap.put("MsgType","text");
        messageMap.put("Content",text_content.getText().toString());
        messageMap.put("DateTime",currentDateandTime);
        messageMap.put("isSeen","false");
        messageMap.put("MsgID",MessageID);
        reference.child(MessageID).setValue(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ChatLists").child(prefs.getString("userID",null));
                            HashMap<String , String> Map = new HashMap<>();
                            Map.put("FrontUserID",FUserID);
                            Map.put("LastMessage",text_content.getText().toString());
                            reference2.child(FUserID).setValue(Map);

                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("ChatLists").child(FUserID);
                            HashMap<String , String> Mapp = new HashMap<>();
                            Mapp.put("FrontUserID",prefs.getString("userID",null));
                            Mapp.put("LastMessage",text_content.getText().toString());
                            reference3.child(prefs.getString("userID",null)).setValue(Mapp);
                        }
                    }
                });
    }

    private void getAllMessages(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatMessages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String sender = snapshot1.child("Sender").getValue().toString();
                    String receiver = snapshot1.child("Reciver").getValue().toString();
                    String type = snapshot1.child("MsgType").getValue().toString();
                    String content = snapshot1.child("Content").getValue().toString();
                    String time = snapshot1.child("DateTime").getValue().toString();
                    String seen = snapshot1.child("isSeen").getValue().toString();
                    String msgid = snapshot1.child("MsgID").getValue().toString();
                    if((sender.equals(prefs.getString("userID",null)) && receiver.equals(FUserID)) || (sender.equals(FUserID) && receiver.equals(prefs.getString("userID",null))) ){
                        messagelList.add(new MessageModel(sender,receiver,type,content,time,seen,msgid));
                    }
                }
                messagesAdapter = new MessagesAdapter(ChatActivity.this,messagelList);
                recyclerView.setAdapter(messagesAdapter);
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBottomSheetDialog() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        View inflate = getLayoutInflater().inflate(R.layout.sheet_menu, null);
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(inflate);
        selectIMG = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectIMG);
        selectMP3 = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectMP3);
        selectMP4 = (LinearLayout) mBottomSheetDialog.findViewById(R.id.selectMP4);
        if (Build.VERSION.SDK_INT >= 21) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        mBottomSheetDialog.show();
        this.mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                mBottomSheetDialog = null;
            }
        });

        selectMP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Video"), 3);
            }
        });
        selectIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Image"), 1);
            }
        });
        selectMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Music"), 2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Intent intent = new Intent(ChatActivity.this,SendChatFilesActivity.class);
            intent.putExtra("type","image");
            intent.putExtra("data",data.getData().toString());
            intent.putExtra("frontUser",FUserID);
            startActivity(intent);
        }
        if(requestCode==2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            audioUri = data.getData();
            Intent intent = new Intent(ChatActivity.this,SendChatFilesActivity.class);
            intent.putExtra("type","audio");
            intent.putExtra("data",data.getData().toString());
            intent.putExtra("frontUser",FUserID);
            startActivity(intent);
        }
        if(requestCode==3 && resultCode == RESULT_OK && data != null && data.getData() != null){
            videoUri = data.getData();
            Intent intent = new Intent(ChatActivity.this,SendChatFilesActivity.class);
            intent.putExtra("type","video");
            intent.putExtra("data",data.getData().toString());
            intent.putExtra("frontUser",FUserID);
            startActivity(intent);
        }

    }



    private void getFrontUsrDetails(){

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserAccountSettings").child(FUserID);
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String usern = snapshot.child("username").getValue().toString();
            String proPic = snapshot.child("profile_photo").getValue().toString();
            username.setText(usern);
            Picasso.get().load(proPic).into(image);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });


    }


    private void seenAllMessages() {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ChatMessages");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot msg : snapshot.getChildren()) {
                    if(msg.child("Reciver").getValue().toString().equals(prefs.getString("userID",null)) && msg.child("Sender").getValue().toString().equals(FUserID)){
                        if(msg.child("isSeen").equals("false")){
                            DatabaseReference databaseRefr = FirebaseDatabase.getInstance().getReference("ChatMessages").child(msg.getKey().toString());
                            databaseRefr.child("isSeen").setValue("true");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}