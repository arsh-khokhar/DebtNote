package com.robotraccoons.debtnote.persistence.stubs;

import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.UserPersistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPersistenceStub implements UserPersistence {
    private List<User> users;

    public UserPersistenceStub() {
        this.users = new ArrayList<>();
        users.add(new User("arsh", "hiarsh")); //, users.size()+1));
        users.add(new User("kiernan", "hikiernan")); //, users.size()+1));
        users.add(new User("matt", "himatt")); //, users.size()+1));
        users.add(new User("mary", "himary")); //, users.size()+1));
        users.add(new User("ian1", "hiian1")); //, users.size()+1));
        users.add(new User("marker", "himarker"));//, users.size()+1));
    }


    //security problem: getting all users information to your device
    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public User insertUser(User currentUser) {
        // don't bother checking for duplicates
        users.add(currentUser);
        return currentUser;
    }

    @Override
    public void deleteUser(User toDelete) {
        int deleteIndex = users.indexOf(toDelete);
        if (deleteIndex >= 0) {
            users.remove(deleteIndex);
        }
    }

    public int getUserId(final String username) {
        int returnId = 0;
        boolean found = false;
        for (int i = 0; i < users.size() && !found; i++) {
            User currUser = users.get(i);
            if (currUser.getUserName().equals(username)) {
                returnId = currUser.getUserID();
                found = true;
            }
        }
        return returnId;
    }

    //
    public User getUserByUsername(final String username) {
        User returnUser = null;
        User currUser = null;
        boolean found = false;
        for (int i = 0; i < users.size() && !found; i++) {
            currUser = users.get(i);
            if (currUser.getUserName().equals(username.trim())) {
                returnUser = currUser;
                found = true;
            }
        }
        return returnUser;
    }

    public boolean validateUserLogin(final String username, final String password){
        for(int i = 0; i < users.size(); i++) {
            User curr = users.get(i);
            if(curr.getUserName().equals(username) && curr.getUserPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUser(final String username){
        for(int i = 0; i < users.size(); i++) {
            User curr = users.get(i);
            if(curr.getUserName().equals(username) ) {
                return true;
            }
        }
        return false;
    }

}