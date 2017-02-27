package com.example.robert.traceit.shape;

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 2/8/2017.
 */

public class LineSegment {

    private PointF startPoint, endPoint;
    private float slope;
    private float yIntercept;
    private double length;
    private DisplayMetrics dm;

    public LineSegment(PointF start, PointF end, DisplayMetrics displayMetrics) {
        this.startPoint = start;
        this.endPoint = end;
        this.slope = calcSlope();
        this.length = calcLength();
        this.yIntercept = calcYIntercept();
        this.dm = displayMetrics;
    }

    private float calcSlope() {
        float dy = this.endPoint.y - this.startPoint.y;
        float dx = this.endPoint.x - this.startPoint.x;
        return dx == 0 ? Integer.MAX_VALUE : dy / dx;
    }

    private double calcLength() {
        return getDistance(this.endPoint, this.startPoint);
    }

    private float calcYIntercept() {
        return startPoint.y - slope * startPoint.x;
    }

    public static double getDistance(PointF p1, PointF p2) {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2)
                + Math.pow((p2.y - p1.y), 2));
    }

    private static double cosLaw(double a, double b, double c){
        double cos0 = 0;

        cos0 = (a*a + b*b - c*c)/(2*a*b);

        return cos0;
    }

    public double getNormalDistance(PointF point) {
        if(isWithinSegment(point, startPoint, endPoint)) {
            return Math.abs((point.y - slope * point.x - yIntercept) / Math.sqrt(slope * slope + 1));
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static boolean isWithinSegment(PointF point, PointF start, PointF end){
        boolean isWithin;

        double pToStart = getDistance(point, start);
        double pToEnd = getDistance(point, end);
        double length = getDistance(start, end);

        double cosAngleA = cosLaw(pToStart, length, pToEnd);
        double cosAngleB = cosLaw(pToEnd, length, pToStart);

        isWithin = (cosAngleA > 0 && cosAngleB > 0) ? true : false;

        return isWithin;
    }

    public List<DetectableZone> toDetectableZones(float density) {
        int numPoints = (int) Math.floor(dpConverter((float)this.length) * density);
        List<DetectableZone> zoneList = new ArrayList<>();

        if(numPoints == 2) {
            zoneList.add(new DetectableZone(new PointF(this.startPoint.x, this.startPoint.y), new PointF(this.endPoint.x, this.endPoint.y)));
        } else if (numPoints > 2) {
            PointF prevPoint = new PointF(this.startPoint.x, this.startPoint.y);
            double intervalRatio = 1.0 / numPoints;
            for (int i = 1; i < numPoints; i++) {
                double ratio = i * intervalRatio;
                float x = (float) ((1 - ratio) * this.startPoint.x + ratio * this.endPoint.x);
                float y = (float) ((1 - ratio) * this.startPoint.y + ratio * this.endPoint.y);
                PointF newPoint = new PointF(x, y);

                zoneList.add(new DetectableZone(prevPoint, newPoint));
                prevPoint = newPoint;
            }
        }

        return zoneList;
    }

    private float dpConverter(float dpSize)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
    }
}
