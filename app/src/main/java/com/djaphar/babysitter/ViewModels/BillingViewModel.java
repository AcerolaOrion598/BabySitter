package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.MainApi;
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

public class BillingViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private MutableLiveData<ArrayList<Bill>> billsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Child>> childrenMutableLiveData = new MutableLiveData<>();
    private MainApi mainApi;

    public BillingViewModel(@NonNull Application application) {
        super(application);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<Bill>> getBills() {
        return billsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Child>> getChildren() {
        return childrenMutableLiveData;
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
}