package com.example.robert.traceit.shape;

import android.graphics.PointF;

import java.util.List;

/**
 * Created by Robert on 2/26/2017.
 */

public class ShapeSide {
    public LineSegment getLine() {
        return line;
    }

    public int getNumberOfCompletedZones() {
        return numberOfCompletedZones;
    }

    public float getDensity() {
        return density;
    }

    public double getNormalDistance(PointF point) {
        return this.line.getNormalDistance(point);
    }

    public void testZones(PointF point) {
        for(DetectableZone dz : this.detectionZones) {
            if(dz.checkState(point)) {
                this.numberOfCompletedZones++;
                break;
            }
        }
    }

    public double getCompletedRatio() {
        return 1.0 * this.numberOfCompletedZones / this.detectionZones.size();
    }

    public void resetDetection() {
        this.numberOfCompletedZones = 0;
        for(int i = 0; i < this.detectionZones.size(); i++) {
            detectionZones.get(i).setState(false);
        }
    }

    private LineSegment line;
    private List<DetectableZone> detectionZones;
    private int numberOfCompletedZones;
    private float density;

    public ShapeSide(LineSegment segment, float density) {
        this.line = segment;
        this.numberOfCompletedZones = 0;
        this.density = density;
        this.detectionZones = segment.toDetectableZones(density);
    }
}
