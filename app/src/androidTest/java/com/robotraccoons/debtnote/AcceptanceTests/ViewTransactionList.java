package com.robotraccoons.debtnote.AcceptanceTests;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.application.Services;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.Transaction;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.TransactionPersistence;
import com.robotraccoons.debtnote.persistence.UserPersistence;
import com.robotraccoons.debtnote.presentation.MainActivity;
import com.robotraccoons.debtnote.presentation.StartupActivity;

import android.app.Service;
import android.os.SystemClock;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
public class ViewTransactionList {
    private UserPersistence userPersistence;

    @Rule
    public ActivityTestRule<StartupActivity> activityActivityTestRule
            = new ActivityTestRule<StartupActivity>(StartupActivity.class, true);

    @Test
    public void viewTransactionList(){
        userPersistence = Services.getUserPersistence();
        userPersistence.insertUser(new User("marker", "himarker"));

        SystemClock.sleep(1500);

        onView(withId(R.id.logInUserNameET)).perform(typeText("marker"));
        onView(withId(R.id.logInPasswordET)).perform(typeText("himarker"));
        pressBack();
        onView(withId(R.id.loginbutton)).perform(click());

        SystemClock.sleep(1000);
    }

}
