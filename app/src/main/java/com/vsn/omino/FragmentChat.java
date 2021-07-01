package com.vsn.omino;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.TextUtils;
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
import com.vsn.omino.Adapters.ChatListAdapter;
import com.vsn.omino.Adapters.GroupsAdapter;
import com.vsn.omino.Room.BedgeEntity;
import com.vsn.omino.Room.DAOs.BedgeDAO;
import com.vsn.omino.Room.DB.AppDatabase;
import com.vsn.omino.models.ChatListModel;
import com.vsn.omino.models.Groups;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentChat extends Fragment {

    RecyclerView recyclerView;
    public List<ChatListModel> chatList;
    ChatListAdapter chatListAdapter;
    DatabaseReference databaseReference;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    SearchView searchView;
    int unread_msgs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_list);
        chatList = new ArrayList<>();
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        LoadMyChatList();
        unread_msgs = 0;

        searchView = (SearchView)view.findViewById(R.id.SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // searchview closed
                // mAddImageView.setVisibility(View.VISIBLE);
                // userButtonHome.setVisibility(View.VISIBLE);
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
//                        secondLastSelectedFrag).commit();
//                bottomNav.getMenu().setGroupCheckable(0, false, false);
//                mAddScrollView.setVisibility(View.GONE);
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

                    chatListAdapter = new ChatListAdapter(getContext(), chatList, query, true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(chatListAdapter);

//
            }
            public void callSearchFalse(String query) {
                    chatListAdapter = new ChatListAdapter(getContext(), chatList, "", false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(chatListAdapter);

            }

        });




        return view;
    }









    private void LoadMyChatList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatLists").child(prefs.getString("userID",null));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot frontUser : snapshot.getChildren()){

                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ChatMessages");
                        databaseRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot msg : snapshot.getChildren()) {
                                        if(msg.child("Reciver").getValue().toString().equals(prefs.getString("userID",null))){
                                            if(msg.child("isSeen").equals("false")){
                                                unread_msgs++;
                                            }
                                        }
                                }

                                String NewMassage = String.valueOf(unread_msgs);
                                String LastMessage = frontUser.child("LastMessage").getValue().toString();
                                String FrontUserID = frontUser.child("FrontUserID").getValue().toString();
                                Boolean isBadge = false;
                                if(unread_msgs>0){
                                    isBadge = true;
                                }
                                else{
                                    isBadge = false;
                                }
                                int unreadChat = 0;
                                chatList.add(new ChatListModel(LastMessage,NewMassage,FrontUserID,isBadge));
                                AppDatabase db = Room.databaseBuilder(getContext(),AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
                                BedgeDAO bedgeDAO = db.BedgeDAO();
                                List<BedgeEntity> bedgeEntities = bedgeDAO.getAll();
                                if(bedgeEntities.size()>0){
                                    for (int i=0; i<chatList.size();i++) {
                                        if(chatList.get(i).getIsBadgeVisible()){
                                            unreadChat ++;
                                            bedgeDAO.update_bedge_chat(unreadChat);
                                        }
                                    }
                                }

                                chatListAdapter = new ChatListAdapter(getContext(),chatList,"",false);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(chatListAdapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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