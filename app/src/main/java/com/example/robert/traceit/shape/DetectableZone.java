package com.example.robert.traceit.shape;

import android.graphics.PointF;

/**
 * Created by Robert on 2/20/2017.
 */

public class DetectableZone {
    private PointF start, end;
    private boolean state;

    public PointF getStart() {
        return start;
    }

    public void setStart(PointF start) {
        this.start = start;
    }

    public PointF getEnd() {
        return end;
    }

    public void setEnd(PointF end) {
        this.end = end;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public DetectableZone(PointF start, PointF end) {

        this.start = start;
        this.end = end;
        this.state = false;
    }

    public boolean checkState(PointF point) {
        if(LineSegment.isWithinSegment(point, this.start, this.end)) {
            boolean prevState = this.state;
            this.state = true;
            if(prevState == false) {
                return true;
            }
        }

        return false;
    }
}
