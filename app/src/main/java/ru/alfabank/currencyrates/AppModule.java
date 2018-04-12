package ru.alfabank.currencyrates;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
