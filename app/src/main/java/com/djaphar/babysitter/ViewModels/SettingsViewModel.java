package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
import com.djaphar.babysitter.SupportClasses.ApiClasses.MainApi;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataDao;
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

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Food>> foodsMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private LocalDataDao dao;
    private MainApi mainApi;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
//        Food meal1 = new Food("Хавка 1");
//        Food meal2 = new Food("Хавка 2");
//        Food meal3 = new Food("Хавка 3");
//        Food meal4 = new Food("Хавка 4");
//        ArrayList<Food> foods = new ArrayList<>();
//        foods.add(meal1);
//        foods.add(meal2);
//        foods.add(meal3);
//        foods.add(meal4);
//        mealsMutableLiveData.setValue(foods);

        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        userLiveData = dao.getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public MutableLiveData<ArrayList<Food>> getFoods() {
        return foodsMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void requestMyFoods(HashMap<String, String> headersMap) {
        Call<ArrayList<Food>> call = mainApi.requestMyFoods(headersMap);
        call.enqueue(new Callback<ArrayList<Food>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Food>> call, @NonNull Response<ArrayList<Food>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                foodsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Food>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestCreateFood(HashMap<String, String> headersMap, Food food) {
        mainApi.requestCreateFood(headersMap, food).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestMyFoods(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeleteFood(HashMap<String, String> headersMap, String foodId) {
        mainApi.requestDeleteFood(headersMap, foodId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestMyFoods(headersMap);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logout() {
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteUser());
    }
}
