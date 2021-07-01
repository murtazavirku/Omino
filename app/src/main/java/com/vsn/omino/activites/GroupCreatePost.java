package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hootsuite.nachos.NachoTextView;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;
import com.vsn.omino.FragmentUpload;
import com.vsn.omino.R;
import com.vsn.omino.utiles.MusicUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class GroupCreatePost extends AppCompatActivity {
    LinearLayout PreviewLayout;
    FrameLayout mainLayout;
    //Music Category
    private FloatingActionButton bt_play;
    private Handler mHandler = new Handler();
    private MediaPlayer mp;
    private ProgressBar song_progressbar;
    private MusicUtils utils;
    Uri musicUri;
    LinearLayout MusicPlayerLayout;
    TextView musicFileName;
    private static final int MUSIC_REQUEST = 1;
    //Photography Art Category
    LinearLayout ImageViewLayout;
    private static final int IMG_REQUEST = 2;
    private static final int COVER_REQUEST = 10;
    ArrayList<Uri> mArrayUri;
    Uri singleImageUri;
    TextView imgFileName;
    ImageView imageView;
    ImageView imgback,imgforward;
    RelativeLayout controllerL;
    int img_position;
    //Animation , Theater , Dance Category
    LinearLayout VideoLayout;
    private static final int VIDEO_REQUEST = 3;
    Uri videoUri;
    TextView videoFileName;
    VideoView videoView;
    //Poetry , Philosophy , literature Category
    LinearLayout TextLayout;
    EditText Textpost;
    ImageView textImageView;


    PowerSpinnerView spinnerView;
    Button uploadBtn;
    CardView green,green2,gray,red,orange,yellow,white,purple,blue;
    NachoTextView nachoTextView;
    //upload variables
    FirebaseStorage mStorage;
    StorageTask uploadTask;
    EditText Caption;

    private static final int REQUEST_PERMISSIONS = 100;
    private static final String PERMISSIONS_REQUIRED[] = new String[]{
            android.Manifest.permission.READ_PHONE_STATE
    };

    DatabaseReference databaseReference;
    Button CreatePostBtn,selectCover;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    ColorDrawable drawable;
    Uri CoverUri;
    TextView cover_name;
    LinearLayout cover_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_post);
        ActivityCompat.requestPermissions(GroupCreatePost.this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);


        imgback = (ImageView)  findViewById(R.id.pr_img);
        imgforward = (ImageView)  findViewById(R.id.fr_img);
        controllerL = (RelativeLayout) findViewById(R.id.controllerL);
        controllerL.setVisibility(View.GONE);
        img_position = 0;
        mArrayUri = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        PreviewLayout = (LinearLayout) findViewById(R.id.PreviewLayout);
        PreviewLayout.setVisibility(View.VISIBLE);
        spinnerView =  findViewById(R.id.category_spinner);
        MusicPlayerLayout =  findViewById(R.id.MusicPlayerLayout);
        this.bt_play = (FloatingActionButton)  findViewById(R.id.bt_play);
        ProgressBar progressBar = (ProgressBar)  findViewById(R.id.song_progressbar);
        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        musicFileName = (TextView) findViewById(R.id.textView15);
        ImageViewLayout = (LinearLayout) findViewById(R.id.ImageViewLayout);
        nachoTextView = (NachoTextView)  findViewById(R.id.et_tag);
        CreatePostBtn = (Button) findViewById(R.id.CreatePostBtn);
        selectCover = (Button) findViewById(R.id.selectCover);
        cover_name = (TextView) findViewById(R.id.cover_name);
        cover_layout = (LinearLayout) findViewById(R.id.cover_layout);
        imgFileName = (TextView) findViewById(R.id.ivtext);
        imageView = (ImageView)  findViewById(R.id.imageView);
        VideoLayout = (LinearLayout) findViewById(R.id.VideoLayout);
        videoFileName = (TextView) findViewById(R.id.videotext);
        videoView = (VideoView) findViewById(R.id.videoplayer);
        TextLayout = (LinearLayout) findViewById(R.id.TextLayout);
        Textpost = (EditText) findViewById(R.id.textforPost);
        textImageView = (ImageView) findViewById(R.id.textCover);
        databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
        textImageView.setBackgroundColor(Color.parseColor("#bdc3c7"));
        green = (CardView)  findViewById(R.id.green);
        green2 = (CardView) findViewById(R.id.green2);
        gray = (CardView) findViewById(R.id.gray);
        red = (CardView) findViewById(R.id.red);
        orange = (CardView) findViewById(R.id.orange);
        yellow = (CardView) findViewById(R.id.yellow);
        white = (CardView) findViewById(R.id.white);
        purple = (CardView) findViewById(R.id.purple);
        blue = (CardView) findViewById(R.id.blue);
        Caption = (EditText) findViewById(R.id.Caption);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        drawable = (ColorDrawable) textImageView.getBackground();

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#1abc9c"));
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#2ecc71"));
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#bdc3c7"));
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#c0392b"));
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#e67e22"));
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#f1c40f"));
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#f5f6fa"));
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#9b59b6"));
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textImageView.setBackgroundColor(Color.parseColor("#3498db"));
            }
        });


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_position>0){
                    img_position--;
                    imageView.setImageURI(mArrayUri.get(img_position));
                }
            }
        });
        imgforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(img_position<mArrayUri.size()-1){
                    img_position++;
                    imageView.setImageURI(mArrayUri.get(img_position));
                }
            }
        });

        CreatePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinnerView.getText().toString().equals("Photography") || spinnerView.getText().toString().equals("Art")){
                    SwitchCompat analytics = (SwitchCompat)findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)findViewById(R.id.NFT);
                    if(analytics.isChecked() == true && NFT.isChecked() == true){
                        CreateImagePost(mArrayUri,spinnerView.getText().toString(),"yes","yes",CoverUri);
                    }
                    if(analytics.isChecked() == true && NFT.isChecked() == false){
                        CreateImagePost(mArrayUri,spinnerView.getText().toString(),"yes","no",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == true){
                        CreateImagePost(mArrayUri,spinnerView.getText().toString(),"no","yes",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == false){
                        CreateImagePost(mArrayUri,spinnerView.getText().toString(),"no","no",CoverUri);
                    }


                }
                if(spinnerView.getText().toString().equals("Animation") || spinnerView.getText().toString().equals("Dance") || spinnerView.getText().toString().equals("Theater")){

                    SwitchCompat analytics = (SwitchCompat)findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)findViewById(R.id.NFT);
                    if(analytics.isChecked() == true && NFT.isChecked() == true){
                        CreatePost(videoUri,spinnerView.getText().toString(),"yes","yes",CoverUri);
                    }
                    if(analytics.isChecked() == true && NFT.isChecked() == false){
                        CreatePost(videoUri,spinnerView.getText().toString(),"yes","no",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == true){
                        CreatePost(videoUri,spinnerView.getText().toString(),"no","yes",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == false){
                        CreatePost(videoUri,spinnerView.getText().toString(),"no","no",CoverUri);
                    }

                }
                if(spinnerView.getText().toString().equals("Music")){

                    SwitchCompat analytics = (SwitchCompat)findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)findViewById(R.id.NFT);
                    if(analytics.isChecked() == true && NFT.isChecked() == true){
                        CreatePost(musicUri,spinnerView.getText().toString(),"yes","yes",CoverUri);
                    }
                    if(analytics.isChecked() == true && NFT.isChecked() == false){
                        CreatePost(musicUri,spinnerView.getText().toString(),"yes","no",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == true){
                        CreatePost(musicUri,spinnerView.getText().toString(),"no","yes",CoverUri);
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == false){
                        CreatePost(musicUri,spinnerView.getText().toString(),"no","no",CoverUri);
                    }

                }
                if(spinnerView.getText().toString().equals("Literature") || spinnerView.getText().toString().equals("Philosophy") || spinnerView.getText().toString().equals("Poetry")){
                    SwitchCompat analytics = (SwitchCompat)findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)findViewById(R.id.NFT);
                    if(analytics.isChecked() == true && NFT.isChecked() == true){
                        drawable = (ColorDrawable) textImageView.getBackground();
                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"yes","yes",drawable.getColor());
                    }
                    if(analytics.isChecked() == true && NFT.isChecked() == false){
                        drawable = (ColorDrawable) textImageView.getBackground();
                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"yes","no", drawable.getColor());
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == true){
                        drawable = (ColorDrawable) textImageView.getBackground();
                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"no","yes", drawable.getColor());
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == false){
                        drawable = (ColorDrawable) textImageView.getBackground();
                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"no","no", drawable.getColor());
                    }
                }
            }
        });
        //======================================  MUSIC
        this.song_progressbar = progressBar;
        progressBar.setProgress(0);
        this.song_progressbar.setMax(10000);
        MediaPlayer mediaPlayer = new MediaPlayer();
        this.mp = mediaPlayer;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                bt_play.setImageResource(R.drawable.play_arrow);
            }
        });

        this.utils = new MusicUtils();
        this.bt_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if ( mp.isPlaying()) {
                     mp.pause();
                     bt_play.setImageResource(R.drawable.play_arrow);
                    return;
                }
                 mp.start();
                 bt_play.setImageResource(R.drawable.ic_pause);
                 mHandler.post( mUpdateTimeTask);
            }
        });
        //======================================  MUSIC


