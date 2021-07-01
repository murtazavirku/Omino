package com.vsn.omino.InsideFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.ExploreGridAdapter;
import com.vsn.omino.R;
import com.vsn.omino.models.Explore;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ExploreSavedFragment extends Fragment {

    GridView gridView;
    List<Explore> list;
    DatabaseReference reference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_saved, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        gridView = (GridView)view.findViewById(R.id.ExploreGridView);
        list = new ArrayList<>();
        loadAll();
        return view;
    }

    private void loadAll() {

        reference = FirebaseDatabase.getInstance().getReference("SavedVibeChannel").child(prefs.getString("userID",null).toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                ArrayList<Explore> arrayList = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("VibeChannels");
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot userSnap : snapshot.getChildren()){
                                if(userSnap.child(snapshot1.getKey()).exists()){
                                    String Cover = userSnap.child(snapshot1.getKey()).child("ChannelCover").getValue().toString();
                                    String ID = userSnap.child(snapshot1.getKey()).child("ChannelID").getValue().toString();
                                    String Name = userSnap.child(snapshot1.getKey()).child("ChannelName").getValue().toString();
                                    String Userid = userSnap.child(snapshot1.getKey()).child("userID").getValue().toString();
                                    arrayList.add(new Explore(ID,Name, Cover,Userid,true));
                                }
                            }
                            for(int i=0; i<arrayList.size();i++){
                                if(list.contains(arrayList.get(i))){

                                }
                                else{
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}