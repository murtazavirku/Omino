package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.Adapters.VibeChannelDataAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.VibeChannelDataModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VibeChannelData extends AppCompatActivity {

    VibeChannelDataAdapter adapter;
    String channel_id,cover,userid,channel_name;
    TextView cname,username;
    de.hdodenhof.circleimageview.CircleImageView profile_photo;
    RecyclerView recycler_view;
    List<VibeChannelDataModel> vibeChannelDataModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibe_channel_data);
        cname = (TextView)findViewById(R.id.cname);
        username = (TextView)findViewById(R.id.username);
        profile_photo = (CircleImageView)findViewById(R.id.profile_photo);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        channel_id = getIntent().getStringExtra("cid");
        cover = getIntent().getStringExtra("cover");
        userid = getIntent().getStringExtra("uid");
        channel_name = getIntent().getStringExtra("cname");
        cname.setText(channel_name);
        vibeChannelDataModelList = new ArrayList<>();
        getUserDetails();
        loadVibeChannelData();




    }

    private void loadVibeChannelData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("VibeChannelData").child(channel_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vibeChannelDataModelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    String Category = snapshot1.child("Category").getValue().toString();
                    String DataCover = snapshot1.child("DataCover").getValue().toString();
                    String DataUrl = snapshot1.child("DataUrl").getValue().toString();
                    vibeChannelDataModelList.add(new VibeChannelDataModel(Category,DataCover, DataUrl));
                }
                adapter = new VibeChannelDataAdapter(VibeChannelData.this,vibeChannelDataModelList);
                recycler_view.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
                recycler_view.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VibeChannelData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getUserDetails() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserAccountSettings").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usern = snapshot.child("username").getValue().toString();
                String proPic = snapshot.child("profile_photo").getValue().toString();
                username.setText(usern);
                Picasso.get().load(proPic).into(profile_photo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VibeChannelData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}