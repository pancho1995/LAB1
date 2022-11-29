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

public class RatesFragment extends Fragment {

    private FragmentRatesBinding binding;
    private Toolbar toolbar;
    private String [] currencies;
    private View view;

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

        String baseCurrency = ((MainActivity)getActivity()).getBaseCurrency();

        Spinner spinnerCurr_Change = (Spinner) view.findViewById(R.id.curr_change);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(spinnerCurr_Change.getContext(),
                R.array.currencies_for_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurr_Change.setAdapter(adapter);
        if(baseCurrency != null) {
            setSelectionByUserLocation(baseCurrency);
        }

        spinnerCurr_Change.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedBase = spinnerCurr_Change.getSelectedItem().toString();
                fetchDataFromResourceFixed(selectedBase);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().getItem(0).setVisible(false);
    }

    private String justifyText(String textToJustify) {
        String[] textSeparated = textToJustify.split(";");
        while (textSeparated[0].length() < 20) {
            textSeparated[0] += " ";
        }
        while (textSeparated[1].length() != 7) {
            textSeparated[1] += "0";
        }
        return textSeparated[0] + textSeparated[1];
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
            txtView.setText(justifyText(curr));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}