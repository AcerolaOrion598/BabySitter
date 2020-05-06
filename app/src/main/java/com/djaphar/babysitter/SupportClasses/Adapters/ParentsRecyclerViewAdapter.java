package com.djaphar.babysitter.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djaphar.babysitter.Fragments.ChildrenFragment;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Parent;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParentsRecyclerViewAdapter extends RecyclerView.Adapter<ParentsRecyclerViewAdapter.ViewHolder> {

    private Child child;
    private ArrayList<Parent> parents;
    private ChildrenFragment childrenFragment;

    public ParentsRecyclerViewAdapter(Child child, ChildrenFragment childrenFragment) {
        this.child = child;
        this.parents = child.getParents();
        this.childrenFragment = childrenFragment;
//        if (getItemCount() == 0) {
//            parents.add(new Parent("123456", null, null, "Инвайт", null, null));
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        final Context context = parentViewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.parents_list, parentViewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.parentLayout.setOnClickListener(lView -> childrenFragment.showParentInfo(parents.get(viewHolder.getAdapterPosition()), child));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parent parent = parents.get(position);
        holder.parentTitle.setText(parent.getRelationDegree());
        String fullName = parent.getName();
        if (parent.getPatronymic() != null) {
            fullName += " " + parent.getPatronymic() + " " + parent.getSurname();
        }
        holder.parentContent.setText(fullName);
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentLayout;
        private TextView parentTitle, parentContent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_parents);
            parentTitle = itemView.findViewById(R.id.parent_title);
            parentContent = itemView.findViewById(R.id.parent_content);
        }
    }
}
