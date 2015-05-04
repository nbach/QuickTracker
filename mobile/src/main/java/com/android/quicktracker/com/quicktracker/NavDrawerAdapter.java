package com.android.quicktracker.com.quicktracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hp1 on 28-12-2014.
 */
public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.ViewHolder> {


    private static final int TYPE_ITEM = 1;
    private String mNavTitles[];
    private int mIcons[];





    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderID;
        TextView textView;
        ImageView imageView;
        TextView headerTextView;
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == 1){
                textView = (TextView) itemView.findViewById(R.id.rowTextView);
                imageView = (ImageView) itemView.findViewById(R.id.iconImageView);
                holderID=1;
            }
            else {
                headerTextView = (TextView) itemView.findViewById(R.id.navigationTextView);
                holderID=0;
            }
        }


    }

    NavDrawerAdapter(String Titles[], int Icons[]) {
        mNavTitles = Titles;
        mIcons = Icons;

    }

    @Override
    public NavDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        }
        else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        }
    }

    @Override
    public void onBindViewHolder(NavDrawerAdapter.ViewHolder holder, int position) {
        if (holder.holderID == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position - 1]);
        }
        else {
            holder.headerTextView.setText("Menu");
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }


}