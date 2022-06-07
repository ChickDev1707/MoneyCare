package com.example.moneycare.ui.viewmodel.transaction;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moneycare.R;
import com.example.moneycare.data.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class WalletArrayAdapter extends ArrayAdapter<Wallet> {
    private final Context mContext;
    private final List<Wallet> wallets;
    private final List<Wallet> walletsAll;
    private final int mLayoutResourceId;

    public WalletArrayAdapter(Context context, int resource, List<Wallet> Wallets) {
        super(context, resource, Wallets);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.wallets = new ArrayList<>(Wallets);
        this.walletsAll = new ArrayList<>(Wallets);

    }

    public int getCount() {
        return wallets.size();
    }

    public Wallet getItem(int position) {
        return wallets.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            Wallet Wallet = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.textView);
            name.setText(Wallet.name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Wallet) resultValue).name;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Wallet> WalletsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (Wallet Wallet : walletsAll) {
                        if (Wallet.name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            WalletsSuggestion.add(Wallet);
                        }
                    }
                    filterResults.values = WalletsSuggestion;
                    filterResults.count = WalletsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                wallets.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using wallets.addAll((ArrayList<Wallet>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Wallet) {
                            wallets.add((Wallet) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    wallets.addAll(walletsAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
