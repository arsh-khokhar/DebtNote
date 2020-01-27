package com.robotraccoons.debtnote.business;

import com.robotraccoons.debtnote.application.Services;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.UserPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessUsers {
    private UserPersistence userPersistence;
    private List<User> users;

    public AccessUsers(){
        userPersistence = Services.getUserPersistence();
        users = userPersistence.getUsers();}

    public AccessUsers(UserPersistence userPersistence)
    {
        this.userPersistence = userPersistence;
        users = userPersistence.getUsers();
    }

    //security problem: we should not get all users information to your device
    public List<User> getUsers()
    {
        return Collections.unmodifiableList(users);
    }

    public List<String> getUserNames() {
        ArrayList<String> toReturn = new ArrayList<String>();
        for(int i = 0; i < users.size(); i++) {
            toReturn.add(users.get(i).getUserName());
        }
        return Collections.unmodifiableList(toReturn);
    }

    public User getUserByUsername(String username) { return userPersistence.getUserByUsername(username); }
    public int getUserId(String username) { return userPersistence.getUserId(username); }
    public User insertUser(User currentUser)
    {
        return userPersistence.insertUser(currentUser);
    }
    public void deleteUser(User currentUser)
    {
        userPersistence.deleteUser(currentUser);
    }

    public boolean validateUserLogin(String username, String password){
        return userPersistence.validateUserLogin(username, password);
    }

    public boolean hasUser(String username){
        return userPersistence.hasUser(username);
    }
}