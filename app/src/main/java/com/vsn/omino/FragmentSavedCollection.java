package com.vsn.omino;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.SavedCollectionAdapter;
import com.vsn.omino.activites.AddVibeChannel;
import com.vsn.omino.activites.VideoSpecificPost;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSavedCollection extends Fragment {
    private RecyclerView recyclerView;
    SavedCollectionAdapter savedCollectionAdapter;
    List<SavedCollectionModel> modelList;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    LinearLayout add_vibeChannel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_collection, container, false);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.SavedRecyclerView);
        add_vibeChannel = (LinearLayout)view.findViewById(R.id.add_vibeChannel);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        modelList = new ArrayList<>();
        modelList.clear();
        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"FragmentSavedCollection");
        LoadAll();

        add_vibeChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AddVibeChannel.class);
                    getContext().startActivity(intent);
            }
        });





        return view;
    }

















    private void LoadAll() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SavedCollection");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SavedCollectionModel> arrayList = new ArrayList<>();
                if(snapshot.child(prefs.getString("userID", null).toString()).exists()){
                    if(snapshot.child(prefs.getString("userID", null).toString()).child("ImageCollections").exists()){
                        for (DataSnapshot s1 : snapshot.child(prefs.getString("userID", null).toString()).child("ImageCollections").getChildren()) {
                            //arrayList.add(new SavedCollectionModel(s1.child("DataUrl").getValue().toString(),0));
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ImagesPostImages");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child(s1.child("Postid").getValue().toString()).exists()){
                                        for (DataSnapshot s2 : snapshot.child(s1.child("Postid").getValue().toString()).getChildren()){
                                            arrayList.add(new SavedCollectionModel(s2.child("ImageLink").getValue().toString(),"img",0,"none"));

                                        }
                                        for(int i=0; i<arrayList.size();i++){
                                            if(modelList.contains(arrayList.get(i))){

                                            }
                                            else{
                                                modelList.add(arrayList.get(i));
                                            }
                                        }
                                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"FragmentSavedCollection");
                                        recyclerView.setAdapter(savedCollectionAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
//                for(int i =0; i<arrayList.size();i++){
//                    modelList.add(arrayList.get(i));
//                }
//                Toast.makeText(getContext(), String.valueOf(arrayList.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });




        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("SavedCollection");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SavedCollectionModel> arrayList = new ArrayList<>();
                if(snapshot.child(prefs.getString("userID", null).toString()).exists()){
                    if(snapshot.child(prefs.getString("userID", null).toString()).child("MusicCollections").exists()){
                        for (DataSnapshot s1 : snapshot.child(prefs.getString("userID", null).toString()).child("MusicCollections").getChildren()) {
                            arrayList.add(new SavedCollectionModel(s1.child("DataUrl").getValue().toString(),"msc",0,s1.child("Cover").getValue().toString()));
                        }
                        for(int i=0; i<arrayList.size();i++){
                            if(modelList.contains(arrayList.get(i))){

                            }
                            else{
                                modelList.add(arrayList.get(i));
                            }
                        }
                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"FragmentSavedCollection");
                        recyclerView.setAdapter(savedCollectionAdapter);
                    }
                }
//                modelList.addAll(arrayList);
//                modelList.addAll(savedCollectionAdapter.getAllitemsList());
//                savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList);
//                recyclerView.setAdapter(savedCollectionAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });



        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("SavedCollection");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<SavedCollectionModel> arrayList = new ArrayList<>();
                if(snapshot.child(prefs.getString("userID", null).toString()).exists()){
                    if(snapshot.child(prefs.getString("userID", null).toString()).child("VideoCollections").exists()){
                        for (DataSnapshot s1 : snapshot.child(prefs.getString("userID", null).toString()).child("VideoCollections").getChildren()) {
                            arrayList.add(new SavedCollectionModel(s1.child("DataUrl").getValue().toString(),"mp4",0,s1.child("Cover").getValue().toString()));
                        }
                        for(int i=0; i<arrayList.size();i++){
                            if(modelList.contains(arrayList.get(i))){

                            }
                            else{
                                modelList.add(arrayList.get(i));
                            }
                        }
                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"FragmentSavedCollection");
                        recyclerView.setAdapter(savedCollectionAdapter);
                    }
                }
//                modelList.addAll(arrayList);
//                modelList.addAll(savedCollectionAdapter.getAllitemsList());
//                savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList);
//                recyclerView.setAdapter(savedCollectionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}