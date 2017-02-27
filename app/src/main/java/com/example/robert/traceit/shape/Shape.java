package com.example.robert.traceit.shape;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert on 2/20/2017.
 */

public class Shape {

    private PointF[] mPointList;
    private ShapeSide[] sides;
    private Path shapePath;
    private PathMeasure pathMeasure;
    private float density;
    private DisplayMetrics dm;
    private boolean[] vertexState;
    private double passThreshold;

    public Shape(PointF[] sortedPointList, float density, double passThreshold, DisplayMetrics displayMetrics) {
        this.mPointList = sortedPointList;
        this.density = density;
        this.passThreshold = passThreshold;
        this.dm = displayMetrics;
        if(sortedPointList != null && sortedPointList.length > 0) {
            init();
        }
    }

    private void init() {

        this.sides = new ShapeSide[mPointList.length];
        this.shapePath = new Path();
        this.pathMeasure = new PathMeasure();
        this.vertexState = new boolean[mPointList.length];
        Arrays.fill(this.vertexState, false);

        this.shapePath.moveTo(mPointList[0].x, mPointList[0].y);
        PointF prevPoint = mPointList[0];

        for(int i = 1; i < mPointList.length; i++) {
            this.shapePath.lineTo(mPointList[i].x, mPointList[i].y);
            addLineSegmentToShape((i - 1), prevPoint, mPointList[i]);
            prevPoint = mPointList[i];
        }

        this.shapePath.lineTo(mPointList[0].x, mPointList[0].y);
        addLineSegmentToShape((mPointList.length - 1), prevPoint, mPointList[0]);
    }

    private void addLineSegmentToShape(int index, PointF start, PointF end) {
        LineSegment segment = new LineSegment(start, end, this.dm);
        this.sides[index] = new ShapeSide(segment, this.density);
    }

    public PointF[] getPointList() {
        return mPointList;
    }

    public ShapeSide[] getSides() {
        return sides;
    }

    public Path getPath() {
        return shapePath;
    }

    public void checkDetection(PointF point, double threshold, double traceLength) {
        double shortestDistance = Integer.MAX_VALUE;
        int indexOfMin = 0;

        withinVertex(point, threshold, traceLength);

        for (int i = 0; i < sides.length; i++) {
            double temp = sides[i].getNormalDistance(point);
            if(temp < shortestDistance) {
                shortestDistance = temp;
                indexOfMin = i;
            }
        }

        if(shortestDistance < threshold) {
            sides[indexOfMin].testZones(point);
        }
    }

    private int getClosestVertex(PointF point)
    {
        PointF vertex;
        int index = -1;
        double minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < this.mPointList.length; i++)
        {
            vertex = this.mPointList[i];
            double tempDist = LineSegment.getDistance(point, vertex);
            if (tempDist < minDistance)
            {
                minDistance = tempDist;
                index = i;
            }
        }

        return index;
    }

    public boolean withinVertex(PointF point, double radius, double traceLength)
    {
        int index = getClosestVertex(point);

        if (index < 0) return false;

        if (LineSegment.getDistance(point, this.mPointList[index]) <= radius && traceLength > 2 * radius)
        {
            this.vertexState[index] = true;
            return true;
        }

        return false;
    }

    public double getMatchRating() {
        double rating = 0.0;

        for(int i = 0; i < this.sides.length; i++) {
            double sidesCompletionRating = sides[i].getCompletedRatio();
            if(sidesCompletionRating > this.passThreshold) {
                rating += sidesCompletionRating;
            } else {
                return 0;
            }
        }

        return  rating / sides.length;
    }

    public boolean passedAllCheckpoints()
    {
        for (int i = 0; i < this.vertexState.length; i++)
        {
            if (this.vertexState[i] == false) return false;
        }

        return true;
    }

    public void resetDetection() {
        for(int i = 0; i < this.sides.length; i++) {
            sides[i].resetDetection();
        }
        Arrays.fill(this.vertexState, false);
    }

    public float getPathLength(){
        this.pathMeasure.setPath(this.shapePath, false);
        return this.pathMeasure.getLength();
    }

}
