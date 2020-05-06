package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Food;
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

public class EventViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Child>> childrenMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Food>> foodsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Food>> selectedMealsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Food>> deniedMealsMutableLiveData = new MutableLiveData<>();
    private LiveData<User> userLiveData;
    private MainApi mainApi;

    public EventViewModel(@NonNull Application application) {
        super(application);
//        Parent parent1 = new Parent("Какой-то", "Азиатский", "Мужык", "Батя", "88005553535",
//                "https://cdn.v2ex.com/gravatar/704e7c12cdc2a663fd7c6521dd8a332d?s=1000&d=mm");
//        Parent parent2 = new Parent("Ещё Один", "Азиатский", "Мужык", "Второй Батя", "89999999999",
//                "https://sun1-83.userapi.com/8VIxiZCE8p0V-eWKGd0erYau3aht-N4Yjo5U1g/GfdITZDwnIA.jpg");
//        ArrayList<Parent> parents = new ArrayList<>();
//        parents.add(parent1);
//        parents.add(parent2);
//        Child child1 = new Child("Мики", "Топ I", "Саяка", "14 лет", "Супер Магическая I",
//                "https://cs9.pikabu.ru/post_img/big/2017/05/12/10/1494605959124044472.jpg",
//                228, parents,1234);
//        Child child2 = new Child("Кёко", "Топ II", "Сакура", "14 лет", "Супер Магическая I",
//                "https://cs9.pikabu.ru/post_img/big/2017/05/14/11/1494784894199428054.jpg",
//                229, parents,1234);
//        Child child3 = new Child("Хомура", "Батя", "Акеми", "14 лет", "Супер Магическая IV",
//                "https://www.kindpng.com/picc/m/415-4159860_homura-akemi-magia-record-hd-png-download.png",
//                230, parents,1234);
//        Child child4 = new Child("Мами", "Отвал Башки", "Томое", "14 лет", "Супер Магическая II",
//                "https://scontent-hel2-1.xx.fbcdn.net/v/t1.0-1/c0.0.720.720a/p720x720/69214742_752504268519202_6911253874263195648_o.jpg?_nc_cat=108&_nc_sid=dbb9e7&_nc_ohc=ZlBxLMDGlGMAX8sx1wI&_nc_ht=scontent-hel2-1.xx&oh=9e7c815e90d41373eb43013fed79137f&oe=5ECB2A21",
//                231, new ArrayList<>(),1234);
//        Child child5 = new Child("Мадока", "Пентагон", "Канаме", "14 лет", "Супер Магическая III",
//                "https://c.wallhere.com/photos/e8/ac/anime_anime_girls_kawaii_girl_pink_white_dress_Mahou_Shoujo_Madoka_Magica_Kaname_Madoka_pink_hair-1309355.jpg!d",
//                232, parents, 1234);
//        Food meal1 = new Food("Хавка 1");
//        Food meal2 = new Food("Хавка 2");
//        Food meal3 = new Food("Хавка 3");
//        Food meal4 = new Food("Хавка 4");
//        ArrayList<Child> children = new ArrayList<>();
//        ArrayList<Food> foods = new ArrayList<>();
//        ArrayList<Food> selectedFoods = new ArrayList<>();
//        ArrayList<Food> deniedFoods = new ArrayList<>();
//        children.add(child1);
//        children.add(child2);
//        children.add(child3);
//        children.add(child4);
//        children.add(child5);
//        foods.add(meal1);
//        foods.add(meal2);
//        foods.add(meal3);
//        foods.add(meal4);
//        selectedFoods.add(meal1);
//        selectedFoods.add(meal3);
//        selectedFoods.add(meal4);
//        deniedFoods.add(meal3);
//        kidsMutableLiveData.setValue(children);
//        mealsMutableLiveData.setValue(foods);
//        selectedMealsMutableLiveData.setValue(selectedFoods);
//        deniedMealsMutableLiveData.setValue(deniedFoods);
        userLiveData = LocalDataRoom.getDatabase(application).localDataDao().getUser();
        mainApi = ApiBuilder.getMainApi();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public MutableLiveData<ArrayList<Child>> getChildren() {
        return childrenMutableLiveData;
    }

    public MutableLiveData<ArrayList<Food>> getFoods() {
        return foodsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Food>> getSelectedMeals() {
        return selectedMealsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Food>> getDeniedMeals() {
        return deniedMealsMutableLiveData;
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