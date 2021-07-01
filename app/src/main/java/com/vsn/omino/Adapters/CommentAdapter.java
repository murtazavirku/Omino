package com.vsn.omino.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.R;
import com.vsn.omino.models.CommentModel;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.MusicUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context mContext;
    List<CommentModel> list;
    ArrayList<String> userData;
    public CommentAdapter(Context mContext, List<CommentModel> list,ArrayList<String> userData) {
        this.mContext = mContext;
        this.list = list;
        this.userData = userData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.comment_chat_by_you , parent,false);


        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.Username.setText(userData.get(3));
        holder.Comment.setText(list.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView Username;
            TextView Comment;

        public ViewHolder(View itemView) {
            super(itemView);

            Username = (TextView)itemView.findViewById(R.id.commentTitle);
            Comment = (TextView)itemView.findViewById(R.id.commentMsg);

        }
    }

    public void LoadUserDatabyID(String UserID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData.add(0,snapshot.child("email").getValue().toString());
                userData.add(1,snapshot.child("phone_number").getValue().toString());
                userData.add(2,snapshot.child("user_id").getValue().toString());
                userData.add(3,snapshot.child("username").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
