package ru.serv_techno.sandwichclub03.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 27.04.2017.
 */

public class UserProfile extends SugarRecord {
    public String name;
    public String phone;
    public String address;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public static UserProfile getUser(){
        List<UserProfile> userProfileList = UserProfile.listAll(UserProfile.class);
        if(userProfileList.size()!=0){
            UserProfile userProfile = userProfileList.get(0);
            return userProfile;
        }
        return null;
    }
}
