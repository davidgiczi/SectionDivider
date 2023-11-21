package com.example.myfirstapplication;

import java.util.ArrayList;
import java.util.List;

public class Calculator {

    private final Point startPoint;
    private final Point endPoint;
    private Point outsiderPoint;
    private int numberOfDividerPoints;
    private String lengthOfSection;
    private String distanceBetweenPoints;
    private List<Point> resultPoints;

    public Calculator(Point startPoint, Point endPoint, int numberOfDividerPoints) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.numberOfDividerPoints = numberOfDividerPoints;
        this.resultPoints = new ArrayList<>();
        lengthOfSection = String.format("%.3fm", calcDistance(startPoint, endPoint))
                .replace(',', '.');
        distanceBetweenPoints =
                String.format("%.3fm", calcDistance(startPoint, endPoint) / (numberOfDividerPoints + 1))
                .replace(',', '.');
        calcResultPoints();
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public String getLengthOfSection() {
        return lengthOfSection;
    }

    public String getDistanceBetweenPoints() {
        return distanceBetweenPoints;
    }

    public List<Point> getResultPoints() {
        return resultPoints;
    }

    public Point getOutsiderPoint() {
        return outsiderPoint;
    }
    public void setOutsiderPoint(Point outsiderPoint) {
        this.outsiderPoint = outsiderPoint;
    }

    private double calcDistance(Point startPoint, Point endPoint){
        return Math.sqrt(Math.pow(startPoint.getY_value() - endPoint.getY_value(), 2) +
                         Math.pow(startPoint.getX_value() - endPoint.getX_value(), 2));
    }
    private Double calcAzimuth(Point startPoint, Point endPoint){

        double deltaX = endPoint.getY_value() - startPoint.getY_value();
        double deltaY = endPoint.getX_value() - startPoint.getX_value();

        if( deltaX >= 0 && deltaY > 0 ) {
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
        else if( deltaX > 0 && deltaY == 0 ) {
            return Math.PI / 2;
        }
        else if( 0 > deltaX && deltaY == 0 ) {
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
        };
        return resultAsString;
    }

    public Point calcPointInsideSection(){
        Point insidePoint = new Point(String.valueOf(resultPoints.size() + 1), 0.0, 0.0);
        if( outsiderPoint == null || calcAzimuth(startPoint, endPoint).isNaN()){
            return insidePoint;
        }
        double alfa= calcAzimuth(startPoint, endPoint) >
                calcAzimuth(startPoint, outsiderPoint) ?
                calcAzimuth(startPoint, endPoint) -
                        calcAzimuth(startPoint, outsiderPoint) :
                calcAzimuth(startPoint, outsiderPoint) -
                calcAzimuth(startPoint, endPoint);
        double distance = calcDistance(startPoint, outsiderPoint) * Math.cos(alfa);
        insidePoint  = new Point(String.valueOf(resultPoints.size() + 1),
                startPoint.getY_value() + distance * Math.sin(calcAzimuth(startPoint, endPoint)),
                startPoint.getX_value() + distance * Math.cos(calcAzimuth(startPoint, endPoint)));
        return insidePoint;
    }

}
