package com.example.myfirstapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    public static String mainLineStartY;
    public static String mainLineStartX;
    public static String mainLineEndY;
    public static String mainLineEndX;
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

    public void popupCrossedLinesIntersectionDialog(){
        ViewGroup container = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_intersection, null);
        PopupWindow intersectionWindow = new PopupWindow(container, 1050, 1500, true);
        intersectionWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, -250);
        EditText startYField = container.findViewById(R.id.start_y_input_field);
        EditText startXField = container.findViewById(R.id.start_x_input_field);
        EditText endYField = container.findViewById(R.id.end_y_input_field);
        EditText endXField = container.findViewById(R.id.end_x_input_field);
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

            EditText mainLineEndYField = (EditText) binding.getRoot().findViewById(R.id.end_y_input_field);
            Double firstAngle = isValidIntersectionByAnglesInputData(mainLineEndY == null ?
            mainLineEndYField.getText().toString() : mainLineEndY);
            Double secondAngle = isValidIntersectionByAnglesInputData(crossedLineEndY);

            if( firstAngle != null && secondAngle != null &&
                    isValidCrossedLinesInputData(crossedLineStartY, crossedLineStartX, crossedLineEndY, crossedLineEndX) ){

                String intersectionPointByAngles = Calculator.calcIntersectionByAngles(
                        new Point("MainStartPoint",
                                Double.parseDouble(mainLineStartY.replace(",", ".")),
                                Double.parseDouble(mainLineStartX.replace(",", "."))),
                        new Point("CrossedStartPoint",
                                Double.parseDouble(crossedLineStartY.replace(",", ".")),
                                Double.parseDouble(crossedLineStartX.replace(",", "."))),
                        firstAngle, secondAngle);
                if( intersectionPointByAngles == null ){
                    TextView errorText = (TextView) container.findViewById(R.id.intersection_point_data);
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.error_intersection);
                }
                else{
                    TextView resultPointData = (TextView) container.findViewById(R.id.intersection_point_data);
                    resultPointData.setTextColor(Color.BLUE);
                    resultPointData.setText(intersectionPointByAngles);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(ClipData.newPlainText("Copied Data", resultPointData.getText()));
                }

            }
            else if( isValidCrossedLinesInputData(crossedLineStartY, crossedLineStartX, crossedLineEndY, crossedLineEndX) &&
                        !isAngle(mainLineEndY) && !isAngle(crossedLineEndY) ){

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
                   if (crossingPointData == null) {
                       TextView errorText = (TextView) container.findViewById(R.id.intersection_point_data);
                       errorText.setTextColor(Color.RED);
                       errorText.setText(R.string.error_intersection);
                   } else {
                       TextView resultPointData = (TextView) container.findViewById(R.id.intersection_point_data);
                       resultPointData.setTextColor(Color.BLUE);
                       resultPointData.setText(crossingPointData);
                       ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                       clipboard.setPrimaryClip(ClipData.newPlainText("Copied Data", resultPointData.getText()));
                   }
               }
            else {
                Toast.makeText(this, "Hiányzó bemeneti adatok.", Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean isValidCrossedLinesInputData(String startY, String startX, String endY, String endX){
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
            Toast.makeText(this, "Az alapvonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( mainLineStartX.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( mainLineEndY.trim().isEmpty() ){
            Toast.makeText(this, "Az alapvonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( mainLineEndX.trim().isEmpty() && !isAngle(mainLineEndY)){
            Toast.makeText(this, "Az alapvonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( startY.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( startX.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( endY.trim().isEmpty() ){
            Toast.makeText(this, "A keresztezett vonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( endX.trim().isEmpty() && !isAngle(endY)){
            Toast.makeText(this, "A keresztezett vonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isAngle(String inputData){
        return inputData != null &&
                (inputData.trim().startsWith(".") ||
                inputData.trim().startsWith(","));
    }

    private Double isValidIntersectionByAnglesInputData(String angleValue){

         if( angleValue.trim().isEmpty() ){
             Toast.makeText(this, "Irányszög megadása szükséges.",
                     Toast.LENGTH_LONG).show();
            return null;
        }
        else if( !angleValue.trim().startsWith(".") && !angleValue.trim().startsWith(",") ){
            return null;
        }
        else if( 6 > angleValue.length() || 8 < angleValue.length() ){
            Toast.makeText(this, "A bevitt irányszög túl sok vagy kevés számértéket tarlamaz.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        int angle = 0;
        int min = 0;
        int sec = 0;
        switch (angleValue.length() ){
            case 6 :
                angle = Integer.parseInt(angleValue.substring(1,2));
                min = Integer.parseInt(angleValue.substring(2,4));
                sec = Integer.parseInt(angleValue.substring(4));
                break;
            case 7 :
                angle = Integer.parseInt(angleValue.substring(1,3));
                min = Integer.parseInt(angleValue.substring(3,5));
                sec = Integer.parseInt(angleValue.substring(5));
                break;
            case 8 :
                angle = Integer.parseInt(angleValue.substring(1,4));
                min = Integer.parseInt(angleValue.substring(4,6));
                sec = Integer.parseInt(angleValue.substring(6));
        }

        if( angle > 359 || angle < 0){
            Toast.makeText(this, "Az irányszög fok értéke 0 =< fok =< 359 lehet.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        else if( min > 59 || min < 0){
            Toast.makeText(this, "Az irányszög perc értéke 0 =< perc =< 59 lehet.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
        else if( sec > 59 || sec < 0){
            Toast.makeText(this, "Az irányszög mperc értéke 0 =< mperc =< 59 lehet.",
                    Toast.LENGTH_LONG).show();
            return null;
        }
            Log.d("angle", ": " + angle);
            Log.d("min", ": " + min);
            Log.d("sec", ": " + sec);
        return angle + min / 60.0 + sec / 3600.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if( item.getItemId() == R.id.option_intersection ){
            popupCrossedLinesIntersectionDialog();
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