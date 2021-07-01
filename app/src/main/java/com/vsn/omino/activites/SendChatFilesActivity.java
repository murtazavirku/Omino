package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.vsn.omino.FragmentUpload;
import com.vsn.omino.R;
import com.vsn.omino.utiles.MusicUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SendChatFilesActivity extends AppCompatActivity {

    ImageView imageView,backpress;
    VideoView videoView;
    FloatingActionButton Musicplaybtn,btn_send;
    ProgressBar progressBar;
    LinearLayout MusicPlayerLayout;
    FirebaseStorage mStorage;
    StorageTask uploadTask;
    Uri dataUri;
    private MusicUtils utils;
    private MediaPlayer mp;
    private Handler mHandler = new Handler();

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_chat_files);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mp = mediaPlayer;
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        dataUri = Uri.parse(getIntent().getStringExtra("data"));
        imageView = (ImageView)findViewById(R.id.imagedata);
        backpress = (ImageView)findViewById(R.id.backpress);
        videoView = (VideoView)findViewById(R.id.videodata);
        Musicplaybtn = (FloatingActionButton)findViewById(R.id.musicdata);
        btn_send = (FloatingActionButton)findViewById(R.id.btn_send);
        progressBar = (ProgressBar)findViewById(R.id.song_progressbar);
        MusicPlayerLayout = (LinearLayout)findViewById(R.id.MusicPlayerLayout);
        mStorage = FirebaseStorage.getInstance();
        progressBar.setProgress(0);
        progressBar.setMax(10000);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                Musicplaybtn.setImageResource(R.drawable.play_arrow);
            }
        });
        utils = new MusicUtils();

        if(getIntent().getStringExtra("type").equals("image")){
            imageView.setVisibility(View.VISIBLE);
            if(dataUri != null){
                Uri uri = dataUri;
                imageView.setImageURI(uri);

                videoView.setVisibility(View.GONE);
                MusicPlayerLayout.setVisibility(View.GONE);
            }


        }else if(getIntent().getStringExtra("type").equals("video")){
            videoView.setVisibility(View.VISIBLE);
            if(dataUri != null){
                Uri uri = dataUri;
                MediaController mediaController= new MediaController(SendChatFilesActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);

                imageView.setVisibility(View.GONE);
                MusicPlayerLayout.setVisibility(View.GONE);
            }


        }else
            if(getIntent().getStringExtra("type").equals("audio")){
                MusicPlayerLayout.setVisibility(View.VISIBLE);
                    if(dataUri != null){
                        Uri uri = dataUri;
                        try{
                            progressBar.setProgress(0);
                            mp.reset();
                            mp.setAudioStreamType(3);
                            //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                            mp.setDataSource(SendChatFilesActivity.this, uri);
                            //openFd.close();
                            mp.prepare();
                        }catch (Exception e){
                            Toast.makeText(SendChatFilesActivity.this, "Cannot load audio file", Toast.LENGTH_SHORT).show();
                        }

                        imageView.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                    }

        }

        Musicplaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.pause();
                    Musicplaybtn.setImageResource(R.drawable.play_arrow);
                    return;
                }
                mp.start();
                Musicplaybtn.setImageResource(R.drawable.ic_pause);
                mHandler.post(mUpdateTimeTask);
            }
        });

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });


    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            if (mp.isPlaying()) {
                mHandler.postDelayed(this, 100);
            }

        }
    };

    private void updateTimerAndSeekbar() {
        long currentPosition = (long) this.mp.getCurrentPosition();
        progressBar.setProgress(this.utils.getProgressSeekBar(currentPosition, (long) this.mp.getDuration()));

    }





    private void SendMessage() {
        final ProgressDialog progressDialog = new ProgressDialog(SendChatFilesActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        Cursor cursor = getContentResolver().query(dataUri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        String Filename = System.currentTimeMillis()+name;
        final StorageReference storageReference = mStorage.getReference().child("ChatData").child(prefs.getString("userID", null).toString()).child(getIntent().getStringExtra("frontUser")).child(Filename);
        uploadTask = storageReference.putFile(dataUri)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String dataURL = uri.toString();
                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String currentDateandTime = sdf.format(currentTime);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatMessages");
                        String MessageID = reference.push().getKey();

                        HashMap<String , String> messageMap = new HashMap<>();
                        messageMap.put("Sender",prefs.getString("userID",null));
                        messageMap.put("Reciver",getIntent().getStringExtra("frontUser"));
                        messageMap.put("MsgType",getIntent().getStringExtra("type"));
                        messageMap.put("Content",dataURL);
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
                                            Map.put("FrontUserID",getIntent().getStringExtra("frontUser"));
                                            Map.put("LastMessage",getIntent().getStringExtra("type"));
                                            reference2.child(getIntent().getStringExtra("frontUser")).setValue(Map);

                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("ChatLists").child(getIntent().getStringExtra("frontUser"));
                                            HashMap<String , String> Mapp = new HashMap<>();
                                            Mapp.put("FrontUserID",prefs.getString("userID",null));
                                            Mapp.put("LastMessage",getIntent().getStringExtra("type"));
                                            reference3.child(prefs.getString("userID",null)).setValue(Mapp);
                                            progressDialog.dismiss();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SendChatFilesActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SendChatFilesActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SendChatFilesActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }
}