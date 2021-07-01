package com.vsn.omino;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.ExploreGridAdapter;
import com.vsn.omino.Adapters.SearchUserAdapter;
import com.vsn.omino.Adapters.SearchVibeChannelAdapter;
import com.vsn.omino.models.Explore;
import com.vsn.omino.models.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSearchVibeChannel extends Fragment {

    GridView gridView;
    List<Explore> list;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    private String oldestPostId;
    public static String searchableData;
    public static boolean searchingTrigger;
    private int limiterVar;
    private static FragmentSearchVibeChannel instance;
    SearchVibeChannelAdapter gridAdapter;


    public static FragmentSearchVibeChannel getInstance() {
        return instance;
    }

    public void myMethod() {
        list.clear();
        loadWithSearchOneTime(0,searchableData);
       // Toast.makeText( getContext(),"Triggered" , Toast.LENGTH_SHORT).show();
        gridAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_vibe_channel, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        gridView = (GridView)view.findViewById(R.id.ExploreGridView);
        list = new ArrayList<>();
        gridAdapter = new SearchVibeChannelAdapter(getContext(), list);
        gridView.setAdapter(gridAdapter);

        searchableData ="";
        searchingTrigger = false;
        limiterVar = 10;
        instance = this;

        //  loadAll();

      //  loadWithSearchOneTime(0,searchableData);

//        gridView.setOnScrollListener(new GridView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                loadWithSearchNext(i,searchableData);
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//
//            }
//
//        });


        return view;
    }



//    private void loadAll(){
//
//        reference = FirebaseDatabase.getInstance().getReference("VibeChannels");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for(DataSnapshot snapshot2 : snapshot.getChildren()){
//                    for(DataSnapshot snapshot1 : snapshot2.getChildren()) {
//                        String Cover = snapshot1.child("ChannelCover").getValue().toString();
//                        String ID = snapshot1.child("ChannelID").getValue().toString();
//                        String Name = snapshot1.child("ChannelName").getValue().toString();
//                        String Userid = snapshot1.child("userID").getValue().toString();
//                        DatabaseReference savedRef = FirebaseDatabase.getInstance().getReference("SavedVibeChannel");
//                        savedRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                ArrayList<Explore> arrayList = new ArrayList<>();
//                                if (snapshot.child(prefs.getString("userID", null).toString()).exists()) {
//                                    if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).exists()) {
//                                        if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).child("isSaved").getValue().toString().equals("yes")) {
//
//                                            arrayList.add(new Explore(ID, Name, Cover, Userid, true));
//                                        } else {
//                                            arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                        }
//                                    } else {
//                                        arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                    }
//                                } else {
//                                    arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                }
//                                for (int i = 0; i < arrayList.size(); i++) {
//                                    if (list.contains(arrayList.get(i))) {
//
//                                    } else {
//                                        list.add(arrayList.get(i));
//                                    }
//                                }
//                                gridAdapter = new SearchVibeChannelAdapter(getContext(), list);
//                                gridView.setAdapter(gridAdapter);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    public void loadWithSearchOneTime(int newState,String searchString)
    {
        reference = FirebaseDatabase.getInstance().getReference("VibeChannels");
        reference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snapshot2.getChildren()) {
                        //oldestPostIdChild = snapshot1.getKey();
                       // oldestPostId = snapshot2.getKey();
                        String Cover = snapshot1.child("ChannelCover").getValue().toString();
                        String ID = snapshot1.child("ChannelID").getValue().toString();
                        String Name = snapshot1.child("ChannelName").getValue().toString();
                        String Userid = snapshot1.child("userID").getValue().toString();
                        DatabaseReference savedRef = FirebaseDatabase.getInstance().getReference("SavedVibeChannel");
                        savedRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Explore> arrayList = new ArrayList<>();
                                if (snapshot.child(prefs.getString("userID", null).toString()).exists()) {
                                    if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).exists()) {
                                        if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).child("isSaved").getValue().toString().equals("yes")) {

                                            arrayList.add(new Explore(ID, Name, Cover, Userid, true));
                                        } else {
                                            arrayList.add(new Explore(ID, Name, Cover, Userid, false));
                                        }
                                    } else {
                                        arrayList.add(new Explore(ID, Name, Cover, Userid, false));
                                    }
                                } else {
                                    arrayList.add(new Explore(ID, Name, Cover, Userid, false));
                                }
                                for (int i = 0; i < arrayList.size(); i++) {
                                    if (list.contains(arrayList.get(i))) {

                                    } else {
                                        if(searchingTrigger)
                                        {
                                            if(Name.toLowerCase().contains(searchString.toLowerCase()))
                                            {
                                                list.add(arrayList.get(i));
//                                                iterator++;
//                                                if(iterator == limiterVar)
//                                                {
//                                                    break;
//                                                }
                                            }
                                        }

                                    }
                                }
                                gridAdapter = new SearchVibeChannelAdapter(getContext(), list);
                                gridView.setAdapter(gridAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
//    public void loadWithSearchNext(int newState,String searchString)
//    {
//
//        if (!gridView.canScrollVertically(1) && newState== 0) {
//            reference = FirebaseDatabase.getInstance().getReference("VibeChannels");
//            reference.orderByKey().startAfter(oldestPostId).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    list.clear();
//                    for(DataSnapshot snapshot2 : snapshot.getChildren()){
//                        for(DataSnapshot snapshot1 : snapshot2.getChildren()) {
//                            oldestPostIdChild = snapshot1.getKey();
//                            oldestPostId = snapshot2.getKey();
//                            String Cover = snapshot1.child("ChannelCover").getValue().toString();
//                            String ID = snapshot1.child("ChannelID").getValue().toString();
//                            String Name = snapshot1.child("ChannelName").getValue().toString();
//                            String Userid = snapshot1.child("userID").getValue().toString();
//                            DatabaseReference savedRef = FirebaseDatabase.getInstance().getReference("SavedVibeChannel");
//                            savedRef.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    ArrayList<Explore> arrayList = new ArrayList<>();
//                                    if (snapshot.child(prefs.getString("userID", null).toString()).exists()) {
//                                        if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).exists()) {
//                                            if (snapshot.child(prefs.getString("userID", null).toString()).child(ID).child("isSaved").getValue().toString().equals("yes")) {
//
//                                                arrayList.add(new Explore(ID, Name, Cover, Userid, true));
//                                            } else {
//                                                arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                            }
//                                        } else {
//                                            arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                        }
//                                    } else {
//                                        arrayList.add(new Explore(ID, Name, Cover, Userid, false));
//                                    }
//                                    for (int i = 0; i < arrayList.size(); i++) {
//                                        if (list.contains(arrayList.get(i))) {
//
//                                        } else {
//                                            if(searchingTrigger)
//                                            {
//                                                if(Name.contains(searchString))
//                                                {
//                                                    list.add(arrayList.get(i));
//                                                    iterator++;
//                                                    if(iterator == limiterVar)
//                                                    {
//                                                        break;
//                                                    }
//                                                }
//                                            }
//                                            else
//                                            {
//                                                list.add(arrayList.get(i));
//                                                iterator = iterator + 1;
//                                                if(iterator == limiterVar)
//                                                {
//                                                    break;
//                                                }
//                                            }
//
//
//                                        }
//                                    }
//                                    gridAdapter.notifyDataSetChanged();
//                                    Toast.makeText(getContext(), "called", Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            reference = FirebaseDatabase.getInstance().getReference("Users");
//            reference.orderByKey().startAfter(oldestPostId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    int iterator = 0;
//                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                        oldestPostId = snapshot1.getKey();
//
//                        String Email = snapshot1.child("email").getValue().toString();
//                        String Phone = snapshot1.child("phone_number").getValue().toString();
//                        String Userid = snapshot1.child("user_id").getValue().toString();
//                        String Name = snapshot1.child("username").getValue().toString();
//                        if(searchingTrigger)
//                        {
//                            if(Email.contains(searchString) || Name.contains(searchString))
//                            {
//                                list.add(new User(Userid,Long.parseLong(Phone),Email,Name) );
//                                iterator++;
//                                if(iterator == limiterVar)
//                                {
//                                    break;
//                                }
//                            }
//                        }
//                        else
//                        {
//                            list.add(new User(Userid,Long.parseLong(Phone),Email,Name) );
//                            iterator = iterator + 1;
//                            if(iterator == limiterVar)
//                            {
//                                break;
//                            }
//                        }
//
//                    }
//                    gridAdapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(), "called", Toast.LENGTH_SHORT).show();
////                    postProgress.setVisibility(View.GONE);
////                    listView.setVisibility(View.VISIBLE);
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }
    public void triggeringFunction()
    {
        loadWithSearchOneTime(0,searchableData);
    }
}