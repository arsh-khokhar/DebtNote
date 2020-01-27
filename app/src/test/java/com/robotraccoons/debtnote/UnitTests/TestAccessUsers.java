package com.robotraccoons.debtnote.UnitTests;
import android.util.Log;

import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.UserPersistence;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestAccessUsers {
    private UserPersistence upmock = mock(UserPersistence.class);
    private AccessUsers a = new AccessUsers(upmock);

    @Before
    public void setup(){
        List<User> users = new ArrayList<User>();
        for (int i=0; i<5; i++){
            users.add(new User("User"+i, "12345"+i));
        }
        when(upmock.getUserByUsername(anyString())).thenReturn(
                new User("arsh", "1234567")
        );
        when(upmock.getUsers()).thenReturn(users);
    }

    @Test
    public void testConstructor() {
        assertNotNull(a.getUsers());
    }

    @Test
    public void testInsertAndRemove() {
        int sizeToTest = a.getUsers().size();
        User test = new User(sizeToTest+1, "testUser", "testPass");
        a.insertUser(test);
        assertEquals(0, a.getUsers().size());
        a.deleteUser(test);
        assertEquals(sizeToTest,a.getUsers().size());
    }

    @Test
    public void testGets() {
        int arshId = a.getUserId("arsh");
        assertEquals(0, arshId);
        User currUser = a.getUserByUsername("arsh");
        String arshUserName = currUser.getUserName();
        assertEquals( "arsh", arshUserName);
    }

    @Test
    public void testsGetUserNames(){
        assertEquals(0, a.getUserNames().size());
    }

    @Test
    public void testValidation(){
        User toTest = new User("test", "testing");
        assertFalse(a.validateUserLogin(toTest.getUserName(), toTest.getUserPassword()));
    }

    @Test
    public void testHasUser() {
        User toTest = new User("test", "testing");
        assertFalse(a.hasUser(toTest.getUserName()));
    }
}
