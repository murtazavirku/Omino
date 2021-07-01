package com.vsn.omino.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vsn.omino.FragmentUpload;
import com.vsn.omino.R;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.MusicUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.AUDIO_SERVICE;

public class NewsfeedAdapter extends BaseAdapter {

    Context mContext;
    List<OmnioPosts> list;
    MediaPlayer mp;
    ProgressBar MusicTime;
    MusicUtils utils;
    Handler mHandler = new Handler();
    ListView listView;
    public NewsfeedAdapter(Context mContext, List<OmnioPosts> list,ListView listView,MediaPlayer mp) {
        this.mContext = mContext;
        this.list = list;
        this.listView = listView;
        this.mp = mp;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getUserid();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View v = View.inflate(mContext, R.layout.omnio_post_design,null);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    //mp.reset();
                    Log.e("SCROLLING DOWN",">>>");
                    if(mp.isPlaying()){
                        stopMusic(mp);
                    }



                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    //mp.reset();
                    Log.e("SCROLLING UP","<<<");
                    if(mp.isPlaying()){
                        stopMusic(mp);
                    }

                }
                mLastFirstVisibleItem=firstVisibleItem;
            }
        });




        if(list.get(position).getCategory().equals("Art") || list.get(position).getCategory().equals("Photography") ){

            // image post components
            CardView ImageViewPost = (CardView)v.findViewById(R.id.ImageViewPost);
            TextView postCategory = (TextView)v.findViewById(R.id.postCategory);
            TextView postColor = (TextView)v.findViewById(R.id.postColor);
            TextView postAnalytics= (TextView)v.findViewById(R.id.postAnalytics);
            TextView postNft =(TextView)v.findViewById(R.id.postNft);
            TextView postTags =(TextView)v.findViewById(R.id.postTags);
            TextView postuserID= (TextView)v.findViewById(R.id.postuserID);
            TextView postuserMail =(TextView)v.findViewById(R.id.postuserMail);
            TextView postID =(TextView)v.findViewById(R.id.postID);
            ImageView posterImage = (ImageView)v.findViewById(R.id.posterImage);
            TextView posterName =(TextView)v.findViewById(R.id.posterName);
            TextView posterDate =(TextView)v.findViewById(R.id.posterDate);
            TextView posterCaption =(TextView)v.findViewById(R.id.posterCaption);
            ImageView posterImageUrl = (ImageView)v.findViewById(R.id.posterImageUrl);
            ImageView likeImagePost = (ImageView)v.findViewById(R.id.likeicon);
            likeImagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });


            postCategory.setText(list.get(position).getCategory());
            postColor.setText(list.get(position).getColor());
            postAnalytics.setText(list.get(position).getIsanalytics());
            postNft.setText(list.get(position).getIsnft());
            postTags.setText(list.get(position).getTags());
            postuserID.setText(list.get(position).getUserid());
            postuserMail.setText(list.get(position).getUsermail());
            postID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            posterName.setText(list.get(position).getUsernamee());
            posterDate.setText(list.get(position).getDatetimepost());
            posterCaption.setText(list.get(position).getCaption());
            Picasso.get().load(list.get(position).getDataURL()).into(posterImageUrl);



            ImageViewPost.setVisibility(View.VISIBLE);


        }
        if(list.get(position).getCategory().equals("Animation") || list.get(position).getCategory().equals("Dance") || list.get(position).getCategory().equals("Theater") ){

            // video post components
            CardView VideoViewPost = (CardView)v.findViewById(R.id.VideoViewPost);
            TextView VideoPostCategory = (TextView)v.findViewById(R.id.VideoPostCategory);
            TextView VideoPostColor = (TextView)v.findViewById(R.id.VideoPostColor);
            TextView VideoPostAnalytics= (TextView)v.findViewById(R.id.VideoPostAnalytics);
            TextView VideoPostNft =(TextView)v.findViewById(R.id.VideoPostNft);
            TextView VideoPostTags =(TextView)v.findViewById(R.id.VideoPostTags);
            TextView VideoPostuserID= (TextView)v.findViewById(R.id.VideoPostuserID);
            TextView VideoPostuserMail =(TextView)v.findViewById(R.id.VideoPostuserMail);
            TextView VideoPostID =(TextView)v.findViewById(R.id.VideoPostID);
            ImageView VideoPosterImage = (ImageView)v.findViewById(R.id.VideoPosterImage);
            TextView VideoPosterName =(TextView)v.findViewById(R.id.VideoPosterName);
            TextView VideoPosterDate =(TextView)v.findViewById(R.id.VideoPosterDate);
            TextView VideoPosterCaption =(TextView)v.findViewById(R.id.VideoPosterCaption);
            //VideoView videoURL = (VideoView)v.findViewById(R.id.videoURL);
            ImageView likeVideoPost = (ImageView)v.findViewById(R.id.VideoPostlikeicon);

            likeVideoPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });

            VideoPostCategory.setText(list.get(position).getCategory());
            VideoPostColor.setText(list.get(position).getColor());
            VideoPostAnalytics.setText(list.get(position).getIsanalytics());
            VideoPostNft.setText(list.get(position).getIsnft());
            VideoPostTags.setText(list.get(position).getTags());
            VideoPostuserID.setText(list.get(position).getUserid());
            VideoPostuserMail.setText(list.get(position).getUsermail());
            VideoPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            VideoPosterName.setText(list.get(position).getUsernamee());
            VideoPosterDate.setText(list.get(position).getDatetimepost());
            VideoPosterCaption.setText(list.get(position).getCaption());
            Uri uri= Uri.parse(list.get(position).getDataURL());
