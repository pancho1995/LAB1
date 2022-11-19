package com.example.lab1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.lab1.databinding.ActivityMainBinding;
import com.example.lab1.databinding.FragmentRatesBinding;

public class RatesFragment extends Fragment {

    private FragmentRatesBinding binding;
    private Toolbar toolbar;

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

//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.getMenu().getItem(0).setVisible(false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rowView);
        String [] currencies = getResources().getStringArray(R.array.currencies);

        for (String curr: currencies) {
            TextView txtView = new TextView(view.getContext());
            txtView.setText(justifyText(curr));
            txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtView.setTextSize(30);
            txtView.setPadding(10, 10, 10, 0);
            linearLayout.addView(txtView);
        }

    }

    private String justifyText(String textToJustify) {
        String [] textSeparated = textToJustify.split(";");
        while (textSeparated[0].length() < 20) {
            textSeparated[0] += " ";
        }
        while (textSeparated[1].length() != 7) {
            textSeparated[1] += "0";
        }
        return textSeparated[0] + textSeparated[1];
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}