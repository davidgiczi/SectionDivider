package com.example.myfirstapplication;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Calculator {

    private final Point startPoint;
    private final Point endPoint;
    private Point outsiderPoint;
    private final int numberOfDividerPoints;
    private final String lengthOfSection;
    private final String distanceBetweenPoints;
    private final List<Point> resultPoints;
    private static final DecimalFormat df = new DecimalFormat("0.0");


    public Calculator(Point startPoint, Point endPoint, int numberOfDividerPoints) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.numberOfDividerPoints = numberOfDividerPoints;
        this.resultPoints = new ArrayList<>();
        lengthOfSection = String.format(Locale.getDefault() ,"%.3fm", calcDistance(startPoint, endPoint))
                .replace(',', '.');
        distanceBetweenPoints =
                String.format(Locale.getDefault(),"%.3fm", calcDistance(startPoint, endPoint) / (numberOfDividerPoints + 1))
                .replace(',', '.');
        calcResultPoints();
    }

    public String getLengthOfSection() {
        return lengthOfSection;
    }

    public String getDistanceBetweenPoints() {
        return distanceBetweenPoints;
    }

    public void setOutsiderPoint(Point outsiderPoint) {
        this.outsiderPoint = outsiderPoint;
    }

    private static double calcDistance(Point startPoint, Point endPoint){
        return Math.sqrt(Math.pow(startPoint.getY_value() - endPoint.getY_value(), 2) +
                         Math.pow(startPoint.getX_value() - endPoint.getX_value(), 2));
    }
    private static Double calcAzimuth(Point startPoint, Point endPoint){

        double deltaX = endPoint.getY_value() - startPoint.getY_value();
        double deltaY = endPoint.getX_value() - startPoint.getX_value();

        if( deltaX > 0 && deltaY > 0 ) {
            return Math.atan(deltaX / deltaY);
        }
        else if( deltaX >= 0 &&  0 > deltaY ) {
            return Math.PI - Math.atan(deltaX / Math.abs(deltaY));
        }
        else if( 0 >= deltaX && 0 > deltaY ) {
            return Math.PI + Math.atan(Math.abs(deltaX) / Math.abs(deltaY));
        }
        else if( 0 >= deltaX && deltaY > 0 ) {
            return 2 * Math.PI - Math.atan(Math.abs(deltaX) / deltaY);
        }
        else if( deltaX > 0 ) {
            return Math.PI / 2;
        }
        else if(0 > deltaX) {
            return 3 * Math.PI / 2;
        }
        return Double.NaN;
    }

    private void calcResultPoints(){
        double distance = calcDistance(startPoint, endPoint) / (numberOfDividerPoints + 1);
       for (int i = 0; i < numberOfDividerPoints; i ++){
           if( calcAzimuth(startPoint, endPoint).isNaN() ){
               continue;
           }
           Point point = new Point(String.valueOf(i + 1),
                   startPoint.getY_value() + (i + 1) * distance * Math.sin(calcAzimuth(startPoint, endPoint)),
                   startPoint.getX_value() + (i + 1) * distance * Math.cos(calcAzimuth(startPoint, endPoint)));
               resultPoints.add(point);
       }
    }

    public ArrayList<String> getDividerPointsAsString(){
        ArrayList<String> resultAsString = new ArrayList<>();
        for (Point point : resultPoints) {
            resultAsString.add(point.toString());
        }
        return resultAsString;
    }

    public Point calcPointInsideSection(){
        if(outsiderPoint == null || calcAzimuth(startPoint, endPoint).isNaN() ||
                         calcAzimuth(startPoint, outsiderPoint).isNaN() ){
           return null;
        }
        double alfa = calcAzimuth(startPoint, endPoint) - calcAzimuth(startPoint, outsiderPoint);
        double distance = calcDistance(startPoint, outsiderPoint) * Math.cos(alfa);
        return new Point(String.valueOf(resultPoints.size() + 1),
                startPoint.getY_value() + distance * Math.sin(calcAzimuth(startPoint, endPoint)),
                startPoint.getX_value() + distance * Math.cos(calcAzimuth(startPoint, endPoint)));
    }

    public String getOrdinate(){
        double alfa = calcAzimuth(startPoint, endPoint) - calcAzimuth(startPoint, outsiderPoint);
        double distance = calcDistance(startPoint, outsiderPoint);
        return "Merőlegesen: " + (Math.sin(alfa) > 0 ? "+" : "") +
                String.format(Locale.getDefault(),"%.3fm",
                Math.sin(alfa) * distance)
                .replace(",", ".");
    }
    public String getAbscissa(){
        double alfa = calcAzimuth(startPoint, endPoint) - calcAzimuth(startPoint, outsiderPoint);
        double distance = calcDistance(startPoint, outsiderPoint);
        return "Vonalban: " + (Math.cos(alfa) > 0 ? "+" :  "")  +
                String.format(Locale.getDefault(),"%.3fm",
                 Math.cos( alfa) * distance)
                .replace(",", ".");
    }

    public String getAbscissaErrorMargin(){
        double lengthOfMainLine = calcDistance(startPoint, endPoint);
        return "|" + df.format(lengthOfMainLine / 4.0).replace("," , ".") + "cm|";
    }
    public String getOrdinateErrorMargin(){
        double lengthOfMainLine = calcDistance(startPoint, endPoint);
        return "|" + df.format(3 * lengthOfMainLine / 10).replace(",", ".") + "cm|";
    }

    public boolean isOkAbscissaValue(){
        double lengthOfMainLine = calcDistance(startPoint, endPoint);
        return 2.5 * lengthOfMainLine / 1000 >= calcDistance(startPoint, calcPointInsideSection()) ;
    }

    public boolean isOkOrdinateValue(){
        double lengthOfMainLine = calcDistance(startPoint, endPoint);
        return 3 * lengthOfMainLine / 1000 >= calcDistance(outsiderPoint, calcPointInsideSection());
    }

    public static String calcCrossedLinesIntersection(Point mainLineStartPoint, Point mainLineEndPoint,
                                              Point crossedLineStartPoint, Point crossedLineEndPoint){

        Double mainLineAzimuth = calcAzimuth(mainLineStartPoint, mainLineEndPoint);
        Double crossedLineAzimuth = calcAzimuth(crossedLineStartPoint, crossedLineEndPoint);
        Double mainToCrossedAzimuth = calcAzimuth(mainLineStartPoint, crossedLineStartPoint);
        Double crossedToMainAzimuth = calcAzimuth(crossedLineStartPoint, mainLineStartPoint);
        if( mainLineAzimuth.isNaN() || crossedLineAzimuth.isNaN() ||
                mainToCrossedAzimuth.isNaN() || crossedToMainAzimuth.isNaN() ){
            return "-";
        }
        double alfa = Math.abs(mainLineAzimuth - mainToCrossedAzimuth);
        if( alfa > Math.PI ){
            alfa = 2 * Math.PI - alfa;
        }
        double beta = Math.abs(crossedLineAzimuth - crossedToMainAzimuth);
        if( beta > Math.PI ){
            beta = 2 * Math.PI - beta;
        }
        if( (alfa + beta) >= Math.PI ){
            return "-";
        }
        double distanceOnMainLine = Math.sin(beta) * calcDistance(mainLineStartPoint, crossedLineStartPoint) / Math.sin(alfa + beta);
        double distanceOnCrossedLine = Math.sin(alfa) * calcDistance(mainLineStartPoint, crossedLineStartPoint) / Math.sin(alfa + beta);
        Point crossingPoint1 = new Point("crossing1",
                mainLineStartPoint.getY_value() + distanceOnMainLine * Math.sin(mainLineAzimuth),
                mainLineStartPoint.getX_value() + distanceOnMainLine * Math.cos(mainLineAzimuth));
        Point crossingPoint2 = new Point("crossing2",
                crossedLineStartPoint.getY_value() + distanceOnCrossedLine * Math.sin(crossedLineAzimuth),
                crossedLineStartPoint.getX_value() + distanceOnCrossedLine * Math.cos(crossedLineAzimuth));
        return String.format(Locale.getDefault(),"%.3f",
                (crossingPoint1.getY_value() + crossingPoint2.getY_value()) / 2) + " " +
                String.format(Locale.getDefault(),"%.3f",
                        (crossingPoint1.getX_value() + crossingPoint2.getX_value()) / 2);
    }

}
