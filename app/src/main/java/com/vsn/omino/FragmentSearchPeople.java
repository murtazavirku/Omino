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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.NewsFeedAddapter;
import com.vsn.omino.Adapters.SearchUserAdapter;
import com.vsn.omino.Adapters.SearchVibeChannelAdapter;
import com.vsn.omino.models.Explore;
import com.vsn.omino.models.OmnioPosts;
import com.vsn.omino.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentSearchPeople extends Fragment {


    GridView gridView;
    List<User> list;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SearchUserAdapter gridAdapter;
    private String oldestPostId;
    public static String searchableData;
    public static boolean searchingTrigger;
    private int limiterVar;
    private static FragmentSearchPeople instance;


    public static FragmentSearchPeople getInstance() {
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
        View view = inflater.inflate(R.layout.fragment_search_people, container, false);


        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        gridView = (GridView)view.findViewById(R.id.ExploreGridView);
        list = new ArrayList<>();
        gridAdapter = new SearchUserAdapter(getContext(), list);
        gridView.setAdapter(gridAdapter);

        searchableData ="";
        searchingTrigger = false;
        limiterVar = 10;
        instance = this;

//        loadWithSearchOneTime(0,searchableData);

        gridView.setOnScrollListener(new GridView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                loadWithSearchNext(i,searchableData);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }

        });
        return view;
    }


    public void loadWithSearchOneTime(int newState,String searchString)
    {
        //if(checkInternet.checkInternetConnection(getContext())){
        //    noInternet.setVisibility(View.GONE);
            reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int iterator = 0;
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        oldestPostId = snapshot1.getKey();

                        String Email = snapshot1.child("email").getValue().toString();
                        String Phone = snapshot1.child("phone_number").getValue().toString();
                        String Userid = snapshot1.child("user_id").getValue().toString();
                        String Name = snapshot1.child("username").getValue().toString();
                        if(searchingTrigger)
                        {
                            if(Email.toLowerCase().contains(searchString.toLowerCase()) || Name.toLowerCase().contains(searchString.toLowerCase()))
                            {
                                list.add(new User(Userid,Phone,Email,Name) );
                                iterator++;
                                if(iterator == limiterVar)
                                {
                                    break;
                                }
                            }
                        }


                    }
                    gridAdapter = new SearchUserAdapter(getContext(), list);
                    gridView.setAdapter(gridAdapter);

//                    postProgress.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });


       // }
//        else{
//        //    listView.setVisibility(View.GONE);
//        //    postProgress.setVisibility(View.GONE);
//        //    noInternet.setVisibility(View.VISIBLE);
//        }

    }
    public void loadWithSearchNext(int newState,String searchString)
    {

            if (!gridView.canScrollVertically(1) && newState== 0) {
                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.orderByKey().startAfter(oldestPostId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int iterator = 0;
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            oldestPostId = snapshot1.getKey();

                            String Email = snapshot1.child("email").getValue().toString();
                            String Phone = snapshot1.child("phone_number").getValue().toString();
                            String Userid = snapshot1.child("user_id").getValue().toString();
                            String Name = snapshot1.child("username").getValue().toString();
                            if(searchingTrigger)
                            {
                                if(Email.toLowerCase().contains(searchString.toLowerCase()) || Name.toLowerCase().contains(searchString.toLowerCase()))
                                {
                                    list.add(new User(Userid,Phone,Email,Name) );
                                    iterator++;
                                    if(iterator == limiterVar)
                                    {
                                        break;
                                    }
                                }
                            }


                        }
                        gridAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "called", Toast.LENGTH_SHORT).show();
//                    postProgress.setVisibility(View.GONE);
//                    listView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
    }
    public void triggeringFunction()
    {
        loadWithSearchOneTime(0,searchableData);
    }
}