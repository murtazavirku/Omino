package com.vsn.omino;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment {


    LinearLayout ll_profile_settings,ll_piece_collection,ll_saved_collection,ll_analytics;
    CircleImageView profile_photo;
    TextView display_name,description,tag,tvPostCount,tvSubCount,tvSubdCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profile_photo = (CircleImageView)view.findViewById(R.id.profile_photo);
        display_name = (TextView)view.findViewById(R.id.display_name);
        description = (TextView)view.findViewById(R.id.description);
        tvPostCount = (TextView)view.findViewById(R.id.tvPosts);
        tvSubCount = (TextView)view.findViewById(R.id.tvFollowers);
        tvSubdCount = (TextView)view.findViewById(R.id.tvFollowing);
        tag = (TextView)view.findViewById(R.id.tag);

        profileInfo();

        ll_profile_settings = (LinearLayout) view.findViewById(R.id.profile_Settings_ll);
        ll_profile_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAccountSettings.class);
                startActivity(intent);

            }
        });

        ll_piece_collection = (LinearLayout) view.findViewById(R.id.fp_ll_piececollection);
        ll_piece_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPieceCollection nextFrag= new FragmentPieceCollection();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        ll_saved_collection = (LinearLayout) view.findViewById(R.id.fp_ll_savedcollection);
        ll_saved_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSavedCollection nextFrag= new FragmentSavedCollection();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag, "findThisFragment1")
                        .addToBackStack(null)
                        .commit();
            }
        });
        ll_analytics = (LinearLayout) view.findViewById(R.id.fp_ll_analytics);
        ll_analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAnalytics nextFrag= new FragmentAnalytics();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag, "findThisFragment2")
                        .addToBackStack(null)
                        .commit();
            }
        });

        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile1);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAccountSettings.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void profileInfo() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserAccountSettings").child(prefs.getString("userID", null).toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String picUrl = snapshot.child("profile_photo").getValue().toString();
                String name = snapshot.child("username").getValue().toString();
                String desc = snapshot.child("description").getValue().toString();
                String tags = snapshot.child("tag").getValue().toString();
                String followers = snapshot.child("followers").getValue().toString();
                String following = snapshot.child("following").getValue().toString();
                String posts = snapshot.child("posts").getValue().toString();
                description.setText("");
                tvPostCount.setText(posts);
                tvSubCount.setText(followers);
                tvSubdCount.setText(following);
                String currentString = desc;
                String[] separated = currentString.split(" ");
                Picasso.get().load(picUrl).into(profile_photo);
                display_name.setText(name);
                for(int i =0; i<separated.length;i++){
                    if(description.getText().equals("")){
                        description.setText(separated[i].toString());
                    }
                    else{
                        description.setText(description.getText()+" | "+separated[i].toString());
                    }

                }

                tag.setText(tags);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}