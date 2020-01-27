package com.robotraccoons.debtnote.IntegrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //IntegrationTests
        TestAccessUsersIT.class,
        TestAccessPaymentsIT.class,
        TestAccessTransactionIT.class,
        ExampleInstrumentedTest.class
})

public class AllIntegrationTests {
}