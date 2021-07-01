package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.ImageSpecificPost;
import com.vsn.omino.activites.MusicSpecificPost;
import com.vsn.omino.activites.TextSpecificPost;
import com.vsn.omino.activites.VideoSpecificPost;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.MusicUtils;
import com.vsn.omino.utiles.UserData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NewsFeedAddapter extends RecyclerView.Adapter<NewsFeedAddapter.ViewHolder> {

    Context mContext;
    List<OmnioPosts> list;
    MediaPlayer mp;
    ProgressBar MusicTime;
    MusicUtils utils;
    Handler mHandler = new Handler();
    ListView listView;
    DatabaseReference referencee;
    SharedPreferences prefs;
    public NewsFeedAddapter(Context mContext, List<OmnioPosts> list, MediaPlayer mp) {
        this.mContext = mContext;
        this.list = list;
        this.mp = mp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_post_design , parent,false);
//        MyViewHolder holder = new MyViewHolder(view);
//        return holder;
//
//
//        Context context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//
//        // Inflate the custom layout
//        View contactView = inflater.inflate(R.layout.omnio_post_design, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(list.get(position).getCategory().equals("Art") || list.get(position).getCategory().equals("Photography") ){

            holder.likeImagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });


            holder.postCategory.setText(list.get(position).getCategory());
            holder.postColor.setText(list.get(position).getColor());
            holder.postAnalytics.setText(list.get(position).getIsanalytics());
            holder.postNft.setText(list.get(position).getIsnft());
            holder.postTags.setText(list.get(position).getTags());
            holder.postuserID.setText(list.get(position).getUserid());
            holder.postuserMail.setText(list.get(position).getUsermail());
            holder.postID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            holder.posterName.setText(list.get(position).getUsernamee());
            holder.posterDate.setText(list.get(position).getDatetimepost());
            holder.posterCaption.setText(list.get(position).getCaption());
            Picasso.get().load(list.get(position).getDataURL()).into(holder.posterImageUrl);
            Load_react_colors(holder.likeImagePost,holder.postuserID.getText().toString());
            Load_UserPics_Name(holder.posterImage,holder.posterName,holder.postuserID.getText().toString());
            holder.posterImageUrl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    addEngagementClicks(position);

                    Intent intent = new Intent(mContext, ImageSpecificPost.class);
                    intent.putExtra("userid",list.get(position).getUserid());
                    intent.putExtra("postid",list.get(position).getPostid());
                    intent.putExtra("caption",list.get(position).getCaption());
                    intent.putExtra("uri",list.get(position).getDataURL());
                    mContext.startActivity(intent);
                }
            });





            holder.ImageViewPost.setVisibility(View.VISIBLE);
            holder.VideoViewPost.setVisibility(View.GONE);
            holder.MusicViewPost.setVisibility(View.GONE);
            holder.TextViewPost.setVisibility(View.GONE);


        }
        if(list.get(position).getCategory().equals("Animation") || list.get(position).getCategory().equals("Dance") || list.get(position).getCategory().equals("Theater") ){


            holder.likeVideoPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });



            holder.VideoPostCategory.setText(list.get(position).getCategory());
            holder.VideoPostColor.setText(list.get(position).getColor());
            holder.VideoPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.VideoPostNft.setText(list.get(position).getIsnft());
            holder.VideoPostTags.setText(list.get(position).getTags());
            holder.VideoPostuserID.setText(list.get(position).getUserid());
            holder.VideoPostuserMail.setText(list.get(position).getUsermail());
            holder.VideoPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            holder.VideoPosterName.setText(list.get(position).getUsernamee());
            holder.VideoPosterDate.setText(list.get(position).getDatetimepost());
            holder.VideoPosterCaption.setText(list.get(position).getCaption());
//            Uri uri= Uri.parse(list.get(position).getDataURL());
//            MediaController mediaController= new MediaController(mContext);
//            mediaController.setAnchorView(holder.videoURL);
            Picasso.get().load(list.get(position).getCoverart()).into(holder.videoURL);
