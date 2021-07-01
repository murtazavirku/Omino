package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.VibeChannelData;
import com.vsn.omino.models.Explore;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchVibeChannelAdapter  extends BaseAdapter {

    Context context;
    List<Explore> exploreList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    public SearchVibeChannelAdapter(Context context, List<Explore> exploreList) {
        this.context = context;
        this.exploreList = exploreList;
    }

    @Override
    public int getCount() {
        return exploreList.size();
    }

    @Override
    public Object getItem(int position) {
        return exploreList.get(position).getChannel_id();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final View v = View.inflate(context, R.layout.omino_searchvibechannel_design,null);


        ImageView CoverImage = (ImageView) v.findViewById(R.id.coverImage);
        TextView Title = (TextView) v.findViewById(R.id.channelTitle);
        TextView  channelID = (TextView) v.findViewById(R.id.channelID);
        ImageView liked = (ImageView) v.findViewById(R.id.liked);
        CardView cardView = (CardView) v.findViewById(R.id.cardView);
        Picasso.get().load(exploreList.get(position).getImage_lnk()).into(CoverImage);
        Title.setText(exploreList.get(position).getVibe_Channel_Name());
        channelID.setText(exploreList.get(position).getChannel_id());
        if(exploreList.get(position).getSaved()){
            liked.setColorFilter(ContextCompat.getColor(context, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
        }




        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liked.getColorFilter() == null) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SavedVibeChannel").child(prefs.getString("userID", null).toString());
                    HashMap<String, String> chSavedMap = new HashMap<>();
                    chSavedMap.put("isSaved", "yes");
                    reference.child(exploreList.get(position).getChannel_id()).setValue(chSavedMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    liked.setColorFilter(ContextCompat.getColor(context, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);

                                }
                            });
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VibeChannelData.class);
                intent.putExtra("cid",exploreList.get(position).getChannel_id());
                intent.putExtra("cover",exploreList.get(position).getImage_lnk());
                intent.putExtra("uid",exploreList.get(position).getUserid());
                intent.putExtra("cname",exploreList.get(position).getVibe_Channel_Name());
                context.startActivity(intent);
            }
        });


        return v;
    }
}
