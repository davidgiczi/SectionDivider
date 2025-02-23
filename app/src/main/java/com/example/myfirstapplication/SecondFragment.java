package com.example.myfirstapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfirstapplication.databinding.FragmentSecondBinding;
import java.util.Objects;


public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("results", this, (requestKey, result) -> {
        binding.lengthOfSection.setText(Objects.requireNonNull(result.get("length")).toString());
        binding.distanceBetweenPoints.setText(Objects.requireNonNull(result.get("distance")).toString());

        if(Objects.requireNonNull(result.get("dividerValue")).toString().equals("0") ||
                Objects.requireNonNull(result.getStringArrayList("dividers")).isEmpty()){
            binding.dividerPointDataText.setText("");
        }
        else if(Objects.requireNonNull(result.getStringArrayList("dividers")).size() == 1){
            binding.dividerPointDataText.setText(R.string.divider_point_coordinate);
        }
        else {
            binding.dividerPointDataText.setText(R.string.divider_points_coordinates);
        }
        StringBuilder sb = new StringBuilder();
        for (String divider : Objects.requireNonNull(result.getStringArrayList("dividers"))) {
            sb.append(divider);
            sb.append("\n\n");
        }
        binding.dividerPointsData.setText(sb.toString());
        if( result.get("insider") == null ){
            return;
        }
        binding.insidePointData.setText(Objects.requireNonNull(result.get("insider")).toString());
        if( result.getBoolean("isOkAbscissa") ){
            binding.abscissa.setTextColor(Color.parseColor("#3BB143"));
        }
        else{
            binding.abscissa.setTextColor(Color.RED);
        }
        String abscissaData = result.get("abscissa") + " " + result.get("abscissaError");
        binding.abscissa.setText(abscissaData);
        if( result.getBoolean("isOkOrdinate") ){
            binding.ordinate.setTextColor(Color.parseColor("#3BB143"));
        }
        else{
            binding.ordinate.setTextColor(Color.RED);
        }
        String ordinateData = result.get("ordinate") + " " + result.get("ordinateError");
        binding.ordinate.setText(ordinateData);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}