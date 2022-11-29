package com.example.lab1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab1.databinding.FragmentHomeBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Spinner spinnerFromCurrency;
    private Spinner spinnerToCurrency;
    private String amount;
    private boolean useOldResource = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerFromCurrency = (Spinner) view.findViewById(R.id.spinnerFromCurrency);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spinnerFromCurrency.getContext(),
                R.array.currencies_for_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromCurrency.setAdapter(adapter);
        this.spinnerFromCurrency = spinnerFromCurrency;

        Spinner spinnerToCurrency = (Spinner) view.findViewById(R.id.spinnerToCurrency);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(spinnerToCurrency.getContext(),
                R.array.currencies_for_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerToCurrency.setAdapter(adapter);
        this.spinnerToCurrency = spinnerToCurrency;
        this.useOldResource = ((MainActivity) getActivity()).getReadFromOldResource();

        spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCurrencyFrom = adapterView.getSelectedItem().toString();
                String selectedCurrencyTo = spinnerToCurrency.getSelectedItem().toString();
                if (useOldResource) {
                    prepareConvertion(selectedCurrencyFrom, selectedCurrencyTo);
                } else {
                    try {
                        prepareConvertionWithAPIData(selectedCurrencyFrom, selectedCurrencyTo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinnerToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCurrencyFrom = spinnerFromCurrency.getSelectedItem().toString();
                String selectedCurrencyTo = adapterView.getSelectedItem().toString();
                if (useOldResource) {
                    prepareConvertion(selectedCurrencyFrom, selectedCurrencyTo);
                } else {
                    try {
                        prepareConvertionWithAPIData(selectedCurrencyFrom, selectedCurrencyTo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        TextView amountFromCurrency = view.findViewById(R.id.editTextNumberDecimalFromValue);
        amountFromCurrency.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (useOldResource) {
                            prepareConvertion(
                                    spinnerFromCurrency.getSelectedItem().toString(),
                                    spinnerToCurrency.getSelectedItem().toString()
                            );
                        } else {
                            try {
                                prepareConvertionWithAPIData(
                                        spinnerFromCurrency.getSelectedItem().toString(),
                                        spinnerToCurrency.getSelectedItem().toString()
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );


//        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }

    public void prepareConvertion(String currencyFrom, String currencyTo) {

        TextView amountFromCurrency = getView().findViewById(R.id.editTextNumberDecimalFromValue);
        amount = amountFromCurrency.getText().toString();
        switch (currencyFrom) {
            case "EUR":
                String[] currencies = getResources().getStringArray(R.array.EUR);
                for (String curr : currencies) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "USD":
                String[] currenciesFromDollar = getResources().getStringArray(R.array.USD);
                for (String curr : currenciesFromDollar) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "SEK":
                String[] currenciesFromSek = getResources().getStringArray(R.array.SEK);
                for (String curr : currenciesFromSek) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "GBP":
                String[] currenciesFromGbp = getResources().getStringArray(R.array.GBP);
                for (String curr : currenciesFromGbp) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "CNY":
                String[] currenciesFromCny = getResources().getStringArray(R.array.CNY);
                for (String curr : currenciesFromCny) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "JPY":
                String[] currenciesFromJpy = getResources().getStringArray(R.array.JPY);
                for (String curr : currenciesFromJpy) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "KRW":
                String[] currenciesFromKrw = getResources().getStringArray(R.array.KRW);
                for (String curr : currenciesFromKrw) {
                    String rate = curr.split(";")[1];
                    String currencyTo1 = curr.split(";")[0];
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
        }
    }

    public void convertCurrency(String amount, double rate) {
        if (amount == "" || amount == null || amount.length() == 0) {
            TextView amountToCurrency = getView().findViewById(R.id.editTextNumberDecimalToValue);
            amountToCurrency.setText("");
        } else {
            double converted = Double.parseDouble(amount) * rate;
            TextView result = getView().findViewById(R.id.editTextNumberDecimalToValue);
            result.setText(converted + "");
        }
    }

    public void prepareConvertionWithAPIData(String currencyFrom, String currencyTo) throws IOException {

        TextView amountFromCurrency = getView().findViewById(R.id.editTextNumberDecimalFromValue);
        amount = amountFromCurrency.getText().toString();
        switch (currencyFrom) {
            case "EUR":
                String currencyEur = readFromFile("EUR.txt");
                currencyEur = currencyEur.substring(0, currencyEur.length() - 1);
                String[] currencies = currencyEur.split(",");
                for (String curr : currencies) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(0, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "USD":
                String currencyUsd = readFromFile("USD.txt");
                currencyUsd = currencyUsd.substring(1, currencyUsd.length() - 1);
                String[] currenciesFromDollar = currencyUsd.split(",");
                for (String curr : currenciesFromDollar) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "SEK":
                String currencySek = readFromFile("SEK.txt");
                currencySek = currencySek.substring(1, currencySek.length() - 1);
                String[] currenciesFromSek = currencySek.split(",");
                for (String curr : currenciesFromSek) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "GBP":
                String currencyGbp = readFromFile("GBP.txt");
                currencyGbp = currencyGbp.substring(1, currencyGbp.length() - 1);
                String[] currenciesFromGbp = currencyGbp.split(",");
                for (String curr : currenciesFromGbp) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "CNY":
                String currencyCny = readFromFile("GBP.txt");
                currencyCny = currencyCny.substring(1, currencyCny.length() - 1);
                String[] currenciesFromCny = currencyCny.split(",");
                for (String curr : currenciesFromCny) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "JPY":
                String currencyJpy = readFromFile("GBP.txt");
                currencyJpy = currencyJpy.substring(1, currencyJpy.length() - 1);
                String[] currenciesFromJpy = currencyJpy.split(",");
                for (String curr : currenciesFromJpy) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
            case "KRW":
                String currencyKrw = readFromFile("GBP.txt");
                currencyKrw = currencyKrw.substring(1, currencyKrw.length() - 1);
                String[] currenciesFromKrw = currencyKrw.split(",");
                for (String curr : currenciesFromKrw) {
                    String rate = curr.split(":")[1];
                    String currencyTo1 = curr.split(":")[0];
                    currencyTo1 = currencyTo1.substring(1, currencyTo1.length() - 1);
                    if (currencyTo.equalsIgnoreCase(currencyTo1)) {
                        convertCurrency(amount, Double.parseDouble(rate));
                    }
                }
                break;
        }
    }

    private String readFromFile(String filename) throws IOException {

        String output = "";
        File fileToRead = getActivity().getBaseContext().getFileStreamPath(filename);
        BufferedReader br = new BufferedReader(new FileReader(fileToRead));
        String tempLine = br.readLine();
        while (tempLine != null) {
            output += tempLine;
            tempLine = br.readLine();
        }
        return output;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}