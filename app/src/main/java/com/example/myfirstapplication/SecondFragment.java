package com.example.myfirstapplication;

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
            LayoutInflater inflater, ViewGroup container,
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
        if(result.getStringArrayList("dividers").size() == 1){
            binding.dividerPointData.setText(R.string.divider_point_coordinate);
        }
        else {
            binding.dividerPointData.setText(R.string.divider_points_coordinates);
        }
        StringBuilder sb = new StringBuilder();
        for (String divider : result.getStringArrayList("dividers")) {
            sb.append(divider);
            sb.append("\n\n");
        }
        binding.dividerPointsData.setText(sb.toString());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}