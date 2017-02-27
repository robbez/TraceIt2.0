package com.example.robert.traceit.shape;

import android.graphics.PointF;

import java.util.Random;

/**
 * Created by Robert on 2/18/2017.
 */

public class EllipticallPointGenerator {

    private PointF mCenter;
    private float mHorizontalRadius, mVerticalRadius;
    private int mDensity;
    private PointF[] listOfDistributedPoints;

    private Random mRandomGenerator;


    public EllipticallPointGenerator() {
        this.mCenter = new PointF(1, 1);
        this.mHorizontalRadius = 1;
        this.mVerticalRadius = 1;
        this.mDensity = 1;
        init();
    }

    public  EllipticallPointGenerator(PointF center, float xRadius, float yRadius, int density) {
        this.mCenter = center;
        this.mHorizontalRadius = xRadius;
        this.mVerticalRadius = yRadius;
        this.mDensity = density;
        init();
    }

    private void init() {
        this.mRandomGenerator = new Random();
        this.listOfDistributedPoints = new PointF[this.mDensity];
        for (int i = 0; i < listOfDistributedPoints.length; i++) {
            listOfDistributedPoints[i] = getPointOnEllipse((i * 2 * Math.PI) / mDensity);
        }
    }

    private PointF getPointOnEllipse(double radians) {
        int x = Math.round((float)(mCenter.x + mHorizontalRadius * Math.cos(radians)));
        int y = Math.round((float)(mCenter.y + mVerticalRadius * Math.sin(radians)));
        return new PointF(x, y);
    }

    public PointF[] generateRandomPoints(int numberOfPoints, float percentThreshold) throws Exception {
        if(numberOfPoints < 1 && numberOfPoints > mDensity) {
            throw new Exception("invalid number of points: Max allowed is " + mDensity);
        }

        float perZoneMaxRatio = (float)1 / numberOfPoints;
        float perZoneMinRatio = percentThreshold * perZoneMaxRatio;
        PointF[] pointsList = new PointF[numberOfPoints];

        // first point
        int previousPointIndex = mRandomGenerator.nextInt(mDensity);
        pointsList[0] = listOfDistributedPoints[previousPointIndex];

        for (int i = 1; i < numberOfPoints; i++) {
            int[] nextPointIndex =  nextPointIndex(previousPointIndex, mDensity, perZoneMaxRatio, perZoneMinRatio);
            pointsList[i] = listOfDistributedPoints[nextPointIndex[0]];
            previousPointIndex = nextPointIndex[1];
        }

        return  pointsList;
    }

    private int[] nextPointIndex(int previousPointIndex, int maxNumberOfPoints, float zoneMaxRatio, float zoneMinRatio) {

        int low = (previousPointIndex + (int)Math.ceil(maxNumberOfPoints * zoneMinRatio));
        int high = (previousPointIndex + (int)Math.ceil(maxNumberOfPoints * zoneMaxRatio));
        int newIndex = (mRandomGenerator.nextInt(high - low) + low)% maxNumberOfPoints;

        return  new int[]{newIndex, high % maxNumberOfPoints};
    }


}
