package ru.alfabank.currencyrates.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.alfabank.currencyrates.mvp.MainActivity;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void injects(MainActivity target);
}