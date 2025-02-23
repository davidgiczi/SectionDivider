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
            @NonNull LayoutInflater inflater, ViewGroup container,
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
                    binding.outsideYField.setText(binding.startYInputField.getText().toString().substring(0, 3));
                }
                else if(binding.startYInputField.getText().toString().length() < 3 ){
                    binding.endYInputField.setText("");
                    binding.outsideYField.setText("");
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
                    binding.outsideXField.setText(binding.startXInputField.getText().toString().substring(0, 3));
                }
                else if(binding.startXInputField.getText().toString().length() < 3 ){
                    binding.endXInputField.setText("");
                    binding.outsideXField.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.buttonFirst.setOnClickListener(view1 -> {

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
                    Double.parseDouble(binding.startYInputField.getText().toString().replace(",", ".")),
                    Double.parseDouble(binding.startXInputField.getText().toString().replace(",", ".")));
            Point endPoint = new Point("Végpont:",
                    Double.parseDouble(binding.endYInputField.getText().toString().replace(",", ".")),
                    Double.parseDouble(binding.endXInputField.getText().toString().replace(",", ".")));
            Point outsiderPoint;
            int numberOfDividerPoints =
                    Integer.parseInt(binding.numberOfDividerPointsInputField.getText().toString());
            Calculator calc = new Calculator(startPoint,endPoint, numberOfDividerPoints);
            if( !binding.outsideYField.getText().toString().isEmpty() &&
                    !binding.outsideXField.getText().toString().isEmpty() ){
                outsiderPoint  = new Point("Outsider",
                        Double.parseDouble(binding.outsideYField.getText().toString().replace(",", ".")),
                        Double.parseDouble(binding.outsideXField.getText().toString().replace(",", ".")));
                calc.setOutsiderPoint(outsiderPoint);
            }
            Bundle resultData = new Bundle();
            resultData.putString("length", calc.getLengthOfSection());
            resultData.putString("distance", calc.getDistanceBetweenPoints());
            resultData.putStringArrayList("dividers", calc.getDividerPointsAsString());
            resultData.putString("dividerValue", binding.numberOfDividerPointsInputField.getText().toString());
            if( calc.calcPointInsideSection() != null){
                resultData.putString("insider", calc.calcPointInsideSection().toString());
                resultData.putString("abscissa", calc.getAbscissa());
                resultData.putString("ordinate", calc.getOrdinate());
                resultData.putString("abscissaError", calc.getAbscissaErrorMargin());
                resultData.putString("ordinateError", calc.getOrdinateErrorMargin());
                resultData.putBoolean("isOkAbscissa", calc.isOkAbscissaValue());
                resultData.putBoolean("isOkOrdinate", calc.isOkOrdinateValue());
            }
            getParentFragmentManager().setFragmentResult("results", resultData);
            NavHostFragment.findNavController(FirstFragment.this)
                   .navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}