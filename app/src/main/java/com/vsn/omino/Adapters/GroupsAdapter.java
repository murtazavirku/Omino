package com.vsn.omino.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.activites.GroupHomePage;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.OmnioPosts;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    Context mContext;
    List<Groups> list;
    MediaPlayer mediaPlayer;

    public GroupsAdapter(Context mContext, List<Groups> list,MediaPlayer mediaPlayer) {
        this.mContext = mContext;
        this.list = list;
        this.mediaPlayer = mediaPlayer;
    }

    @NonNull
    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_group_design , parent,false);
        GroupsAdapter.ViewHolder viewHolder = new GroupsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getGroupIcon()).into(holder.groupIcon);
        holder.groupName.setText(list.get(position).getGroupName());
        holder.groupSubs.setText(list.get(position).getGroupNoSubs());
        holder.newPosts.setText(list.get(position).getGroupNewPosts());
        holder.groupID.setText(list.get(position).getGroupID());
        holder.groupDesc.setText(list.get(position).getGroupDesc());
        CountMemebers(holder.groupID.getText().toString(),holder.groupSubs);
        if(list.get(position).getBadge()){
            holder.badge.setVisibility(View.VISIBLE);
        }
        else{
            holder.badge.setVisibility(View.GONE);
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupHomePage groupHomePage = new GroupHomePage(mediaPlayer);
                Intent intent = new Intent(mContext, groupHomePage.getClass());
                intent.putExtra("icon",list.get(position).getGroupIcon());
                intent.putExtra("name",holder.groupName.getText().toString());
                intent.putExtra("subs",holder.groupSubs.getText().toString());
                intent.putExtra("id",holder.groupID.getText().toString());
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
        CircleImageView groupIcon;
        TextView groupName;
        TextView groupSubs;
        TextView newPosts;
        CardView badge;
        TextView groupID;
        TextView groupDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            groupIcon = (CircleImageView)itemView.findViewById(R.id.groupImg);
            groupName = (TextView) itemView.findViewById(R.id.groupName);
            groupSubs = (TextView)itemView.findViewById(R.id.groupSubs);
            newPosts = (TextView)itemView.findViewById(R.id.groupNewPosts);
            badge = (CardView) itemView.findViewById(R.id.groupBadge);
            groupID = (TextView)itemView.findViewById(R.id.groupid);
            groupDesc = (TextView)itemView.findViewById(R.id.groupdescription);
            mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainLayout);
        }
    }

    private void CountMemebers(String gid ,TextView groupSubs){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(gid).exists()){
                    String countSubs = String.valueOf(snapshot.child(gid).getChildrenCount());
                    groupSubs.setText(countSubs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
