package com.vsn.omino;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class FragmentSearch extends Fragment {
    TabLayout tabLayout;
    public static String visibleFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.sql_tabLayout);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_main,
                new FragmentSearchPosts()).commit();
        visibleFragment ="Posts";
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                if (tab.getPosition() == 0) {
                    selectedFragment = new FragmentSearchPosts();
                    visibleFragment ="Posts";

                } else if (tab.getPosition() == 1) {
                    selectedFragment = new FragmentSearchPeople();
                    visibleFragment ="People";
                } else if (tab.getPosition() == 2) {
                    selectedFragment = new FragmentSearchVibeChannel();
                    visibleFragment ="Channel";
                }
                else if (tab.getPosition() == 3) {
                    selectedFragment = new FragmentSearchGroups();
                    visibleFragment ="Groups";
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