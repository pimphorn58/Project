package com.example.project_pp;

public class Model {
    String id;

    public Model(String id, String name, String res, String room, String tel, String email, String group, String img, String status) {
        this.id = id;
        this.name = name;
        this.res = res;
        this.room = room;
        this.tel = tel;
        this.email = email;
        this.group = group;
        this.img = img;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRes() {
        return res;
    }

    public String getRoom() {
        return room;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getGroup() {
        return group;
    }

    public String getImg() {
        return img;
    }

    public String getStatus() {
        return status;
    }

    String name;
    String res;
    String room;
    String tel;
    String email;
    String group;
    String img;
    String status;


    public Model() {
    }

}