//            MediaController mediaController= new MediaController(mContext);
//            mediaController.setAnchorView(videoURL);
//            videoURL.setMediaController(mediaController);
//            videoURL.setVideoURI(uri);


            VideoViewPost.setVisibility(View.VISIBLE);
        }


        if(list.get(position).getCategory().equals("Music")){

            // Music post components
            CardView MusicViewPost = (CardView)v.findViewById(R.id.MusicViewPost);
            TextView MusicPostCategory = (TextView)v.findViewById(R.id.MusicPostCategory);
            TextView MusicPostColor = (TextView)v.findViewById(R.id.MusicPostColor);
            TextView MusicPostAnalytics= (TextView)v.findViewById(R.id.MusicPostAnalytics);
            TextView MusicPostNft =(TextView)v.findViewById(R.id.MusicPostNft);
            TextView MusicPostTags =(TextView)v.findViewById(R.id.MusicPostTags);
            TextView MusicPostuserID= (TextView)v.findViewById(R.id.MusicPostuserID);
            TextView MusicPostuserMail =(TextView)v.findViewById(R.id.MusicPostuserMail);
            TextView MusicPostID =(TextView)v.findViewById(R.id.MusicPostID);
            ImageView MusicPosterImage = (ImageView)v.findViewById(R.id.MusicPosterImage);
            TextView MusicPosterName =(TextView)v.findViewById(R.id.MusicPosterName);
            TextView MusicPosterDate =(TextView)v.findViewById(R.id.MusicPosterDate);
            TextView MusicPosterCaption =(TextView)v.findViewById(R.id.MusicPosterCaption);

            FloatingActionButton playBtn = (FloatingActionButton)v.findViewById(R.id.MusicPost_play);
            MusicTime = (ProgressBar)v.findViewById(R.id.Music_progressbar);

            ImageView likeMusicPost = (ImageView)v.findViewById(R.id.MusicPostlikeicon);

            likeMusicPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Clicked "+position, Toast.LENGTH_SHORT).show();
                }
            });

            MusicPostCategory.setText(list.get(position).getCategory());
            MusicPostColor.setText(list.get(position).getColor());
            MusicPostAnalytics.setText(list.get(position).getIsanalytics());
            MusicPostNft.setText(list.get(position).getIsnft());
            MusicPostTags.setText(list.get(position).getTags());
            MusicPostuserID.setText(list.get(position).getUserid());
            MusicPostuserMail.setText(list.get(position).getUsermail());
            MusicPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            MusicPosterName.setText(list.get(position).getUsernamee());
            MusicPosterDate.setText(list.get(position).getDatetimepost());
            MusicPosterCaption.setText(list.get(position).getCaption());
            Uri uri= Uri.parse(list.get(position).getDataURL());

            try {
                MusicTime.setProgress(0);
                mp.reset();
                mp.setAudioStreamType(3);
                //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                mp.setDataSource(mContext, uri);
                //openFd.close();
                mp.prepare();
                //this.mp.start();
            } catch (Exception unused) {
                Toast.makeText(mContext, "Cannot load audio file", Toast.LENGTH_SHORT).show();
            }


            //======================================  MUSIC
            MusicTime.setProgress(0);
            MusicTime.setMax(10000);
            utils = new MusicUtils();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playBtn.setImageResource(R.drawable.play_arrow);
                }
            });

            playBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mp.isPlaying()) {
                        mp.pause();
                        playBtn.setImageResource(R.drawable.play_arrow);
                        return;
                    }
                    mp.start();
                    playBtn.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);
                }
            });
            //======================================  MUSIC


            MusicViewPost.setVisibility(View.VISIBLE);
        }

        if(list.get(position).getCategory().equals("Literature") || list.get(position).getCategory().equals("Philosophy") || list.get(position).getCategory().equals("Poetry"))
        {
            // Text post components
            CardView TextViewPost = (CardView)v.findViewById(R.id.TextViewPost);
            TextView TextPostCategory = (TextView)v.findViewById(R.id.TextPostCategory);
            TextView TextPostColor = (TextView)v.findViewById(R.id.TextPostColor);
            TextView TextPostAnalytics= (TextView)v.findViewById(R.id.TextPostAnalytics);
            TextView TextPostNft =(TextView)v.findViewById(R.id.TextPostNft);
            TextView TextPostTags =(TextView)v.findViewById(R.id.TextPostTags);
            TextView TextPostuserID= (TextView)v.findViewById(R.id.TextPostuserID);
            TextView TextPostuserMail =(TextView)v.findViewById(R.id.TextPostuserMail);
            TextView TextPostID =(TextView)v.findViewById(R.id.TextPostID);
            ImageView TextPosterImage = (ImageView)v.findViewById(R.id.TextPosterImage);
            TextView TextPosterName =(TextView)v.findViewById(R.id.TextPosterName);
            TextView TextPosterDate =(TextView)v.findViewById(R.id.TextPosterDate);
            TextView TextPosterCaption =(TextView)v.findViewById(R.id.TextPosterCaption);

            TextView TextPostText = (TextView)v.findViewById(R.id.TextPostText);
            ImageView TextPostCover = (ImageView)v.findViewById(R.id.TextPostCover);

            ImageView likeTextPost = (ImageView)v.findViewById(R.id.TextPostlikeicon);

            TextPostCategory.setText(list.get(position).getCategory());
            TextPostCover.setBackgroundColor(Color.parseColor(list.get(position).getColor()));
            TextPostAnalytics.setText(list.get(position).getIsanalytics());
            TextPostNft.setText(list.get(position).getIsnft());
            TextPostTags.setText(list.get(position).getTags());
            TextPostuserID.setText(list.get(position).getUserid());
            TextPostuserMail.setText(list.get(position).getUsermail());
            TextPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
            TextPosterName.setText(list.get(position).getUsernamee());
            TextPosterDate.setText(list.get(position).getDatetimepost());
            TextPosterCaption.setText(list.get(position).getCaption());
            TextPostText.setText(list.get(position).getDataURL());


            TextViewPost.setVisibility(View.VISIBLE);
        }








            return v;

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




}




