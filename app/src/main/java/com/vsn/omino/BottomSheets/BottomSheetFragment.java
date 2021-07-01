package com.vsn.omino.BottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vsn.omino.Adapters.SavedCollectionAdapter;
import com.vsn.omino.R;
import com.vsn.omino.activites.AddVibeChannel;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    public ImageView cancleBS,doneBS;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    private BottomSheetBehavior mBehavior;
    RecyclerView recyclerView;
    SavedCollectionAdapter savedCollectionAdapter;
    List<SavedCollectionModel> modelList,selectedList;

    AddVibeChannel addVibeChannel;

    public BottomSheetFragment(AddVibeChannel addVibeChannel) {
        this.addVibeChannel = addVibeChannel;
    }
    public BottomSheetFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(bundle);
        View inflate = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        bottomSheetDialog.setContentView(inflate);
        prefs = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        cancleBS = (ImageView)inflate.findViewById(R.id.cancleBS);
        doneBS = (ImageView)inflate.findViewById(R.id.doneBS);
        recyclerView = (RecyclerView)inflate.findViewById(R.id.SavedRecyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        BottomSheetBehavior from = BottomSheetBehavior.from((View) inflate.getParent());
        this.mBehavior = from;
        from.setPeekHeight(-1);
        modelList = new ArrayList<>();
        selectedList = new ArrayList<>();
        modelList.clear();
        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"BottomSheetFragment");
        cancleBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment.this.dismiss();
            }
        });
        doneBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedList = savedCollectionAdapter.getAllselecteditemsList();
                if(selectedList.size()>0){
                    addVibeChannel.SelectContent.setText("Files ("+selectedList.size()+")");
                    BottomSheetFragment.this.dismiss();
                }
                else{
                    Toast.makeText(getContext(), "Select at Least 1 item", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getContext(), String.valueOf(selectedList.size()), Toast.LENGTH_SHORT).show();
            }
        });

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                       selectedList = savedCollectionAdapter.getAllitemsList();
//                        Toast.makeText(getContext(), selectedList.get(0).getImageUrl(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
////                        int itemPosition = recyclerView.getChildLayoutPosition(view);
////                        String item = modelList.get(itemPosition).getImageUrl().toString();
////
////                        Toast.makeText(getContext(), item, Toast.LENGTH_LONG).show();
//                    }
//                })
//        );




        LoadAll();
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList);
//                recyclerView.setAdapter(savedCollectionAdapter);
//            }
//        }, 5000);
        return bottomSheetDialog;
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
                                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"BottomSheetFragment");
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
                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"BottomSheetFragment");
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

                        savedCollectionAdapter = new SavedCollectionAdapter(getContext(),modelList,"BottomSheetFragment");
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

    public List<SavedCollectionModel> getAllselecteditemsList(){
        return selectedList;
    }
}
class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

        public void onLongItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mListener != null) {
                    mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    @Override
    public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
}