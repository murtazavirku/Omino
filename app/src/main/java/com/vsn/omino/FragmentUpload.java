package com.vsn.omino;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vsn.omino.utiles.MusicUtils;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragmentUpload extends Fragment {
    // compression
//    private ProgressDialog progressDialog;
//    private String video_url;
//    private Runnable r;
//    private static final String root= Environment.getExternalStorageDirectory().toString();
//    private static final String app_folder=root+"/GFG/";
//    com.github.hiteshsondhi88.libffmpeg.FFmpeg ffmpeg;
  // compression
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
//    LinearLayout TextLayout;
//    EditText Textpost;
//    ImageView textImageView;


    PowerSpinnerView spinnerView;
    Button uploadBtn;
    //CardView green,green2,gray,red,orange,yellow,white,purple,blue;
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

    //Docs Layout
    LinearLayout DocumentLayout;
    TextView doctext;
    private static final int DOC_REQUEST = 4;
    Uri DocUri;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        imgback = (ImageView) view.findViewById(R.id.pr_img);
        imgforward = (ImageView) view.findViewById(R.id.fr_img);
        controllerL = (RelativeLayout)view.findViewById(R.id.controllerL);
        controllerL.setVisibility(View.GONE);
        img_position = 0;
        mArrayUri = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        mainLayout = (FrameLayout)view.findViewById(R.id.mainLayout);
        PreviewLayout = (LinearLayout)view.findViewById(R.id.PreviewLayout);
        PreviewLayout.setVisibility(View.VISIBLE);
        spinnerView = view.findViewById(R.id.category_spinner);
        MusicPlayerLayout = view.findViewById(R.id.MusicPlayerLayout);
        this.bt_play = (FloatingActionButton) view.findViewById(R.id.bt_play);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.song_progressbar);
        uploadBtn = (Button)view.findViewById(R.id.uploadBtn);
        musicFileName = (TextView)view.findViewById(R.id.textView15);
        ImageViewLayout = (LinearLayout)view.findViewById(R.id.ImageViewLayout);
        nachoTextView = (NachoTextView) view.findViewById(R.id.et_tag);
        CreatePostBtn = (Button)view.findViewById(R.id.CreatePostBtn);
        selectCover = (Button)view.findViewById(R.id.selectCover);
        cover_name = (TextView)view.findViewById(R.id.cover_name);
        cover_layout = (LinearLayout)view.findViewById(R.id.cover_layout);
        imgFileName = (TextView)view.findViewById(R.id.ivtext);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        VideoLayout = (LinearLayout)view.findViewById(R.id.VideoLayout);
        videoFileName = (TextView)view.findViewById(R.id.videotext);
        videoView = (VideoView)view.findViewById(R.id.videoplayer);
//        TextLayout = (LinearLayout)view.findViewById(R.id.TextLayout);
//        Textpost = (EditText)view.findViewById(R.id.textforPost);
//        textImageView = (ImageView)view.findViewById(R.id.textCover);
        databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
//        textImageView.setBackgroundColor(Color.parseColor("#bdc3c7"));
//        green = (CardView) view.findViewById(R.id.green);
//        green2 = (CardView)view.findViewById(R.id.green2);
//        gray = (CardView)view.findViewById(R.id.gray);
//        red = (CardView)view.findViewById(R.id.red);
//        orange = (CardView)view.findViewById(R.id.orange);
//        yellow = (CardView)view.findViewById(R.id.yellow);
//        white = (CardView)view.findViewById(R.id.white);
//        purple = (CardView)view.findViewById(R.id.purple);
//        blue = (CardView)view.findViewById(R.id.blue);
        Caption = (EditText)view.findViewById(R.id.Caption);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        drawable = (ColorDrawable) textImageView.getBackground();
        DocumentLayout= (LinearLayout)view.findViewById(R.id.DocumentLayout);
        doctext= (TextView)view.findViewById(R.id.doctext);
