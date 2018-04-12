package ru.alfabank.currencyrates;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.alfabank.currencyrates.models.RateResponse;

public interface Api {

    @POST("gate")
    @Headers({
            "jmb-protocol-version: 1.0",
            "jmb-protocol-service: Currency"
    })
    Single<RateResponse> loadRates(@Body String body);
}
