package com.example.moneycare.ui.view.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneycare.R;
import com.example.moneycare.data.custom.MoneyFormatter;
import com.example.moneycare.databinding.FragmentMoneyFormatDialogBinding;
import com.example.moneycare.ui.view.transaction.wallet.NewWalletActivity;
import com.example.moneycare.utils.Converter;
import com.example.moneycare.utils.PrefUtil;
import com.example.moneycare.utils.appenum.MoneySeparator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;

public class MoneyFormatDialogFragment extends DialogFragment {

    SharedPreferences sharedPreferences;
    FragmentActivity currentActivity;
    MoneyFormatter formatter;
    private final long money;
    View view;
    SwitchMaterial shortMoneySwitch;
    SwitchMaterial currencySymbolSwitch;
    AutoCompleteTextView fractionSelector;
    AutoCompleteTextView separatorSelector;
    // data
    private static final String[] FRACTIONS = new String[] {
            "21.00", "21"
    };
    private static final String[] SEPARATORS = new String[] {
            "21,000.00", "21.000,00"
    };
    public MoneyFormatDialogFragment(FragmentActivity activity){
        currentActivity = activity;
        money = 2100;
        formatter = new MoneyFormatter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(currentActivity);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_money_format_dialog, null);
        builder.setView(view)
                .setTitle("Kiểu hiển thị tiền số")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveFormatter();
                        Toast toast =  Toast.makeText(currentActivity, "Cập nhật định dạng tiền thành công", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        initElements();
        loadFormatter();
        initShortMoneySwitch();
        initCurrencySymbolSwitch();
        initFractionSelector();
        initSeparatorSelector();
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void initElements() {
        shortMoneySwitch = view.findViewById(R.id.short_money_switch);
        currencySymbolSwitch = view.findViewById(R.id.currency_symbol_switch);
        fractionSelector = view.findViewById(R.id.fraction_selector);
        separatorSelector = view.findViewById(R.id.separator_selector);
    }
    private void loadFormatter(){
        formatter = PrefUtil.getMoneyFormatter(currentActivity);
        updateSampleMoney();
        initElementFromFormatter();
    }
    private void initElementFromFormatter(){
        shortMoneySwitch.setChecked(formatter.isShortType);
        currencySymbolSwitch.setChecked(formatter.hasCurrencySymbol);

        // Note: set Autocomplete text before set adapter
        int fractionIndex = formatter.hasFraction? 0: 1;
        fractionSelector.setText(FRACTIONS[fractionIndex]);

        int sepIndex = formatter.separator.equals(MoneySeparator.COMMA)? 0 : 1;
        separatorSelector.setText(SEPARATORS[sepIndex]);
    }
    private void saveFormatter(){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(formatter);
        prefsEditor.putString("money_format", json);
        prefsEditor.commit();
    }

    private void initShortMoneySwitch(){
        shortMoneySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                formatter.isShortType = isChecked;
                updateSampleMoney();
            }
        });
    }
    private void initCurrencySymbolSwitch() {
        currencySymbolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                formatter.hasCurrencySymbol = isChecked;
                updateSampleMoney();
            }
        });
    }
    private void initFractionSelector() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentActivity, R.layout.dropdown_item, FRACTIONS);
        fractionSelector.setAdapter(adapter);
        fractionSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                formatter.hasFraction = position == 0;
                updateSampleMoney();
            }
        });
    }
    private void initSeparatorSelector() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentActivity, R.layout.dropdown_item, SEPARATORS);
        separatorSelector.setAdapter(adapter);
        separatorSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                formatter.separator = position == 0? MoneySeparator.COMMA: MoneySeparator.DOT;
                updateSampleMoney();
            }
        });
    }
    private void updateSampleMoney(){
        TextView sampleMoney = view.findViewById(R.id.sample_money);
        String convertedMoney = Converter.formatMoney(formatter, money);
        sampleMoney.setText(convertedMoney);
    }
    public void showDialog() {
        this.show(currentActivity.getSupportFragmentManager(), "dialog");
    }
}
