package com.robotraccoons.debtnote.IntegrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //AllIntegrationObjectTests
        TestTransactionObjectIT.class,
        TestUserObjectIT.class,
        TestPaymentObjectIT.class
})

public class AllIntegrationTests {
}