//        green.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#1abc9c"));
//            }
//        });
//        green2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#2ecc71"));
//            }
//        });
//        gray.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#bdc3c7"));
//            }
//        });
//        red.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#c0392b"));
//            }
//        });
//        orange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#e67e22"));
//            }
//        });
//        yellow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#f1c40f"));
//            }
//        });
//        white.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#f5f6fa"));
//            }
//        });
//        purple.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#9b59b6"));
//            }
//        });
//        blue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textImageView.setBackgroundColor(Color.parseColor("#3498db"));
//            }
//        });


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
                    SwitchCompat analytics = (SwitchCompat)view.findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)view.findViewById(R.id.NFT);
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

                    SwitchCompat analytics = (SwitchCompat)view.findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)view.findViewById(R.id.NFT);
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

                    SwitchCompat analytics = (SwitchCompat)view.findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)view.findViewById(R.id.NFT);
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
                    SwitchCompat analytics = (SwitchCompat)view.findViewById(R.id.analytics);
                    SwitchCompat NFT = (SwitchCompat)view.findViewById(R.id.NFT);
                    if(analytics.isChecked() == true && NFT.isChecked() == true){
                        CreateDocPost(DocUri,spinnerView.getText().toString(),"yes","yes",CoverUri);
//                        drawable = (ColorDrawable) textImageView.getBackground();
//                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"yes","yes",drawable.getColor());
                    }
                    if(analytics.isChecked() == true && NFT.isChecked() == false){
                        CreateDocPost(DocUri,spinnerView.getText().toString(),"yes","no",CoverUri);
//                        drawable = (ColorDrawable) textImageView.getBackground();
//                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"yes","no", drawable.getColor());
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == true){
                        CreateDocPost(DocUri,spinnerView.getText().toString(),"no","yes",CoverUri);
//                        drawable = (ColorDrawable) textImageView.getBackground();
//                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"no","yes", drawable.getColor());
                    }
                    if(analytics.isChecked() == false && NFT.isChecked() == false){
                        CreateDocPost(DocUri,spinnerView.getText().toString(),"no","no",CoverUri);
//                        drawable = (ColorDrawable) textImageView.getBackground();
//                        CreateTextPost(Textpost.getText().toString(),spinnerView.getText().toString(),"no","no", drawable.getColor());
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
                FragmentUpload.this.bt_play.setImageResource(R.drawable.play_arrow);
            }
        });

        this.utils = new MusicUtils();
        this.bt_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FragmentUpload.this.mp.isPlaying()) {
                    FragmentUpload.this.mp.pause();
                    FragmentUpload.this.bt_play.setImageResource(R.drawable.play_arrow);
                    return;
                }
                FragmentUpload.this.mp.start();
                FragmentUpload.this.bt_play.setImageResource(R.drawable.ic_pause);
                FragmentUpload.this.mHandler.post(FragmentUpload.this.mUpdateTimeTask);
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
                    DocumentLayout.setVisibility(View.GONE);
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
                    DocumentLayout.setVisibility(View.GONE);
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
                    DocumentLayout.setVisibility(View.GONE);
                    PreviewLayout.setVisibility(View.GONE);
                    cover_layout.setVisibility(View.VISIBLE);
                }
                if(spinnerView.getText().toString().equals("Literature") || spinnerView.getText().toString().equals("Philosophy") || spinnerView.getText().toString().equals("Poetry")){
                    mp.reset();
                    videoView.stopPlayback();
                    DocumentLayout.setVisibility(View.VISIBLE);
                    VideoLayout.setVisibility(View.GONE);
                    MusicPlayerLayout.setVisibility(View.GONE);
                    ImageViewLayout.setVisibility(View.GONE);
                    //uploadBtn.setVisibility(View.GONE);
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
                else if(spinnerView.getText().toString().equals("Literature") || spinnerView.getText().toString().equals("Philosophy") || spinnerView.getText().toString().equals("Poetry")){
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select File"), DOC_REQUEST);
                }
                else{
                    Toast.makeText(getContext(), "Select valid category", Toast.LENGTH_SHORT).show();
                }


            }
        });

        CreateTags("Sheraz","Naeem","Hassan");
        return view;




    }

    private void CreateImagePost(ArrayList<Uri> mArrayUri, String category, String analytics, String nft, Uri coverUri) {

        if (checkInternetConnection(getContext())) {


                if(mArrayUri.size()>0){

                    String post_id = databaseReference.push().getKey();
                    for (int i = 0; i < mArrayUri.size(); i++) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        Uri postUri = mArrayUri.get(i);
                        Cursor cursor = getContext().getContentResolver().query(postUri, null, null, null, null);
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
                                                postMap.put("Groupid","none");
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
                                                                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setCancelable(false);
                            progressDialog.setTitle("Uploading");
                            progressDialog.show();

                            Uri postUri = mArrayUri.get(i);
                            Cursor cursor = getContext().getContentResolver().query(postUri, null, null, null, null);
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
                                                    postMap.put("Groupid","none");
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
                                                                        Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getContext(), "Please Select Valid File", Toast.LENGTH_SHORT).show();
                }
        }



    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
                FragmentUpload.this.updateTimerAndSeekbar();
                if (FragmentUpload.this.mp.isPlaying()) {
                    FragmentUpload.this.mHandler.postDelayed(this, 100);
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
                    FragmentUpload.this.mp.reset();
                    FragmentUpload.this.mp.setAudioStreamType(3);
                    //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                    FragmentUpload.this.mp.setDataSource(getContext(), musicUri);
                    //openFd.close();
                    FragmentUpload.this.mp.prepare();
                    //this.mp.start();
                } catch (Exception unused) {
                    Toast.makeText(getContext(), "Cannot load audio file", Toast.LENGTH_SHORT).show();
                }
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                musicFileName.setText(file.getName().toString());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }


        if(requestCode==IMG_REQUEST && resultCode == RESULT_OK && data != null){
            try {
                if(data.getClipData() != null) {
                    controllerL.setVisibility(View.VISIBLE);
                    mArrayUri.clear();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    //Toast.makeText(getContext(), String.valueOf(count), Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < count; i++){
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        mArrayUri.add(uri);
                        //Toast.makeText(getContext(), data.getClipData().getItemAt(i).getUri().toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

        if(requestCode==VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            try {
                videoUri = data.getData();
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                videoFileName.setText(file.getName().toString());
                MediaController mediaController= new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(videoUri);
                videoView.requestFocus();
                videoView.start();

//                File video_file = FileUtils.getFileFromUri(getContext(), uri);
//                video_url=video_file.getAbsolutePath();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("getFileFromUri",e.getMessage());
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

        if(requestCode==COVER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            try {
                CoverUri = data.getData();
                Uri uri= data.getData();
                File file= new File(uri.getPath());
                Log.e("FileName",file.getName().toString());
                cover_name.setText(uri.getPath());

//                File video_file = FileUtils.getFileFromUri(getContext(), uri);
//                video_url=video_file.getAbsolutePath();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("getFileFromUri",e.getMessage());
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
        if(requestCode==DOC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            try {
                DocUri = data.getData();
//                if(DocUri.getPath().contains(".doc")){
//                    Toast.makeText(getContext(), "doc", Toast.LENGTH_LONG).show();
//                }
//                if(DocUri.getPath().contains(".pdf")){
//                    Toast.makeText(getContext(), "pdf", Toast.LENGTH_LONG).show();
//                }
//                if(DocUri.getPath().contains(".jpeg")){
//                    Toast.makeText(getContext(), "jpg", Toast.LENGTH_LONG).show();
//                }
                File file= new File(DocUri.getPath());
                doctext.setText(file.getPath());
//                docView.fromAsset(DocUri.getPath())
//                        .defaultPage(0)
//                        .enableSwipe(true)
//                        .swipeHorizontal(false)
//                        .enableAnnotationRendering(true)
//                        .scrollHandle(new DefaultScrollHandle(getContext()))
//                        .load();

                Log.e("FileName",file.getPath().toString());

//                File video_file = FileUtils.getFileFromUri(getContext(), uri);
//                video_url=video_file.getAbsolutePath();


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("getFileFromUri",e.getMessage());
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }


    }

    private void CreatePost(Uri postUri,String Category,String analytics,String NFT,Uri CoverUri){

        if (checkInternetConnection(getContext())){
            if(postUri == null || CoverUri == null){
                Toast.makeText(getContext(), "Please Select Valid File & Cover Art", Toast.LENGTH_SHORT).show();
            }
            else{
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setCancelable(false);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    Uri uri = postUri;
                    Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
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
                                                Cursor cursor1 = getContext().getContentResolver().query(cover, null, null, null, null);
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
                                                                        postMap.put("Groupid","none");
                                                                        databaseReference.child(post_id).setValue(postMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if(task.isSuccessful()){
                                                                                            progressDialog.dismiss();
                                                                                        }
                                                                                        else{
                                                                                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                                postMap.put("Groupid","none");
                                databaseReference.child(id).setValue(postMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                }
                                                else{
                                                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

    public static boolean checkInternetConnection(Context context){
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

//    private void CompressVideo() throws Exception {
          /* startMs is the starting time, from where we have to apply the effect.
  	         endMs is the ending time, till where we have to apply effect.
   	         For example, we have a video of 5min and we only want to fast forward a part of video
  	         say, from 1:00 min to 2:00min, then our startMs will be 1000ms and endMs will be 2000ms.
		 */

        //create a progress dialog and show it until this method executes.
//        progressDialog.show();
//
//        //creating a new file in storage
//        final String filePath;
//        String filePrefix = "fastforward";
//        String fileExtn = ".mp4";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            /*
//            With introduction of scoped storage in Android Q the primitive method gives error
//            So, it is recommended to use the below method to create a video file in storage.
//             */
//            ContentValues valuesvideos = new ContentValues();
//            valuesvideos.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "Folder");
//            valuesvideos.put(MediaStore.Video.Media.TITLE, filePrefix+System.currentTimeMillis());
//            valuesvideos.put(MediaStore.Video.Media.DISPLAY_NAME, filePrefix+System.currentTimeMillis()+fileExtn);
//            valuesvideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
//            valuesvideos.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
//            valuesvideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
//            Uri uri = getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, valuesvideos);
//
//            //get the path of the video file created in the storage.
//            File file= FileUtils.getFileFromUri(getContext(),uri);
//            filePath=file.getAbsolutePath();
//
//        }else {
//            //This else statement will work for devices with Android version lower than 10
//            //Here, "app_folder" is the path to your app's root directory in device storage
//            File dest = new File(new File(app_folder), filePrefix + fileExtn);
//            int fileNo = 0;
//            //check if the file name previously exist. Since we don't want to oerwrite the video files
//            while (dest.exists()) {
//                fileNo++;
//                dest = new File(new File(app_folder), filePrefix + fileNo + fileExtn);
//            }
//            //Get the filePath once the file is successfully created.
//            filePath = dest.getAbsolutePath();
//        }
        //String exe;
        //the "exe" string contains the command to process video.The details of command are discussed later in this post.
        // "video_url" is the url of video which you want to edit. You can get this url from intent by selecting any video from gallery.
        //exe="-y -i " +video_url+" -filter_complex [0:v]trim=0:"+startMs/1000+",setpts=PTS-STARTPTS[v1];[0:v]trim="+startMs/1000+":"+endMs/1000+",setpts=0.5*(PTS-STARTPTS)[v2];[0:v]trim="+(endMs/1000)+",setpts=PTS-STARTPTS[v3];[0:a]atrim=0:"+(startMs/1000)+",asetpts=PTS-STARTPTS[a1];[0:a]atrim="+(startMs/1000)+":"+(endMs/1000)+",asetpts=PTS-STARTPTS,atempo=2[a2];[0:a]atrim="+(endMs/1000)+",asetpts=PTS-STARTPTS[a3];[v1][a1][v2][a2][v3][a3]concat=n=3:v=1:a=1 "+"-b:v 2097k -vcodec mpeg4 -crf 0 -preset superfast "+filePath;
        //exe= "-y -i +" +video_url+" -strict experimental -s 160x120 -r 25 -vcodec mpeg4 -b 150k -ab 48000 -ac 2 -ar 22050 "+filePath+"out.mp4";
        /*
            Here, we have used he Async task to execute our query because if we use the regular method the progress dialog
            won't be visible. This happens because the regular method and progress dialog uses the same thread to execute
            and as a result only one is a allowed to work at a time.
            By using we Async task we create a different thread which resolves the issue.
         */
//        try {
//            ffmpeg.execute(new String[]{"-y", "-i",video_url ,"-strict", "experimental", "-s", "160x120", "-r", "25", "-vcodec", "mpeg4", "-b", "150k", "-ab", "48000", "-ac", "2", "-ar", "22050", filePath},
//                    new ExecuteBinaryResponseHandler() {
//
//                        @Override
//                        public void onStart() {
//                            //for logcat
//                            Log.w(null,"Cut started");
//                        }
//
//                        @Override
//                        public void onProgress(String message) {
//                            //for logcat
//                            Log.w(null,message.toString());
//                        }
//
//                        @Override
//                        public void onFailure(String message) {
//
//                            Log.w(null,message.toString());
//                        }
//
//                        @Override
//                        public void onSuccess(String message) {
//
//                            Log.w(null,message.toString());
//                    //after successful execution of ffmpeg command,
//                    //again set up the video Uri in VideoView
//                    videoView.setVideoURI(Uri.parse(filePath));
//                    //change the video_url to filePath, so that we could do more manipulations in the
//                    //resultant video. By this we can apply as many effects as we want in a single video.
//                    //Actually there are multiple videos being formed in storage but while using app it
//                    //feels like we are doing manipulations in only one video
//                    video_url = filePath;
//                    //play the result video in VideoView
//                    videoView.start();
//                    //remove the progress dialog
//                    progressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onFinish() {
//
//                            Log.w(null,"Cutting video finished");
//                        }
//                    });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            // Handle if FFmpeg is already running
//            e.printStackTrace();
//            Log.e("Errorrr",e.toString());
//        }

//        long executionId = com.arthenica.mobileffmpeg.FFmpeg.executeAsync(exe, new ExecuteCallback() {
//
//            @Override
//            public void apply(final long executionId, final int returnCode) {
//                if (returnCode == RETURN_CODE_SUCCESS) {
//                    //after successful execution of ffmpeg command,
//                    //again set up the video Uri in VideoView
//                    videoView.setVideoURI(Uri.parse(filePath));
//                    //change the video_url to filePath, so that we could do more manipulations in the
//                    //resultant video. By this we can apply as many effects as we want in a single video.
//                    //Actually there are multiple videos being formed in storage but while using app it
//                    //feels like we are doing manipulations in only one video
//                    video_url = filePath;
//                    //play the result video in VideoView
//                    videoView.start();
//                    //remove the progress dialog
//                    progressDialog.dismiss();
//                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.i(Config.TAG, "Async command execution cancelled by user.");
//                    progressDialog.dismiss();
//                } else {
//                    progressDialog.dismiss();
//                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
//                }
//            }
//        });
//    }


//    public void LoadFFmpegLibrary()
//    {
//        if(ffmpeg==null)
//        {
//            ffmpeg = com.github.hiteshsondhi88.libffmpeg.FFmpeg.getInstance(getContext());
//            try {
//                ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                    @Override
//                    public void onStart() {}
//
//                    @Override
//                    public void onFailure() {
//                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFinish() {}
//                });
//            } catch (FFmpegNotSupportedException e) {
//                e.printStackTrace();
//            }
//       }
//    }


    private void CreateDocPost(Uri postUri,String Category,String analytics,String NFT,Uri CoverUri){
        if (checkInternetConnection(getContext())){
            if(postUri == null){
                Toast.makeText(getContext(), "Please Select Valid File & Cover Art", Toast.LENGTH_SHORT).show();
            }
            else{
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                Uri uri = postUri;
                Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
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

                                         String PostdataURL = uri.toString();
                                         String post_id = databaseReference.push().getKey();
                                         DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
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
                                         postMap.put("coverart","none");
                                         postMap.put("Groupid","none");
                                            databaseReference.child(post_id).setValue(postMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            progressDialog.dismiss();
                                                        }
                                                        else {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                            });


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

//    private void openFile(File url) {
//
//        try {
//
//            Uri uri = Uri.fromFile(url);
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
//                // Word document
//                intent.setDataAndType(uri, "application/msword");
//            } else if (url.toString().contains(".pdf")) {
//                // PDF file
//                intent.setDataAndType(uri, "application/pdf");
//            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
//                // Powerpoint file
//                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
//                // Excel file
//                intent.setDataAndType(uri, "application/vnd.ms-excel");
//            } else if (url.toString().contains(".zip")) {
//                // ZIP file
//                intent.setDataAndType(uri, "application/zip");
//            } else if (url.toString().contains(".rar")){
//                // RAR file
//                intent.setDataAndType(uri, "application/x-rar-compressed");
//            } else if (url.toString().contains(".rtf")) {
//                // RTF file
//                intent.setDataAndType(uri, "application/rtf");
//            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
//                // WAV audio file
//                intent.setDataAndType(uri, "audio/x-wav");
//            } else if (url.toString().contains(".gif")) {
//                // GIF file
//                intent.setDataAndType(uri, "image/gif");
//            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
//                // JPG file
//                intent.setDataAndType(uri, "image/jpeg");
//            } else if (url.toString().contains(".txt")) {
//                // Text file
//                intent.setDataAndType(uri, "text/plain");
//            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
//                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//                // Video files
//                intent.setDataAndType(uri, "video/*");
//            } else {
//                intent.setDataAndType(uri, "*/*");
//            }
//
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
//        }
//    }


}