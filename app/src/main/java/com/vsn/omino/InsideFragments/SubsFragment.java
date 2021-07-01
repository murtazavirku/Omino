package com.vsn.omino.InsideFragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.Adapters.NewsfeedAdapter;
import com.vsn.omino.Adapters.SubsAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SubsFragment extends Fragment {

    RecyclerView listView;
    List<OmnioPosts> OmnioPostsList;
    NewsFeedAddapter newsfeedAdapter;
    DatabaseReference databaseReference;
    CheckInternet checkInternet;
    MediaPlayer mediaPlayer;
    String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    ProgressDialog pd;
    public SubsFragment(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subs, container, false);
        listView = (RecyclerView) view.findViewById(R.id.news_feed_List);
        listView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        OmnioPostsList = new ArrayList<>();
        checkInternet = new CheckInternet();
        pd = new ProgressDialog(getContext());
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);

        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserSubscriptions");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    if(snapshot1.child(prefs.getString("userID", null).toString()).exists()){

                        GetSubs getSubs = new GetSubs();
                        getSubs.execute(snapshot1.getKey().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }



    private class GetSubs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if(checkInternet.checkInternetConnection(getContext())){
                //noInternet.setVisibility(View.GONE);
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        OmnioPostsList.clear();
                        for(DataSnapshot ss : snapshot.getChildren()) {
                            if(ss.child("userid").getValue().toString().equals(params[0].toString())){

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
                        newsfeedAdapter = new NewsFeedAddapter(getContext(),OmnioPostsList,mediaPlayer);
                        listView.setAdapter(newsfeedAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            pd.dismiss();

        }

        @Override
        protected void onPreExecute() {

            pd.show();


        }

    }















}