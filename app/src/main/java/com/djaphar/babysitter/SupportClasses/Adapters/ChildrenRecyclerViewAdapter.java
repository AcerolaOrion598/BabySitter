package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitter.Fragments.ChildrenFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Kid;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChildrenRecyclerViewAdapter extends RecyclerView.Adapter<ChildrenRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Kid> kids;
    private ChildrenFragment childrenFragment;

    public ChildrenRecyclerViewAdapter(ArrayList<Kid> kids, ChildrenFragment childrenFragment) {
        this.kids = kids;
        this.childrenFragment = childrenFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.children_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> childrenFragment.showKidInfo(kids.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kid kid = kids.get(position);
        Glide.with(childrenFragment).load(kid.getPhotoUrl()).into(holder.childrenListKidPhoto);
        String fullName = kid.getName() + " " + kid.getSurname();
        holder.kidFullName.setText(fullName);
    }

    @Override
    public int getItemCount() {
        return kids.size();
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
