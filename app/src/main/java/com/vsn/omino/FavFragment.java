package com.vsn.omino;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.vsn.omino.InsideFragments.ExploreFragment;
import com.vsn.omino.InsideFragments.ExploreSavedFragment;
import com.vsn.omino.InsideFragments.NewsFeedFragment;

public class FavFragment extends Fragment {

    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.sql_tabLayout);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_fav,
                new ExploreFragment()).commit();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                if (tab.getPosition() == 0) {
                    selectedFragment = new ExploreFragment();

                } else if (tab.getPosition() == 1) {

                    selectedFragment = new ExploreSavedFragment();
                } else {

                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_for_fav,
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