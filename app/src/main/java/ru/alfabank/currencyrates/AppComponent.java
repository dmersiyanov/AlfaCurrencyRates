package ru.alfabank.currencyrates;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
interface AppComponent {

    void injects(MainActivity target);
}