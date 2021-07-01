package com.vsn.omino.InsideFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.GroupsAdapter;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.FragmentSearch;
import com.vsn.omino.FragmentSearchPeople;
import com.vsn.omino.FragmentSearchPosts;
import com.vsn.omino.FragmentSearchVibeChannel;
import com.vsn.omino.R;
import com.vsn.omino.activites.AddGroup;
import com.vsn.omino.activites.GroupHomePage;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.utiles.CheckInternet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GroupsFragment extends Fragment {

    MediaPlayer mediaPlayer;

    RecyclerView recyclerView;
    List<Groups> groupsList;
    GroupsAdapter groupsAdapter;
    DatabaseReference databaseReference;
    LinearLayout add_group;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SearchView searchView;
    String searchableData;
    boolean searchingTrigger;
    EditText editText;
    public GroupsFragment(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.group_list);
        searchView = (SearchView)view.findViewById(R.id.SearchView);
        //searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>Search</font>"));

        searchableData ="";
        searchingTrigger = false;


        groupsList = new ArrayList<>();
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        add_group = (LinearLayout)view.findViewById(R.id.add_group);
        add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), AddGroup.class));

            }
        });

        LoadMyGroups("");

//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                LoadMyGroups("");
                searchingTrigger = false;

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    if(newText.isEmpty()) {
                        callSearchFalse(newText);
                    }
                }
                return true;
            }

            public void callSearch(String query) {
                //Do searching

                    LoadMyGroups(query);
                    searchingTrigger = true;


            }
            public void callSearchFalse(String query) {
                //Do searching

                    LoadMyGroups(query);
                    searchingTrigger = false;


            }

        });



        return view;
    }

    private void LoadMyGroups(String searchString) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupsList.clear();

                for (DataSnapshot usersSnap : snapshot.getChildren()){
                    //if(snapshot.child(prefs.getString("userID", null).toString()).exists()){
                        for(DataSnapshot snapshot1 : usersSnap.getChildren())
                        {
                            String GroupIcon = snapshot1.child("GroupIcon").getValue().toString();
                            String GroupName = snapshot1.child("GroupName").getValue().toString();
                            String GroupDesc = snapshot1.child("GroupDesc").getValue().toString();
                            String GroupID = snapshot1.child("GroupID").getValue().toString();
                            String GroupSubs = snapshot1.child("Subscribers").getValue().toString();
                            if(searchingTrigger)
                            {
                                if(GroupName.toLowerCase().contains(searchString.toLowerCase()))
                                {
                                    groupsList.add(new Groups(GroupIcon, GroupName, GroupSubs, "0", false,GroupID,GroupDesc));
                                }
                            }
                            else
                            {
                                groupsList.add(new Groups(GroupIcon, GroupName, GroupSubs, "0", false,GroupID,GroupDesc));

                            }

                        }
                    //}
                }
                groupsAdapter = new GroupsAdapter(getContext(),groupsList,mediaPlayer);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(groupsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}