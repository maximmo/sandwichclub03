package ru.serv_techno.sandwichclub03;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 27.04.2017.
 */

public class UserProfile extends SugarRecord {
    String name;
    String phone;
    String address;

    public UserProfile() {
    }

    public UserProfile(String name, String phone, String address) {

        this.name = name;
        this.phone = phone;
        this.address = address;

    }
}
