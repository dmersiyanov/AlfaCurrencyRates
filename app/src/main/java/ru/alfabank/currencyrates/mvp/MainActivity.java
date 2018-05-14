package ru.alfabank.currencyrates.mvp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import ru.alfabank.currencyrates.App;
import ru.alfabank.currencyrates.R;
import ru.alfabank.currencyrates.models.Currencies;

public class MainActivity
        extends AppCompatActivity
        implements RatesContract.View {

    private RateAdapter rateAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressBar;
    private View errorTextView;
    @Inject RatesContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.component.injects(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.swipeToRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(true);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateAdapter = new RateAdapter();
        recyclerView.setAdapter(rateAdapter);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        presenter.attachView(this);
        load(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void load(boolean refresh) {
        presenter.load(refresh);
    }

    @Override
    public void showLoading(boolean refresh) {
        if (!refresh) {
            swipeRefreshLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError() {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(List<Currencies> currencies) {
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        rateAdapter.setItems(currencies);
    }
}
