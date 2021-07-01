package com.vsn.omino.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.vsn.omino.Adapters.CommentAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.CommentModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TextSpecificPost extends AppCompatActivity {
    public ArrayList<String> userData;
    public ArrayList<String> frontUser;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    TextView Caption,Author;
    ImageView BtnSend;
    EditText text_content;
    CommentAdapter adapter;
    List<CommentModel> commentModelList;
    RecyclerView recyclerView;
    ImageView likePost,savePost,subUser;
    Boolean likePostFlag;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_specific_post);
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        likePost = (ImageView)findViewById(R.id.likePost);
        subUser = (ImageView)findViewById(R.id.subUser);
        savePost = (ImageView)findViewById(R.id.savePost);
        userData = new ArrayList<>();
        frontUser = new ArrayList<>();
        commentModelList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LoadAllComments();
        Load_react_colors();
        Load_subs_colors();
        Load_saved_colors();
        likePostFlag = false;
        Caption = (TextView)findViewById(R.id.Musiccaption);
        Caption.setText(getIntent().getStringExtra("caption"));
        Author = (TextView)findViewById(R.id.MusicAuthor);
        LoadUserDatabyID(prefs.getString("userID", null).toString());
        FrontUserDatabyID(getIntent().getStringExtra("userid"));
        BtnSend = (ImageView)findViewById(R.id.btn_send);
        text_content = (EditText)findViewById(R.id.text_content);
        webView = (WebView) findViewById(R.id.webView);
        String newUA= "Chrome/43.0.2357.65 ";
        webView.getSettings().setUserAgentString(newUA);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //pDialog.dismiss();
            }
        });
        String url="";
        String doc = getIntent().getStringExtra("uri");
        try {
            url= URLEncoder.encode(doc,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url="+url);
        likePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likePostFlag )
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions").child(getIntent().getStringExtra("postid"));
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        likePost.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                                        likePostFlag = false;
                                    }
                                }
                            });

                }
                else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions").child(getIntent().getStringExtra("postid"));
                    HashMap<String,String> reactMap = new HashMap<>();
                    reactMap.put("React","like");
                    reactMap.put("userid",prefs.getString("userID", null).toString());
                    reactMap.put("userReactTime",currentDateAndTime);
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(reactMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        likePost.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                                        likePostFlag = true;

                                    }
                                }
                            });
                }
            }
        });
        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(savePost.getColorFilter() == null){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedCollection").child(prefs.getString("userID", null).toString()).child("TextCollections");
                    String random_id = databaseReference.push().getKey();
                    HashMap<String,String> CollectionMap = new HashMap<>();
                    CollectionMap.put("DataUrl",getIntent().getStringExtra("uri"));
                    CollectionMap.put("Postid",getIntent().getStringExtra("postid"));
                    CollectionMap.put("Cover",getIntent().getStringExtra("color"));
                    databaseReference.child(random_id).setValue(CollectionMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    savePost.setColorFilter(Color.parseColor("#f44336"));
                                }
                            });
                }


            }
        });
        subUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("userid").equals(prefs.getString("userID", null).toString())){

                }
                else{
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions").child(getIntent().getStringExtra("userid"));
                    HashMap<String,String> reactMap = new HashMap<>();
                    reactMap.put("subscriber",prefs.getString("userID", null).toString());
                    databaseReference.child(prefs.getString("userID", null).toString()).setValue(reactMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        subUser.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);

                                    }
                                }
                            });
                }
            }
        });
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());
                String comment = text_content.getText().toString();
                if (!comment.isEmpty()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(getIntent().getStringExtra("postid"));
                    String random_id = reference.push().getKey();
                    HashMap<String,String> commentMap = new HashMap<>();
                    commentMap.put("usercommentid",prefs.getString("userID", null).toString());
                    commentMap.put("usercomment",text_content.getText().toString());
                    commentMap.put("usercommenttime",currentDateAndTime);
                    reference.child(random_id).setValue(commentMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        adapter.notifyDataSetChanged();
                                        text_content.setText("");
                                    }
                                    else{
                                        Toast.makeText(TextSpecificPost.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void LoadAllComments() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(getIntent().getStringExtra("postid"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModelList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    String user = snapshot1.child("usercommentid").getValue().toString();
                    String comment = snapshot1.child("usercomment").getValue().toString();
                    commentModelList.add(new CommentModel(user,comment));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(TextSpecificPost.this));
                adapter = new CommentAdapter(TextSpecificPost.this,commentModelList,userData);
                recyclerView.setAdapter(adapter);
                if(recyclerView.getAdapter().getItemCount()>0) {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                }
                try {
                    Author.setText(frontUser.get(3));
                }
                catch (IndexOutOfBoundsException e){
                    Log.e("out of bound",e.getMessage());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                Toast.makeText(TextSpecificPost.this, "Something went wronge", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void FrontUserDatabyID(String UserID){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                frontUser.add(0,snapshot.child("email").getValue().toString());
                frontUser.add(1,snapshot.child("phone_number").getValue().toString());
                frontUser.add(2,snapshot.child("user_id").getValue().toString());
                frontUser.add(3,snapshot.child("username").getValue().toString());
                Author.setText(frontUser.get(3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TextSpecificPost .this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void Load_react_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PostReactions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(getIntent().getStringExtra("postid"))){
                        if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){
                            likePost.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                            likePostFlag = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Load_subs_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.getKey().equals(getIntent().getStringExtra("userid"))){
                        if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){
                            subUser.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Load_saved_colors(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedCollection").child(prefs.getString("userID", null).toString()).child("TextCollections");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.child("Postid").getValue().toString().equals(getIntent().getStringExtra("postid"))){

                        savePost.setColorFilter(ContextCompat.getColor(TextSpecificPost.this, R.color.red_500), android.graphics.PorterDuff.Mode.SRC_IN);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}