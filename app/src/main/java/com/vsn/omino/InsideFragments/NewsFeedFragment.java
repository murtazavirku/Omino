package com.vsn.omino.InsideFragments;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.vsn.omino.FragmentSearchPeople;
import com.vsn.omino.FragmentSearchVibeChannel;
import com.vsn.omino.R;
import com.vsn.omino.Room.BedgeEntity;
import com.vsn.omino.Room.DAOs.BedgeDAO;
import com.vsn.omino.Room.DB.AppDatabase;
import com.vsn.omino.activites.GroupCreatePost;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NewsFeedFragment extends Fragment {

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
    private static NewsFeedFragment instance;
    public static String searchableData;
    public static boolean searchingTrigger;
    private int limiterVar;
    private String oldestPostId;
    AppDatabase db;
    List<BedgeEntity> bedgeEntities;

    public NewsFeedFragment(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public NewsFeedFragment(){}

    public static NewsFeedFragment getInstance() {
        return instance;
    }

    public void searchCategory() {
        OmnioPostsList.clear();
        if(checkInternet.checkInternetConnection(getContext())){
            noInternet.setVisibility(View.GONE);

            GetPosts getPosts = new GetPosts();
            getPosts.execute(prefs.getString("userID", null).toString());
        }
        else{
            listView.setVisibility(View.GONE);
            postProgress.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);

        }
        // Toast.makeText( getContext(),"Triggered" , Toast.LENGTH_SHORT).show();
        newsfeedAdapter.notifyDataSetChanged();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        // DB
        db = Room.databaseBuilder(getContext(),AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        BedgeDAO bedgeDAO = db.BedgeDAO();
        //bedgeEntities = bedgeDAO.getAll();
        bedgeEntities = new ArrayList<>();
        // DB
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        listView = (RecyclerView) view.findViewById(R.id.news_feed_List);
        listView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        postProgress = (ProgressBar)view.findViewById(R.id.postProgress);
        postProgress.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        OmnioPostsList = new ArrayList<>();
        checkInternet = new CheckInternet();
        noInternet = (LinearLayout)view.findViewById(R.id.lnI);
        TryAgain = (TextView)view.findViewById(R.id.tryagain);
        instance = this;
        searchableData ="";
        searchingTrigger = false;
        limiterVar = 15;
        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPosts getPosts = new GetPosts();
                getPosts.execute(prefs.getString("userID", null).toString());
            }
        });

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    newsfeedAdapter.addEngagementView(position);
                    //Toast.makeText( getContext(), String.valueOf(position) , Toast.LENGTH_SHORT).show();
                    //String seenPost_id = newsfeedAdapter.getPostID(position);
                    //Toast.makeText(getContext(), seenPost_id, Toast.LENGTH_SHORT).show();

                }
            }
        });

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                //loadWithSearchNext(newState,searchableData);
                GetPostsNext getPosts = new GetPostsNext();
                getPosts.execute(prefs.getString("userID", null).toString(), String.valueOf(newState));
            }

        });
        //listView.itemC

//        databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                OmnioPostsList.clear();
//                for(DataSnapshot ss : snapshot.getChildren()) {
//                    String caption = ss.child("caption").getValue().toString();
//                    String category= ss.child("category").getValue().toString();
//                    String color= ss.child("color").getValue().toString();
//                    String dataURL= ss.child("dataURL").getValue().toString();
//                    String datetimepost= ss.child("datetimepost").getValue().toString();
//                    String isanalytics= ss.child("isanalytics").getValue().toString();
//                    String isnft= ss.child("isnft").getValue().toString();
//                    String tags= ss.child("tags").getValue().toString();
//                    String userid= ss.child("userid").getValue().toString();
//                    String usermail= ss.child("usermail").getValue().toString();
//                    String usernamee= ss.child("usernamee").getValue().toString();
//                    OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee));
//                    newsfeedAdapter = new NewsfeedAdapter(getContext(),OmnioPostsList);
//                    listView.setAdapter(newsfeedAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        if(checkInternet.checkInternetConnection(getContext())){
            noInternet.setVisibility(View.GONE);

        GetPosts getPosts = new GetPosts();
        getPosts.execute(prefs.getString("userID", null).toString());
        }
        else{
            listView.setVisibility(View.GONE);
            postProgress.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);

        }


