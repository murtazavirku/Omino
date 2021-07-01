package com.vsn.omino.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.vsn.omino.BottomSheets.BottomSheetFragment;
import com.vsn.omino.R;
import com.vsn.omino.models.SavedCollectionModel;

import java.util.ArrayList;
import java.util.List;

public class SavedCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<SavedCollectionModel> items = new ArrayList();

    List<SavedCollectionModel> selectedItem;
    ArrayList<Boolean> bool;
    String ActiName;
    public SavedCollectionAdapter(Context ctx, List<SavedCollectionModel> items,String ActiName) {
        this.ctx = ctx;
        this.items = items;
        selectedItem = new ArrayList<>();
        bool = new ArrayList<>();
        this.ActiName = ActiName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(ctx);

        switch (viewType) {
            case 0 :
                View v1 = inflater.inflate(R.layout.saved_item_design, parent, false);
                viewHolder = new SavedItemView(v1);
                //return new FeaturedViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_home_view, viewGroup, false));
                break;
            case 1 :
                View v2 = inflater.inflate(R.layout.sactioned_item_design, parent, false);
                viewHolder = new SectionedView(v2);
                //return new otherVidViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_home_other, viewGroup, false));
                break;
            default: return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SavedItemView) {
            if(items.get(position).getType().equals("img")){
                ((SavedItemView) holder).play_btn.setVisibility(View.GONE);
                ((SavedCollectionAdapter.SavedItemView) holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(items.get(position).getImageUrl()).into(((SavedItemView) holder).image);
            }
            else{
                ((SavedCollectionAdapter.SavedItemView) holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(items.get(position).getCover()).into(((SavedItemView) holder).image);
            }

            ((SavedItemView) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ActiName.equals("BottomSheetFragment")){
                        if(bool.get(position)){
                            ((SavedItemView) holder).selected.setVisibility(View.VISIBLE);
                            ((SavedItemView) holder).tikk.setVisibility(View.VISIBLE);
                            bool.set(position,!bool.get(position));
                            selectedItem.add(items.get(position));
                        }
                        else{
                            ((SavedItemView) holder).selected.setVisibility(View.GONE);
                            ((SavedItemView) holder).tikk.setVisibility(View.GONE);
                            bool.set(position,!bool.get(position));
                            selectedItem.remove(items.get(position));
                        }
                    }
                    else{

                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class SavedItemView extends RecyclerView.ViewHolder {
        public ImageView image;
        public View lyt_parent;
        public ImageView play_btn;
        public View selected;
        public ImageView tikk;

        public SavedItemView(@NonNull View itemView) {
            super(itemView);
            //if()
            image = (ImageView) itemView.findViewById(R.id.image);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);
            play_btn = (ImageView) itemView.findViewById(R.id.play_btn);
            selected = (View)itemView.findViewById(R.id.selected);
            tikk = (ImageView) itemView.findViewById(R.id.tikk);

            for(int i = 0; i<items.size();i++){
                bool.add(true);
            }

        }
    }

    public class SectionedView extends RecyclerView.ViewHolder {
        public TextView title_section;
        public SectionedView(@NonNull View itemView) {
            super(itemView);
            title_section = (TextView) itemView.findViewById(R.id.title_section);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getSaction();
    }


    public List<SavedCollectionModel> getAllselecteditemsList(){

        return selectedItem;
    }



}