//            holder.videoURL.setVideoURI(uri);
            Load_react_colors(holder.likeVideoPost,holder.VideoPostuserID.getText().toString());
            Load_UserPics_Name(holder.VideoPosterImage,holder.VideoPosterName,holder.VideoPostuserID.getText().toString());
            holder.videoPlaybtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addEngagementClicks(position);

                    Intent intent = new Intent(mContext, VideoSpecificPost.class);
                    intent.putExtra("userid",list.get(position).getUserid());
                    intent.putExtra("postid",list.get(position).getPostid());
                    intent.putExtra("caption",list.get(position).getCaption());
                    intent.putExtra("uri",list.get(position).getDataURL());
                    intent.putExtra("cover",list.get(position).getCoverart());
                    mContext.startActivity(intent);
                }
            });

            holder.VideoViewPost.setVisibility(View.VISIBLE);

            holder.ImageViewPost.setVisibility(View.GONE);
            holder.MusicViewPost.setVisibility(View.GONE);
            holder.TextViewPost.setVisibility(View.GONE);
        }


        if(list.get(position).getCategory().equals("Music")){




            holder.likeMusicPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });

            holder.MusicPostCategory.setText(list.get(position).getCategory());
            holder.MusicPostColor.setText(list.get(position).getColor());
            holder.MusicPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.MusicPostNft.setText(list.get(position).getIsnft());
            holder.MusicPostTags.setText(list.get(position).getTags());
            holder.MusicPostuserID.setText(list.get(position).getUserid());
            holder.MusicPostuserMail.setText(list.get(position).getUsermail());
            holder.MusicPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            holder.MusicPosterName.setText(list.get(position).getUsernamee());
            holder.MusicPosterDate.setText(list.get(position).getDatetimepost());
            holder. MusicPosterCaption.setText(list.get(position).getCaption());
            //Uri uri= Uri.parse(list.get(position).getDataURL());
            Picasso.get().load(list.get(position).getCoverart()).into(holder.MusicCover);
            Load_react_colors(holder.likeMusicPost,holder.MusicPostuserID.getText().toString());
            Load_UserPics_Name(holder.MusicPosterImage,holder.MusicPosterName,holder.MusicPostuserID.getText().toString());

//            try {
//                MusicTime.setProgress(0);
//                mp.reset();
//                mp.setAudioStreamType(3);
//                //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
//                mp.setDataSource(mContext, uri);
//                //openFd.close();
//                mp.prepare();
//                //this.mp.start();
//            } catch (Exception unused) {
//                Toast.makeText(mContext, "Cannot load audio file", Toast.LENGTH_SHORT).show();
//            }


            //======================================  MUSIC
//            MusicTime.setProgress(0);
//            MusicTime.setMax(10000);
//            utils = new MusicUtils();
//            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    holder.playBtn.setImageResource(R.drawable.play_arrow);
//                }
//            });

            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    if (mp.isPlaying()) {
//                        mp.pause();
//                        holder.playBtn.setImageResource(R.drawable.play_arrow);
//                        return;
//                    }
//                    mp.start();
//                    holder.playBtn.setImageResource(R.drawable.ic_pause);
//                    mHandler.post(mUpdateTimeTask);
                    //Toast.makeText(mContext, "Play "+position, Toast.LENGTH_SHORT).show();
                    //UserData userData = new UserData();
                    //ArrayList<String> data = userData.getProfileData(list.get(position).getUserid());
                    addEngagementClicks(position);

                    Intent intent = new Intent(mContext,MusicSpecificPost.class);
                    intent.putExtra("userid",list.get(position).getUserid());
                    intent.putExtra("postid",list.get(position).getPostid());
                    intent.putExtra("caption",list.get(position).getCaption());
                    intent.putExtra("uri",list.get(position).getDataURL());
                    intent.putExtra("cover",list.get(position).getCoverart());
                    mContext.startActivity(intent);
                }
            });
            //======================================  MUSIC


            holder.MusicViewPost.setVisibility(View.VISIBLE);


            holder.ImageViewPost.setVisibility(View.GONE);
            holder.VideoViewPost.setVisibility(View.GONE);
            holder.TextViewPost.setVisibility(View.GONE);
        }

        if(list.get(position).getCategory().equals("Literature") || list.get(position).getCategory().equals("Philosophy") || list.get(position).getCategory().equals("Poetry"))
        {

            holder.TextPostCategory.setText(list.get(position).getCategory());
            //holder.TextPostCover.setBackgroundColor(Color.parseColor(list.get(position).getColor()));
            holder.TextPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.TextPostNft.setText(list.get(position).getIsnft());
            holder.TextPostTags.setText(list.get(position).getTags());
            holder. TextPostuserID.setText(list.get(position).getUserid());
            holder.TextPostuserMail.setText(list.get(position).getUsermail());
            holder.TextPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            holder.TextPosterName.setText(list.get(position).getUsernamee());
            holder.TextPosterDate.setText(list.get(position).getDatetimepost());
            holder.TextPosterCaption.setText(list.get(position).getCaption());

            String newUA= "Chrome/43.0.2357.65 ";
            holder.TextPostView.getSettings().setUserAgentString(newUA);
            holder.TextPostView.getSettings().setJavaScriptEnabled(true);
            holder.TextPostView.getSettings().setBuiltInZoomControls(true);
            holder.TextPostView.getSettings().setSupportZoom(true);
            holder.TextPostView.getSettings().setDisplayZoomControls(true);
            holder.TextPostView.getSettings().setLoadWithOverviewMode(true);
            holder.TextPostView.getSettings().setUseWideViewPort(true);
            //webView.loadUrl("https://firebasestorage.googleapis.com/v0/b/instaclone-94522.appspot.com/o/PostData%2FT2E3kjnHFzbIwiBOuscSIzmUGWI3%2F1623429531910Brief.docx?alt=media&token=a5679286-53eb-4d4e-aa7c-ac8cebdcd663");

            holder.TextPostView.getSettings().setJavaScriptEnabled(true);

            holder.TextPostView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    //pDialog.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //pDialog.dismiss();
                }
            });
            String url="";
            String doc = list.get(position).getDataURL();
            try {
                url= URLEncoder.encode(doc,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.TextPostView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
            Load_react_colors(holder.likeTextPost,holder.TextPostuserID.getText().toString());
            Load_UserPics_Name(holder.TextPosterImage,holder.TextPosterName,holder.TextPostuserID.getText().toString());

            holder.TextPostView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    addEngagementClicks(position);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent intent = new Intent(mContext, TextSpecificPost.class);
                        intent.putExtra("userid",list.get(position).getUserid());
                        intent.putExtra("postid",list.get(position).getPostid());
                        intent.putExtra("caption",list.get(position).getCaption());
                        intent.putExtra("uri",list.get(position).getDataURL());
                        intent.putExtra("color",list.get(position).getColor());
                        mContext.startActivity(intent);
                    }
                    return false;
                }
            });
