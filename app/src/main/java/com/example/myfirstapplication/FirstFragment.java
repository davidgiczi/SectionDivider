package com.example.myfirstapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myfirstapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceStat
    ) {
        binding =  FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.startYInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.startYInputField.getText().toString().length() > 3 ){
                    binding.endYInputField.setText(binding.startYInputField.getText().toString().substring(0, 3));
                }
                else if(binding.startYInputField.getText().toString().length() < 3 ){
                    binding.endYInputField.setText("");
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.startXInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(binding.startXInputField.getText().toString().length() > 3 ){
                    binding.endXInputField.setText(binding.startXInputField.getText().toString().substring(0, 3));
                }
                else if(binding.startXInputField.getText().toString().length() < 3 ){
                    binding.endXInputField.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast inputDataToast;
                if(binding.startYInputField.getText().toString().isEmpty()){
                    inputDataToast = Toast.makeText(getContext(),
                            R.string.start_y_coord_field_is_empty_msg, Toast.LENGTH_LONG);
                    inputDataToast.show();
                    return;
                }
                else if(binding.startXInputField.getText().toString().isEmpty()){
                    inputDataToast = Toast.makeText(getContext(),
                            R.string.start_x_coord_field_is_empty_msg, Toast.LENGTH_LONG);
                    inputDataToast.show();
                    return;
                }
                else if(binding.endYInputField.getText().toString().isEmpty()){
                    inputDataToast = Toast.makeText(getContext(),
                            R.string.end_y_coord_field_is_empty_msg, Toast.LENGTH_LONG);
                    inputDataToast.show();
                    return;
                }
                else if(binding.endXInputField.getText().toString().isEmpty()){
                    inputDataToast = Toast.makeText(getContext(),
                            R.string.end_x_coord_field_is_empty_msg, Toast.LENGTH_LONG);
                    inputDataToast.show();
                    return;
                }
                else if(binding.numberOfDividerPointsInputField.getText().toString().isEmpty()){
                    inputDataToast = Toast.makeText(getContext(),
                            "Az osztópontok számának megadása szükséges.", Toast.LENGTH_LONG);
                    inputDataToast.show();
                    return;
                }

                Point startPoint = new Point("Kezdőpont:",
                        Double.parseDouble(binding.startYInputField.getText().toString()),
                        Double.parseDouble(binding.startXInputField.getText().toString()));
                Point endPoint = new Point("Végpont:",
                        Double.parseDouble(binding.endYInputField.getText().toString()),
                        Double.parseDouble(binding.endXInputField.getText().toString()));
                int numberOfDividerPoints =
                        Integer.parseInt(binding.numberOfDividerPointsInputField.getText().toString());
                Calculator calc = new Calculator(startPoint,endPoint, numberOfDividerPoints);
                Bundle resultData = new Bundle();
                resultData.putString("length", calc.getLengthOfSection());
                resultData.putString("distance", calc.getDistanceBetweenPoints());
                resultData.putStringArrayList("dividers", calc.getDividerPointsAsString());
                getParentFragmentManager().setFragmentResult("results", resultData);
                NavHostFragment.findNavController(FirstFragment.this)
                       .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}