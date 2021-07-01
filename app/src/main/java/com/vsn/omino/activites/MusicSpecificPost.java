package com.vsn.omino.activites;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.Adapters.CommentAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.CommentModel;
import com.vsn.omino.utiles.MusicUtils;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MusicSpecificPost extends AppCompatActivity {

    public ArrayList<String> userData;
    public ArrayList<String> frontUser;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    private FloatingActionButton bt_play;
    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            if (mp.isPlaying()) {
                mHandler.postDelayed(this, 100);
            }
        }
    };
    private MediaPlayer mp;
    private View parent_view;
    private AppCompatSeekBar seek_song_progressbar;
    private TextView tv_song_current_duration;
    private TextView tv_song_total_duration;
    private MusicUtils utils;

    TextView Caption,Author;
    ImageView MusicCover;
    ImageView BtnSend;
    EditText text_content;
    CommentAdapter adapter;
    List<CommentModel> commentModelList;
    RecyclerView recyclerView;
    ImageView likePost,savePost,subUser;
    Boolean likePostFlag;
    @Override
    protected void onStart() {
        super.onStart();
        LoadUserDatabyID(prefs.getString("userID", null).toString());
        FrontUserDatabyID(getIntent().getStringExtra("userid"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_specific_post);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        likePost = (ImageView)findViewById(R.id.likePost);
        savePost = (ImageView)findViewById(R.id.savePost);
        subUser = (ImageView)findViewById(R.id.subUser);
        userData = new ArrayList<>();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mp = mediaPlayer;
        commentModelList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        initComponent();
        LoadAllComments();
        Load_react_colors();
        Load_subs_colors();
        Load_saved_colors();
        likePostFlag = false;
        Caption = (TextView)findViewById(R.id.Musiccaption);
        Author = (TextView)findViewById(R.id.MusicAuthor);
        MusicCover = (ImageView) findViewById(R.id.Musiccover);

        LoadUserDatabyID(prefs.getString("userID", null).toString());
        FrontUserDatabyID(getIntent().getStringExtra("userid"));
        Caption.setText(getIntent().getStringExtra("caption"));
        frontUser = new ArrayList<>();
        Picasso.get().load(getIntent().getStringExtra("cover")).into(MusicCover);
        BtnSend = (ImageView)findViewById(R.id.btn_send);
        text_content = (EditText)findViewById(R.id.text_content);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                bt_play.setImageResource(R.drawable.play_arrow);
            }
        });
        likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likePostFlag )
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions").child(getIntent().getStringExtra("postid"));
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        likePost.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                                        likePostFlag = false;
                                    }
                                }
                            });

                }
                else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions").child(getIntent().getStringExtra("postid"));
                    HashMap<String,String> reactMap = new HashMap<>();
                    reactMap.put("React","like");
                    reactMap.put("userid",prefs.getString("userID", null).toString());
                    reactMap.put("userReactTime",currentDateAndTime);
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(reactMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        likePost.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                                        likePostFlag = true;

                                    }
                                }
                            });
                }

            }
        });
        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(savePost.getColorFilter() == null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedCollection").child(prefs.getString("userID", null).toString()).child("MusicCollections");
                    String random_id = databaseReference.push().getKey();
                    HashMap<String, String> CollectionMap = new HashMap<>();
                    CollectionMap.put("DataUrl", getIntent().getStringExtra("uri"));
                    CollectionMap.put("Postid", getIntent().getStringExtra("postid"));
                    CollectionMap.put("Cover", getIntent().getStringExtra("cover"));
                    databaseReference.child(random_id).setValue(CollectionMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    savePost.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                                }
                            });
                }
            }
        });
        subUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("userid").equals(prefs.getString("userID", null).toString())){

                }
                else{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions").child(getIntent().getStringExtra("userid"));
                    HashMap<String,String> reactMap = new HashMap<>();
                    reactMap.put("subscriber",prefs.getString("userID", null).toString());
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(reactMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        subUser.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);

                                    }
                                }
                            });
                }
            }
        });
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = text_content.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());

                if (!comment.isEmpty()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(getIntent().getStringExtra("postid"));
                    String random_id = reference.push().getKey();
                    HashMap<String,String> commentMap = new HashMap<>();
                    commentMap.put("usercommentid",prefs.getString("userID", null).toString());
                    commentMap.put("usercomment",text_content.getText().toString());
                    commentMap.put("usercommenttime",currentDateAndTime);
                    reference.child(random_id).setValue(commentMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        adapter.notifyDataSetChanged();
                                        text_content.setText("");
                                    }
                                    else{
                                        Toast.makeText(MusicSpecificPost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });



    }

    private void LoadAllComments() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(getIntent().getStringExtra("postid"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    String user = snapshot1.child("usercommentid").getValue().toString();
                    String comment = snapshot1.child("usercomment").getValue().toString();
                    commentModelList.add(new CommentModel(user,comment));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MusicSpecificPost.this));
                adapter = new CommentAdapter(MusicSpecificPost.this,commentModelList,userData);
                recyclerView.setAdapter(adapter);
                if(recyclerView.getAdapter().getItemCount()>0) {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                }
                try{
                    Author.setText(frontUser.get(3));
                }catch (IndexOutOfBoundsException e){
                    Log.e("ouf of bound",e.getMessage());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void initComponent() {
        this.parent_view = findViewById(R.id.parent_view);
        this.seek_song_progressbar = (AppCompatSeekBar) findViewById(R.id.seek_song_progressbar);
        this.bt_play = (FloatingActionButton) findViewById(R.id.bt_play);
        this.seek_song_progressbar.setProgress(0);
        this.seek_song_progressbar.setMax(10000);
        this.tv_song_current_duration = (TextView) findViewById(R.id.tv_song_current_duration);
        this.tv_song_total_duration = (TextView) findViewById(R.id.tv_song_total_duration);

        try {
                //MusicTime.setProgress(0);
                //mp.reset();
                mp.setAudioStreamType(3);
                //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                Uri uri= Uri.parse(getIntent().getStringExtra("uri"));
                mp.setDataSource(MusicSpecificPost.this,uri);
                //openFd.close();
                mp.prepareAsync();
        } catch (Exception unused) {
            Snackbar.make(this.parent_view, (CharSequence) "Cannot load audio file", BaseTransientBottomBar.LENGTH_LONG).show();
        }
        this.utils = new MusicUtils();
        this.seek_song_progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mp.seekTo(utils.progressToTimer(seekBar.getProgress(), mp.getDuration()));
                mHandler.post(mUpdateTimeTask);
            }
        });
        buttonPlayerAction();
        updateTimerAndSeekbar();
    }

    private void buttonPlayerAction() {
        this.bt_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mp.isPlaying()) {
                    mp.pause();
                    bt_play.setImageResource(R.drawable.play_arrow);
//                    mHandler.removeCallbacks(mUpdateTimeTask);
//                    mp.release();
                    return;
                }
                mp.start();
                bt_play.setImageResource(R.drawable.ic_pause);
                mHandler.post(mUpdateTimeTask);
            }
        });
    }

    public void controlClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                toggleButtonColor((ImageButton) view);
                //Snackbar.make(this.parent_view, (CharSequence) "Forward", BaseTransientBottomBar.LENGTH_LONG).show();
                if (mp != null) {
                    int currentPosition = mp.getCurrentPosition();
                    if (currentPosition + 5000 <= mp.getDuration()) {
                        mp.seekTo(currentPosition + 5000);
                    } else {
                        mp.seekTo(mp.getDuration());
                    }
                }
                return;
            case R.id.bt_prev:
                toggleButtonColor((ImageButton) view);
                //Snackbar.make(this.parent_view, (CharSequence) "Previous", BaseTransientBottomBar.LENGTH_LONG).show();
                if (mp != null) {
                    int currentPosition = mp.getCurrentPosition();
                    if (currentPosition - 5000 >= 0) {
                        mp.seekTo(currentPosition - 5000);
                    } else {
                        mp.seekTo(0);
                    }
                }
                return;
            default:
                return;
        }
    }

    private boolean toggleButtonColor(ImageButton imageButton) {
        if (((String) imageButton.getTag(imageButton.getId())) != null) {
            imageButton.setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_ATOP);
            imageButton.setTag(imageButton.getId(), null);
            return false;
        }
        imageButton.setTag(imageButton.getId(), "selected");
        imageButton.setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    private void updateTimerAndSeekbar() {
        long duration = (long) mp.getDuration();
        //Toast.makeText(MusicSpecificPost.this, String.valueOf(duration), Toast.LENGTH_SHORT).show();
        long currentPosition = (long) mp.getCurrentPosition();
        tv_song_total_duration.setText(utils.milliSecondsToTimer(duration));
        tv_song_current_duration.setText(utils.milliSecondsToTimer(currentPosition));
        seek_song_progressbar.setProgress(utils.getProgressSeekBar(currentPosition, duration));
    }


    public void LoadUserDatabyID(String UserID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData.add(0,snapshot.child("email").getValue().toString());
                userData.add(1,snapshot.child("phone_number").getValue().toString());
                userData.add(2,snapshot.child("user_id").getValue().toString());
                userData.add(3,snapshot.child("username").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MusicSpecificPost.this, "Something went wronge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacks(this.mUpdateTimeTask);
        this.mp.release();
    }
    public void FrontUserDatabyID(String UserID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                frontUser.add(0,snapshot.child("email").getValue().toString());
                frontUser.add(1,snapshot.child("phone_number").getValue().toString());
                frontUser.add(2,snapshot.child("user_id").getValue().toString());
                frontUser.add(3,snapshot.child("username").getValue().toString());
                Author.setText(frontUser.get(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MusicSpecificPost.this, "Something went wronge", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Load_react_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(getIntent().getStringExtra("postid"))){
                        if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){
                            likePost.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                            likePostFlag = true;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Load_subs_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(getIntent().getStringExtra("userid"))){
                        if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){
                            subUser.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Load_saved_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedCollection").child(prefs.getString("userID", null).toString()).child("MusicCollections");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.child("Postid").getValue().toString().equals(getIntent().getStringExtra("postid"))){

                        savePost.setColorFilter(ContextCompat.getColor(MusicSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}