package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.Adapters.GroupsAdapter;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.R;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupHomePage extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    List<OmnioPosts> OmnioPostsList;
    NewsFeedAddapter newsfeedAdapter;
    RecyclerView groupNewsFeed;

    LinearLayout GroupMembers;
    ImageView GroupIcon;
    TextView name,subs;
    Button createPost,subscribeGroup,createEvent;
    CheckInternet checkInternet;
    DatabaseReference databaseReference;
    public GroupHomePage(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public GroupHomePage(){

    }
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_home_page);
        checkInternet = new CheckInternet();
        groupNewsFeed = (RecyclerView)findViewById(R.id.group_feed_List);
        GroupMembers = (LinearLayout) findViewById(R.id.groupMemebers);
        GroupIcon = (ImageView) findViewById(R.id.groupIcon);
        name  = (TextView) findViewById(R.id.name);
        subs = (TextView) findViewById(R.id.subs);
        createPost = (Button) findViewById(R.id.createPost);
        subscribeGroup = (Button)findViewById(R.id.subscribeGroup);
        createEvent = (Button) findViewById(R.id.createEvent);
        subscribeGroup.setVisibility(View.GONE);
        CheckIfSubscribe();
        CountMemebers();
        Picasso.get().load(getIntent().getStringExtra("icon")).into(GroupIcon);
        name.setText(getIntent().getStringExtra("name"));
        subs.setText(getIntent().getStringExtra("subs"));
        OmnioPostsList = new ArrayList<>();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(getIntent().getStringExtra("id")).exists()){
                            if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).exists()){
                                if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).child("isMember").getValue().toString().equals("true")){
                                    Intent intent = new Intent(GroupHomePage.this,GroupCreatePost.class);
                                    intent.putExtra("id",getIntent().getStringExtra("id"));
                                    startActivity(intent);
                                }
                                else{
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                                    builder1.setMessage("You Need to Subscribe first");
                                    builder1.setCancelable(true);
                                    builder1.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                                builder1.setMessage("You Need to Subscribe first");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }
                        else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                            builder1.setMessage("You Need to Subscribe first");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        subscribeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GroupSubs")
                        .child(getIntent().getStringExtra("id"));
                HashMap<String,String> subsMap = new HashMap<>();
                subsMap.put("isMember","true");

                databaseReference.child(prefs.getString("userID", null).toString()).setValue(subsMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    subscribeGroup.setVisibility(View.GONE);
                            }
                        });

            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(getIntent().getStringExtra("id")).exists()){
                            if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).exists()){
                                if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).child("isMember").getValue().toString().equals("true")){
                                    Intent intent = new Intent(GroupHomePage.this,GroupCreateEvent.class);
                                    intent.putExtra("id",getIntent().getStringExtra("id"));
                                    startActivity(intent);
                                }
                                else{
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                                    builder1.setMessage("You Need to Subscribe first");
                                    builder1.setCancelable(true);
                                    builder1.setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            }
                            else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                                builder1.setMessage("You Need to Subscribe first");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                        }
                        else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(GroupHomePage.this);
                            builder1.setMessage("You Need to Subscribe first");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        GetGroupPosts groupPosts = new GetGroupPosts();
        groupPosts.execute();


    }



    private class GetGroupPosts extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            if(checkInternet.checkInternetConnection(GroupHomePage.this)){

                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        OmnioPostsList.clear();
                        for(DataSnapshot ss : snapshot.getChildren()) {
                            if(ss.child("Groupid").getValue().toString().equals(getIntent().getStringExtra("id"))){
                                String caption = ss.child("caption").getValue().toString();
                                String category= ss.child("category").getValue().toString();
                                String color= ss.child("color").getValue().toString();
                                String dataURL= ss.child("dataURL").getValue().toString();
                                String datetimepost= ss.child("datetimepost").getValue().toString();
                                String isanalytics= ss.child("isanalytics").getValue().toString();
                                String isnft= ss.child("isnft").getValue().toString();
                                String tags= ss.child("tags").getValue().toString();
                                String userid= ss.child("userid").getValue().toString();
                                String usermail= ss.child("usermail").getValue().toString();
                                String usernamee= ss.child("usernamee").getValue().toString();
                                String coverart = ss.child("coverart").getValue().toString();
                                String postid = ss.child("postid").getValue().toString();
                                String groupid = ss.child("Groupid").getValue().toString();
                                OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
                            }

                        }
                        //Toast.makeText(getContext(), String.valueOf(OmnioPostsList.size()), Toast.LENGTH_SHORT).show();
                        newsfeedAdapter = new NewsFeedAddapter(GroupHomePage.this,OmnioPostsList,mediaPlayer);
                        groupNewsFeed.setLayoutManager(new LinearLayoutManager(GroupHomePage.this));
                        groupNewsFeed.setAdapter(newsfeedAdapter);
                        // Set layout manager to position the items


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GroupHomePage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else{
                Toast.makeText(GroupHomePage.this, "No Internet", Toast.LENGTH_SHORT).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

    }
    private void CheckIfSubscribe() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(getIntent().getStringExtra("id")).exists()){
                    if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).exists()){
                        if(snapshot.child(getIntent().getStringExtra("id")).child(prefs.getString("userID", null).toString()).child("isMember").getValue().toString().equals("true")){
                                subscribeGroup.setVisibility(View.GONE);
                        }
                        else{
                            subscribeGroup.setVisibility(View.VISIBLE);
                        }
                    }else{
                        subscribeGroup.setVisibility(View.VISIBLE);
                    }
                }else{
                    subscribeGroup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupHomePage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void CountMemebers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(getIntent().getStringExtra("id")).exists()){
                    String countSubs = String.valueOf(snapshot.child(getIntent().getStringExtra("id")).getChildrenCount());
                    subs.setText(countSubs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupHomePage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}