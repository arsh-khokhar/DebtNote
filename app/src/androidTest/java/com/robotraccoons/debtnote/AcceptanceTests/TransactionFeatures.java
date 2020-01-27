package com.robotraccoons.debtnote.AcceptanceTests;

import com.robotraccoons.debtnote.R;
import com.robotraccoons.debtnote.application.Services;
import com.robotraccoons.debtnote.objects.Payment;
import com.robotraccoons.debtnote.objects.User;
import com.robotraccoons.debtnote.persistence.PaymentPersistence;
import com.robotraccoons.debtnote.persistence.UserPersistence;
import com.robotraccoons.debtnote.presentation.MainActivity;
import com.robotraccoons.debtnote.presentation.StartupActivity;

import android.app.Service;
import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.AllOf.allOf;


import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
public class TransactionFeatures {
    private UserPersistence userPersistence;

    @Rule
    public ActivityTestRule<StartupActivity> activityActivityTestRule
            = new ActivityTestRule<StartupActivity>(StartupActivity.class, true);

    @Before
    public void setup(){
        userPersistence = Services.getUserPersistence();
        userPersistence.insertUser(new User("marker", "himarker"));
        userPersistence.insertUser(new User("Bichard", "hibichard"));
        userPersistence.insertUser(new User("Micycle", "himicycle"));
        userPersistence.insertUser(new User("Jimothy", "hijimothy"));

        SystemClock.sleep(1500);

        onView(withId(R.id.logInUserNameET)).perform(typeText("marker"));
        onView(withId(R.id.logInPasswordET)).perform(typeText("himarker"));
        pressBack();
        onView(withId(R.id.loginbutton)).perform(click());

        SystemClock.sleep(1000);
    }

    @Test
    public void addTransaction(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.newUser)).perform(typeText("Bichard"));
        onView(withId(R.id.transactionTitle)).perform(click());
        onView(withId(R.id.transactionTitle)).perform(typeText("Pizza & Beer"));
        onView(withId(R.id.transactionAmount)).perform(typeText("45.45"));
        onView(withId(R.id.transactionDesc)).perform(typeText(
                "Went out with the boys for some warm ones"));

        pressBack();
        SystemClock.sleep(1500);
        onView(withId(R.id.paidByButton)).perform(click());
        onView(withText("marker")).perform(click());
        onView(withId(R.id.addTransButton)).perform(click());
        SystemClock.sleep(3000);
    }

    @Test
    public void viewTransaction(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.newUser)).perform(typeText("Micycle"));
        onView(withId(R.id.transactionTitle)).perform(click());
        onView(withId(R.id.transactionTitle)).perform(typeText("Mozzarella sticks"));
        onView(withId(R.id.transactionAmount)).perform(typeText("78.12"));
        onView(withId(R.id.transactionDesc)).perform(typeText(
                "Went out with the boys for some long ones"));

        pressBack();
        SystemClock.sleep(1500);
        onView(withId(R.id.paidByButton)).perform(click());
        onView(withText("marker")).perform(click());
        onView(withText("Add Transaction")).perform(click());
        onView(withId(R.id.transactionList)).perform(click());
        SystemClock.sleep(5000);
    }

    @Test
    public void closeTransaction(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.newUser)).perform(typeText("Bichard"));
        onView(withId(R.id.transactionTitle)).perform(click());
        onView(withId(R.id.transactionTitle)).perform(typeText("Wings"));
        onView(withId(R.id.transactionAmount)).perform(typeText("32.50"));
        onView(withId(R.id.transactionDesc)).perform(typeText(
                "Went out with the boys for some hot ones"));

        pressBack();
        SystemClock.sleep(1500);
        onView(withId(R.id.paidByButton)).perform(click());
        onView(withText("marker")).perform(click());
        onView(withText("Add Transaction")).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.transactionList)).perform(click());
        SystemClock.sleep(500);
        onView(withId(R.id.deleteButton)).perform(click());
        SystemClock.sleep(5000);
    }

    @Test
    public void addingUsersToTransaction(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.newUser)).perform(typeText("Jimothy, Bichard, Micycle"));
        onView(withId(R.id.transactionTitle)).perform(click());
        onView(withId(R.id.transactionTitle)).perform(typeText("Sushi Jet"));
        onView(withId(R.id.transactionAmount)).perform(typeText("138.76"));
        onView(withId(R.id.transactionDesc)).perform(typeText(
                "All you can eat team"));

        pressBack();
        SystemClock.sleep(1500);
        onView(withId(R.id.paidByButton)).perform(click());
        onView(withText("marker")).perform(click());
        onView(withText("Add Transaction")).perform(click());
        SystemClock.sleep(2000);
    }

}
