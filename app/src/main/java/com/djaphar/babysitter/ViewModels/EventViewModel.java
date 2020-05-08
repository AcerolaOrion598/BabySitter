package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Child;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Event;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
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

public class EventViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Child>> childrenMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Event> eventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Food>> foodsMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private MainApi mainApi;

    public EventViewModel(@NonNull Application application) {
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

    public MutableLiveData<Event> getEvent() {
        return eventMutableLiveData;
    }

    public MutableLiveData<ArrayList<Food>> getFoods() {
        return foodsMutableLiveData;
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

    public void requestEvent(HashMap<String, String> headersMap, String childId, String date) {
        mainApi.requestEvent(headersMap, childId, date).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        eventMutableLiveData.setValue(new Event(childId, date, null,
                                null, null, null, null, new ArrayList<>()));
                        return;
                    }
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                eventMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdateEvent(HashMap<String, String> headersMap, Event event, String childId, String date) {
        mainApi.requestUpdateEvent(headersMap, event).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplication(), R.string.event_update_success, Toast.LENGTH_SHORT).show();
                requestEvent(headersMap, childId, date);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestMyFoods(HashMap<String, String> headersMap) {
        mainApi.requestMyFoods(headersMap).enqueue(new Callback<ArrayList<Food>>() {
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
}