package ru.alfabank.currencyrates.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.alfabank.currencyrates.mvp.RateRepo;
import ru.alfabank.currencyrates.mvp.RatesContract;
import ru.alfabank.currencyrates.mvp.RatesPresenter;
import ru.alfabank.currencyrates.network.Api;

@Module
public class AppModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }

    @Provides
    @Singleton
    Api provideApi(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://alfa-mobile.alfabank.ru/ALFAJMB/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }

    @Provides
    @Singleton
    RatesContract.Repo provideRepo(Api api) {
        return new RateRepo(api);
    }


    @Provides
    RatesContract.Presenter providePresenter(RatesContract.Repo repo) {
        return new RatesPresenter(repo);
    }
}
