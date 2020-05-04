package com.djaphar.babysitter.ViewModels;

import android.app.Application;

import com.djaphar.babysitter.SupportClasses.ApiClasses.Meal;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataDao;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.LocalDataRoom;
import com.djaphar.babysitter.SupportClasses.LocalDataClasses.User;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Meal>> mealsMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private LocalDataDao dao;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        Meal meal1 = new Meal("Хавка 1");
        Meal meal2 = new Meal("Хавка 2");
        Meal meal3 = new Meal("Хавка 3");
        Meal meal4 = new Meal("Хавка 4");
        ArrayList<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        meals.add(meal3);
        meals.add(meal4);
        mealsMutableLiveData.setValue(meals);

        LocalDataRoom room = LocalDataRoom.getDatabase(application);
        dao = room.localDataDao();
        userLiveData = dao.getUser();
    }

    public MutableLiveData<ArrayList<Meal>> getMeals() {
        return mealsMutableLiveData;
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void logout() {
        LocalDataRoom.databaseWriteExecutor.execute(() -> dao.deleteUser());
    }
}
