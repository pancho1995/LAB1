package com.example.lab1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab1.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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
        Spinner s = (Spinner) view.findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(s.getContext(),
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        TextView amount = view.findViewById(R.id.editTextNumberDecimal5);
        amount.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length() != 0) {
                            convertCurrency(amount.getText().toString());
                        } else {
                            convertCurrency("");
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
    public void convertCurrency(String amount) {
        if(amount == "" || amount == null) {
            TextView result = getView().findViewById(R.id.editTextNumberDecimal6);
            result.setText("");
        } else {
            double converted = Double.parseDouble(amount) * 0.01;
            TextView result = getView().findViewById(R.id.editTextNumberDecimal6);
            result.setText(converted + "");
        }
        //logika za konvertovanje
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}