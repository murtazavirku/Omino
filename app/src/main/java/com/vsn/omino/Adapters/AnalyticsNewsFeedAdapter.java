package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.FragmentAnalytics;
import com.vsn.omino.R;
import com.vsn.omino.activites.ImageSpecificPost;
import com.vsn.omino.activites.MusicSpecificPost;
import com.vsn.omino.activites.TextSpecificPost;
import com.vsn.omino.activites.VideoSpecificPost;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.MusicUtils;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.AUDIO_SERVICE;

public class AnalyticsNewsFeedAdapter  extends RecyclerView.Adapter<AnalyticsNewsFeedAdapter.ViewHolder> {

    Context mContext;
    List<OmnioPosts> list;
    MediaPlayer mp;
    ProgressBar MusicTime;
    MusicUtils utils;
    Handler mHandler = new Handler();
    ListView listView;
    DatabaseReference referencee;
    int lastClickedPosition;

    public AnalyticsNewsFeedAdapter(Context mContext, List<OmnioPosts> list, MediaPlayer mp) {
        this.mContext = mContext;
        this.list = list;
        this.mp = mp;
    }

    @NonNull
    @Override
    public AnalyticsNewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_analytics_post_design , parent,false);
        AnalyticsNewsFeedAdapter.ViewHolder viewHolder = new AnalyticsNewsFeedAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull AnalyticsNewsFeedAdapter.ViewHolder holder, int position) {
        if(list.get(position).getCategory().equals("Art") || list.get(position).getCategory().equals("Photography") ){


            holder.ll1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentAnalytics.tIL1.getEditText().setText(list.get(position).getCaption());
                    FragmentAnalytics.tIL2.getEditText().setText(list.get(position).getCategory());
                    FragmentAnalytics.selectedPostId = list.get(position).getPostid();
                    //holder.ll1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
                   // notifyItemChanged(lastClickedPosition);
                   // lastClickedPosition = position;
                    Snackbar.make(v, "Selected: "+list.get(position).getCaption(), Snackbar.LENGTH_LONG)
                            .setAction("Clear", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentAnalytics.selectedPostId = "";
                                }
                            })
                            .setActionTextColor(mContext.getResources().getColor(android.R.color.white ))
                            .setBackgroundTint(mContext.getResources().getColor(android.R.color.background_dark ))
                            .show();
                    //   nestedScrollView.scrollTo(0, 0);
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                       FragmentAnalytics.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                };
                    FragmentAnalytics.nestedScrollView.post(runnable);
//                //nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });
//            if(lastClickedPosition==position){
//                holder.ll1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
//
//            }
//            else
//            {
//                holder.ll1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//
//            }

            // holder.postCategory.setText(list.get(position).getCategory());
            //holder.postColor.setText(list.get(position).getColor());
            holder.postAnalytics.setText(list.get(position).getIsanalytics());
            holder.postNft.setText(list.get(position).getIsnft());
            holder.postTags.setText(list.get(position).getTags());
            holder.postuserID.setText(list.get(position).getUserid());
            holder.postuserMail.setText(list.get(position).getUsermail());
            holder.postID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
           // holder.posterName.setText(list.get(position).getUsernamee());
            //holder.posterDate.setText(list.get(position).getDatetimepost());
            holder.posterCaption.setText(list.get(position).getCaption());
            Picasso.get().load(list.get(position).getDataURL()).into(holder.posterImageUrl);
            holder.posterImageUrl.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                   // addEngagementClicks(position);

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




            holder.ll2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentAnalytics.tIL1.getEditText().setText(list.get(position).getCaption());
                    FragmentAnalytics.tIL2.getEditText().setText(list.get(position).getCategory());
                    FragmentAnalytics.selectedPostId = list.get(position).getPostid();
                    Snackbar.make(v, "Selected: "+list.get(position).getCaption(), Snackbar.LENGTH_LONG)
                            .setAction("Clear", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentAnalytics.selectedPostId = "";
                                }
                            })
                            .setActionTextColor(mContext.getResources().getColor(android.R.color.white ))
                            .setBackgroundTint(mContext.getResources().getColor(android.R.color.background_dark ))
                            .show();
                    //  holder.ll2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
                  //  lastClickedPosition = position;
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            FragmentAnalytics.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    };
                    FragmentAnalytics.nestedScrollView.post(runnable);
                }
            });
