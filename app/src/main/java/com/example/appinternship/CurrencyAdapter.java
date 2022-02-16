package com.example.appinternship;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class CurrencyAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<Currency> currencies;

    protected CurrencyAdapter(Context context, List<Currency> currencies) {
        this.inflater = LayoutInflater.from(context);
        this.currencies = currencies;
    }

    public int getCount() {
        return this.currencies.size();
    }

    public Currency getItem(int position) {
        return this.currencies.get(position);
    }

    public long getItemId(int position) {
        return this.currencies.get(position).hashCode();
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        final Currency currency = getItem(position);

        if(convertView == null) {

            convertView = this.inflater.inflate(R.layout.currencies, parent, false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.charCode = convertView.findViewById(R.id.tvCharCode);
            viewHolder.id = convertView.findViewById(R.id.tvId);
            viewHolder.name = convertView.findViewById(R.id.tvName);
            viewHolder.nominal = convertView.findViewById(R.id.tvNominal);
            viewHolder.numCode= convertView.findViewById(R.id.tvNumCode);
            viewHolder.prevValue = convertView.findViewById(R.id.tvPrevValue);
            viewHolder.value = convertView.findViewById(R.id.tvValue);

            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.id.setText("ID: " + currency.getId());
        viewHolder.charCode.setText("CharCode: " + currency.getCharCode());
        viewHolder.name.setText("Name: " + currency.getName());
        viewHolder.nominal.setText("Nominal: " + currency.getNominal());
        viewHolder.numCode.setText("NumCode: " + currency.getNumCode());
        viewHolder.prevValue.setText("Previous: " + currency.getPreValue());
        viewHolder.value.setText("Value: " + currency.getValue());

        return convertView;
    }
}
