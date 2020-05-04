package com.djaphar.babysitter.SupportClasses.LocalDataClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "access_token")
    private String access_token;

    @NonNull
    @ColumnInfo(name = "token_type")
    private String token_type;

    public User(@NonNull String access_token, @NonNull String token_type) {
        this.access_token = access_token;
        this.token_type = token_type;
    }

    @NonNull
    public String getAccess_token() {
        return access_token;
    }

    @NonNull
    public String getToken_type() {
        return token_type;
    }

    public void setAccess_token(@NonNull String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(@NonNull String token_type) {
        this.token_type = token_type;
    }
}
