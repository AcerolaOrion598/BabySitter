package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.djaphar.babysitter.Fragments.GalleryFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.GalleryPicture;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private GalleryFragment galleryFragment;
    private ArrayList<GalleryPicture> galleryPictures;

    public GalleryRecyclerViewAdapter(GalleryFragment galleryFragment, ArrayList<GalleryPicture> galleryPictures) {
        this.galleryFragment = galleryFragment;
        this.galleryPictures = galleryPictures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> galleryFragment.setClickedPicture(galleryPictures.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(galleryFragment).load(galleryPictures.get(position).getPhotoLink()).into(holder.galleryPicture);
    }

    @Override
    public int getItemCount() {
        return galleryPictures.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private ImageView galleryPicture;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_gallery);
            galleryPicture = itemView.findViewById(R.id.gallery_picture);
        }
    }
}
