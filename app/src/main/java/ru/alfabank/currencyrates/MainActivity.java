package ru.alfabank.currencyrates;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.alfabank.currencyrates.models.RateResponse;

public class MainActivity extends AppCompatActivity {

    private Api api = new Retrofit.Builder()
            .baseUrl("https://alfa-mobile.alfabank.ru/ALFAJMB/")
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);
    private RateAdapter rateAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateAdapter = new RateAdapter();
        recyclerView.setAdapter(rateAdapter);
        progressBar = findViewById(R.id.progressBar);

        load();
    }

    private void load() {
        swipeRefreshLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("operationId", "Currency:GetCurrencyRates");

        api.loadRates(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(RateResponse response) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        rateAdapter.setItems(response.currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, R.string.unable_to_load_data, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