//        List<IconSpinnerItem> iconSpinnerItems = new ArrayList<>();
//        iconSpinnerItems.add(new IconSpinnerItem("item", contextDrawable(R.drawable.icons8_add_image_96)));
//
//        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(spinnerView);
//        spinnerView.setSpinnerAdapter(iconSpinnerAdapter);
//        spinnerView.setItems(iconSpinnerItems);
//        spinnerView.selectItemByIndex(0);
//        spinnerView.setLifecycleOwner(this);

        spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                Log.e("Spinner Item",spinnerView.getText().toString());
                if(spinnerView.getText().toString().equals("Music")){
                    mp.reset();
                    videoView.stopPlayback();
                    MusicPlayerLayout.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                    ImageViewLayout.setVisibility(View.GONE);
                    VideoLayout.setVisibility(View.GONE);
                    TextLayout.setVisibility(View.GONE);
                    PreviewLayout.setVisibility(View.GONE);
                    cover_layout.setVisibility(View.VISIBLE);
                }
                if(spinnerView.getText().toString().equals("Photography") || spinnerView.getText().toString().equals("Art")){
                    mp.reset();
                    videoView.stopPlayback();
                    ImageViewLayout.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                    MusicPlayerLayout.setVisibility(View.GONE);
                    VideoLayout.setVisibility(View.GONE);
                    TextLayout.setVisibility(View.GONE);
                    PreviewLayout.setVisibility(View.GONE);
                    cover_layout.setVisibility(View.GONE);
                }
                if(spinnerView.getText().toString().equals("Animation") || spinnerView.getText().toString().equals("Dance") || spinnerView.getText().toString().equals("Theater")){
                    mp.reset();
                    videoView.stopPlayback();
                    VideoLayout.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                    MusicPlayerLayout.setVisibility(View.GONE);
                    ImageViewLayout.setVisibility(View.GONE);
                    TextLayout.setVisibility(View.GONE);
                    PreviewLayout.setVisibility(View.GONE);
                    cover_layout.setVisibility(View.VISIBLE);
                }
                if(spinnerView.getText().toString().equals("Literature") || spinnerView.getText().toString().equals("Philosophy") || spinnerView.getText().toString().equals("Poetry")){
                    mp.reset();
                    videoView.stopPlayback();
                    TextLayout.setVisibility(View.VISIBLE);
                    VideoLayout.setVisibility(View.GONE);
                    MusicPlayerLayout.setVisibility(View.GONE);
                    ImageViewLayout.setVisibility(View.GONE);
                    uploadBtn.setVisibility(View.GONE);
                    PreviewLayout.setVisibility(View.GONE);
                    cover_layout.setVisibility(View.GONE);
                }


            }
        });
        selectCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Cover Art"), COVER_REQUEST);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.reset();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    videoView.resetPivot();
                }
                if(spinnerView.getText().toString().equals("Music")){
                    Intent intent = new Intent();
                    intent.setType("audio/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Music"), MUSIC_REQUEST);
                }else if(spinnerView.getText().toString().equals("Photography") || spinnerView.getText().toString().equals("Art")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Image(s)"), IMG_REQUEST);


                }else if(spinnerView.getText().toString().equals("Animation") || spinnerView.getText().toString().equals("Dance") || spinnerView.getText().toString().equals("Theater")){
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Video"), VIDEO_REQUEST);
                }
                else{
                    Toast.makeText(GroupCreatePost.this, "Select valid category", Toast.LENGTH_SHORT).show();
                }


            }
        });

        CreateTags("Sheraz","Naeem","Hassan");


    }



    private void CreateImagePost(ArrayList<Uri> mArrayUri, String category, String analytics, String nft, Uri coverUri) {

        if (checkInternetConnection(GroupCreatePost.this)) {


            if(mArrayUri.size()>0){

                String post_id = databaseReference.push().getKey();
                for (int i = 0; i < mArrayUri.size(); i++) {
                    final ProgressDialog progressDialog = new ProgressDialog(GroupCreatePost.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    Uri postUri = mArrayUri.get(i);
                    Cursor cursor = getContentResolver().query(postUri, null, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    String name = cursor.getString(nameIndex);
                    String Filename = System.currentTimeMillis()+name;
                    final StorageReference storageReference = mStorage.getReference().child("PostData").child(prefs.getString("userID", null).toString()).child(Filename);
                    uploadTask = storageReference.putFile(postUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String PostdataURL = uri.toString();
                                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                            HashMap<String,String> postMap = new HashMap<>();
                                            postMap.put("dataURL",PostdataURL);
                                            postMap.put("caption",Caption.getText().toString());
                                            postMap.put("isanalytics",analytics);
                                            postMap.put("isnft",nft);
                                            postMap.put("tags",nachoTextView.getText().toString());
                                            postMap.put("category",category);
                                            postMap.put("datetimepost",df.format(Calendar.getInstance().getTime()).toString());
                                            postMap.put("userid",prefs.getString("userID", null).toString());
                                            postMap.put("usermail",prefs.getString("userMail", null).toString());
                                            postMap.put("usernamee",prefs.getString("userName", null).toString());
                                            postMap.put("color","none");
                                            postMap.put("postid",post_id);
                                            postMap.put("coverart","none");
                                            postMap.put("Groupid",getIntent().getStringExtra("id"));
                                            databaseReference.child(post_id).setValue(postMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){

                                                                DatabaseReference referencee = FirebaseDatabase.getInstance().getReference("ImagesPostImages").child(post_id);
                                                                String image_id = referencee.push().getKey();
                                                                HashMap<String,String> postMap1 = new HashMap<>();
                                                                postMap1.put("ImageLink",PostdataURL);
                                                                referencee.child(image_id).setValue(postMap1);
                                                                progressDialog.dismiss();
                                                            }
                                                            else{
                                                                progressDialog.dismiss();
                                                                Toast.makeText(GroupCreatePost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        }
                                    });
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                    double finalProgress = Double.parseDouble(String.format("%.1f", progress));
                                    //System.out.println("Upload is " + progress + "% done");
                                    int currentprogress = (int) progress;
                                    progressDialog.setProgress(currentprogress);
                                    progressDialog.setMessage("Please Wait...("+finalProgress+"%)");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Snackbar.make(mainLayout, e.toString(), Snackbar.LENGTH_LONG)
                                            .setAction("CLOSE", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    progressDialog.dismiss();
                                                }
                                            })
                                            .setActionTextColor(getResources().getColor(android.R.color.black))
                                            .setBackgroundTint(getResources().getColor(android.R.color.holo_red_light ))
                                            .show();
                                }
                            });

                }

            }
            else if(singleImageUri != null){
                mArrayUri.clear();
                mArrayUri.add(singleImageUri);
                if(mArrayUri.size()>0){

                    String post_id = databaseReference.push().getKey();
                    for (int i = 0; i < mArrayUri.size(); i++) {
                        final ProgressDialog progressDialog = new ProgressDialog( GroupCreatePost.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        Uri postUri = mArrayUri.get(i);
                        Cursor cursor =  getContentResolver().query(postUri, null, null, null, null);
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        cursor.moveToFirst();
                        String name = cursor.getString(nameIndex);
                        String Filename = System.currentTimeMillis()+name;
                        final StorageReference storageReference = mStorage.getReference().child("PostData").child(prefs.getString("userID", null).toString()).child(Filename);
                        uploadTask = storageReference.putFile(postUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                String PostdataURL = uri.toString();
                                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                HashMap<String,String> postMap = new HashMap<>();
                                                postMap.put("dataURL",PostdataURL);
                                                postMap.put("caption",Caption.getText().toString());
                                                postMap.put("isanalytics",analytics);
                                                postMap.put("isnft",nft);
                                                postMap.put("tags",nachoTextView.getText().toString());
                                                postMap.put("category",category);
                                                postMap.put("datetimepost",df.format(Calendar.getInstance().getTime()).toString());
                                                postMap.put("userid",prefs.getString("userID", null).toString());
                                                postMap.put("usermail",prefs.getString("userMail", null).toString());
                                                postMap.put("usernamee",prefs.getString("userName", null).toString());
                                                postMap.put("color","none");
                                                postMap.put("postid",post_id);
                                                postMap.put("coverart","none");
                                                postMap.put("Groupid",getIntent().getStringExtra("id"));
                                                databaseReference.child(post_id).setValue(postMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){

                                                                    DatabaseReference referencee = FirebaseDatabase.getInstance().getReference("ImagesPostImages").child(post_id);
                                                                    String image_id = referencee.push().getKey();
                                                                    HashMap<String,String> postMap1 = new HashMap<>();
                                                                    postMap1.put("ImageLink",PostdataURL);
                                                                    referencee.child(image_id).setValue(postMap1);
                                                                    progressDialog.dismiss();
                                                                }
                                                                else{
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText( GroupCreatePost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });


                                            }
                                        });
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                        double finalProgress = Double.parseDouble(String.format("%.1f", progress));
                                        //System.out.println("Upload is " + progress + "% done");
                                        int currentprogress = (int) progress;
                                        progressDialog.setProgress(currentprogress);
                                        progressDialog.setMessage("Please Wait...("+finalProgress+"%)");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Snackbar.make(mainLayout, e.toString(), Snackbar.LENGTH_LONG)
                                                .setAction("CLOSE", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        progressDialog.dismiss();
                                                    }
                                                })
                                                .setActionTextColor(getResources().getColor(android.R.color.black))
                                                .setBackgroundTint(getResources().getColor(android.R.color.holo_red_light ))
                                                .show();
                                    }
                                });

                    }

                }
                else{
                    Toast.makeText( GroupCreatePost.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText( GroupCreatePost.this, "Please Select Valid File", Toast.LENGTH_SHORT).show();
            }
        }



    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
             updateTimerAndSeekbar();
            if ( mp.isPlaying()) {
                 mHandler.postDelayed(this, 100);
            }

        }
    };

    private void updateTimerAndSeekbar() {
        long currentPosition = (long) this.mp.getCurrentPosition();
        this.song_progressbar.setProgress(this.utils.getProgressSeekBar(currentPosition, (long) this.mp.getDuration()));

    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==MUSIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            try {
                musicUri = data.getData();
                try {
                    this.song_progressbar.setProgress(0);
                     mp.reset();
                     mp.setAudioStreamType(3);
                    //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                     mp.setDataSource( GroupCreatePost.this, musicUri);
                    //openFd.close();
                     mp.prepare();
                    //this.mp.start();
                } catch (Exception unused) {
                    Toast.makeText( GroupCreatePost.this, "Cannot load audio file", Toast.LENGTH_SHORT).show();
                }
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                musicFileName.setText(file.getName().toString());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText( GroupCreatePost.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }


        if(requestCode==IMG_REQUEST && resultCode == RESULT_OK && data != null){
            try {
                if(data.getClipData() != null) {
                    controllerL.setVisibility(View.VISIBLE);
                    mArrayUri.clear();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    //Toast.makeText( GroupCreatePost.this, String.valueOf(count), Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < count; i++){
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        mArrayUri.add(uri);
                        //Toast.makeText( GroupCreatePost.this, data.getClipData().getItemAt(i).getUri().toString(), Toast.LENGTH_SHORT).show();
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    imageView.setImageURI(mArrayUri.get(0));
                    img_position = 0;
//                    singleImageUri = data.getClipData().getItemAt(0).getUri();
//                    File file= new File(singleImageUri.getPath());
//                    Log.e("FileName",file.getName().toString());
//                    imgFileName.setText(file.getName().toString());
//                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(singleImageUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    imageView.setImageBitmap(selectedImage);
                }else if(data.getData() != null) {
                    controllerL.setVisibility(View.GONE);
                    singleImageUri = data.getData();
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                    Uri uri= singleImageUri;
//                    File file= new File(uri.getPath());
//                    Log.e("FileName",file.getName().toString());
//                    imgFileName.setText(file.getName().toString());
//                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageURI(uri);
                }


            } catch (Exception e) {
                Log.e("ImageChooser",e.getMessage());
                Toast.makeText( GroupCreatePost.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

        if(requestCode==VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            try {
                videoUri = data.getData();
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                videoFileName.setText(file.getName().toString());
                MediaController mediaController= new MediaController( GroupCreatePost.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                videoView.start();

//                File video_file = FileUtils.getFileFromUri( GroupCreatePost.this, uri);
//                video_url=video_file.getAbsolutePath();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("getFileFromUri",e.getMessage());
                Toast.makeText( GroupCreatePost.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

        if(requestCode==COVER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            try {
                CoverUri = data.getData();
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                cover_name.setText(uri.getPath());

//                File video_file = FileUtils.getFileFromUri( GroupCreatePost.this, uri);
//                video_url=video_file.getAbsolutePath();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("getFileFromUri",e.getMessage());
                Toast.makeText( GroupCreatePost.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void CreatePost(Uri postUri,String Category,String analytics,String NFT,Uri CoverUri){

        if (checkInternetConnection( GroupCreatePost.this)){
            if(postUri == null || CoverUri == null){
                Toast.makeText( GroupCreatePost.this, "Please Select Valid File & Cover Art", Toast.LENGTH_SHORT).show();
            }
            else{
                final ProgressDialog progressDialog = new ProgressDialog( GroupCreatePost.this);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                Uri uri = postUri;
                Cursor cursor =  getContentResolver().query(uri, null, null, null, null);
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                String name = cursor.getString(nameIndex);
                String Filename = System.currentTimeMillis()+name;
                final StorageReference storageReference = mStorage.getReference().child("PostData").child(prefs.getString("userID", null).toString()).child(Filename);
                uploadTask = storageReference.putFile(postUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        if(CoverUri != null){
                                            Uri cover = CoverUri;
                                            Cursor cursor1 =  GroupCreatePost.this.getContentResolver().query(cover, null, null, null, null);
                                            int nameIndex1 = cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                            cursor1.moveToFirst();
                                            String name1 = cursor1.getString(nameIndex1);
                                            String Filename1 = System.currentTimeMillis()+name1;
                                            final StorageReference storageReference1 = mStorage.getReference().child("PostData").child(prefs.getString("userID", null).toString()).child(Filename1);
                                            uploadTask = storageReference1.putFile(cover)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            final Task<Uri> coverUri = taskSnapshot.getStorage().getDownloadUrl();
                                                            coverUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri urii) {
                                                                    String PostdataURL = uri.toString();
                                                                    String CoverdataURL = urii.toString();
                                                                    String post_id = databaseReference.push().getKey();
                                                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                                PostDataModel postDataModel = new PostDataModel(PostdataURL,Caption.getText().toString(),analytics,NFT,nachoTextView.getText().toString(),Category,df.format(Calendar.getInstance().getTime()).toString(),prefs.getString("userID", null).toString(),prefs.getString("userMail", null).toString(),prefs.getString("userName", null).toString());

                                                                    HashMap<String,String> postMap = new HashMap<>();
                                                                    postMap.put("dataURL",PostdataURL);
                                                                    postMap.put("caption",Caption.getText().toString());
                                                                    postMap.put("isanalytics",analytics);
                                                                    postMap.put("isnft",NFT);
                                                                    postMap.put("tags",nachoTextView.getText().toString());
                                                                    postMap.put("category",Category);
                                                                    postMap.put("datetimepost",df.format(Calendar.getInstance().getTime()).toString());
                                                                    postMap.put("userid",prefs.getString("userID", null).toString());
                                                                    postMap.put("usermail",prefs.getString("userMail", null).toString());
                                                                    postMap.put("usernamee",prefs.getString("userName", null).toString());
                                                                    postMap.put("color","none");
                                                                    postMap.put("postid",post_id);
                                                                    postMap.put("coverart",CoverdataURL);
                                                                    postMap.put("Groupid",getIntent().getStringExtra("id"));
                                                                    databaseReference.child(post_id).setValue(postMap)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                    else{
                                                                                        progressDialog.dismiss();
                                                                                        Toast.makeText( GroupCreatePost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                        }
                                                    });
                                        }












                                    }
                                });



                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                double finalProgress = Double.parseDouble(String.format("%.1f", progress));
                                //System.out.println("Upload is " + progress + "% done");
                                int currentprogress = (int) progress;
                                progressDialog.setProgress(currentprogress);
                                progressDialog.setMessage("Please Wait...("+finalProgress+"%)");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Snackbar.make(mainLayout, e.toString(), Snackbar.LENGTH_LONG)
                                        .setAction("CLOSE", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.black))
                                        .setBackgroundTint(getResources().getColor(android.R.color.holo_red_light ))
                                        .show();
                            }
                        });

            }
        }
        else{
            Snackbar.make(mainLayout, "Not Internet", Snackbar.LENGTH_LONG)
                    .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.black))
                    .setBackgroundTint(getResources().getColor(android.R.color.holo_red_light ))
                    .show();
        }


    }

    private void CreateTextPost(String Text, String Category, String analytics, String NFT, int color) {

        final ProgressDialog progressDialog = new ProgressDialog( GroupCreatePost.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        String id = databaseReference.push().getKey();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                                PostDataModel postDataModel = new PostDataModel(PostdataURL,Caption.getText().toString(),analytics,NFT,nachoTextView.getText().toString(),Category,df.format(Calendar.getInstance().getTime()).toString(),prefs.getString("userID", null).toString(),prefs.getString("userMail", null).toString(),prefs.getString("userName", null).toString());
        HashMap<String,String> postMap = new HashMap<>();
        postMap.put("dataURL",Text);
        postMap.put("caption",Caption.getText().toString());
        postMap.put("isanalytics",analytics);
        postMap.put("isnft",NFT);
        postMap.put("tags",nachoTextView.getText().toString());
        postMap.put("category",Category);
        postMap.put("datetimepost",df.format(Calendar.getInstance().getTime()).toString());
        postMap.put("userid",prefs.getString("userID", null).toString());
        postMap.put("usermail",prefs.getString("userMail", null).toString());
        postMap.put("usernamee",prefs.getString("userName", null).toString());
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        postMap.put("color",hexColor);
        postMap.put("coverart",hexColor);
        postMap.put("postid",id);
        postMap.put("Groupid",getIntent().getStringExtra("id"));
        databaseReference.child(id).setValue(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                        }
                        else{
                            Toast.makeText( GroupCreatePost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }





    private String getUserName() {
        final String[] name = {"noname"};
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name[0] = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText( GroupCreatePost.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return name[0];
    }

    private void CreateTags(String... tags){

        List arrayList = new ArrayList();

        for (String singleTAG : tags)
        {
            arrayList.add(singleTAG);
        }
        nachoTextView.setText(arrayList);
        nachoTextView.addChipTerminator((char) 10, 0);
    }



    public static boolean checkInternetConnection(Context context)
    {
        try
        {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}