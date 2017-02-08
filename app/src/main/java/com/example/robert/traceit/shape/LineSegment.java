package com.example.robert.traceit.shape;

import android.graphics.PointF;

/**
 * Created by Robert on 2/8/2017.
 */

public class LineSegment {

    private PointF startPoint, endPoint;
    private float slope;
    private float yIntercept;
    private double length;

    public LineSegment() {
        this.startPoint = new PointF(0f, 0f);
        this.endPoint = new PointF(0f, 0f);
        this.slope = 0f;
        this.length = 0;
        this.yIntercept = 0;
    }

    public LineSegment(PointF start, PointF end) {
        this.startPoint = start;
        this.endPoint = end;
        this.slope = calcSlope();
        this.length = calcLength();
        this.yIntercept = calcYIntercept();
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

    private double getDistance(PointF p1, PointF p2) {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2)
                + Math.pow((p2.y - p1.y), 2));
    }
}
