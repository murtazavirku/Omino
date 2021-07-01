package com.vsn.omino;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.GroupsAdapter;
import com.vsn.omino.Adapters.SearchVibeChannelAdapter;
import com.vsn.omino.models.Explore;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSearchGroups extends Fragment {

    RecyclerView gridView;
    List<Groups> list;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    public static String searchableData;
    public static boolean searchingTrigger;
    private int limiterVar;
    private static FragmentSearchGroups instance;
    GroupsAdapter gridAdapter;


    public static FragmentSearchGroups getInstance() {
        return instance;
    }

    public void myMethod() {
        list.clear();
        loadWithSearchOneTime(0,searchableData);
        if(gridAdapter != null){
            gridAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_groups, container, false);

        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        gridView = (RecyclerView)view.findViewById(R.id.ExploreListView);
        list = new ArrayList<>();
//        gridAdapter = new GroupsAdapter(getContext(), list,null);
//        gridView.setAdapter(gridAdapter);

        searchableData ="";
        searchingTrigger = false;
        limiterVar = 10;
        instance = this;

        return view;
    }

    public void loadWithSearchOneTime(int newState,String searchString)
    {
        reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snapshot2.getChildren()) {
                        //oldestPostIdChild = snapshot1.getKey();
                        // oldestPostId = snapshot2.getKey();
                        String GroupDesc = snapshot1.child("GroupDesc").getValue().toString();
                        String GroupID = snapshot1.child("GroupID").getValue().toString();
                        String GroupIcon = snapshot1.child("GroupIcon").getValue().toString();
                        String GroupName = snapshot1.child("GroupName").getValue().toString();
                        String Subscribers = snapshot1.child("Subscribers").getValue().toString();
                        if(searchingTrigger)
                        {
                            if(GroupName.toLowerCase().contains(searchString.toLowerCase()))
                            {
                                list.add(new Groups(GroupIcon, GroupName, Subscribers, "", null, GroupID, GroupDesc));


                            }
                        }
                    }

                }

                gridAdapter = new GroupsAdapter(getContext(), list,new MediaPlayer());
                gridView.setAdapter(gridAdapter);

                //gridView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),String.valueOf(list.size()) , Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void triggeringFunction()
    {
        loadWithSearchOneTime(0,searchableData);
    }
}