package com.pucmm.isc581_ecommerce.models;

import android.net.Uri;

public class User {

//    private String name;
//    private String username;
//    private String email;
//    private String phone;
//    private String imageUrl;
    private long uid;
    private Uri image;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String rol;
    private long birthday;

    public User(long uid, Uri image, String firstName, String lastName, String email, String contact, String rol, long birthday) {
        this.uid = uid;
        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contact = contact;
        this.rol = rol;
        this.birthday = birthday;
    }

    public User() {

    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
}