//            holder.TextPostView.setOnContextClickListener(new View.OnContextClickListener() {
//                @Override
//                public boolean onContextClick(View v) {
//                    addEngagementClicks(position);
//
//                    Intent intent = new Intent(mContext, TextSpecificPost.class);
//                    intent.putExtra("userid",list.get(position).getUserid());
//                    intent.putExtra("postid",list.get(position).getPostid());
//                    intent.putExtra("caption",list.get(position).getCaption());
//                    intent.putExtra("uri",list.get(position).getDataURL());
//                    intent.putExtra("color",list.get(position).getColor());
//                    mContext.startActivity(intent);
//                    return false;
//                }
//            });

//            holder.TextPostView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addEngagementClicks(position);
//
//                    Intent intent = new Intent(mContext, TextSpecificPost.class);
//                    intent.putExtra("userid",list.get(position).getUserid());
//                    intent.putExtra("postid",list.get(position).getPostid());
//                    intent.putExtra("caption",list.get(position).getCaption());
//                    intent.putExtra("uri",list.get(position).getDataURL());
//                    intent.putExtra("color",list.get(position).getColor());
//                    mContext.startActivity(intent);
//                }
//            });


            holder.TextViewPost.setVisibility(View.VISIBLE);
            holder.ImageViewPost.setVisibility(View.GONE);
            holder.VideoViewPost.setVisibility(View.GONE);
            holder.MusicViewPost.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

            public void addEngagementView(int position) {
                prefs = mContext.getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
            String currentDateAndTime = sdf.format(new Date());
            referencee = FirebaseDatabase.getInstance().getReference();
            referencee.child("PostEngagements").child("Views").child(list.get(position).getPostid()).child(prefs.getString("userID", null).toString()).setValue(currentDateAndTime);

    }
    public void addEngagementClicks(int position) {
        prefs = mContext.getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        referencee = FirebaseDatabase.getInstance().getReference();
        referencee.child("PostEngagements").child("Clicks").child(list.get(position).getPostid()).child(prefs.getString("userID", null).toString()).setValue(currentDateAndTime);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView ImageViewPost;
        TextView postCategory;
        TextView postColor;
        TextView postAnalytics;
        TextView postNft ;
        TextView postTags;
        TextView postuserID;
        TextView postuserMail;
        TextView postID;
        ImageView posterImage;
        TextView posterName;
        TextView posterDate;
        TextView posterCaption;
        ImageView posterImageUrl;
        ImageView likeImagePost;
        TextView groupID;




        CardView VideoViewPost ;
        TextView VideoPostCategory;
        TextView VideoPostColor;
        TextView VideoPostAnalytics;
        TextView VideoPostNft;
        TextView VideoPostTags;
        TextView VideoPostuserID;
        TextView VideoPostuserMail;
        TextView VideoPostID ;
        ImageView VideoPosterImage;
        TextView VideoPosterName;
        TextView VideoPosterDate;
        TextView VideoPosterCaption;
        ImageView videoURL;
        ImageView videoPlaybtn;
        ImageView likeVideoPost;
        TextView VideogroupID;

        CardView MusicViewPost ;
        TextView MusicPostCategory;
        TextView MusicPostColor;
        TextView MusicPostAnalytics;
        TextView MusicPostNft;
        TextView MusicPostTags;
        TextView MusicPostuserID;
        TextView MusicPostuserMail;
        TextView MusicPostID ;
        ImageView MusicPosterImage;
        TextView MusicPosterName;
        TextView MusicPosterDate;
        TextView MusicPosterCaption;
        FloatingActionButton playBtn;
        ImageView MusicCover;
        ImageView likeMusicPost;
        TextView MusicgroupID;

        CardView TextViewPost ;
        TextView TextPostCategory;
        TextView TextPostColor;
        TextView TextPostAnalytics;
        TextView TextPostNft;
        TextView TextPostTags;
        TextView TextPostuserID;
        TextView TextPostuserMail;
        TextView TextPostID ;
        ImageView TextPosterImage;
        TextView TextPosterName;
        TextView TextPosterDate;
        TextView TextPosterCaption;
        WebView TextPostView;
        ImageView likeTextPost;
        //ImageView TextPostCover;
        TextView TextgroupID;

        public ViewHolder(View itemView) {
            super(itemView);

             ImageViewPost = (CardView)itemView.findViewById(R.id.ImageViewPost);
             postCategory = (TextView)itemView.findViewById(R.id.postCategory);
             postColor = (TextView)itemView.findViewById(R.id.postColor);
             postAnalytics= (TextView)itemView.findViewById(R.id.postAnalytics);
             postNft =(TextView)itemView.findViewById(R.id.postNft);
             postTags =(TextView)itemView.findViewById(R.id.postTags);
             postuserID= (TextView)itemView.findViewById(R.id.postuserID);
             postuserMail =(TextView)itemView.findViewById(R.id.postuserMail);
             postID =(TextView)itemView.findViewById(R.id.postID);
             posterImage = (ImageView)itemView.findViewById(R.id.posterImage);
             posterName =(TextView)itemView.findViewById(R.id.posterName);
             posterDate =(TextView)itemView.findViewById(R.id.posterDate);
             posterCaption =(TextView)itemView.findViewById(R.id.posterCaption);
             posterImageUrl = (ImageView)itemView.findViewById(R.id.posterImageUrl);
             likeImagePost = (ImageView)itemView.findViewById(R.id.likeicon);
             groupID = (TextView)itemView.findViewById(R.id.groupID);

             VideoViewPost = (CardView)itemView.findViewById(R.id.VideoViewPost);
             VideoPostCategory = (TextView)itemView.findViewById(R.id.VideoPostCategory);
             VideoPostColor = (TextView)itemView.findViewById(R.id.VideoPostColor);
             VideoPostAnalytics= (TextView)itemView.findViewById(R.id.VideoPostAnalytics);
             VideoPostNft =(TextView)itemView.findViewById(R.id.VideoPostNft);
             VideoPostTags =(TextView)itemView.findViewById(R.id.VideoPostTags);
             VideoPostuserID= (TextView)itemView.findViewById(R.id.VideoPostuserID);
             VideoPostuserMail =(TextView)itemView.findViewById(R.id.VideoPostuserMail);
             VideoPostID =(TextView)itemView.findViewById(R.id.VideoPostID);
             VideoPosterImage = (ImageView)itemView.findViewById(R.id.VideoPosterImage);
             VideoPosterName =(TextView)itemView.findViewById(R.id.VideoPosterName);
             VideoPosterDate =(TextView)itemView.findViewById(R.id.VideoPosterDate);
             VideoPosterCaption =(TextView)itemView.findViewById(R.id.VideoPosterCaption);
             videoURL = (ImageView) itemView.findViewById(R.id.videoURL);
             videoPlaybtn= (ImageView) itemView.findViewById(R.id.videoPlayButton);
             likeVideoPost = (ImageView)itemView.findViewById(R.id.VideoPostlikeicon);
             VideogroupID = (TextView)itemView.findViewById(R.id.VideogroupID);



             TextViewPost = (CardView)itemView.findViewById(R.id.TextViewPost);
             TextPostCategory = (TextView)itemView.findViewById(R.id.TextPostCategory);
             TextPostColor = (TextView)itemView.findViewById(R.id.TextPostColor);
             TextPostAnalytics= (TextView)itemView.findViewById(R.id.TextPostAnalytics);
             TextPostNft =(TextView)itemView.findViewById(R.id.TextPostNft);
             TextPostTags =(TextView)itemView.findViewById(R.id.TextPostTags);
             TextPostuserID= (TextView)itemView.findViewById(R.id.TextPostuserID);
             TextPostuserMail =(TextView)itemView.findViewById(R.id.TextPostuserMail);
             TextPostID =(TextView)itemView.findViewById(R.id.TextPostID);
             TextPosterImage = (ImageView)itemView.findViewById(R.id.TextPosterImage);
             TextPosterName =(TextView)itemView.findViewById(R.id.TextPosterName);
             TextPosterDate =(TextView)itemView.findViewById(R.id.TextPosterDate);
             TextPosterCaption =(TextView)itemView.findViewById(R.id.TextPosterCaption);
             TextPostView = (WebView) itemView.findViewById(R.id.TextPostView);
             //TextPostCover = (ImageView)itemView.findViewById(R.id.TextPostCover);
             likeTextPost = (ImageView)itemView.findViewById(R.id.TextPostlikeicon);
             TextgroupID = (TextView)itemView.findViewById(R.id.TextgroupID);


             MusicViewPost = (CardView)itemView.findViewById(R.id.MusicViewPost);
             MusicPostCategory = (TextView)itemView.findViewById(R.id.MusicPostCategory);
             MusicPostColor = (TextView)itemView.findViewById(R.id.MusicPostColor);
             MusicPostAnalytics= (TextView)itemView.findViewById(R.id.MusicPostAnalytics);
             MusicPostNft =(TextView)itemView.findViewById(R.id.MusicPostNft);
             MusicPostTags =(TextView)itemView.findViewById(R.id.MusicPostTags);
             MusicPostuserID= (TextView)itemView.findViewById(R.id.MusicPostuserID);
             MusicPostuserMail =(TextView)itemView.findViewById(R.id.MusicPostuserMail);
             MusicPostID =(TextView)itemView.findViewById(R.id.MusicPostID);
             MusicPosterImage = (ImageView)itemView.findViewById(R.id.MusicPosterImage);
             MusicPosterName =(TextView)itemView.findViewById(R.id.MusicPosterName);
             MusicPosterDate =(TextView)itemView.findViewById(R.id.MusicPosterDate);
             MusicPosterCaption =(TextView)itemView.findViewById(R.id.MusicPosterCaption);
             playBtn = (FloatingActionButton)itemView.findViewById(R.id.MusicPost_play);
             MusicTime = (ProgressBar)itemView.findViewById(R.id.Music_progressbar);
             MusicCover = (ImageView)itemView.findViewById(R.id.MusicCover);
             likeMusicPost = (ImageView)itemView.findViewById(R.id.MusicPostlikeicon);
             MusicgroupID = (TextView)itemView.findViewById(R.id.MusicgroupID);
        }
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
        long currentPosition = (long) mp.getCurrentPosition();
        MusicTime.setProgress(utils.getProgressSeekBar(currentPosition, (long) this.mp.getDuration()));

    }

    private void  stopMusic(MediaPlayer player){
        AudioManager.OnAudioFocusChangeListener focusChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        AudioManager am = (AudioManager)
                                mContext.getSystemService(AUDIO_SERVICE);
                        switch (focusChange) {

                            case
                                    (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                                // Lower the volume while ducking.
                                player.setVolume(0.2f, 0.2f);
                                break;
                            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                                player.pause();
                                break;

                            case (AudioManager.AUDIOFOCUS_LOSS):
                                player.stop();
                                break;

                            case (AudioManager.AUDIOFOCUS_GAIN):

                                player.setVolume(1f, 1f);

                                break;
                            default:
                                break;
                        }
                    }
                };

        AudioManager am = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);

// Request audio focus for playback
        int result = am.requestAudioFocus(focusChangeListener,
// Use the music stream.
                AudioManager.STREAM_MUSIC,
// Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //player.setSource(video);
        }
    }


    public void Load_react_colors(ImageView likeIcon,String PosterID){

        String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences prefs;
        prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(PosterID)){
                        if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){
                            likeIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void Load_UserPics_Name(ImageView profilePic,TextView username,String posterid){

        String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences prefs;
        prefs = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserAccountSettings").child(posterid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgLink = snapshot.child("profile_photo").getValue().toString();
                String usern = snapshot.child("username").getValue().toString();
                Picasso.get().load(imgLink).into(profilePic);
                username.setText(usern);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getPostID(int position){
        return list.get(position).getPostid();
    }

}
