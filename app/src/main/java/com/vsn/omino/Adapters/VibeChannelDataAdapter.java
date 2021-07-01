package com.vsn.omino.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.models.SavedCollectionModel;
import com.vsn.omino.models.VibeChannelDataModel;

import java.util.ArrayList;
import java.util.List;

public class VibeChannelDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context ctx;
    private List<VibeChannelDataModel> items = new ArrayList();

    List<VibeChannelDataModel> selectedItem;
    ArrayList<Boolean> bool;
    public VibeChannelDataAdapter(Context ctx, List<VibeChannelDataModel> items) {
        this.ctx = ctx;
        this.items = items;
        selectedItem = new ArrayList<>();
        bool = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(ctx);

                View v1 = inflater.inflate(R.layout.saved_item_design, parent, false);
                viewHolder = new VibeChannelDataAdapter.SavedItemView(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VibeChannelDataAdapter.SavedItemView) {
            if(items.get(position).getCategory().equals("img")){
                ((SavedItemView) holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ((VibeChannelDataAdapter.SavedItemView) holder).play_btn.setVisibility(View.GONE);
                Picasso.get().load(items.get(position).getDataUrl()).into(((VibeChannelDataAdapter.SavedItemView) holder).image);
            }
            else{
                ((SavedItemView) holder).image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.get().load(items.get(position).getDataCover()).into(((VibeChannelDataAdapter.SavedItemView) holder).image);
            }

            ((VibeChannelDataAdapter.SavedItemView) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if(bool.get(position)){
//                        ((SavedCollectionAdapter.SavedItemView) holder).selected.setVisibility(View.VISIBLE);
//                        ((SavedCollectionAdapter.SavedItemView) holder).tikk.setVisibility(View.VISIBLE);
//                        bool.set(position,!bool.get(position));
//                        selectedItem.add(items.get(position));
//                    }
//                    else{
//                        ((SavedCollectionAdapter.SavedItemView) holder).selected.setVisibility(View.GONE);
//                        ((SavedCollectionAdapter.SavedItemView) holder).tikk.setVisibility(View.GONE);
//                        bool.set(position,!bool.get(position));
//                        selectedItem.remove(items.get(position));
//                    }
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



    public List<VibeChannelDataModel> getAllselecteditemsList(){

        return selectedItem;
    }



}
