package com.robotraccoons.debtnote;
import com.robotraccoons.debtnote.IntegrationTests.TestPaymentObjectIT;
import com.robotraccoons.debtnote.IntegrationTests.TestTransactionObjectIT;
import com.robotraccoons.debtnote.IntegrationTests.TestUserObjectIT;
import com.robotraccoons.debtnote.UnitTests.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        //UnitTests
        TestAccessUsers.class,
        TestAccessPayments.class,
        TestAccessTransaction.class,
        TestTransactionObject.class,
        TestUserObject.class,
        TestPaymentObject.class,
        //IntegrationObjectTests
        TestTransactionObjectIT.class,
        TestUserObjectIT.class,
        TestPaymentObjectIT.class
})
public class AllTests
{

}