//
//            if(lastClickedPosition==position){
//                holder.ll2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
//
//            }
//            else
//            {
//                holder.ll2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//
//            }
            holder.VideoPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.VideoPostNft.setText(list.get(position).getIsnft());
            holder.VideoPostTags.setText(list.get(position).getTags());
            holder.VideoPostuserID.setText(list.get(position).getUserid());
            holder.VideoPostuserMail.setText(list.get(position).getUsermail());
            holder.VideoPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
//            holder.VideoPosterName.setText(list.get(position).getUsernamee());
 //           holder.VideoPosterDate.setText(list.get(position).getDatetimepost());
 //           holder.VideoPosterCaption.setText(list.get(position).getCaption());
            Picasso.get().load(list.get(position).getCoverart()).into(holder.videoURL);
//            holder.videoURL.setVideoURI(uri);
            holder.videoPlaybtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                   // addEngagementClicks(position);

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




            holder.ll3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Selected: "+list.get(position).getCaption(), Toast.LENGTH_SHORT).show();
                    FragmentAnalytics.tIL1.getEditText().setText(list.get(position).getCaption());
                    FragmentAnalytics.tIL2.getEditText().setText(list.get(position).getCategory());
                    FragmentAnalytics.selectedPostId = list.get(position).getPostid();
                    Snackbar.make(v, "Selected: "+list.get(position).getCaption(), Snackbar.LENGTH_LONG)
                            .setAction("Clear", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentAnalytics.selectedPostId = "";
                                }
                            })
                            .setActionTextColor(mContext.getResources().getColor(android.R.color.white ))
                            .setBackgroundTint(mContext.getResources().getColor(android.R.color.background_dark ))
                            .show();
                    // lastClickedPosition = position;
                   // holder.ll3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            FragmentAnalytics.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    };
                    FragmentAnalytics.nestedScrollView.post(runnable);

                }
            });
//            if(lastClickedPosition==position){
//                holder.ll3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
//
//            }
//            else
//            {
//                holder.ll3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//
//            }
            holder.MusicPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.MusicPostNft.setText(list.get(position).getIsnft());
            holder.MusicPostTags.setText(list.get(position).getTags());
            holder.MusicPostuserID.setText(list.get(position).getUserid());
            holder.MusicPostuserMail.setText(list.get(position).getUsermail());
            holder.MusicPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
//            holder.MusicPosterName.setText(list.get(position).getUsernamee());
//            holder.MusicPosterDate.setText(list.get(position).getDatetimepost());
           // holder. MusicPosterCaption.setText(list.get(position).getCaption());
            //Uri uri= Uri.parse(list.get(position).getDataURL());
            Picasso.get().load(list.get(position).getCoverart()).into(holder.MusicCover);

            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                   // addEngagementClicks(position);

                    Intent intent = new Intent(mContext, MusicSpecificPost.class);
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
            holder.ll4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "Selected: "+list.get(position).getCaption(), Toast.LENGTH_SHORT).show();
                    FragmentAnalytics.tIL1.getEditText().setText(list.get(position).getCaption());
                    FragmentAnalytics.tIL2.getEditText().setText(list.get(position).getCategory());
                    FragmentAnalytics.selectedPostId = list.get(position).getPostid();
                    Snackbar.make(v, "Selected: "+list.get(position).getCaption(), Snackbar.LENGTH_LONG)
                            .setAction("Clear", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentAnalytics.selectedPostId = "";
                                }
                            })
                            .setActionTextColor(mContext.getResources().getColor(android.R.color.white ))
                            .setBackgroundTint(mContext.getResources().getColor(android.R.color.background_dark ))
                            .show();
                    //   lastClickedPosition = position;
                 //   holder.ll4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            FragmentAnalytics.nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
                        }
                    };
                    FragmentAnalytics.nestedScrollView.post(runnable);
                }
            });
