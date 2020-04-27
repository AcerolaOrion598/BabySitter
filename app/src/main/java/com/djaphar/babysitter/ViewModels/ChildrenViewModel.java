package com.djaphar.babysitter.ViewModels;

import android.app.Application;

import com.djaphar.babysitter.SupportClasses.ApiClasses.Kid;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Parent;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ChildrenViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Kid>> kidsLiveData = new MutableLiveData<>();

    public ChildrenViewModel(@NonNull Application application) {
        super(application);
        Parent parent1 = new Parent("Какой-то", "Азиатский", "Мужык", "Батя", "88005553535",
                "https://cdn.v2ex.com/gravatar/704e7c12cdc2a663fd7c6521dd8a332d?s=1000&d=mm");
        Parent parent2 = new Parent("Какая-то", "Азиатская", "Баба", "Второй Батя", "89999999999",
                "https://sun1-83.userapi.com/8VIxiZCE8p0V-eWKGd0erYau3aht-N4Yjo5U1g/GfdITZDwnIA.jpg");
        ArrayList<Parent> parents = new ArrayList<>();
        parents.add(parent1);
        parents.add(parent2);
        Kid kid1 = new Kid("Мики", "Топ Вайфу", "Саяка", "14 лет", "Супер Магическая I",
                "https://cs9.pikabu.ru/post_img/big/2017/05/12/10/1494605959124044472.jpg",
                228, parents);
        Kid kid2 = new Kid("Кёко", "Тоже Топ", "Сакура", "14 лет", "Супер Магическая I",
                "https://cs9.pikabu.ru/post_img/big/2017/05/14/11/1494784894199428054.jpg",
                229, parents);
        Kid kid3 = new Kid("Хомура", "МакКонахи", "Акеми", "14 лет", "Супер Магическая IV",
                "https://www.kindpng.com/picc/m/415-4159860_homura-akemi-magia-record-hd-png-download.png",
                230, parents);
        Kid kid4 = new Kid("Мами", "Это Спойлер", "Томое", "14 лет", "Супер Магическая II",
                "https://scontent-hel2-1.xx.fbcdn.net/v/t1.0-1/c0.0.720.720a/p720x720/69214742_752504268519202_6911253874263195648_o.jpg?_nc_cat=108&_nc_sid=dbb9e7&_nc_ohc=ZlBxLMDGlGMAX8sx1wI&_nc_ht=scontent-hel2-1.xx&oh=9e7c815e90d41373eb43013fed79137f&oe=5ECB2A21",
                231, new ArrayList<>());
        Kid kid5 = new Kid("Мадока", "Ведьма", "Канаме", "14 лет", "Супер Магическая III",
                "https://c.wallhere.com/photos/e8/ac/anime_anime_girls_kawaii_girl_pink_white_dress_Mahou_Shoujo_Madoka_Magica_Kaname_Madoka_pink_hair-1309355.jpg!d",
                232, parents);
        ArrayList<Kid> kids = new ArrayList<>();
        kids.add(kid1);
        kids.add(kid2);
        kids.add(kid3);
        kids.add(kid4);
        kids.add(kid5);
        kidsLiveData.setValue(kids);
    }

    public MutableLiveData<ArrayList<Kid>> getKids() {
        return kidsLiveData;
    }
}