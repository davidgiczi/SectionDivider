package com.example.myfirstapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myfirstapplication.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String mainLineStartY;
    private String mainLineStartX;
    private String mainLineEndY;
    private String mainLineEndX;
    private String crossedLineStartY;
    private String crossedLineStartX;
    private String crossedLineEndY;
    private String crossedLineEndX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
    private void exitAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.close_app_title);
        builder.setMessage(R.string.close_app_question);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            System.exit(0);
            dialog.dismiss();
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void popupStartGameDialog(){
        ViewGroup container = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_intersection, null);
        PopupWindow intersectionWindow = new PopupWindow(container, 1050, 1500, true);
        intersectionWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, -250);
        EditText startYField = ((EditText) container.findViewById(R.id.start_y_input_field));
        EditText startXField = ((EditText) container.findViewById(R.id.start_x_input_field));
        EditText endYField = ((EditText) container.findViewById(R.id.end_y_input_field));
        EditText endXField = ((EditText) container.findViewById(R.id.end_x_input_field));
        startYField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(startYField.getText().toString().length() > 3 ){
                    endYField.setText(startYField.getText().toString().substring(0, 3));
                }
                else if(startYField.toString().length() < 3 ){
                   endYField.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        startXField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(startXField.getText().toString().length() > 3 ){
                    endXField.setText(startXField.getText().toString().substring(0, 3));
                }
                else if(startXField.toString().length() < 3 ){
                    endXField.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if( crossedLineStartY != null ){
            startYField.setText(crossedLineStartY);
        }
        if( crossedLineStartX != null ){
            startXField.setText(crossedLineStartX);
        }
        if( crossedLineEndY != null ){
            endYField.setText(crossedLineEndY);
        }
        if( crossedLineEndX != null ){
            endXField.setText(crossedLineEndX);
        }
        Button calcButton = container.findViewById(R.id.button_calc);
        calcButton.setOnClickListener(d -> {
            crossedLineStartY = startYField.getText().toString();
            crossedLineStartX = startXField.getText().toString();
            crossedLineEndY = endYField.getText().toString();
            crossedLineEndX = endXField.getText().toString();
            if( isValidInputData(crossedLineStartY, crossedLineStartX, crossedLineEndY, crossedLineEndX) ){
                String crossingPointData = Calculator.calcCrossedLinesIntersection(
                        new Point("MainStartPoint",
                                Double.parseDouble(mainLineStartY.replace(",", ".")),
                                Double.parseDouble(mainLineStartX.replace(",", "."))),
                        new Point("MainEndPoint",
                                Double.parseDouble(mainLineEndY.replace(",", ".")),
                                Double.parseDouble(mainLineEndX.replace(",", "."))),
                        new Point("CrossedStartPoint",
                                Double.parseDouble(crossedLineStartY.replace(",", ".")),
                                Double.parseDouble(crossedLineStartX.replace(",", "."))),
                        new Point("CrossedEndPoint",
                                Double.parseDouble(crossedLineEndY.replace(",", ".")),
                                Double.parseDouble(crossedLineEndX.replace(",", "."))));

                if( crossingPointData == null ){
                    TextView errorText = (TextView) container.findViewById(R.id.intersection_point_data);
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.error_intersection);
                }
                 else{
                    TextView resultPointData = (TextView) container.findViewById(R.id.intersection_point_data);
                    resultPointData.setTextColor(Color.BLUE);
                    resultPointData.setText(crossingPointData);
                }
            }
        });
    }
    private boolean isValidInputData(String startY, String startX, String endY, String endX){
        EditText mainLineStartYField = (EditText) binding.getRoot().findViewById(R.id.start_y_input_field);
        if( mainLineStartYField != null ){
            mainLineStartY = mainLineStartYField.getText().toString();
        }
        EditText mainLineStartXField = (EditText) binding.getRoot().findViewById(R.id.start_x_input_field);
        if(  mainLineStartXField != null ) {
            mainLineStartX = mainLineStartXField.getText().toString();
        }
        EditText mainLineEndYField = (EditText) binding.getRoot().findViewById(R.id.end_y_input_field);
        if( mainLineEndYField != null ) {
            mainLineEndY = mainLineEndYField.getText().toString();
        }
        EditText mainLineEndXField = (EditText) binding.getRoot().findViewById(R.id.end_x_input_field);
        if( mainLineEndXField != null ){
            mainLineEndX = mainLineEndXField.getText().toString();
        }

        if( mainLineStartY.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( mainLineStartX.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( mainLineEndY.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( mainLineEndX.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( startY.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( startX.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( endY.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if( endX.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if( item.getItemId() == R.id.option_intersection ){
            popupStartGameDialog();
        }
       else if( item.getItemId() == R.id.option_exit){
           exitAppDialog();
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}