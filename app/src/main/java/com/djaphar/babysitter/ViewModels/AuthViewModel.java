package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {

    private LiveData<User> userLiveData;
    private LocalDataDao dao;
    private MainApi mainApi;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        dao = LocalDataRoom.getDatabase(application).localDataDao();
        userLiveData = dao.getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void loginRequest(String username, String password) {
        mainApi.requestLogin(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                LocalDataRoom.databaseWriteExecutor.execute(() -> dao.setUser(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
