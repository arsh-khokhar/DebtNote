package com.robotraccoons.debtnote.IntegrationTests;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.robotraccoons.debtnote.business.AccessUsers;
import com.robotraccoons.debtnote.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static java.lang.System.in;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestAccessUsersIT {
    AccessUsers a = new AccessUsers();
    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void testConstructor() {
        assertNotNull(a.getUsers());
    }

    @Test
    public void testInsertAndRemove() {
        int sizeToTest = a.getUserNames().size();
        User test = new User(sizeToTest+1, "testUser", "testPass");
        a.insertUser(test);
        assertEquals(sizeToTest, a.getUsers().size());
        a.deleteUser(test);
        assertEquals(sizeToTest,a.getUserNames().size());
    }

    @Test
    public void testGets() {
        a = new AccessUsers();
        int totalSize = a.getUserNames().size();
        User testUser = new User(totalSize, "testUser2", "testPass2");
        a.insertUser(testUser);
        int testIdAfterInsert = a.getUserId("testUser2");
        assertTrue(testIdAfterInsert > 0);
        User currUser = a.getUserByUsername("testUser2");
        String testUserName = currUser.getUserName();
        assertEquals( "testUser2", testUserName);
    }
}
