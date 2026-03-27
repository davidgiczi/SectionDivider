package com.example.myfirstapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myfirstapplication.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public ViewGroup container;
    public static String mainLineStartY;
    public static String mainLineStartX;
    public static String mainLineStartZ;
    public static String mainLineEndY;
    public static String mainLineEndX;
    private String crossedLineStartY;
    private String crossedLineStartX;
    private String crossedLineStartZ;
    private String crossedLineEndY;
    private String crossedLineEndX;
    private static final List<String> INVALID_INPUT_CHARS = Arrays.asList(" ", ".", ",", "-", ".-", "-.", ",-", "-," );

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        container = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_intersection, null);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
    public static boolean isInvalidInputChars(String inputData){
        if( inputData.length() == 1 && INVALID_INPUT_CHARS.contains(inputData) ){
            return true;
        }
        else return inputData.length() == 2 && INVALID_INPUT_CHARS.contains(inputData);
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
        PopupWindow intersectionWindow = new PopupWindow(container, 1050, 1500, true);
        intersectionWindow.showAtLocation(binding.getRoot(), Gravity.CENTER, 0, -250);
        EditText startYField = container.findViewById(R.id.start_y_input_field);
        EditText startXField = container.findViewById(R.id.start_x_input_field);
        EditText endYField = container.findViewById(R.id.end_y_input_field);
        EditText endXField = container.findViewById(R.id.end_x_input_field);
        ((TextView) container.findViewById(R.id.intersection_point_data)).setText(null);
        EditText firstElevationField = binding.getRoot().findViewById(R.id.outside_y_field);
        if( firstElevationField != null ){
            firstElevationField.setText(null);
        }
        EditText secondElevationField = binding.getRoot().findViewById(R.id.outside_x_field);
        if( secondElevationField != null ){
            secondElevationField.setText(null);
        }
        setInputData();
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

            setInputData();
            Double firstAngle = isValidIntersectionByAnglesInputData(mainLineEndY);
            Double secondAngle = isValidIntersectionByAnglesInputData(crossedLineEndY);
            Double firstVerticalAngle = isValidVerticalAngleInputData(mainLineEndX);
            Double secondVerticalAngle = isValidVerticalAngleInputData(crossedLineEndX);
            Double firstElevation = isValidElevationInputData(mainLineStartZ);
            Double secondElevation = isValidElevationInputData(crossedLineStartZ);

            if( firstAngle != null && secondAngle != null &&
                    isValidCrossedLinesInputData(crossedLineStartY, crossedLineStartX, crossedLineEndY, crossedLineEndX) ){

                String intersectionPointByAngles = Calculator.calcIntersectionByAngles(
                        new Point("MainStartPoint",
                                Double.parseDouble(mainLineStartY.replace(",", ".")),
                                Double.parseDouble(mainLineStartX.replace(",", "."))),
                        new Point("CrossedStartPoint",
                                Double.parseDouble(crossedLineStartY.replace(",", ".")),
                                Double.parseDouble(crossedLineStartX.replace(",", "."))),
                        firstAngle, secondAngle,
                        firstVerticalAngle, secondVerticalAngle, firstElevation, secondElevation);
                if( intersectionPointByAngles == null ){
                    TextView errorText = container.findViewById(R.id.intersection_point_data);
                    errorText.setTextColor(Color.RED);
                    errorText.setText(R.string.error_intersection);
                }
                else{
                    TextView resultPointData = container.findViewById(R.id.intersection_point_data);
                    resultPointData.setTextColor(Color.BLUE);
                    resultPointData.setText(intersectionPointByAngles);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(ClipData.newPlainText("Copied Data", resultPointData.getText()));
                    if( Calculator.ELEVATION_FIRST == null ){
                        return;
                    }
                    if( firstElevationField != null ){
                        firstElevationField.setText(Calculator.ELEVATION_FIRST);
                    }
                    Calculator.ELEVATION_FIRST = null;
                    if( Calculator.ELEVATION_SECOND == null ){
                        return;
                    }
                    if( secondElevationField != null ){
                        secondElevationField.setText(Calculator.ELEVATION_SECOND);
                    }
                    Calculator.ELEVATION_SECOND = null;
                }
            }
            else if( isValidCrossedLinesInputData(crossedLineStartY, crossedLineStartX, crossedLineEndY, crossedLineEndX) &&
                    isAngle(mainLineEndY) && isAngle(crossedLineEndY)){

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
                       TextView errorText = container.findViewById(R.id.intersection_point_data);
                       errorText.setTextColor(Color.RED);
                       errorText.setText(R.string.error_intersection);
                   } else {
                       TextView resultPointData = container.findViewById(R.id.intersection_point_data);
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
        EditText mainLineStartYField = binding.getRoot().findViewById(R.id.start_y_input_field);
        if( mainLineStartYField != null ){
            mainLineStartY = mainLineStartYField.getText().toString();
        }
        EditText mainLineStartXField = binding.getRoot().findViewById(R.id.start_x_input_field);
        if(  mainLineStartXField != null ) {
            mainLineStartX = mainLineStartXField.getText().toString();
        }
        EditText mainLineEndYField = binding.getRoot().findViewById(R.id.end_y_input_field);
        if( mainLineEndYField != null ) {
            mainLineEndY = mainLineEndYField.getText().toString();
        }
        EditText mainLineEndXField = binding.getRoot().findViewById(R.id.end_x_input_field);
        if( mainLineEndXField != null ){
            mainLineEndX = mainLineEndXField.getText().toString();
        }

        if( mainLineStartY.trim().isEmpty() || MainActivity.isInvalidInputChars(mainLineStartY) ){
            Toast.makeText(this, "Az alapvonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( mainLineStartX.trim().isEmpty() || MainActivity.isInvalidInputChars(mainLineStartX) ){
            Toast.makeText(this, "Az alapvonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( mainLineEndY.trim().isEmpty() || MainActivity.isInvalidInputChars(mainLineEndY) ){
            Toast.makeText(this, "Az alapvonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( (mainLineEndX.trim().isEmpty() || MainActivity.isInvalidInputChars(mainLineEndX)) && isAngle(mainLineEndY)){
            Toast.makeText(this, "Az alapvonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( startY.trim().isEmpty() || MainActivity.isInvalidInputChars(startY.trim()) ){
            Toast.makeText(this, "A keresztezett vonal kezdőpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( startX.trim().isEmpty() || MainActivity.isInvalidInputChars(startX.trim())){
            Toast.makeText(this, "A keresztezett vonal kezdőpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( endY.trim().isEmpty() || MainActivity.isInvalidInputChars(endY.trim())){
            Toast.makeText(this, "A keresztezett vonal végpont Y koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        else if( (endX.trim().isEmpty() || MainActivity.isInvalidInputChars(endX.trim())) && isAngle(endY)){
            Toast.makeText(this, "A keresztezett vonal végpont X koordinátájának megadása szükséges.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isAngle(String inputData){
        return inputData == null ||
                (!inputData.trim().startsWith(".") &&
                        !inputData.trim().startsWith(","));
    }

    private Double isValidIntersectionByAnglesInputData(String angleValue){

         if( angleValue.trim().isEmpty() || isInvalidInputChars(angleValue.trim())){
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

        return angle + min / 60.0 + sec / 3600.0;
    }

    private Double isValidVerticalAngleInputData(String angleValue){

        if( angleValue.trim().isEmpty() || isInvalidInputChars(angleValue.trim())){
            return null;
        }
        else if( !angleValue.trim().startsWith(".") && !angleValue.trim().startsWith(",") ){
            return null;
        }
        else if( 6 > angleValue.length() || 8 < angleValue.length() ){
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

        if( angle > 179 || angle < 0){
            return null;
        }
        else if( min > 59 || min < 0){
            return null;
        }
        else if( sec > 59 || sec < 0){
            return null;
        }

        return Math.toRadians(angle + min / 60.0 + sec / 3600.0);
    }

    private Double isValidElevationInputData(String inputElevation){
        Double elevation = null;
        try{
            elevation = Double.parseDouble(inputElevation);
        }
        catch (NumberFormatException ignored){

        }
        return elevation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

       if( item.getItemId() == R.id.option_intersection ){
            popupCrossedLinesIntersectionDialog();
        }
       else if( item.getItemId() == R.id.option_exit){
           exitAppDialog();
       }
       else if( item.getItemId() == R.id.exchange_data ){
           exchangeData();
       }

        return super.onOptionsItemSelected(item);
    }

    private void exchangeData(){
        EditText mainLineStartYField = binding.getRoot().findViewById(R.id.start_y_input_field);
        if( mainLineStartYField == null || mainLineStartYField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Alapvonal kezdőpont Y koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText mainLineStartXField = binding.getRoot().findViewById(R.id.start_x_input_field);
        if( mainLineStartXField == null || mainLineStartXField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Alapvonal kezdőpont X koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText mainLineEndYField = binding.getRoot().findViewById(R.id.end_y_input_field);
        if( mainLineEndYField == null || mainLineEndYField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Alapvonal végpont Y koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText mainLineEndXField = binding.getRoot().findViewById(R.id.end_x_input_field);
        if( mainLineEndXField == null || mainLineEndXField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Alapvonal végpont X koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText crossedLineStartYField = container.findViewById(R.id.start_y_input_field);
        if( crossedLineStartYField == null || crossedLineStartYField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Keresztezett vonal kezdőpont Y koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText crossedLineStartXField = container.findViewById(R.id.start_x_input_field);
        if( crossedLineStartXField == null || crossedLineStartXField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Keresztezett vonal kezdőpont X koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText crossedLineEndYField = container.findViewById(R.id.end_y_input_field);
        if( crossedLineEndYField == null || crossedLineEndYField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Keresztezett vonal végpont Y koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        EditText crossedLineEndXField = container.findViewById(R.id.end_x_input_field);
        if( crossedLineEndXField == null || crossedLineEndXField.getText().toString().isEmpty() ){
            Toast.makeText(this, "Keresztezett vonal végpont X koordináta megadása szükséges.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String mainLineStartY = mainLineStartYField.getText().toString().replace(",", ".");
        String mainLineStartX = mainLineStartXField.getText().toString().replace(",", ".");
        EditText firstElevationField = binding.getRoot().findViewById(R.id.start_z_input_field);
        String firstElevation = firstElevationField.getText().toString().replace("," , ".");
        EditText secondElevationField = container.findViewById(R.id.start_z_input_field);
        String secondElevation = secondElevationField.getText().toString().replace(",", ".");
        secondElevationField.setText(firstElevation);
        firstElevationField.setText(secondElevation);
        String mainLineEndY = mainLineEndYField.getText().toString().replace(",", ".");
        String mainLineEndX = mainLineEndXField.getText().toString().replace(",", ".");
        String crossedLineStartY = crossedLineStartYField.getText().toString().replace(",", ".");
        String crossedLineStartX = crossedLineStartXField.getText().toString().replace(",", ".");
        String crossedLineEndY = crossedLineEndYField.getText().toString().replace(",", ".");
        String crossedLineEndX = crossedLineEndXField.getText().toString().replace(",", ".");
        this.crossedLineStartY = mainLineStartY;
        this.crossedLineStartX = mainLineStartX;
        this.crossedLineEndY = mainLineEndY;
        this.crossedLineEndX = mainLineEndX;
        MainActivity.mainLineStartY = crossedLineStartY;
        MainActivity.mainLineStartX = crossedLineStartX;
        MainActivity.mainLineEndY = crossedLineEndY;
        MainActivity.mainLineEndX = crossedLineEndX;
        crossedLineStartYField.setText(this.crossedLineStartY);
        crossedLineStartXField.setText(this.crossedLineStartX);
        crossedLineEndYField.setText(this.crossedLineEndY);
        crossedLineEndXField.setText(this.crossedLineEndX);
        mainLineStartYField.setText(MainActivity.mainLineStartY);
        mainLineStartXField.setText(MainActivity.mainLineStartX);
        mainLineEndYField.setText(MainActivity.mainLineEndY);
        mainLineEndXField.setText(MainActivity.mainLineEndX);
    }

    private void setInputData(){
        EditText mainLineStartYField = binding.getRoot().findViewById(R.id.start_y_input_field);
        if( mainLineStartYField != null ){
            MainActivity.mainLineStartY = mainLineStartYField.getText().toString().replace(",", ".");
        }
        EditText mainLineStartXField = binding.getRoot().findViewById(R.id.start_x_input_field);
        if( mainLineStartXField != null ){
            MainActivity.mainLineStartX = mainLineStartXField.getText().toString().replace(",", ".");
        }
        EditText mainLineStartZField = binding.getRoot().findViewById(R.id.start_z_input_field);
        if( mainLineStartZField != null ){
            MainActivity.mainLineStartZ = mainLineStartZField.getText().toString().replace(",", ".");
        }
        EditText mainLineEndYField = binding.getRoot().findViewById(R.id.end_y_input_field);
        if( mainLineEndYField != null ){
            MainActivity.mainLineEndY = mainLineEndYField.getText().toString().replace(",", ".");
        }
        EditText mainLineEndXField =  binding.getRoot().findViewById(R.id.end_x_input_field);
        if( mainLineEndXField != null ) {
            MainActivity.mainLineEndX = mainLineEndXField.getText().toString().replace(",", ".");
        }
        this.crossedLineStartY = ((EditText) container
                .findViewById(R.id.start_y_input_field)).getText().toString().replace(",", ".");
        this.crossedLineStartX = ((EditText) container
                .findViewById(R.id.start_x_input_field)).getText().toString().replace(",", ".");
        this.crossedLineStartZ = ((EditText) container
                .findViewById(R.id.start_z_input_field)).getText().toString().replace(",", ".");
        this.crossedLineEndY = ((EditText) container
                .findViewById(R.id.end_y_input_field)).getText().toString().replace(",", ".");
        this.crossedLineEndX = ((EditText) container
                .findViewById(R.id.end_x_input_field)).getText().toString().replace(",", ".");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}