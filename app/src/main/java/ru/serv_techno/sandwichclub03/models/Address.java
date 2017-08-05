package ru.serv_techno.sandwichclub03.models;

import com.orm.SugarRecord;

import java.util.List;

public class Address extends SugarRecord {

    public String desc;
    public String address;

    public Address(){

    }

    public Address(String desc, String address){
        this.desc = desc;
        this.address = address;
    }

    public static List<Address> findByName(String name) {
        return Address.find(Address.class, "address = ?", name);
    }

}
