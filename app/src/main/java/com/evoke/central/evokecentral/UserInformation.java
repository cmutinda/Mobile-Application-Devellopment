package com.evoke.central.evokecentral;

/**
 * Created by Castrol on 10/30/2017.
 */

class UserInformation {
    private String name;
    private String surname;
    private String displayname;
    private String email;


    public UserInformation(){

    }

    public UserInformation(String name, String surname, String displayname, String email) {
        this.name = name;
        this.surname = surname;
        this.displayname = displayname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setNamee(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
