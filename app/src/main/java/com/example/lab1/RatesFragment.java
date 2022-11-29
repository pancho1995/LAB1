package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.lab1.databinding.ActivityMainBinding;
import com.example.lab1.databinding.FragmentRatesBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RatesFragment extends Fragment {

    private FragmentRatesBinding binding;
    private Toolbar toolbar;
    private String[] currencies;
    private View view;
    private boolean readFromOldResource = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRatesBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
        TextView ratesOnDate = getActivity().findViewById(R.id.ratesOnDate);
        ratesOnDate.setText(((MainActivity) getActivity()).getLastFetchedDate());
        readFromOldResource = ((MainActivity) getActivity()).getReadFromOldResource();

        String baseCurrency = ((MainActivity) getActivity()).getBaseCurrency();

        Spinner spinnerCurr_Change = (Spinner) view.findViewById(R.id.curr_change);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spinnerCurr_Change.getContext(),
                R.array.currencies_for_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurr_Change.setAdapter(adapter);

        if (baseCurrency != null) {
            setSelectionByUserLocation(baseCurrency);
        }

        spinnerCurr_Change.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedBase = spinnerCurr_Change.getSelectedItem().toString();
                if (readFromOldResource) {
                    fetchDataFromResourceFixed(selectedBase);
                } else {
                    try {
                        fetchDataFromResourceAPI(selectedBase);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().getItem(0).setVisible(false);
    }

    private String justifyText(String textToJustify, Boolean fromAPI) {
        String regex = fromAPI ? ":" : ";";
        String[] textSeparated = textToJustify.split(regex);
        while (textSeparated[0].length() < 20) {
            textSeparated[0] += " ";
        }
        while (textSeparated[1].length() != 7) {
            textSeparated[1] += "0";
        }
        return textSeparated[0] + textSeparated[1];
    }

    private void fetchDataFromResourceAPI(String baseCurrency) throws IOException {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rowView);
        linearLayout.removeAllViews();
        this.currencies = null;

        switch (baseCurrency) {
            case "EUR":
                String euroRates = readFromFile("EUR.txt");
                this.currencies = justifyRatesFromAPI(euroRates);
                break;
            case "SEK":
                String sekRates = readFromFile("SEK.txt");
                this.currencies = justifyRatesFromAPI(sekRates);
                break;
            case "USD":
                String usdRates = readFromFile("USD.txt");
                this.currencies = justifyRatesFromAPI(usdRates);
                break;
            case "GBP":
                String gbpRates = readFromFile("GBP.txt");
                this.currencies = justifyRatesFromAPI(gbpRates);
                break;
            case "CNY":
                String cnyRates = readFromFile("CNY.txt");
                this.currencies = justifyRatesFromAPI(cnyRates);
                break;
            case "JPY":
                String jpyRates = readFromFile("JPY.txt");
                this.currencies = justifyRatesFromAPI(jpyRates);
                break;
            case "KRW":
                String krwRates = readFromFile("KRW.txt");
                this.currencies = justifyRatesFromAPI(krwRates);
                break;
        }

        TextView dateRates = getView().findViewById(R.id.ratesOnDate);
        String lastDateFetched = ((MainActivity) getActivity()).getLastFetchedDate();
        dateRates.setText(lastDateFetched);

        for (String curr : this.currencies) {
            TextView txtView = new TextView(view.getContext());
            txtView.setText(curr);
            txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtView.setTextSize(30);
            txtView.setPadding(10, 10, 10, 0);
            linearLayout.addView(txtView);
        }

    }

    private void fetchDataFromResourceFixed(String baseCurrency) {

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rowView);
        linearLayout.removeAllViews();
        this.currencies = null;

        switch (baseCurrency) {
            case "EUR":
                this.currencies = getResources().getStringArray(R.array.EUR);
                break;
            case "SEK":
                this.currencies = getResources().getStringArray(R.array.SEK);
                break;
            case "USD":
                this.currencies = getResources().getStringArray(R.array.USD);
                break;
            case "GBP":
                this.currencies = getResources().getStringArray(R.array.GBP);
                break;
            case "CNY":
                this.currencies = getResources().getStringArray(R.array.CNY);
                break;
            case "JPY":
                this.currencies = getResources().getStringArray(R.array.JPY);
                break;
            case "KRW":
                this.currencies = getResources().getStringArray(R.array.KRW);
                break;
        }

        for (String curr : this.currencies) {
            TextView txtView = new TextView(view.getContext());
            txtView.setText(justifyText(curr, false));
            txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtView.setTextSize(30);
            txtView.setPadding(10, 10, 10, 0);
            linearLayout.addView(txtView);
        }
    }

    private void setSelectionByUserLocation(String country) {

        Spinner spinnerCurr_Change = view.findViewById(R.id.curr_change);
        switch (country) {
            case "United States":
                spinnerCurr_Change.setSelection(2);
                break;
            case "Sweden":
                spinnerCurr_Change.setSelection(1);
                break;
            case "Great Britain":
                spinnerCurr_Change.setSelection(3);
                break;
            case "China":
                spinnerCurr_Change.setSelection(4);
                break;
            case "Japan":
                spinnerCurr_Change.setSelection(5);
                break;
            case "South Korea":
                spinnerCurr_Change.setSelection(6);
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

    private String[] justifyRatesFromAPI(String rates) {
        rates = rates.substring(1, rates.length() - 1);
        String[] ratesArr = rates.split(",");
        return ratesArr;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}