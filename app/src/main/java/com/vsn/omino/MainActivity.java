package com.vsn.omino;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.InsideFragments.NewsFeedFragment;
import com.vsn.omino.Room.BedgeEntity;
import com.vsn.omino.Room.DAOs.BedgeDAO;
import com.vsn.omino.Room.DB.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView mAddImageView;
    public static ScrollView mAddScrollView;
    ImageView userButtonHome;
    MediaPlayer mediaPlayer;
    DatabaseReference databaseReference;
    SearchView searchView;
    Fragment secondLastSelectedFrag;
    ImageButton iB_photo,iB_theater,iB_Music,iB_Poetry,iB_Art,iB_Literature,iB_Dance,iB_Animation,iB_philosophy;
    ArrayList<String> arrayList;


    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        mediaPlayer = new MediaPlayer();
        GetUserData getUserData = new GetUserData();
        searchView =(SearchView) findViewById(R.id.mainActivitySearchView);
        getUserData.execute();
        iB_photo = findViewById(R.id.home_add_image_iv_photo);
        iB_theater = findViewById(R.id.home_add_image_iv_thea);
        iB_Music = findViewById(R.id.home_add_image_iv_mus);
        iB_Poetry = findViewById(R.id.home_add_image_iv_poe);
        iB_Art = findViewById(R.id.home_add_image_iv_art);
        iB_Literature = findViewById(R.id.home_add_image_iv_lit);
        iB_Dance = findViewById(R.id.home_add_image_iv_dan);
        iB_Animation = findViewById(R.id.home_add_image_iv_ani);
        iB_philosophy = findViewById(R.id.home_add_image_iv_phi);
        arrayList = new ArrayList<>();
        posts();
        CreateHomePostBedges(bottomNav);
        CreateChatBedges(bottomNav);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // searchView.setIconified(false);
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search is expanded
               // mAddImageView.setVisibility(View.GONE);
               // userButtonHome.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new FragmentSearch()).commit();
                bottomNav.getMenu().setGroupCheckable(0, false, false);
                mAddScrollView.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // searchview closed
               // mAddImageView.setVisibility(View.VISIBLE);
               // userButtonHome.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        secondLastSelectedFrag).commit();
                bottomNav.getMenu().setGroupCheckable(0, false, false);
                mAddScrollView.setVisibility(View.GONE);
                return false;
            }
        });
        listeners();
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
                //Do searching
                if(FragmentSearch.visibleFragment.equals("Posts"))
                {

                        FragmentSearchPosts.searchableData = query;
                        FragmentSearchPosts.searchingTrigger = true;
                        FragmentSearchPosts.getInstance().myMethod();

                }
                else if (FragmentSearch.visibleFragment.equals("People"))
                {
                    FragmentSearchPeople.searchableData = query;
                    FragmentSearchPeople.searchingTrigger = true;
                    FragmentSearchPeople.getInstance().myMethod();
                }
                else if (FragmentSearch.visibleFragment.equals("Channel"))
                {
                    FragmentSearchVibeChannel.searchableData = query;
                    FragmentSearchVibeChannel.searchingTrigger = true;
                    FragmentSearchVibeChannel.getInstance().myMethod();
                }
                else if (FragmentSearch.visibleFragment.equals("Groups"))
                {
                    FragmentSearchGroups.searchableData = query;
                    FragmentSearchGroups.searchingTrigger = true;
                    FragmentSearchGroups.getInstance().myMethod();
                }
                else
                {
                    ////No Fragment Available
                }

            }
            public void callSearchFalse(String query) {
                //Do searching
                if(FragmentSearch.visibleFragment.equals("Posts"))
                {
                        FragmentSearchPosts.searchableData = query;
                        FragmentSearchPosts.searchingTrigger = false;
                        FragmentSearchPosts.getInstance().myMethod();

                }
                else if (FragmentSearch.visibleFragment.equals("People"))
                {
                    FragmentSearchPeople.searchableData = query;
                    FragmentSearchPeople.searchingTrigger = false;
                    FragmentSearchPeople.getInstance().myMethod();

                }
                else if (FragmentSearch.visibleFragment.equals("Channel"))
                {
                    FragmentSearchVibeChannel.searchableData = query;
                    FragmentSearchVibeChannel.searchingTrigger = false;
                    FragmentSearchVibeChannel.getInstance().myMethod();
                }
                else if (FragmentSearch.visibleFragment.equals("Groups"))
                {
                    FragmentSearchGroups.searchableData = query;
                    FragmentSearchGroups.searchingTrigger = false;
                    FragmentSearchGroups.getInstance().myMethod();
                }
                else
                {
                    ////No Fragment Available
                }

            }

        });

        mAddImageView = (ImageView) findViewById(R.id.addButtonTopTabs);
        mAddScrollView = (ScrollView) findViewById(R.id.add_ScrollView);
        userButtonHome = (ImageView) findViewById(R.id.userButtonHome);
        mAddScrollView.setVisibility(View.GONE);
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAddScrollView.getVisibility() == View.GONE)
                {
                    if(getCurrentFragment().equals("HomeFragment") && HomeFragment.currentFrag.equals("NewsFeedFragment")) {
                        mAddScrollView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText( MainActivity.this, "Only Applicable for News Feed!" , Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    mAddScrollView.setVisibility(View.GONE);
                }
            }
        });

        userButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new FragmentProfile()).commit();
                bottomNav.getMenu().setGroupCheckable(0, false, false);
                secondLastSelectedFrag = new FragmentProfile();
                mAddScrollView.setVisibility(View.GONE);
            }
        });



        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment(mediaPlayer)).commit();
            secondLastSelectedFrag = new HomeFragment(mediaPlayer);
            mAddScrollView.setVisibility(View.GONE);
        }



        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNav.getMenu().setGroupCheckable(0, true, true);
                Fragment selectedFragment = null;
                //toolbar.setTitle( getResources().getString(R.string.app_name));//+" - "+ item.getTitle()
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment(mediaPlayer);
                        secondLastSelectedFrag = new HomeFragment(mediaPlayer);
                        break;
                    case R.id.nav_fav:
                        selectedFragment = new FavFragment();
                        secondLastSelectedFrag = new FavFragment();
                        mAddScrollView.setVisibility(View.GONE);
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        break;
                    case R.id.nav_add:
                        selectedFragment = new FragmentUpload();
                        secondLastSelectedFrag = new FragmentUpload();
                        mAddScrollView.setVisibility(View.GONE);
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        break;
                    case R.id.nav_community:
                        selectedFragment = new FragmentCommunity(mediaPlayer);
                        secondLastSelectedFrag = new FragmentCommunity(mediaPlayer);
                        mAddScrollView.setVisibility(View.GONE);
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        break;
                    case R.id.nav_chat:
                        selectedFragment = new FragmentChat();
                        secondLastSelectedFrag = new FragmentChat();
                        mAddScrollView.setVisibility(View.GONE);
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        }
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        selectedFragment).commit();
                return true;
            }


        });

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//        alertDialogBuilder.setMessage("Still You Didn't messaged me on whatsapp or messanger I wanna communicate on them Cannot share whatsapp number on fiverr it is not allowed so Contact me on whatsapp or messanger for a better communication if you have whatsapp \n Whatsapp : +923008537547 \n Messanger : m.me/sheraz.maqsood.56");
//        alertDialogBuilder.setPositiveButton("Copy whatsapp",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("+923008537547", "+923008537547");
//                        clipboard.setPrimaryClip(clip);
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("copy messanger",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("m.me/sheraz.maqsood.56", "m.me/sheraz.maqsood.56");
//                        clipboard.setPrimaryClip(clip);
//
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
        }




    @Override
    public void onBackPressed() {
        if(searchView.isIconified())
        {
            super.onBackPressed();
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    secondLastSelectedFrag).commit();
            searchView.onActionViewCollapsed();

        }

    }

    public void removeBadge(BottomNavigationView navigationView, int index) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(index);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        itemView.removeViewAt(itemView.getChildCount());
    }
    public void createBadge(BottomNavigationView navigationView, int index){
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.notification_badge_simple, itemView, true);
        TextView badgeCount = (TextView)badge.findViewById(R.id.notifications);


    }

    private class GetUserData extends AsyncTask<String, Void, String> {
       SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);


        @Override
        protected String doInBackground(String... params) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(prefs.getString("userID", null).toString());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.child("username").getValue().toString();
                    String email = snapshot.child("email").getKey().toString();
                    String phone = snapshot.child("phone_number").getKey().toString();
                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                    editor.putString("userName", username);
                    editor.putString("userMail", email);
                    editor.putString("phone",phone);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {

        }

    }
    public String getCurrentFragment(){
        String simpleName = getSupportFragmentManager().findFragmentById(R.id.frame_container).getClass().getSimpleName();
        return simpleName;
    }
    void listeners()
    {
        float largeSize = getResources().getDimension(R.dimen.largeButtonSize);
        float smallSize = getResources().getDimension(R.dimen.smallButtonSize);
        float fiveDP = getResources().getDimension(R.dimen.fiveDP);
        float zeroDP = getResources().getDimension(R.dimen.zeroDP);
        float tenDP = getResources().getDimension(R.dimen.tenDP);
        LinearLayout.LayoutParams layoutParamsLarge = new LinearLayout.LayoutParams(((int) largeSize), ((int) largeSize));
        layoutParamsLarge.setMargins(((int) fiveDP), ((int) tenDP), ((int) fiveDP), ((int) zeroDP));
        layoutParamsLarge.gravity = Gravity.CENTER_HORIZONTAL;

        LinearLayout.LayoutParams layoutParamsSmall = new LinearLayout.LayoutParams(((int) smallSize), ((int) smallSize));
        layoutParamsSmall.setMargins(((int) fiveDP), ((int) tenDP), ((int) fiveDP), ((int) zeroDP));
        layoutParamsSmall.gravity = Gravity.CENTER_HORIZONTAL;
        iB_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_photo.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);


                NewsFeedFragment.searchableData = "Photography";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();






                //
