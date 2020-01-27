package com.robotraccoons.debtnote.UnitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //AllUnitTests
        TestAccessUsers.class,
        TestAccessPayments.class,
        TestAccessTransaction.class,
        TestTransactionObject.class,
        TestUserObject.class,
        TestPaymentObject.class
})
public class AllUnitTests {
}
