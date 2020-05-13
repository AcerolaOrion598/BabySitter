package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
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

public class ChildrenViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Child>> childrenMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Child> currentChildMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private MainApi mainApi;

    public ChildrenViewModel(@NonNull Application application) {
        super(application);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<Child>> getChildren() {
        return childrenMutableLiveData;
    }

    public MutableLiveData<Child> getCurrentChild() {
        return currentChildMutableLiveData;
    }

    public void requestChildrenList(HashMap<String, String> headersMap) {
        mainApi.requestChildrenList(headersMap).enqueue(new Callback<ArrayList<Child>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Child>> call, @NonNull Response<ArrayList<Child>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                childrenMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Child>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestSingleChild(HashMap<String, String> headersMap, String childId) {
        mainApi.requestSingleChild(headersMap, childId).enqueue(new Callback<Child>() {
            @Override
            public void onResponse(@NonNull Call<Child> call, @NonNull Response<Child> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        currentChildMutableLiveData.setValue(null);
                        return;
                    }
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                currentChildMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Child> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestCreateChild(HashMap<String, String> headersMap, Child child) {
        mainApi.requestCreateChild(headersMap, child).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestChildrenList(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdateChild(HashMap<String, String> headersMap, String childId, Child child) {
        mainApi.requestUpdateChild(headersMap, childId, child).enqueue(new Callback<Child>() {
            @Override
            public void onResponse(@NonNull Call<Child> call, @NonNull Response<Child> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                currentChildMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Child> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeleteChild(HashMap<String, String> headersMap, String childId) {
        mainApi.requestDeleteChild(headersMap, childId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestSingleChild(headersMap, childId);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdatePicture(HashMap<String, String> headersMap, UpdatePictureModel updatePictureModel) {
        mainApi.requestUpdatePicture(headersMap, updatePictureModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(), getApplication().getString(R.string.picture_update_success), Toast.LENGTH_SHORT).show();
                requestSingleChild(headersMap, updatePictureModel.getId());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeletePicture(HashMap<String, String> headersMap, UpdatePictureModel updatePictureModel) {
        mainApi.requestDeletePicture(headersMap, updatePictureModel).enqueue(new Callback<Child>() {
            @Override
            public void onResponse(@NonNull Call<Child> call, @NonNull Response<Child> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Child currentChild = currentChildMutableLiveData.getValue();
                if (currentChild == null) {
                    return;
                }

                Child responseChild = response.body();
                if (responseChild == null) {
                    return;
                }
                currentChild.setPhotoLink(responseChild.getPhotoLink());
                currentChildMutableLiveData.setValue(currentChild);
            }

            @Override
            public void onFailure(@NonNull Call<Child> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
