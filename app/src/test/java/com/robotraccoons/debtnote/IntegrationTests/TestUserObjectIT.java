package com.robotraccoons.debtnote.IntegrationTests;

import com.robotraccoons.debtnote.objects.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUserObjectIT {
    @Test
    public void testAddingUsers() {
        User user1 = new User("user1", "password1");
        assertEquals("user1",user1.getUserName());
        assertEquals("password1",user1.getUserPassword());
        //User user2 = new User("user2", "password2");
        //assertEquals(user1.getUserID()+1, user2.getUserID());
    }
}