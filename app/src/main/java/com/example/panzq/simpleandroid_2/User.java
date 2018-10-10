package com.example.panzq.simpleandroid_2;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 8711368828010083044L;
    public int userId;
    public String userName;
    public boolean isMale;

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

}
