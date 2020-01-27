package com.robotraccoons.debtnote.application;

import android.app.Application;
import android.content.Context;

public class DebtNoteApplication extends Application {
    private static Context debtNoteContext;

    @Override
    public void onCreate(){
        super.onCreate();
        debtNoteContext = getApplicationContext();
    }

    public static Context getAppContext(){ return debtNoteContext; }
}
