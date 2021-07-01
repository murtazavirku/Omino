package com.vsn.omino;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.vsn.omino.InsideFragments.GroupEventsFragment;
import com.vsn.omino.InsideFragments.GroupsFragment;
import com.vsn.omino.InsideFragments.NewsFeedFragment;
import com.vsn.omino.InsideFragments.SubsFragment;

public class FragmentCommunity extends Fragment {

    TabLayout tabLayout;
    MediaPlayer mediaPlayer;


    public FragmentCommunity(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.sql_tabLayout);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_main,
                new GroupsFragment(mediaPlayer)).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                if (tab.getPosition() == 0) {
                    selectedFragment = new GroupsFragment(mediaPlayer);

                } else if (tab.getPosition() == 1) {
                    selectedFragment = new GroupEventsFragment();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_main,
                        selectedFragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });


        return view;
    }
}