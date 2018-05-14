package ru.alfabank.currencyrates;

import android.app.Application;

import ru.alfabank.currencyrates.di.AppComponent;
import ru.alfabank.currencyrates.di.DaggerAppComponent;

public class App extends Application {

    public static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.create();
    }
}
