package com.vsn.omino;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.InsideFragments.NewsFeedFragment;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class FragmentSearchPosts extends Fragment {

    private static FragmentSearchPosts instance;

    RecyclerView listView;
    List<OmnioPosts> OmnioPostsList;
    NewsFeedAddapter newsfeedAdapter;
    DatabaseReference databaseReference;
    ProgressBar postProgress;
    CheckInternet checkInternet;
    LinearLayout noInternet;
    TextView TryAgain;
    MediaPlayer mediaPlayer;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    int nextTenLoader;
    private String oldestPostId;
    public static String searchableData;
    public static boolean searchingTrigger;
    private int limiterVar;

    public FragmentSearchPosts(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public FragmentSearchPosts(){}

    public static FragmentSearchPosts getInstance() {
        return instance;
    }

    public void myMethod() {
        OmnioPostsList.clear();
        loadWithSearchOneTime(0,searchableData);
       // Toast.makeText( getContext(),"Triggered" , Toast.LENGTH_SHORT).show();
        newsfeedAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        listView = (RecyclerView) view.findViewById(R.id.news_feed_List);
        postProgress = (ProgressBar)view.findViewById(R.id.postProgress);
        postProgress.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        OmnioPostsList = new ArrayList<>();
        checkInternet = new CheckInternet();
        nextTenLoader = 0;
        searchableData ="";
        searchingTrigger = false;
        instance = this;
        limiterVar = 10;
        noInternet = (LinearLayout)view.findViewById(R.id.lnI);
        TryAgain = (TextView)view.findViewById(R.id.tryagain);
        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadThreeOneTime(0,searchableData);
                loadWithSearchOneTime(0,searchableData);
            }
        });

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    newsfeedAdapter.addEngagementView(position);
                  //  int lastIndex = String.valueOf(position).lastIndexOf(String.valueOf(position));
                 //   char lastValue = String.valueOf(position).charAt(lastIndex);
                 //   if(String.valueOf(lastValue).equals("1"))
                 //   {
                      //  loadTenWithSearch();

                 //   }
                   // Toast.makeText( getContext(), String.valueOf(position)+"LOADNEXT" , Toast.LENGTH_SHORT).show();

                }
            }
        });

       // loadTenWithSearch();
        //loadThreeOneTime(0,searchableData);
        loadWithSearchOneTime(0,searchableData);

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            private int currentVisibleItemCount;
//            private int currentScrollState;
//            private int currentFirstVisibleItem;
//            private int totalItem;
//            private LinearLayout lBelow;



            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
               // super.onScrollStateChanged(recyclerView, newState);
              //  Toast.makeText(getContext(), "String.valueOf(position) ", Toast.LENGTH_SHORT).show();
               // loadThree(newState,searchableData);
                loadWithSearchNext(newState,searchableData);
            }

