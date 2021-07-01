package com.vsn.omino.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.vsn.omino.R;
import com.vsn.omino.models.OmnioPosts;

import java.util.List;

public class SubsAdapter extends BaseAdapter {

    private Context mContext;
    private List<OmnioPosts> list;

    public SubsAdapter(Context mContext, List<OmnioPosts> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getUserid();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View v = View.inflate(mContext, R.layout.omnio_subs_design,null);
        // image post components
        CardView ImageViewSubs = (CardView)v.findViewById(R.id.ImageViewSubs);
        TextView name = (TextView)v.findViewById(R.id.nameforsubs);
        TextView by = (TextView)v.findViewById(R.id.byforsubs);
        ImageView ImageData = (ImageView)v.findViewById(R.id.imageforsubs);

        if(list.get(position).getCategory().equals("Art") || list.get(position).getCategory().equals("Photography") ){

            name.setText(list.get(position).getCategory());
            by.setText("by "+list.get(position).getUsernamee());
            Picasso.get().load(list.get(position).getDataURL()).into(ImageData);
            ImageViewSubs.setVisibility(View.VISIBLE);

        }


        return v;
    }
}
