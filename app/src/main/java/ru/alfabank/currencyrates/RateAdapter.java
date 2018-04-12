package ru.alfabank.currencyrates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.alfabank.currencyrates.models.Currency;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateVH> {

    private final List<Currency> items = new ArrayList<>();

    @NonNull
    @Override
    public RateVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RateVH(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RateVH holder, int position) {
        Currency currency = items.get(position);
        holder.bind(currency);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setItems(List<Currency> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    static class RateVH extends RecyclerView.ViewHolder {

        public RateVH(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_rate, parent, false));
        }

        void bind(Currency currency) {

        }
    }
}