//            if(lastClickedPosition==position){
//                holder.ll4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
//
//            }
//            else
//            {
//                holder.ll4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//
//            }
//            holder.TextPostAnalytics.setText(list.get(position).getIsanalytics());
            holder.TextPostNft.setText(list.get(position).getIsnft());
            holder.TextPostTags.setText(list.get(position).getTags());
            holder. TextPostuserID.setText(list.get(position).getUserid());
            holder.TextPostuserMail.setText(list.get(position).getUsermail());
//            holder.TextPostID.setText(list.get(position).getUserid());
            //Picasso.get().load().into(posterImage);
//            holder.TextPosterName.setText(list.get(position).getUsernamee());
//            holder.TextPosterDate.setText(list.get(position).getDatetimepost());
//            holder.TextPosterCaption.setText(list.get(position).getCaption());
            holder.TextPostText.setText(list.get(position).getDataURL());

            holder.TextPostText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // addEngagementClicks(position);

                    Intent intent = new Intent(mContext, TextSpecificPost.class);
                    intent.putExtra("userid",list.get(position).getUserid());
                    intent.putExtra("postid",list.get(position).getPostid());
                    intent.putExtra("caption",list.get(position).getCaption());
                    intent.putExtra("uri",list.get(position).getDataURL());
                    intent.putExtra("color",list.get(position).getColor());
                    mContext.startActivity(intent);
                }
            });



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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        referencee = FirebaseDatabase.getInstance().getReference();
        referencee.child("PostEngagements").child("Views").child(list.get(position).getPostid()).child(list.get(position).getUserid()).setValue(currentDateAndTime);

    }
