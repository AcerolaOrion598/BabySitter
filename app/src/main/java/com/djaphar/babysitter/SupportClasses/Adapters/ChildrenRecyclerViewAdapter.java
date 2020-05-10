package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.djaphar.babysitter.Fragments.ChildrenFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChildrenRecyclerViewAdapter extends RecyclerView.Adapter<ChildrenRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Child> children;
    private ChildrenFragment childrenFragment;

    public ChildrenRecyclerViewAdapter(ArrayList<Child> children, ChildrenFragment childrenFragment) {
        this.children = children;
        this.childrenFragment = childrenFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.children_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> childrenFragment.showKidInfo(children.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child child = children.get(position);
        Glide.with(childrenFragment)
                .applyDefaultRequestOptions(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .load(child.getPhotoLink())
                .into(holder.childrenListKidPhoto);
        String fullName = child.getName() + " " + child.getSurname();
        holder.kidFullName.setText(fullName);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView kidFullName;
        CircleImageView childrenListKidPhoto;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_children);
            kidFullName = itemView.findViewById(R.id.kid_full_name);
            childrenListKidPhoto = itemView.findViewById(R.id.children_list_kid_photo);
        }
    }
}
