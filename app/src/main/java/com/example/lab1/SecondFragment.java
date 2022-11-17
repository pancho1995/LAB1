package com.example.lab1;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lab1.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
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
        Spinner s = (Spinner) view.findViewById(R.id.spinner3);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.rowView);
        String [] currencies = getResources().getStringArray(R.array.currencies);

        for (String curr: currencies) {
            LinearLayout horLayout = new LinearLayout(view.getContext());
            horLayout.setOrientation(LinearLayout.HORIZONTAL);
//            horLayout.layout(16, 0, 16, 0);


            TextView txtView = new TextView(view.getContext());
            txtView.setText(curr);
            txtView.setWidth(300);

            TextView value = new TextView(view.getContext());
            value.setText("0.93");
            value.setWidth(300);
            value.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            horLayout.addView(txtView);
            horLayout.addView(value);

//            horLayout.setHorizontalGravity(Gravity.RIGHT);
            linearLayout.addView(horLayout);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}