//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            private int mLastFirstVisibleItem;
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if(mLastFirstVisibleItem<firstVisibleItem)
//                {
//                    Log.i("SCROLLING DOWN","TRUE");
////                    listView.setVisibility(View.GONE);
////                    postProgress.setVisibility(View.VISIBLE);
////                    GetPosts getPosts = new GetPosts();
////                    getPosts.execute();
//
//                }
//                if(mLastFirstVisibleItem>firstVisibleItem)
//                {
//                    Log.i("SCROLLING UP","TRUE");
//                }
//                mLastFirstVisibleItem=firstVisibleItem;
//            }
//        });





        return view;
    }
    private int getCurrentItem(){
        return ((StaggeredGridLayoutManager)listView.getLayoutManager())
                .findFirstVisibleItemPositions(null)[0];
    }



    private class GetPosts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
                databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //outerLoop:
                        OmnioPostsList.clear();
                        final int[] iterator = new int[1];
                        iterator[0] = 0;

                        loop(snapshot,params[0],iterator);
                        //Toast.makeText(getContext(), String.valueOf(OmnioPostsList.size()), Toast.LENGTH_SHORT).show();
                        newsfeedAdapter = new NewsFeedAddapter(getContext(),OmnioPostsList,mediaPlayer);
                        listView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
                        listView.setAdapter(newsfeedAdapter);
                        // Set layout manager to position the items
                        //DATABASE-----------------------------------------------------------------

                        BedgeDAO bedgeDAO = db.BedgeDAO();
                        bedgeEntities = bedgeDAO.getAll();
                        if(bedgeEntities.size()>0){
                            try{
//                                for (int i=0; i<OmnioPostsList.size();i++) {
//                                    if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                                        int unreadPosts = OmnioPostsList.size()-i;
//                                        bedgeDAO.update_bedge(unreadPosts);
//                                    }
//                                    else{
//                                        int unreadPosts = OmnioPostsList.size();
//                                        bedgeDAO.update_bedge(unreadPosts);
//                                    }
//                                }

                                BedgeEntity bedgeEntity = new BedgeEntity();
                                bedgeEntity.id = 1;
                                bedgeEntity.home_post_last_id = OmnioPostsList.get(OmnioPostsList.size()-1).getPostid();
                                bedgeDAO.update(bedgeEntity);


                            }
                            catch (Exception e){
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }



                        }
                        else{
                            try{

//                                for (int i=0; i<OmnioPostsList.size();i++) {
//                                    if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                                        int unreadPosts = OmnioPostsList.size()-i;
//                                        bedgeDAO.update_bedge(unreadPosts);
//                                    }
//                                    else{
//                                        int unreadPosts = OmnioPostsList.size();
//                                        bedgeDAO.update_bedge(unreadPosts);
//                                    }
//                                }

                            BedgeEntity bedgeEntity = new BedgeEntity();
                            bedgeEntity.id = 1;
                            bedgeEntity.home_post_last_id = OmnioPostsList.get(OmnioPostsList.size()-1).getPostid();
                            bedgeDAO.insertAll(bedgeEntity);


                                }
                            catch (Exception e){
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                        //DATABASE-----------------------------------------------------------------

                        postProgress.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
            postProgress.setVisibility(View.VISIBLE);
        }
        void loop(DataSnapshot snapshot,String xy,final int[] iterator)
        {


            for(DataSnapshot ss : snapshot.getChildren()) {
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
                if(groupid.equals("none")){
                    if(searchingTrigger)
                    {
                        if(category.toLowerCase().equals(searchableData.toLowerCase()))
                        {
                            OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                            iterator[0]++;
                            if(iterator[0] == limiterVar)
                            {
                                break;
                            }
                        }
                    }
                    else
                    {
                        OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                        iterator[0] = iterator[0] + 1;
                        if(iterator[0] == limiterVar)
                        {
                            break;
                        }
                    }
                    // OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
                }
                else{

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(groupid).exists()){

                                if(snapshot.child(groupid).child(xy).exists()){
                                    if(snapshot.child(groupid).child(xy).child("isMember").getValue().toString().equals("true")){
                                        if(searchingTrigger)
                                        {
                                            if(category.toLowerCase().contains(searchableData.toLowerCase()))
                                            {
                                                OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                                iterator[0]++;
                                                if(iterator[0] == limiterVar)
                                                {
                                                    return;
                                                }

                                            }

                                        }
                                        else
                                        {
                                            OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                            iterator[0] = iterator[0] + 1;
                                            if(iterator[0] == limiterVar)
                                            {
                                                return;
                                            }
                                        }
                                        // OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }

    }

    private class GetPostsNext extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            if (!listView.canScrollVertically(1) && Integer.parseInt(params[1])==RecyclerView.SCROLL_STATE_IDLE) {


                databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
            databaseReference.orderByKey().startAfter(oldestPostId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //outerLoop:
                    final int[] iterator = new int[1];
                    iterator[0] = 0;

                    loop(snapshot, params[0], iterator);
                    //Toast.makeText(getContext(), String.valueOf(OmnioPostsList.size()), Toast.LENGTH_SHORT).show();
                    newsfeedAdapter.notifyDataSetChanged();
                    // Set layout manager to position the items

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
            postProgress.setVisibility(View.VISIBLE);
        }
        void loop(DataSnapshot snapshot,String xy,final int[] iterator)
        {


            for(DataSnapshot ss : snapshot.getChildren()) {
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
                if(groupid.equals("none")){
                    if(searchingTrigger)
                    {
                        if(category.toLowerCase().equals(searchableData.toLowerCase()))
                        {
                            OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                            iterator[0]++;
                            if(iterator[0] == limiterVar)
                            {
                                break;
                            }
                        }
                    }
                    else
                    {
                        OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                        iterator[0] = iterator[0] + 1;
                        if(iterator[0] == limiterVar)
                        {
                            break;
                        }
                    }
                    // OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
                }
                else{

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GroupSubs");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child(groupid).exists()){

                                if(snapshot.child(groupid).child(xy).exists()){
                                    if(snapshot.child(groupid).child(xy).child("isMember").getValue().toString().equals("true")){
                                        if(searchingTrigger)
                                        {
                                            if(category.toLowerCase().contains(searchableData.toLowerCase()))
                                            {
                                                OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                                iterator[0]++;
                                                if(iterator[0] == limiterVar)
                                                {
                                                    return;
                                                }

                                            }

                                        }
                                        else
                                        {
                                            OmnioPostsList.add(new OmnioPosts(caption, category, color, dataURL, datetimepost, isanalytics, isnft, tags, userid, usermail, usernamee, coverart, postid, groupid));
                                            iterator[0] = iterator[0] + 1;
                                            if(iterator[0] == limiterVar)
                                            {
                                                return;
                                            }
                                        }
                                        // OmnioPostsList.add(new OmnioPosts(caption,category,color,dataURL,datetimepost,isanalytics,isnft,tags,userid,usermail,usernamee,coverart,postid,groupid));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        }

    }



    private void  stopMusic(MediaPlayer player){

        if(getContext() != null){
            AudioManager.OnAudioFocusChangeListener focusChangeListener =
                    new AudioManager.OnAudioFocusChangeListener() {
                        public void onAudioFocusChange(int focusChange) {
                            AudioManager am = (AudioManager)
                                    getContext().getSystemService(AUDIO_SERVICE);
                            switch (focusChange) {

                                case
                                        (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                                    // Lower the volume while ducking.
                                    player.setVolume(0.2f, 0.2f);
                                    break;
                                case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                                    player.pause();
                                    break;

                                case (AudioManager.AUDIOFOCUS_LOSS):
                                    player.stop();
                                    break;

                                case (AudioManager.AUDIOFOCUS_GAIN):

                                    player.setVolume(1f, 1f);

                                    break;
                                default:
                                    break;
                            }
                        }
                    };

            AudioManager am = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);

// Request audio focus for playback
            int result = am.requestAudioFocus(focusChangeListener,
// Use the music stream.
                    AudioManager.STREAM_MUSIC,
// Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN);


            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                //player.setSource(video);
            }
        }


   }


    @Override
    public void onPause() {
        super.onPause();

//        BedgeDAO bedgeDAO = db.BedgeDAO();
//        bedgeEntities = bedgeDAO.getAll();
//        if(bedgeEntities.size()>0){
//            for (int i=0; i<OmnioPostsList.size();i++) {
//                if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                    int unreadPosts = OmnioPostsList.size()-i;
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//                else{
//                    int unreadPosts = OmnioPostsList.size();
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//            }
//        }


    }

    @Override
    public void onStop() {
        super.onStop();

//        BedgeDAO bedgeDAO = db.BedgeDAO();
//        bedgeEntities = bedgeDAO.getAll();
//        if(bedgeEntities.size()>0){
//            for (int i=0; i<OmnioPostsList.size();i++) {
//                if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                    int unreadPosts = OmnioPostsList.size()-i;
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//                else{
//                    int unreadPosts = OmnioPostsList.size();
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//            }
//        }

    }

    @Override
    public void onResume() {
        super.onResume();

//        BedgeDAO bedgeDAO = db.BedgeDAO();
//        bedgeEntities = bedgeDAO.getAll();
//        if(bedgeEntities.size()>0){
//            for (int i=0; i<OmnioPostsList.size();i++) {
//                if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                    int unreadPosts = OmnioPostsList.size()-i;
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//                else{
//                    int unreadPosts = OmnioPostsList.size();
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//            }
//        }

    }

    @Override
    public void onStart() {
        super.onStart();

//        BedgeDAO bedgeDAO = db.BedgeDAO();
//        bedgeEntities = bedgeDAO.getAll();
//        if(bedgeEntities.size()>0){
//            for (int i=0; i<OmnioPostsList.size();i++) {
//                if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
//                    int unreadPosts = OmnioPostsList.size()-i;
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//                else{
//                    int unreadPosts = OmnioPostsList.size();
//                    bedgeDAO.update_bedge(unreadPosts);
//                }
//            }
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BedgeDAO bedgeDAO = db.BedgeDAO();
        bedgeEntities = bedgeDAO.getAll();
        if(bedgeEntities.size()>0){
            for (int i=0; i<OmnioPostsList.size();i++) {
                if(bedgeEntities.get(0).home_post_last_id == OmnioPostsList.get(i).getPostid()){
                    int unreadPosts = OmnioPostsList.size()-i;
                    bedgeDAO.update_bedge(unreadPosts);
                }
                else{
                    int unreadPosts = OmnioPostsList.size();
                    bedgeDAO.update_bedge(unreadPosts);
                }
            }
        }

    }
}