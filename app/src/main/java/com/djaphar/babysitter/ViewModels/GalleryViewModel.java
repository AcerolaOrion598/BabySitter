package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.GalleryPicture;
import com.djaphar.babysitter.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitter.SupportClasses.ApiClasses.UpdatePictureModel;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<ArrayList<GalleryPicture>> galleryPicturesMutableLiveData = new MutableLiveData<>();
    private MainApi mainApi;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<GalleryPicture>> getGalleryPictures() {
        return galleryPicturesMutableLiveData;
    }

    public void requestMyGallery(HashMap<String, String> headersMap) {
        mainApi.requestMyGallery(headersMap).enqueue(new Callback<ArrayList<GalleryPicture>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<GalleryPicture>> call, @NonNull Response<ArrayList<GalleryPicture>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                galleryPicturesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<GalleryPicture>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestAddGalleryPicture(HashMap<String, String> headersMap, UpdatePictureModel updatePictureModel) {
        mainApi.requestAddGalleryPicture(headersMap, updatePictureModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestMyGallery(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeleteGalleryPicture(HashMap<String, String> headersMap, String galleryId) {
        mainApi.requestDeleteGalleryPicture(headersMap, galleryId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestMyGallery(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
