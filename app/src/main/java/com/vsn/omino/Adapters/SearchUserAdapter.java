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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.ChatActivity;
import com.vsn.omino.activites.VibeChannelData;
import com.vsn.omino.models.Explore;
import com.vsn.omino.models.User;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchUserAdapter   extends BaseAdapter {

    Context context;
    List<User> exploreList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    public SearchUserAdapter(Context context, List<User> exploreList) {
        this.context = context;
        this.exploreList = exploreList;
    }

    @Override
    public int getCount() {
        return exploreList.size();
    }

    @Override
    public Object getItem(int position) {
        return exploreList.get(position).getUser_id();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final View v = View.inflate(context, R.layout.omino_searchpeople_design,null);


        ImageView CoverImage = (ImageView) v.findViewById(R.id.coverImage);
        TextView name = (TextView) v.findViewById(R.id.userName);
        TextView email = (TextView) v.findViewById(R.id.userEmail);
        TextView  channelID = (TextView) v.findViewById(R.id.userID);
        ImageView liked = (ImageView) v.findViewById(R.id.chat_open);
        CardView cardView = (CardView) v.findViewById(R.id.cardView);
        name.setText(exploreList.get(position).getUsername());
        email.setText(exploreList.get(position).getEmail());
        channelID.setText(exploreList.get(position).getUser_id());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserAccountSettings").child(exploreList.get(position).getUser_id()).child("profile_photo");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue().toString();
                Picasso.get().load(value).into(CoverImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
              //  Log.e(TAG,"Error while reading data");
            }
        });
//
//        if(exploreList.get(position).getSaved()){
//            liked.setColorFilter(ContextCompat.getColor(context, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
//        }




        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("frontuserid",exploreList.get(position).getUser_id());
                intent.putExtra("email",exploreList.get(position).getEmail());
                intent.putExtra("phoneNo",exploreList.get(position).getPhone_number());
                intent.putExtra("cname",exploreList.get(position).getUsername());
                context.startActivity(intent);

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, VibeChannelData.class);
//                intent.putExtra("cid",exploreList.get(position).getUser_id());
//                intent.putExtra("cover",exploreList.get(position).ge());
//                intent.putExtra("uid",exploreList.get(position).getUserid());
//                intent.putExtra("cname",exploreList.get(position).getVibe_Channel_Name());
//                context.startActivity(intent);
            }
        });


        return v;
    }
}
