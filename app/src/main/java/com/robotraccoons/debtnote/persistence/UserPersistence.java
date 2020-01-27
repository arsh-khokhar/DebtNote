package com.robotraccoons.debtnote.persistence;

import java.util.List;

import com.robotraccoons.debtnote.objects.User;

public interface UserPersistence {
    List<User> getUsers();

    int getUserId(final String username);

    User getUserByUsername(final String username);

    User insertUser(final User currentUser);

    void deleteUser(final User currentUser);

    boolean validateUserLogin(final String username, final String password);

    boolean hasUser(final String username);
}
