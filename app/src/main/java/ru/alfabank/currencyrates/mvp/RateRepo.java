package ru.alfabank.currencyrates.mvp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.alfabank.currencyrates.Api;
import ru.alfabank.currencyrates.models.RateResponse;

public class RateRepo implements RatesContract.Repo {

    private final Api api;
    private Single<RateResponse> cache;
    private String dateFrom, dateTo;

    public RateRepo(Api api) {
        this.api = api;
    }

    @Override
    public Single<RateResponse> load(boolean refresh) {
        if (refresh) cache = null;
        if (cache == null) {
            getDates();
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("operationId", "Currency:GetCurrencyRates");
            requestBody.put("dateFrom", dateFrom);
            requestBody.put("dateTo", dateTo);
            cache = api.loadRates(requestBody)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .cache();

        }
        return cache;
    }

    void getDates() {
        String example = "2018-05-14T00:00:00.000+0300";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        Date currentDate = new Date();
        dateTo = dateFormat.format(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_WEEK, -7);
        Date weekBefore = cal.getTime();
        dateFrom = dateFormat.format(weekBefore);
    }
}
