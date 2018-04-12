package ru.alfabank.currencyrates;

import java.util.List;

import io.reactivex.Single;
import ru.alfabank.currencyrates.models.Currencies;
import ru.alfabank.currencyrates.models.RateResponse;

public interface RatesContract {

    interface View {

        void showLoading(boolean refresh);

        void showError();

        void showData(List<Currencies> currencies);
    }

    interface Presenter {

        void attachView(View view);

        void detachView();

        void load(boolean refresh);
    }

    interface Repo {

        Single<RateResponse> load(boolean refresh);
    }
}
