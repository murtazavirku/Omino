package com.vsn.omino.utiles;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserData {

    final ArrayList<String> userdata;


    public UserData() {
        userdata = new ArrayList<>();
    }

    public ArrayList<String> getProfileData (String UserID){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UserID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userdata.add(0,snapshot.child("email").getValue().toString());
                userdata.add(1,snapshot.child("phone_number").getValue().toString());
                userdata.add(2,snapshot.child("user_id").getValue().toString());
                userdata.add(3,snapshot.child("username").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userdata;

    }


}
