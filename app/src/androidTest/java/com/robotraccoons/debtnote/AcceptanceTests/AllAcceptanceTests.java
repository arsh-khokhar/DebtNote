package com.robotraccoons.debtnote.AcceptanceTests;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //AcceptanceTests
        //The tests are flaky and fail, we're not sure why. Run better on a fresh install.
        CreatingAccount.class,
        TransactionFeatures.class,
        ViewTransactionList.class

})
public class AllAcceptanceTests {
}


