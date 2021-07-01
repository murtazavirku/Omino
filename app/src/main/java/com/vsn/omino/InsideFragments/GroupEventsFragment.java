package com.vsn.omino.InsideFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.EventsAdapter;
import com.vsn.omino.Adapters.GroupsAdapter;
import com.vsn.omino.R;
import com.vsn.omino.activites.AddGroup;
import com.vsn.omino.models.Events;
import com.vsn.omino.models.Groups;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GroupEventsFragment extends Fragment {
    RecyclerView recyclerView;
    List<Events> eventList;
    EventsAdapter eventAdapter;
    DatabaseReference databaseReference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_group_events, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        LoadMyEvents();

        return view;
    }

    private void LoadMyEvents() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                ArrayList<Events> arrayList = new ArrayList<>();
                for (DataSnapshot usersSnap : snapshot.getChildren()){
                    //if(snapshot.child(prefs.getString("userID", null).toString()).exists()){
                    for(DataSnapshot snapshot1 : usersSnap.getChildren())
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Events");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(snapshot1.getKey().toString()).exists()){
                                    for(DataSnapshot eventsnap : snapshot.child(snapshot1.getKey().toString()).getChildren()){
                                        String Background = eventsnap.child("Background").getValue().toString();
                                        String EventDate = eventsnap.child("EventDate").getValue().toString();
                                        String EventID = eventsnap.child("EventID").getValue().toString();
                                        String EventName = eventsnap.child("EventName").getValue().toString();
                                        String GroupId = snapshot.child(snapshot1.getKey().toString()).toString();
                                        arrayList.add(new Events(Background, EventName, EventDate, EventID, GroupId));

                                    }
                                    for(int i=0; i<arrayList.size();i++){
                                        if(eventList.contains(arrayList.get(i))){

                                        }
                                        else{
                                            eventList.add(arrayList.get(i));
                                        }
                                    }
                                    eventAdapter = new EventsAdapter(getContext(),eventList);
                                    recyclerView.setAdapter(eventAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}