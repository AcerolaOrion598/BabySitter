package com.djaphar.babysitter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import com.djaphar.babysitter.R;
import com.djaphar.babysitter.SupportClasses.ApiClasses.ApiBuilder;
import com.djaphar.babysitter.SupportClasses.ApiClasses.Bill;
import com.djaphar.babysitter.SupportClasses.ApiClasses.BillPostModel;
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
    private MutableLiveData<Bill> currentBillMutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<Bill> getCurrentBill() {
        return currentBillMutableLiveData;
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

    public void requestMyBills(HashMap<String, String> headersMap) {
        mainApi.requestMyBills(headersMap).enqueue(new Callback<ArrayList<Bill>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Bill>> call, @NonNull Response<ArrayList<Bill>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                billsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Bill>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestSingleBill(HashMap<String, String> headersMap, String billId) {
        mainApi.requestSingleBill(headersMap, billId).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(@NonNull Call<Bill> call, @NonNull Response<Bill> response) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        currentBillMutableLiveData.setValue(null);
                        return;
                    }
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                currentBillMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Bill> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestCreateBill(HashMap<String, String> headersMap, BillPostModel billPostModel) {
        mainApi.requestCreateBill(headersMap, billPostModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplication(), R.string.create_bill_text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestUpdateBill(HashMap<String, String> headersMap, String billId, BillPostModel billPostModel) {
        mainApi.requestUpdateBill(headersMap, billId, billPostModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplication(), R.string.update_bill_text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDeleteBill(HashMap<String, String> headersMap, String billId) {
        mainApi.requestDeleteBill(headersMap, billId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                requestSingleBill(headersMap, billId);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}