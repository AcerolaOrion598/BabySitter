package com.djaphar.babysitter.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djaphar.babysitter.Activities.MainActivity;
import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.Adapters.GalleryRecyclerViewAdapter;
import com.djaphar.babysitter.SupportClasses.ApiClasses.GalleryPicture;
import com.djaphar.babysitter.SupportClasses.ApiClasses.UpdatePictureModel;
import com.djaphar.babysitter.SupportClasses.OtherClasses.MyFragment;
import com.djaphar.babysitter.ViewModels.GalleryViewModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryFragment extends MyFragment {

    private GalleryViewModel galleryViewModel;
    private MainActivity mainActivity;
    private Context context;
    private ConstraintLayout galleryContainer, picDescriptionContainer, picDescriptionAddContainer;
    private RelativeLayout galleryListLayout;
    private RecyclerView galleryRecyclerView;
    private ImageView singlePicture;
    private TextView picDescriptionTv;
    private EditText picDescriptionEd;
    private Button picDescriptionSaveBtn;
    private GalleryPicture picture;
    private HashMap<String, String> authHeader = new HashMap<>();
    private boolean addMode;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        galleryContainer = root.findViewById(R.id.gallery_container);
        picDescriptionContainer = root.findViewById(R.id.pic_description_container);
        picDescriptionAddContainer = root.findViewById(R.id.pic_description_add_container);
        galleryListLayout = root.findViewById(R.id.gallery_list_layout);
        galleryRecyclerView = root.findViewById(R.id.gallery_recycler_view);
        singlePicture = root.findViewById(R.id.single_picture);
        picDescriptionTv = root.findViewById(R.id.pic_description_tv);
        picDescriptionEd = root.findViewById(R.id.pic_description_ed);
        picDescriptionSaveBtn = root.findViewById(R.id.pic_description_save_btn);
        context = getContext();
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            setActionBarTitle(getString(R.string.gallery_title));
            setBackBtnState(View.GONE);
            setNewBtnState(View.VISIBLE);
            setDeleteBtnState(View.GONE);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        galleryViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                return;
            }
            authHeader.put(getString(R.string.auth_header_key), user.getToken_type() + " " + user.getAccess_token());
            galleryViewModel.requestMyGallery(authHeader);
        });

        galleryViewModel.getGalleryPictures().observe(getViewLifecycleOwner(), pictures -> {
            if (pictures == null) {
                return;
            }
            galleryRecyclerView.setAdapter(new GalleryRecyclerViewAdapter(this, pictures));
            galleryRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        });

        singlePicture.setOnClickListener(lView -> {
            if (addMode) {
                toggleContainer(picDescriptionAddContainer);
            } else {
                toggleContainer(picDescriptionContainer);
            }
        });

        picDescriptionSaveBtn.setOnClickListener(lView -> {
            new GalleryPictureUploader(((BitmapDrawable)singlePicture.getDrawable()).getBitmap(),
                    galleryViewModel, authHeader, picDescriptionEd.getText().toString()).execute();
            picDescriptionAddContainer.setVisibility(View.GONE);
            backWasPressed();
        });
    }

    private void setActionBarTitle(String title) {
        mainActivity.setActionBarTitle(title);
    }

    private void setBackBtnState(int visibilityState) {
        mainActivity.setBackBtnState(visibilityState);
    }

    private void setNewBtnState(int visibilityState) {
        mainActivity.setNewBtnState(visibilityState);
    }

    private void setDeleteBtnState(int visibilityState) {
        mainActivity.setDeleteBtnState(visibilityState);
    }

    public boolean everythingIsClosed() {
        return singlePicture.getVisibility() != View.VISIBLE;
    }

    public void backWasPressed() {
        if (picDescriptionAddContainer.getVisibility() == View.VISIBLE) {
            picDescriptionAddContainer.setVisibility(View.GONE);
            return;
        }
        if (picDescriptionContainer.getVisibility() == View.VISIBLE) {
            picDescriptionContainer.setVisibility(View.GONE);
            return;
        }
        galleryListLayout.setVisibility(View.VISIBLE);
        galleryContainer.setBackgroundColor(Color.TRANSPARENT);
        singlePicture.setVisibility(View.GONE);
        setBackBtnState(View.GONE);
        setNewBtnState(View.VISIBLE);
        setDeleteBtnState(View.GONE);
        picture = null;
    }

    public void newBtnWasPressed() {
        mainActivity.selectPicture();
    }

    public void deleteBtnWasPressed() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_picture_title)
                .setMessage(R.string.delete_picture_message)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    galleryViewModel.requestDeleteGalleryPicture(authHeader, picture.getGalleryId());
                    picDescriptionContainer.setVisibility(View.GONE);
                    backWasPressed();
                })
                .show();
    }

    public void setClickedPicture(GalleryPicture picture) {
        this.picture = picture;
        Glide.with(context).load(picture.getPhotoLink()).into(singlePicture);
        String description = picture.getDescription();
        if (description == null || description.equals("")) {
            description = getString(R.string.null_description);
        }
        picDescriptionTv.setText(description);
        showSinglePicture();
        setDeleteBtnState(View.VISIBLE);
        addMode = false;
    }

    public void setSelectedPicture(Uri selectedPictureUri) {
        singlePicture.setImageURI(selectedPictureUri);
        picDescriptionEd.setText("");
        picDescriptionAddContainer.setVisibility(View.VISIBLE);
        showSinglePicture();
        addMode = true;
    }

    private void showSinglePicture() {
        galleryListLayout.setVisibility(View.GONE);
        galleryContainer.setBackgroundColor(context.getResources().getColor(R.color.colorBlack87));
        singlePicture.setVisibility(View.VISIBLE);
        setBackBtnState(View.VISIBLE);
        setNewBtnState(View.GONE);
    }

    private void toggleContainer(ConstraintLayout container) {
        if (container.getVisibility() == View.VISIBLE) {
            container.setVisibility(View.GONE);
            return;
        }
        container.setVisibility(View.VISIBLE);
    }

    private static class GalleryPictureUploader extends AsyncTask<Void, Void, Void> {
        private Bitmap picture;
        private GalleryViewModel galleryViewModel;
        private HashMap<String, String> authHeader;
        private String description;

        GalleryPictureUploader(Bitmap picture, GalleryViewModel galleryViewModel, HashMap<String, String> authHeader, String description) {
            this.picture = picture;
            this.galleryViewModel = galleryViewModel;
            this.authHeader = authHeader;
            this.description = description;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            String pictureStr = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            galleryViewModel.requestAddGalleryPicture(authHeader, new UpdatePictureModel(null, null, pictureStr, description));
            return null;
        }
    }
}
