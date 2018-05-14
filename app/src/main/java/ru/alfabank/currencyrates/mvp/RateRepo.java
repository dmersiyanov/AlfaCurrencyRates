package ru.alfabank.currencyrates.mvp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.currencyrates.Api;
import ru.alfabank.currencyrates.models.RateResponse;

public class RateRepo implements RatesContract.Repo {

    private final Api api;
    private Single<RateResponse> cache;

    public RateRepo(Api api) {
        this.api = api;
    }

    @Override
    public Single<RateResponse> load(boolean refresh) {
        if (refresh) cache = null;
        if (cache == null) {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("operationId", "Currency:GetCurrencyRates");
            cache = api.loadRates(requestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();
        }
        return cache;
    }
}
