package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.SendChatFilesActivity;
import com.vsn.omino.models.MessageModel;
import com.vsn.omino.models.SavedCollectionModel;
import com.vsn.omino.utiles.MusicUtils;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<MessageModel> items;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    public static final int MSG_BY_ME = 0;
    public static final int IMG_BY_ME = 1;
    public static final int MP4_BY_ME = 2;
    public static final int VOICE_BY_ME = 3;
    public static final int DOC_BY_ME = 4;

    public static final int MSG_BY_YOU = 5;
    public static final int IMG_BY_YOU = 6;
    public static final int MP4_BY_YOU = 7;
    public static final int VOICE_BY_YOU = 8;
    public static final int DOC_BY_YOU = 9;
    private MusicUtils utils;
    private MediaPlayer mp;
    private Handler mHandler = new Handler();
    ProgressBar progressBar;


    public MessagesAdapter(Context ctx, List<MessageModel> items) {
        this.ctx = ctx;
        this.items = items;
        prefs = ctx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        this.mp = new MediaPlayer();
        this.utils = new MusicUtils();
        this.progressBar = new ProgressBar(ctx);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(ctx);

        switch (viewType) {
            case MSG_BY_ME :
                View v1 = inflater.inflate(R.layout.message_byme, parent, false);
                viewHolder = new MessagesAdapter.SendTextView(v1);
                break;
            case IMG_BY_ME :
                View v2 = inflater.inflate(R.layout.image_byme, parent, false);
                viewHolder = new MessagesAdapter.SendImgView(v2);
                break;
            case MP4_BY_ME :
                View v3 = inflater.inflate(R.layout.video_byme, parent, false);
                viewHolder = new MessagesAdapter.SendVideoView(v3);
                break;
            case VOICE_BY_ME :
                View v4 = inflater.inflate(R.layout.audio_byme, parent, false);
                viewHolder = new MessagesAdapter.SendAudioView(v4);
                break;
            case MSG_BY_YOU :
                View v5 = inflater.inflate(R.layout.message_byyou, parent, false);
                viewHolder = new MessagesAdapter.ReceiveTextView(v5);
                break;
            case IMG_BY_YOU :
                View v6 = inflater.inflate(R.layout.image_byyou, parent, false);
                viewHolder = new MessagesAdapter.ReceiveImgView(v6);
                break;
            case MP4_BY_YOU :
                View v7 = inflater.inflate(R.layout.video_byyou, parent, false);
                viewHolder = new MessagesAdapter.ReceiveVideoView(v7);
                break;
            case VOICE_BY_YOU :
                View v8 = inflater.inflate(R.layout.audio_byyou, parent, false);
                viewHolder = new MessagesAdapter.ReceiveAudioView(v8);
                break;
            default: return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessagesAdapter.SendTextView) {
                ((SendTextView) holder).text_content.setText(items.get(position).getContent());
                ((SendTextView) holder).text_time.setText(items.get(position).getDateTime());

        }
        else if(holder instanceof  MessagesAdapter.SendImgView){
                 Picasso.get().load(items.get(position).getContent()).into(((SendImgView) holder).img);
                 ((SendImgView) holder).text_time.setText(items.get(position).getDateTime());
        }
        else if(holder instanceof  MessagesAdapter.SendAudioView){
            ((SendAudioView) holder).song_progressbar.setProgress(0);
            ((SendAudioView) holder).song_progressbar.setMax(10000);
            progressBar = ((SendAudioView) holder).song_progressbar;
            try{
                Uri uri = Uri.parse(items.get(position).getContent().toString());
                ((SendAudioView) holder).song_progressbar.setProgress(0);
                mp.reset();
                mp.setAudioStreamType(3);
                //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                mp.setDataSource(ctx, uri);
                //openFd.close();
                mp.prepare();
            }catch (Exception e){
                Toast.makeText(ctx, "Cannot load audio file", Toast.LENGTH_SHORT).show();
            }

            ((SendAudioView) holder).bt_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        mp.pause();
                        ((SendAudioView) holder).bt_play.setImageResource(R.drawable.play_arrow);
                        return;
                    }
                    mp.start();
                    ((SendAudioView) holder).bt_play.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);
                }
            });

        }
        else if(holder instanceof  MessagesAdapter.SendVideoView){

            Uri uri = Uri.parse(items.get(position).getContent().toString());
            MediaController mediaController= new MediaController(ctx);
            mediaController.setAnchorView(((SendVideoView) holder).videoView);
            ((SendVideoView) holder).videoView.setMediaController(mediaController);
            ((SendVideoView) holder).videoView.setVideoURI(uri);

        }
        else if(holder instanceof  MessagesAdapter.ReceiveTextView){
                ((ReceiveTextView) holder).text_content.setText(items.get(position).getContent());
                ((ReceiveTextView) holder).text_time.setText(items.get(position).getDateTime());
        }
        else if(holder instanceof  MessagesAdapter.ReceiveImgView){
            Picasso.get().load(items.get(position).getContent()).into(((ReceiveImgView) holder).img);
            ((ReceiveImgView) holder).text_time.setText(items.get(position).getDateTime());
        }
        else if(holder instanceof  MessagesAdapter.ReceiveAudioView){
            ((ReceiveAudioView) holder).song_progressbar.setProgress(0);
            ((ReceiveAudioView) holder).song_progressbar.setMax(10000);
            progressBar = ((ReceiveAudioView) holder).song_progressbar;
            try{
                Uri uri = Uri.parse(items.get(position).getContent().toString());
                ((ReceiveAudioView) holder).song_progressbar.setProgress(0);
                mp.reset();
                mp.setAudioStreamType(3);
                //AssetFileDescriptor openFd = getAssets().openFd("short_music.mp3");
                mp.setDataSource(ctx, uri);
                //openFd.close();
                mp.prepare();
            }catch (Exception e){
                Toast.makeText(ctx, "Cannot load audio file", Toast.LENGTH_SHORT).show();
            }

            ((ReceiveAudioView) holder).bt_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp.isPlaying()) {
                        mp.pause();
                        ((ReceiveAudioView) holder).bt_play.setImageResource(R.drawable.play_arrow);
                        return;
                    }
                    mp.start();
                    ((ReceiveAudioView) holder).bt_play.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);
                }
            });
        }
        else if(holder instanceof  MessagesAdapter.ReceiveVideoView){
            Uri uri = Uri.parse(items.get(position).getContent().toString());
            MediaController mediaController= new MediaController(ctx);
            mediaController.setAnchorView(((ReceiveVideoView) holder).videoView);
            ((ReceiveVideoView) holder).videoView.setMediaController(mediaController);
            ((ReceiveVideoView) holder).videoView.setVideoURI(uri);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {

        int returnView = -1;

        if(items.get(position).getSender().equals(prefs.getString("userID",null))){
            if(items.get(position).getMsgType().equals("text")){
                returnView = MSG_BY_ME;
            }else
            if(items.get(position).getMsgType().equals("image")){
                returnView = IMG_BY_ME;
            }else
            if(items.get(position).getMsgType().equals("video")){
                returnView = MP4_BY_ME;
            }else
            if(items.get(position).getMsgType().equals("audio")){
                returnView = VOICE_BY_ME;
            }else
            if(items.get(position).getMsgType().equals("other")){
                returnView = DOC_BY_ME;
            }

        }
        else{
            if(items.get(position).getMsgType().equals("text")){
                returnView = MSG_BY_YOU;
            }else
            if(items.get(position).getMsgType().equals("image")){
                returnView = IMG_BY_YOU;
            }else
            if(items.get(position).getMsgType().equals("video")){
                returnView = MP4_BY_YOU;
            }else
            if(items.get(position).getMsgType().equals("audio")){
                returnView = VOICE_BY_YOU;
            }else
            if(items.get(position).getMsgType().equals("other")){
                returnView = DOC_BY_YOU;
            }
        }
        return returnView;
    }

    public void insertItem(MessageModel message) {
        this.items.add(message);
        notifyItemInserted(getItemCount());
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
        progressBar.setProgress(utils.getProgressSeekBar(currentPosition, (long) this.mp.getDuration()));

    }





    public class SendTextView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public TextView text_content;
        public TextView text_time;

        public SendTextView(@NonNull View itemView) {
            super(itemView);
            this.text_content = (TextView) itemView.findViewById(R.id.text_content);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

    public class SendImgView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public ImageView img;
        public TextView text_time;

        public SendImgView(@NonNull View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.msg_img);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

    public class SendAudioView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public FloatingActionButton bt_play;
        public TextView text_time;
        public ProgressBar song_progressbar;
        public SendAudioView(@NonNull View itemView) {
            super(itemView);
            this.bt_play = (FloatingActionButton) itemView.findViewById(R.id.bt_play);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);
            this.song_progressbar = (ProgressBar)itemView.findViewById(R.id.song_progressbar);


        }
    }

    public class SendVideoView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public VideoView videoView;
        public TextView text_time;

        public SendVideoView(@NonNull View itemView) {
            super(itemView);
            this.videoView = (VideoView) itemView.findViewById(R.id.msg_video);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

    public class ReceiveTextView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public TextView text_content;
        public TextView text_time;

        public ReceiveTextView(@NonNull View itemView) {
            super(itemView);
            this.text_content = (TextView) itemView.findViewById(R.id.text_content);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);
        }
    }

    public class ReceiveImgView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public ImageView img;
        public TextView text_time;

        public ReceiveImgView(@NonNull View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.msg_img);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

    public class ReceiveAudioView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public FloatingActionButton bt_play;
        public TextView text_time;
        public ProgressBar song_progressbar;
        public ReceiveAudioView(@NonNull View itemView) {
            super(itemView);
            this.bt_play = (FloatingActionButton) itemView.findViewById(R.id.bt_play);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);
            this.song_progressbar = (ProgressBar)itemView.findViewById(R.id.song_progressbar);


        }
    }

    public class ReceiveVideoView extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public VideoView videoView;
        public TextView text_time;

        public ReceiveVideoView(@NonNull View itemView) {
            super(itemView);
            this.videoView = (VideoView) itemView.findViewById(R.id.msg_video);
            this.text_time = (TextView) itemView.findViewById(R.id.text_time);
            this.lyt_parent = itemView.findViewById(R.id.lyt_parent);


        }
    }

}