//    public void addEngagementClicks(int position) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
//        String currentDateAndTime = sdf.format(new Date());
//        referencee = FirebaseDatabase.getInstance().getReference();
//        referencee.child("PostEngagements").child("Clicks").child(list.get(position).getPostid()).child(list.get(position).getUserid()).setValue(currentDateAndTime);
//
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {



        CardView ImageViewPost;
       // TextView postCategory;
       // TextView postColor;
        TextView postAnalytics;
        TextView postNft ;
        TextView postTags;
        TextView postuserID;
        TextView postuserMail;
        TextView postID;
        ImageView posterImage;
       // TextView posterName;
       // TextView posterDate;
        TextView posterCaption;
        ImageView posterImageUrl;
        TextView groupID;
        public LinearLayout ll1,ll2,ll3,ll4;



        CardView VideoViewPost ;
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

        CardView MusicViewPost ;
        TextView MusicPostAnalytics;
        TextView MusicPostNft;
        TextView MusicPostTags;
        TextView MusicPostuserID;
        TextView MusicPostuserMail;
        TextView MusicPostID ;
        ImageView MusicPosterImage;
        //TextView MusicPosterName;
      //  TextView MusicPosterDate;
      //  TextView MusicPosterCaption;
        FloatingActionButton playBtn;
        ImageView MusicCover;

        CardView TextViewPost ;
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
        TextView TextPostText;
        ImageView likeTextPost;

        public ViewHolder(View itemView) {
            super(itemView);

            ll1 = (LinearLayout) itemView.findViewById(R.id.omino_analytics_post_design_ll);
            ll2 = (LinearLayout) itemView.findViewById(R.id.omino_analytics_post_design_llVideo);
            ll3 = (LinearLayout) itemView.findViewById(R.id.omino_analytics_post_design_llMusic);
            ll4 = (LinearLayout) itemView.findViewById(R.id.omino_analytics_post_design_llText);


            ImageViewPost = (CardView)itemView.findViewById(R.id.ImageViewPost);
            postAnalytics= (TextView)itemView.findViewById(R.id.postAnalytics);
            postNft =(TextView)itemView.findViewById(R.id.postNft);
            postTags =(TextView)itemView.findViewById(R.id.postTags);
            postuserID= (TextView)itemView.findViewById(R.id.postuserID);
            postuserMail =(TextView)itemView.findViewById(R.id.postuserMail);
            postID =(TextView)itemView.findViewById(R.id.postID);
            posterImage = (ImageView)itemView.findViewById(R.id.posterImage);
           // posterName =(TextView)itemView.findViewById(R.id.posterName);
           // posterDate =(TextView)itemView.findViewById(R.id.posterDate);
            posterCaption =(TextView)itemView.findViewById(R.id.posterCaption);
            posterImageUrl = (ImageView)itemView.findViewById(R.id.posterImageUrl);

            VideoViewPost = (CardView)itemView.findViewById(R.id.VideoViewPost);
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



            TextViewPost = (CardView)itemView.findViewById(R.id.TextViewPost);
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
            TextPostText = (TextView)itemView.findViewById(R.id.TextPostText);
            likeTextPost = (ImageView)itemView.findViewById(R.id.TextPostlikeicon);


            MusicViewPost = (CardView)itemView.findViewById(R.id.MusicViewPost);
            MusicPostAnalytics= (TextView)itemView.findViewById(R.id.MusicPostAnalytics);
            MusicPostNft =(TextView)itemView.findViewById(R.id.MusicPostNft);
            MusicPostTags =(TextView)itemView.findViewById(R.id.MusicPostTags);
            MusicPostuserID= (TextView)itemView.findViewById(R.id.MusicPostuserID);
            MusicPostuserMail =(TextView)itemView.findViewById(R.id.MusicPostuserMail);
            MusicPostID =(TextView)itemView.findViewById(R.id.MusicPostID);
            MusicPosterImage = (ImageView)itemView.findViewById(R.id.MusicPosterImage);
           // MusicPosterName =(TextView)itemView.findViewById(R.id.MusicPosterName);
          //  MusicPosterDate =(TextView)itemView.findViewById(R.id.MusicPosterDate);
          //  MusicPosterCaption =(TextView)itemView.findViewById(R.id.MusicPosterCaption);
            playBtn = (FloatingActionButton)itemView.findViewById(R.id.MusicPost_play);
            MusicTime = (ProgressBar)itemView.findViewById(R.id.Music_progressbar);
            MusicCover = (ImageView)itemView.findViewById(R.id.MusicCover);

//            ll1.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if(event.getAction() == MotionEvent.ACTION_BUTTON_PRESS)
//                    {
//                        Paint viewPaint = ((PaintDrawable) v.getBackground()).getPaint();
//                        int colorARGB = viewPaint.getColor();
//                        ColorDrawable buttonColor = (ColorDrawable) v.getBackground();
//                        int color = buttonColor.getColor();
//                        if (color == ContextCompat.getColor(mContext, R.color.white)) {
//                            Toast.makeText(mContext, "Clicked s", Toast.LENGTH_SHORT).show();
//
//                            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ashen_black));
//                        }
//                        else
//                        {
//                            Toast.makeText(mContext, "Clicked saa", Toast.LENGTH_SHORT).show();
//
//                            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//                        }
//
//                    }
////
//                    return false;
//                }
//            });

        }


        public LinearLayout getLl1() {
            return ll1;
        }

        public void setLl1(LinearLayout ll1) {
            this.ll1 = ll1;
        }
    }

    private void updateTimerAndSeekbar() {
        long currentPosition = (long) mp.getCurrentPosition();
        MusicTime.setProgress(utils.getProgressSeekBar(currentPosition, (long) this.mp.getDuration()));

    }



}