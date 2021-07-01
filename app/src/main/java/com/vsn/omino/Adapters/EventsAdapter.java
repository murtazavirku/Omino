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
import com.vsn.omino.activites.GroupHomePage;
import com.vsn.omino.models.Events;
import com.vsn.omino.models.Groups;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    Context mContext;
    List<Events> list;

    public EventsAdapter(Context mContext, List<Events> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.omnio_event_design , parent,false);
        EventsAdapter.ViewHolder viewHolder = new EventsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getBackgroundImg()).into(holder.eventImg);
        holder.eventName.setText(list.get(position).getEventName());
        holder.eventDate.setText(list.get(position).getEventDate());
        holder.groupID.setText(list.get(position).getGroupId());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GroupHomePage groupHomePage = new GroupHomePage(mediaPlayer);
//                Intent intent = new Intent(mContext, groupHomePage.getClass());
//                intent.putExtra("icon",list.get(position).getGroupIcon());
//                intent.putExtra("name",holder.groupName.getText().toString());
//                intent.putExtra("subs",holder.groupSubs.getText().toString());
//                intent.putExtra("id",holder.groupID.getText().toString());
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        CircleImageView eventImg;
        TextView eventName;
        TextView eventDate;
        TextView groupID;

        public ViewHolder(View itemView) {
            super(itemView);
            eventImg = (CircleImageView)itemView.findViewById(R.id.eventImg);
            eventName = (TextView) itemView.findViewById(R.id.eventName);
            eventDate = (TextView)itemView.findViewById(R.id.eventDate);
            groupID = (TextView)itemView.findViewById(R.id.groupid);
            mainLayout = (RelativeLayout)itemView.findViewById(R.id.mainLayout);
        }
    }

}

