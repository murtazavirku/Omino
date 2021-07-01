package com.vsn.omino.InsideFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.ExploreGridAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.Explore;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ExploreFragment extends Fragment {

    GridView gridView;
    List<Explore> list;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        gridView = (GridView)view.findViewById(R.id.ExploreGridView);
        list = new ArrayList<>();
        loadAll();


        return view;
    }


    private void loadAll(){

        reference = FirebaseDatabase.getInstance().getReference("VibeChannels");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snapshot2.getChildren()) {
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
                                        list.add(arrayList.get(i));
                                    }
                                }
                                ExploreGridAdapter gridAdapter = new ExploreGridAdapter(getContext(), list);
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

}