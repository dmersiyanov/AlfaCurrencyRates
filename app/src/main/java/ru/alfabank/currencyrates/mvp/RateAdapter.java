package ru.alfabank.currencyrates.mvp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import ru.alfabank.currencyrates.R;
import ru.alfabank.currencyrates.models.Currencies;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateVH> {

    private final List<Currencies> items = new ArrayList<>();
    private final DecimalFormat formatter;

    public RateAdapter() {
        super();
        formatter = new DecimalFormat();
        formatter.setMinimumFractionDigits(2);
        formatter.setMinimumFractionDigits(0);
        formatter.setGroupingUsed(true);
        formatter.setGroupingSize(3);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
    }

    @NonNull
    @Override
    public RateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RateVH(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RateVH holder, int position) {
        Currencies rate = items.get(position);
        holder.bind(rate);
        holder.setUpArrows(rate);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<Currencies> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class RateVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView description;
        private TextView sellRate;
        private TextView buyRate;
        private ImageView arrowSell;
        private ImageView arrowBuy;


        public RateVH(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_rate, parent, false));
            name = itemView.findViewById(R.id.rates_list_item_name);
            description = itemView.findViewById(R.id.rates_list_item_description);
            sellRate = itemView.findViewById(R.id.rates_list_item_sell_rate);
            buyRate = itemView.findViewById(R.id.rates_list_item_buy_rate);
            arrowSell = itemView.findViewById(R.id.arrow_sell);
            arrowBuy = itemView.findViewById(R.id.arrow_buy);
        }

        void bind(Currencies rate) {
            name.setText(rate.code);
            description.setText(rate.description);
            sellRate.setText(rate.ratesByDate.get(0).currencyRates.get(0).sellRate);
            buyRate.setText(rate.ratesByDate.get(0).currencyRates.get(0).buyRate);

            Log.i("RATE", rate.ratesByDate.get(0).currencyRates.get(0).sellRate);
            Log.i("RATE", rate.ratesByDate.get(7).currencyRates.get(0).sellRate);
        }

        void setUpArrows(Currencies rate) {

            BigDecimal todaySellRate = new BigDecimal(rate.ratesByDate.get(0).currencyRates.get(0).sellRate);
            BigDecimal lastWeekSellRate = new BigDecimal(rate.ratesByDate.get(7).currencyRates.get(0).sellRate);

            BigDecimal todayBuyRate = new BigDecimal(rate.ratesByDate.get(0).currencyRates.get(0).buyRate);
            BigDecimal lastWeekBuyRate = new BigDecimal(rate.ratesByDate.get(7).currencyRates.get(0).buyRate);

            if(todaySellRate.compareTo(lastWeekSellRate) < 0){
                arrowSell.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.red_arrow_down));
            } else {
                arrowSell.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.green_arrow_up));
            }

            if(todayBuyRate.compareTo(lastWeekBuyRate) < 0){
                arrowBuy.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.red_arrow_down));
            } else {
                arrowBuy.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.green_arrow_up));
            }


        }
    }
}