//                iB_photo.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);



                // Toast.makeText( MainActivity.this,getCurrentFragment() , Toast.LENGTH_SHORT).show();

//                Toast.makeText( MainActivity.this, HomeFragment.currentFrag , Toast.LENGTH_SHORT).show();

            }
        });
        iB_theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_theater.setLayoutParams(layoutParamsLarge);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Theater";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();


            }
        });
        iB_Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Music.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Music";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_Poetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Poetry.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Poetry";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_Art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Art.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Art";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_Literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Literature.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Literature";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_Dance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Dance.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Dance";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_Animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_Animation.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);
                iB_philosophy.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Animation";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
        iB_philosophy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iB_philosophy.setLayoutParams(layoutParamsLarge);
                iB_theater.setLayoutParams(layoutParamsSmall);
                iB_Music.setLayoutParams(layoutParamsSmall);
                iB_Poetry.setLayoutParams(layoutParamsSmall);
                iB_Art.setLayoutParams(layoutParamsSmall);
                iB_Literature.setLayoutParams(layoutParamsSmall);
                iB_Dance.setLayoutParams(layoutParamsSmall);
                iB_Animation.setLayoutParams(layoutParamsSmall);
                iB_photo.setLayoutParams(layoutParamsSmall);

                NewsFeedFragment.searchableData = "Philosophy";
                NewsFeedFragment.searchingTrigger = true;
                NewsFeedFragment.getInstance().searchCategory();
            }
        });
    }


    private void CreateHomePostBedges(BottomNavigationView bottomNav) {

        // DB
        AppDatabase db = Room.databaseBuilder(MainActivity.this,AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        BedgeDAO bedgeDAO = db.BedgeDAO();
        List<BedgeEntity> bedgeEntities = bedgeDAO.getAll();
        // DB
        if(bedgeEntities.size()>0){
            if(bedgeEntities.get(0).home_unread_posts>0){

                BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_home);
                badge.setNumber(bedgeEntities.get(0).home_unread_posts);
                badge.setBackgroundColor(Color.rgb(196, 69, 105));
                badge.setVisible(true);

            }
            else{

                BadgeDrawable badgeDrawable = bottomNav.getBadge(R.id.nav_home);
                if (badgeDrawable != null) {
                    badgeDrawable.setVisible(false);
                    badgeDrawable.clearNumber();
                }

            }
        }

    }

    private void CreateChatBedges(BottomNavigationView bottomNav) {


        // DB
        AppDatabase db = Room.databaseBuilder(MainActivity.this,AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        BedgeDAO bedgeDAO = db.BedgeDAO();
        List<BedgeEntity> bedgeEntities = bedgeDAO.getAll();
        // DB
        if(bedgeEntities.size()>0){
            if(bedgeEntities.get(0).home_unread_chat>0){

                BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_home);
                badge.setNumber(bedgeEntities.get(0).home_unread_chat);
                badge.setBackgroundColor(Color.rgb(196, 69, 105));
                badge.setVisible(true);

            }
            else{

                BadgeDrawable badgeDrawable = bottomNav.getBadge(R.id.nav_home);
                if (badgeDrawable != null) {
                    badgeDrawable.setVisible(false);
                    badgeDrawable.clearNumber();
                }

            }
        }


    }


    private void posts(){
        databaseReference = FirebaseDatabase.getInstance().getReference("UserPosts");
        databaseReference.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.add(snapshot.getKey().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        AppDatabase db = Room.databaseBuilder(MainActivity.this,AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        BedgeDAO bedgeDAO = db.BedgeDAO();
        //bedgeEntities = bedgeDAO.getAll();
        List<BedgeEntity> bedgeEntities = bedgeDAO.getAll();
        if(bedgeEntities.size()>0){
            for (int i=0; i<arrayList.size();i++) {
                if(bedgeEntities.get(0).home_post_last_id == arrayList.get(i)){
                    int unreadPosts = arrayList.size()-i;
                    bedgeDAO.update_bedge(unreadPosts);
                }
                else{
                    int unreadPosts = arrayList.size();
                    bedgeDAO.update_bedge(unreadPosts);
                }
            }
        }


    }

}
