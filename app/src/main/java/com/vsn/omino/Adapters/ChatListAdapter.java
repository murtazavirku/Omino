package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.ChatActivity;
import com.vsn.omino.activites.GroupHomePage;
import com.vsn.omino.models.ChatListModel;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context mContext;
    List<ChatListModel> list;
    String SearchableData;
    boolean SearchableTrigger;

    public ChatListAdapter(Context mContext, List<ChatListModel> list,String SearchableData,boolean SearchableTrigger) {
        this.mContext = mContext;
        this.list = list;
        this.SearchableData =SearchableData;
        this.SearchableTrigger = SearchableTrigger;
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_chat_list_design , parent,false);
        ChatListAdapter.ViewHolder viewHolder = new ChatListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {

        holder.lastMsg.setText(list.get(position).getLastMsg());
        holder.newMsgs.setText(list.get(position).getNewMsgs());
        holder.userID.setText(list.get(position).getUserID());
        if(SearchableTrigger)
        {
            loadUserImg_nameBySearch(holder.userImg, holder.username, holder.userID.getText().toString(),SearchableData,position,holder);
        }
        else {
            loadUserImg_name(holder.userImg, holder.username, holder.userID.getText().toString());
        }
        if(list.get(position).getIsBadgeVisible()){
            holder.badge.setVisibility(View.VISIBLE);
        }
        else{
            holder.badge.setVisibility(View.GONE);
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("frontuserid",holder.userID.getText().toString());
                mContext.startActivity(intent);
            }
        });




    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mainLayout;
        CircleImageView userImg;
        TextView username;
        TextView lastMsg;
        TextView newMsgs;
        TextView userID;
        CardView badge;

        public ViewHolder(View itemView) {
            super(itemView);
            userImg = (CircleImageView)itemView.findViewById(R.id.userimg);
            username = (TextView) itemView.findViewById(R.id.username);
            lastMsg = (TextView)itemView.findViewById(R.id.lastmsg);
            newMsgs = (TextView)itemView.findViewById(R.id.newmsgs);
            badge = (CardView) itemView.findViewById(R.id.groupBadge);
            userID = (TextView)itemView.findViewById(R.id.userid);
            mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainLayout);
        }
    }
    private void loadUserImg_name(CircleImageView userImg, TextView username, String id) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserAccountSettings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    String Img = snapshot.child(id).child("profile_photo").getValue().toString();
                    String name = snapshot.child(id).child("username").getValue().toString();
                    Picasso.get().load(Img).into(userImg);
                    username.setText(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void loadUserImg_nameBySearch(CircleImageView userImg, TextView username, String id, String SearchableData, int position, ViewHolder holder) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserAccountSettings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    if(snapshot.child(id).child("username").getValue().toString().contains(SearchableData)) {
                        String Img = snapshot.child(id).child("profile_photo").getValue().toString();
                        String name = snapshot.child(id).child("username").getValue().toString();
                        Picasso.get().load(Img).into(userImg);
                        username.setText(name);
                    }
                    else
                    {
                        holder.mainLayout.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