//            @Override
//            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
////            @Override
////            public void onScroll (AbsListView view,int firstVisibleItem,
////                                  int visibleItemCount, int totalItemCount){
////                this.currentFirstVisibleItem = firstVisibleItem;
////                this.currentVisibleItemCount = visibleItemCount;
////                this.totalItem = totalItemCount;
////
////
////            }
//
//            private void isScrollCompleted () {
//
//            }

        });

        return view;
    }
    private int getCurrentItem(){
        return ((LinearLayoutManager)listView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

//    public void loadThree(int newState,String searchString)
//    {
//        if(checkInternet.checkInternetConnection(getContext())){
//            noInternet.setVisibility(View.GONE);
//
//            if (!listView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//            databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
//            databaseReference.orderByKey().startAfter(oldestPostId).limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ss : dataSnapshot.getChildren()) {
//                        oldestPostId = ss.getKey();
//                        String caption = ss.child("caption").getValue().toString();
//                        String category= ss.child("category").getValue().toString();
//                        String color= ss.child("color").getValue().toString();
//                        String dataURL= ss.child("dataURL").getValue().toString();
//                        String datetimepost= ss.child("datetimepost").getValue().toString();
//                        String isanalytics= ss.child("isanalytics").getValue().toString();
//                        String isnft= ss.child("isnft").getValue().toString();
//                        String tags= ss.child("tags").getValue().toString();
//                        String userid= ss.child("userid").getValue().toString();
//                        String usermail= ss.child("usermail").getValue().toString();
//                        String usernamee= ss.child("usernamee").getValue().toString();
//                        String coverart = ss.child("coverart").getValue().toString();
//                        String postid = ss.child("postid").getValue().toString();
//                        String groupid = ss.child("Groupid").getValue().toString();
//                        OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
//
//                    }
//                    newsfeedAdapter.notifyDataSetChanged();
//                 //   nextTenLoader = nextTenLoader+10;
//                    postProgress.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//    }
//        else{
//        listView.setVisibility(View.GONE);
//        postProgress.setVisibility(View.GONE);
//        noInternet.setVisibility(View.VISIBLE);
//
//    }
//
//    }
//
//    public void loadThreeOneTime(int newState,String searchString)
//    {
//        if(checkInternet.checkInternetConnection(getContext())){
//            noInternet.setVisibility(View.GONE);
//
//            if (!listView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
//            databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
//            databaseReference.orderByKey().limitToFirst(3).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot ss : dataSnapshot.getChildren()) {
//                        oldestPostId = ss.getKey();
//                        String caption = ss.child("caption").getValue().toString();
//                        String category= ss.child("category").getValue().toString();
//                        String color= ss.child("color").getValue().toString();
//                        String dataURL= ss.child("dataURL").getValue().toString();
//                        String datetimepost= ss.child("datetimepost").getValue().toString();
//                        String isanalytics= ss.child("isanalytics").getValue().toString();
//                        String isnft= ss.child("isnft").getValue().toString();
//                        String tags= ss.child("tags").getValue().toString();
//                        String userid= ss.child("userid").getValue().toString();
//                        String usermail= ss.child("usermail").getValue().toString();
//                        String usernamee= ss.child("usernamee").getValue().toString();
//                        String coverart = ss.child("coverart").getValue().toString();
//                        String postid = ss.child("postid").getValue().toString();
//                        String groupid = ss.child("Groupid").getValue().toString();
//                      //  if(caption.contains())
//                        OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
//
//                    }
//                    newsfeedAdapter = new NewsFeedAddapter(getContext(),OmnioPostsList,mediaPlayer);
//                    listView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    listView.setAdapter(newsfeedAdapter);
//                    // Set layout manager to position the items
//                    nextTenLoader = nextTenLoader+10;
//                    postProgress.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//        }
//        else{
//            listView.setVisibility(View.GONE);
//            postProgress.setVisibility(View.GONE);
//            noInternet.setVisibility(View.VISIBLE);
//
//        }
//    }

    public void loadWithSearchOneTime(int newState,String searchString)
    {
        if(checkInternet.checkInternetConnection(getContext())){
            noInternet.setVisibility(View.GONE);

                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int iterator = 0;
                        for(DataSnapshot ss : dataSnapshot.getChildren()) {
                            oldestPostId = ss.getKey();
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
                            if(searchingTrigger)
                            {
                            if(caption.toLowerCase().contains(searchString.toLowerCase()) || tags.toLowerCase().contains(searchString.toLowerCase()))
                            {
                                OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                iterator++;
                                if(iterator == limiterVar)
                                {
                                    break;
                                }
                            }
                            }
                            else
                            {
                                OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                iterator = iterator + 1;
                                if(iterator == limiterVar)
                                {
                                    break;
                                }
                            }

                        }
                        newsfeedAdapter = new NewsFeedAddapter(getContext(),OmnioPostsList,mediaPlayer);
                        listView.setLayoutManager(new LinearLayoutManager(getContext()));
                        listView.setAdapter(newsfeedAdapter);
                        // Set layout manager to position the items
                        postProgress.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        }
        else{
            listView.setVisibility(View.GONE);
            postProgress.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }

    }
    public void loadWithSearchNext(int newState,String searchString)
    {
        if(checkInternet.checkInternetConnection(getContext())){
            noInternet.setVisibility(View.GONE);

            if (!listView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference.orderByKey().startAfter(oldestPostId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int iterator = 0;

                        for(DataSnapshot ss : dataSnapshot.getChildren()) {
                            oldestPostId = ss.getKey();
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
                            if(searchingTrigger)
                            {
                                if(caption.toLowerCase().contains(searchString.toLowerCase()) || tags.toLowerCase().contains(searchString.toLowerCase()))
                                {
                                    OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                    iterator++;
                                    if(iterator == limiterVar)
                                    {
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                iterator = iterator + 1;
                                if(iterator == limiterVar)
                                {
                                    break;
                                }
                            }
                        }
                        newsfeedAdapter.notifyDataSetChanged();
                        // Set layout manager to position the items
                       // nextTenLoader = nextTenLoader+10;
                        postProgress.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
        else{
            listView.setVisibility(View.GONE);
            postProgress.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);

        }
    }
    public void triggeringFunction()
    {
        loadWithSearchOneTime(0,searchableData);
    }
//
//
//    public void loadTenWithSearch()
//    {
//        if(checkInternet.checkInternetConnection(getContext())){
//            noInternet.setVisibility(View.GONE);
//            databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
//            databaseReference.startAt("0").endAt("2").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                   // OmnioPostsList.clear();
//                    for(DataSnapshot ss : snapshot.getChildren()) {
//
//                        String caption = ss.child("caption").getValue().toString();
//                        String category= ss.child("category").getValue().toString();
//                        String color= ss.child("color").getValue().toString();
//                        String dataURL= ss.child("dataURL").getValue().toString();
//                        String datetimepost= ss.child("datetimepost").getValue().toString();
//                        String isanalytics= ss.child("isanalytics").getValue().toString();
//                        String isnft= ss.child("isnft").getValue().toString();
//                        String tags= ss.child("tags").getValue().toString();
//                        String userid= ss.child("userid").getValue().toString();
//                        String usermail= ss.child("usermail").getValue().toString();
//                        String usernamee= ss.child("usernamee").getValue().toString();
//                        String coverart = ss.child("coverart").getValue().toString();
//                        String postid = ss.child("postid").getValue().toString();
//                        String groupid = ss.child("Groupid").getValue().toString();
//                        OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
//
//                    }
//                    newsfeedAdapter = new NewsFeedAddapter(getContext(),OmnioPostsList,mediaPlayer);
//                    listView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    listView.setAdapter(newsfeedAdapter);
//                    // Set layout manager to position the items
//                    nextTenLoader = nextTenLoader+10;
//                    postProgress.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//        else{
//            listView.setVisibility(View.GONE);
//            postProgress.setVisibility(View.GONE);
//            noInternet.setVisibility(View.VISIBLE);
//
//        }
//    }


}

